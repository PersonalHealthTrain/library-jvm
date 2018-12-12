package de.difuture.ekut.pht.lib.train.impl.docker

import de.difuture.ekut.pht.lib.runtime.api.docker.DockerRuntimeClient
import de.difuture.ekut.pht.lib.train.api.StationInfo
import de.difuture.ekut.pht.lib.train.api.TrainCommand
import de.difuture.ekut.pht.lib.train.api.TrainResponse

object ListRequirementsExecutor :
    DockerArrivalExecutor<TrainResponse.ListRequirementsResponse> {

    override val command = TrainCommand.LIST_REQUIREMENTS

    /**
     * Only check whether the exit code of the container is 0
     */
    override fun execArrival(interf: DockerRegistryTrainArrival, client: DockerRuntimeClient, info: StationInfo) =
        execute<TrainResponse.ListRequirementsResponse>(
            command,
            interf,
            client,
            info
        )
}
