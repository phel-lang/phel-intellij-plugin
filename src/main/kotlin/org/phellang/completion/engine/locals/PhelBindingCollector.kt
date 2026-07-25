package org.phellang.completion.engine.locals

import com.intellij.icons.AllIcons
import com.intellij.psi.PsiElement
import org.phellang.completion.infrastructure.PhelLocalSymbolKind
import org.phellang.language.psi.PhelSpecialForms
import org.phellang.language.psi.PhelVec
import org.phellang.language.psi.utils.PhelPsiUtils

/**
 * Offers the names bound by every enclosing binding form.
 *
 * Unlike parameters, the walk does not stop at the first match: `(let [a 1] (let [b 2] ...))` has
 * both `a` and `b` in scope, and the innermost-first order lets the sink resolve shadowing.
 */
internal object PhelBindingCollector {

    val ICON = AllIcons.Nodes.Variable

    /** The two heads with a name of their own; every other binding form falls back below. */
    private val NAMED_KINDS = mapOf(
        "let" to PhelLocalSymbolKind.LET_BINDING,
        "loop" to PhelLocalSymbolKind.LOOP_BINDING,
    )

    fun collect(position: PsiElement, sink: PhelLocalSymbolSink) {
        for (form in PhelEnclosingForms.from(position)) {
            val kind = kindFor(form.head) ?: continue
            val vector = form.list.children.getOrNull(1) as? PhelVec ?: continue

            collectNames(vector, kind, sink)
        }
    }

    /**
     * Derived from [PhelSpecialForms.LET_LIKE] rather than listed here.
     *
     * This used to carry its own set — `let`, `for`, `loop`, `binding` — which silently omitted
     * `if-let`, `when-let`, `if-some`, `when-some`, `foreach` and `dofor`, so those bindings were
     * offered by nothing at all. Reading the canonical set is what keeps a form added there from
     * being missed here.
     */
    private fun kindFor(head: String): PhelLocalSymbolKind? {
        if (head !in PhelSpecialForms.LET_LIKE) return null

        return NAMED_KINDS[head] ?: PhelLocalSymbolKind.LOCAL_VARIABLE
    }

    /**
     * The vector holds name/value pairs, so every other form is a name.
     *
     * Counted over activeForms rather than children: `#_` leaves the form it discards in the tree,
     * and one discarded entry shifts the parity, dropping every later name from the results.
     */
    private fun collectNames(vector: PhelVec, kind: PhelLocalSymbolKind, sink: PhelLocalSymbolSink) {
        val forms = PhelPsiUtils.activeForms(vector)

        for (i in forms.indices step 2) {
            val name = PhelPsiUtils.asSymbol(forms[i])?.text ?: continue

            sink.addScopedSymbol(name, kind, ICON)
        }
    }
}
