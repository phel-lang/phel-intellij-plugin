package org.phellang.language.psi

import com.intellij.psi.util.PsiTreeUtil
import org.phellang.language.psi.files.PhelFile

/**
 * Questions about the imports in an `(ns …)` declaration that more than one feature package asks.
 *
 * Lives in `language` because the annotator reports a duplicate import while the unused-import
 * inspection has to stay quiet on that same symbol, and a feature package may not reach into
 * another's internals. Keeping one copy is what makes the precedence rule hold: a duplicate is
 * trivially also unused, and saying both on the same symbol is noise.
 */
object PhelImportAnalysis {

    /**
     * True when [currentSymbol] is a *later* occurrence of a namespace already imported above it.
     *
     * The first occurrence is the original and is never a duplicate, so the warning lands on the
     * copy the user should delete.
     */
    fun isDuplicateImport(file: PhelFile, currentSymbol: PhelSymbol, namespace: String): Boolean {
        val nsDeclaration = PhelNamespaceUtils.findNamespaceDeclaration(file) ?: return false
        val requireForms = PhelNamespaceUtils.findRequireForms(nsDeclaration)
        val normalizedTarget = PhelNamespaceUtils.normalizeNamespace(namespace)

        var foundFirst = false

        for (requireForm in requireForms) {
            for (symbol in PsiTreeUtil.findChildrenOfType(requireForm, PhelSymbol::class.java)) {
                val symbolText = symbol.text ?: continue
                if (!PhelNamespaceUtils.looksLikeNamespace(symbolText)) continue
                if (PhelNamespaceUtils.normalizeNamespace(symbolText) != normalizedTarget) continue

                if (symbol === currentSymbol) {
                    if (foundFirst) return true
                } else if (!foundFirst) {
                    foundFirst = true
                } else if (symbol.textOffset < currentSymbol.textOffset) {
                    return true
                }
            }
        }

        return false
    }
}
