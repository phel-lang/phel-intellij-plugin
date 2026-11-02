---
globs: [ "src/main/kotlin/**/*.kt", "src/main/java/**/*.java", "src/main/resources/META-INF/plugin.xml" ]
description: Plugin architecture and source layout
---

# Architecture

## Module rule

A module's entry points live at its root; its internals live in subfolders. For a feature package the entry point is whatever `plugin.xml` instantiates.

Example: `actions/CreatePhelFileAction.kt` is the extension point; `actions/namespace/` and `actions/template/` are collaborators not reachable from `plugin.xml`.

| Shape           | Root contains                           | Examples                                                                                               |
|-----------------|-----------------------------------------|--------------------------------------------------------------------------------------------------------|
| Feature package | only classes registered in `plugin.xml` | `actions/` `annotator/` `completion/` `documentation/` `inlay/` `inspection/` `refactoring/` `syntax/` |
| Namespace       | nothing (a category, not a module)      | `editor/` `language/` `core/`                                                                          |
| Library / tool  | its public API, or its `main()`         | `registry/` `tools/`                                                                                   |

`editor/` has no root class and never gets one: folding, typing, quoting, brace matching, formatting and the structure view are separate modules (`editor/folding/PhelFoldingBuilder.kt`). Add a module folder, not a root class.

New extension point: put the class at the root of the module that owns it and its helpers in subfolders. New concern: new module folder.

## Layout

Packages under `src/main/kotlin/org/phellang/`: `actions` `annotator` `completion` `core` (+ `highlighting/`, the `TextAttributesKey` vocabulary shared by `syntax`, `annotator` and color settings; its external IDs persist in user color schemes, so they are contract) `registry` (function/symbol model, API at root, generated `register*Functions.kt` under `data/`) `indexing` (project symbol index, `PhelArityResolver`; PSI-aware, above `registry` and `language`) `documentation` `editor` `inlay` `inspection` (+ `quickfixes/`, `deprecated/`) `language` (`parser/`, `psi/`) `refactoring` `syntax` `tools`. 
## Enforced boundaries

`ArchitectureBoundaryTest` (a source scan) fails the build on a cross-package import that breaks these. To relax one, change the test deliberately.

- `registry/data/**` (generated) is imported only inside `registry`; others use `PhelFunctionRegistry` (stdlib) or `indexing.PhelArityResolver` (stdlib + project).
- `registry` imports nothing under `org.phellang` but itself (not `language`, not `core`). PSI-aware code lives in `indexing/`.
- Dependency order: `registry` <- `language` <- `indexing` <- feature packages. `core` imports nothing under `org.phellang`; `core` and `language` may not import each other.
- `tools` (build-time) is imported by nothing at runtime and imports no feature package.
- A feature package reaches another only through a class at its root (`editor` -> `syntax.PhelSyntaxHighlighter` is fine, `syntax.attributes.*` is not). Shared vocabulary goes down into a leaf (`core`, `registry`, `language`).

## Rules

- Register every feature in `META-INF/plugin.xml`. Moving a registered class is a `plugin.xml` change too: after a move, grep `plugin.xml` and confirm every `implementationClass` / `implementation` resolves.
- `registry/data/**` is generated; never hand-edit. Its output package is pinned in `tools/generator/KotlinCodeGenerator.kt` and `RegistryWiringGenerator.kt`; update both when relocating the registry.
- Completion: the `psiElement(...)` pattern matches the leaf at the caret, the lexer token `PhelTypes.SYM`, not `PhelTypes.SYMBOL` (the parser element wrapping it). Use `PlainPrefixMatcher` for `namespace/function` matching.
