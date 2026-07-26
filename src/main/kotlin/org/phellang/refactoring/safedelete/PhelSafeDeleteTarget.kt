package org.phellang.refactoring.safedelete

import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import org.phellang.language.psi.PhelList
import org.phellang.language.psi.PhelSpecialForms
import org.phellang.language.psi.PhelSymbol
import org.phellang.language.psi.files.PhelFile
import org.phellang.language.psi.utils.PhelPsiUtils

/**
 * What actually disappears when a Phel name is safe-deleted.
 *
 * A name is not a standalone thing in a Lisp: it is the second element of the form that introduces
 * it. Deleting `greet` out of `(defn greet [name] …)` leaves `(defn  [name] …)`, so the form is the
 * unit of deletion.
 *
 * Only *top-level definitions* qualify. A `let` binding or a function parameter also passes
 * `PhelSymbolAnalyzer.isDefinition`, but removing one means editing a binding vector or a parameter
 * list in place and repairing the pairs around it — a different operation with its own failure
 * modes, and one the unused-binding quick fixes already cover for the case that matters.
 */
internal object PhelSafeDeleteTarget {

    /** The top-level definition form [element] names, or null when it does not name one. */
    fun enclosingFormOf(element: PsiElement): PhelList? {
        val symbol = element as? PhelSymbol ?: return null
        val form = PsiTreeUtil.getParentOfType(symbol, PhelList::class.java) ?: return null

        // Top level only: a nested list is a `let`, a body form, or something else entirely.
        if (form.parent !is PhelFile) return null

        val forms = PhelPsiUtils.activeForms(form)
        val head = PhelPsiUtils.asSymbol(forms.firstOrNull())?.text ?: return null
        if (head !in PhelSpecialForms.DEFINITION_FORMS) return null

        // The name being deleted has to be *this* form's name, not something inside its body.
        val name = PhelPsiUtils.asSymbol(forms.getOrNull(1)) ?: return null
        if (name !== symbol) return null

        return form
    }
}
