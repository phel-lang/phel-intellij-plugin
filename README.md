# Phel IntelliJ Plugin

[![Build Status](https://img.shields.io/badge/status-active-brightgreen.svg)]()
[![IntelliJ Platform](https://img.shields.io/badge/platform-IntelliJ-blue.svg)]()
[![Version](https://img.shields.io/jetbrains/plugin/v/28459.svg)](https://plugins.jetbrains.com/plugin/28459)
[![Downloads](https://img.shields.io/jetbrains/plugin/d/28459.svg)](https://plugins.jetbrains.com/plugin/28459)

[![Plugin Icon](src/main/resources/META-INF/pluginIcon.svg)](https://plugins.jetbrains.com/plugin/28459-phel-lang)

This plugin provides comprehensive IDE support for the [Phel programming language](https://phel-lang.org/) in JetBrains IDEs (IntelliJ IDEA, PhpStorm, WebStorm, etc.).

Phel is a functional programming language that transpiles to PHP. It is a dialect of Lisp inspired by Clojure and Janet.

## Installation

### From JetBrains Marketplace

1. Open your JetBrains IDE (IntelliJ IDEA, PhpStorm, etc.)
2. Go to **File → Settings → Plugins** (or **IntelliJ IDEA → Preferences → Plugins** on macOS)
3. Search for "**Phel**"
4. Click **Install** and restart the IDE

## Features

- **Syntax Highlighting** - Full lexer-based syntax highlighting for Phel code
- **Code Completion** - Intelligent completion for core functions, namespaces, and project symbols
- **Documentation Popups** - Hover over functions to see detailed documentation with examples
- **Code Inspections** - Deprecated-function, backslash-namespace, arity-mismatch, unused-let-binding, and shadowed-let-binding checks with quick-fixes
- **Parameter Hints** - Inlay hints showing parameter names at call sites
- **Structure View** - Outline of top-level definitions for quick navigation
- **Rename Refactoring** - Safe rename for symbols across a file
- **Bracket Matching** - Automatic matching and highlighting of parentheses, brackets, and braces
- **Code Folding** - Collapse/expand code blocks for better navigation
- **Smart Typing & Paredit** - Auto-completion of brackets/quotes and structural editing
- **Code Formatting** - Format Phel sources via the external `phel` formatter
- **Semantic Analysis** - Error detection and semantic highlighting
- **File Templates** - Quick creation of new Phel files with proper structure

### Built-in Function Registry

The plugin ships a generated registry of **900+ Phel functions across 35 namespaces** (kept in
sync with the official API via `./gradlew updatePhelRegistry`), including:

- `phel\core` - Core language functions
- `phel\string` - String manipulation functions
- `phel\json`, `phel\edn`, `phel\base64`, `phel\transit` - Encoding/decoding utilities
- `phel\http`, `phel\http-client`, `phel\router` - HTTP and routing helpers
- `phel\html` - HTML generation helpers
- `phel\test`, `phel\test-gen`, `phel\mock` - Testing and property-based testing utilities
- `phel\schema` (and validator/coercer/generator variants) - Data schema tooling
- `phel\debug`, `phel\reflect`, `phel\repl` - Debugging, reflection, and REPL helpers

## Usage

### Getting Started

1. Create or open a `.phel` file
2. The plugin will automatically activate and provide syntax highlighting
3. Start typing to see intelligent code completions
4. Hover over any function to see detailed documentation

## Development

### Building from Source

```bash
git clone git@github.com:phel-lang/phel-intellij-plugin.git
cd intellij-phel-support
./gradlew buildPlugin
```

### Development Setup

1. Clone the repository
2. Open in IntelliJ IDEA (Community or Ultimate)
3. Import as Gradle project
4. Run the plugin with **Gradle → runIde** task

### Testing

Test your changes by running the plugin in a separate IDE instance:

```bash
./gradlew runIde
```

### Project Structure

```txt
src/
├── main/
│   ├── kotlin/org/phellang/          # Main plugin source (Kotlin)
│   │   ├── actions/                  # Contextual menu actions
│   │   ├── annotator/                # Semantic highlighting
│   │   ├── completion/               # Code completion & documentation
│   │   └── ...
│   ├── gen/                          # Generated parser code
│   └── resources/
│       └── META-INF/plugin.xml       # Plugin configuration
└── test/kotlin/org/phellang/         # Test suite
```

### Key Components

#### Language Support

- **PhelLexer** (`.flex`) - Tokenizes Phel source code into tokens
- **PhelParser** (`.bnf`) - Parses tokens into PSI (Program Structure Interface) tree
- **PhelFileType** - Registers `.phel` file type with IntelliJ platform
- **PhelSyntaxHighlighter** - Provides syntax coloring for keywords, strings, numbers, etc.

#### Code Intelligence

- **PhelCompletionContributor** - Intelligent code completion for functions and symbols
- **PhelFunctionRegistry** - Central registry of 900+ Phel functions with full documentation
- **PhelDocumentationProvider** - Hover documentation with examples and deprecation info
- **PhelAnnotator** - Semantic highlighting and error detection

#### Editor Features

- **PhelFoldingBuilder** - Code folding for functions and data structures
- **PhelTypedHandler** - Smart character insertion (auto-closing brackets, quotes)
- **PhelBraceMatcher** - Bracket matching and highlighting
- **PhelCommenter** - Comment/uncomment code blocks

## Technical Details

### Build & Test Commands

Run the test suite:

```bash
./gradlew test
```

Build and verify:

```bash
./gradlew build
```

## Resources

- **Phel Language**: [phel-lang.org](https://phel-lang.org/)
- **Phel API Documentation**: [phel-lang.org/documentation/api/](https://phel-lang.org/documentation/api/)
- **Plugin Page**: [JetBrains Marketplace](https://plugins.jetbrains.com/plugin/28459-phel-lang)
- **GitHub Repository**: [github.com/phel-lang/intellij-phel-support](https://github.com/phel-lang/intellij-phel-support)

## Contributing

Contributions are welcome! Please feel free to submit issues and pull requests.
