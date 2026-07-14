package org.phellang.annotator.validators

import com.intellij.openapi.project.Project
import com.intellij.psi.util.PsiTreeUtil
import org.phellang.annotator.quickfixes.PhelImportNamespaceQuickFix
import org.phellang.language.psi.utils.PhelPsiUtils
import org.phellang.language.psi.PhelForm
import org.phellang.language.psi.PhelInteropShorthands
import org.phellang.language.psi.PhelNamespaceUtils
import org.phellang.language.psi.PhelProjectNamespaceFinder
import org.phellang.language.psi.PhelSymbol
import org.phellang.language.psi.files.PhelFile

object PhelNamespaceValidator {

    fun validateNamespace(symbol: PhelSymbol): List<PhelValidationProblem> {
        val text = symbol.text ?: return emptyList()

        // Only validate namespace-qualified symbols
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

        // Check if the qualifier is imported or aliased AND the namespace exists
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
                // Not imported - check if we can suggest an import
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
        val nsDeclaration = PhelNamespaceUtils.findNamespaceDeclaration(file) ?: return ImportStatus.NOT_IMPORTED

        // Check alias map
        val aliasMap = PhelNamespaceUtils.extractAliasMap(file)
        if (aliasMap.containsKey(qualifier)) {
            val aliasedNamespace = aliasMap[qualifier]
            if (aliasedNamespace != null) {
                val fullNamespace = getFullNamespaceForAlias(file, aliasedNamespace)
                if (fullNamespace != null) {
                    return if (namespaceExistsWithStdLib(file.project, fullNamespace)) {
                        ImportStatus.VALID
                    } else {
                        ImportStatus.IMPORTED_BUT_NOT_EXISTS
                    }
                }
            }
        }

        // Check direct imports
        val requireForms = PhelNamespaceUtils.findRequireForms(nsDeclaration)
        for (requireForm in requireForms) {
            for (form in requireForm.forms) {
                val namespaceSymbol = if (form is PhelSymbol) form
                else PsiTreeUtil.findChildOfType(form, PhelSymbol::class.java)

                if (namespaceSymbol == null) continue
                val fullNamespace = namespaceSymbol.text
                val shortNamespace = PhelProjectNamespaceFinder.extractShortNamespace(fullNamespace)
                if (shortNamespace != qualifier) continue
                return if (namespaceExistsWithStdLib(file.project, fullNamespace)) {
                    ImportStatus.VALID
                } else {
                    ImportStatus.IMPORTED_BUT_NOT_EXISTS
                }
            }
        }

        return ImportStatus.NOT_IMPORTED
    }

    private fun getFullNamespaceForAlias(file: PhelFile, aliasTarget: String): String? {
        val nsDeclaration = PhelNamespaceUtils.findNamespaceDeclaration(file) ?: return null
        val requireForms = PhelNamespaceUtils.findRequireForms(nsDeclaration)

        for (requireForm in requireForms) {
            for (form in requireForm.forms) {
                val namespaceSymbol = if (form is PhelSymbol) form
                else PsiTreeUtil.findChildOfType(form, PhelSymbol::class.java)

                if (namespaceSymbol == null) continue
                val fullNamespace = namespaceSymbol.text
                val shortNamespace = PhelProjectNamespaceFinder.extractShortNamespace(fullNamespace)
                if (shortNamespace == aliasTarget) {
                    return fullNamespace
                }
            }
        }
        return null
    }

    private fun namespaceExistsWithStdLib(project: Project, fullNamespace: String): Boolean {
        // Check if it's a standard library namespace
        if (PhelProjectNamespaceFinder.isStandardLibrary(fullNamespace)) {
            return true
        }

        // Check project files using shared utility
        return PhelProjectNamespaceFinder.namespaceExists(project, fullNamespace)
    }
}
