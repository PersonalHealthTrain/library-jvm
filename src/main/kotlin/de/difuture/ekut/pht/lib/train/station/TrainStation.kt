package de.difuture.ekut.pht.lib.train.station

import de.difuture.ekut.pht.lib.runtime.api.RuntimeClient
import de.difuture.ekut.pht.lib.train.api.ArrivalExecutor
import de.difuture.ekut.pht.lib.train.api.StationRuntimeInfo
import de.difuture.ekut.pht.lib.train.api.TrainArrival
import de.difuture.ekut.pht.lib.train.api.TrainResponse
import de.difuture.ekut.pht.lib.train.api.TrainOutput
import de.difuture.ekut.pht.lib.train.api.TrainDeparture
import de.difuture.ekut.pht.lib.train.api.DepartureExecutor

/**
 * A TrainStation is a client of the TrainAPI that consistently uses
 * the provided [RuntimeClient]. Instances of this class hence serve as an entrypoint
 * for running commands on train arrivals or departures or to generate new departures from
 * arrivals
 *
 * @author Lukas Zimmermann
 * @since 0.1.7
 *
 */
open class TrainStation<T : RuntimeClient>(

    protected open val client: T,
    protected open val stationInfo: StationRuntimeInfo
) {

    /**
     * Executes an [ArrivalExecution] on an [TrainArrival]
     *
     * @param exec The [ArrivalExecution] that should be performed
     * @param arrival The [TrainArrival] that is the target of the execution
     *
     * @return The [TrainOutput] returned by executing the TrainExecution
     *
     */
    fun <A : TrainArrival, B : TrainResponse> execute(
        exec: ArrivalExecutor<A, T, TrainOutput<B>, B>,
        arrival: A
    ) = exec.execArrival(arrival, this.client, this.stationInfo)

    /**
     * Executes on [DepartureExecutor] on a [TrainDeparture].
     *
     * @param exec The [DepartureExecutor] that should be applied on the [TrainDeparture]
     * @param departure The [TrainDeparture] that is the target of the execution
     *
     * @return The [TrainOutput] that is returned by the train execution
     *
     */
    fun <A : TrainDeparture<T>, B : TrainResponse> execute(
        exec: DepartureExecutor<A, T, TrainOutput<B>, B>,
        departure: A
    ) = exec.execDeparture(departure, this.stationInfo)
}
