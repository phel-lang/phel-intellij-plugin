---
globs: ["src/main/kotlin/**/*.kt", "src/main/java/**/*.java", "src/main/resources/META-INF/plugin.xml"]
description: Plugin architecture and source layout
---

# Architecture

Layout under `src/main/kotlin/org/phellang/`:
`actions/` menu actions · `annotator/` highlighting + form-comment detection · `completion/` (+`data/` generated registry) · `core/` utils · `debug/` · `documentation/` hover · `editor/` typing/brace/folding · `inspection/` · `language/` filetype/icons/lexer + `parser/` grammar · `syntax/` highlighter+colors · `tools/` ApiGenerator. `src/main/gen/` = generated (never edit).

Key classes: `PhelCompletionContributor` (completion), `PhelAnnotator` (highlight), `PhelFunctionRegistry`, `PhelDocumentationProvider` (hover), `PhelReference` (resolve/nav), `PhelFoldingBuilder`, `PhelTypedHandler`, `PhelBraceMatcher`, `PhelCommenter`.

Rules:
- Every feature must be registered in `META-INF/plugin.xml`.
- Completion logic: use `PhelTypes.SYMBOL` (not `SYM`); use `PlainPrefixMatcher` for `namespace/function` matching.
- Form-comment detection is hybrid PSI + text-based.
- Follow IntelliJ Platform SDK conventions.
