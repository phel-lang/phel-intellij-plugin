package org.phellang.refactoring

import com.intellij.lang.refactoring.RefactoringSupportProvider
import com.intellij.refactoring.RefactoringActionHandler
import com.intellij.psi.PsiElement
import org.phellang.language.psi.PhelSymbol
import org.phellang.refactoring.extract.PhelIntroduceVariableHandler
import org.phellang.refactoring.safedelete.PhelSafeDeleteTarget

class PhelRefactoringSupportProvider : RefactoringSupportProvider() {

    override fun isMemberInplaceRenameAvailable(element: PsiElement, context: PsiElement?): Boolean {
        return element is PhelSymbol
    }

    override fun isInplaceRenameAvailable(element: PsiElement, context: PsiElement?): Boolean {
        return element is PhelSymbol
    }

    /**
     * Offered for a top-level definition's name, which is what [PhelSafeDeleteProcessor] can remove
     * whole. Deciding it here and in the processor from the same helper keeps the menu entry from
     * appearing where the refactoring would only half-work.
     */
    override fun isSafeDeleteAvailable(element: PsiElement): Boolean =
        PhelSafeDeleteTarget.enclosingFormOf(element) != null

    /**
     * What makes the platform's Extract Variable action light up for Phel.
     *
     * `IntroduceVariableAction` asks every provider registered for the file's language for a handler
     * and hides itself when none returns one, so this method *is* the registration — the handler
     * existing is not enough. It went missing once and nothing noticed, because the tests built
     * [PhelIntroduceVariableHandler] directly instead of going through the action.
     */
    override fun getIntroduceVariableHandler(): RefactoringActionHandler = PhelIntroduceVariableHandler()
}
