package org.phellang.completion.infrastructure

import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.psi.util.PsiTreeUtil
import org.phellang.completion.data.PhelProjectSymbol
import org.phellang.completion.data.SymbolType
import org.phellang.completion.indexing.PhelProjectSymbolIndex
import org.phellang.language.psi.PhelForm
import org.phellang.language.psi.PhelKeyword
import org.phellang.language.psi.PhelList
import org.phellang.language.psi.PhelNamespaceUtils
import org.phellang.language.psi.PhelSymbol
import org.phellang.language.psi.files.PhelFile

object PhelProjectCompletionHelper {

    @JvmStatic
    fun addProjectCompletions(
        result: CompletionResultSet, file: PhelFile, aliasMap: Map<String, String>
    ) {
        val index = PhelProjectSymbolIndex.getInstance(file.project)
        val importedNamespaces = extractImportedNamespaces(file)
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

    private fun extractImportedNamespaces(file: PhelFile): List<ImportInfo> {
        val imports = mutableListOf<ImportInfo>()
        val nsDeclaration = PhelNamespaceUtils.findNamespaceDeclaration(file) ?: return imports
        val requireForms = PhelNamespaceUtils.findRequireForms(nsDeclaration)

        for (requireForm in requireForms) {
            val forms = PsiTreeUtil.getChildrenOfType(requireForm, PhelForm::class.java) ?: continue

            // Skip the :require keyword (first form)
            var i = 1
            while (i < forms.size) {
                val form = forms[i]

                // Get the namespace symbol
                val namespaceSymbol = if (form is PhelSymbol) form
                else PsiTreeUtil.findChildOfType(form, PhelSymbol::class.java)

                if (namespaceSymbol != null) {
                    val fullNamespace = namespaceSymbol.text
                    val shortNamespace = fullNamespace.substringAfterLast("\\")
                    var alias: String?

                    // Check if next form is :as keyword
                    if (i + 1 < forms.size) {
                        val nextForm = forms[i + 1]
                        val asKeyword =
                            nextForm as? PhelKeyword ?: PsiTreeUtil.findChildOfType(nextForm, PhelKeyword::class.java)

                        if (asKeyword?.text == ":as" && i + 2 < forms.size) {
                            // Get the alias symbol
                            val aliasForm = forms[i + 2]
                            val aliasSymbol = if (aliasForm is PhelSymbol) aliasForm
                            else PsiTreeUtil.findChildOfType(aliasForm, PhelSymbol::class.java)

                            if (aliasSymbol != null) {
                                alias = aliasSymbol.text
                                i += 3  // Skip namespace, :as, and alias
                                imports.add(ImportInfo(fullNamespace, shortNamespace, alias))
                                continue
                            }
                        }
                    }

                    imports.add(ImportInfo(fullNamespace, shortNamespace, null))
                }
                i++
            }
        }

        return imports
    }

    private fun extractNamespaceFromDeclaration(nsDeclaration: PhelList): String? {
        return PhelNamespaceUtils.extractShortNamespaceFromDeclaration(nsDeclaration)
    }

    private fun transformWithAlias(
        symbol: PhelProjectSymbol, importInfo: ImportInfo, aliasMap: Map<String, String>
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
        val priority = when (symbol.type) {
            SymbolType.FUNCTION -> PhelCompletionPriority.PROJECT_SYMBOLS
            SymbolType.MACRO -> PhelCompletionPriority.PROJECT_SYMBOLS
            SymbolType.VALUE -> PhelCompletionPriority.PROJECT_SYMBOLS
            SymbolType.STRUCT -> PhelCompletionPriority.PROJECT_SYMBOLS
            SymbolType.INTERFACE -> PhelCompletionPriority.PROJECT_SYMBOLS
        }

        val description = when (symbol.type) {
            SymbolType.FUNCTION -> "function"
            SymbolType.MACRO -> "macro"
            SymbolType.VALUE -> "value"
            SymbolType.STRUCT -> "struct"
            SymbolType.INTERFACE -> "interface"
        }

        PhelCompletionUtils.addRankedCompletionWithNamespace(
            result, displayName, symbol.signature, description, priority, symbol.namespace
        )
    }

    private data class ImportInfo(
        val fullNamespace: String, val shortNamespace: String, val alias: String?
    )
}
