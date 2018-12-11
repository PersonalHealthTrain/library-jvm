package de.difuture.ekut.pht.lib.runtime.api.docker.data

import de.difuture.ekut.pht.lib.runtime.api.interrupt.InterruptHandler
import de.difuture.ekut.pht.lib.runtime.api.interrupt.InterruptSignaler
import dockerdaemon.data.DockerContainerId

/**
 * Represents the optional parameters that can be passed to Docker run.
 *
 * @author Lukas Zimmermann
 * @see DockerRuntimeClient
 * @since 0.0.5
 */
data class DockerRunOptionalParameters(

    val env: Map<String, String>? = null,
    val network: String? = null,
    val interruptSignaler: InterruptSignaler<DockerContainerId>? = null,
    val interruptHandler: InterruptHandler<DockerContainerId>? = null
)
