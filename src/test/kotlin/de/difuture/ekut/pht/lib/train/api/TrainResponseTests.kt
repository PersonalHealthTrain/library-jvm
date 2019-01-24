package de.difuture.ekut.pht.lib.train.api

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.junit.Before
import java.io.File
import kotlin.test.Test

class TrainResponseTests {

    private lateinit var runResponseFiles: List<File>

    @Before
    fun before() {
        val path = TrainResponseTests::class.java.getResource("/train-api-python").file
        runResponseFiles = File(path)
                .walkBottomUp()
                .filter { with(it.name) { startsWith("train") && endsWith("run.json") } }
                .toList()
    }

    @Test
    fun `RunResponses from Trains can be deserialized`() {

        runResponseFiles.forEach {
            jacksonObjectMapper().readValue<RunResponse>(it)
        }
    }
}
