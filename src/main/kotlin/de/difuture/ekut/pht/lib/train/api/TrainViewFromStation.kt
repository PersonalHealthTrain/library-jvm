package de.difuture.ekut.pht.lib.train.api

import de.difuture.ekut.pht.lib.data.TrainName
import de.difuture.ekut.pht.lib.data.TrainTag
import de.difuture.ekut.pht.lib.runtime.api.RuntimeClient

/**
 * Represents how a station sees a train for arrival and departure.
 *
 * @author Lukas Zimmermann
 * @see TrainArrival
 * @see TrainDeparture
 * @since 0.0.1
 *
 */
sealed class TrainViewFromStation {

    abstract val trainName: TrainName
    abstract val trainTag: TrainTag
}

abstract class TrainArrival : TrainViewFromStation()
abstract class TrainDeparture<A : RuntimeClient> : TrainViewFromStation() {

    abstract val client: A
}
