package org.phellang.annotator.validators

import com.intellij.openapi.project.Project
import org.phellang.annotator.quickfixes.PhelImportNamespaceQuickFix
import org.phellang.language.psi.utils.PhelPsiUtils
import org.phellang.language.psi.PhelInteropShorthands
import org.phellang.language.psi.PhelNamespaceUtils
import org.phellang.language.psi.PhelProjectNamespaceFinder
import org.phellang.language.psi.PhelRequireClauseAnalyzer
import org.phellang.language.psi.PhelSymbol
import org.phellang.language.psi.files.PhelFile

object PhelNamespaceValidator {
    fun validateNamespace(symbol: PhelSymbol): List<PhelValidationProblem> {
        val text = symbol.text ?: return emptyList()

        if (!text.contains("/")) {
            return emptyList()
        }

        val qualifier = PhelPsiUtils.getQualifier(symbol) ?: return emptyList()

        // Skip php/ interop - always valid
        if (qualifier == "php") {
            return emptyList()
        }

        // Skip core/ - core functions don't need import
        if (qualifier == "core") {
            return emptyList()
        }

        val containingFile = symbol.containingFile as? PhelFile ?: return emptyList()

        // Skip PHP-class interop shorthands like `DateTime/createFromFormat` or
        // `\Foo\Bar/CONST`. These look namespaced but the qualifier is a PHP class,
        // not a Phel namespace, so the regular import lookup would always fail.
        val usedClasses = PhelNamespaceUtils.extractUsedClasses(containingFile)
        if (PhelInteropShorthands.isInteropShorthand(text, usedClasses)) {
            return emptyList()
        }

        val importStatus = checkImportStatus(containingFile, qualifier)

        when (importStatus) {
            ImportStatus.VALID -> return emptyList()  // Imported and exists
            ImportStatus.IMPORTED_BUT_NOT_EXISTS -> {
                // The namespace is imported but the actual namespace file doesn't exist.
                // Only offer a fix when a project file with a matching short namespace exists.
                val suggestion = PhelProjectNamespaceFinder.findByShortName(symbol.project, qualifier)
                return listOf(
                    if (suggestion != null) {
                        PhelValidationProblem(
                            message = "Imported namespace does not exist. Did you mean '$suggestion'?",
                            quickFix = PhelImportNamespaceQuickFix(suggestion),
                        )
                    } else {
                        PhelValidationProblem("Imported namespace does not exist")
                    }
                )
            }

            ImportStatus.NOT_IMPORTED -> {
            }
        }

        // Not imported, but the namespace does resolve: offer to import it.
        val importable = PhelProjectNamespaceFinder.getStandardLibraryFullNamespace(qualifier)
            ?: PhelProjectNamespaceFinder.findByShortName(symbol.project, qualifier)

        if (importable != null) {
            return listOf(
                PhelValidationProblem(
                    message = "Namespace '$qualifier' is not imported",
                    quickFix = PhelImportNamespaceQuickFix(importable),
                )
            )
        }

        // Namespace doesn't exist at all — nothing to import, so no fix.
        return listOf(PhelValidationProblem("Namespace '$qualifier' does not exist"))
    }

    private enum class ImportStatus {
        VALID,                   // Imported and namespace exists
        IMPORTED_BUT_NOT_EXISTS, // Imported but namespace file doesn't exist
        NOT_IMPORTED             // Not imported at all
    }

    private fun checkImportStatus(file: PhelFile, qualifier: String): ImportStatus {
        val imports = PhelRequireClauseAnalyzer.imports(file)

        // An `:as` alias takes priority; otherwise match the import's short namespace.
        val import = imports.firstOrNull { it.alias == qualifier }
            ?: imports.firstOrNull { it.shortNamespace == qualifier }
            ?: return ImportStatus.NOT_IMPORTED

        return if (namespaceExistsWithStdLib(file.project, import.fullNamespace)) {
            ImportStatus.VALID
        } else {
            ImportStatus.IMPORTED_BUT_NOT_EXISTS
        }
    }

    private fun namespaceExistsWithStdLib(project: Project, fullNamespace: String): Boolean {
        if (PhelProjectNamespaceFinder.isStandardLibrary(fullNamespace)) {
            return true
        }

        return PhelProjectNamespaceFinder.namespaceExists(project, fullNamespace)
    }
}
