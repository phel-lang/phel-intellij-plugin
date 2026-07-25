package org.phellang.inspection

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import org.phellang.inspection.analysis.PhelUnresolvedSymbolFinder
import org.phellang.language.psi.PhelSymbol
import org.phellang.language.psi.PhelVisitor

/**
 * Reports a bare symbol that names nothing in scope.
 *
 * Phel's analyzer throws `PHEL001 Cannot resolve symbol` for these, so the code does not compile —
 * the message deliberately matches the compiler's wording. The plugin already reports the qualified
 * form of this (`ns/name`); this closes the gap for unqualified names.
 */
class PhelUnresolvedSymbolInspection : LocalInspectionTool() {

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : PhelVisitor() {
            override fun visitSymbol(o: PhelSymbol) {
                val name = PhelUnresolvedSymbolFinder.unresolvedName(o) ?: return

                holder.registerProblem(o, "Cannot resolve symbol '$name'", ProblemHighlightType.WARNING)
            }
        }
    }
}
