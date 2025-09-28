package org.phellang.integration.annotator

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.phellang.annotator.analyzers.PhelSymbolPositionAnalyzer
import org.phellang.annotator.infrastructure.PhelAnnotationConstants
import org.phellang.annotator.infrastructure.PhelAnnotationUtils

class PhelAnnotatorWorkflowTest {

    @Test
    fun `annotator workflow should validate text before processing`() {
        // Test the workflow: text validation -> symbol analysis -> annotation
        val validTexts = listOf("symbol", "namespace\\symbol", "php/interop", "test?", "&")
        val invalidTexts = listOf(null, "", "   ", "\t\n")

        validTexts.forEach { text ->
            assertTrue(
                PhelAnnotationUtils.isValidText(text), "Valid text should pass validation: '$text'"
            )
        }

        invalidTexts.forEach { text ->
            assertFalse(
                PhelAnnotationUtils.isValidText(text), "Invalid text should fail validation: '$text'"
            )
        }
    }

    @Test
    fun `annotator workflow should analyze symbol positions correctly`() {
        // Test the workflow: text validation -> symbol position analysis
        val namespacedSymbols = listOf(
            "core\\map", "phel\\str", "user\\my-function", "project\\utils\\helper"
        )

        val regularSymbols = listOf(
            "map", "str", "my-function", "helper"
        )

        namespacedSymbols.forEach { symbol ->
            assertTrue(PhelAnnotationUtils.isValidText(symbol), "Should be valid text")
            assertTrue(
                PhelSymbolPositionAnalyzer.hasNamespacePrefix(symbol), "Should detect namespace prefix: '$symbol'"
            )
        }

        regularSymbols.forEach { symbol ->
            assertTrue(PhelAnnotationUtils.isValidText(symbol), "Should be valid text")
            assertFalse(
                PhelSymbolPositionAnalyzer.hasNamespacePrefix(symbol), "Should not detect namespace prefix: '$symbol'"
            )
        }
    }

    @Test
    fun `annotator workflow should handle Phel-specific constructs`() {
        // Test workflow for Phel-specific language constructs
        val phelConstructs = mapOf(
            "php/array" to "PHP interop",
            "core\\map" to "Namespaced symbol",
            "&" to "Variadic parameter",
            "test?" to "Predicate function",
            "test!" to "Mutating function",
            "my-var" to "Hyphenated symbol",
            "my_var" to "Underscore symbol"
        )

        phelConstructs.forEach { (construct, description) ->
            assertTrue(
                PhelAnnotationUtils.isValidText(construct), "$description should be valid text: '$construct'"
            )
        }
    }

    @ParameterizedTest
    @CsvSource(
        "core\\map, true, Namespaced symbol",
        "php/array, false, PHP interop",
        "regular-symbol, false, Regular symbol",
        "test?, false, Predicate function",
        "&, false, Variadic parameter"
    )
    fun `annotator workflow should categorize symbols correctly`(
        symbol: String, hasNamespace: Boolean, description: String
    ) {
        assertTrue(PhelAnnotationUtils.isValidText(symbol), "$description should be valid")
        assertEquals(
            hasNamespace,
            PhelSymbolPositionAnalyzer.hasNamespacePrefix(symbol),
            "$description namespace detection should be correct"
        )
    }

    @Test
    fun `annotator workflow should have consistent constants`() {
        // Test that all annotation constants are properly initialized and consistent
        val constants = listOf(
            PhelAnnotationConstants.NAMESPACE_SYMBOL,
            PhelAnnotationConstants.FUNCTION_NAME,
            PhelAnnotationConstants.SHORT_FUNCTION,
            PhelAnnotationConstants.PHP_INTEROP,
            PhelAnnotationConstants.FUNCTION_CALL,
            PhelAnnotationConstants.COMMENTED_OUT_FORM,
            PhelAnnotationConstants.FUNCTION_PARAMETER,
            PhelAnnotationConstants.COLLECTION_TYPE,
            PhelAnnotationConstants.KEYWORD,
            PhelAnnotationConstants.REGULAR_SYMBOL,
            PhelAnnotationConstants.VARIADIC_PARAMETER
        )

        // All constants should be initialized
        constants.forEach { constant ->
            assertNotNull(constant, "Constant should be initialized")
            assertNotNull(constant.externalName, "Constant should have external name")
            assertTrue(
                constant.externalName.startsWith("PHEL_"), "Constant should have PHEL_ prefix: ${constant.externalName}"
            )
        }

        // All constants should have unique names
        val names = constants.map { it.externalName }.toSet()
        assertEquals(constants.size, names.size, "All constants should have unique names")
    }

    @Test
    fun `annotator workflow should handle edge cases gracefully`() {
        // Test workflow with edge cases that might occur in real usage
        val edgeCases = listOf(
            "" to false,           // Empty string
            " " to false,          // Whitespace
            "\t" to false,         // Tab
            "\n" to false,         // Newline
            "a" to true,           // Single character
            "a\\b" to true,        // Minimal namespace
            "\\a" to true,         // Leading backslash (actually valid text, just not valid namespace)
            "a\\" to true,         // Trailing backslash (actually valid text, just not valid namespace)
            "a".repeat(100) to true,  // Long symbol (reduced size)
            "测试" to true,         // Unicode
            "emoji😀" to true      // Emoji
        )

        edgeCases.forEach { (input, shouldBeValid) ->
            val actualValid = PhelAnnotationUtils.isValidText(input)
            assertEquals(
                shouldBeValid,
                actualValid,
                "Edge case validation should be correct for: '${input.take(20)}${if (input.length > 20) "..." else ""}' (expected: $shouldBeValid, actual: $actualValid)"
            )
        }
    }

