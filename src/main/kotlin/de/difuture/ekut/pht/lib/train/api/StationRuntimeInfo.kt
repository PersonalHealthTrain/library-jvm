package de.difuture.ekut.pht.lib.train.api

/**
 *
 * Represents the set of information that a Station needs to provide when executing a train command.
 *
 * @author Lukas Zimmermann
 * @since 0.0.1
 *
 */
data class StationRuntimeInfo(

    /**
     * The numeric station id. This means, a train knows at runtime at which station it is running at.
     */
    val stationId: Int,

    /**
     * Optional information on the track that the Train is currently running at
     */
    val trackInfo: String? = null,

    /**
     * Custom User Data for the Train
     */
    val userData: String? = null
) {
    init {
        require(stationId > 0) {
            "Station Id must be a positive integer!"
        }
    }
}
