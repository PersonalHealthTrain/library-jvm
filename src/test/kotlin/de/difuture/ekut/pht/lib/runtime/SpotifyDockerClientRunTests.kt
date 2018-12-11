package de.difuture.ekut.pht.lib.runtime

import com.spotify.docker.client.DefaultDockerClient
import de.difuture.ekut.pht.lib.runtime.api.docker.DockerRuntimeClient
import de.difuture.ekut.pht.lib.runtime.impl.SpotifyDockerClient
import jdregistry.client.data.RepositoryName
import jdregistry.client.data.Tag as DockerTag
import org.junit.Before
import org.junit.Test

/**
 * Class that is particularly meant to test the run method of [DefaultDockerClient]
 *
 * @author Lukas Zimmermann
 * @see DefaultDockerClient
 * @since 0.0.1
 *
 */
class SpotifyDockerClientRunTests {

    private lateinit var client: DockerRuntimeClient

    @Before
    fun before() {
        this.client = SpotifyDockerClient()
    }

    @Test
    fun run_alpine() {

        val image = this.client.pull(RepositoryName.from("alpine"), DockerTag.LATEST)
        this.client.run(image, emptyList(), true)
    }
}
