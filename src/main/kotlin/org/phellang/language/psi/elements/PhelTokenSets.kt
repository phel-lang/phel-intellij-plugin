package org.phellang.language.psi.elements

import com.intellij.psi.tree.TokenSet
import org.phellang.language.psi.PhelTypes

interface PhelTokenSets {
    companion object {
        // Only include token sets for tokens that actually exist in PhelTypes
        val BOOL: TokenSet = TokenSet.create(PhelTypes.BOOL)
        val BRACE1: TokenSet = TokenSet.create(PhelTypes.BRACE1)
        val BRACE2: TokenSet = TokenSet.create(PhelTypes.BRACE2)
        val BRACKET1: TokenSet = TokenSet.create(PhelTypes.BRACKET1)
        val BRACKET2: TokenSet = TokenSet.create(PhelTypes.BRACKET2)
        val CHAR: TokenSet = TokenSet.create(PhelTypes.CHAR)
        val COLON: TokenSet = TokenSet.create(PhelTypes.COLON)
        val COLONCOLON: TokenSet = TokenSet.create(PhelTypes.COLONCOLON)
        val COMMA: TokenSet = TokenSet.create(PhelTypes.COMMA)
        val DOT: TokenSet = TokenSet.create(PhelTypes.DOT)
        val DOTDASH: TokenSet = TokenSet.create(PhelTypes.DOTDASH)
        val HAT: TokenSet = TokenSet.create(PhelTypes.HAT)
        val HEXNUM: TokenSet = TokenSet.create(PhelTypes.HEXNUM)
        val LINE_COMMENT: TokenSet = TokenSet.create(PhelTypes.LINE_COMMENT, PhelTypes.FORM_COMMENT)
        val NAN: TokenSet = TokenSet.create(PhelTypes.NAN)
        val NIL: TokenSet = TokenSet.create(PhelTypes.NIL)
        val NUMBER: TokenSet = TokenSet.create(PhelTypes.NUMBER)
        val PAREN1: TokenSet = TokenSet.create(PhelTypes.PAREN1)
        val PAREN2: TokenSet = TokenSet.create(PhelTypes.PAREN2)
        val QUOTE: TokenSet = TokenSet.create(PhelTypes.QUOTE)
        val STRING: TokenSet = TokenSet.create(PhelTypes.STRING)
        val SYM: TokenSet = TokenSet.create(PhelTypes.SYM)
        val SYNTAX_QUOTE: TokenSet = TokenSet.create(PhelTypes.SYNTAX_QUOTE)
        val TILDE: TokenSet = TokenSet.create(PhelTypes.TILDE)
        val TILDE_AT: TokenSet = TokenSet.create(PhelTypes.TILDE_AT)
    }
}
