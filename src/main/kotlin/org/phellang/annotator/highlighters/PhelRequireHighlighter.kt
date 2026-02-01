package org.phellang.annotator.highlighters

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.psi.util.PsiTreeUtil
import org.phellang.annotator.infrastructure.PhelAnnotationUtils
import org.phellang.annotator.quickfixes.PhelFixImportQuickFix
import org.phellang.annotator.quickfixes.PhelRemoveDuplicateImportQuickFix
import org.phellang.annotator.validators.PhelImportValidator
import org.phellang.language.psi.PhelKeyword
import org.phellang.language.psi.PhelList
import org.phellang.language.psi.PhelSymbol

object PhelRequireHighlighter {

    /**
     * Checks if a symbol is a namespace in a (:require ...) form and validates it.
     * Returns true if the symbol was handled (valid or invalid require), false otherwise.
     */
    fun annotateRequireNamespace(symbol: PhelSymbol, holder: AnnotationHolder): Boolean {
        // Check if this symbol is inside a (:require ...) form
        if (!isNamespaceInRequireForm(symbol)) {
            return false
        }

        // Validate the import
        val validationResult = PhelImportValidator.validateImport(symbol)

        if (validationResult != null) {
            when {
                // Duplicate import - offer to remove it
                validationResult.isDuplicate -> {
                    val quickFix = PhelRemoveDuplicateImportQuickFix(symbol)
                    PhelAnnotationUtils.createWarningAnnotationWithFix(
                        holder, symbol, validationResult.message, quickFix
                    )
                }
                // Namespace doesn't exist but we have a suggestion
                validationResult.suggestedNamespace != null -> {
                    val quickFix = PhelFixImportQuickFix(
                        symbol, validationResult.suggestedNamespace
                    )
                    PhelAnnotationUtils.createWarningAnnotationWithFix(
                        holder, symbol, validationResult.message, quickFix
                    )
                }
                // Namespace doesn't exist, no suggestion
                else -> {
                    PhelAnnotationUtils.createWarningAnnotation(
                        holder, symbol, validationResult.message
                    )
                }
            }
        }

        // Return true to indicate we handled this symbol (whether valid or invalid)
        return true
    }

    private fun isNamespaceInRequireForm(symbol: PhelSymbol): Boolean {
        val text = symbol.text ?: return false

        // Must look like a namespace (contains backslash)
        if (!text.contains("\\")) {
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
