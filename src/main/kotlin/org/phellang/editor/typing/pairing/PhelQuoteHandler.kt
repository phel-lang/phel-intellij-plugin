package org.phellang.editor.typing.pairing

import com.intellij.codeInsight.editorActions.TypedHandlerDelegate
import com.intellij.openapi.editor.Document
import com.intellij.openapi.editor.Editor
import org.phellang.editor.typing.context.PhelStringContextAnalyzer

object PhelQuoteHandler {

    fun handleQuoteCharacter(editor: Editor, document: Document, offset: Int): TypedHandlerDelegate.Result {
        val text = document.charsSequence

        // If we're at a quote, skip over it instead of inserting a new pair
        if (shouldSkipExistingQuote(text, offset)) {
            editor.caretModel.moveToOffset(offset + 1)
            return TypedHandlerDelegate.Result.STOP
        }

        // If we should auto-close the quote
        if (shouldAutoCloseQuote(text, offset)) {
            // Insert both opening and closing quote
            document.insertString(offset, "\"\"")
            // Position cursor between them
            editor.caretModel.moveToOffset(offset + 1)
            return TypedHandlerDelegate.Result.STOP
        }

        return TypedHandlerDelegate.Result.CONTINUE
    }

    private fun shouldSkipExistingQuote(text: CharSequence, offset: Int): Boolean {
        // Skip if we're at a quote character
        return offset < text.length && text[offset] == '"'
    }

    private fun shouldAutoCloseQuote(text: CharSequence, offset: Int): Boolean {
        // Don't auto-close if we're at an existing quote
        if (shouldSkipExistingQuote(text, offset)) {
            return false
        }

        // Auto-close if we're not inside a string
        return !PhelStringContextAnalyzer.isInsideString(text, offset)
    }
}
