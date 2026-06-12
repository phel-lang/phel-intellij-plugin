---
description: Finalize the implementation cycle with tests, commit preparation, and PR summary.
argument-hint: "[feature-path-or-scope]"
allowed-tools: Read, Grep, Glob, Bash, Edit, Write
disable-model-invocation: true
---

Run after implementation, adversarial review, and remediation are complete.

Inputs: the active feature's spec/plan/task artifacts; the latest findings report if any; the current diff and available test commands/results; optional `$ARGUMENTS` for a specific feature directory or delivery scope.

Required workflow:
1. Identify the delivery scope. Use `$ARGUMENTS` if present; otherwise infer the active feature from changed files, findings, and the most relevant SDD artifacts.
2. Verification: run targeted tests first, then `./gradlew build` (regenerates lexer/parser and runs the full suite) when the plan or changed surface calls for it. If grammar changed, confirm `src/main/gen/` is regenerated and staged. Record what ran, passed, failed, and could not run.
3. Delivery summary: shipped scope, key code changes, findings status, remaining risk. Call out version-compat, generated-source, registry-regeneration, or `plugin.xml` registration notes when the feature requires them.
4. Commit preparation: draft the exact conventional commit title and body (`feat:`, `fix:`, `ref:`, …; never `refactor:`; never reference an AI assistant). If the user authorized commits, create it; otherwise present the exact command and message for approval.
5. Pull request preparation:
   - Draft a PR title and a structured PR description that includes:
     - scope
     - spec/plan/task linkage
     - verification
     - findings and remediation summary
     - remaining risk or follow-ups
   - Assign to `Chemaclass`, add a matching label, and use `Closes #X` to auto-close the issue.
   - If `gh` is available and the user authorized PR creation, create or draft it; otherwise leave a ready-to-paste PR summary.
   - If the work used a dedicated feature worktree and the PR is now open, tell the user the worktree path still exists and ask whether to remove it. Never remove it automatically unless the user says yes.
6. Final handoff: end with a concise checklist of verification, commit, PR, and worktree-cleanup status, plus remaining approvals or blockers.

Exit criteria:
- Verification status is explicit (what passed/failed/skipped)
- Commit message is drafted (and created only if authorized)
- A ready-to-use PR summary exists
- Remaining delivery risk is clearly called out
