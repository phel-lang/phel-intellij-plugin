---
description: Use after implementation and before commit or PR to run an adversarial review into adversarial-findings.md.
argument-hint: "[spec-path-or-scope]"
x-claude: {allowed-tools: "Read, Grep, Glob, Bash"}
disable-model-invocation: true
---

Inputs: approved spec, plan and tasks; the branch diff; test output if any; optional scope argument.

1. Scope: the argument, else the active feature inferred from branch, changed files and SDD artifacts.
2. For a broad surface run `/workflow.team-run attack <scope>`.
3. Write one `adversarial-findings.md` at `<active-feature-dir>/adversarial-findings.md` (repo root, with the reason, if the feature dir is unclear).
4. Each finding has: `ID`, `Severity`, `Category`, `Files`, `Problem`, `Why it matters`, `Repro or scenario`, `Suggested fix`, `Status: open`.
5. Attack the real risk surface: lexer/parser ordering and malformed input, PSI/token contract changes, IDE-thread safety and cancellation, completion and inspection correctness, version compat (2024.3+, no until-build), generated-source drift, `plugin.xml` registration gaps.
6. Separate real findings from weak suspicions; no padding.
7. End with a short summary: ready to test or ship, what must be fixed before commit or PR, residual risk.

With no material issue, write `No confirmed findings` with the scope reviewed, artifact paths and test evidence.
