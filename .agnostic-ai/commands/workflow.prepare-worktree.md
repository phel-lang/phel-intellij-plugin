---
description: Prepare a dedicated branch and git worktree for non-trivial implementation work before coding starts.
argument-hint: "[scope-hint]"
allowed-tools: Read, Grep, Glob, Bash, Edit, Write
disable-model-invocation: true
---

Run when implementation should happen in an isolated branch/worktree instead of the current checkout.

Required behavior:
1. Inspect repo context: current branch, working-tree cleanliness, current SDD artifacts and scope hint.
2. Decide whether a dedicated worktree is required. Required for non-trivial work: broad scopes (3+ files), changes crossing 2+ packages, brownfield enhancements, grammar/PSI changes, risky/high-blast-radius work. Optional for trivial single-file or low-risk fixes.
3. If the environment is already a suitable isolated feature branch/worktree, report that and preserve it.
4. Otherwise: derive a clear feature branch name from the active scope or SDD context (e.g. `feat/hover-docs`, `fix/regex-crash`), create the branch/worktree with `git worktree add`, using a sibling directory path that clearly identifies the branch.
5. Verify the new worktree: correct branch, clean tree, build files present (`build.gradle.kts`, `gradlew`), ready for implementation. A quick `./gradlew help -q` or relying on the IDE import is enough — no full build required.
6. Return a concise handoff telling the caller to continue from the prepared worktree path.

Important: create the worktree before edits start, not after a large diff exists; don't force a second worktree if the user is already in a suitable isolated workspace.
