package org.phellang.language.psi.references

import com.intellij.psi.PsiElement
import org.phellang.language.psi.PhelNamespaceFiles
import org.phellang.language.psi.PhelProjectNamespaceFinder
import org.phellang.language.psi.PhelRequireClauseAnalyzer
import org.phellang.language.psi.PhelSymbol
import org.phellang.language.psi.files.PhelFile

/**
 * Resolves a namespace-qualified symbol — `utils/greet`, `m/square`, `string/join` — to its
 * definition, covering direct imports, `:as` aliases, namespaces that were never imported (matched
 * by short name), and the standard library.
 *
 * PHP interop (`ClassName/method`) is *not* handled here: the caller tries [PhpClassResolver] first,
 * since a PHP class qualifier must never be matched against a Phel namespace.
 */
internal object PhelQualifiedSymbolResolver {
    fun resolve(symbol: PhelSymbol, qualifier: String, symbolName: String): List<PsiElement> {
        val containingFile = symbol.containingFile as? PhelFile ?: return emptyList()
        val project = symbol.project

        // An import may alias the qualifier (`:as m`), in which case the namespace to search for is
        // the aliased one rather than the text the user typed.
        val importedNamespace = resolveQualifierToNamespace(containingFile, qualifier)
        val searchNamespace = importedNamespace
            ?.let(PhelProjectNamespaceFinder::extractShortNamespace)
            ?: qualifier

        // distinct: a stdlib file under a `vendor/` the project includes is reached both ways.
        return (PhelDefinitionFinder.collectVendorDefinitions(project, searchNamespace, symbolName) +
                findInProjectFiles(project, qualifier, importedNamespace, symbolName)).distinct()
    }

    /**
     * The definitions in the files of the qualifier's namespace: the imported namespace when the file
     * requires it, else every namespace whose short name is the qualifier (no require to go by).
     */
    private fun findInProjectFiles(
        project: com.intellij.openapi.project.Project,
        qualifier: String,
        importedNamespace: String?,
        symbolName: String,
    ): List<PsiElement> {
        val files = if (importedNamespace != null) {
            PhelNamespaceFiles.filesOf(project, importedNamespace)
        } else {
            PhelNamespaceFiles.filesWithShortNamespace(project, qualifier)
        }

        return files.flatMap { PhelDefinitionFinder.collectDefinitionsIn(it, symbolName) }
    }

    /**
     * The full namespace a qualifier refers to, via the file's `(:require ...)` clauses — resolving
     * `:as` aliases as well as direct imports. Null when no import matches, in which case the caller
     * falls back to matching files by short namespace.
     */
    private fun resolveQualifierToNamespace(file: PhelFile, qualifier: String): String? {
        return PhelRequireClauseAnalyzer.imports(file).firstOrNull { spec ->
            // An aliased import is referenced by its alias only; a plain one by its short name.
            if (spec.alias != null) spec.alias == qualifier else spec.shortNamespace == qualifier
        }?.fullNamespace
    }
}
