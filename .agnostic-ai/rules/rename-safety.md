---
description: Use when renaming a PSI element, token, action ID, extension point, completion key, registry namespace or generated symbol.
---

# Rename safety

The compiler and IDE rename follow only type-resolved Kotlin/Java references. They miss:

- string literals (action IDs, completion keys, `@NonNls` constants, icon paths)
- `plugin.xml` wiring (extension `id`, `implementationClass`, action `id`, language IDs)
- grammar and lexer references: `Phel.bnf` rule names and `Phel.flex` `return PhelTypes.X` resolve against generated code in `src/main/gen/`
- the registry (`register*Functions.kt`, `NamespaceConfig.kt`, `Namespace`, `ALIASES`)

## 1. Inventory before editing

Grep the old name in all case variants and confirm the list first. `PhelSymbol` and `PhelSymbols` are different strings.

```bash
rg -i 'old[-_ ]?name|oldname' --hidden -g '!build' -g '!.gradle' -g '!.idea'
```

Also sweep tests (fixtures, token assertions, mocks) and resources (icons, message bundles).

## 2. Regenerate, never hand-edit (commit the output)

```bash
./gradlew generatePhelLexer     # after Phel.flex changes
./gradlew generatePhelParser    # after Phel.bnf changes
./gradlew updatePhelRegistry    # after NamespaceConfig.kt / api.json changes
```

## 3. Gate before merge

- `./gradlew build` is green.
- The step 1 sweep finds zero old-name hits outside changelog or release notes.
- Exercise the renamed action, completion or inspection in `./gradlew runIde`.
- `plugin.xml` validates and every renamed extension point resolves.

Before merging, note the rollback: which generated files to regenerate from the old sources and which `plugin.xml` IDs to restore.
