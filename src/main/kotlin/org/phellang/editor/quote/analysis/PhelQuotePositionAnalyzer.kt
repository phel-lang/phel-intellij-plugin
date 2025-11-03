package org.phellang.editor.quote.analysis

import com.intellij.openapi.editor.highlighter.HighlighterIterator

object PhelQuotePositionAnalyzer {

    fun isClosingQuotePosition(iterator: HighlighterIterator, offset: Int): Boolean {
        val end = iterator.end
        return offset == end - 1
    }

    fun isOpeningQuotePosition(iterator: HighlighterIterator, offset: Int): Boolean {
        val start = iterator.start
        return offset == start
    }

    fun isAtQuoteCharacter(text: CharSequence, offset: Int): Boolean {
        return offset >= 0 && offset < text.length && text[offset] == '"'
    }
}
