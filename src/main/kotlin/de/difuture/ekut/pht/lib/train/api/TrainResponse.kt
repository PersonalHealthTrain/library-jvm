package de.difuture.ekut.pht.lib.train.api

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Container class for responses that are produced by a train.
 *
 */
sealed class TrainResponse

data class RunResponse(

    @JsonProperty("state")
    val state: AlgorithmExitState,

    @JsonProperty("free_text_message")
    val free_text_message: String,

    @JsonProperty("rebase")
    val rebase: RebaseStrategy

) : TrainResponse() {

enum class AlgorithmExitState(val repr: String) {

        SUCCESS("success"),
        FAILURE("failure"),
        APPLICATION("application")
    }
}
