---
description: Use to diagnose a bug, regression, flaky test or failure into a debug report before any fix.
argument-hint: "<bug-or-failure-description>"
x-claude: {allowed-tools: "Read, Grep, Glob, Bash, Edit, Write"}
disable-model-invocation: true
---

Read `.agnostic-ai/skills/debugging-cycle/SKILL.md` and run it end to end for the argument. It is the only source for the workflow. The first deliverable is `debug-report.md` (`.specify/debug/<slug>/` if Spec Kit is bootstrapped, else `docs/debug/<slug>/`), not a fix.
