---
name: auto-sync
description: Run agnostic-ai sync when spec files change.
alwaysApply: true
---

When any file under the agnostic-ai source directories (`agents`, `skills`, `rules`, `hooks`, `commands`, or `mcps`) is added, edited, or removed during a session, run `agnostic-ai sync` to regenerate all target configs (`.claude/`, `.codex/`, `CLAUDE.md`, `AGENTS.md`).

Edit the specs under `.agnostic-ai/`, never the generated targets — the generated paths are gitignored and overwritten on every sync.
