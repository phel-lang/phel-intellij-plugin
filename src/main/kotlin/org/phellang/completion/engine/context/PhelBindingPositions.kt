package org.phellang.completion.engine.context

import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import org.phellang.language.psi.PhelList
import org.phellang.language.psi.PhelVec
import org.phellang.language.psi.analysis.PhelSymbolAnalyzer
import org.phellang.language.psi.utils.SymbolCategory

/**
 * The caret sitting inside a vector that *declares* names — a parameter list or a binding vector.
 *
 * As with definition names, the user is introducing a name rather than referring to one, so
 * completion has nothing useful to offer.
 */
internal object PhelBindingPositions {

    private val MULTI_ARITY_FORMS = setOf("defn", "defn-", "defmacro", "defmacro-")

    /** `(fn [<caret>] ...)` and the `defn` family's parameter vectors. */
    fun isParameterVector(element: PsiElement): Boolean {
        val vector = PsiTreeUtil.getParentOfType(element, PhelVec::class.java) ?: return false
        val list = PsiTreeUtil.getParentOfType(vector, PhelList::class.java) ?: return false

        val children = list.children
        val head = PhelFormHead.symbolTextOf(children.firstOrNull()) ?: return false

        return when {
            head == "fn" -> children.size >= 2 && children[1] === vector
            head in MULTI_ARITY_FORMS -> isDeclaredParameterVector(children, vector)
            else -> false
        }
    }

    /**
     * For the `defn` family the name occupies slot 1, so parameters start at slot 2. Only the first
     * vector counts: a later one is a value inside the body, not a parameter list.
     */
    private fun isDeclaredParameterVector(children: Array<PsiElement>, vector: PhelVec): Boolean {
        for (i in 2 until children.size) {
            if (children[i] === vector) return true
            if (children[i] is PhelVec) return false
        }

        return false
    }

    /** `(let [<caret> 1] ...)` — the name half of a binding pair, not the value half. */
    fun isBindingName(element: PsiElement): Boolean {
        val vector = PsiTreeUtil.getParentOfType(element, PhelVec::class.java) ?: return false
        val list = PsiTreeUtil.getParentOfType(vector, PhelList::class.java) ?: return false

        val children = list.children
        if (children.size < 2) return false

        val head = PhelFormHead.symbolTextOf(children[0]) ?: return false
        if (!PhelSymbolAnalyzer.isSymbolType(head, SymbolCategory.CONTROL_FLOW)) return false
        if (children[1] !== vector) return false

        return isNameHalfOfAPair(element, vector)
    }

    /** Bindings are name/value pairs, so the names are the even-indexed entries. */
    private fun isNameHalfOfAPair(element: PsiElement, vector: PhelVec): Boolean {
        val entries = vector.children

        for (i in entries.indices step 2) {
            if (PhelFormHead.isPartOf(element, entries[i])) return true
        }

        return false
    }
}
