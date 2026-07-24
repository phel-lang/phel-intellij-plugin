package org.phellang.inspection

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import org.phellang.inspection.analysis.PhelShadowedBindingFinder
import org.phellang.language.psi.PhelList
import org.phellang.language.psi.PhelVisitor

/** Reports a `let`-like binding whose name is already bound by an enclosing binding or parameter. */
class PhelShadowedLetBindingInspection : LocalInspectionTool() {

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : PhelVisitor() {
            override fun visitList(o: PhelList) {
                for (target in PhelShadowedBindingFinder.shadowedBindings(o)) {
                    holder.registerProblem(
                        target,
                        "Binding '${target.text}' shadows an outer binding.",
                        ProblemHighlightType.WEAK_WARNING,
                    )
                }
            }
        }
    }
}
