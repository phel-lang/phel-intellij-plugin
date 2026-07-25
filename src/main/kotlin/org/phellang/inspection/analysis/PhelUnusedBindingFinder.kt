package org.phellang.inspection.analysis

import com.intellij.psi.util.PsiTreeUtil
import org.phellang.language.psi.PhelForm
import org.phellang.language.psi.PhelList
import org.phellang.language.psi.PhelSpecialForms
import org.phellang.language.psi.PhelSymbol
import org.phellang.language.psi.PhelVec
import org.phellang.language.psi.utils.PhelPsiUtils

/** Finds the names a let-like form binds and then never reads. */
internal object PhelUnusedBindingFinder {

    /**
     * The unused binding targets of [list] in source order, or empty when [list] is not a let-like
     * form, has no binding vector, or has no body to read the bindings from.
     */
    fun unusedBindings(list: PhelList): List<PhelSymbol> {
        // activeForms, not forms: a `#_`-discarded form must not shift the head/binding-vector reads
        // or the name/value pairing inside the binding vector.
        val forms = PhelPsiUtils.activeForms(list)
        if (forms.size < 2) return emptyList()

        val head = PhelPsiUtils.asSymbol(forms[0]) ?: return emptyList()
        if (head.text !in PhelSpecialForms.LET_LIKE) return emptyList()

        val bindingVector = forms[1] as? PhelVec ?: return emptyList()
        val body = forms.drop(2)
        if (body.isEmpty()) return emptyList()

        return findUnused(PhelPsiUtils.activeForms(bindingVector), symbolTextsOf(body))
    }

    /**
     * Walks the bindings back to front so that, by the time a name at an even index is reached,
     * [laterValues] already holds every symbol used in the value slots after it. Phel binds
     * sequentially, so a value can only reference names introduced before it.
     */
    private fun findUnused(bindings: List<PhelForm>, bodySymbols: Set<String>): List<PhelSymbol> {
        val laterValues = HashSet<String>()
        val unused = ArrayList<PhelSymbol>()

        for (i in bindings.indices.reversed()) {
            if (i % 2 == 1) {
                collectInto(bindings[i], laterValues)
                continue
            }

            val target = PhelPsiUtils.asSymbol(bindings[i]) ?: continue
            val name = target.text
            if (isIntentionallyUnused(name)) continue

            if (name !in bodySymbols && name !in laterValues) unused.add(target)
        }

        // Collected back to front; reported top to bottom for a natural reading order.
        return unused.asReversed()
    }

    /** A leading `_` marks a deliberate throwaway, and `&` introduces a rest parameter. */
    private fun isIntentionallyUnused(name: String): Boolean =
        name.startsWith("_") || name.startsWith("&")

    private fun symbolTextsOf(forms: List<PhelForm>): Set<String> {
        val result = HashSet<String>()
        for (form in forms) collectInto(form, result)

        return result
    }

    /** Adds the text of [element] (when it is a symbol) plus every descendant symbol. */
    private fun collectInto(element: PhelForm, into: MutableSet<String>) {
        if (element is PhelSymbol) into.add(element.text)

        for (symbol in PsiTreeUtil.findChildrenOfType(element, PhelSymbol::class.java)) {
            into.add(symbol.text)
        }
    }
}
