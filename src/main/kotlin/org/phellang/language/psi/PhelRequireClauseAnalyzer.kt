package org.phellang.language.psi

import com.intellij.psi.PsiElement
import org.phellang.language.psi.files.PhelFile
import org.phellang.language.psi.utils.PhelPsiUtils

/**
 * Reads the `(:require ...)` clauses of a file's `(ns ...)` form: the namespaces they import, their
 * `:as` aliases and their `:refer` lists.
 *
 * Every reader goes through [specsIn], which knows the two spellings the compiler accepts
 * (`NsSymbol::analyzeRequire`): a flat spec, `a.b :as x :refer [y]`, and a vector spec,
 * `[a.b :as x :refer [y]]`, with the options in any order. A `:refer [...]` vector is an option of
 * the spec before it, never a namespace of its own, and `#_`-discarded forms are not there at all.
 *
 * Pure computation: [PhelNamespaceUtils] owns the per-file caching and delegates here.
 */
internal object PhelRequireClauseAnalyzer {
    /** One `(:require ...)` entry: the namespace as written, its short form, and its `:as` alias. */
    data class RequireImport(val fullNamespace: String, val shortNamespace: String, val alias: String?)

    /** One namespace spec of a require clause: the namespace as written, its alias, and its `:refer` vectors. */
    private class Spec(val namespace: String, val alias: String?, val referVectors: List<PhelForm>)

    /** Every namespace imported by [file]'s `(:require ...)` clauses. */
    fun imports(file: PhelFile): List<RequireImport> =
        specsOf(file).map { spec ->
            RequireImport(spec.namespace, PhelProjectNamespaceFinder.extractShortNamespace(spec.namespace), spec.alias)
        }

    /** alias -> short namespace, e.g. `s -> string` for `(:require phel\string :as s)`. */
    fun computeAliasMap(file: PhelFile): Map<String, String> =
        imports(file)
            .mapNotNull { import -> import.alias?.let { it to import.shortNamespace } }
            .toMap()

    /** Symbols brought in unqualified via `(:require ns :refer [a b c])`. */
    fun computeReferredSymbols(file: PhelFile): Set<String> =
        specsOf(file).flatMapTo(mutableSetOf()) { spec -> spec.referVectors.flatMap(::referredNamesIn) }

    /**
     * The namespace a `:refer`-imported [symbolName] comes from, as written, or null when it isn't
     * refer'd from any require.
     *
     * One clause can hold several specs (`(:require a.b :as b c.d :refer [x])`), so the answer is the
     * spec whose `:refer` lists the name, never the clause's `:require` head. That head is what this
     * once returned, which made every referred name look unreferred.
     */
    fun findReferSource(file: PhelFile, symbolName: String): String? =
        specsOf(file).firstOrNull { spec -> spec.referVectors.any { symbolName in referredNamesIn(it) } }?.namespace

    /**
     * The namespace, as written, whose `:refer` is [vector] in [requireForm]; null when [vector] is no
     * `:refer` vector there. The spec that owns it, flat or `[ns :refer [...]]`, never the clause's first
     * namespace: `(:require a.b :as b c.d :refer [x])` refers `x` from `c.d`.
     */
    fun namespaceReferringWith(requireForm: PhelList, vector: PhelVec): String? =
        specsIn(requireForm).firstOrNull { spec -> spec.referVectors.any { unwrapped(it) === vector } }?.namespace

    private fun specsOf(file: PhelFile): List<Spec> {
        val nsDeclaration = PhelNamespaceUtils.findNamespaceDeclaration(file) ?: return emptyList()
        return PhelNamespaceUtils.findRequireForms(nsDeclaration).flatMap(::specsIn)
    }

    /** The specs of one `(:require ...)` clause, flat and vector alike. */
    private fun specsIn(requireForm: PhelList): List<Spec> {
        val forms = PhelPsiUtils.activeForms(requireForm)
        val specs = mutableListOf<Spec>()

        var i = 1
        while (i < forms.size) {
            val form = forms[i]
            if (unwrapped(form) is PhelVec) {
                specAt(PhelPsiUtils.activeForms(unwrapped(form)), 0)?.let { specs += it.first }
                i++
                continue
            }

            val spec = specAt(forms, i)
            if (spec == null) {
                i++
                continue
            }
            specs += spec.first
            i = spec.second
        }

        return specs
    }

    /**
     * The spec whose namespace is at [start] in [forms], with every `:as` / `:refer` option after it,
     * and the index just past them; null when [start] holds no namespace symbol.
     */
    private fun specAt(forms: List<PhelForm>, start: Int): Pair<Spec, Int>? {
        val namespace = namespaceAt(forms, start) ?: return null
        var alias: String? = null
        val referVectors = mutableListOf<PhelForm>()

        var i = start + 1
        while (i < forms.size) {
            when (keywordAt(forms, i)) {
                ":as" -> alias = namespaceAt(forms, i + 1) ?: alias
                ":refer" -> forms.getOrNull(i + 1)?.let { referVectors += it }
                else -> break
            }
            i += 2
        }

        return Spec(namespace, alias, referVectors) to i
    }

    /** The symbol text at [index] when that form is a bare symbol, not a keyword or a collection. */
    private fun namespaceAt(forms: List<PhelForm>, index: Int): String? {
        val form = unwrapped(forms.getOrNull(index) ?: return null)
        if (form is PhelKeyword || form is PhelVec || form is PhelList || form is PhelMap) return null

        return PhelPsiUtils.asSymbol(form)?.text
    }

    /**
     * The keyword text at [index] when that form *is* a keyword. `PhelPsiUtils.asKeyword` would also
     * find one nested inside a vector spec, reading `[a.b :refer [x]]` as a bare `:refer`.
     */
    private fun keywordAt(forms: List<PhelForm>, index: Int): String? {
        val form = forms.getOrNull(index) ?: return null
        return (unwrapped(form) as? PhelKeyword)?.text
    }

    /** The bare names in one `:refer [ …]` vector, `#_`-discarded ones left out. */
    private fun referredNamesIn(vector: PhelForm): List<String> {
        val refers = unwrapped(vector) as? PhelVec ?: return emptyList()

        return PhelPsiUtils.activeForms(refers)
            .mapNotNull { PhelPsiUtils.asSymbol(it)?.text }
            // A qualified symbol or PHP FQN in a :refer vector is not a bare referred name.
            .filter { !it.contains('\\') && !it.contains('/') }
    }

    /** [form] with the reader's generic `form` wrapper (metadata, reader macros) peeled off. */
    private fun unwrapped(form: PsiElement): PsiElement {
        if (form.node?.elementType == PhelTypes.FORM) {
            form.children.firstOrNull { it is PhelForm }?.let { return unwrapped(it) }
        }

        return form
    }
}
