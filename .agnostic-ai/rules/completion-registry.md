---
globs: [ "src/main/kotlin/org/phellang/completion/**", "src/main/kotlin/org/phellang/tools/**" ]
description: Function registry and completion
---

# Registry & Completion

Registry of 350+ functions in `completion/data/`:
- `PhelFunctionRegistry.kt` (loader) · `PhelFunction.kt` (model) · `Namespace.kt` (enum) · `PhelProjectSymbol.kt` · `register*Functions.kt` (generated).
- Covered ns: `phel\core str json html http test base64 debug mock repl`.

## Updating

`./gradlew updatePhelRegistry` → fetch api.json, regen `register*Functions.kt`, auto-sync the `// region GENERATED` blocks of `Namespace.kt` + `PhelFunctionRegistry.kt` from `NamespaceConfig`. Generator: `tools/ApiGeneratorKt`. `KotlinCodeGenerator` writes per-ns files; `RegistryWiringGenerator` rewrites only the GENERATED regions — curated `ALIASES` and helpers untouched.

**Add a namespace**: `NamespaceConfig.kt` is the single source of truth — add ONE entry (`functionName`, `fileName`, optional `subfolder`), run `updatePhelRegistry`. Generated file + enum constant + wiring produced automatically. Enum name derives from `functionName` (`registerPhpInteropFunctions` → `PHP_INTEROP`). `Namespace.ALIASES` stays hand-curated.

## Completion order

parens `()` → templates (def/defn/let, with placeholders) → ns-qualified (`str/split`) → locals (let/params/loop) → PHP interop.
