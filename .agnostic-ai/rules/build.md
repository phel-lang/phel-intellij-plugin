---
globs: ["build.gradle.kts", "settings.gradle.kts", "gradle/**", "gradlew*"]
description: Build system and Gradle tasks
---

# Build

Gradle (Kotlin DSL) · IntelliJ Platform Gradle Plugin · Grammar-Kit (JFlex+BNF) · Java 21 · Kotlin 2.1.
Target IDEA 2025.2 (compat 2024.2 — 2026.2.x). Gson powers the API generator.

Tasks:
- `runIde` — sandbox IDE (don't run `build` concurrently)
- `test` — JUnit 5 unit + integration
- `build` / `buildPlugin` — full build / distributable zip
- `generatePhelLexer` / `generatePhelParser` — regen from `.flex` / `.bnf`
- `updatePhelRegistry` — fetch api.json, regen `register*Functions.kt`

Notes:
- Compile auto-depends on lexer/parser generation. `buildSearchableOptions` disabled.
- Verifier runs vs IC 2024.2, 2024.3, 2025.1, 2025.2, 2025.2.6.
- Publish env: `CERTIFICATE_CHAIN`, `PRIVATE_KEY`, `PRIVATE_KEY_PASSWORD`, `PUBLISH_TOKEN`.
