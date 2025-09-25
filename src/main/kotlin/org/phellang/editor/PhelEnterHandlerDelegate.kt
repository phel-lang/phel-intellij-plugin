package org.phellang.editor

import com.intellij.codeInsight.editorActions.enter.EnterHandlerDelegate
import com.intellij.openapi.actionSystem.DataContext
import com.intellij.openapi.editor.Document
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.actionSystem.EditorActionHandler
import com.intellij.openapi.util.Ref
import com.intellij.psi.PsiFile
import org.phellang.PhelFileType
import org.phellang.editor.indentation.PhelIndentationCalculator

class PhelEnterHandlerDelegate : EnterHandlerDelegate {

    override fun preprocessEnter(
        file: PsiFile,
        editor: Editor,
        caretOffsetRef: Ref<Int>,
        caretAdvanceRef: Ref<Int>,
        dataContext: DataContext,
        originalHandler: EditorActionHandler?
    ): EnterHandlerDelegate.Result {
        // Only handle Phel files
        if (file.fileType != PhelFileType.INSTANCE) {
            return EnterHandlerDelegate.Result.Continue
        }

        val document = editor.document
        val caretOffset = caretOffsetRef.get()

        val currentLineNumber = document.getLineNumber(caretOffset)
        val currentLineStart = document.getLineStartOffset(currentLineNumber)
        val textBeforeCaret = document.text.substring(currentLineStart, caretOffset)
        val currentLineText = document.text.substring(currentLineStart, document.getLineEndOffset(currentLineNumber))

        val indentationCalculator = PhelIndentationCalculator()
        val indentationLevel = indentationCalculator.calculateIndentationLevel(
            document, currentLineNumber, textBeforeCaret
        )

        val currentIndentationSpaces = currentLineText.takeWhile { it.isWhitespace() }.length
        val currentIndentationLevel = currentIndentationSpaces / 2 // Assuming 2 spaces per level

        val additionalIndentationLevel = indentationLevel - currentIndentationLevel
        val indentationSpaces = " ".repeat(maxOf(0, additionalIndentationLevel) * 2) // 2 spaces per level

        originalHandler?.execute(editor, editor.caretModel.currentCaret, dataContext)

        val caretPosition = editor.caretModel.offset
        val shouldAddClosingParen = shouldAddClosingParenthesis(document, caretPosition, textBeforeCaret)

        if (indentationSpaces.isNotEmpty()) {
            document.insertString(caretPosition, indentationSpaces)
            val newCaretPosition = caretPosition + indentationSpaces.length

            if (shouldAddClosingParen) {
                val closingIndent = " ".repeat(currentIndentationSpaces)
                document.insertString(newCaretPosition, "\n$closingIndent)")
            }

            editor.caretModel.moveToOffset(newCaretPosition)
        }

        return EnterHandlerDelegate.Result.Stop
    }

    override fun postProcessEnter(
        file: PsiFile, editor: Editor, dataContext: DataContext
    ): EnterHandlerDelegate.Result {
        return EnterHandlerDelegate.Result.Continue
    }

    private fun shouldAddClosingParenthesis(document: Document, caretPosition: Int, textBeforeCaret: String): Boolean {
        val trimmedText = textBeforeCaret.trimEnd()
        if (!trimmedText.endsWith('(')) {
            return false
        }

        val textAfterCaret = if (caretPosition < document.textLength) {
            document.text.substring(caretPosition).trimStart()
        } else {
            ""
        }

        return !textAfterCaret.startsWith(')')
    }
}
