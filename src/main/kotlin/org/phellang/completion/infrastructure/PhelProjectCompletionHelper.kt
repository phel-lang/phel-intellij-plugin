package org.phellang.completion.infrastructure

import com.intellij.codeInsight.completion.CompletionResultSet
import org.phellang.registry.PhelProjectSymbol
import org.phellang.registry.SymbolType
import org.phellang.registry.indexing.PhelProjectSymbolIndex
import org.phellang.language.psi.PhelList
import org.phellang.language.psi.PhelNamespaceUtils
import org.phellang.language.psi.PhelRequireClauseAnalyzer
import org.phellang.language.psi.files.PhelFile
import org.phellang.registry.PhelCompletionPriority

object PhelProjectCompletionHelper {

    @JvmStatic
    fun addProjectCompletions(
        result: CompletionResultSet, file: PhelFile, aliasMap: Map<String, String>
    ) {
        val index = PhelProjectSymbolIndex.getInstance(file.project)
        val importedNamespaces = PhelRequireClauseAnalyzer.imports(file)
        val currentFileNamespace = PhelNamespaceUtils.findNamespaceDeclaration(file)?.let {
            extractNamespaceFromDeclaration(it)
        }
        val currentFilePath = file.virtualFile?.path

        // Track which namespaces we've already added (to avoid duplicates)
        val addedNamespaces = mutableSetOf<String>()

        // 1. Add completions for imported namespaces (with proper aliases)
        for (importInfo in importedNamespaces) {
            val symbols = index.getSymbolsForNamespace(importInfo.shortNamespace)
            addedNamespaces.add(importInfo.shortNamespace)

            for (symbol in symbols) {
                // Skip symbols from the current file (already provided by local completions)
                if (symbol.file.path == currentFilePath) {
                    continue
                }

                val displayName = transformWithAlias(symbol, importInfo, aliasMap)
                addProjectSymbolCompletion(result, symbol, displayName)
            }
        }

        // 2. Add completions for ALL other project namespaces (not yet imported)
        // These will be auto-imported when selected
        val allSymbols = index.getAllSymbols()
        for (symbol in allSymbols) {
            // Skip if from current file
            if (symbol.file.path == currentFilePath) {
                continue
            }

            // Skip if namespace already handled above (imported)
            if (symbol.shortNamespace in addedNamespaces) {
                continue
            }

            // Skip current file's namespace
            if (symbol.shortNamespace == currentFileNamespace) {
                continue
            }

            // Add with qualified name (namespace/function)
            addProjectSymbolCompletion(result, symbol, symbol.qualifiedName)
        }
    }

    private fun extractNamespaceFromDeclaration(nsDeclaration: PhelList): String? {
        return PhelNamespaceUtils.extractShortNamespaceFromDeclaration(nsDeclaration)
    }

    private fun transformWithAlias(
        symbol: PhelProjectSymbol, importInfo: PhelRequireClauseAnalyzer.RequireImport, aliasMap: Map<String, String>
    ): String {
        // If the import has an alias, use it
        if (importInfo.alias != null) {
            return "${importInfo.alias}/${symbol.name}"
        }

        // Check if there's an alias for this namespace in the aliasMap
        val alias = aliasMap.entries.find { it.value == symbol.shortNamespace }?.key
        return if (alias != null) {
            "$alias/${symbol.name}"
        } else {
            symbol.qualifiedName
        }
    }

    private fun addProjectSymbolCompletion(
        result: CompletionResultSet, symbol: PhelProjectSymbol, displayName: String
    ) {
        val description = when (symbol.type) {
            SymbolType.FUNCTION -> "function"
            SymbolType.MACRO -> "macro"
            SymbolType.VALUE -> "value"
            SymbolType.STRUCT -> "struct"
            SymbolType.INTERFACE -> "interface"
            SymbolType.EXCEPTION -> "exception"
        }

        PhelCompletionUtils.addRankedCompletionWithNamespace(
            result, displayName, symbol.signature, description, PhelCompletionPriority.PROJECT_SYMBOLS, symbol.namespace
        )
    }

}
