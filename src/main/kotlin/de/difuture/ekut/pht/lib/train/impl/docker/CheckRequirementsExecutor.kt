package de.difuture.ekut.pht.lib.train.impl.docker

import de.difuture.ekut.pht.lib.runtime.api.docker.DockerRuntimeClient
import de.difuture.ekut.pht.lib.train.api.StationInfo
import de.difuture.ekut.pht.lib.train.api.TrainCommand
import de.difuture.ekut.pht.lib.train.api.TrainResponse

object CheckRequirementsExecutor
    : DockerArrivalExecutor<TrainResponse.CheckRequirementsResponse> {

    override val command = TrainCommand.CHECK_REQUIREMENTS

    override fun execArrival(interf: DockerRegistryTrainArrival, client: DockerRuntimeClient, info: StationInfo) =
        execute<TrainResponse.CheckRequirementsResponse>(
            command,
            interf,
            client,
            info
        )
}
