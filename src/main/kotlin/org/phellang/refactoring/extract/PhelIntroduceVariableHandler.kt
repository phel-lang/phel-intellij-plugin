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
import org.phellang.language.psi.files.PhelFile

/**
 * Extract Variable: binds the selected expression to a `let` name.
 *
 * The expression is whichever form the selection covers, or the form under the caret when nothing is
 * selected — so the refactoring works from a bare caret the way the rest of the IDE's introduce
 * actions do.
 *
 * A generated name is used, unique against what is already in scope, and the caret lands on it so it
 * can be typed over immediately. Only the selected occurrence is replaced; replacing every identical
 * one needs a prompt to be anything but surprising, and that is a separate decision.
 */
class PhelIntroduceVariableHandler : RefactoringActionHandler {

    override fun invoke(project: Project, editor: Editor?, file: PsiFile?, dataContext: DataContext?) {
        if (editor == null || file !is PhelFile) return

        val expression = expressionAt(editor, file)
        if (expression == null || !PhelExtractVariable.canExtract(expression)) {
            CommonRefactoringUtil.showErrorHint(
                project, editor, CANNOT_EXTRACT, REFACTORING_NAME, null,
            )
            return
        }

        val name = uniqueNameNear(expression)
        val introduced = WriteCommandAction.writeCommandAction(project, file)
            .withName(REFACTORING_NAME)
            .compute<PsiElement?, RuntimeException> { PhelExtractVariable.extract(expression, name) }

        introduced?.let { editor.caretModel.moveToOffset(it.textRange.startOffset) }
    }

    /** Extract Variable is a caret/selection refactoring; the element-array entry point is not it. */
    override fun invoke(project: Project, elements: Array<out PsiElement>, dataContext: DataContext?) = Unit

    /**
     * The selection's outermost covering form, or the innermost form at the caret.
     *
     * `findCommonParent` over the selection ends on whatever node spans it, which for a partial
     * selection is the enclosing form — the nearest thing to what the user meant that is still a
     * valid expression.
     */
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

    /**
     * A name the enclosing top-level form does not already mention.
     *
     * Deliberately blunt — any whole-word occurrence disqualifies a candidate, not just a binding.
     * The name is a placeholder the user types over, so over-avoiding costs nothing, while shadowing
     * a binding the extracted expression itself depends on would silently change what it means.
     */
    private fun uniqueNameNear(expression: PhelForm): String {
        val scope = generateSequence(expression as PsiElement) { it.parent }
            .takeWhile { it !is PhelFile }
            .lastOrNull()?.text.orEmpty()

        return generateSequence(1) { it + 1 }
            .map { if (it == 1) BASE_NAME else "$BASE_NAME$it" }
            .first { candidate -> !Regex("(?<![\\w-])${Regex.escape(candidate)}(?![\\w-])").containsMatchIn(scope) }
    }

    private companion object {
        const val REFACTORING_NAME = "Extract Variable"
        const val CANNOT_EXTRACT = "Select an expression to extract."
        const val BASE_NAME = "x"
    }
}
