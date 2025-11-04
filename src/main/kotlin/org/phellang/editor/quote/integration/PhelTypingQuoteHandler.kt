package org.phellang.editor.quote.integration

import com.intellij.codeInsight.editorActions.TypedHandlerDelegate
import com.intellij.openapi.editor.Document
import com.intellij.openapi.editor.Editor
import org.phellang.editor.quote.analysis.PhelQuotePositionAnalyzer
import org.phellang.editor.typing.context.PhelStringContextAnalyzer

object PhelTypingQuoteHandler {

    fun handleQuoteCharacter(editor: Editor, document: Document, offset: Int): TypedHandlerDelegate.Result {
        val text = document.charsSequence

        // If we're at a quote, skip over it instead of inserting a new pair
        if (shouldSkipExistingQuote(text, offset)) {
            skipExistingQuote(editor, offset)
            return TypedHandlerDelegate.Result.STOP
        }

        // If we should auto-close the quote
        if (shouldAutoCloseQuote(text, offset)) {
            insertQuotePair(editor, document, offset)
            return TypedHandlerDelegate.Result.STOP
        }

        return TypedHandlerDelegate.Result.CONTINUE
    }

    private fun shouldSkipExistingQuote(text: CharSequence, offset: Int): Boolean {
        return PhelQuotePositionAnalyzer.isAtQuoteCharacter(text, offset)
    }

    private fun skipExistingQuote(editor: Editor, offset: Int) {
        editor.caretModel.moveToOffset(offset + 1)
    }

    private fun insertQuotePair(editor: Editor, document: Document, offset: Int) {
        document.insertString(offset, "\"\"")
        editor.caretModel.moveToOffset(offset + 1)
    }

    fun analyzeTypingContext(text: CharSequence, offset: Int): QuoteTypingAction {
        return when {
            shouldSkipExistingQuote(text, offset) -> QuoteTypingAction.SKIP
            shouldAutoCloseQuote(text, offset) -> QuoteTypingAction.AUTO_CLOSE
            else -> QuoteTypingAction.CONTINUE
        }
    }

    private fun shouldAutoCloseQuote(text: CharSequence, offset: Int): Boolean {
        // Don't auto-close if we're at an existing quote
        if (shouldSkipExistingQuote(text, offset)) {
            return false
        }

        // Auto-close if we're not inside a string
        return !PhelStringContextAnalyzer.isInsideString(text, offset)
    }

    enum class QuoteTypingAction {
        SKIP,       // Skip over existing quote
        AUTO_CLOSE, // Insert quote pair and position cursor
        CONTINUE,   // Let other handlers process the character
    }
}
