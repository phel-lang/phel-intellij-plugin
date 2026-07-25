package org.phellang.editor.paredit

import com.intellij.codeInsight.editorActions.moveLeftRight.MoveElementLeftRightHandler
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import org.phellang.language.psi.PhelForm

/**
 * Backs Move Element Left/Right over the forms inside a bracketed container.
 *
 * Reordering arguments and map entries is the operation the paredit actions do not cover: slurp and
 * barf change what a form contains, never the order of what is already there.
 *
 * Every form is movable, the head included. `(+ 1 2)` swapping to `(1 + 2)` is nonsense, but so is
 * every other way of misusing a manual reorder, and the alternative — freezing the first element —
 * would break the data literals where there is no head at all.
 */
class PhelMoveElementLeftRightHandler : MoveElementLeftRightHandler() {

    override fun getMovableSubElements(element: PsiElement): Array<PsiElement> {
        if (!PhelPareditContainers.isContainer(element)) return PsiElement.EMPTY_ARRAY

        val forms = PsiTreeUtil.getChildrenOfType(element, PhelForm::class.java) ?: return PsiElement.EMPTY_ARRAY

        // Nothing to reorder below two, and the platform would still draw the action as available.
        return if (forms.size < 2) PsiElement.EMPTY_ARRAY else forms.toList().toTypedArray()
    }
}
