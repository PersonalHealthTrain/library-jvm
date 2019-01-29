package de.difuture.ekut.pht.lib.train.api

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Container class for responses that are produced by a train.
 *
 */
// TODO Train Description is currently not supported
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
        APPLICATION("application");

        companion object {

            @JsonCreator
            @JvmStatic
            fun from(input: String): AlgorithmExitState = when (input.toLowerCase()) {

                SUCCESS.repr -> SUCCESS
                FAILURE.repr -> FAILURE
                APPLICATION.repr -> APPLICATION
                else -> throw IllegalArgumentException("$input is not a valid AlgorithmExitState")
            }
        }
    }
}
