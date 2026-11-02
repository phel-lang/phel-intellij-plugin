---
description: Use to run a bounded parallel stage (analysis, implementation or attack) with subagents.
argument-hint: "<analysis|implementation|attack> [scope-hint]"
x-claude: {allowed-tools: "Read, Grep, Glob, Bash, Agent"}
disable-model-invocation: true
---

1. The first token is the stage (`analysis`, `implementation`, `attack`); the rest is the scope hint. Missing or unknown stage: stop and list the accepted values.
2. Resolve the scope from the hint, changed files and SDD artifacts.
3. Use real parallel subagents (the `Agent` tool). If unavailable, stop and say so; never fake a team run.
4. Launch three workers:
   - `analysis`: (1) trace impacted files, entrypoints and interfaces under `src/main/kotlin/org/phellang/` (actions, completion providers, annotators, inspections, `plugin.xml` wiring); (2) map hidden dependencies, risks and constraints (`Phel.flex`, `Phel.bnf`, generated PSI in `src/main/gen/`, `NamespaceConfig.kt`, `register*Functions.kt`, platform version compat); (3) synthesize what the next spec or plan update must change.
   - `implementation`: (1) approved task set A; (2) approved task set B; (3) integration checks, verification (`./gradlew test`) and fix follow-through.
   - `attack`: (1) lexer/parser edge cases, ordering, malformed input; (2) IDE-thread safety, cancellation (`ProcessCanceledException`), performance, version compat; (3) normalize findings, repro quality and remediation guidance.
5. Wait for every worker to finish.
6. Report which lanes ran, the scope of each and each terminal status.
