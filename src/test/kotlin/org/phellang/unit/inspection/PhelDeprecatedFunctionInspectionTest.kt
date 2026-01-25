package org.phellang.unit.inspection

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.phellang.completion.data.DeprecationInfo
import org.phellang.completion.data.PhelFunctionRegistry
import org.phellang.inspection.deprecated.DeprecationMessageBuilder
import org.phellang.inspection.deprecated.ReplacementParser

class PhelDeprecatedFunctionInspectionTest {

    @Nested
    inner class DeprecationInfoDataClass {

        @Test
        fun `should store version and replacement`() {
            val deprecation = DeprecationInfo(version = "0.25.0", replacement = "assoc")

            assertEquals("0.25.0", deprecation.version)
            assertEquals("assoc", deprecation.replacement)
        }

        @Test
        fun `should allow null replacement`() {
            val deprecation = DeprecationInfo(version = "1.0")

            assertEquals("1.0", deprecation.version)
            assertNull(deprecation.replacement)
        }

        @Test
        fun `should support Use prefix convention`() {
            val deprecation = DeprecationInfo(version = "Use phel\\str\\contains?")

            assertTrue(deprecation.version.startsWith("Use "))
        }
    }

    @Nested
    inner class RegistryDeprecationLookup {

        @Test
        fun `should detect deprecated functions`() {
            val deprecatedFunctions = listOf("put", "push", "unset", "put-in", "unset-in")

            deprecatedFunctions.forEach { name ->
                assertTrue(
                    PhelFunctionRegistry.isDeprecated(name), "Function '$name' should be detected as deprecated"
                )
            }
        }

        @Test
        fun `should not flag non-deprecated functions`() {
            val validFunctions = listOf("map", "filter", "reduce", "conj", "assoc")

            validFunctions.forEach { name ->
                assertFalse(
                    PhelFunctionRegistry.isDeprecated(name), "Function '$name' should not be deprecated"
                )
            }
        }

        @Test
        fun `should retrieve deprecation info from registry`() {
            val function = PhelFunctionRegistry.getFunction("put")
            assertNotNull(function)

            val deprecation = function?.documentation?.deprecation
            assertNotNull(deprecation)
            assertEquals("assoc", deprecation?.replacement)
        }
    }

    @Nested
    inner class MessageBuilderIntegration {

        @Test
        fun `should build message using DeprecationMessageBuilder`() {
            val deprecation = DeprecationInfo(version = "0.25.0", replacement = "assoc")

            val message = DeprecationMessageBuilder.build("put", deprecation)

            assertTrue(message.contains("'put'"))
            assertTrue(message.contains("deprecated"))
            assertTrue(message.contains("assoc"))
        }

        @Test
        fun `should extract replacement using DeprecationMessageBuilder`() {
            val deprecation = DeprecationInfo(version = "Use phel\\str\\contains?")

            val replacement = DeprecationMessageBuilder.extractReplacement(deprecation)

            assertEquals("phel\\str\\contains?", replacement)
        }
    }

    @Nested
    inner class ReplacementParserIntegration {

        @Test
        fun `should parse namespace replacement using ReplacementParser`() {
            val parsed = ReplacementParser.parse("phel\\str\\contains?")

            assertEquals("str", parsed.namespace)
            assertEquals("str/contains?", parsed.functionName)
        }

        @Test
        fun `should format for display using ReplacementParser`() {
            val display = ReplacementParser.formatForDisplay("phel\\str\\contains?")

            assertEquals("str/contains?", display)
        }
    }
}
