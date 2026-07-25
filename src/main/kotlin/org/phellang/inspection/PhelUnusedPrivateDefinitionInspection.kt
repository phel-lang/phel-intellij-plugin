package org.phellang.inspection

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import org.phellang.inspection.analysis.PhelUnusedPrivateDefinitionFinder
import org.phellang.language.psi.PhelList
import org.phellang.language.psi.PhelVisitor

/** Reports a private top-level definition that nothing in its own file references. */
class PhelUnusedPrivateDefinitionInspection : LocalInspectionTool() {

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : PhelVisitor() {
            override fun visitList(o: PhelList) {
                val name = PhelUnusedPrivateDefinitionFinder.unusedPrivateDefinition(o) ?: return

                // No quick fix: deleting a definition is a bigger step than deleting a binding, and
                // a name reached only through a macro would be gone before the user noticed.
                holder.registerProblem(
                    name,
                    "Private definition '${name.text}' is never used.",
                    ProblemHighlightType.LIKE_UNUSED_SYMBOL,
                )
            }
        }
    }
}
