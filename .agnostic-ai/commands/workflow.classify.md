---
description: Classify a request's complexity and decide whether the full SDD cycle (spec → plan → tasks) is warranted or a direct implementation is faster and sufficient.
argument-hint: "<issue-number-or-freeform-request>"
allowed-tools: Read, Grep, Glob, Bash
disable-model-invocation: true
---

Run before implementation to decide whether the request warrants the full SDD workflow or can be handled directly without spec/plan/tasks artifacts.

Required behavior:
1. Understand the request. `$ARGUMENTS` is the request text, GitHub issue number, or issue URL; use repo state (files, recent git diff, existing SDD artifacts under `docs/specs/`) as context.
    - If `$ARGUMENTS` is an issue number/URL, fetch it via `gh issue view <n>` when available. Use the title, description, and acceptance criteria as the classification input.
    - If `$ARGUMENTS` is freeform text, treat it directly as the request.
    - If `$ARGUMENTS` is empty and resumable artifacts exist (`spec.md`, `plan.md`, `tasks.md` under `docs/specs/` or `.specify/`), classify the scope captured in those artifacts instead.
    - If `$ARGUMENTS` is empty and no resumable artifacts exist, stop and ask: "What would you like to implement?"

2. Estimate blast radius via a quick scan (grep/glob/git diff) of how many files and packages the change touches — a fast proxy is enough, not deep analysis. Note specific files/packages when obvious (`completion/`, `annotator/`, `language/parser/`, `inspection/`, `plugin.xml`).

3. Score the request against these signals:

   Signals that favor DIRECT (no SDD artifacts needed):
    - Change is confined to a single file or a clearly named function/class
    - Estimated diff is small (< ~30 lines)
    - The "how" is fully specified — no design decision is required
    - Request type: bug fix with known root cause, rename, typo, config value change, dependency bump, adding one registry entry, a single completion/inspection, test for existing behavior, comment update, formatting fix
    - No new extension point, PSI/token contract, grammar rule, or observable user-facing behavior introduced
    - No cross-package or cross-subsystem dependency

   Signals that favor SDD (spec → plan → tasks required):
    - New user-visible feature or behavior (new action, inspection, completion category, language feature)
    - New or changed PSI/token contract, grammar rule (`.flex`/`.bnf`), or extension point in `plugin.xml`
    - Change spans 3+ files or crosses 2+ packages
    - Multiple plausible implementation directions exist
    - Lexer/parser ambiguity or ordering-sensitive grammar work
    - Rollout complexity (registry regeneration, generated-source impact, version-compat concerns)
    - Brownfield enhancement where current behavior is unclear or not yet specced
    - High blast radius if implemented incorrectly
    - Ambiguous requirements that could ship in the wrong shape

   Signals that require clarification before routing:
    - Request is too vague to estimate scope
    - Request could be either a trivial fix or a design change depending on unknown context
    - Conflicting signals with roughly equal weight

4. Assign a complexity level and route:
    - TRIVIAL  — single-file fix, obvious how, < ~20 lines → route: direct
    - SIMPLE   — 1–3 files, clear requirements, no new contracts → route: direct
    - MODERATE — new behavior, cross-file, requires a design decision → route: sdd
    - COMPLEX  — multi-subsystem, new grammar/PSI contracts, ambiguity, or high risk → route: sdd+brainstorm
    - UNCLEAR  — not enough information to classify → route: clarify-first

5. Output the classification block. This must always be the last thing in your response:

   ─────────────────────────────────────────
   CLASSIFICATION
   ─────────────────────────────────────────
   Complexity  : <TRIVIAL | SIMPLE | MODERATE | COMPLEX | UNCLEAR>
   Route       : <direct | sdd | sdd+brainstorm | clarify-first>
   Blast radius: <estimated N files / N packages>
   Key signals :
    - <signal 1>
    - <signal 2>
    - <signal 3 if needed>
   ─────────────────────────────────────────

   For `direct`: state the fast-path plan in 1–3 sentences (what changes, which files, how to verify — usually `./gradlew test` or `./gradlew build`).
   For `sdd`/`sdd+brainstorm`: note which SDD stages are most critical and why.
   For `clarify-first`: ask exactly one focused question and stop — do not guess.

Important: write no files or artifacts; do not start implementation (routing output only); when in doubt between two levels, prefer the higher.

Exit criteria:
- The CLASSIFICATION block is present and formatted as shown
- The route is one of the four defined values
- For `direct`, the fast-path plan is stated
- For `clarify-first`, exactly one question is asked and no implementation begins
