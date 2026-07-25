package org.phellang.completion.engine.locals

import com.intellij.codeInsight.completion.CompletionResultSet
import org.phellang.completion.infrastructure.PhelCompletionUtils
import org.phellang.completion.infrastructure.PhelLocalSymbolKind
import org.phellang.registry.PhelCompletionPriority
import javax.swing.Icon

/**
 * Collects local completions, keeping the first offer of each name.
 *
 * First writer wins, and the collectors run innermost-scope first, so the dedupe implements Phel's
 * shadowing: an inner `let` binding hides an outer one, and either hides a same-named top-level
 * definition in the file.
 */
internal class PhelLocalSymbolSink(private val result: CompletionResultSet) {

    private val added = HashSet<String>()

    /** A symbol bound in the current scope: a parameter or a binding-vector entry. */
    fun addScopedSymbol(name: String, kind: PhelLocalSymbolKind, icon: Icon?) {
        if (!claim(name)) return

        PhelCompletionUtils.addLocalSymbolCompletion(result, name, kind, icon)
    }

    /** A top-level definition of the edited file, which carries its own ranking. */
    fun addFileDefinition(name: String, description: String, priority: PhelCompletionPriority) {
        if (!claim(name)) return

        PhelCompletionUtils.addRankedCompletion(result, name, "", description, priority)
    }

    private fun claim(name: String): Boolean = name.isNotBlank() && added.add(name)
}
