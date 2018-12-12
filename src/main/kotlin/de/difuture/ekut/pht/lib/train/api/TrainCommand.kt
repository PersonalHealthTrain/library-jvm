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
 * A train command can be resolved with [StationInfo] to generate a list of commands
 * that can be passed to the run command when starting a container from an image.
 *
 * @author Lukas Zimmermann
 * @since 0.1.3
 *
 */
@JsonSerialize(using = TrainCommandSerializer::class)
@JsonDeserialize(using = TrainCommandDeserializer::class)
enum class TrainCommand(val repr: String) {

    CHECK_REQUIREMENTS("check_requirements"),
    LIST_REQUIREMENTS("list_requirements"),
    PRINT_SUMMARY("print_summary"),
    RUN_ALGORITHM("run_algorithm");

    /**
     * Resolves the provided [StationInfo] on `this` [TrainCommand] to produce the list
     * of command line tokens
     *
     * @param info The [StationInfo] needed to produce the command line tokens
     * @return The list of command line tokens for `this` [TrainCommand]
     */
        fun resolveWith(info: StationInfo) = listOf(
            "--stationid", info.stationId.toString(),
            "--mode", info.mode,
            this.repr
        )
}

fun trainCommand(from: String): TrainCommand = when (from) {
    TrainCommand.CHECK_REQUIREMENTS.repr -> TrainCommand.CHECK_REQUIREMENTS
    TrainCommand.CHECK_REQUIREMENTS.toString() -> TrainCommand.CHECK_REQUIREMENTS

    TrainCommand.LIST_REQUIREMENTS.repr -> TrainCommand.LIST_REQUIREMENTS
    TrainCommand.LIST_REQUIREMENTS.toString() -> TrainCommand.LIST_REQUIREMENTS

    TrainCommand.PRINT_SUMMARY.repr -> TrainCommand.PRINT_SUMMARY
    TrainCommand.PRINT_SUMMARY.toString() -> TrainCommand.PRINT_SUMMARY

    TrainCommand.RUN_ALGORITHM.repr -> TrainCommand.RUN_ALGORITHM
    TrainCommand.RUN_ALGORITHM.toString() -> TrainCommand.RUN_ALGORITHM
    else -> throw IllegalArgumentException("Not a Train Command: $from")
}
