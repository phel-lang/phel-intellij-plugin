package org.phellang.unit.editor.typing.pairing

import com.intellij.openapi.editor.Document
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.mockito.Mockito.*
import org.phellang.editor.typing.pairing.PhelCharacterPairing

class PhelCharacterPairingTest {

    private lateinit var mockDocument: Document

    @BeforeEach
    fun setUp() {
        mockDocument = mock(Document::class.java)
    }

    @ParameterizedTest
    @CsvSource(
        "'(', ')'", "'[', ']'", "'{', '}'"
    )
    fun `getClosingCharacter should return correct closing character`(opening: Char, expected: Char) {
        val result = PhelCharacterPairing.getClosingCharacter(opening)

        assertEquals(expected, result)
    }

    @ParameterizedTest
    @CsvSource("'a', 'b', 'c', '1', '2', '3', ' ', '\n', '\t'")
    fun `getClosingCharacter should return null for non-opening characters`(c: Char) {
        val result = PhelCharacterPairing.getClosingCharacter(c)

        assertNull(result)
    }

    @ParameterizedTest
    @CsvSource("')', ']', '}'")
    fun `isClosingCharacter should return true for closing characters`(c: Char) {
        val result = PhelCharacterPairing.isClosingCharacter(c)

        assertTrue(result)
    }

    @ParameterizedTest
    @CsvSource("'(', '[', '{', 'a', 'b', 'c', '1', '2', '3'")
    fun `isClosingCharacter should return false for non-closing characters`(c: Char) {
        val result = PhelCharacterPairing.isClosingCharacter(c)

        assertFalse(result)
    }

    @Test
    fun `shouldAutoClose should return true for normal text`() {
        val text = "hello world"
        `when`(mockDocument.charsSequence).thenReturn(text)

        val result = PhelCharacterPairing.shouldAutoClose(mockDocument, 5)

        assertTrue(result)
    }

    @Test
    fun `shouldAutoClose should return false when inside string`() {
        val text = "\"hello world\""
        `when`(mockDocument.charsSequence).thenReturn(text)

        val result = PhelCharacterPairing.shouldAutoClose(mockDocument, 5)

        assertFalse(result)
    }

    @Test
    fun `shouldAutoClose should return false when next character is alphanumeric`() {
        val text = "hello"
        `when`(mockDocument.charsSequence).thenReturn(text)

        val result = PhelCharacterPairing.shouldAutoClose(mockDocument, 0) // Next char is 'h'

        assertFalse(result)
    }

    @Test
    fun `shouldAutoClose should return false when next character is dash`() {
        val text = "test-symbol"
        `when`(mockDocument.charsSequence).thenReturn(text)

        val result = PhelCharacterPairing.shouldAutoClose(mockDocument, 4) // Next char is '-'

        assertFalse(result)
    }

    @Test
    fun `shouldAutoClose should return false when next character is underscore`() {
        val text = "test_symbol"
        `when`(mockDocument.charsSequence).thenReturn(text)

        val result = PhelCharacterPairing.shouldAutoClose(mockDocument, 4) // Next char is '_'

        assertFalse(result)
    }

    @Test
    fun `shouldAutoClose should return true when next character is whitespace`() {
        val text = "hello world"
        `when`(mockDocument.charsSequence).thenReturn(text)

        val result = PhelCharacterPairing.shouldAutoClose(mockDocument, 5) // Next char is ' '

        assertTrue(result)
    }

    @Test
    fun `shouldAutoClose should return true at end of document`() {
        val text = "hello"
        `when`(mockDocument.charsSequence).thenReturn(text)

        val result = PhelCharacterPairing.shouldAutoClose(mockDocument, 5) // At end

        assertTrue(result)
    }

    @Test
    fun `shouldSkipClosingChar should return true when closing char is already present`() {
        val text = "hello)"
        `when`(mockDocument.charsSequence).thenReturn(text)

        val result = PhelCharacterPairing.shouldSkipClosingChar(mockDocument, 5, ')')

        assertTrue(result)
    }

    @Test
    fun `shouldSkipClosingChar should return false when different char is present`() {
        val text = "hello]"
        `when`(mockDocument.charsSequence).thenReturn(text)

        val result = PhelCharacterPairing.shouldSkipClosingChar(mockDocument, 5, ')')

        assertFalse(result)
    }

    @Test
    fun `shouldSkipClosingChar should return false at end of document`() {
        val text = "hello"
        `when`(mockDocument.charsSequence).thenReturn(text)

        val result = PhelCharacterPairing.shouldSkipClosingChar(mockDocument, 5, ')')

        assertFalse(result)
    }

    @Test
    fun `getAllPairableCharacters should return all pairable characters`() {
        val pairableChars = PhelCharacterPairing.getAllPairableCharacters()

        assertEquals(6, pairableChars.size)
        assertTrue(pairableChars.contains('('))
        assertTrue(pairableChars.contains('['))
        assertTrue(pairableChars.contains('{'))
        assertTrue(pairableChars.contains(')'))
        assertTrue(pairableChars.contains(']'))
        assertTrue(pairableChars.contains('}'))
    }

    @Test
    fun `pairing should be consistent across multiple calls`() {
        val result1 = PhelCharacterPairing.getClosingCharacter('(')
        val result2 = PhelCharacterPairing.getClosingCharacter('(')
        val result3 = PhelCharacterPairing.getClosingCharacter('(')

        assertEquals(result1, result2)
        assertEquals(result2, result3)
        assertEquals(')', result1)
    }

    @Test
    fun `shouldAutoClose should handle complex string scenarios`() {
        val testCases = listOf(
            "\"hello \\\"world\\\"\"" to 10, // Inside string with escaped quotes
            "\"first\" \"second\"" to 8,    // Between strings
            "code \"string\" more" to 15    // After string
        )

        testCases.forEach { (text, offset) ->
            `when`(mockDocument.charsSequence).thenReturn(text)

            val result = PhelCharacterPairing.shouldAutoClose(mockDocument, offset)

            // Should not auto-close inside strings
            val isInsideString = offset > 0 && text.take(offset).count { it == '"' } % 2 == 1
            if (isInsideString) {
                assertFalse(result, "Should not auto-close inside string at offset $offset in '$text'")
            }
        }
    }
}
