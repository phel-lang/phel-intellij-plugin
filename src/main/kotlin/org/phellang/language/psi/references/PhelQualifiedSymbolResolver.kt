package org.phellang.language.psi.references

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiManager
import com.intellij.psi.search.FilenameIndex
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.util.PsiTreeUtil
import org.phellang.language.psi.PhelKeyword
import org.phellang.language.psi.PhelNamespaceUtils
import org.phellang.language.psi.PhelProjectNamespaceFinder
import org.phellang.language.psi.PhelSymbol
import org.phellang.language.psi.files.PhelFile
import org.phellang.language.psi.utils.PhelPsiUtils

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

        val results = mutableListOf<PsiElement>()
        results += PhelDefinitionFinder.collectVendorDefinitions(project, searchNamespace, symbolName)
        results += findInProjectFiles(project, qualifier, importedNamespace, symbolName)
        return results
    }

    private fun findInProjectFiles(
        project: com.intellij.openapi.project.Project,
        qualifier: String,
        importedNamespace: String?,
        symbolName: String,
    ): List<PsiElement> {
        val psiManager = PsiManager.getInstance(project)
        val phelFiles = FilenameIndex.getAllFilesByExt(project, "phel", GlobalSearchScope.projectScope(project))

        return phelFiles
            .mapNotNull { psiManager.findFile(it) as? PhelFile }
            .filter { declaresSearchedNamespace(it, qualifier, importedNamespace) }
            .flatMap { PhelDefinitionFinder.collectDefinitionsIn(it, symbolName) }
    }

    private fun declaresSearchedNamespace(file: PhelFile, qualifier: String, importedNamespace: String?): Boolean {
        val fileNamespace = PhelNamespaceUtils.extractNamespaceFromFile(file) ?: return false

        // Match on the full namespace when the import told us what it is; otherwise the qualifier is
        // all we have, so match files whose short namespace equals it.
        return if (importedNamespace != null) {
            fileNamespace == importedNamespace
        } else {
            PhelProjectNamespaceFinder.extractShortNamespace(fileNamespace) == qualifier
        }
    }

    /**
     * The full namespace a qualifier refers to, via the file's `(:require ...)` clauses — resolving
     * `:as` aliases as well as direct imports. Null when no import matches, in which case the caller
     * falls back to matching files by short namespace.
     */
    private fun resolveQualifierToNamespace(file: PhelFile, qualifier: String): String? {
        val nsDeclaration = PhelNamespaceUtils.findNamespaceDeclaration(file) ?: return null

        for (requireForm in PhelNamespaceUtils.findRequireForms(nsDeclaration)) {
            val forms = requireForm.forms

            var i = 1
            while (i < forms.size) {
                val namespaceSymbol = PhelPsiUtils.asSymbol(forms.getOrNull(i))
                if (namespaceSymbol == null) {
                    i++
                    continue
                }

                val fullNamespace = namespaceSymbol.text
                val alias = aliasFollowing(forms, i)

                if (alias != null) {
                    if (alias == qualifier) return fullNamespace
                    i += 3   // namespace, :as, alias
                    continue
                }

                if (PhelProjectNamespaceFinder.extractShortNamespace(fullNamespace) == qualifier) {
                    return fullNamespace
                }
                i++
            }
        }

        return null
    }

    /** The alias in `<ns> :as <alias>` starting at [index], or null when no `:as` follows. */
    private fun aliasFollowing(forms: List<org.phellang.language.psi.PhelForm>, index: Int): String? {
        if (index + 2 >= forms.size) return null

        val next = forms[index + 1]
        val asKeyword = next as? PhelKeyword ?: PsiTreeUtil.findChildOfType(next, PhelKeyword::class.java)
        if (asKeyword?.text != ":as") return null

        return PhelPsiUtils.asSymbol(forms[index + 2])?.text
    }
}
