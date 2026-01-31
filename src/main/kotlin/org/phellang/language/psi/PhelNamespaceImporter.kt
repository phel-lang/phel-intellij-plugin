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

        // Check if already imported
        if (PhelNamespaceUtils.isNamespaceRequired(nsDeclaration, phelNamespace)) {
            return true
        }

        // Add the require
        return addRequireToNamespace(file.project, nsDeclaration, phelNamespace)
    }

    fun ensureNamespaceImportedByFullName(file: PhelFile, fullNamespace: String): Boolean {
        val nsDeclaration = PhelNamespaceUtils.findNamespaceDeclaration(file) ?: return false

        // Check if already imported
        if (PhelNamespaceUtils.isNamespaceRequired(nsDeclaration, fullNamespace)) {
            return true
        }

        // Add the :require with the full namespace
        return addRequireToNamespace(file.project, nsDeclaration, fullNamespace)
    }

    private fun addRequireToNamespace(project: Project, nsDeclaration: PhelList, namespace: String): Boolean {
        try {
            addNewRequireForm(project, nsDeclaration, namespace)
            return true
        } catch (_: Exception) {
            return false
        }
    }

    private fun addNewRequireForm(project: Project, nsDeclaration: PhelList, namespace: String) {
        // Create (:require phel\namespace)
        val requireForm = PhelPsiFactory.createList(project, "(:require $namespace)")

        // Find insertion point (before closing paren)
        val closingParen = nsDeclaration.lastChild

        // Add whitespace (newline + indentation), then require form
        val whitespace = PhelPsiFactory.createWhitespace(project, "\n  ")
        nsDeclaration.addBefore(whitespace, closingParen)
        nsDeclaration.addBefore(requireForm, closingParen)
    }
}
