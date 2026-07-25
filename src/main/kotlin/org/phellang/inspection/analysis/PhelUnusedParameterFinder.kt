package org.phellang.inspection.analysis

import org.phellang.language.psi.PhelList
import org.phellang.language.psi.PhelSymbol
import org.phellang.language.psi.PhelVec
import org.phellang.language.psi.utils.PhelPsiUtils

/**
 * Finds parameters a function declares and never reads.
 *
 * The same question `PhelUnusedBindingFinder` answers for `let`, over a parameter vector instead of
 * a binding vector, and with the same exclusions: `_`-prefixed names are deliberate throwaways and
 * `&` introduces a rest marker.
 */
internal object PhelUnusedParameterFinder {

    /** Heads whose second form is a parameter vector followed by a body. */
    private val PARAMETERISED_FORMS = setOf("defn", "defn-", "fn", "defmacro", "defmacro-")

    fun unusedParameters(list: PhelList): List<PhelSymbol> {
        val forms = PhelPsiUtils.activeForms(list)
        if (forms.size < 2) return emptyList()

        val head = PhelPsiUtils.asSymbol(forms[0])?.text ?: return emptyList()
        if (head !in PARAMETERISED_FORMS) return emptyList()

        val vectorIndex = forms.indexOfFirst { it is PhelVec }
        if (vectorIndex < 1) return emptyList()

        val parameters = forms[vectorIndex] as PhelVec
        val body = forms.drop(vectorIndex + 1)
        // A declaration with no body reads nothing, so every parameter would look unused.
        if (body.isEmpty()) return emptyList()

        val used = PhelSymbolTexts.of(body)

        return PhelPsiUtils.activeForms(parameters)
            .mapNotNull { PhelPsiUtils.asSymbol(it) }
            .filterNot { isIntentionallyUnused(it.text) }
            .filter { it.text !in used }
    }

    /**
     * A leading `_` marks a deliberate throwaway, and `&` introduces a rest parameter. Destructuring
     * targets are skipped too: the names a map or vector pattern binds are read through the pattern,
     * not by the parameter symbol itself.
     */
    private fun isIntentionallyUnused(name: String?): Boolean {
        if (name == null) return true

        return name.startsWith("_") || name.startsWith("&")
    }
}
