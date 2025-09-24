package org.phellang

import com.intellij.codeInsight.editorActions.SimpleTokenSetQuoteHandler
import com.intellij.openapi.editor.highlighter.HighlighterIterator
import com.intellij.psi.tree.TokenSet
import org.phellang.language.psi.PhelTypes

class PhelQuoteHandler : SimpleTokenSetQuoteHandler(STRING_LITERALS) {
    override fun isClosingQuote(iterator: HighlighterIterator, offset: Int): Boolean {
        if (STRING_LITERALS.contains(iterator.tokenType)) {
            val end = iterator.end

            return offset == end - 1
        }
        return false
    }

    override fun isOpeningQuote(iterator: HighlighterIterator, offset: Int): Boolean {
        if (STRING_LITERALS.contains(iterator.tokenType)) {
            val start = iterator.start

            return offset == start
        }
        return false
    }
}

private val STRING_LITERALS = TokenSet.create(PhelTypes.STRING)