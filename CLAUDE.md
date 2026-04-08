# Phel IntelliJ Plugin

IntelliJ IDEA plugin for the [Phel programming language](https://phel-lang.org/) — a functional Lisp that transpiles to PHP, inspired by Clojure and Janet.

- **Phel is NOT Clojure** — always verify syntax against [official Phel docs](https://phel-lang.org/)
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
