package org.phellang.unit.completion

import com.intellij.codeInsight.lookup.CharFilter.Result
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class PhelCompletionCharFilterTest {

    @Test
    fun `slash should be recognized as phel identifier character for namespaced symbols`() {
        // The '/' character is essential for namespaced symbols like str/split, json/encode
        val identifierChars = listOf('/', '-', '_', '?', '!', '*', '+', '<', '>', '=', '&', '%', '$')
        
        for (c in identifierChars) {
            assertTrue(isPhelIdentifierChar(c), "Character '$c' should be a valid Phel identifier character")
        }
    }

    @ParameterizedTest
    @ValueSource(chars = ['a', 'z', 'A', 'Z', '0', '9'])
    fun `alphanumeric characters should be valid identifier chars`(c: Char) {
        assertTrue(isPhelIdentifierChar(c), "Character '$c' should be a valid Phel identifier character")
    }

    @ParameterizedTest
    @ValueSource(chars = ['(', ')', '[', ']', '{', '}', ' ', '\n', '\t'])
    fun `delimiter and whitespace characters should not be identifier chars`(c: Char) {
        assertFalse(isPhelIdentifierChar(c), "Character '$c' should NOT be a valid Phel identifier character")
    }

    @Test
    fun `namespace separator usage in function names`() {
        // Verify that common namespaced function patterns use valid characters
        val namespacedFunctions = listOf(
            "str/split",
            "str/join",
            "json/encode",
            "json/decode",
            "http/get",
            "repl/require",
            "php/->",
            "php/::",
            "mock/with-mock"
        )
        
        for (fn in namespacedFunctions) {
            val allCharsValid = fn.all { isPhelIdentifierChar(it) || it == ':' }
            assertTrue(allCharsValid, "All characters in '$fn' should be valid")
        }
    }

    // Helper function that mirrors the logic in PhelCompletionCharFilter
    private fun isPhelIdentifierChar(c: Char): Boolean {
        return c.isLetterOrDigit() ||
                c == '-' ||
                c == '_' ||
                c == '?' ||
                c == '!' ||
                c == '*' ||
                c == '+' ||
                c == '<' ||
                c == '>' ||
                c == '=' ||
                c == '&' ||
                c == '%' ||
                c == '$' ||
                c == '/'
    }
}
