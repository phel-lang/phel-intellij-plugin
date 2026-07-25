package org.phellang.unit.editor.enter

import com.intellij.openapi.editor.CaretModel
import com.intellij.openapi.editor.Document
import com.intellij.openapi.editor.Editor
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito.anyInt
import org.mockito.Mockito.anyString
import org.mockito.Mockito.never
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import org.phellang.editor.enter.PhelEnterHandlerDocumentProcessor

/**
 * The processor *sets* the new line's indentation rather than adding to it.
 *
 * By the time it runs, the platform's enter handler has already copied the previous line's
 * indentation, so inserting could only ever indent further. Closing a form needs the opposite, which
 * is why enter after `(print "hello"))` used to leave the next line two spaces in.
 */
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
        `when`(mockDocument.getLineNumber(10)).thenReturn(0)
        `when`(mockDocument.getLineStartOffset(0)).thenReturn(0)
        `when`(mockDocument.getLineEndOffset(0)).thenReturn(14)
        `when`(mockDocument.text).thenReturn("(defn test [])")

        val result = processor.extractLineInformation(mockDocument, 10)

        assertEquals(0, result.currentLineNumber)
        assertEquals("(defn test", result.textBeforeCaret)
        assertEquals("(defn test [])", result.currentLineText)
    }

    @Test
    fun `should replace the leading whitespace with the target indentation`() {
        stubCaretLine(FOUR_SPACE_LINE)

        processor.applyIndentationAndParenthesis(mockDocument, mockEditor, CARET_AFTER_FOUR, "  ", false, "")

        verify(mockDocument).replaceString(LINE_START, CARET_AFTER_FOUR, "  ")
        verify(mockCaretModel).moveToOffset(LINE_START + 2)
    }

    /** The case the old insert-only contract could not express: dedent once the form is closed. */
    @Test
    fun `should remove the leading whitespace entirely when the target is empty`() {
        stubCaretLine(FOUR_SPACE_LINE)

        processor.applyIndentationAndParenthesis(mockDocument, mockEditor, CARET_AFTER_FOUR, "", false, "")

        verify(mockDocument).replaceString(LINE_START, CARET_AFTER_FOUR, "")
        verify(mockCaretModel).moveToOffset(LINE_START)
    }

    @Test
    fun `should leave the document alone when the indentation already matches`() {
        stubCaretLine("(defn f []\n  ")

        processor.applyIndentationAndParenthesis(mockDocument, mockEditor, LINE_START + 2, "  ", false, "")

        verify(mockDocument, never()).replaceString(anyInt(), anyInt(), anyString())
        verify(mockCaretModel).moveToOffset(LINE_START + 2)
    }

    @Test
    fun `should append the closing parenthesis after the indentation`() {
        stubCaretLine(FOUR_SPACE_LINE)

        processor.applyIndentationAndParenthesis(mockDocument, mockEditor, CARET_AFTER_FOUR, "  ", true, "\n)")

        verify(mockDocument).replaceString(LINE_START, CARET_AFTER_FOUR, "  ")
        verify(mockDocument).insertString(LINE_START + 2, "\n)")
        verify(mockCaretModel).moveToOffset(LINE_START + 2)
    }

    /** Only the leading run is replaced — text already on the line survives. */
    @Test
    fun `should keep text that follows the leading whitespace`() {
        stubCaretLine("(defn f []\n    (print 1)")

        processor.applyIndentationAndParenthesis(mockDocument, mockEditor, CARET_AFTER_FOUR, "", false, "")

        verify(mockDocument).replaceString(LINE_START, CARET_AFTER_FOUR, "")
    }

    private fun stubCaretLine(text: String) {
        `when`(mockDocument.getLineNumber(anyInt())).thenReturn(1)
        `when`(mockDocument.getLineStartOffset(1)).thenReturn(LINE_START)
        `when`(mockDocument.getLineEndOffset(1)).thenReturn(text.length)
        `when`(mockDocument.text).thenReturn(text)
        `when`(mockEditor.caretModel).thenReturn(mockCaretModel)
    }

    private companion object {
        /** `(defn f []` plus its newline is 11 characters, so the second line starts at 11. */
        const val LINE_START = 11

        const val FOUR_SPACE_LINE = "(defn f []\n    "

        const val CARET_AFTER_FOUR = LINE_START + 4
    }
}
