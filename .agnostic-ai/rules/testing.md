---
globs: [ "src/test/**" ]
description: Testing conventions
---

# Testing

- Unit tests: `src/test/kotlin/org/phellang/unit/` (JUnit 5 + Mockito).
- Integration tests: `.../integration/` (IntelliJ `BasePlatformTestCase`, light fixtures).
- Run `./gradlew test`; manual check with `./gradlew runIde`.
- Verify Phel features against https://phel-lang.org/.
