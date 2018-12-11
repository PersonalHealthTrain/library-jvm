package de.difuture.ekut.pht.lib.runtime.impl

import com.spotify.docker.client.DefaultDockerClient
import com.spotify.docker.client.DockerClient
import com.spotify.docker.client.exceptions.ContainerNotFoundException
import com.spotify.docker.client.exceptions.DockerException
import com.spotify.docker.client.exceptions.ImageNotFoundException
import com.spotify.docker.client.messages.ContainerConfig
import com.spotify.docker.client.messages.RegistryAuth
import de.difuture.ekut.pht.lib.data.DockerContainerId
import de.difuture.ekut.pht.lib.data.DockerContainerOutput
import de.difuture.ekut.pht.lib.data.DockerImageId
import de.difuture.ekut.pht.lib.runtime.api.docker.CreateDockerContainerFailedException
import de.difuture.ekut.pht.lib.runtime.api.docker.DockerRuntimeClient
import de.difuture.ekut.pht.lib.runtime.api.docker.DockerRuntimeClientException
import de.difuture.ekut.pht.lib.runtime.api.docker.NoSuchDockerImageException
import de.difuture.ekut.pht.lib.runtime.api.docker.params.DockerCommitOptionalParameters
import de.difuture.ekut.pht.lib.runtime.api.docker.params.DockerRunOptionalParameters
import kotlinext.map.asKeyValueList
import java.lang.IllegalArgumentException
import jdregistry.client.data.RepositoryName as DockerRepositoryName
import jdregistry.client.data.Tag as DockerTag
import java.lang.IllegalStateException
import java.nio.file.Path

/**
 * Spotify-client-based implementation of the [DockerRuntimeClient] interface.
 *
 * This implementation encapsulates completely the base Docker client related properties.
 * For instance, all Spotify related exceptions are converted to exceptions from the library
 *
 * @author Lukas Zimmermann
   @see DockerRuntimeClient
 * @since 0.0.1
 *
 */
class SpotifyDockerClient : DockerRuntimeClient {

    private val baseClient = DefaultDockerClient.fromEnv().build()
    private var closed = false
    private var auth: RegistryAuth? = null

    override fun commitByRebase(
        containerId: DockerContainerId,
        exportFiles: List<Path>,
        from: String,
        targetRepo: DockerRepositoryName,
        targetTag: DockerTag,
        optionalParams: DockerCommitOptionalParameters?
    ): DockerImageId = unlessClosed {

        // Guard: check that all paths in the input are absolute
        exportFiles.firstOrNull { ! it.isAbsolute }?.run {
            throw IllegalArgumentException("Export Path not absolute: $this")
        }

        // 1. First, create a new container from the baseImage in which the files should be copied into
        val targetContainerId = ContainerConfig.builder().image(from).build().let { config ->
            baseClient.createContainer(config).id()
                    ?: throw DockerRuntimeClientException("Creation of Docker Container failed!")
        }

        // 2. Copy all the files from the source container into the target container
        for (path in exportFiles) {

            val file = path.toFile()

            baseClient.archiveContainer(containerId.repr, file.absolutePath).use { inputStream ->
                baseClient.copyToContainer(inputStream, targetContainerId, file.parentFile.absolutePath)
            }
        }

        // 3. Create the new image from the container
        baseClient.commitContainer(
                containerId.repr,
                targetRepo.repr,
                targetTag.repr,
                ContainerConfig.builder().build(),
                optionalParams?.comment,
                optionalParams?.author)

        // 4 Remove the created container
        baseClient.stopContainer(targetContainerId, 20)
        baseClient.removeContainer(targetContainerId)

        // 5 Also remove the source Container
        baseClient.removeContainer(containerId.repr)

        this.repoTagToImageId(targetRepo.resolve(targetTag))
    }

    override fun images() = unlessClosed {
            baseClient.listImages().map { DockerImageId(it.id()) }
    }

    override fun pull(repo: DockerRepositoryName, tag: DockerTag, host: String?): DockerImageId = unlessClosed {

        // The Spotify Docker Client only understands the ':' syntax for images and tags
        val repoTag = repo.resolve(tag, host)
        val auth = this.auth
        try {
            with(baseClient) {
                if (auth != null) {
                    pull(repoTag, auth)
                } else {
                    pull(repoTag)
                }
            }
            this.repoTagToImageId(repoTag)
        } catch (ex: ImageNotFoundException) {
            throw NoSuchDockerImageException(ex, repo = repoTag)
        }
    }

