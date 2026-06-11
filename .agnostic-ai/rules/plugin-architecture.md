---
globs: ["src/main/kotlin/**/*.kt", "src/main/java/**/*.java", "src/main/resources/META-INF/plugin.xml"]
description: Plugin architecture, source layout, and development guidelines
---

# Plugin Architecture

## Source Layout

```
src/main/kotlin/org/phellang/
  actions/          # Contextual menu actions
  annotator/        # Semantic highlighting, form comment detection
  completion/       # Code completion & documentation
    data/           # Auto-generated function registry (register*Functions.kt)
  core/             # Core utilities
  debug/            # Debug support
  documentation/    # Documentation provider (hover popups)
  editor/           # Smart typing, brace matching, folding
  inspection/       # Code inspections
  language/         # File type, icons, lexer (.flex)
    parser/         # Grammar (.bnf), parser definition
  syntax/           # Syntax highlighter, color settings
  tools/            # ApiGenerator for updatePhelRegistry task

src/main/gen/       # Generated lexer/parser (do NOT edit manually)
```

## Key Components

- **PhelCompletionContributor** — main completion orchestrator
- **PhelAnnotator** — semantic highlighting and form comment detection
- **PhelLexer** (`.flex`) — tokenization
- **PhelParser** (`.bnf`) — PSI tree generation
- **PhelFunctionRegistry** — central registry of 350+ Phel functions with documentation
- **PhelDocumentationProvider** — hover documentation with examples
- **PhelReference** — symbol resolution and navigation
- **PhelFoldingBuilder** — code folding
- **PhelTypedHandler** — smart character insertion
- **PhelBraceMatcher** — bracket matching
- **PhelCommenter** — comment/uncomment support

## Development Rules

- All features must be registered in `src/main/resources/META-INF/plugin.xml`
- Use `PhelTypes.SYMBOL` (not `PhelTypes.SYM`) in completion logic
- For namespace completion, use `PlainPrefixMatcher` for full `namespace/function` matching
- Form comment detection uses a hybrid PSI-based + text-based approach for robustness
- Follow IntelliJ Platform SDK conventions: https://plugins.jetbrains.com/docs/intellij/welcome.html
