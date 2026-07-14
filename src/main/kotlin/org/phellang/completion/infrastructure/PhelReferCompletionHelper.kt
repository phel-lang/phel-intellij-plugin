package org.phellang.completion.infrastructure

import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.completion.PrioritizedLookupElement
import com.intellij.codeInsight.lookup.LookupElementBuilder
import org.phellang.registry.Namespace
import org.phellang.registry.PhelFunctionRegistry
import org.phellang.registry.indexing.PhelProjectSymbolIndex
import org.phellang.language.infrastructure.PhelIcons
import org.phellang.language.psi.PhelProjectNamespaceFinder
import org.phellang.language.psi.files.PhelFile
import org.phellang.registry.PhelCompletionPriority

object PhelReferCompletionHelper {

    /**
     * Adds completions for symbols that can be referred from a namespace.
     * 
     * @param result The completion result set
     * @param namespaceText The full namespace text (e.g., "phel\test")
     * @param file The current file (for project symbol lookup)
     * @param alreadyReferred Symbols already in the :refer vector (to exclude from suggestions)
     */
    @JvmStatic
    fun addReferCompletions(
        result: CompletionResultSet,
        namespaceText: String,
        file: PhelFile?,
        alreadyReferred: Set<String> = emptySet()
    ) {
        val shortNamespace = PhelProjectNamespaceFinder.extractShortNamespace(namespaceText)

        // Try standard library first
        val namespace = mapToNamespace(shortNamespace)
        if (namespace != null) {
            addStandardLibraryCompletions(result, namespace, shortNamespace, alreadyReferred)
        }

        // Also check project symbols
        if (file != null) {
            addProjectSymbolCompletions(result, shortNamespace, file, alreadyReferred)
        }
    }

    private fun mapToNamespace(shortNamespace: String): Namespace? =
        Namespace.fromShortName(shortNamespace)

    private fun addStandardLibraryCompletions(
        result: CompletionResultSet,
        namespace: Namespace,
        shortNamespace: String,
        alreadyReferred: Set<String>
    ) {
        val functions = PhelFunctionRegistry.getFunctions(namespace)

        for (function in functions) {
            // Extract just the function name (e.g., "test/deftest" -> "deftest")
            val functionName = function.name.substringAfter("/")

            // Skip if already referred
            if (functionName in alreadyReferred) {
                continue
            }

            val summaryText = function.documentation.summary.take(50)
            val tailText = if (summaryText.length == 50) " - $summaryText..." else " - $summaryText"

            val element = LookupElementBuilder.create(functionName)
                .withIcon(PhelIcons.FILE)
                .withTypeText(function.signature)
                .withTailText(tailText, true)

            result.addElement(
                PrioritizedLookupElement.withPriority(element, PhelCompletionPriority.REFER_COMPLETIONS.value)
            )
        }
    }

    private fun addProjectSymbolCompletions(
        result: CompletionResultSet,
        shortNamespace: String,
        file: PhelFile,
        alreadyReferred: Set<String>
    ) {
        val index = PhelProjectSymbolIndex.getInstance(file.project)
        val symbols = index.getSymbolsForNamespace(shortNamespace)

        for (symbol in symbols) {
            // Skip symbols from the current file
            if (symbol.file.path == file.virtualFile?.path) {
                continue
            }

            // Skip if already referred
            if (symbol.name in alreadyReferred) {
                continue
            }

            val element = LookupElementBuilder.create(symbol.name)
                .withIcon(PhelIcons.FILE)
                .withTypeText(symbol.signature)
                .withTailText(" - ${symbol.type.name.lowercase()}", true)

            result.addElement(
                PrioritizedLookupElement.withPriority(element, PhelCompletionPriority.PROJECT_SYMBOLS.value)
            )
        }
    }
}
