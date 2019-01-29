package de.difuture.ekut.pht.lib.train.impl.docker

import de.difuture.ekut.pht.lib.runtime.api.docker.DockerRuntimeClient
import de.difuture.ekut.pht.lib.train.api.RunResponse
import de.difuture.ekut.pht.lib.train.api.StationRuntimeInfo
import de.difuture.ekut.pht.lib.train.api.TrainCommand

object RunExecutor : DockerArrivalExecutor<RunResponse> {

    override val command = TrainCommand.RUN

    // Run command is not allowed to remove the container, hence rm = false
    override fun execArrival(interf: DockerRegistryTrainArrival, client: DockerRuntimeClient, info: StationRuntimeInfo) =
        execute<RunResponse>(command, interf, client, info, rm = false)
}
