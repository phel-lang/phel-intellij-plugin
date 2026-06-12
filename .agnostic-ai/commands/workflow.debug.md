---
description: Diagnose a bug, regression, flaky test, or failure into a grounded debug report before any fix is proposed.
argument-hint: "<bug-or-failure-description>"
allowed-tools: Read, Grep, Glob, Bash, Edit, Write
disable-model-invocation: true
---

Run when the user wants to debug, investigate, diagnose, or find the root cause of a failure rather than implement a feature.

Required behavior:
1. Read the `debugging-cycle` skill file (`.claude/skills/debugging-cycle/SKILL.md`) and execute it end-to-end for the current repo and `$ARGUMENTS`.
2. Treat that skill as the canonical source of truth for the diagnosis workflow — do not duplicate or improvise it here.
3. The first deliverable is always a grounded `debug-report.md` (under `.specify/debug/<slug>/` when Spec Kit-bootstrapped, otherwise `docs/debug/<slug>/`), not an immediate fix.
