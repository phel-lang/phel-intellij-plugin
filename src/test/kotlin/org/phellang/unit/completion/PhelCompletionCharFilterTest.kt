package org.phellang.unit.completion

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.phellang.completion.PhelCompletionCharFilter

class PhelCompletionCharFilterTest {

    private val charFilter = PhelCompletionCharFilter()

    @Test
    fun `special characters should be recognized as phel identifier characters`() {
        val identifierChars = listOf('/', ':', '-', '_', '?', '!', '*', '+', '<', '>', '=', '&', '%', '$')

        for (c in identifierChars) {
            assertTrue(charFilter.isPhelIdentifierChar(c), "Character '$c' should be a valid Phel identifier character")
        }
    }

    @ParameterizedTest
    @ValueSource(chars = ['a', 'z', 'A', 'Z', '0', '9'])
    fun `alphanumeric characters should be valid identifier chars`(c: Char) {
        assertTrue(charFilter.isPhelIdentifierChar(c), "Character '$c' should be a valid Phel identifier character")
    }

    @ParameterizedTest
    @ValueSource(chars = ['(', ')', '[', ']', '{', '}', ' ', '\n', '\t'])
    fun `delimiter and whitespace characters should not be identifier chars`(c: Char) {
        assertFalse(charFilter.isPhelIdentifierChar(c), "Character '$c' should NOT be a valid Phel identifier character")
    }

    @Test
    fun `namespace separator usage in function names`() {
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
            val allCharsValid = fn.all { charFilter.isPhelIdentifierChar(it) }
            assertTrue(allCharsValid, "All characters in '$fn' should be valid")
        }
    }
}
