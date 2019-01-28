package de.difuture.ekut.pht.lib.data

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import kotlin.IllegalArgumentException
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class TrainNameTests {

    private val validTrainNames = listOf(
            "train_iris",
            "train_dummy"
    )

    private val illegalTrainNames = listOf(
            "",
            " ",
            "\t",
            "foobar",
            " foobar",
            "foo bar",
            "train_foo ",
            "train_foo bar"
    )

    private fun String.quoted() = "\"$this\""

    @Test
    fun `repr of TrainName is same as repr with quotes`() {
        with(jacksonObjectMapper()) {
            validTrainNames.forEach {
                assertEquals(writeValueAsString(it), it.toTrainName().repr.quoted())
            }
        }
    }

    @Test
    fun `illegal Strings cannot be turned into Train Names`() {
            illegalTrainNames.forEach {
                assertFailsWith<IllegalArgumentException> { it.toTrainName() }
            }
        }

    @Test
    fun `== operator on TrainName works as expected`() {
        assertEquals("train_foo".toTrainName(), "train_foo".toTrainName())
    }
}
