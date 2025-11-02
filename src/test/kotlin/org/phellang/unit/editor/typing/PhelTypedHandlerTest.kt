package org.phellang.unit.editor.typing

import com.intellij.codeInsight.AutoPopupController
import com.intellij.codeInsight.editorActions.TypedHandlerDelegate
import com.intellij.openapi.editor.CaretModel
import com.intellij.openapi.editor.Document
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.fileTypes.FileType
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.mockito.Mockito.*
import org.phellang.PhelFileType
import org.phellang.editor.PhelTypedHandler

class PhelTypedHandlerTest {

    private lateinit var typedHandler: PhelTypedHandler
    private lateinit var mockProject: Project
    private lateinit var mockEditor: Editor
    private lateinit var mockFile: PsiFile
    private lateinit var mockDocument: Document
    private lateinit var mockCaretModel: CaretModel

    @BeforeEach
    fun setUp() {
        typedHandler = PhelTypedHandler()
        mockProject = mock(Project::class.java)
        mockEditor = mock(Editor::class.java)
        mockFile = mock(PsiFile::class.java)
        mockDocument = mock(Document::class.java)
        mockCaretModel = mock(CaretModel::class.java)

        `when`(mockEditor.document).thenReturn(mockDocument)
        `when`(mockEditor.caretModel).thenReturn(mockCaretModel)
        `when`(mockCaretModel.offset).thenReturn(5)
    }

    @Test
    fun `charTyped should return CONTINUE for non-Phel files`() {
        val nonPhelFileType = mock(FileType::class.java)
        `when`(mockFile.fileType).thenReturn(nonPhelFileType)

        val result = typedHandler.charTyped('(', mockProject, mockEditor, mockFile)

        assertEquals(TypedHandlerDelegate.Result.CONTINUE, result)
    }

    @Test
    fun `charTyped should return CONTINUE for Phel files`() {
        val phelFileType = PhelFileType.INSTANCE
        `when`(mockFile.fileType).thenReturn(phelFileType)

        val mockController = mock(AutoPopupController::class.java)
        mockStatic(AutoPopupController::class.java).use { mockedStatic ->
            mockedStatic.`when`<AutoPopupController> { AutoPopupController.getInstance(mockProject) }
                .thenReturn(mockController)

            val result = typedHandler.charTyped('(', mockProject, mockEditor, mockFile)

            assertEquals(TypedHandlerDelegate.Result.CONTINUE, result)
            verify(mockController).scheduleAutoPopup(mockEditor)
        }
    }

    @Test
    fun `beforeCharTyped should return CONTINUE for non-Phel file types`() {
        val nonPhelFileType = mock(FileType::class.java)

        val result = typedHandler.beforeCharTyped('(', mockProject, mockEditor, mockFile, nonPhelFileType)

        assertEquals(TypedHandlerDelegate.Result.CONTINUE, result)
    }

    @Test
    fun `beforeCharTyped should handle opening parenthesis auto-closing`() {
        val phelFileType = PhelFileType.INSTANCE
        val text = "hello world"
        `when`(mockDocument.charsSequence).thenReturn(text)

        val result = typedHandler.beforeCharTyped('(', mockProject, mockEditor, mockFile, phelFileType)

        assertEquals(TypedHandlerDelegate.Result.STOP, result)
        verify(mockDocument).insertString(5, "()")
        verify(mockCaretModel).moveToOffset(6)
    }

    @Test
    fun `beforeCharTyped should handle opening bracket auto-closing`() {
        val phelFileType = PhelFileType.INSTANCE
        val text = "hello world"
        `when`(mockDocument.charsSequence).thenReturn(text)

        val result = typedHandler.beforeCharTyped('[', mockProject, mockEditor, mockFile, phelFileType)

        assertEquals(TypedHandlerDelegate.Result.STOP, result)
        verify(mockDocument).insertString(5, "[]")
        verify(mockCaretModel).moveToOffset(6)
    }

    @Test
    fun `beforeCharTyped should handle opening brace auto-closing`() {
        val phelFileType = PhelFileType.INSTANCE
        val text = "hello world"
        `when`(mockDocument.charsSequence).thenReturn(text)

        val result = typedHandler.beforeCharTyped('{', mockProject, mockEditor, mockFile, phelFileType)

        assertEquals(TypedHandlerDelegate.Result.STOP, result)
        verify(mockDocument).insertString(5, "{}")
        verify(mockCaretModel).moveToOffset(6)
    }

    @ParameterizedTest
    @ValueSource(chars = [')', ']', '}'])
    fun `beforeCharTyped should skip existing closing characters`(closingChar: Char) {
        val phelFileType = PhelFileType.INSTANCE
        val text = "hello${closingChar}world"
        `when`(mockDocument.charsSequence).thenReturn(text)

        val result = typedHandler.beforeCharTyped(closingChar, mockProject, mockEditor, mockFile, phelFileType)

        assertEquals(TypedHandlerDelegate.Result.STOP, result)
        verify(mockCaretModel).moveToOffset(6) // Skip over the closing character
        verify(mockDocument, never()).insertString(anyInt(), anyString())
    }

    @Test
    fun `beforeCharTyped should not auto-close when inside string`() {
        val phelFileType = PhelFileType.INSTANCE
        val text = "\"hello world\""
        `when`(mockDocument.charsSequence).thenReturn(text)

        val result = typedHandler.beforeCharTyped('(', mockProject, mockEditor, mockFile, phelFileType)

        assertEquals(TypedHandlerDelegate.Result.CONTINUE, result)
        verify(mockDocument, never()).insertString(anyInt(), anyString())
    }

