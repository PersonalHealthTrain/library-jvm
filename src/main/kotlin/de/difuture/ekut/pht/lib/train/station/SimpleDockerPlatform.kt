package de.difuture.ekut.pht.lib.train.station

import de.difuture.ekut.pht.lib.runtime.api.docker.DockerRuntimeClient
import de.difuture.ekut.pht.lib.train.api.DockerRebaseStrategy
import de.difuture.ekut.pht.lib.train.api.RunResponse
import de.difuture.ekut.pht.lib.train.api.StationRuntimeInfo
import de.difuture.ekut.pht.lib.train.impl.docker.DockerRegistryTrainArrival
import de.difuture.ekut.pht.lib.train.impl.docker.DockerRegistryTrainDeparture
import de.difuture.ekut.pht.lib.train.impl.docker.RunExecutor
import jdregistry.client.data.Tag as DockerTag
import java.nio.file.Paths

class SimpleDockerPlatform(

    private val client: DockerRuntimeClient,
    private val stationInfo: StationRuntimeInfo
) {

    fun departWithAlgorithm(arrival: DockerRegistryTrainArrival): DockerRegistryTrainDeparture {

        val output = RunExecutor.execArrival(arrival, this.client, this.stationInfo)
        val response = output.response

        // The Output has an error if there either is an error or the response is null
        if (output.error != null || response == null) {

            throw TrainException.Output(output)
        }

        if (response.state != RunResponse.AlgorithmExitState.SUCCESS) {

            throw TrainException.RunAlgorithmFailed(output)
        }
        val rebase = response.rebase

        // SimpleDockerPlatform only supports the DockerRebaseStrategy
        if (rebase is DockerRebaseStrategy) {

            val nextTrainTag = rebase.next_train_tag

            // Now the container needs to be commited to create the imageId for the
            val imageId = this.client.commitByRebase(
                    output.containerOutput.containerId,
                    rebase.export_files.map { Paths.get(it) },
                    rebase.from,
                    arrival.repoName,
                    DockerTag.from(nextTrainTag.repr)
            )
            // Return the train departure. The trainId is not going to change, the new TrainTag
            // is communicated via the nextTrainTag
            return DockerRegistryTrainDeparture(
                    imageId,
                    arrival.trainName,
                    nextTrainTag,
                    this.client)
        }
        throw TrainException.UnsupportedRebaseStrategy(rebase)
    }
}
