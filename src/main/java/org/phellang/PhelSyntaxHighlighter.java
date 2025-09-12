package org.phellang;

import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.HighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;
import org.phellang.language.PhelLexerAdapter;
import org.phellang.language.psi.PhelTypes;
import org.phellang.language.psi.PhelTokenType;

import static com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey;

/**
 * Comprehensive syntax highlighter for Phel language
 * Supports 15+ distinct token types for rich visual experience
 */
public class PhelSyntaxHighlighter extends SyntaxHighlighterBase {

    // Core language elements
    public static final TextAttributesKey COMMENT =
            createTextAttributesKey("PHEL_COMMENT", DefaultLanguageHighlighterColors.LINE_COMMENT);
    
    public static final TextAttributesKey STRING =
            createTextAttributesKey("PHEL_STRING", DefaultLanguageHighlighterColors.STRING);
    
    public static final TextAttributesKey NUMBER =
            createTextAttributesKey("PHEL_NUMBER", DefaultLanguageHighlighterColors.NUMBER);
    
    public static final TextAttributesKey KEYWORD =
            createTextAttributesKey("PHEL_KEYWORD", DefaultLanguageHighlighterColors.KEYWORD);
    
    public static final TextAttributesKey BOOLEAN =
            createTextAttributesKey("PHEL_BOOLEAN", DefaultLanguageHighlighterColors.KEYWORD);

    // Literals and constants
    public static final TextAttributesKey NIL_LITERAL =
            createTextAttributesKey("PHEL_NIL", DefaultLanguageHighlighterColors.CONSTANT);
    
    public static final TextAttributesKey NAN_LITERAL =
            createTextAttributesKey("PHEL_NAN", DefaultLanguageHighlighterColors.CONSTANT);
    
    public static final TextAttributesKey CHARACTER =
            createTextAttributesKey("PHEL_CHARACTER", DefaultLanguageHighlighterColors.STRING);

    // Brackets and delimiters
    public static final TextAttributesKey PARENTHESES =
            createTextAttributesKey("PHEL_PARENTHESES", DefaultLanguageHighlighterColors.PARENTHESES);
    
    public static final TextAttributesKey BRACKETS =
            createTextAttributesKey("PHEL_BRACKETS", DefaultLanguageHighlighterColors.BRACKETS);
    
    public static final TextAttributesKey BRACES =
            createTextAttributesKey("PHEL_BRACES", DefaultLanguageHighlighterColors.BRACES);

    // Macro and quote syntax
    public static final TextAttributesKey QUOTE =
            createTextAttributesKey("PHEL_QUOTE", DefaultLanguageHighlighterColors.METADATA);
    
    public static final TextAttributesKey SYNTAX_QUOTE =
            createTextAttributesKey("PHEL_SYNTAX_QUOTE", DefaultLanguageHighlighterColors.METADATA);
    
    public static final TextAttributesKey UNQUOTE =
            createTextAttributesKey("PHEL_UNQUOTE", DefaultLanguageHighlighterColors.METADATA);
    
    public static final TextAttributesKey UNQUOTE_SPLICING =
            createTextAttributesKey("PHEL_UNQUOTE_SPLICING", DefaultLanguageHighlighterColors.METADATA);

    // Symbols and identifiers  
    public static final TextAttributesKey SYMBOL =
            createTextAttributesKey("PHEL_SYMBOL", DefaultLanguageHighlighterColors.IDENTIFIER);
    
    public static final TextAttributesKey KEYWORD_IDENTIFIER =
            createTextAttributesKey("PHEL_KEYWORD_ID", DefaultLanguageHighlighterColors.INSTANCE_FIELD);

    // Special operators
    public static final TextAttributesKey METADATA =
            createTextAttributesKey("PHEL_METADATA", DefaultLanguageHighlighterColors.METADATA);
    
    public static final TextAttributesKey DOT_OPERATOR =
            createTextAttributesKey("PHEL_DOT", DefaultLanguageHighlighterColors.DOT);
    
    public static final TextAttributesKey COMMA =
            createTextAttributesKey("PHEL_COMMA", DefaultLanguageHighlighterColors.COMMA);

    // Error highlighting
    public static final TextAttributesKey BAD_CHARACTER =
            createTextAttributesKey("PHEL_BAD_CHARACTER", HighlighterColors.BAD_CHARACTER);

    private static final TextAttributesKey[] COMMENT_KEYS = new TextAttributesKey[]{COMMENT};
    private static final TextAttributesKey[] STRING_KEYS = new TextAttributesKey[]{STRING};
    private static final TextAttributesKey[] NUMBER_KEYS = new TextAttributesKey[]{NUMBER};
    private static final TextAttributesKey[] BOOLEAN_KEYS = new TextAttributesKey[]{BOOLEAN};
    private static final TextAttributesKey[] NIL_KEYS = new TextAttributesKey[]{NIL_LITERAL};
    private static final TextAttributesKey[] NAN_KEYS = new TextAttributesKey[]{NAN_LITERAL};
    private static final TextAttributesKey[] CHARACTER_KEYS = new TextAttributesKey[]{CHARACTER};
    private static final TextAttributesKey[] PARENTHESES_KEYS = new TextAttributesKey[]{PARENTHESES};
    private static final TextAttributesKey[] BRACKETS_KEYS = new TextAttributesKey[]{BRACKETS};
    private static final TextAttributesKey[] BRACES_KEYS = new TextAttributesKey[]{BRACES};
    private static final TextAttributesKey[] QUOTE_KEYS = new TextAttributesKey[]{QUOTE};
    private static final TextAttributesKey[] SYNTAX_QUOTE_KEYS = new TextAttributesKey[]{SYNTAX_QUOTE};
    private static final TextAttributesKey[] UNQUOTE_KEYS = new TextAttributesKey[]{UNQUOTE};
    private static final TextAttributesKey[] UNQUOTE_SPLICING_KEYS = new TextAttributesKey[]{UNQUOTE_SPLICING};
    private static final TextAttributesKey[] SYMBOL_KEYS = new TextAttributesKey[]{SYMBOL};
    private static final TextAttributesKey[] KEYWORD_KEYS = new TextAttributesKey[]{KEYWORD};
    private static final TextAttributesKey[] METADATA_KEYS = new TextAttributesKey[]{METADATA};
    private static final TextAttributesKey[] DOT_KEYS = new TextAttributesKey[]{DOT_OPERATOR};
    private static final TextAttributesKey[] COMMA_KEYS = new TextAttributesKey[]{COMMA};
    private static final TextAttributesKey[] BAD_CHARACTER_KEYS = new TextAttributesKey[]{BAD_CHARACTER};
    private static final TextAttributesKey[] EMPTY_KEYS = new TextAttributesKey[0];

