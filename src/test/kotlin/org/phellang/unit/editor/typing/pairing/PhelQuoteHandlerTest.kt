package org.phellang.unit.editor.typing.pairing

import com.intellij.codeInsight.editorActions.TypedHandlerDelegate
import com.intellij.openapi.editor.CaretModel
import com.intellij.openapi.editor.Document
import com.intellij.openapi.editor.Editor
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.phellang.editor.typing.pairing.PhelQuoteHandler

class PhelQuoteHandlerTest {

    private lateinit var mockEditor: Editor
    private lateinit var mockDocument: Document
    private lateinit var mockCaretModel: CaretModel

    @BeforeEach
    fun setUp() {
        mockEditor = mock(Editor::class.java)
        mockDocument = mock(Document::class.java)
        mockCaretModel = mock(CaretModel::class.java)

        `when`(mockEditor.caretModel).thenReturn(mockCaretModel)
    }

    @Test
    fun `handleQuoteCharacter should skip existing quote when not inside string`() {
        val text = "hello \"world\""
        `when`(mockDocument.charsSequence).thenReturn(text)

        val result = PhelQuoteHandler.handleQuoteCharacter(mockEditor, mockDocument, 6) // At quote

        assertEquals(TypedHandlerDelegate.Result.STOP, result)
        verify(mockCaretModel).moveToOffset(7) // Move past the quote
    }

    @Test
    fun `handleQuoteCharacter should auto-close quote when not inside string`() {
        val text = "hello world"
        `when`(mockDocument.charsSequence).thenReturn(text)

        val result = PhelQuoteHandler.handleQuoteCharacter(mockEditor, mockDocument, 5)

        assertEquals(TypedHandlerDelegate.Result.STOP, result)
        verify(mockDocument).insertString(5, "\"\"")
        verify(mockCaretModel).moveToOffset(6) // Move between quotes
    }

    @Test
    fun `handleQuoteCharacter should continue when inside string`() {
        val text = "\"hello world"
        `when`(mockDocument.charsSequence).thenReturn(text)

        val result = PhelQuoteHandler.handleQuoteCharacter(mockEditor, mockDocument, 5)

        assertEquals(TypedHandlerDelegate.Result.CONTINUE, result)
        verifyNoInteractions(mockCaretModel)
        verify(mockDocument, never()).insertString(anyInt(), anyString())
    }

    @Test
    fun `handleQuoteCharacter should handle escaped quotes correctly`() {
        val text = "\"hello \\\"world\\\""
        `when`(mockDocument.charsSequence).thenReturn(text)

        // Inside string with escaped quotes
        val result = PhelQuoteHandler.handleQuoteCharacter(mockEditor, mockDocument, 10)

        assertEquals(TypedHandlerDelegate.Result.CONTINUE, result)
        verifyNoInteractions(mockCaretModel)
        verify(mockDocument, never()).insertString(anyInt(), anyString())
    }

    @Test
    fun `handleQuoteCharacter should handle multiple strings correctly`() {
        val text = "\"first\" \"second\""
        `when`(mockDocument.charsSequence).thenReturn(text)

        // At opening quote of second string - should skip over it
        val result = PhelQuoteHandler.handleQuoteCharacter(mockEditor, mockDocument, 8)

        assertEquals(TypedHandlerDelegate.Result.STOP, result)
        verify(mockCaretModel).moveToOffset(9) // Skip over the quote
        verify(mockDocument, never()).insertString(anyInt(), anyString())
    }

    @Test
    fun `handleQuoteCharacter should not interfere with document when continuing`() {
        val text = "\"hello world"
        `when`(mockDocument.charsSequence).thenReturn(text)

        val result = PhelQuoteHandler.handleQuoteCharacter(mockEditor, mockDocument, 5)

        assertEquals(TypedHandlerDelegate.Result.CONTINUE, result)
        // Should not modify document or caret when continuing
        verify(mockDocument, never()).insertString(anyInt(), anyString())
        verifyNoInteractions(mockCaretModel)
    }
}
