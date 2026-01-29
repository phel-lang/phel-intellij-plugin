package org.phellang.language.psi

import com.intellij.psi.util.PsiTreeUtil
import org.phellang.language.psi.files.PhelFile

/**
 * Utilities for working with Phel namespace declarations.
 */
object PhelNamespaceUtils {

    /**
     * Finds the namespace declaration (ns ...) in a Phel file.
     * Returns the PhelList element representing the (ns ...) form.
     */
    fun findNamespaceDeclaration(file: PhelFile): PhelList? {
        // (ns my-ns (:require ...) (:use ...))
        val lists = PsiTreeUtil.findChildrenOfType(file, PhelList::class.java)
        return lists.firstOrNull { list ->
            // Get child forms properly, skipping whitespace and parentheses
            val forms = PsiTreeUtil.getChildrenOfType(list, PhelForm::class.java)
            if (forms.isNullOrEmpty()) return@firstOrNull false

            // First form should be "ns" symbol
            // It could be the symbol itself OR contain a symbol (if wrapped)
            val firstForm = forms[0]
            val firstSymbol = if (firstForm is PhelSymbol) firstForm
            else PsiTreeUtil.findChildOfType(firstForm, PhelSymbol::class.java)
            firstSymbol?.text == "ns"
        }
    }

    /**
     * Checks if a namespace is already required in the ns declaration.
     * @param nsDeclaration The (ns ...) form
     * @param namespace The namespace to check (e.g., "phel\\str")
     */
    fun isNamespaceRequired(nsDeclaration: PhelList, namespace: String): Boolean {
        val requireForms = findRequireForms(nsDeclaration)
        return requireForms.any { requireForm ->
            // Check if the require form contains the namespace
            val symbols = PsiTreeUtil.findChildrenOfType(requireForm, PhelSymbol::class.java)
            symbols.any { it.text == namespace }
        }
    }

    /**
     * Finds all (:require ...) forms within the namespace declaration.
     */
    fun findRequireForms(nsDeclaration: PhelList): List<PhelList> {
        return PsiTreeUtil.findChildrenOfType(nsDeclaration, PhelList::class.java).filter { list ->
            // Get child forms properly, skipping whitespace and parentheses
            val forms = PsiTreeUtil.getChildrenOfType(list, PhelForm::class.java)
            if (forms.isNullOrEmpty()) return@filter false

            // First form should be the :require keyword
            // It could be the keyword itself OR contain a keyword (if wrapped)
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
            val forms = PsiTreeUtil.getChildrenOfType(requireForm, PhelForm::class.java) ?: continue

            // Skip the :require keyword (first form)
            // Look for pattern: namespace :as alias
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
                                // Extract short namespace (e.g., "phel\str" -> "str")
                                val shortNamespace = namespace.substringAfterLast("\\")
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
     * Converts namespace to Phel format.
     * e.g., "str" -> "phel\\str"
     *       "http" -> "phel\\http"
     */
    fun toPhelNamespace(shortNamespace: String): String {
        return "phel\\$shortNamespace"
    }

    /**
     * Checks if a namespace is a core namespace (doesn't need explicit require).
     */
    fun isCoreNamespace(namespace: String?): Boolean {
        return namespace == null || namespace == "core"
    }

    /**
     * Checks if a namespace is imported either directly or via an alias.
     * @param file The Phel file to check
     * @param shortNamespace The short namespace name (e.g., "str", "http")
     * @return true if the namespace is imported (directly or aliased)
     */
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

    /**
     * Finds the alias for a namespace if one exists.
     * @param file The Phel file to check
     * @param shortNamespace The short namespace name (e.g., "str", "http")
     * @return The alias if found (e.g., "s" for "str"), or null if no alias exists
     */
    fun findAliasForNamespace(file: PhelFile, shortNamespace: String): String? {
        val aliasMap = extractAliasMap(file)
        // Reverse lookup: find the alias (key) for this namespace (value)
        return aliasMap.entries.find { it.value == shortNamespace }?.key
    }
}
