package org.phellang.editor.enter

import com.intellij.application.options.CodeStyle
import com.intellij.openapi.editor.Document
import com.intellij.psi.PsiFile
import org.phellang.editor.indentation.PhelIndentationCalculator

class PhelEnterHandlerIndentationCalculator {

    private val indentationCalculator = PhelIndentationCalculator()

    /**
     * The indentation the new line should end up with: one level per still-open bracket.
     *
     * Absolute, not relative. This used to return only the *extra* indentation to add on top of what
     * the platform's enter handler had already copied from the previous line, clamped at zero — so it
     * could indent further but never less. Closing a form therefore left the new line at the old
     * depth: enter after `(print "hello"))` gave two spaces where the form was over and the answer
     * was none, and after `(print 1)))` gave four.
     *
     * The width comes from [file]'s Code Style options rather than a constant. It was hard-coded to
     * two spaces, so the Code Style page could show an indent size the formatter honoured and Enter
     * ignored, and switching the page to tabs reformatted with tabs but typed spaces.
     */
    fun targetIndentation(file: PsiFile, document: Document, currentLineNumber: Int, textBeforeCaret: String): String {
        val level = indentationCalculator.calculateIndentationLevel(document, currentLineNumber, textBeforeCaret)
        val options = CodeStyle.getIndentOptions(file)

        if (options.USE_TAB_CHARACTER) {
            return "\t".repeat(level)
        }

        return " ".repeat(level * options.INDENT_SIZE)
    }

    fun getCurrentIndentationSpaces(currentLineText: String): Int {
        return currentLineText.takeWhile { it.isWhitespace() }.length
    }
}
