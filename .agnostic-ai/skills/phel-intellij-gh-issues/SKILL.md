---
description: Use to work through all open unassigned or self-assigned issues.
argument-hint: "[--limit N] [--label foo] [--dry-run]"
disable-model-invocation: true
allowed-tools: Read, Bash(gh *), Bash(git *), Bash(./gradlew *), Skill(phel-intellij-gh-issue)
---

# GitHub issues watcher

Process each open issue that is unassigned or assigned to `@me` through `/phel-intellij-gh-issue`, one at a time. Stop on the first hard failure.

Args: `--limit N` (default all), `--label foo`, `--dry-run` (run Phase 1 only; no assignment, branch or commit). Strip a leading `#`.

## Phase 1: discover

Search cannot OR assignees, so run two queries and merge:

```bash
gh issue list --state open --search "no:assignee" --json number,title,labels,assignees,createdAt --limit 200
gh issue list --state open --assignee "@me" --json number,title,labels,assignees,createdAt --limit 200
```

Deduplicate by `number`, drop issues assigned to anyone else (`gh api user -q .login` is me), apply `--label` and `--limit`, sort by `createdAt` ascending. Print `#<num> <title> [unassigned|@me]` per line. Exit if empty.

## Phase 2: clean tree

```bash
git status --porcelain
git fetch origin main
git checkout main && git reset --hard origin/main
```

Abort if dirty. Never auto-stash.

## Phase 3: loop

For each issue:

1. Re-check assignees: `gh issue view <num> --json assignees -q '.assignees[].login'`. Empty or only me: proceed. Anyone else: skip.
2. Run `/phel-intellij-gh-issue <num>`.
3. `gh pr checks --watch`. Fix red checks on the branch before moving on.
4. `gh pr merge --auto --squash --admin`
5. `git checkout main && git fetch origin main && git reset --hard origin/main`

## Stop and report

Halt, naming the issue and why, when: the issue skill errors or leaves the tree dirty; `./gradlew test` fails and reproduces in an isolated targeted run (the integration suite is flaky); CI stays red after one fix attempt; merge is blocked beyond `--admin`; `--limit` reached; queue empty. Do not retry blindly.

## Preconditions

`gh` authenticated with issue and PR rights; clean tree; `main` tracks `origin/main`; `/phel-intellij-gh-issue` available.

No pre-commit hook; never pass `--no-verify`. GitHub CI is the full gate: run focused tests while implementing and the full `./gradlew test` once before commit. Do not split bundled changes into several PRs unless the issue demands it.
