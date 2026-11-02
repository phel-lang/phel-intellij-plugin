---
description: Use to fetch the latest Phel API and regenerate the function registry.
---

1. `./gradlew updatePhelRegistry` (fetches https://phel-lang.org/api.json).
2. Review `git diff` on `src/main/kotlin/org/phellang/registry/data/`.
3. `./gradlew test`.
4. Summarize functions added, removed, updated.
