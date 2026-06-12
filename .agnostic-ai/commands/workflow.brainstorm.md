---
description: Run a short discovery and design-alignment pass before implementation when scope or requirements are unclear.
argument-hint: "<feature-or-problem-statement>"
allowed-tools: Read, Grep, Glob, Bash, Edit, Write
disable-model-invocation: true
---

Run when the request is large, ambiguous, high-risk, brownfield, or likely to benefit from a short design/discovery pass before spec generation.

Required behavior:
1. Restate the problem in one or two concise sentences.
2. Identify and summarize: likely scope, constraints, assumptions, non-goals, key risks, and plausible alternatives when they materially affect design/implementation shape.
3. Keep the output short and decision-oriented — this is a discovery/alignment step, not the final spec.
4. If brownfield or broad enough to need codebase inspection, review local context first: Grep/Glob/Read over `src/main/kotlin/org/phellang/` (`actions/`, `annotator/`, `completion/`, `language/` + `parser/`, `inspection/`, `syntax/`), the extension wiring in `src/main/resources/META-INF/plugin.xml`, the grammar sources (`Phel.flex`, `Phel.bnf`), and the registry (`NamespaceConfig.kt`, generated `register*Functions.kt`). Verify Phel language facts against `api.json` and https://phel-lang.org/.
5. End with a concrete handoff: proceed to spec generation, clarify a specific open question, or narrow the scope first.

Important: don't let brainstorming become an essay — use it to reduce rework risk before the formal spec.
