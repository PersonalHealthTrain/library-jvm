package de.difuture.ekut.pht.lib.runtime.api.interrupt

/**
 * Handler that can be passed to runtime commands to determine whether the call was interrupted from
 * outside.
 *
 * @author Lukas Zimmermann
 * @since 0.0.1
 *
 */
interface InterruptSignaler<in A> {

    /**
     * Signals whether object has been interrupted
     *
     * @return Whether object [A] has been interrupted
     *
     */
    fun wasInterrupted(obj: A): Boolean
}
