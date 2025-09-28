package org.phellang.unit.annotator.analyzers

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.params.provider.ValueSource
import org.phellang.annotator.analyzers.PhelSymbolPositionAnalyzer

class PhelSymbolPositionAnalyzerSimpleTest {

    @ParameterizedTest
    @CsvSource(
        "'namespace\\symbol', true",
        "'core\\map', true",
        "'phel\\str', true",
        "'my-ns\\func', true",
        "'a\\b\\c', true",
        "'complex\\nested\\namespace\\symbol', true"
    )
    fun `hasNamespacePrefix should return true for valid namespace prefixes`(text: String, expected: Boolean) {
        assertEquals(expected, PhelSymbolPositionAnalyzer.hasNamespacePrefix(text))
    }

    @ParameterizedTest
    @ValueSource(strings = ["\\symbol", "symbol\\", "symbol", "no-namespace", "", "\\", "\\\\"])
    fun `hasNamespacePrefix should return false for invalid namespace prefixes`(text: String) {
        assertFalse(PhelSymbolPositionAnalyzer.hasNamespacePrefix(text))
    }

    @Test
    fun `hasNamespacePrefix should handle special characters in namespace names`() {
        // Test namespace names with hyphens and underscores
        assertTrue(PhelSymbolPositionAnalyzer.hasNamespacePrefix("my-namespace\\symbol"))
        assertTrue(PhelSymbolPositionAnalyzer.hasNamespacePrefix("my_namespace\\symbol"))
        assertTrue(PhelSymbolPositionAnalyzer.hasNamespacePrefix("ns-1\\func-2"))

        // Test multiple namespace levels
        assertTrue(PhelSymbolPositionAnalyzer.hasNamespacePrefix("level1\\level2\\symbol"))
        assertTrue(PhelSymbolPositionAnalyzer.hasNamespacePrefix("a\\b\\c\\d\\e"))
    }

    @Test
    fun `hasNamespacePrefix should be consistent with Phel namespace syntax`() {
        // These should match the Phel namespace syntax patterns
        val validNamespaces = listOf(
            "core\\map", "phel\\str", "user\\my-function", "project\\utils\\helper", "test-ns\\test-func"
        )

        validNamespaces.forEach { namespace ->
            assertTrue(
                PhelSymbolPositionAnalyzer.hasNamespacePrefix(namespace),
                "Should recognize '$namespace' as having namespace prefix"
            )
        }
    }

    @Test
    fun `hasNamespacePrefix should reject malformed namespace patterns`() {
        val invalidPatterns = listOf(
            "\\leading-backslash", "trailing-backslash\\", "no-namespace", "", " ", "\\", "just\\", "\\just"
        )

        invalidPatterns.forEach { pattern ->
            assertFalse(
                PhelSymbolPositionAnalyzer.hasNamespacePrefix(pattern),
                "Should reject '$pattern' as invalid namespace pattern"
            )
        }

        // Test double backslash separately (this is actually valid in Phel namespace syntax)
        // "double\\backslash" contains a backslash not at start/end, so it's valid
        assertTrue(PhelSymbolPositionAnalyzer.hasNamespacePrefix("double\\backslash"))
    }

    @Test
    fun `hasNamespacePrefix should handle performance with large strings`() {
        // Test performance with larger namespace strings
        val largeNamespaces = listOf(
            "very\\long\\nested\\namespace\\structure\\with\\many\\levels\\symbol",
            "a".repeat(100) + "\\" + "b".repeat(100),
            "short\\${"level\\".repeat(50)}symbol"
        )

        largeNamespaces.forEach { namespace ->
            assertTrue(
                PhelSymbolPositionAnalyzer.hasNamespacePrefix(namespace),
                "Should handle large namespace: ${namespace.take(50)}..."
            )
        }
    }

    @Test
    fun `hasNamespacePrefix should handle Unicode characters`() {
        // Test with Unicode characters in namespace names
        val unicodeNamespaces = listOf(
            "测试\\symbol", "тест\\функция", "テスト\\シンボル", "emoji😀\\test"
        )

        unicodeNamespaces.forEach { namespace ->
            assertTrue(
                PhelSymbolPositionAnalyzer.hasNamespacePrefix(namespace), "Should handle Unicode namespace: $namespace"
            )
        }
    }

    @Test
    fun `hasNamespacePrefix should be consistent across multiple calls`() {
        val testCases = listOf(
            "namespace\\symbol" to true, "no-namespace" to false, "\\invalid" to false, "valid\\test" to true
        )

        // Test multiple times to ensure consistency
        repeat(3) {
            testCases.forEach { (input, expected) ->
                assertEquals(
                    expected, PhelSymbolPositionAnalyzer.hasNamespacePrefix(input), "Should be consistent for: $input"
                )
            }
        }
    }
}
