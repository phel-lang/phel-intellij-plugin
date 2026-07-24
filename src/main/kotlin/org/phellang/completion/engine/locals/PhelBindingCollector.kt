package org.phellang.completion.engine.locals

import com.intellij.icons.AllIcons
import com.intellij.psi.PsiElement
import org.phellang.completion.infrastructure.PhelLocalSymbolKind
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

    private val KINDS = mapOf(
        "let" to PhelLocalSymbolKind.LET_BINDING,
        "loop" to PhelLocalSymbolKind.LOOP_BINDING,
        "for" to PhelLocalSymbolKind.LOCAL_VARIABLE,
        "binding" to PhelLocalSymbolKind.LOCAL_VARIABLE,
    )

    fun collect(position: PsiElement, sink: PhelLocalSymbolSink) {
        for (form in PhelEnclosingForms.from(position)) {
            val kind = KINDS[form.head] ?: continue
            val vector = form.list.children.getOrNull(1) as? PhelVec ?: continue

            collectNames(vector, kind, sink)
        }
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
