package de.difuture.ekut.pht.lib.runtime.api.interrupt

/**
 * Handler that can be passed to runtime commands to determine whether the call was interrupted from
 * outside.
 *
 * @author Lukas Zimmermann
 * @since 0.0.1
 *
 */
interface InterruptHandler<in A> {

    /**
     * Performs action to handle the interrupt on the object A that has been interrupted
     *
     * @param obj Object for which the interrupt should be handled
     *
     */
    fun handleInterrupt(obj: A)
}
