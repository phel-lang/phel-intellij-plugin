---
description: Commits, branches, PRs and the pre-push checklist.
alwaysApply: true
---

# Git workflow

Conventional commits: `<type>(<scope>): <description>` (scope optional). Types: `feat`, `fix`, `ref`, `test`, `docs`, `chore`, `perf`, `ci`.

- Use `ref:`, never `refactor:`.
- Description: at least 3 characters, lowercase start, no trailing period.
- Never reference an AI assistant in messages, bodies or trailers.
- Merge and revert commits are always allowed.
- Branches: `<type>/<slug>`, for example `feat/hover-docs`.

Work test-first (happy, edge and sad paths).

PRs: read `git diff main...HEAD`; write a summary and test plan; assign `JesusValeraDev`, add a matching label, use `Closes #X`; push with `git push -u origin <branch>`.

Before pushing: `./gradlew build` green; new features registered in `src/main/resources/META-INF/plugin.xml`; `src/main/gen/` matches `.flex`/`.bnf`; no debug code; branch up to date with `main`.
