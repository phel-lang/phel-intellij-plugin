package org.phellang.language.psi.references

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiManager
import org.phellang.language.psi.PhelAccess
import org.phellang.language.psi.PhelList
import org.phellang.language.psi.PhelNamespaceFiles
import org.phellang.language.psi.PhelNamespaceUtils
import org.phellang.language.psi.PhelSymbol
import org.phellang.language.psi.PhelVec
import org.phellang.language.psi.PhelVendorUtils
import org.phellang.language.psi.files.PhelFile
import org.phellang.language.psi.utils.PhelPsiUtils

/**
 * Resolves the namespace at the head of a `(:require ...)` spec to the `(ns ...)` form of the module
 * it imports, so go-to-definition on an import jumps to that module — as it does in other languages.
 *
 * Returns an empty list when the symbol isn't a require head, letting the caller fall through to
 * ordinary symbol resolution.
 */
internal object PhelRequireNamespaceResolver {
    fun resolve(symbol: PhelSymbol): List<PsiElement> {
        if (!isRequireNamespaceHead(symbol)) return emptyList()

        val namespaceText = symbol.text?.takeIf { it.isNotEmpty() } ?: return emptyList()
        val project = symbol.project
        val target = PhelNamespaceUtils.normalizeNamespace(namespaceText)

        val inProject = findInProjectFiles(symbol, target)
        if (inProject.isNotEmpty()) return inProject

        return findInVendor(symbol, namespaceText, target)
    }

    /** The `(ns ...)` forms that declare [target]; a file that only joins it with `in-ns` has none to jump to. */
    private fun findInProjectFiles(symbol: PhelSymbol, target: String): List<PsiElement> =
        PhelNamespaceFiles.filesOf(symbol.project, target)
            .mapNotNull { PhelNamespaceUtils.findNamespaceDeclaration(it) }
            .filter { declaresNamespace(it, target) }
            .map(::navigationTarget)

    private fun findInVendor(symbol: PhelSymbol, namespaceText: String, target: String): List<PsiElement> {
        val project = symbol.project
        val psiManager = PsiManager.getInstance(project)

        val declarations = PhelVendorUtils.findStandardLibraryFiles(project, namespaceText)
            .mapNotNull { psiManager.findFile(it) as? PhelFile }
            .mapNotNull { PhelNamespaceUtils.findNamespaceDeclaration(it) }

        val exact = declarations.filter { declaresNamespace(it, target) }.map(::navigationTarget)
        if (exact.isNotEmpty()) return exact

        // No exact match: fall back to the first vendor file's ns form, because a namespace such as
        // phel\core is split across many bucket files and none of them declares it on its own.
        return listOfNotNull(declarations.firstOrNull()?.let(::navigationTarget))
    }

    private fun declaresNamespace(nsDeclaration: PhelList, target: String): Boolean =
        PhelNamespaceUtils.extractNamespaceFromDeclaration(nsDeclaration)
            ?.let(PhelNamespaceUtils::normalizeNamespace) == target

    /** The `(ns <name> ...)` name symbol to navigate to, falling back to the whole form. */
    private fun navigationTarget(nsDeclaration: PhelList): PsiElement {
        val nameForm = nsDeclaration.forms.getOrNull(1)
        return PhelPsiUtils.asSymbol(nameForm) ?: nsDeclaration
    }

    private fun isRequireNamespaceHead(symbol: PhelSymbol): Boolean {
        // A dot-separated namespace like `phel.string` parses as a PhelAccess form (the `.` is
        // interop-access syntax), so the node under the clause may be that wrapper, not the symbol.
        val node: PsiElement = if (symbol.parent is PhelAccess) symbol.parent else symbol
        val parent = node.parent

        // (:require foo\bar) / (:require phel.string) — namespace directly under the clause
        if (parent is PhelList && isRequireClause(parent)) return true

        // (:require [foo\bar :as fb]) — namespace is the vector spec's head
        if (parent is PhelVec) {
            val grandParent = parent.parent
            return grandParent is PhelList && isRequireClause(grandParent) && parent.forms.firstOrNull() === node
        }

        return false
    }

    private fun isRequireClause(list: PhelList): Boolean {
        return PhelPsiUtils.asKeyword(list.forms.firstOrNull())?.text == ":require"
    }
}
