package org.phellang.unit.editor.quote.analysis

import com.intellij.openapi.editor.highlighter.HighlighterIterator
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.mockito.Mockito.*
import org.phellang.editor.quote.analysis.PhelQuotePositionAnalyzer

class PhelQuotePositionAnalyzerTest {

    @Test
    fun `isClosingQuotePosition should return true when offset is at end minus one`() {
        val iterator = mock(HighlighterIterator::class.java)
        `when`(iterator.end).thenReturn(10)

        val result = PhelQuotePositionAnalyzer.isClosingQuotePosition(iterator, 9)

        assertTrue(result)
    }

    @Test
    fun `isClosingQuotePosition should return false when offset is not at end minus one`() {
        val iterator = mock(HighlighterIterator::class.java)
        `when`(iterator.end).thenReturn(10)

        val result = PhelQuotePositionAnalyzer.isClosingQuotePosition(iterator, 8)

        assertFalse(result)
    }

    @Test
    fun `isOpeningQuotePosition should return true when offset equals start`() {
        val iterator = mock(HighlighterIterator::class.java)
        `when`(iterator.start).thenReturn(5)

        val result = PhelQuotePositionAnalyzer.isOpeningQuotePosition(iterator, 5)

        assertTrue(result)
    }

    @Test
    fun `isOpeningQuotePosition should return false when offset does not equal start`() {
        val iterator = mock(HighlighterIterator::class.java)
        `when`(iterator.start).thenReturn(5)

        val result = PhelQuotePositionAnalyzer.isOpeningQuotePosition(iterator, 6)

        assertFalse(result)
    }

    @ParameterizedTest
    @CsvSource(
        "'\"hello\"', 0, true", "'\"hello\"', 6, true", "'hello', 0, false", "'hello', 2, false"
    )
    fun `isAtQuoteCharacter should correctly identify quote characters`(text: String, offset: Int, expected: Boolean) {
        val result = PhelQuotePositionAnalyzer.isAtQuoteCharacter(text, offset)

        assertEquals(expected, result)
    }

    @Test
    fun `isAtQuoteCharacter should return false for out of bounds offset`() {
        val text = "hello"

        val result = PhelQuotePositionAnalyzer.isAtQuoteCharacter(text, 10)

        assertFalse(result)
    }

    @Test
    fun `position analyzer should be consistent across multiple calls`() {
        val text = "\"test\""

        val result1 = PhelQuotePositionAnalyzer.isAtQuoteCharacter(text, 0)
        val result2 = PhelQuotePositionAnalyzer.isAtQuoteCharacter(text, 0)

        assertEquals(result1, result2)
        assertTrue(result1)
    }

    @Test
    fun `position analyzer should handle empty text gracefully`() {
        val text = ""

        val result = PhelQuotePositionAnalyzer.isAtQuoteCharacter(text, 0)

        assertFalse(result)
    }
}
