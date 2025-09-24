package org.phellang

import com.intellij.codeInsight.editorActions.enter.EnterHandlerDelegate
import com.intellij.openapi.actionSystem.DataContext
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.actionSystem.EditorActionHandler
import com.intellij.openapi.util.Ref
import com.intellij.psi.PsiFile
import org.phellang.editor.PhelEnterPatternDetector
import org.phellang.editor.PhelIndentationCalculator
import org.phellang.editor.PhelParenthesesMover

class PhelEnterHandlerDelegate : EnterHandlerDelegate {

    override fun preprocessEnter(
        file: PsiFile,
        editor: Editor,
        caretOffsetRef: Ref<Int>,
        caretAdvance: Ref<Int>,
        dataContext: DataContext,
        originalHandler: EditorActionHandler?
    ): EnterHandlerDelegate.Result {
        if (file.fileType !is PhelFileType) {
            return EnterHandlerDelegate.Result.Continue
        }

        val document = editor.document
        val caretOffset = caretOffsetRef.get()
        val text = document.charsSequence

        if (PhelEnterPatternDetector.shouldHandleSmartEnter(text, caretOffset)) {
            val lineStart = document.getLineStartOffset(document.getLineNumber(caretOffset))
            val lineEnd = document.getLineEndOffset(document.getLineNumber(caretOffset))
            val currentLine = text.substring(lineStart, lineEnd)

            val currentIndent = PhelIndentationCalculator.getIndentSize(currentLine)
            val newIndentSpaces = PhelIndentationCalculator.getNextIndentLevel(currentIndent)
            val newIndent = PhelIndentationCalculator.createIndent(newIndentSpaces)
            val originalIndent = PhelIndentationCalculator.createIndent(currentIndent)

            val insertText = "\n${newIndent}\n${originalIndent})"

            document.insertString(caretOffset, insertText)

            editor.caretModel.moveToOffset(caretOffset + 1 + newIndentSpaces)

            return EnterHandlerDelegate.Result.Stop
        }

        return EnterHandlerDelegate.Result.Continue
    }

    override fun postProcessEnter(
        file: PsiFile, editor: Editor, dataContext: DataContext
    ): EnterHandlerDelegate.Result {
        if (file.fileType !is PhelFileType) {
            return EnterHandlerDelegate.Result.Continue
        }

        val document = editor.document
        val caretOffset = editor.caretModel.offset
        val text = document.charsSequence

        if (PhelEnterPatternDetector.shouldHandlePostEnter(text, caretOffset, document)) {
            val currentLineNum = document.getLineNumber(caretOffset)

            val prevLineNum = currentLineNum - 1
            val prevLineStart = document.getLineStartOffset(prevLineNum)
            val prevLineEnd = document.getLineEndOffset(prevLineNum)
            val prevLine = text.substring(prevLineStart, prevLineEnd)
            val baseIndent = PhelIndentationCalculator.getIndentSize(prevLine)
            val targetIndent = PhelIndentationCalculator.getNextIndentLevel(baseIndent)
            val indentText = PhelIndentationCalculator.createIndent(targetIndent)

            if (PhelParenthesesMover.moveClosingParenthesesFromNextLine(editor, document, caretOffset, indentText)) {
                return EnterHandlerDelegate.Result.Stop
            }

            document.insertString(caretOffset, indentText)
            editor.caretModel.moveToOffset(caretOffset + targetIndent)

            return EnterHandlerDelegate.Result.Stop
        }

        return EnterHandlerDelegate.Result.Continue
    }
}
