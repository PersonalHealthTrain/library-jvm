package de.difuture.ekut.pht.lib.runtime.api

/**
 * Top-most api of runtime clients implemented by the station.
 *
 * Represents the Client that will run trains at the station. Implementers of this api use
 * and depend on the resources offered by the infrastructure of the station. As an example, the
 * [DockerRuntimeClient] depends on the host system offering a running Docker daemon and the user being
 * able to communicate with the daemon.
 *
 * @author Lukas Zimmermann
 * @since 0.0.1
 *
 */
interface RuntimeClient : AutoCloseable
