package de.difuture.ekut.pht.lib.train.impl.docker

/**
 * Specifies how the Print Summary trainCommand is to be executed on a particular
 * Train Arrival using a Docker Client.
 *
 * @author Lukas Zimmermann
 * @since 0.0.3
 *
 */
//object PrintSummaryExecutor : DockerArrivalExecutor<TrainResponse.PrintSummaryResponse>,
//    DockerDepartureExecutor<TrainResponse.PrintSummaryResponse> {
//
//    override val command = TrainCommand.PRINT_SUMMARY
//
//    /**
//     * Executes the "print_summary" train trainCommand for b
//     * using a specific [DockerRuntimeClient]
//     *
//     */
//    override fun execArrival(interf: DockerRegistryTrainArrival, client: DockerRuntimeClient, info: StationInfo) =
//        execute<TrainResponse.PrintSummaryResponse>(
//            command,
//            interf,
//            client,
//            info
//        )
//
//    /**
//     * Executes the "print_summary" trainCommand on the [DockerRegistryTrainDeparture].
//     *
//     */
//    override fun execDeparture(interf: DockerRegistryTrainDeparture, info: StationInfo) =
//        execute<TrainResponse.PrintSummaryResponse>(
//            command,
//            interf,
//            info
//        )
//}
