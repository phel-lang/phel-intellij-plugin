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

## Usage

### Getting Started

1. Create or open a `.phel` file
2. The plugin will automatically activate and provide syntax highlighting
3. Start typing to see intelligent code completions

## Development

### Building from Source

```bash
git clone https://github.com/phel-lang/intellij-phel-support.git
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

- `src/main/java/org/phellang/` - Main plugin source code
- `src/main/resources/META-INF/plugin.xml` - Plugin configuration
- `src/main/java/org/phellang/language/` - Lexer and parser definitions
- `src/main/java/org/phellang/completion/` - Code completion providers

### Key Components

- **PhelLexer** (`.flex`) - Tokenizes Phel source code
- **PhelParser** (`.bnf`) - Parses tokens into PSI tree
- **PhelFileType** - Registers `.phel` file type with IntelliJ
- **PhelCompletionContributor** - Provides code completion
- **PhelDocumentationProvider** - Hover documentation and quick info
- **PhelAnnotator** - Semantic highlighting and error detection
- **PhelSyntaxHighlighter** - Basic syntax coloring
- **PhelFoldingBuilder** - Code folding support
- **PhelTypedHandler** - Smart character insertion
- **PhelBraceMatcher** - Bracket matching

## Contributing

Contributions are welcome! Please feel free to submit issues and pull requests.
