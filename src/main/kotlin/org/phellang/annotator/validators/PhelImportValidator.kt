package org.phellang.annotator.validators

import com.intellij.openapi.project.Project
import com.intellij.psi.util.PsiTreeUtil
import org.phellang.annotator.quickfixes.PhelFixImportQuickFix
import org.phellang.annotator.quickfixes.PhelRemoveImportQuickFix
import org.phellang.language.psi.PhelList
import org.phellang.language.psi.PhelImportAnalysis
import org.phellang.language.psi.PhelNamespaceUtils
import org.phellang.language.psi.PhelProjectNamespaceFinder
import org.phellang.language.psi.PhelSymbol
import org.phellang.language.psi.files.PhelFile

/**
 * Validates (:require ...) statements to ensure imported namespaces exist.
 */
object PhelImportValidator {

    fun validateImport(namespaceSymbol: PhelSymbol): List<PhelValidationProblem> {
        val fullNamespace = namespaceSymbol.text ?: return emptyList()

        // Must look like a namespace: dot-separated (Phel 0.35+ canonical) or legacy backslash.
        if (!looksLikeNamespace(fullNamespace)) {
            return emptyList()
        }

        val project = namespaceSymbol.project
        val containingFile = namespaceSymbol.containingFile as? PhelFile

        // A duplicate is reported on its own: it is trivially also "unused", and saying so twice
        // on the same symbol is noise.
        if (containingFile != null && PhelImportAnalysis.isDuplicateImport(containingFile, namespaceSymbol, fullNamespace)) {
            return listOf(
                PhelValidationProblem(
                    message = "Duplicate import: '$fullNamespace' is already imported",
                    quickFix = PhelRemoveImportQuickFix(namespaceSymbol, "Remove duplicate import"),
                )
            )
        }

        // An unused import is reported by PhelUnusedImportInspection, not here: it is switchable
        // from Settings, and reporting it in both places would flag every one of them twice.
        return listOfNotNull(missingNamespaceProblem(project, namespaceSymbol, fullNamespace))
    }

    /** Null when the namespace resolves — to the standard library or to a project file. */
    private fun missingNamespaceProblem(
        project: Project, namespaceSymbol: PhelSymbol, fullNamespace: String
    ): PhelValidationProblem? {
        if (PhelProjectNamespaceFinder.isStandardLibrary(fullNamespace)) return null
        if (PhelProjectNamespaceFinder.namespaceExists(project, fullNamespace)) return null

        val shortNamespace = PhelProjectNamespaceFinder.extractShortNamespace(fullNamespace)
        val suggestion = PhelProjectNamespaceFinder.findByShortName(project, shortNamespace)

        return if (suggestion != null && suggestion != fullNamespace) {
            PhelValidationProblem(
                message = "Namespace '$fullNamespace' does not exist. Did you mean '$suggestion'?",
                quickFix = PhelFixImportQuickFix(namespaceSymbol, suggestion),
            )
        } else {
            PhelValidationProblem("Namespace '$fullNamespace' does not exist")
        }
    }

    private fun looksLikeNamespace(text: String): Boolean = PhelNamespaceUtils.looksLikeNamespace(text)
}
