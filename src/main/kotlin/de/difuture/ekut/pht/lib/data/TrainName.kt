package de.difuture.ekut.pht.lib.data

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import de.difuture.ekut.pht.lib.internal.TrainNameDeserializer
import de.difuture.ekut.pht.lib.internal.TrainNameSerializer

/**
 * Represents the name of a Train.
 *
 * For a given repository name /segment1/segment2/../segmentn,
 * the train name corresponds to segmentn, which has to start with
 * train_. The train name is identical between train yard and train registy if
 * a train has been instantiated from the train yard.
 *
 * @author Lukas Zimmermann
 * @since 0.0.1
 */
@JsonSerialize(using = TrainNameSerializer::class)
@JsonDeserialize(using = TrainNameDeserializer::class)
sealed class TrainName(open val repr: String)

fun String.toTrainName(): TrainName = PrivateTrainName(this)

fun String.isValidTrainName(): Boolean = matches(regex)

private val regex = Regex("train_[a-zA-Z](?:[a-zA-Z0-9_-]*[a-z0-9])?")

private data class PrivateTrainName(
    override val repr: String
) : TrainName(repr) {
    init {
        require(repr.isValidTrainName())
    }
}
