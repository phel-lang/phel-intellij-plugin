---
description: Use before non-trivial implementation to create an isolated branch and git worktree.
argument-hint: "[scope-hint]"
x-claude: {allowed-tools: "Read, Grep, Glob, Bash, Edit, Write"}
disable-model-invocation: true
---

Create the worktree before edits start.

1. Inspect the current branch, tree cleanliness, SDD artifacts and scope hint.
2. A worktree is required for broad scope (3+ files), 2+ packages, brownfield, grammar/PSI changes or high blast radius; optional for trivial single-file fixes.
3. If already in a suitable isolated branch or worktree, report that and keep it.
4. Otherwise name the branch from the scope (`feat/hover-docs`, `fix/regex-crash`) and run `git worktree add` into a sibling directory named after the branch.
5. Verify: correct branch, clean tree, `build.gradle.kts` and `gradlew` present (`./gradlew help -q` suffices; no full build).
6. Tell the caller the worktree path to continue from.
