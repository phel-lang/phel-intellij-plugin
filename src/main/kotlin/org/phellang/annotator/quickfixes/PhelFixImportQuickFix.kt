package org.phellang.annotator.quickfixes

import com.intellij.codeInsight.intention.IntentionAction
import com.intellij.codeInsight.intention.PsiElementBaseIntentionAction
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFileFactory
import com.intellij.psi.util.PsiTreeUtil
import org.phellang.language.infrastructure.PhelLanguage
import org.phellang.language.psi.PhelSymbol

class PhelFixImportQuickFix(
    private val symbolToFix: PhelSymbol, private val correctNamespace: String
) : PsiElementBaseIntentionAction(), IntentionAction {

    override fun getText(): String = "Change to '$correctNamespace'"

    override fun getFamilyName(): String = "Phel namespace imports"

    override fun isAvailable(project: Project, editor: Editor?, element: PsiElement): Boolean {
        return symbolToFix.isValid
    }

    override fun invoke(project: Project, editor: Editor?, element: PsiElement) {
        WriteCommandAction.runWriteCommandAction(project) {
            replaceNamespace()
        }
    }

    private fun replaceNamespace() {
        val project = symbolToFix.project

        // Create a temporary file with just the namespace symbol
        val tempFile =
            PsiFileFactory.getInstance(project).createFileFromText("temp.phel", PhelLanguage, correctNamespace)

        // Extract the symbol from the temporary file
        val newSymbol = PsiTreeUtil.findChildOfType(tempFile, PhelSymbol::class.java)

        if (newSymbol != null) {
            symbolToFix.replace(newSymbol)
        }
    }

    override fun startInWriteAction(): Boolean = false
}
