package de.difuture.ekut.pht.lib.runtime

import de.difuture.ekut.pht.lib.runtime.api.docker.DockerRuntimeClient
import de.difuture.ekut.pht.lib.runtime.api.docker.AbstractDockerRuntimeClient
import jdregistry.client.data.RepositoryName
import jdregistry.client.data.Tag as DockerTag
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class SpotifyDockerClientPullTests {

    private lateinit var client: DockerRuntimeClient

    @Before
    fun before() {

        this.client = AbstractDockerRuntimeClient()
    }

    // Tests pull several image and ensure that the image id can be listed

    @Test
    fun pull_alpine() {

        val imageId = this.client.pull(RepositoryName.from("alpine"), DockerTag.LATEST)

        Assert.assertTrue(imageId in client.images())
    }
}
