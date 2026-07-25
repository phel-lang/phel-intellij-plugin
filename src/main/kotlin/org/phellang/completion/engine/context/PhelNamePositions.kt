package org.phellang.completion.engine.context

import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import org.phellang.language.psi.PhelList
import org.phellang.language.psi.PhelMap
import org.phellang.language.psi.PhelSpecialForms
import org.phellang.language.psi.PhelVec

/**
 * The caret sitting on the *name* a definition introduces, e.g. `(defn <caret> [x] ...)`.
 *
 * The user is naming something new there, so offering existing names is noise at best and, when one
 * is accepted, a redefinition the user did not intend.
 *
 * Which heads declare a name is a per-form fact, held in [PhelSpecialForms.NAME_DECLARING]. Asking
 * the completion-priority bucket instead — as this used to — answered a different question and got
 * it close to backwards: `(throw <caret>)` and `(do <caret>)` suppressed, `(defn <caret>)` did not.
 */
internal object PhelNamePositions {

    /** `(defn <caret> ...)` read through the list's forms, skipping any `#_`-discarded entries. */
    fun isFunctionName(element: PsiElement): Boolean {
        val list = PsiTreeUtil.getParentOfType(element, PhelList::class.java) ?: return false
        val forms = list.forms
        if (forms.isEmpty()) return false

        val head = PhelFormHead.symbolTextOf(forms[0]) ?: return false
        if (head !in PhelSpecialForms.NAME_DECLARING) return false
        if (forms.size < 2) return false

        return PhelFormHead.occupiesSlot(element, forms[1])
    }

    /** The same slot read through the list's raw children, which is what catches a partial parse. */
    fun isDefinitionName(element: PsiElement): Boolean {
        val list = PsiTreeUtil.getParentOfType(element, PhelList::class.java) ?: return false
        val children = list.children
        if (children.size < 2) return false

        val head = PhelFormHead.symbolTextOf(children[0]) ?: return false
        if (head !in PhelSpecialForms.NAME_DECLARING) return false

        return PhelFormHead.occupiesSlot(element, children[1])
    }
}
