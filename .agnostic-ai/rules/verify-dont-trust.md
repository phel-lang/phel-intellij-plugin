---
name: verify-dont-trust
description: Read actual files before making claims about the codebase — never assume, infer, or trust stale docs
alwaysApply: true
---

# Verify, Don't Trust

Factual claims about this codebase must be backed by a file read in *this conversation*. No inferring, guessing, or paraphrasing from memory, issues, PRs, ADRs, or comments.

Treat "is this really X?", "how does this work?", "is this issue valid?" as verify-first. Open files before opining. Trace call chains end-to-end (action → annotator/completion → PSI/parser → registry/api.json). Cite the file+line you actually read.

If not locally verifiable (IntelliJ Platform internals, JFlex/Grammar-Kit runtime behavior, third-party APIs), say so — don't guess. For Phel language semantics, verify against the official docs (https://phel-lang.org/) and `api.json`, not memory.

**The code is law. The code never lies.**

## Anti-patterns

- "Yes, valid issue" without opening cited files
- "This token wins because longest-match" without checking the actual JFlex rule order
- "All completions do X" without grepping `completion/`
- Paraphrasing the SDK docs instead of checking the generated PSI in `src/main/gen/`
- Answering "how does this work?" from path/class name alone
- Asserting a function exists in Phel without checking `api.json` / `NamespaceConfig.kt`
