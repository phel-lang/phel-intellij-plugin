package org.phellang.editor

import com.intellij.codeInsight.editorActions.enter.EnterHandlerDelegate
import com.intellij.openapi.actionSystem.DataContext
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.actionSystem.EditorActionHandler
import com.intellij.openapi.util.Ref
import com.intellij.psi.PsiFile
import org.phellang.editor.enter.PhelEnterHandlerDocumentProcessor
import org.phellang.editor.enter.PhelEnterHandlerIndentationCalculator
import org.phellang.editor.enter.PhelEnterHandlerParenthesisManager
import org.phellang.editor.enter.PhelEnterHandlerValidator

class PhelEnterHandlerDelegate : EnterHandlerDelegate {

    private val validator = PhelEnterHandlerValidator()
    private val indentationCalculator = PhelEnterHandlerIndentationCalculator()
    private val parenthesisManager = PhelEnterHandlerParenthesisManager()
    private val documentProcessor = PhelEnterHandlerDocumentProcessor()

    override fun preprocessEnter(
        file: PsiFile,
        editor: Editor,
        caretOffsetRef: Ref<Int>,
        caretAdvanceRef: Ref<Int>,
        dataContext: DataContext,
        originalHandler: EditorActionHandler?
    ): EnterHandlerDelegate.Result {
        if (!validator.shouldHandleFile(file)) {
            return EnterHandlerDelegate.Result.Continue
        }

        val document = editor.document
        val caretOffset = caretOffsetRef.get()

        val lineInfo = documentProcessor.extractLineInformation(document, caretOffset)
        
        val indentationSpaces = indentationCalculator.calculateIndentation(
            document, lineInfo.currentLineNumber, lineInfo.textBeforeCaret, lineInfo.currentLineText
        )

        originalHandler?.execute(editor, editor.caretModel.currentCaret, dataContext)

        val caretPosition = editor.caretModel.offset
        val shouldAddClosingParen = parenthesisManager.shouldAddClosingParenthesis(
            document, caretPosition, lineInfo.textBeforeCaret
        )

        val currentIndentationSpaces = indentationCalculator.getCurrentIndentationSpaces(lineInfo.currentLineText)
        val closingParenthesisText = parenthesisManager.createClosingParenthesisText(currentIndentationSpaces)

        documentProcessor.applyIndentationAndParenthesis(
            document, editor, caretPosition, indentationSpaces, shouldAddClosingParen, closingParenthesisText
        )

        return EnterHandlerDelegate.Result.Stop
    }

    override fun postProcessEnter(
        file: PsiFile, editor: Editor, dataContext: DataContext
    ): EnterHandlerDelegate.Result {
        return EnterHandlerDelegate.Result.Continue
    }
}
