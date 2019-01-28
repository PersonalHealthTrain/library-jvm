package de.difuture.ekut.pht.lib.data

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import de.difuture.ekut.pht.lib.internal.TrainTagDeserializer
import de.difuture.ekut.pht.lib.internal.TrainTagSerializer
import jdregistry.client.data.Tag as DockerTag

/**
 * Represents the Tag of a Train.
 *
 * @author Lukas Zimmermann
 * @since 0.0.1
 *
 */
@JsonSerialize(using = TrainTagSerializer::class)
@JsonDeserialize(using = TrainTagDeserializer::class)
sealed class TrainTag(open val repr: String)

fun String.toTrainTag(): TrainTag = PrivateTrainTag(this)

fun DockerTag.toTrainTag(): TrainTag = PrivateTrainTag(this.repr)

private data class PrivateTrainTag(
    override val repr: String
) : TrainTag(repr)
