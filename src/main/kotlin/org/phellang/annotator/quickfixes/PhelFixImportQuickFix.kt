package org.phellang.annotator.quickfixes

import com.intellij.codeInsight.intention.IntentionAction
import com.intellij.codeInsight.intention.PsiElementBaseIntentionAction
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFileFactory
import com.intellij.psi.SmartPointerManager
import com.intellij.psi.SmartPsiElementPointer
import com.intellij.psi.util.PsiTreeUtil
import org.phellang.language.infrastructure.PhelLanguage
import org.phellang.language.psi.PhelSymbol

class PhelFixImportQuickFix(
    symbolToFix: PhelSymbol, private val correctNamespace: String
) : PsiElementBaseIntentionAction(), IntentionAction {

    private val symbolPointer: SmartPsiElementPointer<PhelSymbol> =
        SmartPointerManager.getInstance(symbolToFix.project).createSmartPsiElementPointer(symbolToFix)

    override fun getText(): String = "Change to '$correctNamespace'"

    override fun getFamilyName(): String = "Phel namespace imports"

    override fun isAvailable(project: Project, editor: Editor?, element: PsiElement): Boolean {
        return symbolPointer.element?.isValid == true
    }

    override fun invoke(project: Project, editor: Editor?, element: PsiElement) {
        val symbolToFix = symbolPointer.element ?: return
        WriteCommandAction.runWriteCommandAction(project) {
            val tempFile =
                PsiFileFactory.getInstance(project).createFileFromText("temp.phel", PhelLanguage, correctNamespace)
            val newSymbol = PsiTreeUtil.findChildOfType(tempFile, PhelSymbol::class.java) ?: return@runWriteCommandAction
            symbolToFix.replace(newSymbol)
        }
    }

    override fun startInWriteAction(): Boolean = false
}
