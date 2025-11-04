package org.phellang.integration.editor

import com.intellij.codeInsight.editorActions.TypedHandlerDelegate
import com.intellij.openapi.editor.CaretModel
import com.intellij.openapi.editor.Document
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.highlighter.HighlighterIterator
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mockito
import org.phellang.editor.PhelQuoteHandler
import org.phellang.editor.quote.analysis.PhelQuotePositionAnalyzer
import org.phellang.editor.quote.integration.PhelTypingQuoteHandler
import org.phellang.language.psi.PhelTypes

class PhelQuoteHandlingIntegrationTest {

    @Test
    fun `complete quote handling workflow should work end-to-end`() {
        // Test the complete integration between all quote handling components
        val editor = Mockito.mock(Editor::class.java)
        val document = Mockito.mock(Document::class.java)
        val caretModel = Mockito.mock(CaretModel::class.java)
        val iterator = Mockito.mock(HighlighterIterator::class.java)

        Mockito.`when`(editor.caretModel).thenReturn(caretModel)
        Mockito.`when`(iterator.tokenType).thenReturn(PhelTypes.STRING)
        Mockito.`when`(iterator.start).thenReturn(0)
        Mockito.`when`(iterator.end).thenReturn(7)

        // Test scenario: Auto-closing quotes in normal text
        val text = "hello world"
        Mockito.`when`(document.charsSequence).thenReturn(text)

        // 1. Analyze the context
        val action = PhelTypingQuoteHandler.analyzeTypingContext(text, 5)
        Assertions.assertEquals(PhelTypingQuoteHandler.QuoteTypingAction.AUTO_CLOSE, action)

        // 2. Handle the quote character
        val result = PhelTypingQuoteHandler.handleQuoteCharacter(editor, document, 5)
        Assertions.assertEquals(TypedHandlerDelegate.Result.STOP, result)

        // 3. Verify the operations were performed
        Mockito.verify(document).insertString(5, "\"\"")
        Mockito.verify(caretModel).moveToOffset(6)
    }

    @Test
    fun `quote handling components should work together for skipping`() {
        val editor = Mockito.mock(Editor::class.java)
        val document = Mockito.mock(Document::class.java)
        val caretModel = Mockito.mock(CaretModel::class.java)

        Mockito.`when`(editor.caretModel).thenReturn(caretModel)

        // Test scenario: Skipping existing quote
        val text = "hello\"world"
        Mockito.`when`(document.charsSequence).thenReturn(text)

        // Position analyzer should detect quote
        Assertions.assertTrue(PhelQuotePositionAnalyzer.isAtQuoteCharacter(text, 5))

        // Integration handler should skip
        val result = PhelTypingQuoteHandler.handleQuoteCharacter(editor, document, 5)
        Assertions.assertEquals(TypedHandlerDelegate.Result.STOP, result)

        // Verify skip operation
        Mockito.verify(caretModel).moveToOffset(6)
        Mockito.verify(document, Mockito.never()).insertString(ArgumentMatchers.anyInt(), ArgumentMatchers.anyString())
    }

    @Test
    fun `quote handling should respect string context`() {
        val editor = Mockito.mock(Editor::class.java)
        val document = Mockito.mock(Document::class.java)

        // Test scenario: Inside string - should continue
        val text = "\"hello world\""
        Mockito.`when`(document.charsSequence).thenReturn(text)

        val result = PhelTypingQuoteHandler.handleQuoteCharacter(editor, document, 5)

        Assertions.assertEquals(TypedHandlerDelegate.Result.CONTINUE, result)
        Mockito.verify(document, Mockito.never()).insertString(ArgumentMatchers.anyInt(), ArgumentMatchers.anyString())
    }

    @Test
    fun `main quote handler should integrate with token analysis`() {
        val quoteHandler = PhelQuoteHandler()
        val iterator = Mockito.mock(HighlighterIterator::class.java)

        // Test with string token
        Mockito.`when`(iterator.tokenType).thenReturn(PhelTypes.STRING)
        Mockito.`when`(iterator.start).thenReturn(0)
        Mockito.`when`(iterator.end).thenReturn(10)

        // Should work with valid positions
        Assertions.assertTrue(quoteHandler.isOpeningQuote(iterator, 0))
        Assertions.assertTrue(quoteHandler.isClosingQuote(iterator, 9))

        // Should not work with invalid positions
        Assertions.assertFalse(quoteHandler.isOpeningQuote(iterator, 1))
        Assertions.assertFalse(quoteHandler.isClosingQuote(iterator, 8))
    }

    @Test
    fun `quote handling should maintain performance with large text`() {
        val largeText = "\"" + "a".repeat(10000) + "\""
        val document = Mockito.mock(Document::class.java)

        Mockito.`when`(document.charsSequence).thenReturn(largeText)

        // Should handle large strings efficiently
        val startTime = System.currentTimeMillis()

        repeat(100) {
            PhelTypingQuoteHandler.analyzeTypingContext(largeText, 5000)
        }

        val endTime = System.currentTimeMillis()
        val duration = endTime - startTime

        // Should complete within reasonable time (less than 1 second for 100 iterations)
        Assertions.assertTrue(duration < 1000, "Quote handling should be performant with large text")
    }

    @Test
    fun `quote handling integration should support all typing scenarios`() {
        data class TestScenario(
            val description: String,
            val text: String,
            val offset: Int,
            val expectedAction: PhelTypingQuoteHandler.QuoteTypingAction
        )

        val testScenarios = listOf(
            TestScenario("Skip existing quote", "\"hello\"", 0, PhelTypingQuoteHandler.QuoteTypingAction.SKIP),
            TestScenario(
                "Auto-close in normal text", "hello world", 5, PhelTypingQuoteHandler.QuoteTypingAction.AUTO_CLOSE
            ),
            TestScenario(
                "Continue inside string", "\"hello world\"", 5, PhelTypingQuoteHandler.QuoteTypingAction.CONTINUE
            ),
            TestScenario("Skip at end quote", "\"hello\"", 6, PhelTypingQuoteHandler.QuoteTypingAction.SKIP),
            TestScenario("Auto-close at end of text", "hello", 5, PhelTypingQuoteHandler.QuoteTypingAction.AUTO_CLOSE)
        )

        testScenarios.forEach { scenario ->
            val action = PhelTypingQuoteHandler.analyzeTypingContext(scenario.text, scenario.offset)
            Assertions.assertEquals(scenario.expectedAction, action, "Failed scenario: ${scenario.description}")
        }
    }
}
