---
description: Use to add a new code inspection to the Phel plugin.
---

Ask for: what to detect, severity (error, warning, info), whether it offers a quick fix.

Then:
1. Read the existing inspections in `src/main/kotlin/org/phellang/inspection/` and follow their patterns.
2. Register the class in `src/main/resources/META-INF/plugin.xml`.
3. Add unit and/or integration tests.
4. Run `./gradlew test`.
