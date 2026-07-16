---
globs: [ "src/main/kotlin/org/phellang/completion/**", "src/main/kotlin/org/phellang/registry/**", "src/main/kotlin/org/phellang/tools/**" ]
description: Function registry and completion
---

# Registry & Completion

Registry of 900+ functions in `registry/` (`org.phellang.registry`) — a **leaf** package: it may
import `language/psi` and the platform, never a feature package. It is not a completion concern
(annotator, inspection, documentation, inlay and core all consume it), which is why it does not
live under `completion/`.
- `PhelFunctionRegistry.kt` (loader) · `PhelFunction.kt` (model) · `Namespace.kt` (enum) · `PhelProjectSymbol.kt` · `PhelCompletionPriority.kt` · `register*Functions.kt` (generated) · `indexing/` (project symbol index).
- Every `Namespace` the registry knows is offered in completion (`PhelRegistryCompletionHelper.addStandardLibraryFunctions` iterates `Namespace.entries`), so a namespace added to `NamespaceConfig` is surfaced automatically.

## Updating

`./gradlew updatePhelRegistry` → fetch api.json, regen `register*Functions.kt`, auto-sync the `// region GENERATED` blocks of `Namespace.kt` + `PhelFunctionRegistry.kt` from `NamespaceConfig`. Generator: `tools/ApiGeneratorKt`. `KotlinCodeGenerator` writes per-ns files; `RegistryWiringGenerator` rewrites only the GENERATED regions — curated `ALIASES` and helpers untouched.

**Add a namespace**: `NamespaceConfig.kt` is the single source of truth — add ONE entry (`functionName`, `fileName`, optional `subfolder`), run `updatePhelRegistry`. Generated file + enum constant + wiring produced automatically. Enum name derives from `functionName` (`registerPhpInteropFunctions` → `PHP_INTEROP`). `Namespace.ALIASES` stays hand-curated.

## Completion order

parens `()` → templates (def/defn/let, with placeholders) → ns-qualified (`str/split`) → locals (let/params/loop) → PHP interop.
