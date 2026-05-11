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

%state REGEX_LITERAL

WHITE_SPACE=[\s,]+
LINE_COMMENT=;.*
STR_CHAR=[^\\\"]|\\.|\\\"
STRING=\" {STR_CHAR}* \"
NUMBER=[+-]? [0-9]+ (\.[0-9]*)? ([eE][+-]?[0-9]+)? [NM]?
HEXNUM=[+-]? "0x" [\da-fA-F_]+ N?
BINNUM=[+-]? "0b" [01_]+ N?
OCTNUM=[+-]? "0o" [0-7_]+ N?
RADIXNUM=[0-9]{1,2}r[0-9a-zA-Z]+
RATIO=[+-]? [0-9]+ "/" [0-9]+
CHARACTER=\\([btrnf]|u[0-9a-fA-F]{4}|o[0-7]{3}|backspace|tab|newline|formfeed|return|space|.)
BAD_LITERAL=\" ([^\\\"]|\\.|\\\")*

// Atoms: first char excludes reader macro chars (' # @ ; ~ ^ `) and whitespace (comma is whitespace now)
// Subsequent chars allow ' and # (for auto-gensym name# and symbol names with ')
ATOM_START=[^\(\)\[\]\{\}',`@;\~\^ \n\r\t\#]
ATOM_CONT=[^\(\)\[\]\{\},`@;\~\^ \n\r\t]
ATOM={ATOM_START}{ATOM_CONT}*

KEYWORD_TAIL={ATOM} ("/" {ATOM})? (":" {ATOM}+)?

// Tagged literal dispatch: #inst, #uuid, #regex, #php, #cpp, etc.
// Must start with a letter so #_ still matches the form-comment rule via first-match-wins.
TAG_NAME=[a-zA-Z] [a-zA-Z0-9_\-]*
TAG="#" {TAG_NAME}

%%
<YYINITIAL> {
  {WHITE_SPACE}          { return WHITE_SPACE; }

  // Dispatch forms: order matters — specific tokens must come before the generic TAG rule
  "#?@("                 { return PhelTypes.READER_COND_SPLICE; }
  "#?("                  { return PhelTypes.READER_COND; }
  "#("                   { return PhelTypes.HASH_PAREN; }
  "#'"                   { return PhelTypes.VAR_QUOTE; }
  "##-Inf"               { return PhelTypes.SYMBOLIC_NUM; }
  "##Inf"                { return PhelTypes.SYMBOLIC_NUM; }
  "##NaN"                { return PhelTypes.SYMBOLIC_NUM; }
  "##" [^\r\n]*          { return BAD_CHARACTER; } // Malformed symbolic number (not ##Inf, ##-Inf, ##NaN)
  "#\""                  { yybegin(REGEX_LITERAL); return PhelTypes.REGEX_START; }
  "#_"                   { return PhelTypes.FORM_COMMENT; }
  "#{"                   { return PhelTypes.HASH_BRACE; }
  {TAG}                  { return PhelTypes.TAG; }

  // Reader macros
  "^"                    { return PhelTypes.HAT; }
  "~@"                   { return PhelTypes.TILDE_AT; }
  "@"                    { return PhelTypes.DEREF; }
  "~"                    { return PhelTypes.TILDE; }
  "'"                    { return PhelTypes.QUOTE; }
  "`"                    { return PhelTypes.SYNTAX_QUOTE; }

  // Delimiters
  "("                    { return PhelTypes.PAREN1; }
  ")"                    { return PhelTypes.PAREN2; }
  "["                    { return PhelTypes.BRACKET1; }
  "]"                    { return PhelTypes.BRACKET2; }
  "{"                    { return PhelTypes.BRACE1; }
  "}"                    { return PhelTypes.BRACE2; }

  // Comments
  {LINE_COMMENT}         { return PhelTypes.LINE_COMMENT; }

  // Literals
  "nil"                  { return PhelTypes.NIL; }
  "NAN"                  { return PhelTypes.NAN; }
  "true" | "false"       { return PhelTypes.BOOL; }

  {STRING}               { return PhelTypes.STRING; }
  {RADIXNUM}             { return PhelTypes.RADIXNUM; }
  {HEXNUM}               { return PhelTypes.HEXNUM; }
  {BINNUM}               { return PhelTypes.BINNUM; }
  {OCTNUM}               { return PhelTypes.OCTNUM; }
  {RATIO}                { return PhelTypes.RATIO; }
  {NUMBER}               { return PhelTypes.NUMBER; }
  {CHARACTER}            { return PhelTypes.CHAR; }
  {BAD_LITERAL}          { return BAD_CHARACTER; }

  // Keywords
  "::" {KEYWORD_TAIL}    { return PhelTypes.KEYWORD_TOKEN; }
  "::"                   { return PhelTypes.COLONCOLON; }
  ":" {KEYWORD_TAIL}     { return PhelTypes.KEYWORD_TOKEN; }
  ":"                    { return PhelTypes.COLON; }

  // Access operators
  ".-"                   { return PhelTypes.DOTDASH; }
  "."                    { return PhelTypes.DOT; }

  // Symbols (catch-all)
  {ATOM}                 { return PhelTypes.SYM; }
}

<REGEX_LITERAL> {
  ([^\\\"\r\n]|\\.)*\"   { yybegin(YYINITIAL); return PhelTypes.REGEX_BODY; }
  ([^\\\"\r\n]|\\.)*     { yybegin(YYINITIAL); return BAD_CHARACTER; } // Unterminated regex on this line
}

[^] { return BAD_CHARACTER; }
