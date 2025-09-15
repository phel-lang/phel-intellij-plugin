package org.phellang.language;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;
import org.phellang.language.psi.PhelTypes;

import static com.intellij.psi.TokenType.BAD_CHARACTER;
import static com.intellij.psi.TokenType.WHITE_SPACE;

%%

%public
%class PhelLexer
%implements FlexLexer
%function advance
%type IElementType
%unicode

%state SYMBOL0, SYMBOL1, SYMBOL2, SYMBOL3, MULTILINE_COMMENT

WHITE_SPACE=\s+
LINE_COMMENT=;.*
HASH_LINE_COMMENT=#([^_|].*)?
STR_CHAR=[^\\\"]|\\.|\\\"
STRING=\" {STR_CHAR}* \"
NUMBER=[+-]? [0-9]+ (\.[0-9]*)? ([eE][+-]?[0-9]+)?
HEXNUM=[+-]? "0x" [\da-fA-F_]+ 
BINNUM=[+-]? "0b" [01_]+
OCTNUM=[+-]? "0o" [0-7_]+
CHARACTER=\\([btrnf]|u[0-9a-fA-F]{4}|o[0-7]{3}|backspace|tab|newline|formfeed|return|space|.)
BAD_LITERAL=\" ([^\\\"]|\\.|\\\")*

SYM_START=[[\w<>$%&=*+\-!?_|\\@]--#\d] | ".."
SYM_PART=[.]? {SYM_CHAR} | ".."
SYM_CHAR=[\w<>$%&=*+\-!?_|'#\\@]
SYM_ANY={SYM_CHAR} | [./]

SYM_TAIL={SYM_PART}+ (":" {SYM_PART}+)?
KEYWORD_TAIL={SYM_PART}+ ("/" {SYM_PART}+)? (":" {SYM_PART}+)?

%%
<YYINITIAL> {
  {WHITE_SPACE}          { return WHITE_SPACE; }

  "^"                    { return PhelTypes.HAT; }
  ",@"                   { return PhelTypes.COMMA_AT; }
  "~@"                   { return PhelTypes.TILDE_AT; }
  "#_"                   { return PhelTypes.FORM_COMMENT; }
  "#|"                   { yybegin(MULTILINE_COMMENT); return PhelTypes.MULTILINE_COMMENT; }
  {HASH_LINE_COMMENT}    { return PhelTypes.LINE_COMMENT; }
  {LINE_COMMENT}         { return PhelTypes.LINE_COMMENT; }
  ","                    { return PhelTypes.COMMA; }
  "~"                    { return PhelTypes.TILDE; }
  "("                    { return PhelTypes.PAREN1; }
  ")"                    { return PhelTypes.PAREN2; }
  "["                    { return PhelTypes.BRACKET1; }
  "]"                    { return PhelTypes.BRACKET2; }
  "{"                    { return PhelTypes.BRACE1; }
  "}"                    { return PhelTypes.BRACE2; }
  ","                    { return PhelTypes.COMMA; }
  "'"                    { return PhelTypes.QUOTE; }
  "`"                    { return PhelTypes.SYNTAX_QUOTE; }

  "nil"                  { return PhelTypes.NIL; }
  "NAN"                  { return PhelTypes.NAN; }
  "true" | "false"       { return PhelTypes.BOOL; }

  {STRING}               { return PhelTypes.STRING; }
  {NUMBER}               { return PhelTypes.NUMBER; }
  {HEXNUM}               { return PhelTypes.HEXNUM; }
  {BINNUM}               { return PhelTypes.BINNUM; }
  {OCTNUM}               { return PhelTypes.OCTNUM; }
  {CHARACTER}            { return PhelTypes.CHAR; }
  {BAD_LITERAL}          { return BAD_CHARACTER; }

  "::" {KEYWORD_TAIL}    { return PhelTypes.KEYWORD_TOKEN; }
  "::"                   { yybegin(SYMBOL0); return PhelTypes.COLONCOLON; }
  ":" {KEYWORD_TAIL}     { return PhelTypes.KEYWORD_TOKEN; }
  ":"                    { yybegin(SYMBOL0); return PhelTypes.COLON; }
  ".-"  /  {SYM_CHAR}    { yybegin(SYMBOL0); return PhelTypes.DOTDASH; }
  ".-"                   { return PhelTypes.SYM; }
  "."   /  {SYM_CHAR}    { yybegin(SYMBOL0); return PhelTypes.DOT; }
  "."                    { return PhelTypes.SYM; }
  "/" {SYM_ANY}+         { yybegin(YYINITIAL); return BAD_CHARACTER; }
  "/"                    { return PhelTypes.SYM; }

  {SYM_START}{SYM_TAIL}? { yybegin(SYMBOL1); return PhelTypes.SYM; }
}

<SYMBOL0> {
  {SYM_TAIL}             { yybegin(SYMBOL1); return PhelTypes.SYM; }
  [^]                    { yybegin(YYINITIAL); yypushback(yylength()); }
}

<SYMBOL1> {
  ":"                    { yybegin(YYINITIAL); return BAD_CHARACTER; }
  "/"                    { yybegin(SYMBOL2); return PhelTypes.SLASH; }
  "."                    { yybegin(YYINITIAL); return PhelTypes.DOT; }
  [^]                    { yybegin(YYINITIAL); yypushback(yylength()); }
}

<SYMBOL2> {
  "::"                   { yybegin(YYINITIAL); return PhelTypes.COLONCOLON; }
  ".-"                   { yybegin(YYINITIAL); return PhelTypes.DOTDASH; }
  "!=="                  { yybegin(YYINITIAL); return PhelTypes.NOT_IDENTICAL; }
  "!="                   { yybegin(YYINITIAL); return PhelTypes.NOT_EQUAL; }
  "&&"                   { yybegin(YYINITIAL); return PhelTypes.AND_AND; }
  "||"                   { yybegin(YYINITIAL); return PhelTypes.OR_OR; }
  "<<"                   { yybegin(YYINITIAL); return PhelTypes.SHIFT_LEFT; }
  ">>"                   { yybegin(YYINITIAL); return PhelTypes.SHIFT_RIGHT; }
  "++"                   { yybegin(YYINITIAL); return PhelTypes.INCREMENT; }
  "--"                   { yybegin(YYINITIAL); return PhelTypes.DECREMENT; }
  "^"                    { yybegin(YYINITIAL); return PhelTypes.HAT; }
  "~"                    { yybegin(YYINITIAL); return PhelTypes.TILDE; }
  {SYM_TAIL}             { yybegin(SYMBOL3); return PhelTypes.SYM; }
}

<SYMBOL2, SYMBOL3> {
  ":"                    { yybegin(YYINITIAL); return BAD_CHARACTER; }
  "."                    { yybegin(YYINITIAL); return PhelTypes.DOT; }
  [^]                    { yybegin(YYINITIAL); yypushback(yylength()); }
}

<YYINITIAL, SYMBOL2, SYMBOL3> {
  "/" {SYM_ANY}+         { yybegin(YYINITIAL); return BAD_CHARACTER; }
}

<MULTILINE_COMMENT> {
  "|#"                   { yybegin(YYINITIAL); return PhelTypes.MULTILINE_COMMENT; }
  [^]                    { return PhelTypes.MULTILINE_COMMENT; }
}

[^] { return BAD_CHARACTER; }
