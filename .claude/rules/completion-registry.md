---
globs: [ "src/main/kotlin/org/phellang/completion/**", "src/main/kotlin/org/phellang/tools/**" ]
description: Function registry and code completion system
---

# Function Registry & Completion

## Registry

The plugin ships a registry of 350+ Phel functions. Registry files in `src/main/kotlin/org/phellang/completion/data/`:

- `PhelFunctionRegistry.kt` — central registry loader
- `PhelFunction.kt` — function data model
- `Namespace.kt` — namespace definitions
- `PhelProjectSymbol.kt` — project-level symbol tracking
- `register*Functions.kt` — auto-generated per-namespace files

### Namespaces covered

`phel\core`, `phel\str`, `phel\json`, `phel\html`, `phel\http`, `phel\test`, `phel\base64`, `phel\debug`, `phel\mock`,
`phel\repl`

## Updating the Registry

Run `./gradlew updatePhelRegistry` to:

1. Fetch `https://phel-lang.org/api.json`
2. Regenerate all `register*Functions.kt` files
3. Auto-sync the wired regions of `Namespace.kt` (enum constants) and `PhelFunctionRegistry.kt` (imports + `init` block)
   from `NamespaceConfig`
4. The generator lives in `src/main/kotlin/org/phellang/tools/ApiGeneratorKt`

`KotlinCodeGenerator` writes the per-namespace files; `RegistryWiringGenerator` rewrites only the
`// region GENERATED ...` blocks — the curated `ALIASES` map and registry helper logic are left untouched.

### Adding a namespace

`NamespaceConfig.kt` is the single source of truth. To support a new namespace, add ONE entry there (`functionName`,
`fileName`, optional `subfolder`) and run `./gradlew updatePhelRegistry`. The generated file, the `Namespace` enum
constant, and the registry wiring are all produced automatically — no hand-editing, no compile-time bootstrap deadlock.
Enum names derive from `functionName` (e.g. `registerPhpInteropFunctions` → `PHP_INTEROP`). Short-name aliases in
`Namespace.ALIASES` remain hand-curated and optional.

## Completion Priorities

- `()` parentheses completion shown first
- Template completions (def, defn, let, etc.) with placeholder selection
- Namespace-qualified functions (e.g., `str/split`)
- Local symbols from let bindings, function parameters, loop variables
- PHP interop functions and constants
