package org.phellang.annotator.quickfixes

import com.intellij.codeInsight.intention.IntentionAction
import com.intellij.codeInsight.intention.PsiElementBaseIntentionAction
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import org.phellang.language.psi.PhelList
import org.phellang.language.psi.PhelSymbol

class PhelRemoveDuplicateImportQuickFix(
    private val symbolToRemove: PhelSymbol
) : PsiElementBaseIntentionAction(), IntentionAction {

    override fun getText(): String = "Remove duplicate import"

    override fun getFamilyName(): String = "Phel namespace imports"

    override fun isAvailable(project: Project, editor: Editor?, element: PsiElement): Boolean {
        return symbolToRemove.isValid
    }

    override fun invoke(project: Project, editor: Editor?, element: PsiElement) {
        WriteCommandAction.runWriteCommandAction(project) {
            removeRequireForm()
        }
    }

    private fun removeRequireForm() {
        // Find the parent (:require ...) list and remove it
        val requireList = PsiTreeUtil.getParentOfType(symbolToRemove, PhelList::class.java)
        if (requireList != null) {
            // Also remove any whitespace/newline before the require form
            val prevSibling = requireList.prevSibling
            if (prevSibling != null && prevSibling.text.isBlank()) {
                prevSibling.delete()
            }
            requireList.delete()
        }
    }

    override fun startInWriteAction(): Boolean = false
}
