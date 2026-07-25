package org.phellang.inspection.quickfixes

import com.intellij.codeInspection.LocalQuickFix
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import org.phellang.language.psi.PhelList
import org.phellang.language.psi.files.PhelFile
import org.phellang.language.psi.utils.PhelPsiUtils

/**
 * Creates a `defn` for a name that is being called but does not exist.
 *
 * Inserted *above* the form that calls it, never appended. Phel resolves a symbol against the
 * namespace's globals as they stand at that point in the file, so a definition placed after its
 * caller does not compile — `(defn caller [] (callee))` before `(defn callee [] 42)` fails with
 * `PHEL001 Cannot resolve symbol 'callee'`. Appending would have produced code as broken as the code
 * it was fixing.
 */
class PhelCreateFunctionQuickFix(private val functionName: String) : LocalQuickFix {

    override fun getFamilyName(): String = "Create function"

    override fun getName(): String = "Create function '$functionName'"

    override fun applyFix(project: Project, descriptor: ProblemDescriptor) {
        val symbol = descriptor.psiElement ?: return
        val call = PsiTreeUtil.getParentOfType(symbol, PhelList::class.java) ?: return
        val anchor = topLevelFormContaining(call) ?: return

        val file = symbol.containingFile ?: return
        val document = PsiDocumentManager.getInstance(project).getDocument(file) ?: return

        val definition = "(defn $functionName [${parameterNames(call).joinToString(" ")}]\n  )\n\n"
        document.insertString(anchor.textRange.startOffset, definition)

        PsiDocumentManager.getInstance(project).commitDocument(document)
    }

    /** The outermost list around [call] — the definition it sits in, which the new one goes above. */
    private fun topLevelFormContaining(call: PhelList): PsiElement? {
        var current: PsiElement = call

        while (true) {
            val parent = current.parent ?: return null
            if (parent is PhelFile) return current
            current = parent
        }
    }

    /**
     * Parameter names taken from the call's own arguments, so `(greet user count)` yields
     * `[user count]` rather than `[arg1 arg2]`. An argument that is not a plain symbol — or one
     * whose name is already taken — falls back to its position.
     */
    private fun parameterNames(call: PhelList): List<String> {
        val arguments = PhelPsiUtils.activeForms(call).drop(1)
        val names = mutableListOf<String>()

        arguments.forEachIndexed { index, argument ->
            // The symbol has to *be* the argument, not merely sit inside it: asSymbol digs into a
            // form, so `(dec n)` would otherwise contribute its head and name the parameter `dec`.
            val candidate = PhelPsiUtils.asSymbol(argument)
                ?.takeIf { it.textRange == argument.textRange }
                ?.text

            names += candidate?.takeIf { it.isValidName() && it !in names } ?: "arg${index + 1}"
        }

        return names
    }

    /** `&` marks a rest parameter and `%`/`$` are short-fn anaphors; none can name a parameter. */
    private fun String.isValidName(): Boolean =
        isNotBlank() && !startsWith("&") && !startsWith("%") && !startsWith("$") && !contains("/")
}
