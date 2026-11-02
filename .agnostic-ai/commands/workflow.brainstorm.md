---
description: Use before the spec when scope is large, ambiguous, risky or brownfield, for a short design-alignment pass.
argument-hint: "<feature-or-problem-statement>"
x-claude: {allowed-tools: "Read, Grep, Glob, Bash, Edit, Write"}
disable-model-invocation: true
---

1. Restate the problem in 1-2 sentences.
2. Summarize likely scope, constraints, assumptions, non-goals, key risks, and alternatives that change the design.
3. For brownfield work, read local context first: `src/main/kotlin/org/phellang/` (`actions/`, `annotator/`, `completion/`, `language/` + `parser/`, `inspection/`, `syntax/`), `src/main/resources/META-INF/plugin.xml`, `Phel.flex`, `Phel.bnf`, `NamespaceConfig.kt`, generated `register*Functions.kt`. Check Phel facts against `api.json` and https://phel-lang.org/.
4. End with one handoff: proceed to spec, clarify a named question, or narrow the scope.

Keep it short and decision-oriented; this is not the spec.
