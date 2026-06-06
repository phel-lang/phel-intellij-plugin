package org.phellang.completion.data.schema

import org.phellang.completion.data.CompletionInfo
import org.phellang.completion.data.DocumentationInfo
import org.phellang.completion.data.DocumentationLinks
import org.phellang.completion.data.DeprecationInfo
import org.phellang.completion.data.PhelFunction

import org.phellang.completion.infrastructure.PhelCompletionPriority

internal fun registerSchemaInstrumentFunctions(): List<PhelFunction> = listOf(
    PhelFunction(
        namespace = "schema.instrument",
        name = "schema.instrument/clear-instrumented!",
        signature = "(clear-instrumented!)",
        completion = CompletionInfo(
            tailText = "Removes every instrumentation entry",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Removes every instrumentation entry. Returns <code>nil</code>.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.42.0/src/phel/schema/instrument.phel#L160",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "schema.instrument",
        name = "schema.instrument/function-schema?",
        signature = "(function-schema? schema)",
        completion = CompletionInfo(
            tailText = "Returns true if schema is a function schema of the shape [:=> args-schema return-schema]",
            priority = PhelCompletionPriority.PREDICATE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns <code>true</code> if <code>schema</code> is a function schema of the shape<br />
  <code>[:=> args-schema return-schema]</code>.
""",
            example = "(function-schema? [:=&gt; [:int] :int])",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.42.0/src/phel/schema/instrument.phel#L60",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "schema.instrument",
        name = "schema.instrument/instrument!",
        signature = "(instrument! name f schema)",
        completion = CompletionInfo(
            tailText = "Registers f under name (any key) wrapped with schema",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Registers <code>f</code> under <code>name</code> (any key) wrapped with <code>schema</code>. Returns the wrapped fn. Subsequent calls to <code>unstrument!</code> with the same name can restore the original via <code>(instrumented-original name)</code>.
""",
            example = "(def add* (instrument! :add add [:=&gt; [:int :int] :int]))",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.42.0/src/phel/schema/instrument.phel#L128",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "schema.instrument",
        name = "schema.instrument/instrumented-original",
        signature = "(instrumented-original name)",
        completion = CompletionInfo(
            tailText = "Returns the original, unwrapped function associated with name, or nil if no instrumentation is re...",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns the original, unwrapped function associated with <code>name</code>, or<br />
  <code>nil</code> if no instrumentation is registered under <code>name</code>.
""",
            example = "(instrumented-original :add)",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.42.0/src/phel/schema/instrument.phel#L152",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "schema.instrument",
        name = "schema.instrument/instrumented?",
        signature = "(instrumented? name)",
        completion = CompletionInfo(
            tailText = "Returns true if name currently has an instrumentation entry",
            priority = PhelCompletionPriority.PREDICATE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns <code>true</code> if <code>name</code> currently has an instrumentation entry.
""",
            example = "(instrumented? :add)",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.42.0/src/phel/schema/instrument.phel#L146",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "schema.instrument",
        name = "schema.instrument/schema-check?",
        signature = "(schema-check?)",
        completion = CompletionInfo(
            tailText = "Returns true when runtime validation is enabled",
            priority = PhelCompletionPriority.PREDICATE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns <code>true</code> when runtime validation is enabled.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.42.0/src/phel/schema/instrument.phel#L26",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "schema.instrument",
        name = "schema.instrument/schema?",
        signature = "(schema? x)",
        completion = CompletionInfo(
            tailText = "Returns true if x has the shape of a schema value (a keyword or a vector whose head is a keyword,...",
            priority = PhelCompletionPriority.PREDICATE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns <code>true</code> if <code>x</code> has the shape of a schema value (a keyword or a<br />
  vector whose head is a keyword, including the <code>:=></code> function marker).
""",
            example = "(schema? :int) ; =&gt; true",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.42.0/src/phel/schema/instrument.phel#L49",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "schema.instrument",
        name = "schema.instrument/set-schema-check!",
        signature = "(set-schema-check! enabled?)",
        completion = CompletionInfo(
            tailText = "Enables (true) or disables (false) runtime validation performed by wrap-with-schema/instrument",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Enables (<code>true</code>) or disables (<code>false</code>) runtime validation performed by<br />
  <code>wrap-with-schema</code>/<code>instrument!</code>. Returns the new value.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.42.0/src/phel/schema/instrument.phel#L31",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "schema.instrument",
        name = "schema.instrument/unstrument!",
        signature = "(unstrument! name)",
        completion = CompletionInfo(
            tailText = "Unregisters the instrumentation bound to name and returns the original (unwrapped) function if pr...",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Unregisters the instrumentation bound to <code>name</code> and returns the original (unwrapped) function if present, otherwise <code>nil</code>.
""",
            example = "(unstrument! :add)",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.42.0/src/phel/schema/instrument.phel#L137",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "schema.instrument",
        name = "schema.instrument/with-schema-check",
        signature = "(with-schema-check enabled? f)",
        completion = CompletionInfo(
            tailText = "Runs thunk f with runtime validation set to enabled",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Runs thunk <code>f</code> with runtime validation set to <code>enabled?</code>, restoring the previous value even if <code>f</code> throws.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.42.0/src/phel/schema/instrument.phel#L37",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "schema.instrument",
        name = "schema.instrument/wrap-with-function-schema",
        signature = "(wrap-with-function-schema f schema)",
        completion = CompletionInfo(
            tailText = "Convenience: accepts a [:=> args-schema return-schema] schema vector and returns f wrapped accord...",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Convenience: accepts a <code>[:=> args-schema return-schema]</code> schema vector<br />
  and returns <code>f</code> wrapped accordingly.
""",
            example = "(wrap-with-function-schema add [:=&gt; [:int :int] :int])",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.42.0/src/phel/schema/instrument.phel#L116",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "schema.instrument",
        name = "schema.instrument/wrap-with-schema",
        signature = "(wrap-with-schema f arg-schema return-schema)",
        completion = CompletionInfo(
            tailText = "Wraps f so each call validates its arguments against arg-schema (a vector of per-arg schemas) and...",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Wraps <code>f</code> so each call validates its arguments against <code>arg-schema</code><br />
  (a vector of per-arg schemas) and its return value against<br />
  <code>return-schema</code>. When <code>schema-check?</code> returns <code>false</code> at call time<br />
  the wrapper short-circuits and invokes <code>f</code> directly, skipping<br />
  validation on both sides.
""",
            example = "(wrap-with-schema add [:int :int] :int)",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.42.0/src/phel/schema/instrument.phel#L98",
                docs = "",
            ),
        ),
    )
)
