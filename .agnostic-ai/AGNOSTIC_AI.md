# Phel IntelliJ Plugin

IntelliJ IDEA plugin for the [Phel language](https://phel-lang.org/) (Lisp-inspired, compiles to PHP; almost Clojure). Verify syntax against the official docs and https://phel-lang.org/api.json. Marketplace: https://plugins.jetbrains.com/plugin/28459-phel-lang. Repository: https://github.com/phel-lang/phel-intellij-plugin.

## Commands

```bash
./gradlew runIde                # sandbox IDE with the plugin
./gradlew test                  # unit + integration tests
./gradlew build                 # full build
./gradlew generatePhelLexer     # lexer from .flex
./gradlew generatePhelParser    # parser from .bnf
./gradlew updatePhelRegistry    # fetch api.json, regenerate function registry
```

## Layout

Sources are in `src/main/kotlin/org/phellang/` (layering in `.agnostic-ai/rules/plugin-architecture.md`). `src/main/gen/` is the generated lexer/parser, committed; never edit by hand. Tests: `src/test/kotlin/org/phellang/unit/` (JUnit 5 + Mockito) and `integration/` (`BasePlatformTestCase`, light fixtures). Stack: Gradle Kotlin DSL, Grammar-Kit, Java 21, Kotlin 2.1, IDEA 2025.2 (compat 2024.3+, no upper bound).

## Conventions

- After editing `.flex`/`.bnf`, run `./gradlew generatePhelLexer generatePhelParser` (a hook does it for Claude).
- Register every plugin feature in `src/main/resources/META-INF/plugin.xml`.
- Completion matches `PhelTypes.SYM` (lexer token at the caret), not `PhelTypes.SYMBOL`; use `PlainPrefixMatcher` for `namespace/function`.
- `NamespaceConfig.kt` is the registry's single source of truth: add one entry, run `./gradlew updatePhelRegistry`. Hand-edit only the curated `ALIASES` map.
- Test Phel features against the official docs.
- Commits and PRs follow `.agnostic-ai/rules/git-workflow.md`.
