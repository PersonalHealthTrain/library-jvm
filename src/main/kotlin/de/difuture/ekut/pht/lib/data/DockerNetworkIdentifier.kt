package de.difuture.ekut.pht.lib.data

import de.difuture.ekut.pht.lib.internal.requireIsValidDockerHash
import kotlinext.string.containsWhitespace

/**
 * Represents a reference to a Docker Network on a Docker host.

 * The [String] representation of instances must be a valid reference to a Docker network. The reference
 * is meaningless without the underlying Docker daemon.
 *
 * @author Lukas Zimmermann
 * @since 0.1.1
 */
sealed class DockerNetworkReference(open val repr: String)

/**
 * Represents the Id of a Docker Network.
 */
data class DockerNetworkId(override val repr: String) : DockerNetworkReference(repr) {
    init {
        requireIsValidDockerHash(repr, DockerNetworkId::class.java)
    }
}

/**
 * The Name of a Docker Network
 */
data class DockerNetworkName(override val repr: String) : DockerNetworkReference(repr) {
    init {
        require(repr.isNotBlank() && ! repr.containsWhitespace())
    }
}
