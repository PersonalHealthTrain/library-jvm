package de.difuture.ekut.pht.lib.train.impl.docker

import de.difuture.ekut.pht.lib.runtime.api.docker.DockerRuntimeClient
import de.difuture.ekut.pht.lib.train.api.ArrivalExecutor
import de.difuture.ekut.pht.lib.train.api.TrainOutput
import de.difuture.ekut.pht.lib.train.api.TrainResponse

/**
 * Specialization of the [ArrivalExecutor] for [DockerRuntimeClient], [DockerRegistryTrainArrival]
 * and [TrainOutput.DockerTrainOutput]
 *
 */
interface DockerArrivalExecutor<A : TrainResponse> :
    ArrivalExecutor<DockerRegistryTrainArrival, DockerRuntimeClient, TrainOutput.DockerTrainOutput<A>, A>
