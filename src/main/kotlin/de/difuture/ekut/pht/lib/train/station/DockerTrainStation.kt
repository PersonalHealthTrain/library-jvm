package de.difuture.ekut.pht.lib.train.station

//import de.difuture.ekut.pht.lib.train.impl.docker.RunAlgorithmExecutor

//class DockerTrainStation(
//
//    override val client: DockerRuntimeClient,
//    override val stationInfo: StationRuntimeInfo
//) : TrainStation<DockerRuntimeClient>(client, stationInfo) {
//
//    fun departWithAlgorithm(arrival: DockerRegistryTrainArrival): DockerRegistryTrainDeparture {
//
//        val output = RunAlgorithmExecutor.execArrival(arrival, this.client, this.stationInfo)
//        val response = output.response
//
//        // The Output has an error if there either is an error or the response is null
//        if (output.error != null || response == null) {
//
//            throw TrainException.Output(output)
//        }
//
//        if (!response.success) {
//
//            throw TrainException.RunAlgorithmFailed(output)
//        }
//        val nextTrainTag = response.nextTrainTag
//
//        // Now the container needs to be commited to create the imageId for the
//        val imageId = this.client.commitByRebase(
//                output.containerOutput.containerId,
//                response.exportFiles.map { Paths.get(it) },
//                response.dockerBaseImage,
//                arrival.repoName,
//                DockerTag.from(nextTrainTag.repr)
//        )
//        // Return the train departure. The trainId is not going to change, the new TrainTag
//        // is communicated via the nextTrainTag
//        return DockerRegistryTrainDeparture(
//                imageId,
//                arrival.trainName,
//                nextTrainTag,
//                this.client)
//    }
//}
