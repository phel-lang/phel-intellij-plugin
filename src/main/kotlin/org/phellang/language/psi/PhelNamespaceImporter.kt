package org.phellang.language.psi

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import org.phellang.language.psi.files.PhelFile

/**
 * Handles adding namespace imports to Phel files.
 */
object PhelNamespaceImporter {

    /**
     * Ensures a namespace is imported in the file.
     * If not already imported, adds (:require phel\namespace) to the ns declaration.
     * 
     * @param file The Phel file
     * @param namespace The short namespace name (e.g., "str", "http")
     * @return true if import was added or already existed
     */
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

    /**
     * Adds a require form to the namespace declaration.
     */
    private fun addRequireToNamespace(project: Project, nsDeclaration: PhelList, namespace: String): Boolean {
        try {
            val requireForms = PhelNamespaceUtils.findRequireForms(nsDeclaration)
            
            if (requireForms.isEmpty()) {
                // No existing require forms, add a new one
                addFirstRequireForm(project, nsDeclaration, namespace)
            } else {
                // Add to existing require section
                addToExistingRequire(project, requireForms.first(), namespace)
            }
            return true
        } catch (e: Exception) {
            return false
        }
    }

    private fun addFirstRequireForm(project: Project, nsDeclaration: PhelList, namespace: String) {
        // Create (:require phel\namespace)
        val requireForm = PhelPsiFactory.createList(project, "(:require $namespace)")
        
        // Find insertion point (before closing paren)
        val closingParen = nsDeclaration.lastChild
        
        // Add whitespace (newline + indentation), then require form
        // Both are added before closing paren - order matters:
        // 1. Add whitespace first -> (ns abc\def\n  )
        // 2. Add require form -> (ns abc\def\n  (:require phel\str))
        val whitespace = PhelPsiFactory.createWhitespace(project, "\n  ")
        nsDeclaration.addBefore(whitespace, closingParen)
        nsDeclaration.addBefore(requireForm, closingParen)
    }

    private fun addToExistingRequire(project: Project, requireForm: PhelList, namespace: String) {
        // Create a new require entry: phel\namespace
        val namespaceSymbol = PhelPsiFactory.createSymbol(project, namespace)
        
        // Find a good insertion point (before closing paren)
        val closingParen = requireForm.lastChild
        
        // Add newline and indentation
        val whitespace = PhelPsiFactory.createWhitespace(project, "\n             ")
        requireForm.addBefore(whitespace, closingParen)
        
        // Add the namespace
        requireForm.addBefore(namespaceSymbol, closingParen)
    }
}
