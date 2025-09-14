package org.phellang

import com.intellij.codeInsight.editorActions.SimpleTokenSetQuoteHandler
import com.intellij.openapi.editor.highlighter.HighlighterIterator
import com.intellij.psi.tree.TokenSet
import org.phellang.language.psi.PhelTypes

/**
 * Quote handler for Phel language - provides smart quote handling
 * Handles auto-closing and navigation within string literals
 */
class PhelQuoteHandler : SimpleTokenSetQuoteHandler(STRING_LITERALS) {
    override fun isClosingQuote(iterator: HighlighterIterator, offset: Int): Boolean {
        // Check if this quote closes a string literal
        if (STRING_LITERALS.contains(iterator.tokenType)) {
            val end = iterator.end

            // If we're at the end of a string token, this is a closing quote
            return offset == end - 1
        }
        return false
    }

    override fun isOpeningQuote(iterator: HighlighterIterator, offset: Int): Boolean {
        // Check if this quote opens a string literal
        if (STRING_LITERALS.contains(iterator.tokenType)) {
            val start = iterator.start

            // If we're at the start of a string token, this is an opening quote
            return offset == start
        }
        return false
    }
}

private val STRING_LITERALS = TokenSet.create(PhelTypes.STRING)