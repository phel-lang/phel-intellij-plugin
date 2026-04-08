---
globs: ["**/*.phel"]
description: Phel language specification and syntax reference
---

# Phel Language Reference

Phel is a functional programming language that transpiles to PHP. It is a dialect of Lisp inspired by Clojure and Janet. Always refer to https://phel-lang.org/ as the source of truth.

## Comments
- Line comments: `#` or `;`
- Form comments: `#_` comments out the next form. Stackable: `#_#_` comments out two forms
- No block comments

```phel
# This is a line comment
; This is also a line comment
[#_:one :two :three]   # results in [:two :three]
[#_#_:one :two :three] # results in [:three]
```

## Keywords
- Simple: `:keyword`, `:simple-name`
- Namespaced: `:namespace/keyword`, `:php/array`
- Current namespace: `::foo` (expands to `:current-namespace/foo`)
- Aliased: `::alias/foo`

## PHP Interop
- Static calls: `(php/:: ClassName method)`
- Instance calls: `(php/-> object method)`
- Operators: `php/+`, `php/-`, `php/&&`, `php/||`, `php/!==`, `php/@`, `php/^`, `php/~`

## File Structure
- Every file starts with `(ns namespace\name)`
- Top-level forms: `def`, `defn`, `defmacro`, comments
- Bare literals at top level are not idiomatic

## Reader Macros
- Quote: `'form` — shorthand for `(quote form)`
- Syntax quote: `` `form `` — template with auto-gensym
- Unquote: `~form` — evaluate within syntax quote
- Unquote-splice: `~@form` — splice sequence
- Meta: `^{:meta true} form` — attach metadata

## Special Forms & Macros
- Core: `def`, `defn`, `let`, `if`, `when`, `fn`, `do`, `quote`, `var`, `throw`, `try`
- Macros: `when-let`, `if-let`, `defmacro`, `time`, `binding`
