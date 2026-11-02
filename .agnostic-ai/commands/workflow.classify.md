---
description: Use before implementing to decide whether a request needs the full spec/plan/tasks cycle or a direct change.
argument-hint: "<issue-number-or-freeform-request>"
x-claude: {allowed-tools: "Read, Grep, Glob, Bash"}
disable-model-invocation: true
---

Routing only: write no files and start no implementation. When torn between two levels, pick the higher.

1. Input: request text, issue number or URL (`gh issue view <n>`; use title, description, acceptance criteria). Empty argument with resumable artifacts (`spec.md`, `plan.md`, `tasks.md` under `docs/specs/` or `.specify/`): classify that scope. Empty with none: stop and ask "What would you like to implement?"
2. Estimate blast radius with a quick grep, glob or `git diff` (files, packages such as `completion/`, `annotator/`, `language/parser/`, `inspection/`, `plugin.xml`).
3. Signals:
   - DIRECT: one file or named function/class; diff under ~30 lines; the how is fully specified; bug fix with known cause, rename, typo, config value, dependency bump, one registry entry, a single completion or inspection, test for existing behavior, comment or format fix; no new extension point, PSI/token contract, grammar rule or user-visible behavior; no cross-package dependency.
   - SDD: new user-visible feature (action, inspection, completion category, language feature); new or changed PSI/token contract, grammar rule or `plugin.xml` extension point; 3+ files or 2+ packages; several plausible designs; lexer/parser ordering work; rollout concerns (registry regeneration, generated source, version compat); brownfield with unclear current behavior; high blast radius; ambiguous requirements.
   - CLARIFY: too vague to estimate; could be trivial or a design change; conflicting signals of equal weight.
4. Level and route:
   - TRIVIAL (single-file, obvious, under ~20 lines): `direct`
   - SIMPLE (1-3 files, clear, no new contracts): `direct`
   - MODERATE (new behavior, cross-file, design decision): `sdd`
   - COMPLEX (multi-subsystem, new grammar/PSI contracts, ambiguity, high risk): `sdd+brainstorm`
   - UNCLEAR: `clarify-first`
5. End the response with this block:

```
CLASSIFICATION
Complexity  : <TRIVIAL | SIMPLE | MODERATE | COMPLEX | UNCLEAR>
Route       : <direct | sdd | sdd+brainstorm | clarify-first>
Blast radius: <N files / N packages>
Key signals :
 - <signal>
```

`direct`: add a 1-3 sentence plan (changes, files, verification, usually `./gradlew test` or `build`). `sdd` and `sdd+brainstorm`: name the SDD stages that matter most and why. `clarify-first`: ask exactly one question and stop.
