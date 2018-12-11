package de.difuture.ekut.pht.lib.data

class DockerContainerIdTests : DockerIdTestBase<DockerContainerId>() {
    override fun createObject(input: String) = DockerContainerId(input)
    override fun reprOfObject(obj: DockerContainerId) = obj.repr
}

class DockerImageIdTests : DockerIdTestBase<DockerImageId>() {
    override fun createObject(input: String) = DockerImageId(input)
    override fun reprOfObject(obj: DockerImageId) = obj.repr
}

class DockerNetworkIdTests : DockerIdTestBase<DockerNetworkId>() {
    override fun createObject(input: String) = DockerNetworkId(input)
    override fun reprOfObject(obj: DockerNetworkId) = obj.repr
}
