package org.phellang.integration.inspection

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.phellang.completion.data.DeprecationInfo
import org.phellang.completion.data.PhelFunctionRegistry
import org.phellang.inspection.deprecated.DeprecationMessageBuilder
import org.phellang.inspection.deprecated.ReplacementParser

class PhelDeprecatedFunctionInspectionIntegrationTest {

    @Nested
    inner class DeprecationDetectionWorkflow {

        @Test
        fun `should detect deprecated function and build correct message`() {
            val functionName = "put"
            assertTrue(PhelFunctionRegistry.isDeprecated(functionName))

            val function = PhelFunctionRegistry.getFunction(functionName)
            val deprecation = function?.documentation?.deprecation
            assertNotNull(deprecation)

            val message = DeprecationMessageBuilder.build(functionName, deprecation)
            assertTrue(message.contains("'put'"))
            assertTrue(message.contains("deprecated"))
        }

        @Test
        fun `should extract replacement and parse it correctly`() {
            val functionName = "str-contains?"
            val function = PhelFunctionRegistry.getFunction(functionName)
            val deprecation = function?.documentation?.deprecation

            val replacement = DeprecationMessageBuilder.extractReplacement(deprecation)
            assertNotNull(replacement)

            val parsed = ReplacementParser.parse(replacement!!)

            assertEquals("str", parsed.namespace)
            assertEquals("str/contains?", parsed.functionName)
        }

        @Test
        fun `should handle simple replacement without namespace`() {
            val functionName = "put"
            val function = PhelFunctionRegistry.getFunction(functionName)
            val deprecation = function?.documentation?.deprecation

            val replacement = DeprecationMessageBuilder.extractReplacement(deprecation)

            if (replacement != null) {
                val parsed = ReplacementParser.parse(replacement)

                if (parsed.namespace != null) {
                    assertTrue(parsed.functionName.contains("/"))
                }
            }
        }
    }

    @Nested
    inner class QuickFixWorkflow {

        @Test
        fun `should format replacement for display in quick-fix`() {
            val testCases = mapOf(
                "phel\\str\\contains?" to "str/contains?",
                "phel\\json\\encode" to "json/encode",
                "assoc" to "assoc",
                "map" to "map"
            )

            testCases.forEach { (input, expected) ->
                val display = ReplacementParser.formatForDisplay(input)
                assertEquals(expected, display, "Failed for input: $input")
            }
        }

        @Test
        fun `should determine if namespace import is needed`() {
            val strReplacement = ReplacementParser.parse("phel\\str\\contains?")
            assertNotNull(strReplacement.namespace)
            assertEquals("str", strReplacement.namespace)

            val coreReplacement = ReplacementParser.parse("assoc")
            assertNull(coreReplacement.namespace)
        }
    }

    @Nested
    inner class RegistryIntegration {

        @Test
        fun `should find all deprecated functions have valid deprecation info`() {
            val deprecatedFunctions = listOf("put", "push", "unset", "put-in", "unset-in", "str-contains?")

            deprecatedFunctions.forEach { name ->
                assertTrue(PhelFunctionRegistry.isDeprecated(name), "Expected $name to be deprecated")

                val function = PhelFunctionRegistry.getFunction(name)
                assertNotNull(function, "Expected to find function $name")
                assertNotNull(function?.documentation?.deprecation, "Expected $name to have deprecation info")
            }
        }

        @Test
        fun `should not flag non-deprecated functions`() {
            val nonDeprecatedFunctions = listOf("map", "filter", "reduce", "assoc", "conj")

            nonDeprecatedFunctions.forEach { name ->
                assertFalse(PhelFunctionRegistry.isDeprecated(name), "Expected $name to NOT be deprecated")
            }
        }

        @Test
        fun `deprecated functions should have either version or replacement`() {
            val deprecatedNames = listOf("put", "push", "unset")

            deprecatedNames.forEach { name ->
                val function = PhelFunctionRegistry.getFunction(name)
                val deprecation = function?.documentation?.deprecation
                assertNotNull(deprecation, "Expected $name to have deprecation info")

                val hasInfo = deprecation!!.version.isNotBlank() || deprecation.replacement != null
                assertTrue(hasInfo, "Deprecation info for $name should have version or replacement")
            }
        }
    }

    @Nested
    inner class MessageFormatting {

        @Test
        fun `messages should be user-friendly`() {
            val testCases = listOf(
                Triple("put", DeprecationInfo("0.25.0", "assoc"), "assoc"),
                Triple("func", DeprecationInfo("Use new-func"), "new-func"),
                Triple("old", DeprecationInfo("1.0"), null)
            )

            testCases.forEach { (funcName, deprecation, expectedReplacement) ->
                val message = DeprecationMessageBuilder.build(funcName, deprecation)

                assertTrue(message.contains("'$funcName'"), "Message should contain function name")

                assertTrue(message.contains("deprecated"), "Message should mention deprecation")

                if (expectedReplacement != null) {
                    assertTrue(message.contains(expectedReplacement), "Message should mention replacement")
                }
            }
        }
    }
}
