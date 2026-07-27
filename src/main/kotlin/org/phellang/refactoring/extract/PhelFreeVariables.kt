package org.phellang.refactoring.extract

import com.intellij.psi.util.PsiTreeUtil
import org.phellang.language.psi.PhelForm
import org.phellang.language.psi.PhelList
import org.phellang.language.psi.PhelSpecialForms
import org.phellang.language.psi.PhelSymbol
import org.phellang.language.psi.PhelVec
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
     * A `let` vector binds every *even* element; a parameter vector binds all of them.
     *
     * Taking every symbol in a `let` vector would treat its initialisers as bindings too, so
     * `(let [a (f b)] …)` would wrongly report `b` as bound rather than free.
     */
    private fun namesIn(vector: PhelVec, isBindingVector: Boolean): List<String> {
        val forms = PhelPsiUtils.activeForms(vector)
        val binding = if (isBindingVector) forms.filterIndexed { index, _ -> index % 2 == 0 } else forms

        return binding.mapNotNull { PhelPsiUtils.asSymbol(it)?.text }
    }

    /**
     * True when [symbol] is being introduced here rather than read.
     *
     * The symbol has to be a *direct* element of the vector, at an even index when the vector binds
     * pairs. Treating every symbol inside one as bound made `n` in `(let [a (* n 2)] …)` a binding
     * rather than the free variable it is, so the extracted function took `a` and not `n`.
     */
    private fun isBindingSite(symbol: PhelSymbol): Boolean {
        val vector = PsiTreeUtil.getParentOfType(symbol, PhelVec::class.java) ?: return false
        val owner = PsiTreeUtil.getParentOfType(vector, PhelList::class.java) ?: return false
        val head = PhelPsiUtils.asSymbol(PhelPsiUtils.activeForms(owner).firstOrNull())?.text ?: return false

        val binds = head in PhelSpecialForms.LET_LIKE || head in PhelSpecialForms.FUNCTION_DEFINING
        if (!binds) return false

        val index = PhelPsiUtils.activeForms(vector).indexOfFirst { PhelPsiUtils.asSymbol(it) === symbol }
        if (index < 0) return false

        return head !in PhelSpecialForms.LET_LIKE || index % 2 == 0
    }
}
