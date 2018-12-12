package de.difuture.ekut.pht.lib.internal

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.node.TextNode
import de.difuture.ekut.pht.lib.data.TrainTag
import de.difuture.ekut.pht.lib.data.toTrainTag
import java.io.IOException

internal class TrainTagDeserializer @JvmOverloads constructor(vc: Class<*>? = null) : StdDeserializer<TrainTag>(vc) {

    @Throws(IOException::class, JsonProcessingException::class)
    override fun deserialize(jp: JsonParser, ctxt: DeserializationContext): TrainTag {
        val node = jp.codec.readTree<TextNode>(jp)
        return node.asText().toTrainTag()
    }
}
