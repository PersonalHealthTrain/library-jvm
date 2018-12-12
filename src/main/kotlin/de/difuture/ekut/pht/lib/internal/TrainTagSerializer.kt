package de.difuture.ekut.pht.lib.internal

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import de.difuture.ekut.pht.lib.data.TrainTag
import java.io.IOException

internal class TrainTagSerializer @JvmOverloads constructor(t: Class<TrainTag>? = null) : StdSerializer<TrainTag>(t) {

    @Throws(IOException::class, JsonProcessingException::class)
    override fun serialize(
        value: TrainTag,
        jgen: JsonGenerator,
        provider: SerializerProvider
    ) {
        jgen.writeString(value.repr)
    }
}
