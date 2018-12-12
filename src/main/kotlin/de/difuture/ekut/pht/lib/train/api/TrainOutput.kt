package de.difuture.ekut.pht.lib.train.api

import com.fasterxml.jackson.annotation.JsonProperty
import dockerdaemon.data.DockerContainerOutput

/**
 * The output that is extracted from the an exited train instance
 *
 * @author Lukas Zimmermann
 * @since 0.1.3
 *
 */
sealed class TrainOutput<T : TrainResponse>(
    open val response: T?,
    open val error: String?
) {

    data class DockerTrainOutput<T : TrainResponse>(
        @JsonProperty("response")
        override val response: T?,

        @JsonProperty("error")
        override val error: String?,

        @JsonProperty("containerOutput")
        val containerOutput: DockerContainerOutput

    ) : TrainOutput<T>(response, error)
}
