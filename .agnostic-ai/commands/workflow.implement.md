---
description: Run the full implementation cycle using SDD artifacts, approval gates, adversarial review, remediation, tests, and delivery handoff.
argument-hint: "<issue-number-or-URL-or-freeform-request>"
allowed-tools: Read, Grep, Glob, Bash, Edit, Write
disable-model-invocation: true
---

Repo-local entrypoint for requests like `implement #42` or `implement hover docs for core macros`.

Required behavior:
1. Read the `implementation-cycle` skill file (`.claude/skills/implementation-cycle/SKILL.md`) and execute it end-to-end for the current repo and `$ARGUMENTS`.
2. Treat that skill as the canonical source of truth for the implementation cycle — do not duplicate or improvise the workflow here.
