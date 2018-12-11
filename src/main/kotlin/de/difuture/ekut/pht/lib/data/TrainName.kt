package de.difuture.ekut.pht.lib.data

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import de.difuture.ekut.pht.lib.internal.TrainNameDeserializer
import de.difuture.ekut.pht.lib.internal.TrainNameSerializer

/**
 * Represents the Name of a train.
 *
 * @author Lukas Zimmermann
 * @since 0.0.1
 */
@JsonSerialize(using = TrainNameSerializer::class)
@JsonDeserialize(using = TrainNameDeserializer::class)
interface TrainName {

    /**
     * The [String] representation of the Train Name
     */
    val repr: String

    private data class GenericTrainName(override val repr: String) : TrainName

    companion object {
        fun from(input: String): TrainName {
            require(input.matches(regex))
            return GenericTrainName(input)
        }

        // The regex that we allow for potential values from the train name
        // A train needs to start with the train_ prefix
        private val regex = Regex("train_[a-zA-Z](?:[a-zA-Z0-9_-]*[a-z0-9])?")
    }
}
