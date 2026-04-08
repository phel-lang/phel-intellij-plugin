---
globs: ["build.gradle.kts", "settings.gradle.kts", "gradle/**", "gradlew*"]
description: Build system configuration and Gradle tasks
---

# Build System

- Gradle with Kotlin DSL (`build.gradle.kts`)
- IntelliJ Platform Gradle Plugin 2.13.1
- Grammar-Kit for parser/lexer generation (JFlex + BNF)
- Java 21 toolchain, Kotlin 2.1 language level
- Target platform: IntelliJ IDEA 2025.1 (compatibility: 2024.2 — 2026.1.x)
- Dependencies include Gson for JSON parsing (used by API generator)

## Gradle Tasks

- `./gradlew runIde` — launch sandboxed IDE with plugin loaded
- `./gradlew test` — run JUnit 5 tests (unit + integration)
- `./gradlew build` — full build (may conflict if runIde is active)
- `./gradlew buildPlugin` — build distributable plugin zip
- `./gradlew generatePhelLexer` — regenerate lexer from `.flex`
- `./gradlew generatePhelParser` — regenerate parser from `.bnf`
- `./gradlew updatePhelRegistry` — fetch api.json from phel-lang.org and regenerate register*Functions.kt

## Build Notes

- Java/Kotlin compile tasks depend on lexer/parser generation automatically
- `buildSearchableOptions` is disabled
- Plugin verification runs against IC 2024.2.5, 2024.3.1, and 2025.1
- Signing and publishing use environment variables: `CERTIFICATE_CHAIN`, `PRIVATE_KEY`, `PRIVATE_KEY_PASSWORD`, `PUBLISH_TOKEN`
