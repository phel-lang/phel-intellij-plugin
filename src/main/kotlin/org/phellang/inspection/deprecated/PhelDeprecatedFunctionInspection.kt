package org.phellang.inspection.deprecated

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import org.phellang.completion.data.DeprecationInfo
import org.phellang.completion.data.PhelFunctionRegistry
import org.phellang.language.psi.PhelSymbol
import org.phellang.language.psi.PhelVisitor

class PhelDeprecatedFunctionInspection : LocalInspectionTool() {

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : PhelVisitor() {
            override fun visitSymbol(symbol: PhelSymbol) {
                val text = symbol.text ?: return

                if (PhelFunctionRegistry.isDeprecated(text)) {
                    val deprecation = getDeprecationInfo(text)
                    registerProblem(holder, symbol, text, deprecation)
                }
            }
        }
    }

    private fun getDeprecationInfo(functionName: String): DeprecationInfo? {
        return PhelFunctionRegistry.getFunction(functionName)?.documentation?.deprecation
    }

    private fun registerProblem(
        holder: ProblemsHolder, symbol: PhelSymbol, functionName: String, deprecation: DeprecationInfo?
    ) {
        val message = DeprecationMessageBuilder.build(functionName, deprecation)
        val replacement = DeprecationMessageBuilder.extractReplacement(deprecation)

        if (replacement != null) {
            holder.registerProblem(
                symbol, message, ProblemHighlightType.LIKE_DEPRECATED, PhelDeprecatedFunctionQuickFix(replacement)
            )
        } else {
            holder.registerProblem(
                symbol, message, ProblemHighlightType.LIKE_DEPRECATED
            )
        }
    }
}
