package org.phellang.language.todo

import com.intellij.lexer.Lexer
import com.intellij.psi.PsiFile
import com.intellij.psi.impl.search.IndexPatternBuilder
import com.intellij.psi.tree.IElementType
import com.intellij.psi.tree.TokenSet
import org.phellang.language.lexer.PhelLexerAdapter
import org.phellang.language.psi.PhelTypes
import org.phellang.language.psi.files.PhelFile

/**
 * Lets the platform find `TODO` and `FIXME` inside Phel comments.
 *
 * Without this a note in a `.phel` file reaches nothing: not the TODO tool window, not the commit
 * dialog's TODO check, not the gutter. The platform does the pattern matching itself; all it needs
 * is a lexer and the set of tokens whose text counts as prose.
 *
 * Only `;` line comments participate. `#_` discards the *form* that follows it, so a note inside one
 * is commented-out code rather than a message to the reader — and the `FORM_COMMENT` token is only
 * the two-character `#_` marker anyway, with the discarded form left in the tree beside it, so
 * including it would scan no prose at all.
 */
class PhelTodoIndexPatternBuilder : IndexPatternBuilder {

    override fun getIndexingLexer(file: PsiFile): Lexer? = if (file is PhelFile) PhelLexerAdapter() else null

    override fun getCommentTokenSet(file: PsiFile): TokenSet? = if (file is PhelFile) LINE_COMMENTS else null

    /**
     * Zero: the whole token, `;` included, is handed to the pattern.
     *
     * A delta exists to skip a delimiter that would otherwise be read as part of the text. `;` is not
     * a word character, so it cannot run into the `TODO` the pattern is anchored on.
     */
    override fun getCommentStartDelta(tokenType: IElementType?): Int = 0

    override fun getCommentEndDelta(tokenType: IElementType?): Int = 0

    private companion object {
        val LINE_COMMENTS: TokenSet = TokenSet.create(PhelTypes.LINE_COMMENT)
    }
}
