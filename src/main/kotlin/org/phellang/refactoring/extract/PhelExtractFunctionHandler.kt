package org.phellang.refactoring.extract

import com.intellij.openapi.actionSystem.DataContext
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.refactoring.RefactoringActionHandler
import com.intellij.refactoring.util.CommonRefactoringUtil
import org.phellang.language.psi.PhelForm
import org.phellang.language.psi.PhelList
import org.phellang.language.psi.PhelPsiFactory
import org.phellang.language.psi.files.PhelFile

/**
 * Extract Function: lifts the selected expression into a new top-level `defn` and calls it.
 *
 * The parameter list is derived, not asked for: [PhelFreeVariables] works out which locals the
 * expression uses but does not bind, and those become the parameters, in the order they appear.
 * Everything else it references — globals, stdlib, `(:require)`d names — resolves the same from the
 * new function's position, so it needs no help.
 *
 * The new `defn` goes immediately before the top-level form the expression came from, which is where
 * a reader looks for it and keeps the diff local.
 */
class PhelExtractFunctionHandler : RefactoringActionHandler {

    override fun invoke(project: Project, editor: Editor?, file: PsiFile?, dataContext: DataContext?) {
        if (editor == null || file !is PhelFile) return

        val expression = expressionAt(editor, file)
        if (expression == null || expression !is PhelList) {
            CommonRefactoringUtil.showErrorHint(project, editor, CANNOT_EXTRACT, REFACTORING_NAME, null)
            return
        }

        val enclosing = topLevelFormOf(expression)
        if (enclosing == null || enclosing === expression) {
            CommonRefactoringUtil.showErrorHint(project, editor, NOTHING_TO_LIFT, REFACTORING_NAME, null)
            return
        }

        val name = uniqueNameIn(file)
        val parameters = PhelFreeVariables.of(expression)

        val call = WriteCommandAction.writeCommandAction(project, file)
            .withName(REFACTORING_NAME)
            .compute<PsiElement?, RuntimeException> { extract(file, enclosing, expression, name, parameters) }

        call?.let { editor.caretModel.moveToOffset(it.textRange.startOffset) }
    }

    override fun invoke(project: Project, elements: Array<out PsiElement>, dataContext: DataContext?) = Unit

    private fun extract(
        file: PhelFile,
        enclosing: PhelList,
        expression: PhelList,
        name: String,
        parameters: List<String>,
    ): PsiElement {
        val definition = PhelPsiFactory.createList(
            file.project,
            "(defn $name [${parameters.joinToString(" ")}]\n  ${expression.text})",
        )

        // The call replaces the expression first: adding the definition above shifts every offset
        // below it, and the expression's own node is what is being swapped out.
        val callText = if (parameters.isEmpty()) "($name)" else "($name ${parameters.joinToString(" ")})"
        val call = expression.replace(PhelPsiFactory.createList(file.project, callText))

        file.addBefore(definition, enclosing)
        file.addBefore(PhelPsiFactory.createWhitespace(file.project, "\n\n"), enclosing)

        return call
    }

    /** The outermost list the expression sits in — the `defn` or `def` it belongs to. */
    private fun topLevelFormOf(expression: PhelForm): PhelList? =
        generateSequence(PsiTreeUtil.getParentOfType(expression, PhelList::class.java, false)) {
            PsiTreeUtil.getParentOfType(it, PhelList::class.java)
        }.lastOrNull { it.parent is PhelFile }

    private fun expressionAt(editor: Editor, file: PhelFile): PhelForm? {
        val selection = editor.selectionModel
        if (selection.hasSelection()) {
            val start = file.findElementAt(selection.selectionStart) ?: return null
            val end = file.findElementAt(selection.selectionEnd - 1) ?: return null
            val common = PsiTreeUtil.findCommonParent(start, end) ?: return null

            return common as? PhelForm ?: PsiTreeUtil.getParentOfType(common, PhelForm::class.java)
        }

        val at = file.findElementAt(editor.caretModel.offset) ?: return null

        return PsiTreeUtil.getParentOfType(at, PhelForm::class.java)
    }

    /** A name the file does not already mention, so the new `defn` cannot collide with one. */
    private fun uniqueNameIn(file: PhelFile): String {
        val text = file.text

        return generateSequence(1) { it + 1 }
            .map { if (it == 1) BASE_NAME else "$BASE_NAME$it" }
            .first { candidate -> !Regex("(?<![\\w-])${Regex.escape(candidate)}(?![\\w-])").containsMatchIn(text) }
    }

    private companion object {
        const val REFACTORING_NAME = "Extract Function"
        const val CANNOT_EXTRACT = "Select an expression to extract into a function."
        const val NOTHING_TO_LIFT = "This expression is already a top-level form."
        const val BASE_NAME = "extracted"
    }
}
