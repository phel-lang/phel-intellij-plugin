---
globs: ["src/test/**"]
description: Testing strategy and conventions
---

# Testing

## Structure

- `src/test/kotlin/org/phellang/unit/` — unit tests
- `src/test/kotlin/org/phellang/integration/` — integration tests using IntelliJ test framework

## Stack

- JUnit 5 (Jupiter) with `useJUnitPlatform()`
- Mockito for mocking
- IntelliJ's `BasePlatformTestCase` / light fixtures for integration tests

## Running

- `./gradlew test` — run all tests
- Manual testing: `./gradlew runIde` launches a sandboxed IDE instance

## Conventions

- Place new unit tests in the `unit/` package, integration tests in `integration/`
- Integration tests should use IntelliJ's test infrastructure (light project fixtures)
- Test Phel language features against official documentation at https://phel-lang.org/
