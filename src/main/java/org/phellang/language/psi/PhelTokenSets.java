package org.phellang.language.psi;

import com.intellij.psi.tree.TokenSet;

public interface PhelTokenSets {
    // Only include token sets for tokens that actually exist in PhelTypes
    TokenSet BOOL = TokenSet.create(PhelTypes.BOOL);
    TokenSet BRACE1 = TokenSet.create(PhelTypes.BRACE1);
    TokenSet BRACE2 = TokenSet.create(PhelTypes.BRACE2);
    TokenSet BRACKET1 = TokenSet.create(PhelTypes.BRACKET1);
    TokenSet BRACKET2 = TokenSet.create(PhelTypes.BRACKET2);
    TokenSet CHAR = TokenSet.create(PhelTypes.CHAR);
    TokenSet COLON = TokenSet.create(PhelTypes.COLON);
    TokenSet COLONCOLON = TokenSet.create(PhelTypes.COLONCOLON);
    TokenSet COMMA = TokenSet.create(PhelTypes.COMMA);
    TokenSet DOT = TokenSet.create(PhelTypes.DOT);
    TokenSet DOTDASH = TokenSet.create(PhelTypes.DOTDASH);
    TokenSet HAT = TokenSet.create(PhelTypes.HAT);
    TokenSet HEXNUM = TokenSet.create(PhelTypes.HEXNUM);
    TokenSet LINE_COMMENT = TokenSet.create(PhelTypes.LINE_COMMENT, PhelTypes.FORM_COMMENT);
    TokenSet NAN = TokenSet.create(PhelTypes.NAN);
    TokenSet NIL = TokenSet.create(PhelTypes.NIL);
    TokenSet NUMBER = TokenSet.create(PhelTypes.NUMBER);
    TokenSet PAREN1 = TokenSet.create(PhelTypes.PAREN1);
    TokenSet PAREN2 = TokenSet.create(PhelTypes.PAREN2);
    TokenSet QUOTE = TokenSet.create(PhelTypes.QUOTE);
    TokenSet SLASH = TokenSet.create(PhelTypes.SLASH);
    TokenSet STRING = TokenSet.create(PhelTypes.STRING);
    TokenSet SYM = TokenSet.create(PhelTypes.SYM);
    TokenSet SYNTAX_QUOTE = TokenSet.create(PhelTypes.SYNTAX_QUOTE);
    TokenSet TILDE = TokenSet.create(PhelTypes.TILDE);
    TokenSet TILDE_AT = TokenSet.create(PhelTypes.TILDE_AT);
}
