package de.difuture.ekut.pht.lib.utils

import java.net.URI
import kotlin.test.Test
import kotlin.test.assertEquals

class URIsTests {

    @Test
    fun hostWithPorts() {
        assertEquals("foo:123", URI.create("https://foo:123").hostWithPort())
        assertEquals("foo", URI.create("https://foo").hostWithPort())
        assertEquals("192.168.0.0", URI.create("ftp://192.168.0.0").hostWithPort())
        assertEquals("192.168.0.0:5000", URI.create("ftp://192.168.0.0:5000").hostWithPort())
    }
}
