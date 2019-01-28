package de.difuture.ekut.pht.lib.train.api

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import de.difuture.ekut.pht.lib.internal.TrainCommandDeserializer
import de.difuture.ekut.pht.lib.internal.TrainCommandSerializer

/**
 * Enum that represents all commands that can be sent to a train.
 * This is essentially a fixed set of strings that the train understands
 * according to its API. If a train command is supposed to be enriched with
 * parameters, one might think about using sealed classes instead of an enum.
 *
 * A train command can be resolved with [StationRuntimeInfo] to generate a list of commands
 * that can be passed to the run command when starting a container from an image.
 *
 * @author Lukas Zimmermann
 * @since 0.1.3
 *
 */
@JsonSerialize(using = TrainCommandSerializer::class)
@JsonDeserialize(using = TrainCommandDeserializer::class)
enum class TrainCommand(val repr: String) {

    RUN("run"),
    DESCRIBE("describe");
    /**
     * Resolves the provided [StationRuntimeInfo] on `this` [TrainCommand] to produce the list
     * of command line tokens
     *
     * @param info The [StationRuntimeInfo] needed to produce the command line tokens
     * @return The list of command line tokens for `this` [TrainCommand]
     */
    fun resolveWith(info: StationRuntimeInfo): List<String> {
        val base = mutableListOf(
                "--station-id", info.stationId.toString()
        )
        if (info.trackInfo != null) {
            base.add("--track-info")
            base.add(info.trackInfo)
        }
        if (info.userData != null) {
            base.add("--user-data")
            base.add(info.userData)
        }
        base.add(this.repr)
        return base
    }
}

fun trainCommand(from: String): TrainCommand = when (from) {
    TrainCommand.DESCRIBE.repr -> TrainCommand.DESCRIBE
    TrainCommand.DESCRIBE.toString() -> TrainCommand.DESCRIBE

    TrainCommand.RUN.repr -> TrainCommand.RUN
    TrainCommand.RUN.toString() -> TrainCommand.RUN
    else -> throw IllegalArgumentException("Not a Train Command: $from")
}
