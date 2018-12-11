package de.difuture.ekut.pht.lib.runtime.api.docker

import de.difuture.ekut.pht.lib.runtime.api.docker.data.DockerRunOptionalParameters
import dockerdaemon.data.DockerImageId

fun DockerRuntimeClient.withDefaultRunParameters(params: DockerRunOptionalParameters): DockerRuntimeClient =

    object : DockerRuntimeClient by this {

        override fun run(
            imageId: DockerImageId,
            commands: List<String>,
            rm: Boolean,
            optionalParams: DockerRunOptionalParameters?
        ) =
                this@withDefaultRunParameters.run(
                imageId,
                commands,
                rm,
                    DockerRunOptionalParameters(
                        env = optionalParams?.env ?: params.env,
                        network = optionalParams?.network ?: params.network,
                        interruptSignaler = optionalParams?.interruptSignaler ?: params.interruptSignaler,
                        interruptHandler = optionalParams?.interruptHandler ?: params.interruptHandler
                    )
                )
        }
