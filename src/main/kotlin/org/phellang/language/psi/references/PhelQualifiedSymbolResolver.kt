package org.phellang.language.psi.references

import com.intellij.psi.PsiElement
import org.phellang.language.psi.PhelNamespaceFiles
import org.phellang.language.psi.PhelNamespaceUtils
import org.phellang.language.psi.PhelProjectNamespaceFinder
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
     * requires it. With no require to go by, the compiler reads the qualifier as the namespace itself
     * (`SymbolResolver::resolveWithAlias`); a namespace whose short name is the qualifier is also
     * accepted, a leniency the plugin keeps.
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
            (PhelNamespaceFiles.filesOf(project, qualifier) + PhelNamespaceFiles.filesWithShortNamespace(project, qualifier))
                .distinct()
        }

        return files.flatMap { PhelDefinitionFinder.collectDefinitionsIn(it, symbolName) }
    }

    /**
     * The full namespace a qualifier refers to, via the file's `(:require ...)` clauses. Null when no
     * import matches, in which case the caller looks the qualifier up as a namespace name.
     *
     * An aliased import is referenced by its alias, a plain one by its short name (the compiler's
     * default alias), and either by its full name, which a qualifier that is no alias always means.
     * The full name used to be missed, so `(app.util/helper)` resolved to nothing and Safe Delete
     * reported a live function as unused.
     */
    private fun resolveQualifierToNamespace(file: PhelFile, qualifier: String): String? {
        val asWritten = PhelNamespaceUtils.normalizeNamespace(qualifier)

        return PhelNamespaceUtils.extractImports(file).firstOrNull { spec ->
            val byAlias = if (spec.alias != null) spec.alias == qualifier else spec.shortNamespace == qualifier
            byAlias || PhelNamespaceUtils.normalizeNamespace(spec.fullNamespace) == asWritten
        }?.fullNamespace
    }
}
