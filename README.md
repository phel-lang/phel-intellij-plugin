# Phel IntelliJ Plugin

[![Build](https://github.com/phel-lang/phel-intellij-plugin/actions/workflows/build.yml/badge.svg)](https://github.com/phel-lang/phel-intellij-plugin/actions/workflows/build.yml)
[![Version](https://img.shields.io/jetbrains/plugin/v/28459.svg)](https://plugins.jetbrains.com/plugin/28459)
[![Downloads](https://img.shields.io/jetbrains/plugin/d/28459.svg)](https://plugins.jetbrains.com/plugin/28459)

[![Plugin Icon](src/main/resources/META-INF/pluginIcon.svg)](https://plugins.jetbrains.com/plugin/28459-phel-lang)

[Phel](https://phel-lang.org/) language support for JetBrains IDEs (IntelliJ IDEA, PhpStorm, and others).

Phel is a functional programming language that compiles to PHP — a Lisp dialect inspired by Clojure and Janet.

## Installation

1. Open **Settings → Plugins → Marketplace**
2. Search for "**Phel**"
3. Click **Install** and restart the IDE

Compatible with IDE versions 2024.2 through 2026.2. Open any `.phel` file and the plugin activates automatically.

## Features

- **Syntax highlighting** with semantic analysis and error detection
- **Code completion** for the standard library, namespaces, and project symbols
- **Documentation popups** on hover, with examples and deprecation info
- **Inspections & quick-fixes** — deprecated functions, arity mismatches, unused and shadowed `let` bindings, backslash
  namespaces
- **Navigation** — go to definition, structure view, rename refactoring
- **Parameter hints** at call sites
- **Structural editing** — paredit, bracket matching, smart typing, code folding
- **Formatting** via the external `phel fmt` command
- **File templates** for new Phel files

Completion and documentation are backed by a generated registry of 900+ functions across 35 namespaces (`phel\core`,
`phel\string`, `phel\json`, `phel\http`, `phel\test`, `phel\schema`, …), kept in sync with
the [official Phel API](https://phel-lang.org/documentation/api/).

## Development

```bash
git clone git@github.com:phel-lang/phel-intellij-plugin.git
cd phel-intellij-plugin

./gradlew runIde              # launch a sandbox IDE with the plugin
./gradlew test                # run the test suite
./gradlew buildPlugin         # build the distributable zip
./gradlew updatePhelRegistry  # regenerate the function registry from api.json
```

Open the project in IntelliJ IDEA as a Gradle project. The lexer (`.flex`) and parser (`.bnf`) sources are regenerated
automatically during compilation.

## Resources

- [Phel language](https://phel-lang.org/)
- [Phel API documentation](https://phel-lang.org/documentation/api/)
- [Plugin on JetBrains Marketplace](https://plugins.jetbrains.com/plugin/28459-phel-lang)

## Contributing

Issues and pull requests are welcome.
