---
globs: ["src/test/**"]
description: Testing conventions
---

# Testing

- Unit → `src/test/kotlin/org/phellang/unit/` (JUnit 5 + Mockito).
- Integration → `.../integration/` using IntelliJ `BasePlatformTestCase` / light fixtures.
- Run: `./gradlew test`. Manual: `./gradlew runIde`.
- Verify Phel features against https://phel-lang.org/.
