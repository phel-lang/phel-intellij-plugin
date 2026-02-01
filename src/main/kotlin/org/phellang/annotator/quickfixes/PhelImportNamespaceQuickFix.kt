package org.phellang.annotator.quickfixes

import com.intellij.codeInsight.intention.IntentionAction
import com.intellij.codeInsight.intention.PsiElementBaseIntentionAction
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import org.phellang.language.psi.PhelNamespaceImporter
import org.phellang.language.psi.files.PhelFile

class PhelImportNamespaceQuickFix(
    private val fullNamespace: String
) : PsiElementBaseIntentionAction(), IntentionAction {

    override fun getText(): String = "Import '$fullNamespace'"

    override fun getFamilyName(): String = "Phel namespace imports"

    override fun isAvailable(project: Project, editor: Editor?, element: PsiElement): Boolean {
        val file = element.containingFile as? PhelFile ?: return false
        // Only available if not already imported
        return !isAlreadyImported(file)
    }

    override fun invoke(project: Project, editor: Editor?, element: PsiElement) {
        val file = element.containingFile as? PhelFile ?: return

        WriteCommandAction.runWriteCommandAction(project) {
            PhelNamespaceImporter.ensureNamespaceImportedByFullName(file, fullNamespace)
        }
    }

    private fun isAlreadyImported(file: PhelFile): Boolean {
        val nsDeclaration = org.phellang.language.psi.PhelNamespaceUtils.findNamespaceDeclaration(file) ?: return false
        return org.phellang.language.psi.PhelNamespaceUtils.isNamespaceRequired(nsDeclaration, fullNamespace)
    }

    override fun startInWriteAction(): Boolean = false
}
