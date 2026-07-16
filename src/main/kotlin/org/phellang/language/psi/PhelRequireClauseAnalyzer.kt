package org.phellang.language.psi

import com.intellij.psi.util.PsiTreeUtil
import org.phellang.language.psi.files.PhelFile
import org.phellang.language.psi.utils.PhelPsiUtils

/**
 * Reads the `(:require ...)` clauses of a file's `(ns ...)` form: the `:as` aliases and the `:refer`
 * imports. Both walk a require spec's forms looking for a keyword and its following operand, so the
 * traversal is shared.
 *
 * Pure computation — [PhelNamespaceUtils] owns the per-file caching and delegates here.
 */
internal object PhelRequireClauseAnalyzer {
    /** One `(:require ...)` entry: the namespace as written, its short form, and its `:as` alias. */
    data class RequireImport(val fullNamespace: String, val shortNamespace: String, val alias: String?)

    /** Every namespace imported by [file]'s `(:require ...)` clauses. */
    fun imports(file: PhelFile): List<RequireImport> {
        val nsDeclaration = PhelNamespaceUtils.findNamespaceDeclaration(file) ?: return emptyList()

        val imports = mutableListOf<RequireImport>()
        for (requireForm in PhelNamespaceUtils.findRequireForms(nsDeclaration)) {
            forEachSpec(requireForm.forms) { namespaceText, aliasAt ->
                val short = PhelProjectNamespaceFinder.extractShortNamespace(namespaceText)
                imports += RequireImport(namespaceText, short, aliasAt)
            }
        }
        return imports
    }

    /** alias -> short namespace, e.g. `s -> string` for `(:require phel\string :as s)`. */
    fun computeAliasMap(file: PhelFile): Map<String, String> =
        imports(file)
            .mapNotNull { import -> import.alias?.let { it to import.shortNamespace } }
            .toMap()

    /** Symbols brought in unqualified via `(:require [ns :refer [a b c]])`. */
    fun computeReferredSymbols(file: PhelFile): Set<String> {
        val nsDeclaration = PhelNamespaceUtils.findNamespaceDeclaration(file) ?: return emptySet()

        val referred = mutableSetOf<String>()
        for (requireForm in PhelNamespaceUtils.findRequireForms(nsDeclaration)) {
            referSymbolsIn(requireForm.forms).forEach { referred += it }
        }
        return referred
    }

    /**
     * The namespace a `:refer`-imported [symbolName] comes from — the head of the require clause
     * that refers it — or null when it isn't refer'd from any require.
     */
    fun findReferSource(file: PhelFile, symbolName: String): String? {
        val nsDeclaration = PhelNamespaceUtils.findNamespaceDeclaration(file) ?: return null

        for (requireForm in PhelNamespaceUtils.findRequireForms(nsDeclaration)) {
            val forms = requireForm.forms
            val sourceNamespace = forms.firstOrNull()?.let(PhelPsiUtils::asSymbol)?.text ?: continue
            if (symbolName in referSymbolsIn(forms)) return sourceNamespace
        }
        return null
    }

    /** The symbols in every `:refer [ …]` vector within one require spec's [forms]. */
    private fun referSymbolsIn(forms: List<PhelForm>): Set<String> {
        val result = mutableSetOf<String>()
        var i = 1
        while (i < forms.size) {
            if (keywordTextAt(forms, i) == ":refer" && i + 1 < forms.size) {
                PsiTreeUtil.findChildrenOfType(forms[i + 1], PhelSymbol::class.java)
                    .mapNotNull { it.text }
                    // A qualified symbol or PHP FQN in a :refer vector is not a bare referred name.
                    .filter { !it.contains('\\') && !it.contains('/') }
                    .forEach { result += it }
                i += 2
            } else {
                i++
            }
        }
        return result
    }

    /**
     * Visits each namespace spec in a require clause's [forms], reporting the namespace text and,
     * when the spec is `<ns> :as <alias>`, the alias. Advances past the `:as alias` pair so it is
     * not re-read as another namespace.
     */
    private inline fun forEachSpec(forms: List<PhelForm>, action: (namespaceText: String, aliasAt: String?) -> Unit) {
        var i = 1
        while (i < forms.size) {
            val namespaceSymbol = PhelPsiUtils.asSymbol(forms[i])
            if (namespaceSymbol == null) {
                i++
                continue
            }

            val alias = aliasFollowing(forms, i)
            action(namespaceSymbol.text, alias)
            i += if (alias != null) 3 else 1
        }
    }

    /** The alias in `<ns> :as <alias>` starting at [index], or null when no `:as` follows. */
    private fun aliasFollowing(forms: List<PhelForm>, index: Int): String? {
        if (index + 2 >= forms.size) return null
        if (keywordTextAt(forms, index + 1) != ":as") return null
        return PhelPsiUtils.asSymbol(forms[index + 2])?.text
    }

    private fun keywordTextAt(forms: List<PhelForm>, index: Int): String? {
        return PhelPsiUtils.asKeyword(forms.getOrNull(index))?.text
    }
}
