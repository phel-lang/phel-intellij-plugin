package org.phellang.annotator.validators

import com.intellij.psi.util.PsiTreeUtil
import org.phellang.language.psi.PhelKeyword
import org.phellang.language.psi.PhelList
import org.phellang.language.psi.PhelNamespaceUtils
import org.phellang.language.psi.PhelProjectNamespaceFinder
import org.phellang.language.psi.PhelSymbol
import org.phellang.language.psi.files.PhelFile

data class ImportValidationResult(
    val message: String,
    val suggestedNamespace: String?,
    val isDuplicate: Boolean = false,
    val isUnused: Boolean = false
)

/**
 * Validates (:require ...) statements to ensure imported namespaces exist.
 */
object PhelImportValidator {

    fun validateImport(namespaceSymbol: PhelSymbol): ImportValidationResult? {
        val fullNamespace = namespaceSymbol.text ?: return null

        // Skip if it doesn't look like a namespace (must contain backslash)
        if (!fullNamespace.contains("\\")) {
            return null
        }

        val project = namespaceSymbol.project
        val containingFile = namespaceSymbol.containingFile as? PhelFile

        // Check for duplicate imports first
        if (containingFile != null && isDuplicateImport(containingFile, namespaceSymbol, fullNamespace)) {
            return ImportValidationResult(
                message = "Duplicate import: '$fullNamespace' is already imported",
                suggestedNamespace = null,
                isDuplicate = true
            )
        }

        // Check if it's a standard library namespace
        if (PhelProjectNamespaceFinder.isStandardLibrary(fullNamespace)) {
            return null
        }

        // Check if namespace exists as a project file
        if (PhelProjectNamespaceFinder.namespaceExists(project, fullNamespace)) {
            return null
        }

        // Namespace doesn't exist - try to find a suggestion
        val shortNamespace = PhelProjectNamespaceFinder.extractShortNamespace(fullNamespace)
        val suggestion = PhelProjectNamespaceFinder.findByShortName(project, shortNamespace)

        return if (suggestion != null && suggestion != fullNamespace) {
            ImportValidationResult(
                message = "Namespace '$fullNamespace' does not exist. Did you mean '$suggestion'?",
                suggestedNamespace = suggestion
            )
        } else {
            ImportValidationResult(
                message = "Namespace '$fullNamespace' does not exist", suggestedNamespace = null
            )
        }
    }

    private fun isDuplicateImport(file: PhelFile, currentSymbol: PhelSymbol, namespace: String): Boolean {
        val nsDeclaration = PhelNamespaceUtils.findNamespaceDeclaration(file) ?: return false
        val requireForms = PhelNamespaceUtils.findRequireForms(nsDeclaration)

        var foundFirst = false

        for (requireForm in requireForms) {
            val symbols = PsiTreeUtil.findChildrenOfType(requireForm, PhelSymbol::class.java)

            for (symbol in symbols) {
                val symbolText = symbol.text ?: continue
                if (!symbolText.contains("\\")) continue

                if (symbolText == namespace) {
                    if (symbol === currentSymbol) {
                        if (foundFirst) {
                            return true
                        }
                    } else {
                        if (!foundFirst) {
                            foundFirst = true
                        } else if (symbol.textOffset < currentSymbol.textOffset) {
                            return true
                        }
                    }
                }
            }
        }

        return false
    }

    fun isUnusedImport(namespaceSymbol: PhelSymbol): Boolean {
        val fullNamespace = namespaceSymbol.text ?: return false
        if (!fullNamespace.contains("\\")) return false

        val containingFile = namespaceSymbol.containingFile as? PhelFile ?: return false

        // Check if this import uses :refer - if so, it's not "unused" in the traditional sense
        if (hasReferClause(namespaceSymbol)) {
            return false
        }

        val shortNamespace = PhelProjectNamespaceFinder.extractShortNamespace(fullNamespace)

        // Get the alias if one exists for this namespace
        val aliasMap = PhelNamespaceUtils.extractAliasMap(containingFile)
        val alias = aliasMap.entries.find { it.value == shortNamespace }?.key

        // The qualifier to look for is either the alias or the short namespace
        val qualifierToFind = alias ?: shortNamespace

        // Scan all symbols in the file for usage
        val allSymbols = PsiTreeUtil.findChildrenOfType(containingFile, PhelSymbol::class.java)
        val nsDeclaration = PhelNamespaceUtils.findNamespaceDeclaration(containingFile)

        for (symbol in allSymbols) {
            // Skip symbols inside the namespace declaration
            if (nsDeclaration != null && PsiTreeUtil.isAncestor(nsDeclaration, symbol, false)) {
                continue
            }

            val text = symbol.text ?: continue

            // Check if this symbol uses the qualifier (e.g., "str/join" uses "str")
            if (text.contains("/")) {
                val qualifier = text.substringBefore("/")
                if (qualifier == qualifierToFind) {
                    return false // Found a usage
                }
            }
        }

        return true // No usage found
    }

    private fun hasReferClause(namespaceSymbol: PhelSymbol): Boolean {
        val requireForm = PsiTreeUtil.getParentOfType(namespaceSymbol, PhelList::class.java) ?: return false

        val keywords = PsiTreeUtil.findChildrenOfType(requireForm, PhelKeyword::class.java)
        return keywords.any { it.text == ":refer" }
    }
}
