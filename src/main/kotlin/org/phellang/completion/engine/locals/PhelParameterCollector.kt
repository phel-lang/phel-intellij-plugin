package org.phellang.completion.engine.locals

import com.intellij.icons.AllIcons
import com.intellij.psi.PsiElement
import org.phellang.completion.infrastructure.PhelLocalSymbolKind
import org.phellang.language.psi.PhelSpecialForms
import org.phellang.language.psi.analysis.PhelDestructuringAnalyzer
import org.phellang.language.psi.analysis.PhelSymbolAnalyzer

/** Offers the parameters of the function being edited. */
internal object PhelParameterCollector {

    val ICON = AllIcons.Nodes.Parameter

    fun collect(position: PsiElement, sink: PhelLocalSymbolSink) {
        // Only the innermost function contributes: once one is found the search stops, even if it
        // has no parameter vector, so an enclosing function's parameters never leak into an inner
        // `fn`'s scope.
        val function = PhelEnclosingForms.from(position)
            .firstOrNull { it.head in PhelSpecialForms.FUNCTION_DEFINING }
            ?: return

        val paramVec = PhelSymbolAnalyzer.findParameterVector(function.list) ?: return

        // Read through the destructuring analyzer, which walks active forms only: a `#_`-discarded
        // parameter is still in the tree but is not bound, so offering it would complete a name that
        // does not exist at runtime. A destructured parameter (`[{n :name}]`) offers `n`.
        for (symbol in PhelDestructuringAnalyzer.parameterSymbols(paramVec)) {
            val name = symbol.text ?: continue

            sink.addScopedSymbol(name, PhelLocalSymbolKind.FUNCTION_PARAMETER, ICON)
        }
    }
}
