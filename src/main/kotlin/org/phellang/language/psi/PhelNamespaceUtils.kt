package org.phellang.language.psi

import com.intellij.psi.PsiElement
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
            val firstKeyword = if (firstForm is PhelKeyword) firstForm
                               else PsiTreeUtil.findChildOfType(firstForm, PhelKeyword::class.java)
            firstKeyword?.text == ":require"
        }
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
}
