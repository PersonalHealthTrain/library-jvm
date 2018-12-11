package de.difuture.ekut.pht.lib.internal

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import de.difuture.ekut.pht.lib.data.TrainName
import java.io.IOException

internal class TrainNameSerializer @JvmOverloads constructor(t: Class<TrainName>? = null) : StdSerializer<TrainName>(t) {

    @Throws(IOException::class, JsonProcessingException::class)
    override fun serialize(
        value: TrainName,
        jgen: JsonGenerator,
        provider: SerializerProvider
    ) {
        jgen.writeString(value.repr)
    }
}
