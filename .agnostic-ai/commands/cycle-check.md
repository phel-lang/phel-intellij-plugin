Detect package-level circular and wrong-layer dependencies across the plugin's Kotlin source.

Steps:
1. For each top-level package under `src/main/kotlin/org/phellang/` (`actions`, `annotator`, `completion`, `core`, `debug`, `documentation`, `editor`, `inspection`, `language`, `registry`, `syntax`, `tools`), grep `^import org\.phellang\.` and tally which other packages it imports from.
2. Build an import matrix (rows = source package, cols = target package). Flag any bidirectional edge as a cycle.
3. Apply the expected layering: `language/` is foundation; `core/` is shared utilities; `registry/` is the shared Phel function/symbol model (generated from `api.json`) and must stay a **leaf** — it may import `language/psi` and the platform, nothing else; feature packages (`annotator`, `completion`, `editor`, `inspection`, `documentation`, `actions`, `debug`) consume `language/`, `core/` and `registry/` but should not depend on each other except where explicitly justified (e.g. `editor/` references `annotator/TextAttributesKey` for color settings).
4. Report:
   - **HIGH**: confirmed cycles (bidirectional edges) with concrete `file:line: import ...` evidence. Any import of a feature package from `registry/` is HIGH — it re-creates the cycle the registry extraction removed.
   - **MEDIUM**: wrong-layer imports (foundation reaching up into feature packages).
   - **LOW**: cross-sibling imports between feature packages that may be justified.
5. Known architectural debt to ignore unless worsened:
   - `core.psi ↔ language.psi`: `PhelSymbolAnalyzer` imports PSI types; `language/psi/{references,navigation,impl}` import the analyzer back. Moving the analyzer into `language/psi` does not fix it — it would create `language.psi ↔ registry` instead, since `registry/indexing` imports `language.psi`.
   - `completion.infrastructure ↔ completion.handlers`: `PhelCompletionUtils` ↔ `NamespacedInsertHandler`. Benign (no class-init hazard; constructed inside a function body).
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
