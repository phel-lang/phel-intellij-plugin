---
description: Run an adversarial AI review over the current implementation and ensure findings land in a repo-local markdown report.
argument-hint: "[spec-path-or-scope]"
allowed-tools: Read, Grep, Glob, Bash
disable-model-invocation: true
---

Run after implementation is complete and before commit finalization or PR creation.

Inputs: the approved spec/plan/task artifacts; the current branch diff and changed files; available test output if any; optional `$ARGUMENTS` for a specific spec directory, feature path, or review scope.

Required workflow:
1. Identify the review scope. Use `$ARGUMENTS` if provided; otherwise infer the active feature from branch, changed files, and the most relevant SDD artifacts. Write the report next to the active feature artifacts; fall back to repo root only if no better home is obvious.
2. Route the attack stage through team-run when the surface is broad: run `/workflow.team-run attack <scope>` using the parallel agent capabilities in the current environment, not a separate external team binary.
3. Produce or confirm a single `adversarial-findings.md`. Prefer `<active-feature-dir>/adversarial-findings.md`; if the feature dir is unclear, use repo root and say why.
4. Use this finding format for every item:
   - `ID`
   - `Severity`
   - `Category`
   - `Files`
   - `Problem`
   - `Why it matters`
   - `Repro or scenario`
   - `Suggested fix`
   - `Status: open`
5. Focus the attack on this plugin's real risk surface: lexer/parser ordering and malformed input, PSI/token contract changes, IDE-thread safety and cancellation, completion/inspection correctness, version-compat (2024.2–2026.1.x), generated-source drift, and `plugin.xml` registration gaps.
6. Separate real findings from weak suspicions. Do not pad with noise.
7. End with a short reviewer summary: whether it's ready to test/ship, what must be fixed before commit/PR, and any residual risk for the PR summary.
8. If no material issues are found, still record the reviewed scope, artifact paths, and test evidence consulted.

Exit criteria:
- A single `adversarial-findings.md` exists at the documented path
- Every finding uses the required format
- If no material issues are found, record `No confirmed findings` with the scope you reviewed
