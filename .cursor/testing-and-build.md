---
description: Testing and build workflow rules for Phel IntelliJ plugin
appliesTo: ["**/*.java", "**/*.kt", "**/*.phel", "build.gradle.kts"]
tags: ["testing", "gradle", "build"]
priority: medium
---

## Testing Strategy

- Create test `.phel` files for manual testing
- Clean up temporary test files after use
- Run `./gradlew runIde` for integration testing
- Verify that plugin behavior complies with official Phel examples (`phel-docs.md`)

## Gradle Tasks

- `./gradlew generatePhelLexer` → regenerate lexer from `.flex` files
- `./gradlew generatePhelParser` → regenerate parser from `.bnf` files
- `./gradlew compileJava compileKotlin` → compile Java and Kotlin sources
- `./gradlew runIde` → launch IDE with plugin for testing
- `./gradlew build` → perform full build (may fail if IDE is running)

> When implementing tests, reference official Phel docs (`phel-docs.md`) for language features and syntax.

## Code Quality

- Follow IntelliJ coding conventions
- Use `@NotNull` and `@Nullable` annotations consistently
- Implement proper error handling
- Add comprehensive JavaDoc comments
- Use meaningful variable and method names
- Focus on practical, working solutions over theory
