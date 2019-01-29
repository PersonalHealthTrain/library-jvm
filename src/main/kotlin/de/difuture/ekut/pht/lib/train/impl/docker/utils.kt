package de.difuture.ekut.pht.lib.train.impl.docker

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import de.difuture.ekut.pht.lib.runtime.api.docker.DockerRuntimeClient
import de.difuture.ekut.pht.lib.train.api.StationRuntimeInfo
import de.difuture.ekut.pht.lib.train.api.TrainCommand
import de.difuture.ekut.pht.lib.train.api.TrainOutput
import de.difuture.ekut.pht.lib.train.api.TrainResponse
import dockerdaemon.data.DockerContainerOutput
import kotlinext.string.maybeTrailingJson
import java.lang.Exception

/**
 * Tries to read the [TrainResponse] from a [DockerContainerOutput] and returns
 * an appropriate [TrainOutput.DockerTrainOutput]
 *
 * @param output The [DockerContainerOutput] from which the [TrainResponse] should be read from
 * @return The [TrainOutput.DockerTrainOutput] generated from the input [DockerContainerOutput]
 */
private inline fun <reified T : TrainResponse> toTrainOutput(
    output: DockerContainerOutput
): TrainOutput.DockerTrainOutput<T> {
    // Tries to read the TrainResponse from the Container Output. Any Exception will result
    // in a null response. The error String will be the message of the thrown exception
    // The Train API specification tells us that we need to parse the Trailing Json as a Train Response
    val (response, error) = try {
        Pair<T, String?>(jacksonObjectMapper().readValue(output.stdout.maybeTrailingJson()), null)
    } catch (e: Exception) {
        Pair<T?, String>(null, e.message.orEmpty())
    }
    return TrainOutput.DockerTrainOutput(response, error, output)
}

/**
 * Executes a [TrainCommand] on a [DockerRegistryTrainArrival] with the provided [DockerRuntimeClient] with the
 * given [StationRuntimeInfo]
 *
 * @param
 *
 */
internal inline fun <reified T : TrainResponse> execute(
    command: TrainCommand,
    interf: DockerRegistryTrainArrival,
    client: DockerRuntimeClient,
    info: StationRuntimeInfo,
    rm: Boolean = true
): TrainOutput.DockerTrainOutput<T> {

    // First, use the Docker Client to pull the Docker image from the Docker Registry
    val imageId = client.pull(interf.repoName, interf.tag, interf.host)

    // Second, use the client to execute generate the Docker Container Output
    val containerOutput = client.run(imageId, command.resolveWith(info), rm = rm)

    // Extend the DockerContainer Output to a Train Output (by reading the stdout of the container)
    return toTrainOutput(containerOutput)
}

/**
 * Executes the DockerRegistryTrainDeparture
 *
 */
internal inline fun <reified T : TrainResponse> execute(
    command: TrainCommand,
    interf: DockerRegistryTrainDeparture,
    info: StationRuntimeInfo
): TrainOutput.DockerTrainOutput<T> {

    // Generate the DockerContainerOutput by using the client on the departure interface
    val containerOutput = interf.client.run(interf.imageId, command.resolveWith(info), rm = true)

    return toTrainOutput(containerOutput)
}
