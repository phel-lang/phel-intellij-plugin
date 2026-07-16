package org.phellang.language.psi.elements

import com.intellij.psi.tree.TokenSet
import org.phellang.language.psi.PhelTypes

object PhelTokenSets {
    val LINE_COMMENT: TokenSet = TokenSet.create(PhelTypes.LINE_COMMENT, PhelTypes.FORM_COMMENT)
    val SYM: TokenSet = TokenSet.create(PhelTypes.SYM)
}
