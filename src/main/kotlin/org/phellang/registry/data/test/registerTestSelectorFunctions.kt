package org.phellang.registry.data.test

import org.phellang.registry.CompletionInfo
import org.phellang.registry.DocumentationInfo
import org.phellang.registry.DocumentationLinks
import org.phellang.registry.DeprecationInfo
import org.phellang.registry.PhelFunction

import org.phellang.registry.PhelCompletionPriority

internal fun registerTestSelectorFunctions(): List<PhelFunction> = listOf(
    PhelFunction(
        namespace = "test.selector",
        name = "test.selector/has-selectors?",
        signature = "(has-selectors? options)",
        completion = CompletionInfo(
            tailText = "Returns true when options contains at least one non-empty selector key",
            priority = PhelCompletionPriority.PREDICATE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns <code>true</code> when <code>options</code> contains at least one non-empty selector key.
""",
            example = "(has-selectors? {:include [:integration]})",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/test/selector.phel#L205",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "test.selector",
        name = "test.selector/has-tag?",
        signature = "(has-tag? meta tag)",
        completion = CompletionInfo(
            tailText = "Returns true when meta carries tag (a keyword or a string treated as a keyword)",
            priority = PhelCompletionPriority.PREDICATE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns <code>true</code> when <code>meta</code> carries <code>tag</code> (a keyword or a string treated as a keyword). Single-keyword metadata (<code>^:integration</code>) and map-based multi-tag metadata (<code>^{:tags [:integration :slow]}</code>) both match.
""",
            example = "(has-tag? {:integration true} :integration)",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/test/selector.phel#L54",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "test.selector",
        name = "test.selector/keep-test?",
        signature = "(keep-test? options ns-name meta)",
        completion = CompletionInfo(
            tailText = "Returns true when the test described by meta and ns-name should be run under options",
            priority = PhelCompletionPriority.PREDICATE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns <code>true</code> when the test described by <code>meta</code> and <code>ns-name</code> should be run under <code>options</code>. <code>options</code> is a map that may carry any of <code>:include</code>, <code>:exclude</code>, <code>:ns-patterns</code>, <code>:filters</code>. Selectors that are absent or empty impose no restriction; specified selectors are AND'd together.
""",
            example = "(keep-test? {:include [:integration]} \"my.ns\" {:integration true :test-name \"t\"})",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/test/selector.phel#L188",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "test.selector",
        name = "test.selector/matches-exclude?",
        signature = "(matches-exclude? excludes meta)",
        completion = CompletionInfo(
            tailText = "Returns true when any tag in excludes is truthy on meta",
            priority = PhelCompletionPriority.PREDICATE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns <code>true</code> when any tag in <code>excludes</code> is truthy on <code>meta</code>. An empty or nil <code>excludes</code> vector always returns <code>false</code> (nothing excluded).
""",
            example = "(matches-exclude? [:slow] {:slow true})",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/test/selector.phel#L78",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "test.selector",
        name = "test.selector/matches-filter?",
        signature = "(matches-filter? patterns test-name)",
        completion = CompletionInfo(
            tailText = "Returns true when patterns is empty (no restriction) or when any entry in patterns matches test-name",
            priority = PhelCompletionPriority.PREDICATE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns <code>true</code> when <code>patterns</code> is empty (no restriction) or when any entry in <code>patterns</code> matches <code>test-name</code>.
""",
            example = "(matches-filter? [\"add-\"] \"test-add-one\")",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/test/selector.phel#L176",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "test.selector",
        name = "test.selector/matches-include?",
        signature = "(matches-include? includes meta)",
        completion = CompletionInfo(
            tailText = "Returns true when includes is empty (no restriction) or when any tag in includes is truthy on meta",
            priority = PhelCompletionPriority.PREDICATE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns <code>true</code> when <code>includes</code> is empty (no restriction) or when any tag in <code>includes</code> is truthy on <code>meta</code>.
""",
            example = "(matches-include? [:integration] {:integration true})",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/test/selector.phel#L70",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "test.selector",
        name = "test.selector/matches-ns?",
        signature = "(matches-ns? patterns ns-name)",
        completion = CompletionInfo(
            tailText = "Returns true when patterns is empty (no restriction) or when any glob in patterns matches ns-name",
            priority = PhelCompletionPriority.PREDICATE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns <code>true</code> when <code>patterns</code> is empty (no restriction) or when any glob in <code>patterns</code> matches <code>ns-name</code>.
""",
            example = "(matches-ns? [\"phel.http.*\"] \"phel.http.client\")",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/test/selector.phel#L145",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "test.selector",
        name = "test.selector/name-matches?",
        signature = "(name-matches? pattern test-name)",
        completion = CompletionInfo(
            tailText = "Returns true when pattern (a PCRE /",
            priority = PhelCompletionPriority.PREDICATE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns <code>true</code> when <code>pattern</code> (a PCRE <code>/.../</code> string or a plain substring) matches <code>test-name</code>.
""",
            example = "(name-matches? \"add-\" \"test-add-one\")",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/test/selector.phel#L167",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "test.selector",
        name = "test.selector/ns-matches?",
        signature = "(ns-matches? pattern ns-name)",
        completion = CompletionInfo(
            tailText = "Returns true when ns-name matches the glob pattern",
            priority = PhelCompletionPriority.PREDICATE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns <code>true</code> when <code>ns-name</code> matches the glob <code>pattern</code>. Supports<br />
  <code>*</code> (single segment) and <code>**</code> (any run). Backslash-separated<br />
  namespaces (<code>phel\http\client</code>) are treated as dotted<br />
  (<code>phel.http.client</code>) so globs stay portable.
""",
            example = "(ns-matches? \"phel.http.*\" \"phel\\\\http\\\\client\")",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/test/selector.phel#L132",
                docs = "",
            ),
        ),
    )
)
