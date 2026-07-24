package org.phellang.completion.engine.context

import com.intellij.psi.PsiElement
import org.phellang.language.psi.PhelAccess
import org.phellang.language.psi.PhelList
import org.phellang.language.psi.PhelMap
import org.phellang.language.psi.PhelSymbol
import org.phellang.language.psi.PhelVec

/** The two reads every declaration-position predicate performs. */
internal object PhelFormHead {

    /**
     * The text of [element] when it is a symbol.
     *
     * A bare symbol reaches PSI as either `PhelSymbol` or `PhelAccess` depending on where it sits,
     * so both have to be accepted for a head or name slot to be recognised at all.
     */
    fun symbolTextOf(element: PsiElement?): String? =
        if (element is PhelSymbol || element is PhelAccess) element.text else null

    /**
     * True when [element] sits inside [form] without crossing into a nested collection.
     *
     * The boundary is what stops `(defn f [x] (g <caret>))` from reading as the name slot of the
     * `defn`: the caret is inside a nested list, so it belongs to that call, not to the name.
     */
    fun isPartOf(element: PsiElement, form: PsiElement): Boolean {
        var current: PsiElement? = element

        while (current != null) {
            if (current === form) return true
            if (current is PhelList || current is PhelVec || current is PhelMap) return false
            current = current.parent
        }

        return false
    }
}
