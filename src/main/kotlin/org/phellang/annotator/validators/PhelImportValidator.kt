package org.phellang.annotator.validators

import com.intellij.psi.util.PsiTreeUtil
import org.phellang.language.psi.PhelNamespaceUtils
import org.phellang.language.psi.PhelProjectNamespaceFinder
import org.phellang.language.psi.PhelSymbol
import org.phellang.language.psi.files.PhelFile

data class ImportValidationResult(
    val message: String, val suggestedNamespace: String?, val isDuplicate: Boolean = false
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
}
