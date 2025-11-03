package org.phellang.unit.editor

import com.intellij.openapi.editor.highlighter.HighlighterIterator
import com.intellij.psi.tree.IElementType
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.phellang.editor.PhelQuoteHandler
import org.phellang.language.psi.PhelTypes

class PhelQuoteHandlerTest {

    private lateinit var quoteHandler: PhelQuoteHandler

    @BeforeEach
    fun setUp() {
        quoteHandler = PhelQuoteHandler()
    }

    @Test
    fun `isClosingQuote should return true for valid closing quote position`() {
        val iterator = Mockito.mock(HighlighterIterator::class.java)
        Mockito.`when`(iterator.tokenType).thenReturn(PhelTypes.STRING)
        Mockito.`when`(iterator.start).thenReturn(0)
        Mockito.`when`(iterator.end).thenReturn(7)

        val result = quoteHandler.isClosingQuote(iterator, 6) // End - 1

        Assertions.assertTrue(result)
    }

    @Test
    fun `isClosingQuote should return false for non-string token`() {
        val iterator = Mockito.mock(HighlighterIterator::class.java)
        val nonStringToken = Mockito.mock(IElementType::class.java)
        Mockito.`when`(iterator.tokenType).thenReturn(nonStringToken)
        Mockito.`when`(iterator.start).thenReturn(0)
        Mockito.`when`(iterator.end).thenReturn(7)

        val result = quoteHandler.isClosingQuote(iterator, 6)

        Assertions.assertFalse(result)
    }

    @Test
    fun `isClosingQuote should return false for invalid position`() {
        val iterator = Mockito.mock(HighlighterIterator::class.java)
        Mockito.`when`(iterator.tokenType).thenReturn(PhelTypes.STRING)
        Mockito.`when`(iterator.start).thenReturn(0)
        Mockito.`when`(iterator.end).thenReturn(7)

        val result = quoteHandler.isClosingQuote(iterator, 5) // Not end - 1

        Assertions.assertFalse(result)
    }

    @Test
    fun `isOpeningQuote should return true for valid opening quote position`() {
        val iterator = Mockito.mock(HighlighterIterator::class.java)
        Mockito.`when`(iterator.tokenType).thenReturn(PhelTypes.STRING)
        Mockito.`when`(iterator.start).thenReturn(5)
        Mockito.`when`(iterator.end).thenReturn(12)

        val result = quoteHandler.isOpeningQuote(iterator, 5) // At start

        Assertions.assertTrue(result)
    }

    @Test
    fun `isOpeningQuote should return false for non-string token`() {
        val iterator = Mockito.mock(HighlighterIterator::class.java)
        val nonStringToken = Mockito.mock(IElementType::class.java)
        Mockito.`when`(iterator.tokenType).thenReturn(nonStringToken)
        Mockito.`when`(iterator.start).thenReturn(5)
        Mockito.`when`(iterator.end).thenReturn(12)

        val result = quoteHandler.isOpeningQuote(iterator, 5)

        Assertions.assertFalse(result)
    }

    @Test
    fun `isOpeningQuote should return false for invalid position`() {
        val iterator = Mockito.mock(HighlighterIterator::class.java)
        Mockito.`when`(iterator.tokenType).thenReturn(PhelTypes.STRING)
        Mockito.`when`(iterator.start).thenReturn(5)
        Mockito.`when`(iterator.end).thenReturn(12)

        val result = quoteHandler.isOpeningQuote(iterator, 6) // Not at start

        Assertions.assertFalse(result)
    }

    @Test
    fun `quote handler should be reusable across multiple calls`() {
        val iterator = Mockito.mock(HighlighterIterator::class.java)
        Mockito.`when`(iterator.tokenType).thenReturn(PhelTypes.STRING)
        Mockito.`when`(iterator.start).thenReturn(0)
        Mockito.`when`(iterator.end).thenReturn(7)

        val result1 = quoteHandler.isClosingQuote(iterator, 6)
        val result2 = quoteHandler.isClosingQuote(iterator, 6)
        val result3 = quoteHandler.isClosingQuote(iterator, 6)

        Assertions.assertEquals(result1, result2)
        Assertions.assertEquals(result2, result3)
        Assertions.assertTrue(result1)
    }

    @Test
    fun `quote handler should handle various token scenarios`() {
        val stringIterator = Mockito.mock(HighlighterIterator::class.java)
        Mockito.`when`(stringIterator.tokenType).thenReturn(PhelTypes.STRING)
        Mockito.`when`(stringIterator.start).thenReturn(0)
        Mockito.`when`(stringIterator.end).thenReturn(10)

        val nonStringIterator = Mockito.mock(HighlighterIterator::class.java)
        val nonStringToken = Mockito.mock(IElementType::class.java)
        Mockito.`when`(nonStringIterator.tokenType).thenReturn(nonStringToken)

        // String token should work
        Assertions.assertTrue(quoteHandler.isOpeningQuote(stringIterator, 0))
        Assertions.assertTrue(quoteHandler.isClosingQuote(stringIterator, 9))

        // Non-string token should not work
        Assertions.assertFalse(quoteHandler.isOpeningQuote(nonStringIterator, 0))
        Assertions.assertFalse(quoteHandler.isClosingQuote(nonStringIterator, 9))
    }

    @Test
    fun `quote handler should integrate with IntelliJ quote handling system`() {
        // Verify that the handler can be used in IntelliJ contexts
        Assertions.assertNotNull(quoteHandler)

        // The handler should be ready for IDE integration
        Assertions.assertDoesNotThrow {
            val iterator = Mockito.mock(HighlighterIterator::class.java)
            Mockito.`when`(iterator.tokenType).thenReturn(PhelTypes.STRING)
            Mockito.`when`(iterator.start).thenReturn(0)
            Mockito.`when`(iterator.end).thenReturn(5)

            quoteHandler.isOpeningQuote(iterator, 0)
            quoteHandler.isClosingQuote(iterator, 4)
        }
    }
}