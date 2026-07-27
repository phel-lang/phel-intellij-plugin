package org.phellang.editor.smartenter

import com.intellij.codeInsight.editorActions.smartEnter.SmartEnterProcessor
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiFile
import org.phellang.editor.indentation.PhelLineAnalyzer
import org.phellang.language.psi.files.PhelFile

/**
 * Complete Current Statement: closes every bracket the current line leaves open.
 *
 * For a Lisp that is what "finish this statement" means — `(defn f [x` becomes `(defn f [x])` with
 * the caret after it — and it is the one editor action a paren-heavy language most wants.
 *
 * Which brackets are open comes from [PhelLineAnalyzer.unclosedOpeners], the same scanner the Enter
 * handler indents from, so the two cannot disagree about what a bracket is: both skip strings, `;`
 * comments and `\(` character literals alike.
 *
 * It balances what is open and stops there. Adding a newline and an indent afterwards is Enter's
 * job, and doing it here would make one keystroke do two things a user may not want together.
 */
class PhelSmartEnterProcessor : SmartEnterProcessor() {

    override fun process(project: Project, editor: Editor, psiFile: PsiFile): Boolean {
        if (psiFile !is PhelFile) return false

        val document = editor.document
        val analyzer = PhelLineAnalyzer(document)

        val line = document.getLineNumber(editor.caretModel.offset)
        val lineStart = document.getLineStartOffset(line)
        val lineEnd = document.getLineEndOffset(line)

        // Before any trailing comment, not at the end of the line: `(println (inc 1) ; note` ends
        // inside a comment, and a paren appended there would be commented out along with the note.
        val insertAt = lineStart + analyzer.activeCodeLength(document.getText(TextRange(lineStart, lineEnd)))

        // Scanned to the insertion point rather than to the caret: the statement being completed is
        // the line, so a caret sitting mid-line still closes what the whole line leaves open.
        val open = analyzer.unclosedOpeners(document.getText(TextRange(0, insertAt)))
        if (open.isEmpty()) return false

        val closers = open.reversed().map(::closerFor).joinToString("")
        document.insertString(insertAt, closers)
        editor.caretModel.moveToOffset(insertAt + closers.length)

        return true
    }

    private fun closerFor(opener: Char): Char = when (opener) {
        '[' -> ']'
        '{' -> '}'
        else -> ')'
    }
}
