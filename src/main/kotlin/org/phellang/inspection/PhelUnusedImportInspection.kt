package org.phellang.inspection

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.util.PsiTreeUtil
import org.phellang.inspection.analysis.PhelUnusedImportFinder
import org.phellang.inspection.quickfixes.PhelRemoveUnusedImportQuickFix
import org.phellang.language.psi.PhelNamespaceUtils
import org.phellang.language.psi.PhelSymbol
import org.phellang.language.psi.PhelVisitor
import org.phellang.language.psi.files.PhelFile

/**
 * Reports a `(:require …)` whose namespace is never used in the file.
 *
 * Previously reported by the annotator, which meant it could not be switched off or re-levelled
 * from Settings. The annotator no longer reports it; a duplicate or unresolvable import is still
 * its business.
 */
class PhelUnusedImportInspection : LocalInspectionTool() {

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : PhelVisitor() {
            override fun visitSymbol(o: PhelSymbol) {
                if (!isInsideRequireForm(o)) return
                if (!PhelUnusedImportFinder.isUnusedImport(o)) return

                holder.registerProblem(
                    o,
                    "Unused import",
                    ProblemHighlightType.LIKE_UNUSED_SYMBOL,
                    PhelRemoveUnusedImportQuickFix(),
                )
            }
        }
    }

    /**
     * Every symbol in the file reaches this visitor, and a namespace-shaped symbol can appear in
     * ordinary code. Only one inside a `(:require …)` form is an import.
     *
     * Narrower than "inside the `(ns …)` declaration" on purpose: the declaration's *own* name is
     * namespace-shaped, is never used as a qualifier in its own file, and was therefore reported as
     * an unused import by that broader test.
     */
    private fun isInsideRequireForm(symbol: PhelSymbol): Boolean {
        val file = symbol.containingFile as? PhelFile ?: return false
        val declaration = PhelNamespaceUtils.findNamespaceDeclaration(file) ?: return false

        return PhelNamespaceUtils.findRequireForms(declaration)
            .any { PsiTreeUtil.isAncestor(it, symbol, false) }
    }
}
