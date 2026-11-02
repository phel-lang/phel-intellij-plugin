---
globs: [ "src/main/kotlin/org/phellang/completion/**", "src/main/kotlin/org/phellang/registry/**", "src/main/kotlin/org/phellang/tools/**" ]
description: Function registry and completion
---

# Registry and completion

The registry (900+ functions) lives in `org.phellang.registry`, a leaf package: platform imports only, no other `org.phellang` package (see `plugin-architecture.md`). Many packages consume it, so it is not under `completion/`.

- Loader `PhelFunctionRegistry.kt`, model `PhelFunction.kt`, enum `Namespace.kt`; the project symbol index is in `indexing/`.
- Completion offers every `Namespace` (`PhelRegistryCompletionHelper.addStandardLibraryFunctions` iterates `Namespace.entries`), so a new `NamespaceConfig` entry shows up automatically.

## Updating

`./gradlew updatePhelRegistry` fetches api.json, regenerates `register*Functions.kt`, and syncs the `// region GENERATED` blocks of `Namespace.kt` and `PhelFunctionRegistry.kt` from `NamespaceConfig`. Generator: `tools/ApiGeneratorKt`; `KotlinCodeGenerator` writes per-namespace files; `RegistryWiringGenerator` rewrites only the GENERATED regions (curated `ALIASES` and helpers stay).

Add a namespace: add ONE entry to `NamespaceConfig.kt` (`functionName`, `fileName`, optional `subfolder`) and run `updatePhelRegistry`. The enum name derives from `functionName` (`registerPhpInteropFunctions` -> `PHP_INTEROP`). `Namespace.ALIASES` is hand-curated.

Completion order: parens `()`, templates (def/defn/let, with placeholders), ns-qualified (`str/split`), locals (let/params/loop), PHP interop.
