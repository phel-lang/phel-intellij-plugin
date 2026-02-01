package org.phellang.annotator.validators

import com.intellij.openapi.project.Project
import com.intellij.psi.util.PsiTreeUtil
import org.phellang.core.psi.PhelPsiUtils
import org.phellang.language.psi.PhelForm
import org.phellang.language.psi.PhelNamespaceUtils
import org.phellang.language.psi.PhelProjectNamespaceFinder
import org.phellang.language.psi.PhelSymbol
import org.phellang.language.psi.files.PhelFile

data class NamespaceValidationResult(val message: String, val fullNamespace: String?, val shortNamespace: String)

object PhelNamespaceValidator {

    fun validateNamespace(symbol: PhelSymbol): NamespaceValidationResult? {
        val text = symbol.text ?: return null

        // Only validate namespace-qualified symbols
        if (!text.contains("/")) {
            return null
        }

        val qualifier = PhelPsiUtils.getQualifier(symbol) ?: return null

        // Skip php/ interop - always valid
        if (qualifier == "php") {
            return null
        }

        // Skip core/ - core functions don't need import
        if (qualifier == "core") {
            return null
        }

        val containingFile = symbol.containingFile as? PhelFile ?: return null

        // Check if the qualifier is imported or aliased AND the namespace exists
        val importStatus = checkImportStatus(containingFile, qualifier)

        when (importStatus) {
            ImportStatus.VALID -> return null  // Imported and exists
            ImportStatus.IMPORTED_BUT_NOT_EXISTS -> {
                // The namespace is imported but the actual namespace file doesn't exist
                // Check if a project file exists with matching short namespace
                val suggestion = PhelProjectNamespaceFinder.findByShortName(symbol.project, qualifier)
                return if (suggestion != null) {
                    NamespaceValidationResult(
                        message = "Imported namespace does not exist. Did you mean '$suggestion'?",
                        fullNamespace = suggestion,
                        shortNamespace = qualifier
                    )
                } else {
                    NamespaceValidationResult(
                        message = "Imported namespace does not exist", fullNamespace = null, shortNamespace = qualifier
                    )
                }
            }

            ImportStatus.NOT_IMPORTED -> {
                // Not imported - check if we can suggest an import
            }
        }

        // Check if it's a standard library namespace
        val stdLibNamespace = PhelProjectNamespaceFinder.getStandardLibraryFullNamespace(qualifier)
        if (stdLibNamespace != null) {
            return NamespaceValidationResult(
                message = "Namespace '$qualifier' is not imported",
                fullNamespace = stdLibNamespace,
                shortNamespace = qualifier
            )
        }

        // Check if a project file exists with this namespace
        val projectNamespace = PhelProjectNamespaceFinder.findByShortName(symbol.project, qualifier)
        if (projectNamespace != null) {
            return NamespaceValidationResult(
                message = "Namespace '$qualifier' is not imported",
                fullNamespace = projectNamespace,
                shortNamespace = qualifier
            )
        }

        // Namespace doesn't exist at all
        return NamespaceValidationResult(
            message = "Namespace '$qualifier' does not exist", fullNamespace = null, shortNamespace = qualifier
        )
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
            val forms = PsiTreeUtil.getChildrenOfType(requireForm, PhelForm::class.java) ?: continue

            for (form in forms) {
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
            val forms = PsiTreeUtil.getChildrenOfType(requireForm, PhelForm::class.java) ?: continue

            for (form in forms) {
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
