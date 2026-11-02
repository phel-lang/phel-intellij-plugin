---
name: debugging-cycle
description: Use to debug, investigate, or find the root cause of a bug, regression, flaky test, or failure before proposing a fix.
---

Examples: `debug flaky completion test`, `investigate parser crash on unterminated regex`, `find root cause of duplicate annotations`.

Input: the bug or failure in the argument, plus code, tests, generated sources, logs, optional issue or path hints.

## Report location

`.specify/debug/<case-slug>/debug-report.md` if `.specify/` exists, else `docs/debug/<case-slug>/debug-report.md`. `<case-slug>` is `YYYY-MM-DD-short-kebab-summary`, prefixed with the issue number when there is one. Load `debug-report-template.md` before writing it.

## Rules

- The first deliverable is the debug report, not a fix. Propose no fix until the report has concrete evidence, a best hypothesis and a recommended path.
- The report is pre-spec evidence; it does not replace `spec.md`.
- Ground every claim in repo or runtime evidence; label the rest hypothesis or unknown. Check Phel and grammar facts against `api.json`, `.flex`/`.bnf` and `src/main/gen/`, never memory.
- For 3+ files, 2+ packages or end-to-end tracing, run `/workflow.team-run analysis <scope>` and merge its evidence into the report.

## Workflow

1. Reuse the open case directory when resuming; else create one.
2. Triage: record symptom, impact, severity, suspected surface and any safe containment. Provisionally guess the resolution class.
3. Investigate:
   - Reproduce, or document why not. Prefer `./gradlew test --tests "<FullyQualifiedTest>"`.
   - Read errors fully, compare recent changes (`git log`, `git diff`), trace data flow across lexer -> parser -> PSI -> annotator/completion/inspection.
   - Grammar issues: read `src/main/gen/` output and rule order in `Phel.flex`/`Phel.bnf`.
   - IDE-runtime issues: read the `./gradlew runIde` sandbox log; look for swallowed `ProcessCanceledException` and EDT violations.
4. Synthesize: update the report with evidence, best root-cause hypothesis, unknowns and the smallest next validating step. Set `Resolution class`:
   - `restore-fix`: narrow repair restoring intended behavior.
   - `spec-required`: the permanent fix changes intended behavior, a token/PSI/grammar contract or another spec decision.
   - `investigation-only`: more evidence needed.
5. Gate: summarize diagnosis, evidence quality and class, then end with `Debug report ready. Reply with "approve debug report" to continue or tell me what to change.`
6. After approval: `restore-fix` -> narrow bugfix in the implementation workflow, report as scope source; `spec-required` -> new or updated feature spec, then spec -> plan -> tasks -> implementation; `investigation-only` -> keep investigating.
7. Finish with the updated report, evidence used, confidence, class and the exact next workflow step.
