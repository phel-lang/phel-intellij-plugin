package org.phellang.editor.paredit

import com.intellij.lang.surroundWith.SurroundDescriptor
import com.intellij.lang.surroundWith.Surrounder
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.util.PsiTreeUtil
import org.phellang.editor.paredit.surround.PhelBracketSurrounder
import org.phellang.language.psi.PhelForm

/** Surround With (Ctrl+Alt+T) over a selection of Phel forms. */
class PhelSurroundDescriptor : SurroundDescriptor {

    override fun getSurrounders(): Array<Surrounder> = arrayOf(
        PhelBracketSurrounder("(", ")", "( ) list"),
        PhelBracketSurrounder("[", "]", "[ ] vector"),
        PhelBracketSurrounder("{", "}", "{ } map"),
    )

    /**
     * The top-level forms the selection covers.
     *
     * Only whole forms: a selection cutting through the middle of one has no valid surround, and
     * wrapping it anyway would produce unbalanced brackets.
     */
    override fun getElementsToSurround(file: PsiFile, startOffset: Int, endOffset: Int): Array<PsiElement> {
        if (startOffset >= endOffset) return PsiElement.EMPTY_ARRAY

        val start = file.findElementAt(startOffset) ?: return PsiElement.EMPTY_ARRAY
        val end = file.findElementAt(endOffset - 1) ?: return PsiElement.EMPTY_ARRAY

        val common = PsiTreeUtil.findCommonParent(start, end) ?: return PsiElement.EMPTY_ARRAY
        // Falls back to the file so a selection spanning top-level forms still surrounds.
        val container = PhelPareditContainers.enclosingContainerOf(common) ?: file

        val covered = PsiTreeUtil.getChildrenOfType(container, PhelForm::class.java)
            ?.filter { it.textRange.startOffset >= startOffset && it.textRange.endOffset <= endOffset }
            ?: return PsiElement.EMPTY_ARRAY

        return covered.toTypedArray()
    }

    /** Other descriptors (live templates) stay available alongside these. */
    override fun isExclusive(): Boolean = false
}
