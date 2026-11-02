---
name: code-to-spec
description: Use to document or reverse-spec existing behavior into a grounded spec.md without changing it.
---

Examples: `document existing completion ranking`, `reverse spec the regex-literal lexer state`.

Input: a capability description, plus code, tests, generated sources, configs, docs, optional path hints.

## Spec location

`.specify/templates/spec-template.md` -> `spec.md` if `.specify/` exists, else `docs/specs/<feature-slug>/spec.md`. Update an existing `spec.md` in place.

## Rules

- Never invent extension points, PSI types, tokens, actions, grammar rules, Phel functions or behaviors. Check Phel facts against `api.json` and https://phel-lang.org/; grammar and PSI facts against `Phel.flex`, `Phel.bnf` and `src/main/gen/`.
- Cite evidence for each claim (path + symbol, config key, or test). Unprovable claims go to `Unknowns & Questions` or are labeled assumptions.
- Use cautious wording: `observed`, `implied by`, `appears to`, `not observable in code`. Avoid `definitely`, `guarantees`, `always`, `never`.
- Two conflicting sources: document both and add a question; do not guess.
- For 3+ files, 2+ packages or end-to-end tracing, run `/workflow.team-run analysis <scope>` first.
- Checklists: `checklists/hallucination_guardrails.md`, `checklists/plugin_behavior_safe_spec.md`.

## Workflow

1. Resolve or create the feature directory.
2. Scope: identify entrypoints (actions, completion contributors, annotators, inspections), PSI and grammar boundaries, `plugin.xml` registrations. Build an evidence table before drafting.
3. Extract current behavior strictly from code, tests and docs: invariants, state transitions (lexer states, PSI shape, completion order), threading, cancellation, performance, version compat. Say so when one is not observable.
4. Write the spec: Summary, Evidence, Current Behavior, Invariants & Edge Cases, Constraints (version compat, generated source, registration), Acceptance Criteria, Unknowns & Questions.
5. Quality loop: self-check with `evals/eval_template.md`, grade with `evals/grader_prompt.md`. Pass: `structure >= 0.9`, `grounding >= 0.95`, `behavior_safety >= 0.9` (when grammar, PSI or threading is involved), `overall >= 0.9`, zero high-severity issues. On failure, fix using evidence only and re-grade, at most 3 rounds; then record gaps in `Unknowns & Questions` and report a safe fail.
6. Report evidence sources, remaining unknowns, whether parallel analysis ran, and whether the loop passed or safe-failed.
