package org.phellang.unit.editor.enter

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import org.phellang.editor.enter.PhelEnterHandlerDocumentProcessor
import com.intellij.openapi.editor.Document
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.CaretModel

@ExtendWith(MockitoExtension::class)
class PhelEnterHandlerDocumentProcessorTest {

    @Mock
    private lateinit var mockDocument: Document

    @Mock
    private lateinit var mockEditor: Editor

    @Mock
    private lateinit var mockCaretModel: CaretModel

    private lateinit var processor: PhelEnterHandlerDocumentProcessor

    @BeforeEach
    fun setUp() {
        processor = PhelEnterHandlerDocumentProcessor()
    }

    @Test
    fun `should extract line information correctly`() {
        val caretOffset = 10

        `when`(mockDocument.getLineNumber(caretOffset)).thenReturn(0)
        `when`(mockDocument.getLineStartOffset(0)).thenReturn(0)
        `when`(mockDocument.getLineEndOffset(0)).thenReturn(14)
        `when`(mockDocument.text).thenReturn("(defn test [])")

        val result = processor.extractLineInformation(mockDocument, caretOffset)

        assertEquals(0, result.currentLineNumber)
        assertEquals("(defn test", result.textBeforeCaret)
        assertEquals("(defn test [])", result.currentLineText)
    }

    @Test
    fun `should apply indentation without closing parenthesis`() {
        val caretPosition = 20
        val indentationSpaces = "  "

        `when`(mockEditor.caretModel).thenReturn(mockCaretModel)

        processor.applyIndentationAndParenthesis(
            mockDocument, mockEditor, caretPosition, indentationSpaces, false, ""
        )

        verify(mockDocument).insertString(caretPosition, indentationSpaces)
        verify(mockCaretModel).moveToOffset(caretPosition + indentationSpaces.length)
    }

    @Test
    fun `should apply indentation with closing parenthesis`() {
        val caretPosition = 20
        val indentationSpaces = "  "
        val closingParenthesisText = "\n)"

        `when`(mockEditor.caretModel).thenReturn(mockCaretModel)

        processor.applyIndentationAndParenthesis(
            mockDocument, mockEditor, caretPosition, indentationSpaces, true, closingParenthesisText
        )

        val newCaretPosition = caretPosition + indentationSpaces.length
        verify(mockDocument).insertString(caretPosition, indentationSpaces)
        verify(mockDocument).insertString(newCaretPosition, closingParenthesisText)
        verify(mockCaretModel).moveToOffset(newCaretPosition)
    }

    @Test
    fun `should not apply anything when indentation is empty`() {
        val caretPosition = 20
        val indentationSpaces = ""

        processor.applyIndentationAndParenthesis(
            mockDocument, mockEditor, caretPosition, indentationSpaces, false, ""
        )

        verify(mockDocument, never()).insertString(anyInt(), anyString())
        verify(mockEditor, never()).caretModel
    }
}