    @Test
    fun `beforeCharTyped should not auto-close when next character is alphanumeric`() {
        val phelFileType = PhelFileType.INSTANCE
        val text = "hello"
        `when`(mockCaretModel.offset).thenReturn(0) // At start, next char is 'h'
        `when`(mockDocument.charsSequence).thenReturn(text)

        val result = typedHandler.beforeCharTyped('(', mockProject, mockEditor, mockFile, phelFileType)

        assertEquals(TypedHandlerDelegate.Result.CONTINUE, result)
        verify(mockDocument, never()).insertString(anyInt(), anyString())
    }

    @Test
    fun `beforeCharTyped should handle quote character auto-closing`() {
        val phelFileType = PhelFileType.INSTANCE
        val text = "hello world"
        `when`(mockDocument.charsSequence).thenReturn(text)

        val result = typedHandler.beforeCharTyped('"', mockProject, mockEditor, mockFile, phelFileType)

        assertEquals(TypedHandlerDelegate.Result.STOP, result)
        verify(mockDocument).insertString(5, "\"\"")
        verify(mockCaretModel).moveToOffset(6)
    }

    @Test
    fun `beforeCharTyped should skip existing quote when not inside string`() {
        val phelFileType = PhelFileType.INSTANCE
        val text = "hello\"world"
        `when`(mockDocument.charsSequence).thenReturn(text)

        val result = typedHandler.beforeCharTyped('"', mockProject, mockEditor, mockFile, phelFileType)

        assertEquals(TypedHandlerDelegate.Result.STOP, result)
        verify(mockCaretModel).moveToOffset(6) // Skip over the quote
        verify(mockDocument, never()).insertString(anyInt(), anyString())
    }

    @Test
    fun `beforeCharTyped should not interfere with quote inside string`() {
        val phelFileType = PhelFileType.INSTANCE
        val text = "\"hello world"
        `when`(mockDocument.charsSequence).thenReturn(text)

        val result = typedHandler.beforeCharTyped('"', mockProject, mockEditor, mockFile, phelFileType)

        assertEquals(TypedHandlerDelegate.Result.CONTINUE, result)
        verify(mockDocument, never()).insertString(anyInt(), anyString())
    }

    @Test
    fun `typed handler should be reusable across multiple calls`() {
        val phelFileType = PhelFileType.INSTANCE
        `when`(mockFile.fileType).thenReturn(phelFileType)

        val mockController = mock(AutoPopupController::class.java)
        mockStatic(AutoPopupController::class.java).use { mockedStatic ->
            mockedStatic.`when`<AutoPopupController> { AutoPopupController.getInstance(mockProject) }
                .thenReturn(mockController)

            val result1 = typedHandler.charTyped('(', mockProject, mockEditor, mockFile)
            val result2 = typedHandler.charTyped('(', mockProject, mockEditor, mockFile)
            val result3 = typedHandler.charTyped('(', mockProject, mockEditor, mockFile)

            assertEquals(result1, result2)
            assertEquals(result2, result3)
            assertEquals(TypedHandlerDelegate.Result.CONTINUE, result1)
        }
    }

    @Test
    fun `beforeCharTyped should handle complex scenarios correctly`() {
        val phelFileType = PhelFileType.INSTANCE
        val text = "\"first\" "
        `when`(mockCaretModel.offset).thenReturn(8) // After space, at end
        `when`(mockDocument.charsSequence).thenReturn(text)

        val result = typedHandler.beforeCharTyped('(', mockProject, mockEditor, mockFile, phelFileType)

        assertEquals(TypedHandlerDelegate.Result.STOP, result)
        verify(mockDocument).insertString(8, "()")
        verify(mockCaretModel).moveToOffset(9)
    }

    @Test
    fun `beforeCharTyped should handle escaped quotes correctly`() {
        val phelFileType = PhelFileType.INSTANCE
        val text = "\"hello \\\"world\\\""
        `when`(mockCaretModel.offset).thenReturn(10) // Inside string with escaped quotes
        `when`(mockDocument.charsSequence).thenReturn(text)

        val result = typedHandler.beforeCharTyped('(', mockProject, mockEditor, mockFile, phelFileType)

        assertEquals(TypedHandlerDelegate.Result.CONTINUE, result)
        verify(mockDocument, never()).insertString(anyInt(), anyString())
    }

    @Test
    fun `typed handler should handle all pairable characters consistently`() {
        val phelFileType = PhelFileType.INSTANCE
        val text = "hello world"
        `when`(mockDocument.charsSequence).thenReturn(text)

        val pairableChars = mapOf(
            '(' to ")", '[' to "]", '{' to "}"
        )

        pairableChars.forEach { (opening, closing) ->
            reset(mockDocument, mockCaretModel)
            `when`(mockDocument.charsSequence).thenReturn(text)
            `when`(mockCaretModel.offset).thenReturn(5)

            val result = typedHandler.beforeCharTyped(opening, mockProject, mockEditor, mockFile, phelFileType)

            assertEquals(TypedHandlerDelegate.Result.STOP, result)
            verify(mockDocument).insertString(5, opening + closing)
            verify(mockCaretModel).moveToOffset(6)
        }
    }
}
