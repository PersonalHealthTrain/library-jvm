package de.difuture.ekut.pht.lib.station.api

import com.fasterxml.jackson.annotation.JsonProperty

data class StationPingResponse(

    @JsonProperty("status")
    val status: String
)
