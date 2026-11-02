---
description: Use after implementation, review and remediation to verify, draft the commit and PR, and hand off.
argument-hint: "[feature-path-or-scope]"
x-claude: {allowed-tools: "Read, Grep, Glob, Bash, Edit, Write"}
disable-model-invocation: true
---

Inputs: the feature's spec, plan, tasks; the latest findings report; the diff and test results; optional scope argument (else infer the feature from changed files).

1. Verify: targeted tests first, then `./gradlew build` (regenerates lexer/parser, runs the full suite) when the plan or surface calls for it. If grammar changed, confirm `src/main/gen/` is regenerated and staged. Record what passed, failed or could not run.
2. Summarize shipped scope, key changes, findings status, remaining risk, and any version-compat, generated-source, registry or `plugin.xml` notes.
3. Draft the conventional commit title and body (`feat:`, `fix:`, `ref:`, ...; never `refactor:`; no AI reference). Create it if the user authorized commits; else show the exact command.
4. Draft the PR title and description: scope, spec/plan/task links, verification, findings and remediation, remaining risk. Assign `JesusValeraDev`, add a matching label, use `Closes #X`. Create or draft it if `gh` is available and the user authorized it; else leave a ready-to-paste summary.
5. If a dedicated worktree was used and the PR is open, tell the user its path and ask whether to remove it. Never remove it unasked.
6. End with a checklist: verification, commit, PR, worktree cleanup, open approvals or blockers.
