package de.difuture.ekut.pht.lib.utils

import java.net.URI

/**
 * Returns <host>:<port> if port is defined for this [URI], otherwise return only the <host>.
 *
 * @return <host>:<port> if port is defined for this [URI], otherwise return only the <host>
 */
fun URI.hostWithPort(): String = port.let {

    if (it == -1) host else "$host:$it"
}
