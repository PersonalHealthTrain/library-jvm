package de.difuture.ekut.pht.lib.internal

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.ObjectNode
import de.difuture.ekut.pht.lib.data.toTrainTag
import de.difuture.ekut.pht.lib.train.api.DockerRebaseStrategy
import de.difuture.ekut.pht.lib.train.api.RebaseStrategy
import java.io.IOException
import java.lang.IllegalArgumentException

internal class RebaseStrategyDeserializer
@JvmOverloads constructor(vc: Class<*>? = null) : StdDeserializer<RebaseStrategy>(vc) {

    @Throws(IOException::class, JsonProcessingException::class)
    override fun deserialize(jp: JsonParser, ctxt: DeserializationContext): RebaseStrategy {
        val node = jp.codec.readTree<ObjectNode>(jp)
        return when (val type = node["type"].textValue()) {
            "docker" -> DockerRebaseStrategy(
                    export_files = (node["export_files"] as ArrayNode).map { it.textValue() }.toSet(),
                    next_train_tag = node["next_train_tag"].textValue().toTrainTag(),
                    from = node["from"].asText()
            )
            else -> throw IllegalArgumentException("$type is not a valid type for a RebaseStrategy")
        }
    }
}
