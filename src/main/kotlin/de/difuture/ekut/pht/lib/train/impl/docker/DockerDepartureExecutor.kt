package de.difuture.ekut.pht.lib.train.impl.docker

import de.difuture.ekut.pht.lib.runtime.api.docker.DockerRuntimeClient
import de.difuture.ekut.pht.lib.train.api.DepartureExecutor
import de.difuture.ekut.pht.lib.train.api.TrainOutput
import de.difuture.ekut.pht.lib.train.api.TrainResponse

/**
 * Specialization of the [DepartureExecutor] for [DockerRuntimeClient], [DockerRegistryTrainDeparture]
 * and [TrainOutput.DockerTrainOutput]
 *
 */
interface DockerDepartureExecutor<A : TrainResponse> :
    DepartureExecutor<DockerRegistryTrainDeparture, DockerRuntimeClient, TrainOutput.DockerTrainOutput<A>, A>
