package org.phellang.inspection

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import org.phellang.inspection.analysis.PhelUnusedParameterFinder
import org.phellang.language.psi.PhelList
import org.phellang.language.psi.PhelVisitor

/** Reports a function parameter that the body never reads. */
class PhelUnusedParameterInspection : LocalInspectionTool() {

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : PhelVisitor() {
            override fun visitList(o: PhelList) {
                for (parameter in PhelUnusedParameterFinder.unusedParameters(o)) {
                    // No quick fix: removing a parameter changes the function's arity, so every
                    // call site would have to change with it.
                    holder.registerProblem(
                        parameter,
                        "Parameter '${parameter.text}' is never used.",
                        ProblemHighlightType.LIKE_UNUSED_SYMBOL,
                    )
                }
            }
        }
    }
}
