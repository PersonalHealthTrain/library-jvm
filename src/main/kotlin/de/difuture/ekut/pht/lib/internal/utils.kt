package de.difuture.ekut.pht.lib.internal

/**
 * Tests whether the input [String] is a valid value for a Docker Hash, as
 * it is used for IDs for containers, images, networks, etc.
 *
 * @param input The input [String] to be tested whether it is a valid Docker ID
 * @param clazz The [Class] that this input [String] should represent as object
 * @throws IllegalArgumentException if the input [String] is not a valid Docker ID
 */
internal fun requireIsValidDockerHash(input: String, clazz: Class<*>) {
    require(input.matches(dockerHashRegex)) {
        "Input String $input is not a valid Docker Hash for class: ${clazz.canonicalName}"
    }
}

/**
 * The regular expression for Docker Hash values. The sha256: prefix is optional.
 */
private val dockerHashRegex = Regex("(?:sha256:)?[a-z0-9]+")
