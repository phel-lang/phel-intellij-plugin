package org.phellang.language.psi

import com.intellij.psi.util.PsiTreeUtil
import org.phellang.language.psi.files.PhelFile

object PhelNamespaceUtils {

    fun findNamespaceDeclaration(file: PhelFile): PhelList? {
        // (ns my-ns (:require ...) (:use ...))
        val lists = PsiTreeUtil.findChildrenOfType(file, PhelList::class.java)
        return lists.firstOrNull { list ->
            val forms = list.forms
            if (forms.isEmpty()) return@firstOrNull false

            val firstForm = forms[0]
            val firstSymbol = if (firstForm is PhelSymbol) firstForm
            else PsiTreeUtil.findChildOfType(firstForm, PhelSymbol::class.java)
            firstSymbol?.text == "ns"
        }
    }

    fun isNamespaceRequired(nsDeclaration: PhelList, namespace: String): Boolean {
        val target = normalizeNamespace(namespace)
        val requireForms = findRequireForms(nsDeclaration)
        return requireForms.any { requireForm ->
            // Check if the require form contains the namespace (either dot or backslash form)
            val symbols = PsiTreeUtil.findChildrenOfType(requireForm, PhelSymbol::class.java)
            symbols.any { sym -> sym.text?.let { normalizeNamespace(it) } == target }
        }
    }

    fun findRequireForms(nsDeclaration: PhelList): List<PhelList> {
        return PsiTreeUtil.findChildrenOfType(nsDeclaration, PhelList::class.java).filter { list ->
            val forms = list.forms
            if (forms.isEmpty()) return@filter false

            val firstForm = forms[0]
            val firstKeyword = firstForm as? PhelKeyword
                ?: PsiTreeUtil.findChildOfType(firstForm, PhelKeyword::class.java)
            firstKeyword?.text == ":require"
        }
    }

    fun extractAliasMap(file: PhelFile): Map<String, String> {
        val aliasMap = mutableMapOf<String, String>()

        val nsDeclaration = findNamespaceDeclaration(file) ?: return aliasMap
        val requireForms = findRequireForms(nsDeclaration)

        for (requireForm in requireForms) {
            val forms = requireForm.forms

            var i = 1
            while (i < forms.size) {
                val form = forms[i]

                // Get the namespace symbol
                val namespaceSymbol = if (form is PhelSymbol) form
                else PsiTreeUtil.findChildOfType(form, PhelSymbol::class.java)

                if (namespaceSymbol != null) {
                    val namespace = namespaceSymbol.text

                    // Check if next form is :as keyword
                    if (i + 1 < forms.size) {
                        val nextForm = forms[i + 1]
                        val asKeyword = nextForm as? PhelKeyword
                            ?: PsiTreeUtil.findChildOfType(nextForm, PhelKeyword::class.java)

                        if (asKeyword?.text == ":as" && i + 2 < forms.size) {
                            // Get the alias symbol
                            val aliasForm = forms[i + 2]
                            val aliasSymbol = if (aliasForm is PhelSymbol) aliasForm
                            else PsiTreeUtil.findChildOfType(aliasForm, PhelSymbol::class.java)

                            if (aliasSymbol != null) {
                                val alias = aliasSymbol.text
                                val shortNamespace = PhelProjectNamespaceFinder.extractShortNamespace(namespace)
                                aliasMap[alias] = shortNamespace
                                i += 3  // Skip namespace, :as, and alias
                                continue
                            }
                        }
                    }
                }
                i++
            }
        }

        return aliasMap
    }

    /**
     * Extracts the namespace name from a function name.
     * e.g., "str/contains?" -> "str"
     *       "contains?" -> null (no namespace)
     */
    fun extractNamespace(functionName: String): String? {
        return if (functionName.contains("/")) {
            functionName.substringBefore("/")
        } else {
            null
        }
    }

    /**
     * Canonicalises a namespace to dot-separated form (Phel 0.35+).
     * Accepts the legacy backslash form so callers can compare namespaces written
     * either way without caring which the user typed.
     *
     * e.g., "phel\\string" -> "phel.string"
     *       "phel.string"  -> "phel.string"
     */
    fun normalizeNamespace(namespace: String): String {
        return namespace.replace('\\', '.')
    }

    fun extractNamespaceFromDeclaration(nsDeclaration: PhelList): String? {
        val forms = nsDeclaration.forms
        if (forms.size < 2) return null

        val namespaceSymbol = forms[1] as? PhelSymbol
            ?: PsiTreeUtil.findChildOfType(forms[1], PhelSymbol::class.java)
        return namespaceSymbol?.text
    }

