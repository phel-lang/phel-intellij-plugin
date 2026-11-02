---
name: implementation-cycle
description: Use to implement or change plugin behavior from an issue number or request, with approval gates, review and delivery.
---

Examples: `implement #42`, `implement hover docs for core macros`.

Input: an issue number, URL or freeform request; existing feature artifacts when resuming.

## Artifacts

`.specify/` structure if it exists, else `docs/specs/<feature-slug>/` with `spec.md`, `plan.md`, `tasks.md` (`<feature-slug>` is short-kebab, prefixed with the issue number when there is one). They are the source of truth; no ad hoc files outside.

## Rules

- Resume from the first unfinished stage when a spec, plan, tasks, findings or delivery notes exist; reset only on request.
- Stop at each gate with the exact phrase below, unless the user asked for an autonomous run.
- Documenting existing behavior -> `/workflow.document-existing`. Diagnosing a bug, flaky test or failure -> `/workflow.debug`.
- Check Phel facts against `api.json` and https://phel-lang.org/, grammar and PSI facts against `.flex`/`.bnf` and `src/main/gen/`, never memory.

## Workflow

1. Validate: repo root has `build.gradle.kts` and `gradlew`; the `/workflow.*` commands exist in `.claude/commands`.
2. Classify: issue number or URL = ticket mode, else freeform. With nothing to go on and nothing to resume, stop and say what is missing. Run `/workflow.classify <argument>` when available and follow it:
   - `direct`: skip steps 3-11. State the change in 1-2 sentences, edit, run `./gradlew test` (or `build`), summarize. No spec, plan or tasks unless asked.
   - `sdd`: full workflow from step 3.
   - `sdd+brainstorm`: from step 3, run `/workflow.brainstorm <scope>` before the spec.
   - `clarify-first`: ask the one returned question, wait, re-classify. In autonomous mode log it and continue.
   - Without `/workflow.classify`, run the full cycle.
3. Intake: ticket mode runs `gh issue view <n>`; freeform restates the request as an objective with scope, constraints and assumptions. Find the affected surface first (Grep/Read over `src/main/kotlin/org/phellang/`, `plugin.xml`, grammar sources, registry). Large, ambiguous, brownfield, risky or multi-direction: `/workflow.brainstorm <scope>`. Changing an existing capability with no trustworthy spec: `/workflow.document-existing <scope>` first. 3+ files, 2+ packages or end-to-end tracing: `/workflow.team-run analysis <scope>` before finalizing the spec.
4. Spec: create or update `spec.md` with scope, non-goals, dependencies, version-compat assumptions, open questions, risks (grammar ordering, PSI contracts, generated-source impact).
5. Gate: summarize scope, decisions, assumptions, blast radius; end with `Spec ready. Reply with "approve spec" to continue or tell me what to change.`
6. Plan: create or update `plan.md`, with verification (`./gradlew test`/`build`), rollback, registry regeneration, generated source and version compat where relevant.
7. Gate: summarize order, verification, external dependencies; end with `Plan ready. Reply with "approve plan" to continue or tell me what to change.`
8. Tasks: create or update `tasks.md` with order, dependencies, validation steps, parallel-safe work.
9. Consistency: cross-check spec, plan and tasks; apply fixes before the last gate.
10. Gate: summarize order, parallel work, high-risk tasks, findings, verification; end with `Tasks ready. Reply with "approve tasks" to continue or tell me what to change.`
11. Worktree: run `/workflow.prepare-worktree <scope>` for non-trivial work (broad, cross-package, brownfield, grammar/PSI, risky). Skip when already in a suitable isolated workspace.
12. Implement directly, or `/workflow.team-run implementation <scope>` for parallel-safe tasks.
    - After `.flex`/`.bnf` or `NamespaceConfig.kt` changes: `./gradlew generatePhelLexer generatePhelParser updatePhelRegistry`, stage the output.
    - Register every new feature in `plugin.xml`.
    - On a material scope change, update the artifact and reopen its gate (unless autonomous).
13. Review: `/workflow.attack`, then `/workflow.fix` on its findings report.
14. Deliver: `/workflow.delivery`.
15. Report what shipped, what was verified, what is blocked, and the next action if unfinished.

Gates are skipped only in autonomous mode.

## Resume

On `approve spec`, `approve plan`, `approve tasks`, `continue` or `resume`, read the artifacts and continue at the next unfinished stage. Review, remediation, tests, commit or PR prep: `/workflow.attack`, `/workflow.fix`, `/workflow.delivery`. Reframed as diagnosis: `/workflow.debug`; as documentation: `/workflow.document-existing`. For freeform features, the spec and plan replace the original message.

## Done when

Spec, plan, tasks are current; approval gates were respected; implementation matches the approved scope; generated sources and `plugin.xml` are in sync; adversarial findings are remediated; test status, commit status, PR summary and remaining risk are explicit.
