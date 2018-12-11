package de.difuture.ekut.pht.lib.runtime.api.docker

import com.spotify.docker.client.DockerClient
import com.spotify.docker.client.exceptions.ContainerNotFoundException
import com.spotify.docker.client.exceptions.DockerException
import com.spotify.docker.client.exceptions.ImageNotFoundException
import com.spotify.docker.client.messages.RegistryAuth
import de.difuture.ekut.pht.lib.runtime.api.docker.data.DockerContainerCreation
import de.difuture.ekut.pht.lib.runtime.api.docker.data.DockerRunOptionalParameters
import dockerdaemon.data.DockerContainerId
import dockerdaemon.data.DockerContainerOutput
import dockerdaemon.data.DockerImageId
import java.io.InputStream
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
abstract class AbstractDockerRuntimeClient : DockerRuntimeClient {

    private var closed = false
    private var auth: RegistryAuth? = null

    fun commitByRebase(
        containerId: DockerContainerId,
        exportFiles: List<Path>,
        fromRepo: DockerRepositoryName,
        fromTag: DockerTag,
        targetRepo: DockerRepositoryName,
        targetTag: DockerTag
    ): DockerImageId = unlessClosed {

        // Guard: check that all paths in the input are absolute
        exportFiles.firstOrNull { ! it.isAbsolute }?.run {
            throw IllegalArgumentException("Export Path not absolute: $this")
        }

        // 1. First, create a new container from the baseImage in which the files should be copied into
        val targetContainerId = createContainer(repoTagToImageId(fromRepo.resolve(fromTag))).containerId

        // 2. Copy all the files from the source container into the target container
        for (path in exportFiles) {

            copyFileFromContainer(containerId, path).use { inputStream ->
                copyFileToContainer(inputStream, targetContainerId, path)
            }
        }

        // 3. Create the new image from the container
        commitContainer(
                containerId,
                targetRepo,
                targetTag)

        // 4 Remove the created container
        stopAndRemoveContainer(targetContainerId)
        stopAndRemoveContainer(containerId)
        this.repoTagToImageId(targetRepo.resolve(targetTag))
    }

//    override fun images() = unlessClosed {
//            baseClient.listImages().map { DockerImageId(it.id()) }
//    }

//    override fun pull(repo: DockerRepositoryName, tag: DockerTag, host: String?): DockerImageId = unlessClosed {
//
//        // The Spotify Docker Client only understands the ':' syntax for images and tags
//        val repoTag = repo.resolve(tag, host)
//        val auth = this.auth
//        try {
//            with(baseClient) {
//                if (auth != null) {
//                    pull(repoTag, auth)
//                } else {
//                    pull(repoTag)
//                }
//            }
//            this.repoTagToImageId(repoTag)
//        } catch (ex: ImageNotFoundException) {
//            throw NoSuchDockerImageException(ex, repo = repoTag)
//        }
//    }
//
//    override fun push(repo: DockerRepositoryName, tag: DockerTag, host: String?) = unlessClosed {
//
//        val repoTag = repo.resolve(tag, host)
//        val auth = this.auth
//        try {
//            with(baseClient) {
//                if (auth != null) {
//                    push(repoTag, auth)
//                } else {
//                    push(repoTag)
//                }
//            }
//        } catch (ex: ImageNotFoundException) {
//            throw NoSuchDockerImageException(ex, repoTag)
//        }
//    }

//    override fun login(username: String, password: String, host: String?): Boolean = unlessClosed {
//
//        val builder = RegistryAuth.builder().username(username).password(password)
//        val auth = (host?.let { builder.serverAddress(it) } ?: builder).build()
//
//        return if (baseClient.auth(auth) == 200) {
//            this.auth = auth
//            true
//        } else false
//    }

    override fun run(
        imageId: DockerImageId,
        commands: List<String>,
        rm: Boolean,
        optionalParams: DockerRunOptionalParameters?
    ): DockerContainerOutput = unlessClosed {

        try {
            // We rethrow ImageNotFound and DockerClient exceptions, but let Interrupted Exceptions pass throw
            // TODO Platform type. Can this be null? This would not be documented by the method
            // TODO Network
            val containerId = createContainer(imageId, commands, optionalParams?.env).containerId

            // Now start the container
            startContainer(containerId)

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

//    override fun tag(
//        imageId: DockerImageId,
//        targetRepo: DockerRepositoryName,
//        targetTag: DockerTag,
//        host: String?
//    ) = unlessClosed {
//
//            this.baseClient.tag(imageId.repr, targetRepo.resolve(targetTag, host))
//    }

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

    // Abstract members // TODO Which belong to the runtime API

    abstract fun createContainer(
        imageId: DockerImageId,
        commands: List<String>? = null,
        env: Map<String, String>? = null,
        network: DockerNetworkReference? = null): DockerContainerCreation

    abstract fun startContainer(containerId: DockerContainerId)

    abstract fun copyFileFromContainer(containerId: DockerContainerId, path: Path): InputStream

    abstract fun copyFileToContainer(input: InputStream, containerId: DockerContainerId, path: Path)

    abstract fun commitContainer(containerId: DockerContainerId, targetRepo: DockerRepositoryName, targetTag: DockerTag)

    abstract fun stopAndRemoveContainer(containerId: DockerContainerId)

    /**
     * Lists the [DockerImageId] that this [DockerRuntimeClient] has access to.
     *
     * Resembles the `data images -q` command.
     *
     * *Contract:* The method should fail by throwing an exception if something prevents listing the available
     * images.
     *
     * @return The list of [DockerImageId] that this [DockerRuntimeClient] has access to.
     *
     */
    abstract fun listImages(): List<DockerImageId>

    abstract fun pullImage(repo: DockerRepositoryName, tag: DockerTag, host: String? = null): DockerImageId

    abstract fun pushImage(repo: DockerRepositoryName, tag: DockerTag, host: String? = null)

    abstract fun connectToNetwork(containerId: DockerContainerId, network: DockerNetworkReference)

    abstract fun waitForContainer(containerId: DockerContainerId)

    /**
     * Logs the Docker Client in to the remote host using the provided `username` and `password`.
     *
     * @param username The username that is used for login
     * @param password Password for login.
     * @param host The host for login. If omitted, the implementor is expected to fall back to Docker Hub.
     * @return Whether login has succeeded.
     *
     */
    abstract fun login(username: String, password: String, host: String? = null): Boolean
}
