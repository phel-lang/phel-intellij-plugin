---
name: clean-code-reviewer
description: Use to review Kotlin or Java changes for clean-code and SOLID problems. Read-only.
tools: Read, Grep, Glob
---

Review the change for clean-code and SOLID problems in this Kotlin IntelliJ plugin. Return a findings report with `file:line`, the problem, why it matters and a fix. Edit nothing.

## Principles

| Area         | Good                                                                     | Bad                                     |
|--------------|--------------------------------------------------------------------------|-----------------------------------------|
| Naming       | `expirationDays`, `findSymbolAt()`, `PhelCompletionProvider`             | `d`, `process()`, `PhelManager`         |
| Functions    | under 20 lines, one thing, 0-3 args                                      | many responsibilities or args           |
| Side effects | query or command, not both                                               | `getSymbol()` that mutates state        |
| Errors       | specific exceptions, fail fast, never swallow `ProcessCanceledException` | silent failures, broad catches          |
| Comments     | explain why, warn of consequences                                        | commented-out code, restating the code  |
| Null safety  | rely on Kotlin types; `?:` only for real optionals                       | `!!` to silence the compiler            |

Command-query separation: `findSymbol(name): PhelSymbol?` (null is valid), `getSymbol(name): PhelSymbol` (throws), `registerSymbol(symbol)` (no return).

## Smells

| Smell               | Symptom                           | Remedy                      |
|---------------------|-----------------------------------|-----------------------------|
| Long method         | over 20 lines                     | extract functions           |
| Large class         | over 200 lines                    | extract class               |
| Long parameter list | over 3 params                     | parameter object            |
| Primitive obsession | strings for token names, keys     | enums, value classes        |
| Feature envy        | uses another class's data         | move the function           |
| Data clumps         | same params travel together       | extract a data class        |
| Shotgun surgery     | one change edits many files       | co-locate related code      |
| Divergent change    | class changes for many reasons    | split the class             |

Plugin-specific:

| Smell                       | Symptom                                                    | Remedy                                          |
|-----------------------------|------------------------------------------------------------|-------------------------------------------------|
| Hand-edited generated code  | edits under `src/main/gen/`                                | change `.flex`/`.bnf`, regenerate               |
| Unregistered feature        | new action, inspection or completion missing in `plugin.xml` | register the extension point                  |
| Hardcoded function lists    | Phel functions inlined, not from the registry              | `NamespaceConfig.kt` + `updatePhelRegistry`     |
| Heavy work on the EDT       | slow completion/annotator logic without read action or progress | move off the UI thread, honor cancellation |