    @NotNull
    @Override
    public Lexer getHighlightingLexer() {
        return new PhelLexerAdapter();
    }

    @NotNull
    @Override
    public TextAttributesKey[] getTokenHighlights(IElementType tokenType) {
        String tokenName = tokenType.toString();
        
        // Comments
        if (tokenType.equals(PhelTypes.LINE_COMMENT) || tokenType.equals(PhelTypes.FORM_COMMENT)) {
            return COMMENT_KEYS;
        }
        
        // Literals
        if (tokenType.equals(PhelTypes.STRING)) {
            return STRING_KEYS;
        }
        if (tokenType.equals(PhelTypes.NUMBER) || tokenType.equals(PhelTypes.HEXNUM) || 
            tokenType.equals(PhelTypes.BINNUM) || tokenType.equals(PhelTypes.OCTNUM)) {
            return NUMBER_KEYS;
        }
        if (tokenType.equals(PhelTypes.BOOL)) {
            return BOOLEAN_KEYS;
        }
        if (tokenType.equals(PhelTypes.NIL)) {
            return NIL_KEYS;
        }
        if (tokenType.equals(PhelTypes.NAN)) {
            return NAN_KEYS;
        }
        if (tokenType.equals(PhelTypes.CHAR)) {
            return CHARACTER_KEYS;
        }
        
        // Delimiters
        if (tokenType.equals(PhelTypes.PAREN1) || tokenType.equals(PhelTypes.PAREN2)) {
            return PARENTHESES_KEYS;
        }
        if (tokenType.equals(PhelTypes.BRACKET1) || tokenType.equals(PhelTypes.BRACKET2)) {
            return BRACKETS_KEYS;
        }
        if (tokenType.equals(PhelTypes.BRACE1) || tokenType.equals(PhelTypes.BRACE2)) {
            return BRACES_KEYS;
        }
        
        // Quote and macro syntax
        if (tokenType.equals(PhelTypes.QUOTE)) {
            return QUOTE_KEYS;
        }
        if (tokenType.equals(PhelTypes.SYNTAX_QUOTE)) {
            return SYNTAX_QUOTE_KEYS;
        }
        if (tokenType.equals(PhelTypes.TILDE) || tokenType.equals(PhelTypes.COMMA)) {
            return UNQUOTE_KEYS;
        }
        if (tokenType.equals(PhelTypes.TILDE_AT) || tokenType.equals(PhelTypes.COMMA_AT)) {
            return UNQUOTE_SPLICING_KEYS;
        }
        
        // Keywords - complete keyword elements (:something, ::something)
        if (tokenType.equals(PhelTypes.KEYWORD) || tokenType.equals(PhelTypes.KEYWORD_TOKEN)) {
            return KEYWORD_KEYS;
        }
        
        // Individual colon tokens (for partial keywords during typing)
        if (tokenType.equals(PhelTypes.COLON) || tokenType.equals(PhelTypes.COLONCOLON)) {
            return KEYWORD_KEYS;
        }
        
        // Multi-character PHP operators
        if (tokenType.equals(PhelTypes.AND_AND) || tokenType.equals(PhelTypes.OR_OR) ||
            tokenType.equals(PhelTypes.SHIFT_LEFT) || tokenType.equals(PhelTypes.SHIFT_RIGHT) ||
            tokenType.equals(PhelTypes.NOT_EQUAL) || tokenType.equals(PhelTypes.NOT_IDENTICAL) ||
            tokenType.equals(PhelTypes.INCREMENT) || tokenType.equals(PhelTypes.DECREMENT)) {
            return SYMBOL_KEYS;
        }
        
        // Metadata
        if (tokenType.equals(PhelTypes.HAT)) {
            return METADATA_KEYS;
        }
        
        // Operators
        if (tokenType.equals(PhelTypes.DOT) || tokenType.equals(PhelTypes.DOTDASH)) {
            return DOT_KEYS;
        }
        if (tokenType.equals(PhelTypes.COMMA)) {
            return COMMA_KEYS;
        }
        
        // Symbols (everything else that's valid)
        if (tokenType.equals(PhelTypes.SYM)) {
            return SYMBOL_KEYS;
        }
        
        // Bad characters (handled by token name)
        if ("BAD_CHARACTER".equals(tokenName) || tokenType.equals(TokenType.BAD_CHARACTER)) {
            return BAD_CHARACTER_KEYS;
        }
        
        return EMPTY_KEYS;
    }
}
