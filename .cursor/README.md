# Phel IntelliJ Plugin Cursor Rules

This folder contains Cursor rules and setup for developing IntelliJ IDEA plugins for the [Phel programming language](https://phel-lang.org/).  
It integrates official Phel documentation, IntelliJ plugin guidelines, and testing/build workflows. Playwright MCP is used to fetch documentation pages dynamically for accurate context.

---

## Folder Structure

```txt
.cursor/
    rules/
        communication.md      → Communication style
        intellij-plugin.md    → IntelliJ plugin implementation guidelines
        mcp.json              → Playwright MCP configuration
        phel-docs.md          → Centralized official Phel documentation references
        phel-language.md      → Language specification & compliance rules
        testing-and-build.md  → Testing strategy and Gradle workflow
        README.md
```

---

## Playwright MCP Setup

The `.cursor/mcp.json` should look like this:

```json
{
  "mcpServers": {
    "playwright": {
      "command": "npx",
      "args": ["@playwright/mcp@latest"]
    }
  }
}
```

- Restart Cursor after creating or updating this file
- Verify the Playwright MCP is running in `Settings → Tools & Integrations → MCP`
- All official documentation pages referenced in `phel-docs.md` should be fetched using Playwright MCP to ensure full context

## How to Use the Rules

- **Scope**: Each `.md` file contains rules for a specific context (language, plugin, testing, docs)
- **Scoping**: appliesTo ensures rules apply only to relevant files (`*.phel`, `*.java`, `*.kt`, `build.gradle.kts`, etc.)
- **Best Practice**: Do not duplicate documentation inline; reference phel-docs.md and fetch pages with Playwright MCP


## Master Template Prompt

Use this template for any Phel-related task in Cursor:

```md
Use the following Cursor rule files:

- communication.md
- phel-language.md
- intellij-plugin.md
- testing-and-build.md
- phel-docs.md

Whenever referencing official documentation, always fetch the full page using the Playwright MCP. Do NOT rely on summaries or web search.

Instructions:

1. Clearly identify which documentation section(s) you are using
2. Provide runnable code examples whenever applicable (Phel code, Gradle commands, XML, Java/Kotlin snippets)
3. Include testing guidance or verification steps if relevant
4. Cite official links where appropriate (use the URLs in phel-docs.md)

Task:
{Insert your specific task or question here}

Output format:

- Context Summary: Short description of the docs or rules applied
- Step-by-Step Instructions: Detailed instructions with examples
- Code Snippets: Fully runnable examples where applicable
- Verification / Testing: Steps or files to validate behavior
- References: URLs from official documentation
```

### Example Usage

```md
Task:
Explain how destructuring works for maps and vectors in Phel, provide a runnable example, and a minimal test file.
```
