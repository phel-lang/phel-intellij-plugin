package org.phellang.language.psi

import com.intellij.openapi.project.Project
import org.phellang.language.psi.files.PhelFile

object PhelNamespaceImporter {
    fun ensureNamespaceImported(file: PhelFile, namespace: String): Boolean {
        if (PhelNamespaceUtils.isCoreNamespace(namespace)) {
            return true // Core namespace doesn't need import
        }

        val nsDeclaration = PhelNamespaceUtils.findNamespaceDeclaration(file) ?: return false
        val phelNamespace = PhelNamespaceUtils.toPhelNamespace(namespace)

        if (PhelNamespaceUtils.isNamespaceRequired(nsDeclaration, phelNamespace)) {
            return true
        }

        return addRequireToNamespace(file.project, nsDeclaration, phelNamespace)
    }

    fun ensureNamespaceImportedByFullName(file: PhelFile, fullNamespace: String): Boolean {
        val nsDeclaration = PhelNamespaceUtils.findNamespaceDeclaration(file) ?: return false

        if (PhelNamespaceUtils.isNamespaceRequired(nsDeclaration, fullNamespace)) {
            return true
        }

        return addRequireToNamespace(file.project, nsDeclaration, fullNamespace)
    }

    private fun addRequireToNamespace(project: Project, nsDeclaration: PhelList, namespace: String): Boolean {
        // Runs inside the insert handler's write action. If PSI manipulation fails, let it out:
        // the platform rolls the write action back and reports it. Swallowing it here left the
        // user with the symbol inserted and no `(:require ...)`, with nothing to indicate why.
        addNewRequireForm(project, nsDeclaration, namespace)
        return true
    }

    private fun addNewRequireForm(project: Project, nsDeclaration: PhelList, namespace: String) {
        val requireForm = PhelPsiFactory.createList(project, "(:require $namespace)")

        val closingParen = nsDeclaration.lastChild

        val whitespace = PhelPsiFactory.createWhitespace(project, "\n  ")
        nsDeclaration.addBefore(whitespace, closingParen)
        nsDeclaration.addBefore(requireForm, closingParen)
    }
}
