package org.phellang.editor.paredit.surround

import com.intellij.lang.surroundWith.Surrounder
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement

/**
 * Wraps the selected forms in a bracket pair.
 *
 * Distinct from the paredit wrap actions, which take the single form at the caret. This works on a
 * selection spanning several forms, which is what Surround With is for.
 */
class PhelBracketSurrounder(
    private val open: String,
    private val close: String,
    private val templateDescription: String,
) : Surrounder {

    override fun getTemplateDescription(): String = templateDescription

    override fun isApplicable(elements: Array<out PsiElement>): Boolean = elements.isNotEmpty()

    override fun surroundElements(
        project: Project,
        editor: Editor,
        elements: Array<out PsiElement>,
    ): TextRange? {
        if (elements.isEmpty()) return null

        val start = elements.first().textRange.startOffset
        val end = elements.last().textRange.endOffset

        // End first: inserting at the start would shift the end offset out from under us.
        editor.document.insertString(end, close)
        editor.document.insertString(start, open)

        // Caret just inside the new opening bracket, ready to type the head of the form.
        return TextRange.from(start + open.length, 0)
    }
}
