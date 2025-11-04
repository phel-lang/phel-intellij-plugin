package org.phellang.debug

import com.intellij.lexer.Lexer
import com.intellij.psi.tree.IElementType
import org.phellang.language.lexer.PhelLexerAdapter

/**
 * Debug utility to examine token types and their text content.
 * This is useful for understanding how the lexer tokenizes Phel code.
 */
object PhelTokenDebugger {
    
    fun debugTokens(text: String) {
        val lexer: Lexer = PhelLexerAdapter()
        lexer.start(text)
        
        println("=== TOKENIZING: '$text' ===")
        
        var tokenIndex = 0
        while (lexer.tokenType != null) {
            val tokenType: IElementType = lexer.tokenType!!
            val tokenText = lexer.tokenText
            val start = lexer.tokenStart
            val end = lexer.tokenEnd
            
            println("Token $tokenIndex: '$tokenText' -> $tokenType (pos: $start-$end)")
            
            lexer.advance()
            tokenIndex++
        }
        
        println("=== END TOKENIZING ===")
    }
    
    fun debugSingleToken(text: String): Pair<IElementType?, String> {
        val lexer: Lexer = PhelLexerAdapter()
        lexer.start(text)
        
        val tokenType = lexer.tokenType
        val tokenText = lexer.tokenText
        
        println("Single token: '$tokenText' -> $tokenType")
        
        return Pair(tokenType, tokenText)
    }
}

// Example usage (you can call this from anywhere to test):
// PhelTokenDebugger.debugTokens("(defn hello [name] (str \"Hello, \" name))")
// PhelTokenDebugger.debugSingleToken("hello")
