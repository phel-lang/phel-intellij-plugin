package org.phellang.language.psi

import com.intellij.psi.util.PsiTreeUtil
import org.phellang.language.psi.files.PhelFile

object PhelNamespaceUtils {

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

    fun isNamespaceRequired(nsDeclaration: PhelList, namespace: String): Boolean {
        val requireForms = findRequireForms(nsDeclaration)
        return requireForms.any { requireForm ->
            // Check if the require form contains the namespace
            val symbols = PsiTreeUtil.findChildrenOfType(requireForm, PhelSymbol::class.java)
            symbols.any { it.text == namespace }
        }
    }

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

    fun extractNamespaceFromDeclaration(nsDeclaration: PhelList): String? {
        val forms = PsiTreeUtil.getChildrenOfType(nsDeclaration, PhelForm::class.java) ?: return null
        if (forms.size < 2) return null

        // Second form should be the namespace name
        val namespaceSymbol = if (forms[1] is PhelSymbol) {
            forms[1] as PhelSymbol
        } else {
            PsiTreeUtil.findChildOfType(forms[1], PhelSymbol::class.java)
        }

        return namespaceSymbol?.text
    }

    fun extractShortNamespaceFromDeclaration(nsDeclaration: PhelList): String? {
        return extractNamespaceFromDeclaration(nsDeclaration)?.substringAfterLast("\\")
    }

    fun extractNamespaceFromFile(file: PhelFile): String? {
        val nsDeclaration = findNamespaceDeclaration(file) ?: return null
        return extractNamespaceFromDeclaration(nsDeclaration)
    }

    /**
     * Converts namespace to Phel format.
     * e.g., "str" -> "phel\\str"
     *       "http" -> "phel\\http"
     */
    fun toPhelNamespace(shortNamespace: String): String {
        return "phel\\$shortNamespace"
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
}
