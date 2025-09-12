---
description: Rules for the Phel programming language specification and compliance
appliesTo: ["**/*.phel"]
tags: ["phel", "language", "spec"]
priority: high
---

## Language Specification Compliance

Always refer to the **official Phel documentation** (see `phel-docs.md`) as the single source of truth.  
Load docs with **Playwright MCP** rather than relying on summaries.

## Official References

For plugin development context, always refer to the **JetBrains Plugin SDK**:
- [JetBrains Plugin SDK](https://plugins.jetbrains.com/docs/intellij/welcome.html)

Use **Playwright MCP** to fetch the SDK pages when you need detailed guidance. Do not rely on summaries from web search.

### Key Language Notes

### Comments

- Line comments start with `#` or `;` character
- Form comments use `#_` to comment out the next form
- Form comments can be stacked: `#_#_` comments out two forms
- No traditional block comments supported

Example:
```phel
# This is a line comment
; This is also a line comment
[:one :two :three]     # results to [:one :two :three]
[#_:one :two :three]   # results to [:two :three] 
[#_#_:one :two :three] # results to [:three]
```

### Keywords

- Simple: `:keyword`, `:simple-name`
- Namespaced: `:namespace/keyword`, `:php/array`
- Current namespace shortcuts: `::foo` (evaluates to `:current-namespace/foo`)
- Aliased namespace shortcuts: `::alias/foo` (using namespace aliases)
- Mixed separators: `:my\namespace/keyword` (backslash + forward slash)

### PHP Interoperability

- Static method calls: `(php/:: ClassName method)`
- Instance method calls: `(php/-> object method)`
- Property access: `(php/-> object property)`
- All PHP operators supported: `php/+`, `php/-`, `php/*`, etc.
- Multi-character operators: `php/&&`, `php/||`, `php/!==`
- Error suppression: `php/@`
- Bitwise operators: `php/^`, `php/~`

### File Structure

- Every file must start with `(ns namespace\name)`
- Top-level forms: `def`, `defn`, `defmacro`, comments
- Bare literals at top level are not idiomatic

### Reader Macros

- Quote: `'form` (shorthand for `(quote form)`)
- Syntax quote: `` `form`` (template with auto-gensym)
- Unquote: `~form` (evaluate within syntax quote)
- Unquote-splice: `~@form` (splice sequence within syntax quote)  
- Comma: `,form` (whitespace in templates)
- Comma-splice: `,@form` (whitespace splice)
- Meta: `^{:meta true} form` (attach metadata)

### Special Forms and Macros

Core special forms: `def`, `defn`, `let`, `if`, `when`, `fn`, `do`, `quote`, `var`, `throw`, `try`
Core macros: `when-let`, `if-let`, `defmacro`, `time`, `binding`
