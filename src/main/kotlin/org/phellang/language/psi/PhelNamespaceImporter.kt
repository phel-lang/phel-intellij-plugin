package org.phellang.language.psi

import com.intellij.openapi.project.Project
import org.phellang.language.psi.files.PhelFile

object PhelNamespaceImporter {
    fun ensureNamespaceImported(file: PhelFile, namespace: String): Boolean {
        if (PhelNamespaceUtils.isNeverRequired(namespace)) {
            return true // Already available: `core` is auto-referred, `php` is the interop prefix
        }

        val nsDeclaration = targetDeclaration(file) ?: return false
        val phelNamespace = PhelNamespaceUtils.toPhelNamespace(namespace)

        if (PhelNamespaceUtils.isNamespaceRequired(nsDeclaration, phelNamespace)) {
            return true
        }

        return addRequireToNamespace(file.project, nsDeclaration, phelNamespace)
    }

    /** Requires [fullNamespace] unless it already is, with `:as` [alias] when one is given. */
    fun ensureNamespaceImportedByFullName(file: PhelFile, fullNamespace: String, alias: String? = null): Boolean {
        val nsDeclaration = targetDeclaration(file) ?: return false

        if (PhelNamespaceUtils.isNamespaceRequired(nsDeclaration, fullNamespace)) {
            return true
        }

        val spec = if (alias != null) "$fullNamespace :as $alias" else fullNamespace
        return addRequireToNamespace(file.project, nsDeclaration, spec)
    }

    /**
     * The `(ns ...)` form a require for [file] goes into: its own, else, for a file that joins a
     * namespace with `(in-ns ...)`, that namespace's `ns` form in another file, which is where the
     * compiler reads the namespace's requires from.
     *
     * Null when there is none, or when that other file cannot be edited: it is read-only, a vendored
     * namespace say, or [file] is the copy a quick-fix preview runs on, which must not touch a real
     * file.
     */
    private fun targetDeclaration(file: PhelFile): PhelList? {
        val declaration = PhelNamespaceUtils.environmentDeclaration(file) ?: return null
        val target = declaration.containingFile ?: return null
        if (target == file) return declaration

        return declaration.takeIf { file.isPhysical && target.isWritable }
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
