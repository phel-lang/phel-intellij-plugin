package org.phellang.editor.paredit

import com.intellij.openapi.actionSystem.DataContext
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.editor.Caret
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.actionSystem.EditorActionHandler
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiFile
import org.phellang.language.psi.files.PhelFile

class PhelPareditActionHandler(
    private val operation: (PsiFile, Int) -> List<PhelTextEdit>?,
) : EditorActionHandler() {

    override fun isEnabledForCaret(editor: Editor, caret: Caret, dataContext: DataContext?): Boolean {
        val project = editor.project ?: return false
        val psiFile = PsiDocumentManager.getInstance(project).getPsiFile(editor.document)
        return psiFile is PhelFile
    }

    override fun doExecute(editor: Editor, caret: Caret?, dataContext: DataContext?) {
        val project = editor.project ?: return
        val psiManager = PsiDocumentManager.getInstance(project)
        psiManager.commitDocument(editor.document)

        val psiFile = psiManager.getPsiFile(editor.document) as? PhelFile ?: return
        val offset = (caret ?: editor.caretModel.primaryCaret).offset
        val edits = operation(psiFile, offset) ?: return

        WriteCommandAction.runWriteCommandAction(project) {
            edits.sortedByDescending { it.range.startOffset }.forEach { edit ->
                editor.document.replaceString(edit.range.startOffset, edit.range.endOffset, edit.replacement)
            }
            psiManager.commitDocument(editor.document)
        }
    }
}
