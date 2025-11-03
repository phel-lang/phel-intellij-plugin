package org.phellang.editor

import com.intellij.codeInsight.editorActions.SimpleTokenSetQuoteHandler
import com.intellij.openapi.editor.highlighter.HighlighterIterator
import com.intellij.psi.tree.TokenSet.create
import org.phellang.editor.quote.analysis.PhelQuotePositionAnalyzer
import org.phellang.language.psi.PhelTypes.STRING

class PhelQuoteHandler : SimpleTokenSetQuoteHandler(create(STRING)) {

    override fun isClosingQuote(iterator: HighlighterIterator, offset: Int): Boolean {
        if (!shouldPerformQuoteOperation(iterator, offset)) {
            return false
        }

        return PhelQuotePositionAnalyzer.isClosingQuotePosition(iterator, offset)
    }

    override fun isOpeningQuote(iterator: HighlighterIterator, offset: Int): Boolean {
        if (!shouldPerformQuoteOperation(iterator, offset)) {
            return false
        }

        return PhelQuotePositionAnalyzer.isOpeningQuotePosition(iterator, offset)
    }

    private fun shouldPerformQuoteOperation(iterator: HighlighterIterator, offset: Int): Boolean {
        return isValidQuotePosition(iterator, offset)
    }

    private fun isValidQuotePosition(iterator: HighlighterIterator, offset: Int): Boolean {
        return create(STRING).contains(iterator.tokenType) && offset >= iterator.start && offset < iterator.end
    }
}