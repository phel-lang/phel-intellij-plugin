package org.phellang.language.psi

import com.intellij.openapi.project.Project
import org.phellang.language.psi.files.PhelFile

object PhelNamespaceImporter {
    fun ensureNamespaceImported(file: PhelFile, namespace: String): Boolean {
        if (PhelNamespaceUtils.isNeverRequired(namespace)) {
            return true // Already available: `core` is auto-referred, `php` is the interop prefix
        }

        val nsDeclaration = PhelNamespaceUtils.findNamespaceDeclaration(file) ?: return false
        val phelNamespace = PhelNamespaceUtils.toPhelNamespace(namespace)

        if (PhelNamespaceUtils.isNamespaceRequired(nsDeclaration, phelNamespace)) {
            return true
        }

        return addRequireToNamespace(file.project, nsDeclaration, phelNamespace)
    }

    /** Requires [fullNamespace] unless it already is, with `:as` [alias] when one is given. */
    fun ensureNamespaceImportedByFullName(file: PhelFile, fullNamespace: String, alias: String? = null): Boolean {
        val nsDeclaration = PhelNamespaceUtils.findNamespaceDeclaration(file) ?: return false

        if (PhelNamespaceUtils.isNamespaceRequired(nsDeclaration, fullNamespace)) {
            return true
        }

        val spec = if (alias != null) "$fullNamespace :as $alias" else fullNamespace
        return addRequireToNamespace(file.project, nsDeclaration, spec)
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
