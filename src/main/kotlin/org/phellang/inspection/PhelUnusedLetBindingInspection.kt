package org.phellang.inspection

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import org.phellang.inspection.analysis.PhelUnusedBindingFinder
import org.phellang.inspection.quickfixes.PhelRemoveLetBindingQuickFix
import org.phellang.language.psi.PhelList
import org.phellang.language.psi.PhelVisitor

/** Reports `let`-like bindings whose name is never read, offering to delete the pair. */
class PhelUnusedLetBindingInspection : LocalInspectionTool() {

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : PhelVisitor() {
            override fun visitList(o: PhelList) {
                for (target in PhelUnusedBindingFinder.unusedBindings(o)) {
                    holder.registerProblem(
                        target,
                        "Binding '${target.text}' is never used.",
                        ProblemHighlightType.LIKE_UNUSED_SYMBOL,
                        PhelRemoveLetBindingQuickFix(),
                    )
                }
            }
        }
    }
}
