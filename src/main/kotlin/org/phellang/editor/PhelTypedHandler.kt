package org.phellang.editor

import com.intellij.codeInsight.AutoPopupController.getInstance
import com.intellij.codeInsight.editorActions.TypedHandlerDelegate
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.fileTypes.FileType
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile
import org.phellang.PhelFileType
import org.phellang.editor.typing.pairing.PhelCharacterPairing
import org.phellang.editor.typing.pairing.PhelQuoteHandler

class PhelTypedHandler : TypedHandlerDelegate() {

    override fun charTyped(c: Char, project: Project, editor: Editor, file: PsiFile): Result {
        if (file.fileType !is PhelFileType) {
            return Result.CONTINUE
        }

        if (c in setOf('(', '[', '{')) {
            getInstance(project).scheduleAutoPopup(editor)
        }

        return Result.CONTINUE
    }

    override fun beforeCharTyped(c: Char, project: Project, editor: Editor, file: PsiFile, fileType: FileType): Result {
        if (fileType !is PhelFileType) {
            return Result.CONTINUE
        }

        val document = editor.document
        val offset = editor.caretModel.offset

        if (PhelCharacterPairing.isClosingCharacter(c)
            && PhelCharacterPairing.shouldSkipClosingChar(document, offset, c)
        ) {
            editor.caretModel.moveToOffset(offset + 1)
            return Result.STOP
        }

        val closingChar = PhelCharacterPairing.getClosingCharacter(c)
        if (closingChar != null && PhelCharacterPairing.shouldAutoClose(document, offset)) {
            document.insertString(offset, c.toString() + closingChar)
            editor.caretModel.moveToOffset(offset + 1)
            return Result.STOP // Prevent other handlers from processing this character
        }

        if (c == '"') {
            return PhelQuoteHandler.handleQuoteCharacter(editor, document, offset)
        }

        return Result.CONTINUE
    }
}
