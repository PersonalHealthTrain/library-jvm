package de.difuture.ekut.pht.lib.train.impl.docker

import de.difuture.ekut.pht.lib.data.toTrainName
import de.difuture.ekut.pht.lib.data.toTrainTag
import de.difuture.ekut.pht.lib.train.api.TrainArrival
import jdregistry.client.data.RepositoryName as DockerRepositoryName
import jdregistry.client.data.Tag as DockerTag

/**
 * A [TrainArrival] as they are available from Docker Registries.
 *
 * For a Docker Registry, a Train Arrival is identified by the [DockerRepositoryName]
 * and the associated [DockerTag].
 *
 * @author Lukas Zimmermann
 * @since 0.0.3
 *
 */
data class DockerRegistryTrainArrival(
    val host: String,
    val repoName: DockerRepositoryName,
    val tag: DockerTag
) : TrainArrival() {

    override val trainTag = tag.toTrainTag()

    // If the the second component exists, it is the trainName and the first
    // is the namespace. Else, the first component is the namespace
    override val trainName =
        (if (repoName.list.size > 1) repoName.list[1].repr else repoName.list[0].repr).toTrainName()
}
