# Phel IntelliJ Plugin

IntelliJ IDEA plugin for the [Phel programming language](https://phel-lang.org/) — a functional, Lisp-inspired language that compiles to PHP. Inspired by Clojure, Phel brings macros, persistent data structures, and expressive functional idioms to the PHP ecosystem.

- **Phel is (_almost_) Clojure** — always verify syntax against [official Phel docs](https://phel-lang.org/)
- **API reference**: https://phel-lang.org/api.json
- **Plugin SDK**: https://plugins.jetbrains.com/docs/intellij/welcome.html
- **Marketplace**: https://plugins.jetbrains.com/plugin/28459-phel-lang
- **Repository**: https://github.com/phel-lang/intellij-phel-support

## Quick Commands

```bash
./gradlew runIde                # Launch IDE sandbox with the plugin
./gradlew test                  # Run unit + integration tests
./gradlew build                 # Full build
./gradlew generatePhelLexer     # Regenerate lexer from .flex
./gradlew generatePhelParser    # Regenerate parser from .bnf
./gradlew updatePhelRegistry    # Fetch api.json and regenerate function registry
```
