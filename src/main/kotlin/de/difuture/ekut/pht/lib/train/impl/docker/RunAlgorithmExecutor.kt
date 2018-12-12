package de.difuture.ekut.pht.lib.train.impl.docker

import de.difuture.ekut.pht.lib.runtime.api.docker.DockerRuntimeClient
import de.difuture.ekut.pht.lib.train.api.StationInfo
import de.difuture.ekut.pht.lib.train.api.TrainCommand
import de.difuture.ekut.pht.lib.train.api.TrainResponse

object RunAlgorithmExecutor :
    DockerArrivalExecutor<TrainResponse.RunAlgorithmResponse> {

    override val command = TrainCommand.RUN_ALGORITHM

    override fun execArrival(interf: DockerRegistryTrainArrival, client: DockerRuntimeClient, info: StationInfo) =
            // RunAlgorithmExecutor will not remove the container on finish, as the container might be used for the new trainImage
        execute<TrainResponse.RunAlgorithmResponse>(
            command,
            interf,
            client,
            info,
            rm = false
        )
}
