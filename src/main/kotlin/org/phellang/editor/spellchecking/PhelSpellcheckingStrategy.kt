package org.phellang.editor.spellchecking

import com.intellij.psi.PsiElement
import com.intellij.spellchecker.tokenizer.SpellcheckingStrategy
import com.intellij.spellchecker.tokenizer.Tokenizer
import org.phellang.language.psi.PhelTypes

/**
 * Spellchecks the prose in a Phel file: string literals, which includes docstrings, and comments.
 *
 * Symbols are deliberately left alone. In a Lisp almost every token is a symbol — `defn`, `pos?`,
 * `str/join`, plus the 900-odd names in the registry — and checking them would underline most of a
 * file. Docstrings are where a typo actually escapes, since the plugin renders them in hover
 * documentation and the completion popup, and `phel doc` publishes them.
 */
class PhelSpellcheckingStrategy : SpellcheckingStrategy() {

    override fun getTokenizer(element: PsiElement): Tokenizer<*> = when (element.node?.elementType) {
        PhelTypes.STRING -> TEXT_TOKENIZER
        PhelTypes.LINE_COMMENT -> TEXT_TOKENIZER
        else -> EMPTY_TOKENIZER
    }
}
