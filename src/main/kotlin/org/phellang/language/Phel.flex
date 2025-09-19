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

%state MULTILINE_COMMENT

WHITE_SPACE=\s+
LINE_COMMENT=;.*
HASH_LINE_COMMENT=#([^_|{].*)?
STR_CHAR=[^\\\"]|\\.|\\\"
STRING=\" {STR_CHAR}* \"
NUMBER=[+-]? [0-9]+ (\.[0-9]*)? ([eE][+-]?[0-9]+)?
HEXNUM=[+-]? "0x" [\da-fA-F_]+ 
BINNUM=[+-]? "0b" [01_]+
OCTNUM=[+-]? "0o" [0-7_]+
CHARACTER=\\([btrnf]|u[0-9a-fA-F]{4}|o[0-7]{3}|backspace|tab|newline|formfeed|return|space|.)
BAD_LITERAL=\" ([^\\\"]|\\.|\\\")*

ATOM=[^\(\)\[\]\{\}',`@ \n\r\t\#]+

KEYWORD_TAIL={ATOM} ("/" {ATOM})? (":" {ATOM}+)?

%%
<YYINITIAL> {
  {WHITE_SPACE}          { return WHITE_SPACE; }

  "|("                   { return PhelTypes.FN_SHORT; }
  "^"                    { return PhelTypes.HAT; }
  ",@"                   { return PhelTypes.COMMA_AT; }
  "~@"                   { return PhelTypes.TILDE_AT; }
  "#_"                   { return PhelTypes.FORM_COMMENT; }
  "#{"                   { return PhelTypes.HASH_BRACE; }
  "|"                    { return PhelTypes.SYM; }
  "#|"                   { yybegin(MULTILINE_COMMENT); return PhelTypes.MULTILINE_COMMENT; }
  {LINE_COMMENT}         { return PhelTypes.LINE_COMMENT; }
  {HASH_LINE_COMMENT}    { return PhelTypes.LINE_COMMENT; }
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
  "::"                   { return PhelTypes.COLONCOLON; }
  ":" {KEYWORD_TAIL}     { return PhelTypes.KEYWORD_TOKEN; }
  ":"                    { return PhelTypes.COLON; }
  ".-"                   { return PhelTypes.DOTDASH; }
  "."                    { return PhelTypes.DOT; }
  "/"                    { return PhelTypes.SLASH; }

  {ATOM}                 { return PhelTypes.SYM; }
}

<MULTILINE_COMMENT> {
  "|#"                   { yybegin(YYINITIAL); return PhelTypes.MULTILINE_COMMENT; }
  [^]                    { return PhelTypes.MULTILINE_COMMENT; }
}

[^] { return BAD_CHARACTER; }
