package de.difuture.ekut.pht.lib.internal

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import de.difuture.ekut.pht.lib.train.api.TrainCommand
import java.io.IOException

internal class TrainCommandSerializer @JvmOverloads constructor(t: Class<TrainCommand>? = null) : StdSerializer<TrainCommand>(t) {

    @Throws(IOException::class, JsonProcessingException::class)
    override fun serialize(value: TrainCommand, gen: JsonGenerator, provider: SerializerProvider) {
        gen.writeString(value.repr)
    }
}
