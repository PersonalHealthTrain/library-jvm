package de.difuture.ekut.pht.lib.data

import kotlin.IllegalArgumentException
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

abstract class DockerIdTestBase<T> {

    private companion object {

        /**
         *  IDs that should be valid and have no sha256: prefix
         */
        private val validIds = listOf(
                "a8c94ff83800",
                "0",
                "a",
                "nd632dw82e",
                "ababjhdt2",
                "51f9a1145165",
                "4e85772126c2",
                "74b5007d50ab",
                "48e0cda65fc9",
                "c5005ae06992",
                "d42c5d32fbeb",
                "8166239ca3b3",
                "196d12cf6ab1",
                "196d12cf6ab1",
                "efc483abdcc1",
                "efc483abdcc1",
                "5e35105718d5",
                "5c2e49cfd3c6",
                "940e677eea8d",
                "89a083358f3a",
                "c09547a0989a",
                "1a3f90c2ae47",
                "fbd89fa9d331",
                "ee05fd641832",
                "2a649da9add5",
                "67ebcfc41df0",
                "0bb4ddaeb6c5",
                "1fa511a83703",
                "c9f91304e512",
                "96d40cae7835",
                "395cc36c043e",
                "bb9c5d258bfc",
                "c5f07f36919f",
                "3f6e2fa1f452",
                "21e0503443c6",
                "a45e613c2267",
                "86e7117e43fd",
                "1bbf5ec86934",
                "c5f320fdf5c0",
                "3b16546bfe96",
                "59eb2f01533f",
                "af1d161b5361",
                "ce708bbffcba",
                "3ced40c0533a",
                "b659344f2f3f",
                "f1cb46627094",
                "ba8e7f82097f",
                "f21dd6c17811",
                "b2d2f7a041fe",
                "3b0e0f777bfc",
                "ac00d675847b",
                "8c4e049170f6",
                "ed27a20ea942",
                "dc0d76160f50",
                "b25d2c11d8c5",
                "9c3ce1306ae9",
                "6b39c189195b",
                "166195ec64fd",
                "5b6e2ebd3888",
                "d469a4440c7c",
                "535ca4219eb3",
                "070a9f02cf37",
                "e9caa8aff17b",
                "391baa6cbc74",
                "a568adc61965",
                "59e9c4dc4734",
                "b1b07966a2c9",
                "5e8e334e6c23",
                "78417df42da5",
                "f7e696f13da4",
                "f242ba7bbae8",
                "81e737f7fd92",
                "8b5d4f5152af"
        )

        private val invalidIDs = listOf(
                "   -",
                "",
                " "
        )
    }

    abstract fun createObject(input: String): T
    abstract fun reprOfObject(obj: T): String

    @Test
    fun `repr of object is same as input`() {
        validIds.forEach { noPrefix ->
            val withPrefix = "sha256:$noPrefix"
            assertEquals(noPrefix, reprOfObject(createObject(noPrefix)))
            assertEquals(withPrefix, reprOfObject(createObject(withPrefix)))
        }
    }

    @Test
    fun `invalidIds will throw Invalid Argument Exception`() {
        invalidIDs.forEach {
            assertFailsWith<IllegalArgumentException> { createObject(it) }
        }
    }
}
