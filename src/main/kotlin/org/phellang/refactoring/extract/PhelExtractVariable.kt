package org.phellang.refactoring.extract

import com.intellij.psi.util.PsiTreeUtil
import org.phellang.language.psi.PhelForm
import org.phellang.language.psi.PhelList
import org.phellang.language.psi.PhelPsiFactory
import org.phellang.language.psi.PhelSpecialForms
import org.phellang.language.psi.PhelSymbol
import org.phellang.language.psi.PhelVec
import org.phellang.language.psi.utils.PhelPsiUtils

/**
 * Introduces a `let` binding for an expression.
 *
 * Where the binding goes is the decision this makes, in three cases, in order:
 *
 * 1. **Inside an existing `let` body** — append to that `let`'s binding vector. Nesting a second
 *    `let` inside the first would say the same thing with more brackets.
 * 2. **Inside a function body** — wrap the *whole body* in a new `let`. Wrapping only the expression
 *    would produce `(let [x expr] x)`, a binding nothing else can reach.
 * 3. **Anywhere else** — wrap the expression where it stands, which is all a `(def …)` value or a
 *    bare top-level form allows.
 *
 * The rewrite is done on text and re-parsed rather than by PSI surgery. Lisp source is shaped like
 * its tree, so building the new form as a string is both shorter and harder to get subtly wrong than
 * splicing nodes, and the parser round-trips whatever it produces.
 */
internal object PhelExtractVariable {

    /**
     * Replaces [expression] with [name], bound to it. Returns the introduced binding's name symbol so
     * the caller can offer to rename it; null when the expression cannot be extracted.
     *
     * The caller supplies the write action.
     */
    fun extract(expression: PhelForm, name: String): PhelSymbol? {
        val plan = plan(expression, name) ?: return null

        val rewritten = PhelPsiFactory.createList(expression.project, plan.text)
        val replaced = plan.target.replace(rewritten) as? PhelList ?: return null

        return bindingNamed(replaced, name)
    }

    /**
     * True when [expression] is something this can introduce a binding for: a call.
     *
     * A plain `is PhelList` test, because in this grammar a list *is* a form — `form_upper` re-tags
     * the inner rule rather than wrapping it, so there is no form node around the list to look
     * through. [PhelPsiUtils.asSymbol] is the wrong question here for the opposite reason: it finds
     * the symbol a form contains *anywhere*, so it answers "yes" for `(* 2 3)` on the strength of
     * the `*`.
     *
     * A bare name is excluded because binding it to another name buys nothing.
     */
    fun canExtract(expression: PhelForm): Boolean = expression is PhelList

    private class Plan(val target: PhelList, val text: String)

    private fun plan(expression: PhelForm, name: String): Plan? {
        if (!canExtract(expression)) return null

        enclosingLetWithBodyContaining(expression)?.let { return appendToLet(it, expression, name) }
        enclosingFunctionWithBodyContaining(expression)?.let { return wrapFunctionBody(it, expression, name) }

        // Nowhere to hoist it to — a `(def …)` value or a bare top-level form. Binding it where it
        // stands is all that is available, and still names the expression.
        val itself = expression as? PhelList ?: return null

        return Plan(itself, "(let [$name ${expression.text}] $name)")
    }

    /** `(let [a 1] … expr …)` becomes `(let [a 1 name expr] … name …)`. */
    private fun appendToLet(letForm: PhelList, expression: PhelForm, name: String): Plan? {
        val bindings = bindingVectorOf(letForm) ?: return null
        val letStart = letForm.textRange.startOffset

        val insertAt = bindings.textRange.endOffset - 1 - letStart
        val expressionRange = expression.textRange.shiftLeft(letStart)
        if (expressionRange.startOffset < insertAt) return null

        val text = StringBuilder(letForm.text)
        // Right to left, so the earlier offset is still valid when the later edit has been applied.
        text.replace(expressionRange.startOffset, expressionRange.endOffset, name)
        text.insert(insertAt, " $name ${expression.text}")

        return Plan(letForm, text.toString())
    }

    /** `(defn f [x] body…)` becomes `(defn f [x] (let [name expr] body…))`. */
    private fun wrapFunctionBody(function: PhelList, expression: PhelForm, name: String): Plan? {
        val forms = PhelPsiUtils.activeForms(function)
        val body = forms.drop(forms.indexOfFirst { it is PhelVec } + 1)
        if (body.isEmpty()) return null

        val functionStart = function.textRange.startOffset
        val bodyStart = body.first().textRange.startOffset - functionStart
        val bodyEnd = body.last().textRange.endOffset - functionStart

        val bodyText = StringBuilder(function.text.substring(bodyStart, bodyEnd))
        val expressionRange = expression.textRange.shiftLeft(functionStart + bodyStart)
        bodyText.replace(expressionRange.startOffset, expressionRange.endOffset, name)

        val text = StringBuilder(function.text)
        text.replace(bodyStart, bodyEnd, "(let [$name ${expression.text}] $bodyText)")

        return Plan(function, text.toString())
    }

    /** The nearest `let` whose *body* holds [expression] — not its binding vector. */
    private fun enclosingLetWithBodyContaining(expression: PhelForm): PhelList? =
        enclosingLists(expression).firstOrNull { list ->
            val head = PhelPsiUtils.asSymbol(PhelPsiUtils.activeForms(list).firstOrNull())?.text
            if (head !in PhelSpecialForms.LET_LIKE) return@firstOrNull false

            val bindings = bindingVectorOf(list) ?: return@firstOrNull false
            !bindings.textRange.contains(expression.textRange)
        }

    private fun enclosingFunctionWithBodyContaining(expression: PhelForm): PhelList? =
        enclosingLists(expression).firstOrNull { list ->
            val head = PhelPsiUtils.asSymbol(PhelPsiUtils.activeForms(list).firstOrNull())?.text
            if (head !in PhelSpecialForms.FUNCTION_DEFINING) return@firstOrNull false

            val parameters = bindingVectorOf(list) ?: return@firstOrNull false
            !parameters.textRange.contains(expression.textRange)
        }

    /** The first vector in [list] — a `let`'s bindings, or a function's parameters. */
    private fun bindingVectorOf(list: PhelList): PhelVec? =
        PhelPsiUtils.activeForms(list).filterIsInstance<PhelVec>().firstOrNull()

    private fun enclosingLists(from: PhelForm): Sequence<PhelList> =
        generateSequence(PsiTreeUtil.getParentOfType(from, PhelList::class.java)) {
            PsiTreeUtil.getParentOfType(it, PhelList::class.java)
        }

    private fun bindingNamed(form: PhelList, name: String): PhelSymbol? =
        PsiTreeUtil.findChildrenOfType(form, PhelSymbol::class.java).firstOrNull { it.text == name }
}
