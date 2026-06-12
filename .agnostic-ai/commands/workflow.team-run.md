---
description: Coordinate a parallel team-run stage for analysis, implementation, or attack work using the available multi-agent capabilities.
argument-hint: "<analysis|implementation|attack> [scope-hint]"
allowed-tools: Read, Grep, Glob, Bash, Agent
disable-model-invocation: true
---

Run when the workflow needs bounded parallel work rather than a single in-process pass.

Inputs: the stage in `$ARGUMENTS` (`analysis`, `implementation`, or `attack`); an optional scope hint after the stage; the repo's SDD artifacts and workflow outputs.

Required workflow:
1. Validate input. First token is the stage, the rest is the scope hint. If the stage is missing or unsupported, stop and explain the accepted values.
2. Resolve the active scope from the scope hint, changed files, and current SDD artifacts.
3. Use the parallel agent capabilities available in the current environment (the `Agent` tool / subagents). If parallel execution isn't available, stop and explain the blocker rather than faking a team run.
4. Launch bounded parallel work for the stage:
   - `analysis` stage:
     - worker 1 traces impacted files, entrypoints, and interfaces: Grep/Read `src/main/kotlin/org/phellang/` to map actions, completion providers, annotators, inspections, and the extension wiring in `plugin.xml`.
     - worker 2 maps hidden dependencies, risks, and constraints: grammar sources (`Phel.flex`, `Phel.bnf`), generated PSI in `src/main/gen/`, the registry (`NamespaceConfig.kt`, `register*Functions.kt`), and IntelliJ Platform version-compat concerns.
     - worker 3 synthesizes what must change in the next spec or plan update.
   - `implementation` stage:
     - worker 1 owns approved task set A
     - worker 2 owns approved task set B
     - worker 3 handles integration checks, verification support (`./gradlew test`), and fix follow-through
   - `attack` stage:
     - worker 1 attacks lexer/parser edge cases, ordering, and malformed-input handling
     - worker 2 attacks IDE-thread safety, cancellation (`ProcessCanceledException`), performance, and version-compat behavior
     - worker 3 normalizes findings, repro quality, and remediation guidance
5. Wait for all parallel work to reach a terminal state before continuing.
6. Surface execution evidence explicitly: which roles/lanes ran, the scope each covered, terminal status for each.

Exit criteria:
- Parallel agent execution was actually used
- Execution evidence was surfaced
- All worker lanes reached terminal states before handoff
- No silent downgrade to a fake team run occurred
