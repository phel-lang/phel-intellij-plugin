---
description: Use to check the plugin's Kotlin packages for circular and wrong-layer dependencies.
---

Run the `cycle-detector` agent (`.agnostic-ai/agents/cycle-detector.md`) over `src/main/kotlin/org/phellang/` and report. Layers, baseline and method live there; do not restate them.

Severity:
- HIGH: confirmed cycle (bidirectional edge) with `file:line: import ...`. Any import of a feature package from `registry/` is HIGH.
- MEDIUM: wrong-layer import (foundation reaching up into a feature package).
- LOW: cross-sibling feature imports that may be justified.

Skip findings already in `.omc/research/ai-slop/04-cycles.md` (if present) unless the evidence changed. Report under 200 lines with sections: Import matrix, Cycles, Wrong-layer imports, Cross-sibling imports, Delta vs last audit. Diagnostic only; no edits.
