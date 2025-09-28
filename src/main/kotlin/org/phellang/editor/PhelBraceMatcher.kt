package org.phellang.editor

import com.intellij.lang.BracePair
import com.intellij.lang.PairedBraceMatcher
import com.intellij.psi.PsiFile
import com.intellij.psi.tree.IElementType
import org.phellang.editor.matching.PhelBraceContextAnalyzer
import org.phellang.editor.matching.PhelBracePairProvider

class PhelBraceMatcher : PairedBraceMatcher {

    private val bracePairProvider = PhelBracePairProvider()
    private val contextAnalyzer = PhelBraceContextAnalyzer(bracePairProvider)

    override fun getPairs(): Array<BracePair> {
        return bracePairProvider.getBracePairs()
    }

    override fun isPairedBracesAllowedBeforeType(lbraceType: IElementType, contextType: IElementType?): Boolean {
        return contextAnalyzer.isPairedBracesAllowedBeforeType(contextType)
    }

    override fun getCodeConstructStart(file: PsiFile, openingBraceOffset: Int): Int {
        return openingBraceOffset
    }
}
