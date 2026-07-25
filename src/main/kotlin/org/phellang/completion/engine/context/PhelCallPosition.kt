package org.phellang.completion.engine.context

import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import org.phellang.language.psi.PhelList
import org.phellang.language.psi.PhelSpecialForms
import org.phellang.language.psi.utils.PhelPsiUtils

/** Where the caret sits within the form around it, in the sense Lisp gives that word. */
enum class PhelCallPosition {

    /** The first element of a list: what is being called. Anything callable belongs here. */
    HEAD,

    /**
     * An argument of a *plain* function call, where only values are expressible.
     *
     * "Plain" excludes the forms whose textual arguments are not really arguments — threading
     * macros, `doto`, `comment` and the rest of [PhelSpecialForms.VARIADIC_HEADS]. In
     * `(-> x (when-let [y v] y))` the `when-let` reads as an argument of `->` but becomes a head
     * once the macro expands, so a macro is perfectly valid there.
     */
    PLAIN_CALL_ARGUMENT,

    /** Anywhere else: top level, inside a vector or map, or inside a non-plain form. */
    OTHER;

    companion object {

        fun of(element: PsiElement): PhelCallPosition {
            val list = PsiTreeUtil.getParentOfType(element, PhelList::class.java) ?: return OTHER
            val forms = PhelPsiUtils.activeForms(list)
            val head = forms.firstOrNull() ?: return HEAD

            if (PsiTreeUtil.isAncestor(head, element, false)) return HEAD

            val headName = PhelPsiUtils.asSymbol(head)?.text ?: return OTHER
            if (headName in PhelSpecialForms.VARIADIC_HEADS) return OTHER

            return PLAIN_CALL_ARGUMENT
        }
    }
}
