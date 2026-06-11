Regenerate the lexer and parser from the grammar files after changes to `.flex` or `.bnf`.

Steps:
1. Run `./gradlew generatePhelLexer generatePhelParser`
2. Check git diff on `src/main/gen/` to review generated changes
3. Run `./gradlew compileKotlin` to verify compilation succeeds
4. Run `./gradlew test` to verify tests still pass
5. Summarize what changed in the generated code
