---
description: Triage adversarial review findings, fix confirmed issues, and update the findings report with remediation status.
argument-hint: "[findings-file-or-scope]"
allowed-tools: Read, Edit, Write, Grep, Glob, Bash
disable-model-invocation: true
---

Run after `/workflow.attack` has produced a findings report.

Inputs: `adversarial-findings.md` or the `$ARGUMENTS` path; the approved spec/plan/task artifacts; current implementation and test status.

Required workflow:
1. Locate the findings file. Use the `$ARGUMENTS` path if given; otherwise look for `adversarial-findings.md` near the active feature artifacts or repo root.
2. Triage every finding as `confirmed`, `false-positive`, or `deferred`.
3. Fix all confirmed critical-, high-, and medium-severity issues unless the approved spec says otherwise.
4. Add or update tests when a confirmed issue can regress silently (unit tests in `src/test/kotlin/.../unit/`, integration tests in `.../integration/`).
5. Update the findings report in place: keep the original finding ID, set `Status` to `fixed`/`false-positive`/`deferred`, add a short remediation note.
6. Re-run the most relevant verification after each confirmed fix: targeted tests first (`./gradlew test --tests "..."`), broader `./gradlew build` when the affected surface is large or risky. If grammar changed, regenerate first (`./gradlew generatePhelLexer generatePhelParser`).
7. If any finding is deferred, explain why the deferment is acceptable and what follow-up should happen before/after merge.
8. Summarize what changed, what verification passed, and what risk, if any, remains open.

Exit criteria:
- The findings report is updated with final statuses
- Confirmed issues have corresponding code or test changes
- Post-fix verification status is explicit
- Remaining deferred risk is explicitly documented
