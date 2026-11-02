---
description: Use after editing a `.flex` or `.bnf` file to regenerate the lexer and parser and verify.
---

1. `./gradlew generatePhelLexer generatePhelParser`
2. Review `git diff` on `src/main/gen/`.
3. `./gradlew compileKotlin`, then `./gradlew test`.
4. Summarize what changed in the generated code.
