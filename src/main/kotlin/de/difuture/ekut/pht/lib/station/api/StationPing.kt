package de.difuture.ekut.pht.lib.station.api

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Ping that the stations sends regularly to an endpoint to register itself
 *
 * @author Lukas Zimmermann
 * @since 0.0.1
 *
 */
data class StationPing(

    @JsonProperty("id")
    val id: Int,

    @JsonProperty("display_name")
    val displayName: String?
)
