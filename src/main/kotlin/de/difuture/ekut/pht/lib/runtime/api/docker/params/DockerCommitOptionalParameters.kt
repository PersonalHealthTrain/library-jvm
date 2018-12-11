package de.difuture.ekut.pht.lib.runtime.api.docker.params

/**
 * Optional Parameters for Docker Commit
 *
 * @author Lukas Zimmermann
 * @see DockerRuntimeClient
 * @since 0.0.5
 */
data class DockerCommitOptionalParameters(

    val targetHost: String? = null,
    val author: String? = null,
    val comment: String? = null
)
