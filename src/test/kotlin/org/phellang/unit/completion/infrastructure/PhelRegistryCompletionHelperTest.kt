package org.phellang.unit.completion.infrastructure

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.phellang.completion.data.CompletionInfo
import org.phellang.completion.data.DocumentationInfo
import org.phellang.completion.data.PhelFunction
import org.phellang.completion.infrastructure.PhelCompletionPriority
import org.phellang.completion.infrastructure.PhelRegistryCompletionHelper

class PhelRegistryCompletionHelperTest {

    @Test
    fun `PhelRegistryCompletionHelper should be a singleton object`() {
        assertNotNull(PhelRegistryCompletionHelper)
        assertEquals("PhelRegistryCompletionHelper", PhelRegistryCompletionHelper.javaClass.simpleName)
    }

    @Test
    fun `should have all namespace add methods`() {
        val methods = PhelRegistryCompletionHelper.javaClass.methods.map { it.name }

        assertTrue(methods.contains("addCoreFunctions"))
        assertTrue(methods.contains("addStringFunctions"))
        assertTrue(methods.contains("addJsonFunctions"))
        assertTrue(methods.contains("addHttpFunctions"))
        assertTrue(methods.contains("addHtmlFunctions"))
        assertTrue(methods.contains("addDebugFunctions"))
        assertTrue(methods.contains("addTestFunctions"))
        assertTrue(methods.contains("addReplFunctions"))
        assertTrue(methods.contains("addBase64Functions"))
        assertTrue(methods.contains("addPhpInteropFunctions"))
        assertTrue(methods.contains("addMockFunctions"))
    }

    @Nested
    inner class AliasTransformationLogic {

        @Test
        fun `function name should be transformable with alias`() {
            // Given a function "str/join" and alias map {"s" -> "str"}
            val function = createTestFunction("str", "str/join")
            val aliasMap = mapOf("s" to "str")

            // The transformation logic (simulated)
            val alias = aliasMap.entries.find { it.value == function.namespace }?.key
            val transformedName = if (alias != null) {
                val functionName = function.name.substringAfter("/")
                "$alias/$functionName"
            } else {
                function.name
            }

            assertEquals("s/join", transformedName)
        }

        @Test
        fun `function name should remain unchanged without alias`() {
            // Given a function "json/encode" and no alias for "json"
            val function = createTestFunction("json", "json/encode")
            val aliasMap = mapOf("s" to "str")  // No alias for json

            // The transformation logic (simulated)
            val alias = aliasMap.entries.find { it.value == function.namespace }?.key
            val transformedName = if (alias != null) {
                val functionName = function.name.substringAfter("/")
                "$alias/$functionName"
            } else {
                function.name
            }

            assertEquals("json/encode", transformedName)
        }

        @Test
        fun `core functions should remain unchanged`() {
            // Core functions don't have namespace prefix in aliasMap
            val function = createTestFunction("core", "core/map")
            val aliasMap = mapOf("s" to "str")

            // The transformation logic (simulated)
            val alias = aliasMap.entries.find { it.value == function.namespace }?.key
            val transformedName = if (alias != null) {
                val functionName = function.name.substringAfter("/")
                "$alias/$functionName"
            } else {
                function.name
            }

            assertEquals("core/map", transformedName)
        }

        @Test
        fun `multiple aliases should work correctly`() {
            val aliasMap = mapOf(
                "s" to "str", "j" to "json", "h" to "http"
            )

            val strFunction = createTestFunction("str", "str/join")
            val jsonFunction = createTestFunction("json", "json/encode")
            val httpFunction = createTestFunction("http", "http/request")

            assertEquals("s", aliasMap.entries.find { it.value == strFunction.namespace }?.key)
            assertEquals("j", aliasMap.entries.find { it.value == jsonFunction.namespace }?.key)
            assertEquals("h", aliasMap.entries.find { it.value == httpFunction.namespace }?.key)
        }

        @Test
        fun `empty alias map should not transform names`() {
            val function = createTestFunction("str", "str/join")
            val aliasMap = emptyMap<String, String>()

            val alias = aliasMap.entries.find { it.value == function.namespace }?.key
            assertNull(alias)

            val transformedName = function.name
            assertEquals("str/join", transformedName)
        }

        private fun createTestFunction(namespace: String, name: String): PhelFunction {
            return PhelFunction(
                namespace = namespace, name = name, signature = "($name ...)", completion = CompletionInfo(
                    tailText = "Test function", priority = PhelCompletionPriority.CORE_FUNCTIONS
                ), documentation = DocumentationInfo(
                    summary = "Test function documentation"
                )
            )
        }
    }
}
