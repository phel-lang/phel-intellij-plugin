# Phel IntelliJ Plugin

IntelliJ IDEA plugin for the [Phel programming language](https://phel-lang.org/) — a functional, Lisp-inspired language that compiles to PHP. Inspired by Clojure, Phel brings macros, persistent data structures, and expressive functional idioms to the PHP ecosystem.

- **Phel is (_almost_) Clojure** — always verify syntax against [official Phel docs](https://phel-lang.org/)
- **API reference**: https://phel-lang.org/api.json
- **Plugin SDK**: https://plugins.jetbrains.com/docs/intellij/welcome.html
- **Marketplace**: https://plugins.jetbrains.com/plugin/28459-phel-lang
- **Repository**: https://github.com/phel-lang/phel-intellij-plugin

> Path-scoped deep dives live under `.agnostic-ai/rules/` (emitted to Claude via globs). This file is the durable policy every target shares — keep it in sync when a rule changes a project-wide convention.

## Quick Commands

```bash
./gradlew runIde                # Launch IDE sandbox with the plugin
./gradlew test                  # Run unit + integration tests
./gradlew build                 # Full build
./gradlew generatePhelLexer     # Regenerate lexer from .flex
./gradlew generatePhelParser    # Regenerate parser from .bnf
./gradlew updatePhelRegistry    # Fetch api.json and regenerate function registry
```

## Project Structure

Plugin sources live under `src/main/kotlin/org/phellang/`:

- `actions/` contextual menu actions · `annotator/` semantic highlighting & form-comment detection · `completion/` completion + docs · `core/` utilities · `registry/` the shared Phel function/symbol model (API at the root, generated `register*Functions.kt` under `data/`, symbol index under `indexing/`) — a leaf package consumed by completion, annotator, inspection, documentation and inlay alike · `debug/` · `documentation/` hover popups · `editor/` smart typing, brace matching, folding · `inspection/` code inspections · `language/` file type, icons, lexer (`.flex`) and `parser/` grammar (`.bnf`) · `syntax/` highlighter & colors · `tools/` `ApiGenerator` for `updatePhelRegistry`.
- `src/main/gen/` — **generated** lexer/parser. Never edit by hand; committed to the repo.
- `src/test/kotlin/org/phellang/` — `unit/` (JUnit 5 + Mockito) and `integration/` (IntelliJ `BasePlatformTestCase` / light fixtures).

## Build & Tooling

- Gradle (Kotlin DSL), IntelliJ Platform Gradle Plugin, Grammar-Kit (JFlex + BNF), Java 21 toolchain, Kotlin 2.1.
- Target IntelliJ IDEA 2025.2 (compat 2024.3 — 2026.2.x). `buildSearchableOptions` disabled.
- Kotlin/Java compile tasks depend on lexer/parser generation automatically.
- After editing `.flex`/`.bnf`, run `./gradlew generatePhelLexer generatePhelParser` (a PostToolUse hook does this for Claude).

## Conventions

- Every plugin feature must be registered in `src/main/resources/META-INF/plugin.xml`.
- In completion logic use `PhelTypes.SYMBOL` (not `SYM`); use `PlainPrefixMatcher` for `namespace/function` matching.
- Registry: `NamespaceConfig.kt` is the single source of truth — add one entry and run `./gradlew updatePhelRegistry`; generated files, the `Namespace` enum, and registry wiring are produced automatically. Hand-edit only the curated `ALIASES` map.
- New unit tests go in `unit/`, integration tests in `integration/`. Test Phel features against the official docs.

## Commits & PRs

- Conventional commits; use `ref:` (not `refactor:`). Never reference an AI assistant in commit messages or trailers.
- PRs: assign to `JesusValeraDev`, add a matching label, and use `Closes #X` to auto-close issues.
