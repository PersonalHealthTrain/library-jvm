package de.difuture.ekut.pht.lib.data

import kotlin.test.Test
import kotlin.test.assertEquals

class TrainTagTests {

    @Test
    fun `== operator on TrainTag works as expected`() {
        assertEquals("train_foo".toTrainTag(), "train_foo".toTrainTag())
    }
}
