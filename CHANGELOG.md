# Changelog

All notable changes to the Phel IntelliJ plugin are documented here.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

Entries are reconstructed from the git history. Each release notes the bundled Phel API registry version where it was
refreshed, since completion, hover and arity checking are all driven by it.

## [Unreleased]

### Added

- The `bench` namespace (`defbench`, `run-benchmarks`) now completes and hovers. It shipped in Phel 0.50.0 but was
  missing from the registry's namespace config, so the generator skipped it with a warning on every run (#321).
- PHP superglobals (`php/$_SERVER`, `php/$_GET`, …) now appear in completion and hover, matching what Phel's own LSP
  and REPL gained in 0.50.0. They are variables rather than functions, so no generator can derive them from the PHP
  docs and the list is maintained by hand (#321).
- A **Superseded interop or var form** inspection flags the source spellings Phel 0.50.0 deprecated: `php/new`,
  `php/->`, `php/::` and `set-var`, each reported with the Clojure-style spelling to write instead. The rest of the
  `php/` family is untouched, since each of those reaches a PHP capability Phel has no other word for (#321).

### Changed

- Registry refreshed to **Phel 0.50.0**. Documentation links now point at the 0.50.0 sources and multi-arity
  signatures are shown one arity per line instead of collapsed into an `& [...]` tail (#321).
- Phel 0.50.0 removed every deprecated function from the language, so no stdlib symbol is reported as deprecated any
  more. The deprecated-function inspection, annotator and quick fix are unchanged and will light up again the next
  time Phel deprecates something (#321).

### Fixed

- Arity checking is no longer suppressed for calls containing a bare `|`. That accommodated the `|(...)` short fn,
  which Phel 0.50.0 removed; `(some |(> % 10) coll)` is now correctly reported as a wrong-arity call rather than
  silently skipped (#321).
- `$`-prefixed names are checked by the unresolved-symbol inspection again. Only bare `$` is still meaningful, as the
  return value inside an `fn` `:post` condition; `$1` was a `|(...)` parameter and is now an ordinary symbol (#321).
- The Gradle version the `Wrapper` task writes is back in step with the wrapper itself. Dependabot bumps only
  `gradle-wrapper.properties`, so the task had been left on 9.3.0 while the wrapper reached 9.6.1, and running
  `./gradlew wrapper` would have quietly downgraded the project by six minor versions. Both now read 9.7.0 (#324).

## [1.1.0] - 2026-07-25

### Fixed

- Reformat Code no longer fails with `env: php: No such file or directory` on macOS. The formatter now runs with the
  login shell's environment, and gains a 30-second timeout so a hung `phel` cannot block the editor (#257).
- Pressing Enter after a closed form now returns to the right column: `(print "hello"))` starts the next line at
  column zero, and closing one of two forms dedents by a single level (#258).
- Completion works again in `do`, `try`, `throw` and `recur` bodies, and on the value half of a `let` or `loop`
  binding (#254).
- Completion no longer suggests existing names while you are naming a new `def`, `defn` or `defmacro` (#254).
- Completion no longer offers names discarded by `#_` (#254).
- Completion is suppressed where only a binding or parameter vector can follow, as in `(let …)` and `(fn …)` (#254).
- Go to Definition no longer jumps to an arbitrary usage inside `do`, `try`, `when` or a threading macro (#254).
- `defonce` definitions now resolve (#254).
- `if-some`, `when-some`, `if-let`, `when-let`, `foreach`, `dofor` and `doseq` bindings now resolve, appear in
  completion, and are covered by the binding inspections and parameter hints (#254, #256).
- `defonce`, `defenum`, `defprotocol`, `defrecord`, `deftype` and `defmulti` now appear in cross-file completion and
  navigation, fold correctly, and are named in the structure view and Find Usages (#254).
- Parameters of a `defn` documented with a metadata map containing a vector, such as `{:see-also ["assoc"]}`, are
  recognised again: they are highlighted, resolve, and can be renamed (#255).
- Hovering a recursive call, or a call to a function defined earlier in the same file, shows that function's signature
  and docstring instead of "Function Argument" (#261).

### Added

- A **run configuration** for Phel files. Right-click a `.phel` file, or use the Run icon in the gutter beside its
  `(ns …)` form, to run it through the project's `phel` binary. The binary is found the same way the formatter finds
  it (`./bin/phel`, then `./vendor/bin/phel`) and runs with the login shell's environment, so a Homebrew, Herd, asdf
  or mise `php` is on its `PATH` even when the IDE was started from Dock or Spotlight (#263).
- **Test files now run into the test tree.** Opening a file whose `ns` requires `phel\test` and using the gutter icon,
  the context menu or Run Anything runs `phel test` on it, so results arrive as a green or red bar instead of plain
  console output. Ordinary `.phel` files still run through `phel run` and its console. Detection is based on the
  `phel\test` require rather than on `phel-config.php`, so it works for a test file kept outside the configured test
  directories, and both the `phel.test` and `phel\test` spellings are recognised.
- **Run a single test** from the gutter icon beside any `deftest`. The test is selected with an anchored pattern, so
  running `void-tags` cannot also pull in `void-tags-ignore-content`, which a plain name filter would.
- **Live templates** for eleven forms: `defn-`, `defmacro`, `ns`, `deftest`, `when`, `when-let`, `loop`, `cond`,
  `case`, `try` and `foreach`. They appear under Settings > Editor > Live Templates > Phel, expand with Tab, and can
  be edited or extended. The six abbreviations completion already offers (`()`, `defn`, `def`, `let`, `if`, `fn`) are
  deliberately not redefined, so no name produces two differently-behaving entries (#268).
- **Spellchecking** of string literals, including docstrings, and line comments. Symbols are not checked: in a Lisp
  nearly every token is one, and checking them would underline most of a file (#268).
- **Default keyboard shortcuts for the nine paredit actions**, which previously shipped reachable only through the
  menu. All are two-stroke, under a shared `Ctrl+Alt+Shift+P` prefix: `S` / `B` slurp and barf forward, `Shift+S` /
  `Shift+B` backward, `W` / `V` / `M` wrap in `( )` / `[ ]` / `{ }`, `U` splice, `R` raise (#267).
- **Move Element Left/Right** (`Ctrl+Alt+Shift+Left/Right`) over the forms inside a list, vector, map or set, for
  reordering arguments and map entries (#267).
- **Surround With** (`Ctrl+Alt+T`) for wrapping a selection of whole forms in `( )`, `[ ]` or `{ }`. Unlike the
  paredit wrap actions, which take the single form at the caret, this works across a multi-form selection (#267).
- An **Unused private definition** inspection, reporting a `defn-` / `def-` / `^:private` definition that nothing in
  its own file references. Only private definitions are reported: a public one may be called from any namespace, so
  its own file cannot tell (#266).
- An **Unused function parameter** inspection, covering `defn`, `defn-`, `fn`, `defmacro` and `defmacro-`. Names
  starting with `_` or `&` are never reported (#266).
- A **built-in formatter**, used when the project has no `phel` binary. Reformat Code, format-on-paste and
  reindent-selection now work before `composer install`, in a scratch file, or where the binary is not on one of the
  searched paths. `phel format` stays the preferred path and still wins whenever it is available; the built-in one
  indents two spaces per level, matching what pressing Enter already does (#265).
- A **Code Style page** for Phel (Settings > Editor > Code Style > Phel), covering indentation and blank lines:
  how many consecutive blank lines to keep, and how many to force between top-level forms. Only options the
  formatter actually honours are shown (#265).
- A **REPL** run configuration, launching `phel repl` in the Run console, which accepts typed input and forwards it
  to the process (#263).
- A **test** run configuration, running `phel test` over the whole suite or over named paths, into a real test tree
  with pass/fail status, durations and failure details. Built from the `junit-xml` reporter, which is written
  alongside the normal console output and read when the run finishes (#263). The tree shows one node per `deftest`:
  that reporter emits an entry per `is` form, so the entries of a test are folded into a single node that fails when
  any of its assertions does and lists every failing form in its details.
- A **run configuration** for Phel files. Right-click a `.phel` file, or use the Run icon in the gutter beside its
  `(ns …)` form, to run it through the project's `phel` binary. The binary is found the same way the formatter finds
  it (`./bin/phel`, then `./vendor/bin/phel`) and runs with the login shell's environment, so a Homebrew, Herd, asdf
  or mise `php` is on its `PATH` even when the IDE was started from Dock or Spotlight (#263).
- **Go to Symbol** (Navigate > Symbol) now finds Phel definitions. The project symbol index that already backed
  completion and arity resolution simply had no route into the platform's name popups; definitions are listed with
  their namespace, so two functions sharing a name can be told apart, and choosing one jumps to the name itself
  rather than the top of the file (#264).
- **Breadcrumbs** above the editor, showing the trail of enclosing forms — `defn greet > let > when`. Only forms that
  establish context are shown, so the trail does not simply mirror parenthesis depth (#264).
- A **built-in formatter**, used when the project has no `phel` binary. Reformat Code, format-on-paste and
  reindent-selection now work before `composer install`, in a scratch file, or where the binary is not on one of the
  searched paths. `phel format` stays the preferred path and still wins whenever it is available; the built-in one
  indents two spaces per level, matching what pressing Enter already does (#265).
- A **Code Style page** for Phel (Settings > Editor > Code Style > Phel), covering indentation and blank lines:
  how many consecutive blank lines to keep, and how many to force between top-level forms. Only options the
  formatter actually honours are shown (#265).
- An **Unresolved symbol** inspection, reporting a name that exists nowhere in scope. Phel raises
  `PHEL001 Cannot resolve symbol` for these, so the code does not compile (#256, #259).
- A **Create function** quick fix on that inspection. Where the missing name is being called, it writes a `defn` above
  the calling form, taking its parameter names from the call's arguments — `(greet user count)` gives
  `(defn greet [user count] )` (#259).
- An **Unused private definition** inspection, reporting a `defn-` / `def-` / `^:private` definition that nothing in
  its own file references. Only private definitions are reported: a public one may be called from any namespace, so
  its own file cannot tell (#266).
- An **Unused function parameter** inspection, covering `defn`, `defn-`, `fn`, `defmacro` and `defmacro-`. Names
  starting with `_` or `&` are never reported (#266).
- **Default keyboard shortcuts for the nine paredit actions**, which previously shipped reachable only through the
  menu. All are two-stroke, under a shared `Ctrl+Alt+Shift+P` prefix: `S` / `B` slurp and barf forward, `Shift+S` /
  `Shift+B` backward, `W` / `V` / `M` wrap in `( )` / `[ ]` / `{ }`, `U` splice, `R` raise (#267).
- **Move Element Left/Right** (`Ctrl+Alt+Shift+Left/Right`) over the forms inside a list, vector, map or set, for
  reordering arguments and map entries (#267).
- **Surround With** (`Ctrl+Alt+T`) for wrapping a selection of whole forms in `( )`, `[ ]` or `{ }`. Unlike the
  paredit wrap actions, which take the single form at the caret, this works across a multi-form selection (#267).
- **Live templates** for eleven forms: `defn-`, `defmacro`, `ns`, `deftest`, `when`, `when-let`, `loop`, `cond`,
  `case`, `try` and `foreach`. They appear under Settings > Editor > Live Templates > Phel, expand with Tab, and can
  be edited or extended. The six abbreviations completion already offers (`()`, `defn`, `def`, `let`, `if`, `fn`) are
  deliberately not redefined, so no name produces two differently-behaving entries (#268).
- **Spellchecking** of string literals, including docstrings, and line comments. Symbols are not checked: in a Lisp
  nearly every token is one, and checking them would underline most of a file (#268).

### Changed

- The **unused-import** warning is now a switchable inspection (Settings > Editor > Inspections > Phel > Unused
  import) instead of a fixed annotation, so it can be disabled or re-levelled. What it reports is unchanged, including
  its silence on `:refer` imports and on the duplicated copy of an import already reported as a duplicate (#266).
- The `phel` binary is now also looked up on the login shell's `PATH`, after `./bin/phel` and `./vendor/bin/phel`. A
  project-local binary still wins, so a version pinned through Composer is never overridden by a global install
  (#265).
- Completion now offers only what a position can hold. Macros and special forms are no longer suggested as arguments,
  where they cannot appear; functions still are, and threading macros are exempt. In head position, definition forms
  no longer outrank every function (#260).
- Large internal refactor: the biggest classes were split into focused collaborators, the longest function dropping
  from 100 to 43 lines, and the definition-form vocabulary that navigation, folding, indexing and the structure view
  had each copied is now a single set (#254).

## [1.0.0] - 2026-07-24

### Added

- `trace` namespace support; registry refreshed to **Phel 0.49.0**.
- `CODE_OF_CONDUCT.md` (Contributor Covenant 2.1), linked from the README and `CONTRIBUTING.md` (#229).
- The standard-library documentation popup now shows a deprecation notice with the suggested replacement, kept
  consistent with the deprecated-function inspection (#230).
- Completion and hover documentation for native PHP functions called through `php/` (e.g. `(php/strlen s)`,
  `(php/array_map f xs)`) — the core-extension built-ins, generated from the official PHP documentation
  (`php/doc-en`) and each linking to its php.net page (#240).

### Fixed

- Multi-arity standard-library signatures now render one arity per line in the documentation popup, matching the
  project-symbol popup (#231).
- `#_` form comments are resolved over the PSI instead of a text regex, so discarded forms no longer shift argument
  positions.
- Project symbol index build is now cancellable and deadlock-safe, and VFS-triggered parsing is moved off the EDT and
  scoped to the project.
- Private definitions are detected from metadata rather than body text.

### Changed

- The build keeps the Kotlin stdlib, gson and the registry generator out of the distribution jar.
- CI enforces the plugin project-configuration check and uploads real Kover coverage to Codecov.
- Gradle configuration cache enabled: removed the configuration-time `System.setProperty` /
  `settingsEvaluated` deprecation-suppression side effects (folded into `gradle.properties`), made the
  change-notes provider cache-safe, and pinned the lexer to generate before the parser so its purge no
  longer races the parser's generated PSI output (#228).
- Issue templates migrated to GitHub issue forms, with a `config.yml` routing support questions to the Phel and
  IntelliJ Platform docs (#227).

## [0.5.3] - 2026-07-16

- Registry refreshed to **Phel 0.48.0**.
- Broad internal cleanup: stronger types, broken import cycles, consolidated PSI-walking logic.

## [0.5.2] - 2026-07-15

### Fixed

- Local completions are no longer dropped in large files.
- Explicit completion invocation respects the namespace prefix.

### Changed

- Large architectural refactor: every module now exposes its entry point at its root; oversized classes (
  `PhelReference`, `PhelSymbolAnalyzer`, the PHP reflection layer, ns-clause analysis) were split into focused
  collaborators, enforced by a new module-boundary architecture test.

## [0.5.1] - 2026-06-25

### Added

- Lexer support for namespaced tagged literals.
- Registry refreshed.

## [0.5.0] - 2026-06-13

### Performance

- Wide performance pass across highlighting and analysis: per-file caching of the `(ns …)`declaration, `(:require …)`/
  `(:use …)` clauses, alias maps, referred symbols, used classes and local function names; map-based token
  classification; lazy API-doc rendering; and fast paths that skip anon-function and form-comment work when the file
  contains no `#(` or `#_`.

### Fixed

- Eliminated arity-mismatch false positives for variadic, multi-arity and short-function calls.
- Arity mismatch, inlay parameter hints and let-binding inspections now fire for `PhelAccess`-wrapped heads; `when-let`
  bindings resolve in reference resolution.
- Symbol-index map updates are now atomic.
- Registry refreshed to **Phel 0.42.0**.

## [0.4.5] - 2026-06-05

- Go-to-definition from `(:use …)` PHP classes.

## [0.4.4] - 2026-06-05

### Added

- Navigate from `load` / `require` forms to their target files.

### Fixed

- PHP interop operators `php/^`, `php/~`, `php/@` are lexed as single symbols.
- Cleared the deprecated- and experimental-API Marketplace warnings.

## [0.4.3] - 2026-05-25

### Added

- Recognise PHP interop shorthands and resolve classes brought in via `(:use …)`.

### Fixed

- Function parameters are no longer mis-marked as functions when they share a name with a Phel standard-library
  function.

## [0.4.2] - 2026-05-21

### Added

- Rename refactoring.
- Inlay parameter hints.
- Arity-mismatch and unused/shadowed let-binding inspections.

### Fixed

- Resolved `:refer` import warnings.

## [0.4.1] - 2026-05-20

- Updated for Phel 0.35.0–0.39.0, plus platform and dependency updates.

## [0.4.0] - 2026-05-11

### Added

- Recognise multi-arity `defn`/`fn` parameter vectors.
- Surface the `defn` docstring on hover instead of only the category label; resolve hover docs for symbols imported via
  `:refer`.
- Paredit structural-editing actions.
- Structure view for top-level Phel forms.
- Reformat Code integration with `./bin/phel fmt`.
- Registry updated to **Phel 0.36.0**.

## [0.3.6] - 2026-04-21

- Syntax and registry updated to **Phel 0.34.1**.

## [0.3.5] - 2026-04-18

- Syntax support updated for Phel 0.3.3; added `CONTRIBUTING.md` and `RELEASE.md`.

## [0.3.4] - 2026-04-13

- Syntax support updated for Phel 0.3.2.

## [0.3.3] - 2026-04-13

- Registry refreshed.

## [0.3.2] - 2026-04-08

### Added

- Suggest `require`, `require-file`, `use` and `refer` inside the `ns` form (#84).
- Registry updated to **Phel 0.31.0**.

## [0.3.0] - 2026-02-01

### Added

- Symbol completion, namespace validation and Go to Definition.
- Automatic namespace import when a function belongs to another namespace.
- Documentation on hover for aliased and `:refer`-imported functions.
- Visual deprecation warning on deprecated functions.
- Completion results sorted by priority.

### Fixed

- Grey out unused namespaces; correct the color for `:refer` keys.

## [0.2.4] - 2026-01-25

### Added

- `updatePhelRegistry` command to regenerate the function registry from the official Phel API.

## [0.2.3] - 2025-12-12

### Fixed

- Slash-symbol handling, including `::` for `php/::`.

### Changed

- Renamed the `DataFunction` model to `PhelFunction`.

## [0.2.2] - 2025-12-09

- Reformatted the documentation popup.

## [0.2.1] - 2025-12-08

- Compatibility with IntelliJ 2025.3.

## [0.2.0] - 2025-11-15

### Added

- Documentation module: quick-documentation popups for standard-library functions.

### Fixed

- Parenthesis-position and short-set `#{}` highlighting.

## [0.1.10] - 2025-11-04

### Added

- GitHub Actions CI (build, test and plugin-verification jobs) and a "Build Distribution" run configuration.

### Fixed

- Invalid-namespace handling.

### Changed

- Large internal modularization of the editor, language and syntax packages; renamed the file type to "Phel File".

## [0.1.9] - 2025-09-27

- Show completion suggestions only where they make sense; refactored the actions module.

## [0.1.8] - 2025-09-24

### Added

- "Phel File" entry in the New-file context menu.

### Fixed

- Stop rendering raw HTML in completion suggestions.

## [0.1.6] - 2025-09-21

- Refined syntax colors and plugin description; silenced warning logs.

## [0.1.5] - 2025-09-20

### Added

- Completion of user-defined function names.

## [0.1.4] - 2025-09-20

### Added

- Code folding.
- Multi-line comment support.
- Richer semantic coloring, including set literals `#{}` and namespaced constructions (`QUALIFIED_SYMBOL` token).

## [0.1.3] - 2025-09-14

### Added

- Auto-completion of the `{` character.

### Changed

- Migrated the plugin sources from Java to Kotlin.

## [0.1.2] - 2025-09-12

- Compatibility bump: build target moved from 242 to 252.

## [0.1.0] - 2025-09-12

The first public release. Core language support for `.phel` files:

### Added

- File type, JFlex lexer and Grammar-Kit parser/PSI for the Phel language.
- Syntax highlighting.
- Code completion, with `namespace/function` matching.
- Reference resolution and symbol navigation.
- `;` line comments and `#_` form-comment tokens.
- Auto-closing brackets and a comment shortcut.
