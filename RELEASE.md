# Release Process

This document describes how to publish a new version of the Phel IntelliJ Plugin.

## Prerequisites

- Push access to the `main` branch
- Access to the [JetBrains Marketplace](https://plugins.jetbrains.com/) plugin upload UI

## Release Checklist

### 1. Update the function registry

If Phel has a new release, update the built-in function registry first.

In IntelliJ, run the **Update Registry Repository** task (or from terminal):

```bash
./gradlew updatePhelRegistry
```

### 2. Smoke-test the plugin

Run the **Run Plugin** task in IntelliJ (or `./gradlew runIde`). This opens a sandboxed IDE instance with the plugin loaded. Verify everything works as expected.

### 3. Bump the version

Update `version` on line 5 of `build.gradle.kts`:

```kotlin
version = "X.Y.Z"
```

### 4. Update IDE compatibility (if needed)

Update `sinceBuild` / `untilBuild` in `build.gradle.kts` if targeting new IDE versions.

### 5. Build the distribution

Run the **Build Distribution** task in IntelliJ (or from terminal):

```bash
./gradlew buildPlugin
```

This generates the plugin zip at:

```
build/distributions/phelplugin-X.Y.Z.zip
```

### 6. Publish to JetBrains Marketplace

1. Open the [Marketplace plugin page](https://plugins.jetbrains.com/plugin/28459-phel-lang) in your browser.
2. Go to the plugin upload modal.
3. Drag and drop the `phelplugin-X.Y.Z.zip` file from `build/distributions/`.
4. Submit and wait for approval.

Alternatively, with the `PUBLISH_TOKEN` (and `CERTIFICATE_CHAIN`, `PRIVATE_KEY`,
`PRIVATE_KEY_PASSWORD` for signing) environment variables set, publish from the terminal:

```bash
./gradlew publishPlugin
```

### 7. Commit and push

```bash
git add build.gradle.kts src/main/kotlin/org/phellang/registry/data
git commit -m "chore: bump plugin to X.Y.Z and update registry"
git push origin main
```
