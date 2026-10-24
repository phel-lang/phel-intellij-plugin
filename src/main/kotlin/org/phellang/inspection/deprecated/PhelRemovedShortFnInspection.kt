package org.phellang.inspection.deprecated

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.LocalQuickFix
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.util.PsiTreeUtil
import org.phellang.language.psi.PhelHashFn
import org.phellang.language.psi.PhelSymbol
import org.phellang.language.psi.PhelVisitor
import org.phellang.language.psi.analysis.PhelPipeShortFn

/**
 * Flags the `|(...)` short function Phel 0.50 removed (phel-lang #2827). From 0.50 on, `|` is an
 * ordinary symbol and the list after it a call of its own, so such code no longer means what it
 * says: it used to surface as "Cannot resolve symbol '|'" and a miscounted call around it.
 *
 * The quick fix is a plain respelling, unlike the reordering [PhelSupersededFormInspection] would
 * need: `|` becomes `#`, and each placeholder of this function, `$`, `$1` or `$&`, its `%` twin.
 */
class PhelRemovedShortFnInspection : LocalInspectionTool() {

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : PhelVisitor() {
            override fun visitSymbol(symbol: PhelSymbol) {
                val body = PhelPipeShortFn.bodyOf(symbol) ?: return

                // Converting a body that holds a `#(...)` would nest one `#(...)` in another; whether
                // the reader accepts that is not settled, so only the report is offered then.
                val fixes = if (PsiTreeUtil.findChildOfType(body, PhelHashFn::class.java) == null) {
                    arrayOf<LocalQuickFix>(ConvertToHashFnQuickFix())
                } else {
                    emptyArray()
                }

                holder.registerProblem(
                    symbol,
                    "'|(...)' short functions were removed in Phel 0.50; write #(...) with % placeholders",
                    ProblemHighlightType.GENERIC_ERROR_OR_WARNING,
                    *fixes,
                )
            }
        }
    }

    private class ConvertToHashFnQuickFix : LocalQuickFix {

        override fun getFamilyName(): String = "Convert to #(...)"

        override fun applyFix(project: Project, descriptor: ProblemDescriptor) {
            val marker = descriptor.psiElement as? PhelSymbol ?: return
            val body = PhelPipeShortFn.bodyOf(marker) ?: return
            val documentManager = PsiDocumentManager.getInstance(project)
            val document = documentManager.getDocument(marker.containingFile) ?: return

            // Latest offset first, so each replacement leaves the earlier ranges where they were.
            val edits = PhelPipeShortFn.placeholdersOf(body).map { it.textRange to "%" + it.text.drop(1) } +
                    (marker.textRange to "#")
            documentManager.doPostponedOperationsAndUnblockDocument(document)
            for ((range, replacement) in edits.sortedByDescending { it.first.startOffset }) {
                document.replaceString(range.startOffset, range.endOffset, replacement)
            }
            documentManager.commitDocument(document)
        }
    }
}
