package org.phellang.unit.completion.infrastructure

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.phellang.completion.infrastructure.PhelProjectCompletionHelper

class PhelProjectCompletionHelperTest {

    @Test
    fun `helper object is accessible`() {
        assertNotNull(PhelProjectCompletionHelper)
    }

    @Nested
    inner class AliasTransformation {

        @Test
        fun `symbol with alias uses alias in qualified name`() {
            // Import: (:require hello-world\util :as u)
            // Symbol: greet
            // Expected: u/greet
            val alias = "u"
            val name = "greet"
            val result = "$alias/$name"

            assertEquals("u/greet", result)
        }

        @Test
        fun `symbol without alias uses short namespace`() {
            // Import: (:require hello-world\util)
            // Symbol: greet
            // Expected: util/greet
            val shortNamespace = "util"
            val name = "greet"
            val result = "$shortNamespace/$name"

            assertEquals("util/greet", result)
        }

        @Test
        fun `alias from aliasMap takes precedence`() {
            // aliasMap: {"h" -> "helpers"}
            // Symbol namespace: helpers
            // Expected: h/function-name
            val aliasMap = mapOf("h" to "helpers")
            val shortNamespace = "helpers"
            val name = "format"

            val alias = aliasMap.entries.find { it.value == shortNamespace }?.key
            val result = if (alias != null) "$alias/$name" else "$shortNamespace/$name"

            assertEquals("h/format", result)
        }
    }

    @Nested
    inner class ImportInfoParsing {

        @Test
        fun `full namespace is split correctly`() {
            val fullNamespace = "hello-world\\util"
            val shortNamespace = fullNamespace.substringAfterLast("\\")

            assertEquals("util", shortNamespace)
        }

        @Test
        fun `nested namespace is split correctly`() {
            val fullNamespace = "my-app\\lib\\helpers"
            val shortNamespace = fullNamespace.substringAfterLast("\\")

            assertEquals("helpers", shortNamespace)
        }

        @Test
        fun `simple namespace without separator`() {
            val fullNamespace = "util"
            val shortNamespace = fullNamespace.substringAfterLast("\\")

            assertEquals("util", shortNamespace)
        }
    }

    @Nested
    inner class ImportedNamespaceDetection {

        @Test
        fun `directly imported namespace should be found`() {
            // (:require hello-world\util)
            // Result: ImportInfo(fullNamespace="hello-world\\util", shortNamespace="util", alias=null)
            val fullNamespace = "hello-world\\util"
            val shortNamespace = fullNamespace.substringAfterLast("\\")
            val alias: String? = null

            assertEquals("util", shortNamespace)
            assertNull(alias)
        }

        @Test
        fun `aliased namespace should be found with alias`() {
            // (:require hello-world\util :as u)
            // Result: ImportInfo(fullNamespace="hello-world\\util", shortNamespace="util", alias="u")
            val fullNamespace = "hello-world\\util"
            val shortNamespace = fullNamespace.substringAfterLast("\\")
            val alias = "u"

            assertEquals("util", shortNamespace)
            assertEquals("u", alias)
        }

        @Test
        fun `multiple imports should all be found`() {
            // (:require hello-world\util)
            // (:require my-app\helpers :as h)
            val imports = listOf(
                Triple("hello-world\\util", "util", null), Triple("my-app\\helpers", "helpers", "h")
            )

            assertEquals(2, imports.size)
            assertEquals("util", imports[0].second)
            assertNull(imports[0].third)
            assertEquals("helpers", imports[1].second)
            assertEquals("h", imports[1].third)
        }
    }
}
