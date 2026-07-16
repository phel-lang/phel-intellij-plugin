Bump the plugin to track a new Phel upstream release. Regenerates the API registry, updates the plugin version, and verifies the build.

Usage: `/bump-phel-version <new-version>` (e.g. `/bump-phel-version 0.3.7`)

Steps:
1. Confirm the requested version is newer than the current version in `build.gradle.kts` (the `version` property near the top).
2. Run `./gradlew updatePhelRegistry` to fetch the latest `https://phel-lang.org/api.json` and regenerate `src/main/kotlin/org/phellang/registry/data/register*Functions.kt` (and subfolder files). Report any `Unknown namespace` warnings — those signal new upstream namespaces that need an entry in `NamespaceConfig` (the `Namespace` enum and registry wiring regenerate automatically) plus hand-curated entries in `PhelProjectNamespaceFinder.STANDARD_LIBRARY_SHORT_TO_FULL` and `PhelVendorUtils.NAMESPACE_TO_FILE`.
3. Review the `git status` on `src/main/kotlin/org/phellang/registry/` — diff should only touch generated files.
4. Update the plugin version in `build.gradle.kts` to the requested value.
5. If the `CHANGELOG.md` file exists, add a new entry under `## Unreleased` or a new section for this version.
6. Run `./gradlew compileKotlin test` as a verification gate.
7. Run `./gradlew buildPlugin` to confirm the plugin packages.
8. Summarize: version bumped, registry diff size, any new namespaces flagged, build status.

Halt if any step fails and surface the failure; do not continue past a red build.
