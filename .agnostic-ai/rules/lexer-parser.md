---
globs: ["**/*.flex", "**/*.bnf", "src/main/gen/**"]
description: Lexer/parser generation
---

# Lexer & Parser

- Lexer: `language/Phel.flex` (JFlex). Grammar: `language/parser/Phel.bnf` (Grammar-Kit). Output: `src/main/gen/org/phellang/language/`.
- Never hand-edit `src/main/gen/` — overwritten on generation; committed to repo.
- After `.flex`/`.bnf` edits run `./gradlew generatePhelLexer generatePhelParser` (Claude hook does this automatically).
- Lexer returns must use `PhelTypes.TOKEN_NAME` constants.
- Compile auto-depends on generation.
