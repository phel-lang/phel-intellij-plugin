package org.phellang.annotator.highlighters

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.psi.util.PsiTreeUtil
import org.phellang.annotator.infrastructure.PhelAnnotationUtils
import org.phellang.annotator.quickfixes.PhelFixImportQuickFix
import org.phellang.annotator.quickfixes.PhelRemoveImportQuickFix
import org.phellang.annotator.validators.PhelImportValidator
import org.phellang.annotator.validators.PhelReferSymbolValidator
import org.phellang.language.psi.PhelKeyword
import org.phellang.language.psi.PhelList
import org.phellang.language.psi.PhelSymbol

object PhelRequireHighlighter {

    /**
     * Checks if a symbol is a namespace in a (:require ...) form and validates it.
     * Returns true if the symbol was handled (valid or invalid require), false otherwise.
     */
    fun annotateRequireNamespace(symbol: PhelSymbol, holder: AnnotationHolder): Boolean {
        // First check if this is a symbol inside a :refer vector
        if (annotateReferSymbol(symbol, holder)) {
            return true
        }

        // Check if this symbol is inside a (:require ...) form
        if (!isNamespaceInRequireForm(symbol)) {
            return false
        }

        // Duplicate-vs-unused precedence is the validator's call, not ours; we just render.
        PhelImportValidator.validateImport(symbol).forEach { problem ->
            PhelAnnotationUtils.report(holder, symbol, problem)
        }

        // Return true to indicate we handled this symbol (whether valid or invalid)
        return true
    }

    private fun annotateReferSymbol(symbol: PhelSymbol, holder: AnnotationHolder): Boolean {
        if (!PhelReferSymbolValidator.isInsideReferVector(symbol)) {
            return false
        }

        PhelReferSymbolValidator.validateReferSymbol(symbol).forEach { problem ->
            PhelAnnotationUtils.report(holder, symbol, problem)
        }

        return true
    }

    private fun isNamespaceInRequireForm(symbol: PhelSymbol): Boolean {
        val text = symbol.text ?: return false

        // Must look like a namespace: dot-separated (Phel 0.35+) or legacy backslash.
        if (!text.contains('.') && !text.contains('\\')) {
            return false
        }

        // Check if parent is a list starting with :require keyword
        val parentList = PsiTreeUtil.getParentOfType(symbol, PhelList::class.java) ?: return false

        val children = parentList.children
        if (children.isEmpty()) return false

        // First child should be :require keyword
        val firstChild = children.firstOrNull()

        return firstChild is PhelKeyword && firstChild.text == ":require"
    }
}
