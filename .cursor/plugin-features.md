---
description: Comprehensive feature documentation for the Phel IntelliJ plugin
appliesTo: ["**/*.java", "**/*.kt", "**/*.phel"]
tags: ["features", "completion", "highlighting", "plugin"]
priority: high
---

# Phel IntelliJ Plugin Features

## Code Completion Features

### 1. Template Completions

Smart templates with placeholder selection for common Phel constructs:

- `def` → `(def name value)` - `name` is selected for immediate editing
- `defn` → `(defn name [args] body)` - `name` is selected first
- `let` → `(let [bindings] body)` - `bindings` is selected first  
- `if` → `(if condition then else)` - `condition` is selected first
- `when` → `(when condition body)` - `condition` is selected first
- `fn` → `(fn [args] body)` - `args` is selected first

### 2. Namespace Function Completion

Complete support for all official Phel namespaces:

- `str/` - String manipulation functions (split, join, replace, etc.)
- `json/` - JSON encoding/decoding functions
- `base64/` - Base64 encoding/decoding functions  
- `html/` - HTML rendering and escaping functions
- `http/` - HTTP request/response functions
- `test/` - Testing framework functions
- `repl/` - REPL utility functions
- `core/` - Core language functions

### 3. API Function Completion

Comprehensive completion for all core Phel functions:

- **Collection functions**: map, filter, reduce, take, drop, etc.
- **Sequence functions**: first, rest, cons, conj, etc.
- **Predicate functions**: nil?, empty?, some?, every?, etc.
- **Math functions**: +, -, *, /, mod, inc, dec, etc.
- **Comparison functions**: =, <, >, <=, >=, compare, etc.
- **Type functions**: type, symbol, keyword, etc.

### 4. PHP Interop Completion

Common PHP functions and constants:

- **Array functions**: array_merge, array_map, array_filter, etc.
- **String functions**: strlen, substr, str_replace, etc.
- **Math functions**: abs, ceil, floor, random_int, etc.
- **Constants**: PHP_INT_MAX, PHP_INT_MIN, NAN, etc.

### 5. Local Symbol Completion

Context-aware completion for:

- Local variables from `let` bindings
- Function parameters
- Loop variables
- Namespace-local definitions

## Syntax Highlighting Features

### 1. Form Comment Highlighting

Complete support for Phel's `#_` form comments:

```phel
[:one :two :three]     # Normal highlighting
[#_:one :two :three]   # :one is grayed out (commented)
[#_#_:one :two :three] # :one and :two are grayed out (stacked)
```

**Implementation**: Hybrid PSI-based + text-based detection system

### 2. Line Comment Highlighting

Support for both Phel comment styles:

```phel
# This is a comment (traditional)
; This is also a comment (Lisp-style)
```

### 3. Reader Macro Highlighting

Semantic highlighting for all reader macros:

- `'form` - Quote (syntax highlighting)
- `` `form`` - Syntax quote (template highlighting)
- `~form` - Unquote (evaluation highlighting)
- `~@form` - Unquote-splice (splice highlighting)
- `,form` - Comma (whitespace highlighting)
- `,@form` - Comma-splice (whitespace splice highlighting)
- `^{:meta true} form` - Metadata (meta highlighting)

### 4. Semantic Coloring

Context-aware coloring for:

- **Keywords**: `:keyword`, `:namespace/keyword`
- **Symbols**: `symbol`, `namespace/symbol`  
- **Special forms**: `def`, `defn`, `let`, `if`, `when`, `fn`
- **Macros**: `defmacro`, `when-let`, `if-let`
- **Comments**: Line and form comments with different styles

## Smart Editing Features

### 1. Auto-insertion

- Automatic parentheses insertion for nested expressions
- Smart `()` completion prioritized at the top of suggestion list
- Context-aware bracket matching

### 2. Template Insert Handlers

- Cursor positioning after template insertion
- Placeholder selection for immediate editing
- Proper indentation following Lisp conventions

### 3. Namespace Templates

Smart templates for namespace constructs:

- `(:require )` - Cursor positioned inside parentheses
- `(:use )` - Cursor positioned inside parentheses  
- `(:require-file )` - Cursor positioned inside parentheses

## Navigation & Refactoring Features

### 1. Symbol Navigation

- **Go to definition** - Navigate to symbol definitions
- **Find usages** - Find all references to symbols
- **Reference highlighting** - Highlight symbol occurrences

### 2. Renaming Support

- **Smart renaming** with namespace awareness
- **Qualified symbol handling** - Preserves namespace prefixes
- **Cross-file references** - Updates all references

## Architecture Components

### Core Classes

- `PhelCompletionContributor` - Main completion orchestrator
- `PhelAnnotator` - Semantic highlighting and form comment detection
- `PhelLexer` - Tokenization with comment support
- `PhelParser` - PSI tree generation with form comment macros

### Completion Providers

- `PhelApiCompletions` - Core API functions
- `PhelLanguageCompletions` - Special forms and macros
- `PhelNamespaceFunctions` - Centralized namespace functions
- `PhelPhpInteropCompletions` - PHP interoperability  
- `PhelLocalSymbolCompletions` - Context-aware local symbols

### Supporting Classes

- `PhelReference` - Symbol resolution and navigation
- `PhelCompletionRanking` - Intelligent suggestion prioritization
- `PhelSyntaxHighlighter` - Basic syntax coloring
- `PhelCommenter` - Comment/uncomment functionality

## Testing & Quality

### Manual Testing

All features tested with comprehensive `.phel` test files covering:

- Nested completion contexts
- Namespace function resolution
- Form comment edge cases (stacking, inside vectors)
- Template placeholder behavior
- Symbol navigation and renaming

### Build Integration  

- Gradle build system with IntelliJ Platform Plugin
- Automated lexer/parser generation from `.flex`/`.bnf` files
- Searchable options generation for plugin distribution
