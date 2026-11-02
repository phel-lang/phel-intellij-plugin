---
description: Use to bump the plugin for a new Phel upstream release (registry, version, build check).
argument-hint: "<new-version>"
---

Usage: `/bump-phel-version <new-version>` (for example `0.3.7`).

1. Confirm the version is newer than the `version` property in `build.gradle.kts`.
2. Run `./gradlew updatePhelRegistry` (fetches `https://phel-lang.org/api.json`, regenerates `src/main/kotlin/org/phellang/registry/data/register*Functions.kt`). Report each `Unknown namespace` warning: it needs an entry in `NamespaceConfig` (enum and wiring regenerate) plus hand-curated entries in `PhelProjectNamespaceFinder.STANDARD_LIBRARY_SHORT_TO_FULL` and `PhelVendorUtils.NAMESPACE_TO_FILE`.
3. Check `git status` on `src/main/kotlin/org/phellang/registry/`: only generated files may change.
4. Set the new version in `build.gradle.kts`.
5. If `CHANGELOG.md` exists, add an entry under `## Unreleased` or a new version section.
6. Run `./gradlew compileKotlin test`, then `./gradlew buildPlugin`.
7. Summarize: version, registry diff size, new namespaces, build status.

Stop at the first failing step.
