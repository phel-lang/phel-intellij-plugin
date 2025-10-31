package org.phellang.syntax

import com.intellij.lexer.Lexer
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase
import com.intellij.psi.tree.IElementType
import org.phellang.language.PhelLexerAdapter
import org.phellang.syntax.mapping.PhelTokenAttributeMapper
import org.phellang.syntax.classification.PhelTokenClassifier

class PhelSyntaxHighlighter : SyntaxHighlighterBase() {
    
    override fun getHighlightingLexer(): Lexer {
        return PhelLexerAdapter()
    }

    override fun getTokenHighlights(tokenType: IElementType): Array<TextAttributesKey> {
        val category = PhelTokenClassifier.classifyToken(tokenType)
        return PhelTokenAttributeMapper.getTextAttributes(category)
    }
}
