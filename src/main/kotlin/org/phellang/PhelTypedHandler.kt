package org.phellang

import com.intellij.codeInsight.AutoPopupController
import com.intellij.codeInsight.editorActions.TypedHandlerDelegate
import com.intellij.openapi.editor.Document
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.fileTypes.FileType
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile

class PhelTypedHandler : TypedHandlerDelegate() {
    override fun charTyped(c: Char, project: Project, editor: Editor, file: PsiFile): Result {
        if (file.fileType !is PhelFileType) {
            return Result.CONTINUE
        }

        // Trigger completion popup for certain contexts after character has been inserted
        if (c == '(' || c == '[' || c == '{') {
            AutoPopupController.getInstance(project).scheduleAutoPopup(editor)
        }

        return Result.CONTINUE
    }

    override fun beforeCharTyped(c: Char, project: Project, editor: Editor, file: PsiFile, fileType: FileType): Result {
        if (fileType !is PhelFileType) {
            return Result.CONTINUE
        }

        val document = editor.document
        val offset = editor.caretModel.offset

        // Skip over closing characters if they're already present
        if (isClosingCharacter(c) && shouldSkipClosingChar(document, offset, c)) {
            editor.caretModel.moveToOffset(offset + 1)
            return Result.STOP
        }

        // Handle opening characters - auto-close them
        val closingChar = getClosingCharacter(c)
        if (closingChar != null && shouldAutoClose(document, offset)) {
            // Insert both the opening and closing character
            document.insertString(offset, c.toString() + closingChar)
            // Position cursor between them (after the opening character)
            editor.caretModel.moveToOffset(offset + 1)
            return Result.STOP // Prevent other handlers from processing this character
        }

        // Handle quote characters specially
        if (c == '"') {
            return handleQuoteCharacter(editor, document, offset)
        }

        return Result.CONTINUE
    }

    private fun getClosingCharacter(c: Char): String? {
        return when (c) {
            '(' -> ")"
            '[' -> "]"
            '{' -> "}"
            else -> null
        }
    }

    private fun isClosingCharacter(c: Char): Boolean {
        return c == ')' || c == ']' || c == '}'
    }

    private fun shouldAutoClose(document: Document, offset: Int): Boolean {
        val text = document.charsSequence

        // Don't auto-close if we're inside a string
        if (isInsideString(text, offset)) {
            return false
        }

        // Don't auto-close if the next character is alphanumeric (avoid interfering with typing)
        if (offset < text.length) {
            val nextChar = text[offset]
            if (Character.isLetterOrDigit(nextChar) || nextChar == '-' || nextChar == '_') {
                return false
            }
        }

        return true
    }

    private fun shouldSkipClosingChar(document: Document, offset: Int, closingChar: Char): Boolean {
        val text = document.charsSequence

        if (offset >= text.length) {
            return false
        }

        val charAtOffset = text[offset]

        // Skip if the same closing character is already at this position
        return charAtOffset == closingChar
    }

    private fun handleQuoteCharacter(editor: Editor, document: Document, offset: Int): Result {
        val text = document.charsSequence

        // If we're at a quote, skip over it instead of inserting a new pair
        if (offset < text.length && text[offset] == '"') {
            if (!isInsideString(text, offset)) {
                editor.caretModel.moveToOffset(offset + 1)
                return Result.STOP
            }
        }

        // If we should auto-close the quote
        if (!isInsideString(text, offset)) {
            // Insert both opening and closing quote
            document.insertString(offset, "\"\"")
            // Position cursor between them
            editor.caretModel.moveToOffset(offset + 1)
            return Result.STOP
        }

        return Result.CONTINUE
    }

    private fun isInsideString(text: CharSequence, offset: Int): Boolean {
        // Count unescaped quotes before this position
        var quoteCount = 0
        var i = 0
        while (i < offset && i < text.length) {
            val c = text[i]
            if (c == '"') {
                // Check if this quote is escaped
                var backslashCount = 0
                var j = i - 1
                while (j >= 0 && text[j] == '\\') {
                    backslashCount++
                    j--
                }
                // If even number of backslashes (including 0), quote is not escaped
                if (backslashCount % 2 == 0) {
                    quoteCount++
                }
            }
            i++
        }

        // Odd number of quotes means we're inside a string
        return quoteCount % 2 == 1
    }
}