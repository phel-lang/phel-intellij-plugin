package org.phellang.language

import com.intellij.lang.ASTNode
import com.intellij.lang.ParserDefinition
import com.intellij.lang.PsiParser
import com.intellij.lexer.Lexer
import com.intellij.openapi.project.Project
import com.intellij.psi.FileViewProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.tree.IFileElementType
import com.intellij.psi.tree.TokenSet
import org.phellang.PhelLanguage
import org.phellang.language.parser.PhelParser
import org.phellang.language.psi.PhelFile
import org.phellang.language.psi.PhelTokenSets
import org.phellang.language.psi.PhelTypes

internal class PhelParserDefinition : ParserDefinition {
    override fun createLexer(project: Project?): Lexer {
        return PhelLexerAdapter()
    }

    override fun getCommentTokens(): TokenSet {
        return PhelTokenSets.LINE_COMMENT
    }

    override fun getStringLiteralElements(): TokenSet {
        return TokenSet.EMPTY
    }

    override fun createParser(project: Project?): PsiParser {
        return PhelParser()
    }

    override fun getFileNodeType(): IFileElementType {
        return FILE
    }

    override fun createFile(viewProvider: FileViewProvider): PsiFile {
        return PhelFile(viewProvider)
    }

    override fun createElement(node: ASTNode): PsiElement {
        return PhelTypes.Factory.createElement(node)
    }
}

@JvmField
val FILE: IFileElementType = IFileElementType(PhelLanguage.INSTANCE)