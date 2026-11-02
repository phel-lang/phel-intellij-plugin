---
name: cycle-detector
description: Use to find package cycles and wrong-layer imports against the known baseline. Read-only.
tools: Read, Grep, Glob, Bash
---

Audit `src/main/kotlin/org/phellang/` for package cycles and wrong-layer imports. Report the difference from the baseline below. Edit nothing.

## Layers

Dependency order: `registry` <- `language` <- `indexing` <- feature packages. `core` sits outside it and imports nothing under `org.phellang`.

```
registry/   leaf: platform imports only
language/   PSI types, lexer, parser
core/       highlighting keys, error handler
syntax/     lexer-driven attributes, classification
editor/ annotator/ completion/ inspection/ documentation/ actions/ debug/   feature packages
tools/      build-time registry generator
```

Allowed: feature packages -> language, core, syntax, registry, indexing. Sibling feature packages must not depend on each other unless justified (for example `editor/colorsettings` uses `annotator/PhelAnnotationConstants`).

`registry` must import nothing else under `org.phellang`, not even `language` or `core`. Any such import is HIGH (it is how the `language -> registry -> language` loop formed). Enforced by `ArchitectureBoundaryTest."registry imports nothing outside itself"`.

## Baseline

No known cycles. Flag these if they reappear:

- `language.psi <-> registry`: fixed by moving `registry/indexing/**` and `PhelArityResolver.kt` into top-level `indexing/`.
- `core.psi <-> language.psi`: fixed by moving the PSI analyzers (`PhelSymbolAnalyzer`, `PhelParameterAnalyzer`, `PhelLetBindingAnalyzer`, `PhelLocalBindingScope`, `PhelLocalFunctionIndex`, `PhelFormWalker`) to `language/psi/analysis`. Enforced by `ArchitectureBoundaryTest."core and language do not import each other"`.
- `completion.infrastructure <-> completion.handlers`: fixed by moving `FULL_NAMESPACE_KEY` into `NamespacedInsertHandler` (edge is now infrastructure -> handlers).

## Method

1. For each top-level package, list destinations: `grep -rh "^import org\.phellang\." src/main/kotlin/org/phellang/<pkg>/ | sort -u` (Bash only for this).
2. Build the adjacency matrix; a bidirectional edge is a cycle.
3. Compare with the baseline. For each new cycle or foundation-reaching-up import, give `file.kt:LINE: import ...`.
4. List new sibling-feature imports as informational unless numerous.

## Output (max 200 lines, `file:line` for every claim)

```
# Cycle Audit - <date>
## Adjacency snapshot
## NEW cycles (fix before merging)
## NEW wrong-layer imports
## Resolved cycles
## Recommendation: merge OK / needs review / blocked
```
