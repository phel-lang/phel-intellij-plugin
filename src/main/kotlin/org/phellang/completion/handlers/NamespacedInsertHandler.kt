package org.phellang.completion.handlers

import com.intellij.codeInsight.completion.InsertHandler
import com.intellij.codeInsight.completion.InsertionContext
import com.intellij.codeInsight.lookup.LookupElement
import org.phellang.core.utils.PhelErrorHandler

class NamespacedInsertHandler : InsertHandler<LookupElement?> {
    override fun handleInsert(context: InsertionContext, item: LookupElement) {
        PhelErrorHandler.safeOperation {
            val editor = context.editor
            val document = editor.document
            val caretOffset = context.startOffset

            var symbolStart = caretOffset
            val text = document.charsSequence

            // Move back to find the beginning of the symbol
            while (symbolStart > 0) {
                val c = text[symbolStart - 1]
                // Stop at whitespace, opening parenthesis, or other delimiters
                if (Character.isWhitespace(c) || c == '(' || c == '[' || c == '{') {
                    break
                }
                symbolStart--
            }

            val symbolEnd = context.tailOffset
            document.replaceString(symbolStart, symbolEnd, item.lookupString)

            editor.caretModel.moveToOffset(symbolStart + item.lookupString.length)
        }
    }
}
