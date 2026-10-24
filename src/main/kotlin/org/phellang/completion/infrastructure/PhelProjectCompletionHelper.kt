package org.phellang.completion.infrastructure

import com.intellij.codeInsight.completion.CompletionResultSet
import org.phellang.indexing.PhelProjectSymbolIndex
import org.phellang.language.psi.PhelNamespaceUtils
import org.phellang.language.psi.PhelProjectNamespaceFinder
import org.phellang.language.psi.files.PhelFile
import org.phellang.registry.PhelCompletionPriority
import org.phellang.registry.PhelProjectSymbol
import org.phellang.registry.SymbolType

/** Offers symbols defined elsewhere in the project, spelled the way they would have to be typed. */
object PhelProjectCompletionHelper {

    @JvmStatic
    fun addProjectCompletions(result: CompletionResultSet, file: PhelFile) {
        val index = PhelProjectSymbolIndex.getInstance(file.project)
        val current = CurrentFile(file)

        val imported = addImportedSymbols(result, index, file, current)
        addUnimportedSymbols(result, index, file, current, imported)
    }

    /**
     * Namespaces the file already requires, spelled with whatever qualifier is in force.
     *
     * Returns the namespaces handled here, normalized, so the second pass does not offer them again.
     */
    private fun addImportedSymbols(
        result: CompletionResultSet,
        index: PhelProjectSymbolIndex,
        file: PhelFile,
        current: CurrentFile,
    ): Set<String> {
        val handled = mutableSetOf<String>()

        for (import in PhelNamespaceUtils.extractImports(file)) {
            val namespace = PhelNamespaceUtils.normalizeNamespace(import.fullNamespace)
            // A namespace required twice is offered once.
            if (!handled.add(namespace)) continue
            // The registry offers it, under this alias too.
            if (PhelProjectNamespaceFinder.isStandardLibrary(namespace)) continue

            val qualifier = requiredQualifier(file, namespace)
            for (symbol in index.symbolsInNamespace(namespace)) {
                // The edited file's own symbols come from local completions, unqualified.
                if (current.owns(symbol)) continue

                addCompletion(result, symbol, "$qualifier/${symbol.name}")
            }
        }

        return handled
    }

    /** Everything else in the project, required on acceptance. */
    private fun addUnimportedSymbols(
        result: CompletionResultSet,
        index: PhelProjectSymbolIndex,
        file: PhelFile,
        current: CurrentFile,
        handled: Set<String>,
    ) {
        val byNamespace = index.getAllSymbols()
            .filterNot(current::owns)
            .groupBy { PhelNamespaceUtils.normalizeNamespace(it.namespace) }

        for ((namespace, symbols) in byNamespace) {
            // A stdlib namespace's functions come from the registry, documented and spelled the way it
            // spells them. A vendored copy in the index, `phel.core`'s `in-ns` buckets say, adds none.
            if (namespace in handled || PhelProjectNamespaceFinder.isStandardLibrary(namespace)) continue

            // Another file of the namespace being edited, an `in-ns` one say: its names need no qualifier.
            if (namespace == current.namespace) {
                symbols.forEach { addCompletion(result, it, it.name) }
                continue
            }

            val qualifier = unrequiredQualifier(file, namespace) ?: continue

            for (symbol in symbols) addCompletion(result, symbol, "$qualifier/${symbol.name}")
        }
    }

    /**
     * How a required [namespace] is written in front of a name: the alias it was required with, or its
     * short name for a plain require, while that still names it; else its full name, which always does.
     * The compiler keeps the last alias of a name, so a later require can take either one over.
     */
    private fun requiredQualifier(file: PhelFile, namespace: String): String {
        val shortName = PhelProjectNamespaceFinder.extractShortNamespace(namespace)

        return PhelNamespaceUtils.extractImports(file)
            .filter { PhelNamespaceUtils.normalizeNamespace(it.fullNamespace) == namespace }
            .map { it.alias ?: shortName }
            .firstOrNull { namedBy(file, it) == namespace }
            ?: namespace
    }

    /**
     * How a [namespace] completion will require is written: its short name, which the plain require
     * makes its alias, unless that already means another namespace here. Requiring it plainly would then
     * take the name over and re-point the file's existing calls, so it gets an alias of its own
     * (`app-util` for `app.util`), or is not offered when even that is taken.
     */
    private fun unrequiredQualifier(file: PhelFile, namespace: String): String? {
        val shortName = PhelProjectNamespaceFinder.extractShortNamespace(namespace)
        if (namedBy(file, shortName) == null) return shortName

        return namespace.replace('.', '-').takeIf { namedBy(file, it) == null }
    }

    /** The namespace, normalized, that [qualifier] names through the file's requires. */
    private fun namedBy(file: PhelFile, qualifier: String): String? =
        PhelNamespaceUtils.importedNamespaceFor(file, qualifier)?.let(PhelNamespaceUtils::normalizeNamespace)

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

    /** The edited file's identity. Both its path and its namespace, normalized, suppress a suggestion. */
    private class CurrentFile(file: PhelFile) {
        val path: String? = file.virtualFile?.path

        val namespace: String? = PhelNamespaceUtils.fileNamespace(file)
            ?.let(PhelNamespaceUtils::normalizeNamespace)

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
