package org.phellang.inspection.analysis

import com.intellij.psi.util.PsiTreeUtil
import org.phellang.language.psi.PhelForm
import org.phellang.language.psi.PhelSymbol
import org.phellang.language.psi.PhelTypeTags

/**
 * The set of symbol names appearing anywhere in a group of forms.
 *
 * Every "is this name ever read?" inspection asks the same question of a body — the let-binding
 * finder and the parameter finder had the same walk written twice, and the second copy is the sort
 * that quietly stops matching the first.
 *
 * Type tags are left out: `^atom` spells `atom` but names a type, so it never reads a binding called `atom`.
 */
internal object PhelSymbolTexts {

    fun of(forms: List<PhelForm>): Set<String> {
        val result = HashSet<String>()
        for (form in forms) collectInto(form, result)

        return result
    }

    /** Adds the text of [element] (when it is itself a symbol) plus every descendant symbol, tags aside. */
    private fun collectInto(element: PhelForm, into: MutableSet<String>) {
        if (element is PhelSymbol) into.add(element.text)

        for (symbol in PsiTreeUtil.findChildrenOfType(element, PhelSymbol::class.java)) {
            if (!PhelTypeTags.isTypeTag(symbol)) into.add(symbol.text)
        }
    }
}
