---
globs: ["**/*.phel"]
description: Phel syntax reference
---

# Phel Syntax

Lisp transpiling to PHP; Clojure/Janet dialect. Source of truth: https://phel-lang.org/.

**Comments**: `;` line. `#_` comments next form (stackable `#_#_` = two).
`[#_:one :two :three]` → `[:two :three]`.

**Deprecated — never emit, never lex** (Phel still accepts these with an `E_USER_DEPRECATED`;
the plugin lexer deliberately does not support any of them, so don't "fix" that as a gap):
bare `#` line comments (use `;`) · `#| ... |#` nesting block comments (use `;` or `#_`) ·
`|()` short fn (use `#()`) · `,` / `,@` unquote+splice (use `~` / `~@`; the plugin lexes `,`
as whitespace).

**Keywords**: `:kw` · `:ns/kw` · `::foo` (current-ns) · `::alias/foo`.

**PHP interop**: static `(php/:: Class method)` · instance `(php/-> obj method)` · ops `php/+ - && || !== @ ^ ~`.
Bring classes in via `(:use \DateTime \Exception)` in `ns`. Shorthands:

| Shorthand               | Expands to                       |
|-------------------------|----------------------------------|
| `(Class. args)` / `(new Class args)` | `(php/new Class args)`  |
| `(.method obj args)`    | `(php/-> obj (method args))`     |
| `(.-field obj)`         | `(php/-> obj field)`             |
| `(Class/method args)`   | `(php/:: Class (method args))`   |
| `Class/MEMBER`          | `(php/:: Class MEMBER)`          |

**File**: starts `(ns namespace\name)`. Top-level: `def`/`defn`/`defmacro`/comments. Bare literals not idiomatic.

**Reader macros**: `'form` quote · `` `form `` syntax-quote · `~form` unquote · `~@form` splice · `^{...} form` meta.

**Forms**: `def defn let if when fn do quote var throw try` · macros `when-let if-let defmacro time binding`.
