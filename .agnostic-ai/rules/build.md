---
globs: [ "build.gradle.kts", "settings.gradle.kts", "gradle/**", "gradlew*" ]
description: Build system and Gradle tasks
---

# Build

Do not run `build` while `runIde` is up. `buildPlugin` makes the distributable zip. The verifier runs against IC 2024.3, 2025.1, 2025.2, 2025.2.6 and IU 2026.2.4, 263 (2026.3 RC). Publish env: `CERTIFICATE_CHAIN`, `PRIVATE_KEY`, `PRIVATE_KEY_PASSWORD`, `PUBLISH_TOKEN`.
