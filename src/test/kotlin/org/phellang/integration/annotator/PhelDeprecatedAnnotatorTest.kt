package org.phellang.integration.annotator

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.phellang.annotator.infrastructure.PhelAnnotationConstants
import org.phellang.completion.data.PhelFunctionRegistry

class PhelDeprecatedAnnotatorTest {

    @Nested
    inner class DeprecationHighlighting {

        @Test
        fun `deprecated symbols should use DEPRECATED_SYMBOL attribute key`() {
            val deprecatedKey = PhelAnnotationConstants.DEPRECATED_SYMBOL
            assertTrue(deprecatedKey.externalName == "PHEL_DEPRECATED_SYMBOL")
        }

        @Test
        fun `deprecated symbol attribute should be properly configured`() {
            val deprecatedKey = PhelAnnotationConstants.DEPRECATED_SYMBOL
            assertTrue(deprecatedKey.externalName.isNotBlank())
        }
    }

    @Nested
    inner class DeprecationWorkflow {

        @Test
        fun `workflow should identify deprecated functions correctly`() {
            // Workflow: symbol text -> registry lookup -> deprecation check
            val deprecatedSymbols = listOf("put", "push", "unset", "put-in", "unset-in")
            
            deprecatedSymbols.forEach { symbol ->
                val isDeprecated = PhelFunctionRegistry.isDeprecated(symbol)
                assertTrue(isDeprecated, "Symbol '$symbol' should be detected as deprecated")
            }
        }

        @Test
        fun `workflow should not mark non-deprecated functions as deprecated`() {
            val nonDeprecatedSymbols = listOf("map", "filter", "reduce", "conj", "assoc")
            
            nonDeprecatedSymbols.forEach { symbol ->
                val isDeprecated = PhelFunctionRegistry.isDeprecated(symbol)
                assertFalse(isDeprecated, "Symbol '$symbol' should not be detected as deprecated")
            }
        }

        @Test
        fun `workflow should handle qualified names`() {
            assertTrue(PhelFunctionRegistry.isDeprecated("core/put"))
            assertTrue(PhelFunctionRegistry.isDeprecated("core/push"))
            assertFalse(PhelFunctionRegistry.isDeprecated("core/map"))
        }

        @Test
        fun `workflow should handle unknown symbols gracefully`() {
            assertFalse(PhelFunctionRegistry.isDeprecated("unknown-symbol"))
            assertFalse(PhelFunctionRegistry.isDeprecated("non/existent"))
        }
    }

    @Nested
    inner class AnnotationAttributeProperties {

        @Test
        fun `deprecated symbol constant should have unique name`() {
            val deprecatedName = PhelAnnotationConstants.DEPRECATED_SYMBOL.externalName
            val otherNames = listOf(
                PhelAnnotationConstants.FUNCTION_CALL.externalName,
                PhelAnnotationConstants.FUNCTION_NAME.externalName,
                PhelAnnotationConstants.NAMESPACE_SYMBOL.externalName
            )

            assertTrue(deprecatedName !in otherNames, "DEPRECATED_SYMBOL should have unique name")
        }
    }
}
