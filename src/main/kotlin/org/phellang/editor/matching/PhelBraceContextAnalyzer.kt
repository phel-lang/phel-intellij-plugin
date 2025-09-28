package org.phellang.editor.matching

import com.intellij.psi.tree.IElementType

class PhelBraceContextAnalyzer(private val bracePairProvider: PhelBracePairProvider) {

    fun isPairedBracesAllowedBeforeType(contextType: IElementType?): Boolean {
        if (contextType == null) {
            return true
        }

        if (bracePairProvider.isClosingBrace(contextType)) {
            return false
        }

        return true
    }
}
