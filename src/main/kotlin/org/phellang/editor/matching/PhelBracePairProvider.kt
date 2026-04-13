package org.phellang.editor.matching

import com.intellij.lang.BracePair
import com.intellij.psi.tree.IElementType
import org.phellang.language.psi.PhelTypes.BRACE1
import org.phellang.language.psi.PhelTypes.BRACE2
import org.phellang.language.psi.PhelTypes.BRACKET1
import org.phellang.language.psi.PhelTypes.BRACKET2
import org.phellang.language.psi.PhelTypes.FN_SHORT
import org.phellang.language.psi.PhelTypes.HASH_BRACE
import org.phellang.language.psi.PhelTypes.HASH_PAREN
import org.phellang.language.psi.PhelTypes.PAREN1
import org.phellang.language.psi.PhelTypes.PAREN2
import org.phellang.language.psi.PhelTypes.READER_COND
import org.phellang.language.psi.PhelTypes.READER_COND_SPLICE

class PhelBracePairProvider {

    fun getBracePairs(): Array<BracePair> {
        return arrayOf(
            BracePair(PAREN1, PAREN2, false),             // ()
            BracePair(HASH_PAREN, PAREN2, false),         // #( ... )
            BracePair(FN_SHORT, PAREN2, false),           // |( ... ) (deprecated)
            BracePair(READER_COND, PAREN2, false),        // #?( ... )
            BracePair(READER_COND_SPLICE, PAREN2, false), // #?@( ... )
            BracePair(BRACKET1, BRACKET2, false),         // []
            BracePair(BRACE1, BRACE2, false),             // {}
            BracePair(HASH_BRACE, BRACE2, false)          // #{ ... }
        )
    }

    fun isClosingBrace(elementType: IElementType): Boolean {
        return setOf(PAREN2, BRACKET2, BRACE2).contains(elementType)
    }
}
