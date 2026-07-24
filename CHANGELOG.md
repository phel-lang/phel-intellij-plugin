# Changelog

All notable changes to the Phel IntelliJ plugin are documented here.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

Entries are reconstructed from the git history. Each release notes the bundled Phel API registry version where it was
refreshed, since completion, hover and arity checking are all driven by it.

## [Unreleased]

### Added

- `trace` namespace support; registry refreshed to **Phel 0.49.0**.
- `CODE_OF_CONDUCT.md` (Contributor Covenant 2.1), linked from the README and `CONTRIBUTING.md` (#229).

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
- Gradle configuration cache enabled; the configuration-time `System.setProperty` / `settingsEvaluated`
  deprecation-suppression side effects were removed and folded into `gradle.properties` (#228).
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