    @Test
    fun `annotator workflow should be performant with large inputs`() {
        // Test that the workflow performs well with large inputs
        val largeSymbol = "namespace\\" + "symbol".repeat(1000)
        val largeNamespace = "level\\".repeat(100) + "symbol"

        val startTime = System.nanoTime()

        // Test validation
        assertTrue(PhelAnnotationUtils.isValidText(largeSymbol))
        assertTrue(PhelAnnotationUtils.isValidText(largeNamespace))

        // Test namespace analysis
        assertTrue(PhelSymbolPositionAnalyzer.hasNamespacePrefix(largeSymbol))
        assertTrue(PhelSymbolPositionAnalyzer.hasNamespacePrefix(largeNamespace))

        val endTime = System.nanoTime()
        val durationMs = (endTime - startTime) / 1_000_000.0

        // Should complete in reasonable time (less than 10ms)
        assertTrue(durationMs < 10.0, "Workflow should be performant with large inputs: ${durationMs}ms")
    }

    @Test
    fun `annotator workflow should handle batch processing`() {
        // Test processing multiple symbols in batch (simulating real IDE usage)
        val symbols = listOf(
            "defn",
            "let",
            "if",
            "fn",
            "do",
            "core\\map",
            "core\\reduce",
            "core\\filter",
            "php/array",
            "php/new",
            "php/->",
            "my-function",
            "test?",
            "valid!",
            "&rest"
        )

        val results = symbols.map { symbol ->
            Triple(
                symbol, PhelAnnotationUtils.isValidText(symbol), PhelSymbolPositionAnalyzer.hasNamespacePrefix(symbol)
            )
        }

        // All symbols should be valid
        results.forEach { (symbol, isValid, _) ->
            assertTrue(isValid, "Symbol should be valid: '$symbol'")
        }

        // Check namespace detection
        val namespacedCount = results.count { it.third }
        val expectedNamespaced = symbols.count { it.contains("\\") && !it.startsWith("\\") && !it.endsWith("\\") }
        assertEquals(expectedNamespaced, namespacedCount, "Namespace detection should be accurate")
    }

    @Test
    fun `annotator workflow should maintain consistency across calls`() {
        // Test that repeated calls produce consistent results
        val testSymbols = listOf("core\\map", "regular-symbol", "php/interop", "test?")

        repeat(5) { iteration ->
            testSymbols.forEach { symbol ->
                val isValid1 = PhelAnnotationUtils.isValidText(symbol)
                val isValid2 = PhelAnnotationUtils.isValidText(symbol)
                val hasNamespace1 = PhelSymbolPositionAnalyzer.hasNamespacePrefix(symbol)
                val hasNamespace2 = PhelSymbolPositionAnalyzer.hasNamespacePrefix(symbol)

                assertEquals(isValid1, isValid2, "Validation should be consistent for '$symbol' (iteration $iteration)")
                assertEquals(
                    hasNamespace1,
                    hasNamespace2,
                    "Namespace detection should be consistent for '$symbol' (iteration $iteration)"
                )
            }
        }
    }

    @Test
    fun `annotator workflow should support real Phel code patterns`() {
        // Test with patterns that would appear in real Phel code
        val realPhelPatterns = listOf(
            // Function definitions
            "defn" to false, "defn-" to false,

            // Core library functions
            "core\\map" to true, "core\\reduce" to true, "core\\filter" to true,

            // PHP interop
            "php/array" to false, "php/new" to false, "php/->" to false,

            // User functions
            "calculate-total" to false, "user\\calculate-total" to true, "utils\\format-date" to true,

            // Predicates and mutators
            "empty?" to false, "nil?" to false, "set!" to false,

            // Special symbols
            "&" to false, "&rest" to false, "..." to false
        )

        realPhelPatterns.forEach { (pattern, expectedNamespaced) ->
            assertTrue(
                PhelAnnotationUtils.isValidText(pattern), "Real Phel pattern should be valid: '$pattern'"
            )
            assertEquals(
                expectedNamespaced,
                PhelSymbolPositionAnalyzer.hasNamespacePrefix(pattern),
                "Namespace detection should be correct for real pattern: '$pattern'"
            )
        }
    }

    @Test
    fun `annotator workflow should integrate with comment analysis`() {
        // Test integration between different analyzer components
        // Note: This tests the public API without complex PSI mocking

        val validSymbols = listOf("test", "core\\map", "php/array")

        validSymbols.forEach { symbol ->
            // All should pass text validation
            assertTrue(PhelAnnotationUtils.isValidText(symbol))

            // Should be able to analyze namespace
            val hasNamespace = PhelSymbolPositionAnalyzer.hasNamespacePrefix(symbol)
            assertTrue(hasNamespace == symbol.contains("\\"))
        }
    }
}
