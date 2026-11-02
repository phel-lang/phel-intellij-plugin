---
description: Use to take one GitHub issue to a merged PR.
argument-hint: "[issue-number]"
---

# GitHub issue workflow

Read the issue body and every comment as requirements. A later maintainer comment overrides the body.

::target claude
!`gh issue view ${ARGUMENTS#\#} --json number,url,title,body,labels,assignees,state,comments 2>/dev/null || echo "Provide an issue number"`
::end

## Setup

1. Strip `#` from the argument.
2. Self-assign: `gh issue edit <number> --add-assignee @me`
3. Branch from fresh `main` as `<prefix><issue-number>-<slug>`. Prefix by label: `bug` -> `fix/`, `enhancement` -> `feat/`, `documentation` -> `docs/`, none -> `feat/`.
   ```bash
   git checkout main && git pull
   git checkout -b <branch-name>
   ```

## Plan

4. Enter plan mode. Explore the affected code and follow the module rule in `.agnostic-ai/rules/plugin-architecture.md` (registered classes at the module root, internals in subfolders).
5. Write the plan: what the issue requires, files to change, test strategy (unit in `src/test/kotlin/org/phellang/unit/`, integration in `.../integration/`), implementation order.

## Implement

6. After plan approval, work test-first: failing test, minimum code, refactor.
7. Run `./gradlew test` and fix every failure. If `.flex` or `.bnf` changed, run `./gradlew generatePhelLexer generatePhelParser` and commit the regenerated `src/main/gen/`. If `NamespaceConfig.kt` changed, run `./gradlew updatePhelRegistry`. The integration suite is flaky: a failure that does not reproduce in an isolated targeted run is not real.

## Ship

8. Add a `CHANGELOG.md` entry under `## Unreleased` only if the file exists (it does not yet; issue #213).
9. Commit with `git add <specific-files>`: `<type>(<scope>): <description>` plus body line `Related to #<issue-number>`.
10. Last commit before the PR, mandatory: re-review every touched file for duplication, dead branches, unused params, leftover debug, naming drift, over-engineering, and violations of the rules in `.agnostic-ai/rules/` (`plugin-architecture.md` incl. `ArchitectureBoundaryTest`, `lexer-parser.md`, `completion-registry.md`). Fix, re-run `./gradlew test`, commit as `ref(<scope>): polish <area> after #<issue-number>`. If nothing changes, say so in the PR body.
11. Push and open the PR with a summary and test plan. Per `.agnostic-ai/rules/git-workflow.md`: assignee `JesusValeraDev`, label matching the issue, `Closes #<issue-number>`.
    ```bash
    git push -u origin <branch-name>
    gh pr create --assignee JesusValeraDev --label <label> --title "<type>: <description>" --body "<summary>

    ## Test plan
    <steps>

    Closes #<issue-number>"
    ```

## Verify and merge

12. `gh pr checks <pr> --watch`. Fix red checks on the branch; do not proceed while a required check fails.
13. Merge: `gh pr merge <pr> --squash --admin --delete-branch`. If `--admin` is rejected, use `--auto --squash --delete-branch` and report the PR awaits human approval. Never `--no-verify` past a failing required check.
14. `git checkout main && git fetch origin main && git reset --hard origin/main`
