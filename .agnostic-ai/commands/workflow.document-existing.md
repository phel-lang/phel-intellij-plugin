---
description: Reverse-engineer an existing capability into a grounded spec artifact.
argument-hint: "<existing-feature-or-flow-description>"
allowed-tools: Read, Grep, Glob, Bash, Edit, Write
disable-model-invocation: true
---

Run when the user wants to document or reverse-spec behavior that already exists in the codebase.

Required behavior:
1. Read the `code-to-spec` skill file (`.claude/skills/code-to-spec/SKILL.md`) and execute it end-to-end for the current repo and `$ARGUMENTS`.
2. Treat that skill as the canonical source of truth for reverse-spec behavior — do not duplicate or improvise the workflow here.
3. The final artifact must be the standard `spec.md` in the active feature directory (under `.specify/` when the repo is Spec Kit-bootstrapped, otherwise `docs/specs/<slug>/spec.md`) — not a parallel custom template family or a sidecar `*-code-to-spec-*` document.
