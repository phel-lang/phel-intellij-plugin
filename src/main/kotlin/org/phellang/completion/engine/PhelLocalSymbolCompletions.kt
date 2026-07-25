package org.phellang.completion.engine

import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.psi.PsiElement
import org.phellang.completion.engine.locals.PhelBindingCollector
import org.phellang.completion.engine.locals.PhelFileDefinitionCollector
import org.phellang.completion.engine.locals.PhelLocalSymbolSink
import org.phellang.completion.engine.locals.PhelParameterCollector

/**
 * Symbols bound in the file being edited: parameters, let/loop bindings, and its own top-level
 * definitions. All are legal to type unqualified, and all are found by walking PSI the user has
 * already parsed.
 *
 * Symbols from *other* files are deliberately not handled here — `PhelProjectCompletionHelper`
 * serves those from the symbol index, namespace-qualified and with auto-import, which is the only
 * spelling that actually compiles.
 */
object PhelLocalSymbolCompletions {

    /**
     * Collectors run narrowest scope first. The sink keeps the first offer of each name, so that
     * order is what makes a parameter shadow a same-named top-level definition.
     */
    @JvmStatic
    fun addLocalSymbols(result: CompletionResultSet, position: PsiElement) {
        val sink = PhelLocalSymbolSink(result)

        PhelParameterCollector.collect(position, sink)
        PhelBindingCollector.collect(position, sink)
        PhelFileDefinitionCollector.collect(position, sink)
    }
}
