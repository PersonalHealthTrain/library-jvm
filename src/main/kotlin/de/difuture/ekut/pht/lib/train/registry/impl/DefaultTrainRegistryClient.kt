package de.difuture.ekut.pht.lib.train.registry.impl

import de.difuture.ekut.pht.lib.data.TrainName
import de.difuture.ekut.pht.lib.data.TrainTag
import de.difuture.ekut.pht.lib.data.isValidTrainName
import de.difuture.ekut.pht.lib.runtime.api.docker.DockerRuntimeClient
import de.difuture.ekut.pht.lib.train.api.TrainArrival
import de.difuture.ekut.pht.lib.train.impl.docker.DockerRegistryTrainArrival
import de.difuture.ekut.pht.lib.train.impl.docker.DockerRegistryTrainDeparture
import de.difuture.ekut.pht.lib.train.registry.api.TrainRegistryClient
import jdregistry.client.api.DockerRegistryGetClient
import jdregistry.client.data.RepositoryName as DockerRepositoryName
import jdregistry.client.data.Tag as DockerTag

/**
 * Canonical implementation of the [TrainRegistryClient].
 *
 * This [TrainRegistryClient] is implemented using an underlying [DockerRegistryGetClient] as
 * engine. Furthermore, the namespace of the remote registry needs to be specified, since
 * an instance of the [DefaultTrainRegistryClient] can only target one namespace at a time.
 *
 * @param dockerRegistryClient The [DockerRegistryGetClient] that should be used for interacting with the train registry
 * @param namespace The namespace of the Docker Registry that is targeted by this client. If the repoName
 * space is null, then the root namespace of the registry is targeted
 *
 * @author Lukas Zimmermann
 * @since 0.1
 *
 */
class DefaultTrainRegistryClient(
    private val dockerRegistryClient: DockerRegistryGetClient,
    private val namespace: String? = null
) : TrainRegistryClient<DockerRegistryTrainArrival, DockerRegistryTrainDeparture, DockerRuntimeClient> {

    override fun listTrainArrivals(predicate: (TrainArrival) -> Boolean) = with(dockerRegistryClient) {

        // We have to inspect all repositories in the remote registry
        listRepositories().repositories.orEmpty()

                // First, filter down to all repos that have the correct namespace and designate valid Train IDs
                .filter { repo ->
                    when (repo.list.size) {

                        1 -> namespace == null && repo.list[0].repr.isValidTrainName()
                        2 -> repo.list[0].repr == namespace && repo.list[1].repr.isValidTrainName()
                        else -> false
                    }
                }
                // Map all hits to the Train Arrivals
                .flatMap { repo ->

                    listTags(repo).tags.orEmpty().map { tag ->

                        DockerRegistryTrainArrival(uri.host, repo, tag)
                    }
                }
                // At last, apply the filter from the predicate
                .filter(predicate)
    }

    override fun getTrainArrival(trainId: TrainName, trainTag: TrainTag) =

            this.listTrainArrivals { it.trainName == trainId && it.trainTag == trainTag }.singleOrNull()

    override fun submitTrainDeparture(departure: DockerRegistryTrainDeparture): Boolean {

        // The DockerRepositoryName is derived from the trainId of the Train Departure and the namespace
        // of this DockerClient
        val repoName = departure.trainName.repr
        val repo = namespace?.let { DockerRepositoryName.from("$it/$repoName") }
                ?: DockerRepositoryName.from(repoName)

        // The DockerTag will be identical to the trainTag
        val dockerTag = DockerTag.from(departure.trainTag.repr)

        // The host is simply derived from the uri of this registry
        val host = dockerRegistryClient.uri.host
        val port = dockerRegistryClient.uri.port
        val portString = if (port == -1) "" else ":$port"
        val hostString = "$host$portString"

        // The image needs to be tagged with the new image Name
        departure.client.tag(departure.imageId, repo, dockerTag, hostString)
        departure.client.push(repo, dockerTag, hostString)
        return true
    }
}
