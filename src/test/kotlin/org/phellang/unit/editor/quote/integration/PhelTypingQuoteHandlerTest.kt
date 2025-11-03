package org.phellang.unit.editor.quote.integration

import com.intellij.codeInsight.editorActions.TypedHandlerDelegate
import com.intellij.openapi.editor.CaretModel
import com.intellij.openapi.editor.Document
import com.intellij.openapi.editor.Editor
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.mockito.Mockito.*
import org.phellang.editor.quote.integration.PhelTypingQuoteHandler

class PhelTypingQuoteHandlerTest {

    @Test
    fun `handleQuoteCharacter should skip existing quote`() {
        val editor = mock(Editor::class.java)
        val document = mock(Document::class.java)
        val caretModel = mock(CaretModel::class.java)
        val text = "hello\"world"

        `when`(editor.caretModel).thenReturn(caretModel)
        `when`(document.charsSequence).thenReturn(text)

        val result = PhelTypingQuoteHandler.handleQuoteCharacter(editor, document, 5)

        assertEquals(TypedHandlerDelegate.Result.STOP, result)
        verify(caretModel).moveToOffset(6)
        verify(document, never()).insertString(anyInt(), anyString())
    }

    @Test
    fun `handleQuoteCharacter should auto-close quote when appropriate`() {
        val editor = mock(Editor::class.java)
        val document = mock(Document::class.java)
        val caretModel = mock(CaretModel::class.java)
        val text = "hello world"

        `when`(editor.caretModel).thenReturn(caretModel)
        `when`(document.charsSequence).thenReturn(text)

        val result = PhelTypingQuoteHandler.handleQuoteCharacter(editor, document, 5)

        assertEquals(TypedHandlerDelegate.Result.STOP, result)
        verify(document).insertString(5, "\"\"")
        verify(caretModel).moveToOffset(6)
    }

    @Test
    fun `handleQuoteCharacter should continue when inside string`() {
        val editor = mock(Editor::class.java)
        val document = mock(Document::class.java)
        val text = "\"hello world\""

        `when`(document.charsSequence).thenReturn(text)

        val result = PhelTypingQuoteHandler.handleQuoteCharacter(editor, document, 5)

        assertEquals(TypedHandlerDelegate.Result.CONTINUE, result)
        verify(document, never()).insertString(anyInt(), anyString())
    }

    @ParameterizedTest
    @CsvSource(
        "'\"hello\"', 0, SKIP", "'hello world', 5, AUTO_CLOSE", "'\"hello world\"', 5, CONTINUE"
    )
    fun `analyzeTypingContext should return correct action`(
        text: String, offset: Int, expectedAction: PhelTypingQuoteHandler.QuoteTypingAction
    ) {
        val result = PhelTypingQuoteHandler.analyzeTypingContext(text, offset)

        assertEquals(expectedAction, result)
    }

    @Test
    fun `typing quote handler should handle complex scenarios`() {
        val editor = mock(Editor::class.java)
        val document = mock(Document::class.java)
        val caretModel = mock(CaretModel::class.java)

        `when`(editor.caretModel).thenReturn(caretModel)

        val testCases = listOf(
            // (text, offset, expectedResult, shouldInsert, shouldMove, moveToOffset)
            Triple("\"hello\"", 0, TypedHandlerDelegate.Result.STOP) to Triple(false, true, 1),
            Triple("hello world", 5, TypedHandlerDelegate.Result.STOP) to Triple(true, true, 6),
            Triple("\"inside string\"", 5, TypedHandlerDelegate.Result.CONTINUE) to Triple(false, false, null)
        )

        testCases.forEach { (params, expectations) ->
            reset(document, caretModel)
            val (text, offset, expectedResult) = params
            val (shouldInsert, shouldMove, moveOffset) = expectations

            `when`(document.charsSequence).thenReturn(text)

            val result = PhelTypingQuoteHandler.handleQuoteCharacter(editor, document, offset)

            assertEquals(expectedResult, result, "Wrong result for '$text' at offset $offset")

            if (shouldInsert) {
                verify(document).insertString(offset, "\"\"")
            } else {
                verify(document, never()).insertString(anyInt(), anyString())
            }

            if (shouldMove && moveOffset != null) {
                verify(caretModel).moveToOffset(moveOffset)
            }
        }
    }

    @Test
    fun `typing quote handler should be consistent across multiple calls`() {
        val text = "hello world"

        val result1 = PhelTypingQuoteHandler.analyzeTypingContext(text, 5)
        val result2 = PhelTypingQuoteHandler.analyzeTypingContext(text, 5)

        assertEquals(result1, result2)
        assertEquals(PhelTypingQuoteHandler.QuoteTypingAction.AUTO_CLOSE, result1)
    }

    @Test
    fun `QuoteTypingAction enum should have all expected values`() {
        val actions = PhelTypingQuoteHandler.QuoteTypingAction.values()

        assertEquals(3, actions.size)
        assertTrue(actions.contains(PhelTypingQuoteHandler.QuoteTypingAction.SKIP))
        assertTrue(actions.contains(PhelTypingQuoteHandler.QuoteTypingAction.AUTO_CLOSE))
        assertTrue(actions.contains(PhelTypingQuoteHandler.QuoteTypingAction.CONTINUE))
    }
}
