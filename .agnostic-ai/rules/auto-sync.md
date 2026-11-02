---
name: auto-sync
description: Run agnostic-ai sync when spec files change.
alwaysApply: true
---

After adding, editing or removing a file under `.agnostic-ai/` (`agents`, `skills`, `rules`, `hooks`, `commands`, `mcps`), run `agnostic-ai sync`. Edit the specs, never the generated targets (`.claude/`, `.codex/`, `CLAUDE.md`, `AGENTS.md`; gitignored, overwritten on sync).
