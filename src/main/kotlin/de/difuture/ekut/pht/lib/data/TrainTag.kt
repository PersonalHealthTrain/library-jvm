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
interface TrainTag {

    val repr: String
}

fun String.toTrainTag(): TrainTag = object : TrainTag {
    override val repr = this@toTrainTag
}

fun DockerTag.toTrainTag(): TrainTag = object : TrainTag {
    override val repr = this@toTrainTag.repr
}
