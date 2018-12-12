package de.difuture.ekut.pht.lib.train.api

/**
 *
 * Represents the set of information that a Station needs to provide when executing a train command.
 *
 * @author Lukas Zimmermann
 * @since 0.0.1
 *
 */
data class StationInfo(

    /**
     * The numeric station id. This means, a train knows at runtime at which station it is running at.
     */
    val stationId: Int,

    /**
     * The mode in which the train was executed. This is currently unused and has no semantics, so the station
     * can pass anything.
     */
    val mode: String
)
