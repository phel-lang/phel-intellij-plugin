package org.phellang.annotator.quickfixes

import com.intellij.codeInsight.intention.IntentionAction
import com.intellij.codeInsight.intention.PsiElementBaseIntentionAction
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.SmartPointerManager
import com.intellij.psi.SmartPsiElementPointer
import com.intellij.psi.util.PsiTreeUtil
import org.phellang.language.psi.PhelList
import org.phellang.language.psi.PhelSymbol

class PhelRemoveImportQuickFix(
    symbolToRemove: PhelSymbol, private val actionText: String = "Remove import"
) : PsiElementBaseIntentionAction(), IntentionAction {

    private val symbolPointer: SmartPsiElementPointer<PhelSymbol> =
        SmartPointerManager.getInstance(symbolToRemove.project).createSmartPsiElementPointer(symbolToRemove)

    override fun getText(): String = actionText

    override fun getFamilyName(): String = "Phel namespace imports"

    override fun isAvailable(project: Project, editor: Editor?, element: PsiElement): Boolean {
        return symbolPointer.element?.isValid == true
    }

    override fun invoke(project: Project, editor: Editor?, element: PsiElement) {
        val symbolToRemove = symbolPointer.element ?: return
        WriteCommandAction.runWriteCommandAction(project) {
            val requireList = PsiTreeUtil.getParentOfType(symbolToRemove, PhelList::class.java) ?: return@runWriteCommandAction
            val prevSibling = requireList.prevSibling
            if (prevSibling != null && prevSibling.text.isBlank()) {
                prevSibling.delete()
            }
            requireList.delete()
        }
    }

    override fun startInWriteAction(): Boolean = false
}
