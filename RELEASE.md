# Release Process

Publishing is automated. A **draft GitHub Release** is kept up to date for the current version on
every green push to `main`; **publishing that release** signs the plugin and uploads it to the
JetBrains Marketplace. You never build or upload the zip by hand.

## One-time setup

The signing/publish workflow ([`release.yml`](.github/workflows/release.yml)) needs four repository
secrets (Settings → Secrets and variables → Actions). They are already configured; this is only for
reference or rotation:

| Secret | Source |
|---|---|
| `PUBLISH_TOKEN` | [Marketplace](https://plugins.jetbrains.com/) → your profile → **Tokens** → Generate |
| `CERTIFICATE_CHAIN`, `PRIVATE_KEY`, `PRIVATE_KEY_PASSWORD` | a [plugin signing certificate](https://plugins.jetbrains.com/docs/intellij/plugin-signing.html) (`openssl`) |

The signing certificate expires (365 days by default) — regenerate and update the two cert secrets
before then.

## Cutting a release

### 1. Update the function registry (if Phel released)

```bash
./gradlew updatePhelRegistry     # Phel stdlib
./gradlew updatePhpRegistry      # native PHP docs (php/doc-en), if refreshing those
```

### 2. Smoke-test

`./gradlew runIde` opens a sandbox IDE with the plugin loaded. Verify the change set works.

### 3. Bump the version and promote the changelog

Set the new `version` in `build.gradle.kts`:

```kotlin
version = "X.Y.Z"
```

Then promote the `## [Unreleased]` entries in `CHANGELOG.md` into a new `## [X.Y.Z] - YYYY-MM-DD`
section (or run `./gradlew patchChangelog`). This section is the single source of the release notes:
the draft release and the Marketplace **What's new** (`patchPluginXml` renders it into
`<change-notes>`) both come from it — so releasing with an empty section ships empty notes.

### 4. Update IDE compatibility (if needed)

Adjust `sinceBuild` / `untilBuild` in `build.gradle.kts` if targeting new IDE versions.

### 5. Merge to `main`

Open a PR with the version/changelog bump and merge it. On the resulting green push to `main`, the
`releaseDraft` job in [`build.yml`](.github/workflows/build.yml) creates or refreshes a **draft**
GitHub Release `vX.Y.Z` with the changelog notes. A draft publishes nothing.

### 6. Publish the release

On the repo's **Releases** page, review the draft `vX.Y.Z` (notes and version), then **Publish**.
That triggers [`release.yml`](.github/workflows/release.yml), which runs `publishPlugin` to sign and
upload to the Marketplace and attaches the distribution zip to the release. Track the run under
**Actions**; the Marketplace listing updates after JetBrains' review.

## Manual fallback

If the automation is unavailable, publish from a terminal with the four secrets exported as
environment variables:

```bash
./gradlew publishPlugin
```
