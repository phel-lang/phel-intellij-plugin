package org.phellang.integration.editor

import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.spellchecker.tokenizer.SpellcheckingStrategy
import org.phellang.editor.spellchecking.PhelSpellcheckingStrategy
import org.phellang.integration.PhelIntegrationTestCase
import org.phellang.language.psi.PhelTypes

class PhelSpellcheckingStrategyTest : PhelIntegrationTestCase() {

    private val strategy = PhelSpellcheckingStrategy()

    private var fileIndex = 0

    /** The leaf carrying [elementType], as the spellchecker would encounter it. */
    private fun leafOfType(text: String, elementType: Any): PsiElement? {
        val file = myFixture.configureByText("sp${fileIndex++}.phel", text)
        return PsiTreeUtil.collectElements(file) { it.firstChild == null && it.node?.elementType == elementType }
            .firstOrNull()
    }

    private fun tokenizes(element: PsiElement?): Boolean =
        element != null && strategy.getTokenizer(element) !== SpellcheckingStrategy.EMPTY_TOKENIZER

    fun testChecksStringLiterals() {
        assertTrue(tokenizes(leafOfType("(def greeting \"helo wrld\")\n", PhelTypes.STRING)))
    }

    /** Docstrings are string literals, and are the case that matters most: hover renders them. */
    fun testChecksADocstring() {
        val source = "(defn greet\n  \"Returns a greetng.\"\n  [name]\n  name)\n"

        assertTrue(tokenizes(leafOfType(source, PhelTypes.STRING)))
    }

    fun testChecksLineComments() {
        assertTrue(tokenizes(leafOfType("; a coment about things\n(def x 1)\n", PhelTypes.LINE_COMMENT)))
    }

    /**
     * Almost every token in a Lisp is a symbol, so checking them would underline most of a file.
     */
    fun testLeavesSymbolsAlone() {
        assertFalse(tokenizes(leafOfType("(defn greet [nme] nme)\n", PhelTypes.SYM)))
    }

    fun testLeavesKeywordsAlone() {
        assertFalse(tokenizes(leafOfType("(def m {:naem 1})\n", PhelTypes.KEYWORD)))
    }

    fun testLeavesNumbersAlone() {
        assertFalse(tokenizes(leafOfType("(def x 42)\n", PhelTypes.NUMBER)))
    }
}