    override fun push(repo: DockerRepositoryName, tag: DockerTag, host: String?) = unlessClosed {

        val repoTag = repo.resolve(tag, host)
        val auth = this.auth
        try {
            with(baseClient) {
                if (auth != null) {
                    push(repoTag, auth)
                } else {
                    push(repoTag)
                }
            }
        } catch (ex: ImageNotFoundException) {
            throw NoSuchDockerImageException(ex, repoTag)
        }
    }

    override fun login(username: String, password: String, host: String?): Boolean = unlessClosed {

        val builder = RegistryAuth.builder().username(username).password(password)
        val auth = (host?.let { builder.serverAddress(it) } ?: builder).build()

        return if (baseClient.auth(auth) == 200) {
            this.auth = auth
            true
        } else false
    }

    override fun run(
        imageId: DockerImageId,
        commands: List<String>,
        rm: Boolean,
        optionalParams: DockerRunOptionalParameters?
    ): DockerContainerOutput = unlessClosed {

        //  Map the environment map to the list format required by the Spotify Docker Client
        val envList = optionalParams?.env?.asKeyValueList().orEmpty()

        // Configuration for the Container Creation, currently only takes the Image Id
        val config = ContainerConfig.builder()
                .image(imageId.repr)
                .cmd(commands)
                .env(envList)
                .build()

        try {
            val warnings = mutableListOf<String>()
            // We rethrow ImageNotFound and DockerClient exceptions, but let Interrupted Exceptions pass throw
            // TODO Platform type. Can this be null? This would not be documented by the method
            val creation = baseClient.createContainer(config)

            // Collect warnings to warnings list if present and if we are interested in warnings
            creation.warnings()?.let { warnings.addAll(it) }

            // Fetch the Container ID
            val containerId = creation.id()
                    ?: throw CreateDockerContainerFailedException("Spotify Docker Client did not make the Docker Container ID available!")

            // We also need to container ID as proper object
            val containerIdObj = DockerContainerId(containerId)

            // Attach the container to a network, if this is requested
            val network = optionalParams?.network
            if (network != null) {

                baseClient.connectToNetwork(containerId, network)
            }

            // Now start the container
            baseClient.startContainer(containerId)

            // The Interrupt needs to be handled after the container has been started
            val interruptSignaler = optionalParams?.interruptSignaler
            val interruptHandler = optionalParams?.interruptHandler

            if (interruptSignaler != null && interruptHandler != null) {

                // Find the container that we have just started (we cannot use the baseClient waitContainer method,
                // as this does not allow the handling of timeouts
                // I use collection operations here, because I do not understand how the parameter of listContainers works
                val container = baseClient.listContainers().first { it.id() == containerId }

                while (container.status() != "exited") {

                    if (interruptSignaler.wasInterrupted(containerIdObj)) {

                        interruptHandler.handleInterrupt(containerIdObj)
                    }
                }
            }
            // Now fetch the container exit
            val exit = baseClient.waitContainer(containerId)

            // Stdout and Stderr need to be read before the container is gonna be removed
            val stdout = baseClient.logs(containerId, DockerClient.LogsParam.stdout()).readFully()
            val stderr = baseClient.logs(containerId, DockerClient.LogsParam.stderr()).readFully()

            // Remove the container if this was requested
            if (rm) {

                baseClient.removeContainer(containerId)
            }

            DockerContainerOutput(
                    containerIdObj,
                    exit.statusCode().toInt(),
                    stdout,
                    stderr,
                    warnings)
            // Rethrow as NoSuchImageException
        } catch (ex: ImageNotFoundException) {
            throw NoSuchDockerImageException(ex, imageId)
        } catch (ex: ContainerNotFoundException) {
            throw DockerRuntimeClientException(ex)
        }
        // TODO Cleanup
    }

    override fun tag(
        imageId: DockerImageId,
        targetRepo: DockerRepositoryName,
        targetTag: DockerTag,
        host: String?
    ) = unlessClosed {

            this.baseClient.tag(imageId.repr, targetRepo.resolve(targetTag, host))
    }

    /**
     * Translates the repoTag string to the corresponding unique Image ID
     */
    private fun repoTagToImageId(repoTag: String): DockerImageId {
        val images = baseClient.listImages().filter {

            val repoTags = it.repoTags()
            repoTags != null && repoTag in repoTags
        }
        return images.singleOrNull()?.let { DockerImageId(it.id()) }
                ?: throw IllegalStateException("Implementation Error! Zero or more than one image found for $repoTag")
    }

    override fun close() {
        closed = true
        baseClient.close()
    }

    private inline fun <T> unlessClosed(body: () -> T): T {
        if (closed) {
            throw IllegalStateException("This DockerRuntimeClient has been closed!")
        }
        try {
            return body()
        } catch (ex: DockerException) {
            throw DockerRuntimeClientException(ex)
        }
    }
}
