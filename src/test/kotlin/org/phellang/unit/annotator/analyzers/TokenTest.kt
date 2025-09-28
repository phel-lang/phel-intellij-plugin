package org.phellang.unit.annotator.analyzers

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import org.phellang.annotator.analyzers.Token
import org.phellang.annotator.analyzers.TokenType

class TokenTest {

    @Test
    fun `Token should be created with all properties`() {
        val token = Token(TokenType.FORM, 0, 10, "test-form")

        assertEquals(TokenType.FORM, token.type)
        assertEquals(0, token.start)
        assertEquals(10, token.end)
        assertEquals("test-form", token.text)
    }

    @Test
    fun `Token should handle different token types`() {
        val formToken = Token(TokenType.FORM, 0, 5, "form")
        val commentToken = Token(TokenType.COMMENT, 5, 7, "#_")

        assertEquals(TokenType.FORM, formToken.type)
        assertEquals(TokenType.COMMENT, commentToken.type)
    }

    @Test
    fun `Token should handle various text ranges`() {
        val tokens = listOf(
            Token(TokenType.FORM, 0, 1, "a"),
            Token(TokenType.FORM, 10, 20, "longer-text"),
            Token(TokenType.COMMENT, 100, 102, "#_"),
            Token(TokenType.FORM, 1000, 2000, "very-long-form-name")
        )

        tokens.forEach { token ->
            assertTrue(token.end > token.start, "End should be greater than start for token: $token")
            assertNotNull(token.text, "Text should not be null for token: $token")
        }
    }

    @Test
    fun `Token should handle edge case ranges`() {
        // Test minimum valid range (length 1)
        val minToken = Token(TokenType.FORM, 0, 1, "x")
        assertEquals(1, minToken.end - minToken.start)

        // Test zero-position start
        val zeroStartToken = Token(TokenType.COMMENT, 0, 2, "#_")
        assertEquals(0, zeroStartToken.start)

        // Test large ranges
        val largeToken = Token(TokenType.FORM, 1000, 2000, "large-form")
        assertEquals(1000, largeToken.end - largeToken.start)
    }

    @Test
    fun `Token should handle empty and special text`() {
        val tokens = listOf(
            Token(TokenType.FORM, 0, 0, ""),
            Token(TokenType.COMMENT, 0, 2, "#_"),
            Token(TokenType.FORM, 0, 10, "namespace\\symbol"),
            Token(TokenType.FORM, 0, 15, "php/interop-call"),
            Token(TokenType.FORM, 0, 5, "test?"),
            Token(TokenType.FORM, 0, 5, "test!"),
            Token(TokenType.FORM, 0, 1, "&")
        )

        tokens.forEach { token ->
            assertNotNull(token.text, "Text should not be null")
            assertTrue(token.start >= 0, "Start should be non-negative")
            assertTrue(token.end >= token.start, "End should be >= start")
        }
    }

    @Test
    fun `Token should support equality comparison`() {
        val token1 = Token(TokenType.FORM, 0, 10, "test")
        val token2 = Token(TokenType.FORM, 0, 10, "test")
        val token3 = Token(TokenType.COMMENT, 0, 10, "test")
        val token4 = Token(TokenType.FORM, 5, 10, "test")

        assertEquals(token1, token2)
        assertNotEquals(token1, token3) // Different type
        assertNotEquals(token1, token4) // Different start position
    }

    @Test
    fun `Token should have consistent hashCode`() {
        val token1 = Token(TokenType.FORM, 0, 10, "test")
        val token2 = Token(TokenType.FORM, 0, 10, "test")

        assertEquals(token1.hashCode(), token2.hashCode())
    }

    @Test
    fun `Token should have meaningful toString`() {
        val formToken = Token(TokenType.FORM, 5, 15, "my-function")
        val commentToken = Token(TokenType.COMMENT, 0, 2, "#_")

        val formString = formToken.toString()
        val commentString = commentToken.toString()

        assertTrue(formString.contains("FORM"), "toString should contain token type")
        assertTrue(formString.contains("5"), "toString should contain start position")
        assertTrue(formString.contains("15"), "toString should contain end position")
        assertTrue(formString.contains("my-function"), "toString should contain text")

        assertTrue(commentString.contains("COMMENT"), "toString should contain token type")
        assertTrue(commentString.contains("#_"), "toString should contain comment marker")
    }

    @ParameterizedTest
    @EnumSource(TokenType::class)
    fun `Token should work with all TokenType values`(tokenType: TokenType) {
        val token = Token(tokenType, 0, 5, "test")

        assertEquals(tokenType, token.type)
        assertNotNull(token.toString())
    }

    @Test
    fun `Token should handle Unicode text`() {
        val unicodeTokens = listOf(
            Token(TokenType.FORM, 0, 4, "测试"),
            Token(TokenType.FORM, 0, 8, "тест-функция"),
            Token(TokenType.FORM, 0, 6, "テスト"),
            Token(TokenType.FORM, 0, 5, "emoji😀")
        )

        unicodeTokens.forEach { token ->
            assertNotNull(token.text)
            assertTrue(token.text.isNotEmpty())
            assertEquals(TokenType.FORM, token.type)
        }
    }

    @Test
    fun `Token should be immutable`() {
        val token = Token(TokenType.FORM, 0, 10, "immutable")

        // All properties should be val (read-only)
        // This is enforced by the compiler, but we can verify the values don't change
        val originalType = token.type
        val originalStart = token.start
        val originalEnd = token.end
        val originalText = token.text

        // After any operations, values should remain the same
        token.toString()
        token.hashCode()
        token.equals(token)

        assertEquals(originalType, token.type)
        assertEquals(originalStart, token.start)
        assertEquals(originalEnd, token.end)
        assertEquals(originalText, token.text)
    }
}
