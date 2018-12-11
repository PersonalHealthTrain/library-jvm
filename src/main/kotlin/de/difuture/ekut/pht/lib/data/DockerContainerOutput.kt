package de.difuture.ekut.pht.lib.data

/**
 * Represents the output from a Docker Container after it has been exited.
 *
 * Note that the existence of objects of this class is unrelated to the existence
 * of the container on the underlying Docker host system.
 *
 * @author Lukas Zimmermann
 * @since 0.0.1
 *
 */
data class DockerContainerOutput(

    /**
    * The ID from the container that has been started and which has exited to produce this output
    */
    val containerId: DockerContainerId,

    /**
     * The exit code from the exited container
     */
    val exitCode: Int,

    /**
     * Standard output from the container, represented as [String]
     *
     */
    val stdout: String,

    /**
     * Standard error from the container, represented as [String]
     */
    val stderr: String,

    /**
     * List from warnings that is potentially generated while executing the container.
     * This list is potentially empty
     */
    val warnings: List<String>
)
