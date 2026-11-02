---
description: Use after /workflow.attack to triage findings, fix confirmed issues and update the report.
argument-hint: "[findings-file-or-scope]"
x-claude: {allowed-tools: "Read, Edit, Write, Grep, Glob, Bash"}
disable-model-invocation: true
---

1. Find `adversarial-findings.md`: the argument path, else near the feature artifacts or repo root.
2. Triage every finding: `confirmed`, `false-positive` or `deferred`.
3. Fix all confirmed critical, high and medium findings unless the approved spec says otherwise.
4. Add or update tests where a confirmed issue could regress silently (`src/test/kotlin/.../unit/` or `.../integration/`).
5. Update the report in place: keep the ID, set `Status` to `fixed`, `false-positive` or `deferred`, add a short remediation note.
6. After each fix re-run targeted tests (`./gradlew test --tests "..."`), and `./gradlew build` when the surface is large or risky. If grammar changed, run `./gradlew generatePhelLexer generatePhelParser` first.
7. For each deferred finding, say why deferring is acceptable and the follow-up.
8. Summarize changes, verification and remaining risk.
