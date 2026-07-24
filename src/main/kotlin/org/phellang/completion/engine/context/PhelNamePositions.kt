package org.phellang.completion.engine.context

import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import org.phellang.language.psi.PhelList
import org.phellang.language.psi.analysis.PhelSymbolAnalyzer
import org.phellang.language.psi.utils.SymbolCategory

/**
 * The caret sitting on the *name* a definition introduces, e.g. `(defn <caret> [x] ...)`.
 *
 * The user is naming something new there, so offering existing names is noise at best and, when one
 * is accepted, a redefinition the user did not intend.
 */
internal object PhelNamePositions {

    /** `(defn <caret> ...)` read through the list's forms, skipping any `#_`-discarded entries. */
    fun isFunctionName(element: PsiElement): Boolean {
        val list = PsiTreeUtil.getParentOfType(element, PhelList::class.java) ?: return false
        val forms = list.forms
        if (forms.isEmpty()) return false

        // A special form one level out means this list is an argument of it, not a definition of
        // its own — `(let [x (defn ...)])` must not suppress inside the binding's value.
        if (isInsideSpecialForm(list)) return false

        val head = PhelFormHead.symbolTextOf(forms[0]) ?: return false
        if (!isSpecialForm(head)) return false
        if (forms.size < 2) return false

        return PhelFormHead.isPartOf(element, forms[1])
    }

    /** The same slot read through the list's raw children, which is what catches a partial parse. */
    fun isDefinitionName(element: PsiElement): Boolean {
        val list = PsiTreeUtil.getParentOfType(element, PhelList::class.java) ?: return false
        val children = list.children
        if (children.size < 2) return false

        val head = PhelFormHead.symbolTextOf(children[0]) ?: return false
        if (!isSpecialForm(head)) return false

        return PhelFormHead.isPartOf(element, children[1])
    }

    private fun isInsideSpecialForm(list: PhelList): Boolean {
        val parent = PsiTreeUtil.getParentOfType(list, PhelList::class.java) ?: return false
        val head = PhelFormHead.symbolTextOf(parent.forms.firstOrNull()) ?: return false

        return isSpecialForm(head)
    }

    private fun isSpecialForm(head: String) =
        PhelSymbolAnalyzer.isSymbolType(head, SymbolCategory.SPECIAL_FORMS)
}
