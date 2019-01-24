package de.difuture.ekut.pht.lib.train.api

import de.difuture.ekut.pht.lib.runtime.api.RuntimeClient

/**
 * Represents the execution of a
 *
 * @author Lukas Zimmermann
 */
interface TrainExecutor {

    val command: TrainCommand
}

/**
 * The [TrainExecutor] for Train Arrivals.
 *
 * @param A The return type of the Arrival TrainCommand that is chained by this [ArrivalExecutor]
 * @param B The [TrainInterface] for which this command execution is defined
 * @param C The [RuntimeClient] that is required for this execution
 *
 */
interface ArrivalExecutor<A : TrainArrival, B : RuntimeClient, C : TrainOutput<D>, D : TrainResponse> : TrainExecutor {

    /**
     * Executes the trainCommand for a given object with the Train Interface using
     * a particular Runtime client.
     *
     * @param interf The object implementing the [TrainInterface]
     * @param client The [RuntimeClient] for which the CommandExecution should be performed
     * @param info Additional Information that the payload needs to provide at runtime
     * @return The value that [TrainCommand] is supposed to return.
     */
    fun execArrival(interf: A, client: B, info: StationRuntimeInfo): C
}

/**
 * The [TrainExecutor] for Train Departures.
 *
 * @param A The return type of the Arrival TrainCommand that is chained by this [ArrivalExecutor]
 * @param B The [TrainInterface] for which this command execution is defined
 * @param C The [RuntimeClient] that is required for this execution
 *
 */
interface DepartureExecutor<A : TrainDeparture<B>, B : RuntimeClient, C : TrainOutput<D>, D : TrainResponse> : TrainExecutor {

    /**
     * Executes the trainCommand for a given object with the Train Interface using
     * a particular Runtime client.
     *
     * @param interf The object implementing the [TrainInterface]
     * @param info Additional Information that the payload needs to provide at runtime
     * @return The value that [TrainCommand] is supposed to return.
     */
    fun execDeparture(interf: A, info: StationRuntimeInfo): C
}
