---
globs: [ "**/*.phel" ]
description: Phel syntax reference
---

# Phel syntax

**Comments**: `;` line. `#_` comments the next form (`#_#_` = two).

**Removed in v0.50.0: never emit, never lex.** The plugin lexer deliberately omits them; do not "fix" that.

| Removed                             | Replacement                                     |
|-------------------------------------|-------------------------------------------------|
| `#` line comment                    | `;`                                             |
| `#\| ... \|#` block comment         | `;` or `#_`                                     |
| `\|(...)` short fn, `$`/`$1` params | `#(...)`, `%`/`%1` params                       |
| `,` / `,@` unquote+splice           | `~` / `~@` (the plugin lexes `,` as whitespace) |
| `foo$` auto-gensym                  | `foo#`                                          |

Only the trailing-`$` auto-gensym is gone: bare `$` is still the return value in an `fn` `:post` condition.

**PHP interop**: the Clojure-style shorthand is the spelling. `php/new`, `php/->`, `php/::` and `set-var` were removed as source in v0.52.0 (writing one is a `PHEL012` error); they remain the expansion target (the table emits them) and `PhelSupersededFormInspection` flags any in source. Root classes need no leading `\`; namespaced classes use dots (`Symfony.Component.Console.Command.Command/SUCCESS`). A class is recognized by an upper-case first segment; import lower-case vendor namespaces via `(:use ...)` first.

| Written                      | Expands to              | Position |
|------------------------------|-------------------------|----------|
| `(.m obj args)`              | `(php/-> obj (m args))` | call     |
| `(.-field obj)`              | `(php/-> obj field)`    | call     |
| `(C/m args)`                 | `(php/:: C (m args))`   | call     |
| `(C. args)` / `(new C args)` | `(php/new C args)`      | call     |
| `C/CONST`                    | `(php/:: C CONST)`      | value    |
| `C/$prop`                    | `(php/:: C $prop)`      | value    |
| `C/m`                        | `(php/callable C m)`    | value    |
| `C/.m`                       | `(fn [o & args] ...)`   | value    |

`(set! C/slot v)` assigns a static property; reading it needs the sigil (`C/$prop`). Still current: `php/aget` `php/aset` `php/apush` `php/aunset` `php/oset` `php/ref` `php/callable` and the `php/` operators.

**Type tags**: `^int ^string ^float ^bool ^array`, a class name, and since v0.53.0 `^map ^vector ^set ^list ^keyword ^symbol ^atom`. Nullable: `^?map` or `^map|null`; `|` and `&` combine members. The symbol after `^` names a type, never a var. `language/psi/PhelTypeTags` alone decides what is a tag (`isTypeTag`) and holds the short-tag table . Every feature that reads a symbol as a var asks it first (references, highlighting, hover, completion after `^`, usage/unused/extract checks).

**Strings**: an octal escape above `\377` is a compile error since v0.53.0; the plugin lexes it as an ordinary string and does not validate escapes.

**Runtime**: Phel v0.53.0 needs PHP 8.5+.

**Destructuring** (v0.51.0+): map patterns are binding-first, Clojure order: `{n :name}`, `{{:keys [c]} :inner}`, `{[a b] :pair}`. Key-first `{:name n}` is deprecated (flagged with a swap quick fix) but compiles. `:keys`/`:strs`/`:syms [syms]`, `:as sym`, `:or {sym default}` are directives; `:or` binds nothing. `{k v}` with two symbols is ambiguous and read key-first (`k` is evaluated). `PhelDestructuringAnalyzer` is the single reader of patterns; go through it.
