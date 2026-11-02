---
name: verify-dont-trust
description: Read actual files before making claims about the codebase.
alwaysApply: true
---

# Verify, don't trust

Back every claim about this codebase with a file read in this conversation, not memory, issues, PRs or comments. Trace the chain (action -> annotator/completion -> PSI/parser -> registry/api.json) and cite file and line. Say so when something cannot be verified locally (Platform internals, JFlex/Grammar-Kit runtime, third-party APIs). Check Phel semantics against https://phel-lang.org/ and `api.json`.

Do not: call an issue valid without opening the cited files; claim a token wins by longest-match without reading the JFlex rule order; say "all completions do X" without grepping `completion/`; assert a Phel function exists without checking `api.json` or `NamespaceConfig.kt`.
