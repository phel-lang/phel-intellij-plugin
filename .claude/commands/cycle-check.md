Detect package-level circular and wrong-layer dependencies across the plugin's Kotlin source.

Steps:
1. For each top-level package under `src/main/kotlin/org/phellang/` (`actions`, `annotator`, `completion`, `core`, `debug`, `documentation`, `editor`, `inspection`, `language`, `syntax`, `tools`), grep `^import org\.phellang\.` and tally which other packages it imports from.
2. Build an import matrix (rows = source package, cols = target package). Flag any bidirectional edge as a cycle.
3. Apply the expected layering: `language/` is foundation; `core/` is shared utilities; feature packages (`annotator`, `completion`, `editor`, `inspection`, `documentation`, `actions`, `debug`) consume `language/` and `core/` but should not depend on each other except where explicitly justified (e.g. `editor/` references `annotator/TextAttributesKey` for color settings).
4. Report:
   - **HIGH**: confirmed cycles (bidirectional edges) with concrete `file:line: import ...` evidence.
   - **MEDIUM**: wrong-layer imports (foundation reaching up into feature packages).
   - **LOW**: cross-sibling imports between feature packages that may be justified.
5. Known architectural debt to ignore unless worsened:
   - `core/psi/PhelSymbolAnalyzer.kt` imports `completion.data.PhelFunctionRegistry` + `completion.infrastructure.PhelCompletionPriority` (classification data lives with the registry; fixing requires extracting a SymbolClassifier interface).
6. Omit findings already present in `.omc/research/ai-slop/04-cycles.md` (if the file exists) unless the evidence has changed.

Output format:

```
# Package Dependency Audit

## Import matrix
<concise table of pkg→pkg edges>

## Cycles (HIGH)
- <A ↔ B>: file:line: import ...

## Wrong-layer imports (MEDIUM)
- ...

## Cross-sibling imports (LOW / informational)
- ...

## Delta vs. last audit
- New: ...
- Resolved: ...
```

Keep the report under 200 lines. No implementation — this is a diagnostic only.
