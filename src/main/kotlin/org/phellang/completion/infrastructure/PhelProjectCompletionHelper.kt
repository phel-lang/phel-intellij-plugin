package org.phellang.completion.infrastructure

import com.intellij.codeInsight.completion.CompletionResultSet
import org.phellang.indexing.PhelProjectSymbolIndex
import org.phellang.language.psi.PhelNamespaceUtils
import org.phellang.language.psi.PhelRequireClauseAnalyzer
import org.phellang.language.psi.files.PhelFile
import org.phellang.registry.PhelCompletionPriority
import org.phellang.registry.PhelProjectSymbol
import org.phellang.registry.SymbolType

/** Offers symbols defined elsewhere in the project, spelled the way they would have to be typed. */
object PhelProjectCompletionHelper {

    @JvmStatic
    fun addProjectCompletions(result: CompletionResultSet, file: PhelFile, aliasMap: Map<String, String>) {
        val index = PhelProjectSymbolIndex.getInstance(file.project)
        val current = CurrentFile(file)

        val imported = addImportedSymbols(result, index, file, aliasMap, current)
        addUnimportedSymbols(result, index, current, imported)
    }

    /**
     * Namespaces the file already requires, spelled with whatever alias is in force.
     *
     * Returns the short namespaces handled here, so the second pass does not offer them again
     * fully-qualified.
     */
    private fun addImportedSymbols(
        result: CompletionResultSet,
        index: PhelProjectSymbolIndex,
        file: PhelFile,
        aliasMap: Map<String, String>,
        current: CurrentFile,
    ): Set<String> {
        val handled = mutableSetOf<String>()

        for (import in PhelRequireClauseAnalyzer.imports(file)) {
            handled += import.shortNamespace

            for (symbol in index.getSymbolsForNamespace(import.shortNamespace)) {
                // The edited file's own symbols come from local completions, unqualified.
                if (current.owns(symbol)) continue

                addCompletion(result, symbol, aliasedName(symbol, import, aliasMap))
            }
        }

        return handled
    }

    /** Everything else in the project, offered fully qualified and auto-imported on acceptance. */
    private fun addUnimportedSymbols(
        result: CompletionResultSet,
        index: PhelProjectSymbolIndex,
        current: CurrentFile,
        handled: Set<String>,
    ) {
        for (symbol in index.getAllSymbols()) {
            if (current.owns(symbol)) continue
            if (symbol.shortNamespace in handled) continue
            if (symbol.shortNamespace == current.namespace) continue

            addCompletion(result, symbol, symbol.qualifiedName)
        }
    }

    /** An explicit `:as` wins; otherwise an alias pointing at the same namespace, else the full name. */
    private fun aliasedName(
        symbol: PhelProjectSymbol,
        import: PhelRequireClauseAnalyzer.RequireImport,
        aliasMap: Map<String, String>,
    ): String {
        import.alias?.let { return "$it/${symbol.name}" }

        val alias = aliasMap.entries.find { it.value == symbol.shortNamespace }?.key

        return if (alias != null) "$alias/${symbol.name}" else symbol.qualifiedName
    }

    private fun addCompletion(result: CompletionResultSet, symbol: PhelProjectSymbol, displayName: String) {
        PhelCompletionUtils.addRankedCompletionWithNamespace(
            result,
            displayName,
            symbol.signature,
            symbol.type.description,
            PhelCompletionPriority.PROJECT_SYMBOLS,
            symbol.namespace,
        )
    }

    /** The edited file's identity. Both its path and its namespace suppress a suggestion. */
    private class CurrentFile(file: PhelFile) {
        val path: String? = file.virtualFile?.path

        val namespace: String? = PhelNamespaceUtils.findNamespaceDeclaration(file)
            ?.let { PhelNamespaceUtils.extractShortNamespaceFromDeclaration(it) }

        fun owns(symbol: PhelProjectSymbol): Boolean = symbol.file.path == path
    }

    /** The tail text shown beside a project symbol in the lookup. */
    private val SymbolType.description: String
        get() = when (this) {
            SymbolType.FUNCTION -> "function"
            SymbolType.MACRO -> "macro"
            SymbolType.VALUE -> "value"
            SymbolType.STRUCT -> "struct"
            SymbolType.INTERFACE -> "interface"
            SymbolType.EXCEPTION -> "exception"
            SymbolType.ENUM -> "enum"
            SymbolType.PROTOCOL -> "protocol"
            SymbolType.RECORD -> "record"
            SymbolType.TYPE -> "type"
            SymbolType.MULTIMETHOD -> "multimethod"
        }
}
