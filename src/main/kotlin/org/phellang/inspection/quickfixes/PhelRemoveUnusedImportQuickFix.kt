package org.phellang.inspection.quickfixes

import com.intellij.codeInspection.LocalQuickFix
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.openapi.project.Project
import org.phellang.language.psi.PhelSymbol
import org.phellang.language.psi.utils.PhelImportRemoval

class PhelRemoveUnusedImportQuickFix : LocalQuickFix {

    override fun getFamilyName(): String = "Remove unused import"

    /**
     * No write action of its own: the platform already runs `applyFix` inside one, and inside a
     * command, so a failed PSI edit is rolled back and reported rather than leaving the user with a
     * fix that silently did nothing.
     */
    override fun applyFix(project: Project, descriptor: ProblemDescriptor) {
        val symbol = descriptor.psiElement as? PhelSymbol ?: return
        PhelImportRemoval.removeEnclosingImport(symbol)
    }
}
