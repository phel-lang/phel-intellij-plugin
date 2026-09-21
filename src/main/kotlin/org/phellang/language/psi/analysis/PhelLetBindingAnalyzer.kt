package org.phellang.language.psi.analysis

import org.phellang.language.psi.PhelSpecialForms
import org.phellang.language.psi.PhelSymbol
import org.phellang.language.psi.PhelVec

/**
 * Bindings introduced by `let`-like forms: `(let [x 1] …)`, `loop`, `for`, `binding`, `when-let`.
 *
 * A binding vector alternates patterns and values — `[pattern1 value1 pattern2 value2]` — so only
 * the even positions declare anything, and each of those is a pattern that may destructure:
 * `[[a b] xs]`, `[{n :name} m]`. [PhelDestructuringAnalyzer] reads the names out of a pattern.
 */
internal object PhelLetBindingAnalyzer {

    private val LET_LIKE_FORMS = PhelSpecialForms.LET_LIKE

    /**
     * True when [symbol] IS one of the names a let-like form binds, at any depth of destructuring.
     *
     * The vector asked is the *binding* vector the symbol sits in, not the nearest one: a nested
     * pattern is a vector too, and the innermost enclosing `let` may be one whose value slot the
     * symbol is read in rather than one that binds it.
     */
    fun isLetBinding(symbol: PhelSymbol): Boolean {
        val bindingVector = PhelDestructuringAnalyzer.enclosingBindingVector(symbol) ?: return false
        if (!bindingVector.isLetLike) return false

        return PhelDestructuringAnalyzer.declares(bindingVector, symbol)
    }

    /** True when [symbolText] names a binding of some enclosing let-like form — i.e. a *reference*. */
    fun isReferenceToLetBinding(symbol: PhelSymbol, symbolText: String): Boolean {
        return PhelFormWalker.enclosingLists(symbol)
            .filter { PhelFormWalker.headText(it) in LET_LIKE_FORMS }
            .mapNotNull { it.children.getOrNull(1) as? PhelVec }
            .any { bindingVec -> bindsName(bindingVec, symbolText) }
    }

    private fun bindsName(bindingVec: PhelVec, symbolText: String): Boolean =
        PhelDestructuringAnalyzer.letBoundSymbols(bindingVec).any { it.text == symbolText }
}
