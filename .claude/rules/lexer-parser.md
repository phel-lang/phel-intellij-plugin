---
globs: ["**/*.flex", "**/*.bnf", "src/main/gen/**"]
description: Lexer and parser generation rules
---

# Lexer & Parser

- **Lexer**: `src/main/kotlin/org/phellang/language/Phel.flex` (JFlex)
- **Grammar**: `src/main/kotlin/org/phellang/language/parser/Phel.bnf` (Grammar-Kit)
- **Generated output**: `src/main/gen/org/phellang/language/`

## Rules

- Always regenerate after `.flex` or `.bnf` changes: `./gradlew generatePhelLexer generatePhelParser`
- Never edit files in `src/main/gen/` manually — they are overwritten on generation
- Generated files are committed to the repo
- Always use `PhelTypes.TOKEN_NAME` constants in lexer returns
- Java/Kotlin compilation depends on generation tasks automatically
