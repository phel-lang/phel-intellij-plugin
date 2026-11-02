---
description: Use to document or reverse-spec behavior that already exists in the codebase.
argument-hint: "<existing-feature-or-flow-description>"
x-claude: {allowed-tools: "Read, Grep, Glob, Bash, Edit, Write"}
disable-model-invocation: true
---

Read `.agnostic-ai/skills/code-to-spec/SKILL.md` and run it end to end for the argument. It is the only source for the workflow. The result is the standard `spec.md` in the active feature directory (`.specify/` if Spec Kit is bootstrapped, else `docs/specs/<slug>/spec.md`), not a parallel template or a `*-code-to-spec-*` sidecar.
