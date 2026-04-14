# Contributing to Phel IntelliJ Plugin

Thank you for your interest in contributing! This guide will help you get started.

## Getting Started

### Prerequisites

- **Java 21** (Zulu or any OpenJDK distribution)
- **IntelliJ IDEA** (Community or Ultimate) for development
- Familiarity with the [Phel language](https://phel-lang.org/)

### Setup

```bash
git clone git@github.com:phel-lang/intellij-phel-support.git
cd intellij-phel-support
./gradlew buildPlugin
```

Open the project in IntelliJ IDEA and import as a Gradle project.

### Running the Plugin

```bash
./gradlew runIde
```

This launches a sandboxed IDE instance with the plugin loaded.

### Running Tests

```bash
./gradlew test
```

## Development Workflow

1. **Fork** the repository and create a feature branch from `main`.
2. **Make your changes** following the conventions below.
3. **Write tests** for new features or bug fixes.
4. **Run the full build** to ensure nothing is broken:
   ```bash
   ./gradlew clean test verifyPlugin buildPlugin
   ```
5. **Submit a pull request** against `main`.

## Project Conventions

### Code Style

- Kotlin with official code style (`kotlin.code.style=official`)
- Follow [IntelliJ Platform SDK](https://plugins.jetbrains.com/docs/intellij/welcome.html) conventions
- Use `PhelTypes.SYMBOL` (not `PhelTypes.SYM`) in completion logic

### Testing

- **Unit tests** go in `src/test/kotlin/org/phellang/unit/`
- **Integration tests** go in `src/test/kotlin/org/phellang/integration/`
- Integration tests should use IntelliJ's `BasePlatformTestCase` / light fixtures
- JUnit 5 (Jupiter) with `useJUnitPlatform()`

### Lexer & Parser

- Lexer source: `src/main/kotlin/org/phellang/language/Phel.flex`
- Grammar source: `src/main/kotlin/org/phellang/language/parser/Phel.bnf`
- After modifying `.flex` or `.bnf`, regenerate:
  ```bash
  ./gradlew generatePhelLexer generatePhelParser
  ```
- **Never edit** files in `src/main/gen/` — they are auto-generated
- Generated files are committed to the repo

### Plugin Registration

All features (completions, inspections, annotators, etc.) must be registered in `src/main/resources/META-INF/plugin.xml`.

## What to Contribute

- Bug fixes
- New code inspections
- Improved completion / documentation
- Lexer/parser improvements for better Phel coverage
- Test coverage improvements
- Documentation improvements

## Reporting Issues

Please use [GitHub Issues](https://github.com/phel-lang/intellij-phel-support/issues) to report bugs or request features. Include:

- IDE version and OS
- Plugin version
- Steps to reproduce
- Expected vs actual behavior
- Sample `.phel` file if applicable

## Code of Conduct

Be respectful and constructive. We follow the [Contributor Covenant](https://www.contributor-covenant.org/) code of conduct.
