package de.difuture.ekut.pht.lib.internal

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.node.TextNode
import de.difuture.ekut.pht.lib.train.api.TrainCommand
import de.difuture.ekut.pht.lib.train.api.trainCommand
import java.io.IOException

internal class TrainCommandDeserializer @JvmOverloads constructor(vc: Class<*>? = null) : StdDeserializer<TrainCommand>(vc) {

    @Throws(IOException::class, JsonProcessingException::class)
    override fun deserialize(jp: JsonParser, ctxt: DeserializationContext): TrainCommand {
        val node = jp.codec.readTree<TextNode>(jp)
        return trainCommand(node.asText())
    }
}
