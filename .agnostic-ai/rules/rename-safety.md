# Rename Safety

Cross-codebase renames (PSI element, token, action ID, extension point, completion
key, registry namespace, generated symbol) are among the highest-risk changes in
this plugin. The compiler follows type-resolved references; it is blind to the
string literals and generated artifacts that wire an IntelliJ plugin together.

A rename is NOT done when the Kotlin compiles. It is done when every layer that
references the old name — including string literals, `plugin.xml` registrations,
and generated sources under `src/main/gen/` — is updated and the renamed path is
exercised end-to-end in the IDE sandbox or a test.

## Why symbol refactors are not enough

IDE/`rename-symbol` refactors only follow type-resolved Kotlin/Java references.
They are blind to:

- String literals (action IDs, completion keys, `@NonNls` constants, icon paths)
- XML wiring in `src/main/resources/META-INF/plugin.xml` (extension `id`,
  `implementationClass`, action `id`, language IDs)
- Grammar/lexer references — `Phel.bnf` rule names and `Phel.flex` `return PhelTypes.X`
  resolve against **generated** code in `src/main/gen/`, not hand-written sources
- The auto-generated completion registry (`register*Functions.kt`) and
  `NamespaceConfig.kt` / `Namespace` enum
- `api.json`-derived data and the curated `ALIASES` map

So every rename needs a raw-text sweep **in addition to** the symbol refactor.

## Step 1 — Full-reference inventory (before editing anything)

Grep the OLD name across every layer. Search all case variants in one pass:

```bash
# camel, Pascal, snake, kebab — one sweep, excluding build output
rg -i 'old[-_ ]?name|oldname' --hidden \
  -g '!build' -g '!.gradle' -g '!.idea'
```

| Layer                 | What to grep                                                      |
|-----------------------|-------------------------------------------------------------------|
| Kotlin/Java code      | class / object / function / property names                        |
| plugin.xml            | extension `id`, `implementationClass`, action `id`, language IDs  |
| PSI / parser          | `Phel.bnf` rule names, mixin/impl bindings, `PhelTypes` constants |
| Lexer                 | `Phel.flex` `return PhelTypes.X` references                       |
| Generated sources     | `src/main/gen/**` (regenerate, don't hand-edit)                   |
| Completion registry   | `register*Functions.kt`, `NamespaceConfig.kt`, `Namespace` enum   |
| String literals       | action IDs, completion keys, icon paths, `@NonNls` constants      |
| Tests                 | fixture data, expected-token assertions, mocks                    |
| Resources             | icon file names, messages bundles, `api.json` keys                |

Produce the inventory and confirm it before changing anything. Plural vs singular
and case variants bite hardest — `PhelSymbol` and `PhelSymbols` are different strings.

## Step 2 — Regenerate generated sources (mandatory if grammar/registry changes)

Hand-editing `src/main/gen/` or the generated registry is never the fix. Re-run the
generators so the committed generated code matches the source of truth:

```bash
./gradlew generatePhelLexer     # after Phel.flex changes
./gradlew generatePhelParser    # after Phel.bnf changes
./gradlew updatePhelRegistry    # after NamespaceConfig.kt / api.json changes
```

Commit the regenerated files — they are tracked in the repo.

## Step 3 — Verification gate (before merge)

- `./gradlew build` is green — this regenerates lexer/parser and runs all tests.
- Re-run the Step 1 sweep: zero hits on the OLD name outside CHANGELOG/release notes.
- Smoke-test the renamed path in the IDE sandbox (`./gradlew runIde`) — exercise the
  actual action/completion/inspection, not just its registration.
- Confirm `plugin.xml` still validates and every renamed extension point resolves.

## Rollback plan

Before merging, note the revert: which generated files must be regenerated from the
old sources, and which `plugin.xml` IDs to restore. A rename with no rehearsed
rollback is not ready to merge.