    fun extractShortNamespaceFromDeclaration(nsDeclaration: PhelList): String? {
        return extractNamespaceFromDeclaration(nsDeclaration)
            ?.let(PhelProjectNamespaceFinder::extractShortNamespace)
    }

    fun extractNamespaceFromFile(file: PhelFile): String? {
        val nsDeclaration = findNamespaceDeclaration(file) ?: return null
        return extractNamespaceFromDeclaration(nsDeclaration)
    }

    /**
     * Converts a short namespace to its canonical Phel form (Phel 0.35+ dot-separated).
     * e.g., "string" -> "phel.string"
     *       "http"   -> "phel.http"
     */
    fun toPhelNamespace(shortNamespace: String): String {
        return "phel.$shortNamespace"
    }

    fun isCoreNamespace(namespace: String?): Boolean {
        return namespace == null || namespace == "core"
    }

    fun isNamespaceImportedOrAliased(file: PhelFile, shortNamespace: String): Boolean {
        if (isCoreNamespace(shortNamespace)) {
            return true
        }

        val nsDeclaration = findNamespaceDeclaration(file) ?: return false
        val phelNamespace = toPhelNamespace(shortNamespace)

        // Check direct import
        if (isNamespaceRequired(nsDeclaration, phelNamespace)) {
            return true
        }

        // Check if imported via alias
        val aliasMap = extractAliasMap(file)
        return aliasMap.values.contains(shortNamespace)
    }

    fun extractReferredSymbols(file: PhelFile): Set<String> {
        val referredSymbols = mutableSetOf<String>()

        val nsDeclaration = findNamespaceDeclaration(file) ?: return referredSymbols
        val requireForms = findRequireForms(nsDeclaration)

        for (requireForm in requireForms) {
            val forms = requireForm.forms

            var i = 1
            while (i < forms.size) {
                val form = forms[i]

                // Check if this is a :refer keyword
                val keyword = form as? PhelKeyword
                    ?: PsiTreeUtil.findChildOfType(form, PhelKeyword::class.java)

                if (keyword?.text == ":refer" && i + 1 < forms.size) {
                    // Next form should be a vector with symbols
                    val vectorForm = forms[i + 1]

                    // Extract symbols from the vector (vec contains PhelSymbol children)
                    val symbols = PsiTreeUtil.findChildrenOfType(vectorForm, PhelSymbol::class.java)
                    for (symbol in symbols) {
                        val symbolText = symbol.text
                        // Skip namespace-qualified symbols (either separator) and PHP FQNs.
                        if (symbolText != null && !symbolText.contains("\\") && !symbolText.contains("/")) {
                            referredSymbols.add(symbolText)
                        }
                    }

                    i += 2 // Skip :refer and the vector
                    continue
                }

                i++
            }
        }

        return referredSymbols
    }

    fun isReferredSymbol(file: PhelFile, symbolName: String): Boolean {
        return extractReferredSymbols(file).contains(symbolName)
    }

    /**
     * If [symbolName] is referred from a `(:require [ns :refer [...]])` clause in [file],
     * returns the source namespace (the head of that require clause). Returns null when
     * the symbol isn't refer'd from any require, so callers can fall back to other lookups.
     */
    fun findReferSource(file: PhelFile, symbolName: String): String? {
        val nsDeclaration = findNamespaceDeclaration(file) ?: return null
        val requireForms = findRequireForms(nsDeclaration)

        for (requireForm in requireForms) {
            val forms = requireForm.forms
            if (forms.isEmpty()) continue

            val sourceNamespace = (forms[0] as? PhelSymbol)?.text
                ?: PsiTreeUtil.findChildOfType(forms[0], PhelSymbol::class.java)?.text
                ?: continue

            var i = 1
            while (i < forms.size) {
                val keyword = (forms[i] as? PhelKeyword)
                    ?: PsiTreeUtil.findChildOfType(forms[i], PhelKeyword::class.java)
                if (keyword?.text == ":refer" && i + 1 < forms.size) {
                    val symbols = PsiTreeUtil.findChildrenOfType(forms[i + 1], PhelSymbol::class.java)
                    if (symbols.any { it.text == symbolName }) {
                        return sourceNamespace
                    }
                    i += 2
                    continue
                }
                i++
            }
        }
        return null
    }
}
