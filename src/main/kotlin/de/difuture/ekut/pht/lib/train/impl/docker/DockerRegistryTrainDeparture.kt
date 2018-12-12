package de.difuture.ekut.pht.lib.train.impl.docker

import de.difuture.ekut.pht.lib.data.TrainName
import de.difuture.ekut.pht.lib.data.TrainTag
import de.difuture.ekut.pht.lib.runtime.api.docker.DockerRuntimeClient
import de.difuture.ekut.pht.lib.train.api.TrainDeparture
import dockerdaemon.data.DockerImageId

class DockerRegistryTrainDeparture(

    val imageId: DockerImageId,
    override val trainName: TrainName,
    override val trainTag: TrainTag,
    override val client: DockerRuntimeClient
) : TrainDeparture<DockerRuntimeClient>()
