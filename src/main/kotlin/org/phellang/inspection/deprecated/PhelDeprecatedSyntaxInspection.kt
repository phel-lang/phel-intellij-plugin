package org.phellang.inspection.deprecated

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiElementVisitor
import org.phellang.language.psi.PhelTypes
import org.phellang.language.psi.PhelShortFn
import org.phellang.language.psi.PhelVisitor

class PhelDeprecatedSyntaxInspection : LocalInspectionTool() {

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : PhelVisitor() {
            override fun visitElement(element: PsiElement) {
                val elementType = element.node?.elementType ?: return

                when (elementType) {
                    PhelTypes.LINE_COMMENT -> checkHashComment(element, holder)
                    PhelTypes.MULTILINE_COMMENT -> checkMultilineComment(element, holder)
                    PhelTypes.COMMA -> checkCommaUnquote(element, holder)
                    PhelTypes.COMMA_AT -> checkCommaAtUnquote(element, holder)
                }
            }

            override fun visitShortFn(shortFn: PhelShortFn) {
                holder.registerProblem(
                    shortFn.firstChild,
                    "Deprecated: '|(...)' short function syntax. Use '#(...)' with %, %1, %2, %& placeholders instead.",
                    ProblemHighlightType.LIKE_DEPRECATED
                )
            }
        }
    }

    private fun checkHashComment(element: PsiElement, holder: ProblemsHolder) {
        val text = element.text ?: return
        if (text.startsWith("#") && !text.startsWith(";")) {
            holder.registerProblem(
                element, "Deprecated: '#' line comment. Use ';' instead.", ProblemHighlightType.LIKE_DEPRECATED
            )
        }
    }

    private fun checkMultilineComment(element: PsiElement, holder: ProblemsHolder) {
        val text = element.text ?: return
        if (text.startsWith("#|") || text == "|#") {
            holder.registerProblem(
                element,
                "Deprecated: '#| ... |#' block comments. Use ';' line comments or '#_' form comments instead.",
                ProblemHighlightType.LIKE_DEPRECATED
            )
        }
    }

    private fun checkCommaUnquote(element: PsiElement, holder: ProblemsHolder) {
        holder.registerProblem(
            element, "Deprecated: ',' unquote. Use '~' instead.", ProblemHighlightType.LIKE_DEPRECATED
        )
    }

    private fun checkCommaAtUnquote(element: PsiElement, holder: ProblemsHolder) {
        holder.registerProblem(
            element, "Deprecated: ',@' unquote-splicing. Use '~@' instead.", ProblemHighlightType.LIKE_DEPRECATED
        )
    }
}
