# Changelog

All notable changes to the Phel IntelliJ plugin are documented here.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

Entries are reconstructed from the git history. Each release notes the bundled Phel API registry version where it was
refreshed, since completion, hover and arity checking are all driven by it.

## [Unreleased]

### Added

### Changed

### Deprecated

### Removed

### Fixed

### Security

### Performance

## [1.1.0] - 2026-07-25

### Fixed

- Reformat Code no longer fails with `env: php: No such file or directory` on macOS. The formatter now runs with the
  login shell's environment, and gains a 30-second timeout so a hung `phel` cannot block the editor (#257).
- Pressing Enter after a closed form now returns to the right column: `(print "hello"))` starts the next line at
  column zero, and closing one of two forms dedents by a single level (#258).
- Completion works again in `do`, `try`, `throw` and `recur` bodies, and on the value half of a `let` or `loop`
  binding (#254).
- Completion no longer suggests existing names while you are naming a new `def`, `defn` or `defmacro` (#254).
- Completion no longer offers names discarded by `#_`, in a definition name, a `let` binding vector or a parameter
  vector. `(let [alpha 1 #_[beta 2] gamma 3] …)` offers `gamma` again, and `(defn f [iota #_kappa lambda] …)` no
  longer offers `kappa` (#253, #254).
- Completing a `php/` name no longer inserts `(:require phel.php)`, which the namespace validator then flagged as
  "Namespace 'phel.php' does not exist". `php/` is the interop prefix, not a namespace, so nothing is imported for it
  (#253).
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
- Requiring the `edn`, `reflect`, `trace` or `transit` namespaces no longer reports them as unknown. The short-name map
  the import validator reads was hand-maintained and had drifted from the generated registry (#253).
- Parameter hints are no longer rendered inside `(use …)`, where `(use \DateTimeImmutable :as Date)` labelled its
  arguments `ClassName:` and `options:` (#253).
- Character literals with one or two octal digits, such as `\o7` and `\o77`, now lex as characters instead of symbols.
  The lexer required exactly three digits, while Phel's reader has accepted one to three since v0.32.0 (#252).

### Added

- An **Unresolved symbol** inspection, reporting a name that exists nowhere in scope. Phel raises
  `PHEL001 Cannot resolve symbol` for these, so the code does not compile (#256, #259).
- A **Create function** quick fix on that inspection. Where the missing name is being called, it writes a `defn` above
  the calling form, taking its parameter names from the call's arguments — `(greet user count)` gives
  `(defn greet [user count] )` (#259).

### Changed

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

[Unreleased]: https://github.com/phel-lang/phel-intellij-plugin/compare/v1.1.0...HEAD
[1.1.0]: https://github.com/phel-lang/phel-intellij-plugin/compare/v1.0.0...v1.1.0
[1.0.0]: https://github.com/phel-lang/phel-intellij-plugin/compare/v0.5.3...v1.0.0
[0.5.3]: https://github.com/phel-lang/phel-intellij-plugin/compare/v0.5.2...v0.5.3
[0.5.2]: https://github.com/phel-lang/phel-intellij-plugin/compare/v0.5.1...v0.5.2
[0.5.1]: https://github.com/phel-lang/phel-intellij-plugin/compare/v0.5.0...v0.5.1
[0.5.0]: https://github.com/phel-lang/phel-intellij-plugin/compare/v0.4.5...v0.5.0
[0.4.5]: https://github.com/phel-lang/phel-intellij-plugin/compare/v0.4.4...v0.4.5
[0.4.4]: https://github.com/phel-lang/phel-intellij-plugin/compare/v0.4.3...v0.4.4
[0.4.3]: https://github.com/phel-lang/phel-intellij-plugin/compare/v0.4.2...v0.4.3
[0.4.2]: https://github.com/phel-lang/phel-intellij-plugin/compare/v0.4.1...v0.4.2
[0.4.1]: https://github.com/phel-lang/phel-intellij-plugin/compare/v0.4.0...v0.4.1
[0.4.0]: https://github.com/phel-lang/phel-intellij-plugin/compare/v0.3.6...v0.4.0
[0.3.6]: https://github.com/phel-lang/phel-intellij-plugin/compare/v0.3.5...v0.3.6
[0.3.5]: https://github.com/phel-lang/phel-intellij-plugin/compare/v0.3.4...v0.3.5
[0.3.4]: https://github.com/phel-lang/phel-intellij-plugin/compare/v0.3.3...v0.3.4
[0.3.3]: https://github.com/phel-lang/phel-intellij-plugin/compare/v0.3.2...v0.3.3
[0.3.2]: https://github.com/phel-lang/phel-intellij-plugin/compare/v0.3.0...v0.3.2
[0.3.0]: https://github.com/phel-lang/phel-intellij-plugin/compare/v0.2.4...v0.3.0
[0.2.4]: https://github.com/phel-lang/phel-intellij-plugin/compare/v0.2.3...v0.2.4
[0.2.3]: https://github.com/phel-lang/phel-intellij-plugin/compare/v0.2.2...v0.2.3
[0.2.2]: https://github.com/phel-lang/phel-intellij-plugin/compare/v0.2.1...v0.2.2
[0.2.1]: https://github.com/phel-lang/phel-intellij-plugin/compare/v0.2.0...v0.2.1
[0.2.0]: https://github.com/phel-lang/phel-intellij-plugin/compare/v0.1.10...v0.2.0
[0.1.10]: https://github.com/phel-lang/phel-intellij-plugin/compare/v0.1.9...v0.1.10
[0.1.9]: https://github.com/phel-lang/phel-intellij-plugin/compare/v0.1.8...v0.1.9
[0.1.8]: https://github.com/phel-lang/phel-intellij-plugin/compare/v0.1.6...v0.1.8
[0.1.6]: https://github.com/phel-lang/phel-intellij-plugin/compare/v0.1.5...v0.1.6
[0.1.5]: https://github.com/phel-lang/phel-intellij-plugin/compare/v0.1.4...v0.1.5
[0.1.4]: https://github.com/phel-lang/phel-intellij-plugin/compare/v0.1.3...v0.1.4
[0.1.3]: https://github.com/phel-lang/phel-intellij-plugin/compare/v0.1.2...v0.1.3
[0.1.2]: https://github.com/phel-lang/phel-intellij-plugin/compare/v0.1.0...v0.1.2
[0.1.0]: https://github.com/phel-lang/phel-intellij-plugin/commits/v0.1.0
