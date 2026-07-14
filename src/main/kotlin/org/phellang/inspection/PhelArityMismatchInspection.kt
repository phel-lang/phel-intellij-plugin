package org.phellang.inspection

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import org.phellang.registry.PhelArityResolver
import org.phellang.registry.accepts
import org.phellang.registry.describe
import org.phellang.language.psi.PhelList
import org.phellang.language.psi.utils.PhelPsiUtils
import org.phellang.language.psi.PhelVisitor

/**
 * Warns when a call passes the wrong number of arguments to a known function.
 *
 * Whether a list is even a checkable call site — not a quoted form, a threading argument, a
 * shadowing local binding, or a variadic special form — is decided by [PhelArityCallSite]; this
 * class only compares the argument count against the resolved arities.
 */
class PhelArityMismatchInspection : LocalInspectionTool() {

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : PhelVisitor() {
            override fun visitList(o: PhelList) {
                val forms = PhelPsiUtils.activeForms(o)
                if (forms.isEmpty()) return

                val head = PhelPsiUtils.asSymbol(forms[0]) ?: return
                val name = head.text ?: return
                if (PhelArityCallSite.shouldSkip(o, head, name, forms)) return

                val arities = PhelArityResolver.resolve(head.project, name) ?: return
                if (arities.isEmpty()) return

                val argCount = forms.size - 1
                if (!arities.accepts(argCount)) {
                    holder.registerProblem(
                        head,
                        "Wrong number of args ($argCount) passed to '$name'. Expected: ${arities.describe()}.",
                        ProblemHighlightType.GENERIC_ERROR_OR_WARNING,
                    )
                }
            }
        }
    }
}
