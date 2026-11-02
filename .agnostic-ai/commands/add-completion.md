---
description: Use to add a new completion feature (template, namespace function, PHP interop, local symbol).
---

Ask for: the kind of completion (template, namespace function, PHP interop, local symbol), the trigger text and expansion, any hover documentation.

Then:
1. Read the existing code in `src/main/kotlin/org/phellang/completion/` and follow its patterns.
2. Register it in `plugin.xml` if needed.
3. Add a test.
4. Run `./gradlew test`.
