---
description: Rules for IntelliJ IDEA plugin development for the Phel language
appliesTo: ["**/*.java", "**/*.kt", "**/*.xml", "build.gradle.kts"]
tags: ["intellij", "plugin", "gradle"]
priority: high
---

## Project Context

This project is an IntelliJ IDEA plugin for the [Phel programming language](https://phel-lang.org/).  
For core plugin SDK guidance, always refer to the **JetBrains Plugin SDK**:

- [JetBrains Plugin SDK](https://plugins.jetbrains.com/docs/intellij/welcome.html)

Use **Playwright MCP** to fetch SDK pages when detailed guidance is needed.

## Technical Implementation Guidelines

### Build System

- Gradle with Kotlin DSL (`build.gradle.kts`)
- IntelliJ Platform Gradle Plugin 2.x
- Java 21 toolchain
- IntelliJ IDEA 2024.2.5 platform

### Lexer and Parser (JFlex + Grammar-Kit)

- Lexer: `src/main/kotlin/org/phellang/language/Phel.flex`
- Grammar: `src/main/kotlin/org/phellang/language/Phel.bnf`
- Generated files: `src/main/gen/org/phellang/language/`
- Always refer to official Phel docs (`phel-docs.md`) when verifying grammar or syntax rules

### Plugin Registration (plugin.xml)

Always register new features in `META-INF/plugin.xml`:

```xml
<lang.syntaxHighlighterFactory language="Phel" implementationClass="..."/>
<lang.parserDefinition language="Phel" implementationClass="..."/>
<lang.commenter language="Phel" implementationClass="..."/>
<colorSettingsPage implementation="..."/>
<annotator language="Phel" implementationClass="..."/>
<completion.contributor language="Phel" implementationClass="..."/>
```

## Best Practices and Error Prevention

### Common Pitfalls

- **Namespace completion**: Use `PlainPrefixMatcher` for full `namespace/function` highlighting
- **Form comments**: Implement both PSI-based and text-based detection for robustness
- **PSI element types**: Use `PhelTypes.SYMBOL` not `PhelTypes.SYM` in completion logic
- **Template priorities**: Balance completion priorities to show `()` first, then templates
- **Don't assume Clojure syntax** - always verify against official Phel docs (`phel-docs.md`)
- **Regenerate lexer/parser** after `.flex`/`.bnf` changes
- **Always use `PhelTypes.TOKEN_NAME`** in lexer returns
- **Clear Gradle caches** if encountering build issues
