package org.phellang.refactoring.extract

import com.intellij.psi.util.PsiTreeUtil
import org.phellang.language.psi.PhelForm
import org.phellang.language.psi.PhelList
import org.phellang.language.psi.PhelSpecialForms
import org.phellang.language.psi.PhelSymbol
import org.phellang.language.psi.PhelTypeTags
import org.phellang.language.psi.PhelVec
import org.phellang.language.psi.analysis.PhelDestructuringAnalyzer
import org.phellang.language.psi.analysis.PhelLocalBindingScope
import org.phellang.language.psi.utils.PhelPsiUtils

/**
 * The locals an expression uses but does not itself bind — the parameter list a function extracted
 * from it must take.
 *
 * A name qualifies when it resolves to a local binding *and* nothing inside the expression binds it.
 * The first half is [PhelLocalBindingScope], which is what already decides that a shadowing local is
 * a different function from the stdlib name it shadows; the second is what keeps a `let` written
 * inside the selection from being passed in as an argument to itself.
 *
 * Globals, stdlib names and `(:require)`d functions are not free variables: they resolve the same
 * from anywhere in the file, so the extracted function reaches them without help.
 */
internal object PhelFreeVariables {

    /** In first-appearance order, so the generated parameter list reads like the expression does. */
    fun of(expression: PhelForm): List<String> {
        val boundInside = boundWithin(expression)
        val free = LinkedHashSet<String>()

        for (symbol in PsiTreeUtil.findChildrenOfType(expression, PhelSymbol::class.java)) {
            // `^atom` names a type, not the `atom` binding it spells, so it needs no parameter.
            if (PhelTypeTags.isTypeTag(symbol)) continue
            val name = symbol.text ?: continue
            if (name in boundInside) continue
            if (isBindingSite(symbol)) continue
            if (!PhelLocalBindingScope.resolvesToLocalBinding(symbol, name)) continue

            free.add(name)
        }

        return free.toList()
    }

    /**
     * Every name introduced by a binding vector inside [expression], including one it introduces
     * itself.
     *
     * `findChildrenOfType` returns descendants only, so selecting the `(let [a 1] …)` *itself* left
     * its own `a` unaccounted for and passed it in as an argument to itself.
     */
    private fun boundWithin(expression: PhelForm): Set<String> {
        val bound = HashSet<String>()
        val lists = listOfNotNull(expression as? PhelList) +
                PsiTreeUtil.findChildrenOfType(expression, PhelList::class.java)

        for (list in lists) {
            val head = PhelPsiUtils.asSymbol(PhelPsiUtils.activeForms(list).firstOrNull())?.text ?: continue
            if (head !in PhelSpecialForms.LET_LIKE && head !in PhelSpecialForms.FUNCTION_DEFINING) continue

            val vector = PhelPsiUtils.activeForms(list).filterIsInstance<PhelVec>().firstOrNull() ?: continue
            bound += namesIn(vector, isBindingVector = head in PhelSpecialForms.LET_LIKE)
        }

        return bound
    }

    /**
     * A `let` vector binds every *even* element; a parameter vector binds all of them. Either kind
     * of entry may destructure, and then every name the pattern introduces is bound.
     *
     * Taking every symbol in a `let` vector would treat its initialisers as bindings too, so
     * `(let [a (f b)] …)` would wrongly report `b` as bound rather than free. The same holds inside a
     * pattern: the default in `{:or {n (g m)}}` is an expression, and `m` stays free.
     */
    private fun namesIn(vector: PhelVec, isBindingVector: Boolean): List<String> {
        val bound = if (isBindingVector) PhelDestructuringAnalyzer.letBoundSymbols(vector)
        else PhelDestructuringAnalyzer.parameterSymbols(vector)

        return bound.mapNotNull { it.text }
    }

    /**
     * True when [symbol] is being introduced here rather than read.
     *
     * The symbol has to be a name the binding vector introduces, which for a `let` means the
     * pattern half of a pair. Treating every symbol inside one as bound made `n` in
     * `(let [a (* n 2)] …)` a binding rather than the free variable it is, so the extracted function
     * took `a` and not `n`.
     */
    private fun isBindingSite(symbol: PhelSymbol): Boolean {
        val bindingVector = PhelDestructuringAnalyzer.enclosingBindingVector(symbol) ?: return false

        return PhelDestructuringAnalyzer.declares(bindingVector, symbol)
    }
}
