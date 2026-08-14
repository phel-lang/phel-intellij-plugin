---
globs: ["**/*.phel"]
description: Phel syntax reference
---

# Phel Syntax

Lisp transpiling to PHP; Clojure/Janet dialect. Source of truth: https://phel-lang.org/.

**Comments**: `;` line. `#_` comments next form (stackable `#_#_` = two).
`[#_:one :two :three]` → `[:two :three]`.

**Removed in v0.50.0 — never emit, never lex.** These are gone from the language, not merely
deprecated: a file using one fails to lex or reports an unresolvable symbol. The plugin lexer
deliberately does not support any of them, so don't "fix" that as a gap.

| Removed              | Replacement                                       |
|----------------------|---------------------------------------------------|
| `#` line comment     | `;`                                               |
| `#\| ... \|#` block comment | `;` or `#_`                                |
| `\|(...)` short fn, `$`/`$1` params | `#(...)`, `%`/`%1` params          |
| `,` / `,@` unquote+splice | `~` / `~@` (the plugin lexes `,` as whitespace) |
| `foo$` auto-gensym   | `foo#`                                            |

Only the auto-gensym meaning of a trailing `$` is gone: bare `$` is still the return value inside
an `fn` `:post` condition, and `$` remains an ordinary character in a name.

**Keywords**: `:kw` · `:ns/kw` · `::foo` (current-ns) · `::alias/foo`.

**PHP interop**: the Clojure-style shorthand is **the** spelling. `php/new`, `php/->`, `php/::`
and `set-var` are deprecated as source since v0.50.0 (they remain as the expansion target, and
`PhelSupersededFormInspection` flags them). A root class needs no leading `\`; namespaced classes
take the dotted form (`Symfony.Component.Console.Command.Command/SUCCESS`). A class is recognised
lexically by an upper-case first segment — import a lower-case-initial vendor namespace via
`(:use ...)` first.

| Written                 | Expands to                         | Position |
|-------------------------|------------------------------------|----------|
| `(.m obj args)`         | `(php/-> obj (m args))`            | call     |
| `(.-field obj)`         | `(php/-> obj field)`               | call     |
| `(C/m args)`            | `(php/:: C (m args))`              | call     |
| `(C. args)` / `(new C args)` | `(php/new C args)`            | call     |
| `C/CONST`               | `(php/:: C CONST)`                 | value    |
| `C/$prop`               | `(php/:: C $prop)`                 | value    |
| `C/m`                   | `(php/callable C m)`               | value    |
| `C/.m`                  | `(fn [o & args] ...)`              | value    |

`(set! C/slot v)` assigns a static property; reading it back needs the sigil (`C/$prop`), since a
bare name in read position is the constant. The rest of the `php/` family stays current:
`php/aget` `php/aset` `php/apush` `php/aunset` `php/oset` `php/ref` `php/callable`, plus the
operators `php/+ - && || !== @ ^ ~`.

**File**: starts `(ns namespace\name)`. Top-level: `def`/`defn`/`defmacro`/comments. Bare literals not idiomatic.

**Reader macros**: `'form` quote · `` `form `` syntax-quote · `~form` unquote · `~@form` splice · `^{...} form` meta.

**Forms**: `def defn let if when fn do quote var throw try` · macros `when-let if-let defmacro time binding`.
