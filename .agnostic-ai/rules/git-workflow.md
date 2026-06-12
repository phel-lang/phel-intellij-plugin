# Git Workflow Rules

## Commit Message Format

Conventional commits.

```
<type>: <description>
<type>(<scope>): <description>

[optional body]
```

### Types

| Type    | Use For                                 |
|---------|-----------------------------------------|
| `feat`  | New features                            |
| `fix`   | Bug fixes                               |
| `ref`   | Code restructuring (no behavior change) |
| `test`  | Adding/updating tests                   |
| `docs`  | Documentation changes                   |
| `chore` | Maintenance tasks                       |
| `perf`  | Performance improvements                |
| `ci`    | CI/CD changes                           |

### Rules

- Use `ref:` for refactors — **never** `refactor:`
- Description must be at least 3 characters
- Description starts lowercase
- No period at the end
- Scope is optional: `feat(completion): add namespace alias matching`
- **Never** reference an AI assistant in commit messages, bodies, or trailers (no `Co-Authored-By`, no "generated with…")
- Merge and revert commits are always allowed

### Examples

```
feat: add hover docs for core functions
feat(lexer): support symbolic numbers (##Inf / ##NaN)
fix: prevent crash on unterminated regex literal
ref: extract namespace registry wiring into NamespaceConfig
test: add integration tests for brace matching
docs: document the completion registry regeneration flow
```

## Branch Naming

```
feat/hover-docs
fix/unterminated-regex-crash
ref/extract-namespace-config
test/brace-matching
```

## Feature Development Cycle

### 1. Planning
- Understand requirements fully
- Identify affected packages under `src/main/kotlin/org/phellang/`
- Plan test strategy (unit vs integration)

### 2. Test-Driven Development
- Write a failing test first
- Implement the minimum code to pass
- Refactor while green
- Every feature should include:
  - **Happy path** tests (expected success)
  - **Edge case** tests (boundaries, empty input, ordering)
  - **Sad path** tests (malformed input, not-found, errors)

### 3. Review
- Self-review before PR
- Verify the feature is registered in `src/main/resources/META-INF/plugin.xml`
- Verify no debug code remains
- If grammar (`.flex`/`.bnf`) changed, confirm `src/main/gen/` was regenerated and committed

### 4. Integration
- Write a descriptive conventional commit message
- Create the PR with a summary and test plan
- Address review feedback

## Pull Request Process

1. **Review all commits** — not just the latest
2. **Run full diff**: `git diff main...HEAD`
3. **Write a thorough PR summary** with context
4. **Include a test plan** with verification steps
5. **Assign to `Chemaclass`**, add a matching label, and use `Closes #X` to auto-close the issue
6. **Push with tracking**: `git push -u origin branch-name`

## Before Pushing

Verify:

- [ ] `./gradlew build` passes (includes lexer/parser generation + tests)
- [ ] `./gradlew test` is green (unit + integration)
- [ ] New plugin features are registered in `plugin.xml`
- [ ] Generated lexer/parser under `src/main/gen/` is in sync with the `.flex`/`.bnf` sources
- [ ] Commit messages follow the convention and contain no AI references
- [ ] Branch is up to date with `main`
