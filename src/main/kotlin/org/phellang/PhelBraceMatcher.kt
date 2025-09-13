package org.phellang

import com.intellij.lang.BracePair
import com.intellij.lang.PairedBraceMatcher
import com.intellij.psi.PsiFile
import com.intellij.psi.tree.IElementType
import org.phellang.language.psi.PhelTypes

class PhelBraceMatcher : PairedBraceMatcher {
    override fun getPairs(): Array<BracePair?> {
        return PAIRS
    }

    override fun isPairedBracesAllowedBeforeType(lbraceType: IElementType, contextType: IElementType?): Boolean {
        // Allow pairing before most tokens, but not before closing braces
        return contextType !== PhelTypes.PAREN2 && contextType !== PhelTypes.BRACKET2 && contextType !== PhelTypes.BRACE2
    }

    override fun getCodeConstructStart(file: PsiFile, openingBraceOffset: Int): Int {
        return openingBraceOffset
    }
}

private val PAIRS: Array<BracePair?> = arrayOf(
    BracePair(PhelTypes.PAREN1, PhelTypes.PAREN2, false),  // ()
    BracePair(PhelTypes.BRACKET1, PhelTypes.BRACKET2, false),  // []
    BracePair(PhelTypes.BRACE1, PhelTypes.BRACE2, false) // {}
)