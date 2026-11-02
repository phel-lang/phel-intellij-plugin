---
globs: [ "**/*.flex", "**/*.bnf", "src/main/gen/**" ]
description: Lexer/parser generation
---

# Lexer and parser

- Lexer: `language/Phel.flex` (JFlex). Grammar: `language/parser/Phel.bnf` (Grammar-Kit). Output: `src/main/gen/org/phellang/language/`, committed, never hand-edited.
- After `.flex`/`.bnf` edits run `./gradlew generatePhelLexer generatePhelParser` (the Claude hook does it; compile also depends on it).
- Lexer returns use `PhelTypes.TOKEN_NAME` constants.
