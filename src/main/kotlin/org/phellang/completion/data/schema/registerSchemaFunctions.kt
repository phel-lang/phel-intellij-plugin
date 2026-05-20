package org.phellang.completion.data.schema

import org.phellang.completion.data.CompletionInfo
import org.phellang.completion.data.DocumentationInfo
import org.phellang.completion.data.DocumentationLinks
import org.phellang.completion.data.DeprecationInfo
import org.phellang.completion.data.PhelFunction

import org.phellang.completion.infrastructure.PhelCompletionPriority

internal fun registerSchemaFunctions(): List<PhelFunction> = listOf(
    PhelFunction(
        namespace = "schema",
        name = "schema/coerce",
        signature = "(coerce schema value)",
        completion = CompletionInfo(
            tailText = "Walks schema and coerces string-shaped input into schema-required types",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Walks <code>schema</code> and coerces string-shaped input into schema-required<br />
  types. Idempotent for already-typed values.
""",
            example = "(coerce :int \"42\") ; =&gt; 42",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.39.0/src/phel/schema.phel#L84",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "schema",
        name = "schema/conform",
        signature = "(conform schema value)",
        completion = CompletionInfo(
            tailText = "Coerces value against schema",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Coerces <code>value</code> against <code>schema</code>. Returns the coerced value on<br />
  success, otherwise <code>:phel.schema/invalid</code>.
""",
            example = "(conform :int \"42\") ; =&gt; 42",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.39.0/src/phel/schema.phel#L92",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "schema",
        name = "schema/deref-ref",
        signature = "(deref-ref name)",
        completion = CompletionInfo(
            tailText = "Returns the schema registered under name, or nil",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns the schema registered under <code>name</code>, or <code>nil</code>.
""",
            example = "(deref-ref :email)",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.39.0/src/phel/schema.phel#L138",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "schema",
        name = "schema/explain",
        signature = "(explain schema value)",
        completion = CompletionInfo(
            tailText = "Returns nil when value conforms to schema",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns <code>nil</code> when <code>value</code> conforms to <code>schema</code>. On mismatch returns<br />
  <code>{:schema schema :value value :errors [...]}</code> with one error per<br />
  violation, each carrying <code>:path</code>, <code>:in</code>, <code>:schema</code>, <code>:value</code>, <code>:type</code>.
""",
            example = "(explain :int :oops)",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.39.0/src/phel/schema.phel#L67",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "schema",
        name = "schema/generate",
        signature = "(generate schema)",
        completion = CompletionInfo(
            tailText = "Generates a single value conforming to schema",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Generates a single value conforming to <code>schema</code>. Accepts <code>:size</code> and<br />
  <code>:seed</code> options.
""",
            example = "(generate :int)",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.39.0/src/phel/schema.phel#L105",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "schema",
        name = "schema/human-readable-explain",
        signature = "(human-readable-explain result)",
        completion = CompletionInfo(
            tailText = "Renders an explain result as a multi-line human string",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Renders an <code>explain</code> result as a multi-line human string. Returns<br />
  <code>nil</code> for a <code>nil</code> input.
""",
            example = "(human-readable-explain (explain :int :oops))",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.39.0/src/phel/schema.phel#L76",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "schema",
        name = "schema/instrument!",
        signature = "(instrument! name f schema)",
        completion = CompletionInfo(
            tailText = "Registers f wrapped with schema under name",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Registers <code>f</code> wrapped with <code>schema</code> under <code>name</code>. Returns the wrapped<br />
  function. The original is preserved so <code>unstrument!</code> can restore it.
""",
            example = "(instrument! :add add [:=&gt; [:int :int] :int])",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.39.0/src/phel/schema.phel#L193",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "schema",
        name = "schema/instrumented?",
        signature = "(instrumented? name)",
        completion = CompletionInfo(
            tailText = "Returns true if name is currently instrumented",
            priority = PhelCompletionPriority.PREDICATE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns <code>true</code> if <code>name</code> is currently instrumented.
""",
            example = "(instrumented? :add)",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.39.0/src/phel/schema.phel#L208",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "schema",
        name = "schema/invalid-marker",
        signature = "",
        completion = CompletionInfo(
            tailText = "Sentinel returned by conform when a value cannot be made to fit a schema",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Sentinel returned by <code>conform</code> when a value cannot be made to fit a<br />
  schema.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.39.0/src/phel/schema.phel#L100",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "schema",
        name = "schema/register!",
        signature = "(register! name schema)",
        completion = CompletionInfo(
            tailText = "Registers schema under name in the global schema registry",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Registers <code>schema</code> under <code>name</code> in the global schema registry.
""",
            example = "(register! :email [:and :string [:re \"/@/\"]])",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.39.0/src/phel/schema.phel#L124",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "schema",
        name = "schema/registered?",
        signature = "(registered? name)",
        completion = CompletionInfo(
            tailText = "Returns true if a schema is registered under name",
            priority = PhelCompletionPriority.PREDICATE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns <code>true</code> if a schema is registered under <code>name</code>.
""",
            example = "(registered? :email)",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.39.0/src/phel/schema.phel#L144",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "schema",
        name = "schema/schema->gen",
        signature = "(schema->gen schema)",
        completion = CompletionInfo(
            tailText = "Returns the phel\\test\\gen generator associated with schema",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns the <code>phel\test\gen</code> generator associated with <code>schema</code>.
""",
            example = "(schema-&gt;gen :int)",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.39.0/src/phel/schema.phel#L113",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "schema",
        name = "schema/schema-args",
        signature = "(schema-args schema)",
        completion = CompletionInfo(
            tailText = "Returns the positional arguments of schema (children past the head and optional options map)",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns the positional arguments of <code>schema</code> (children past the head<br />
  and optional options map).
""",
            example = "(schema-args [:vector :int]) ; =&gt; [:int]",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.39.0/src/phel/schema.phel#L43",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "schema",
        name = "schema/schema-check?",
        signature = "(schema-check?)",
        completion = CompletionInfo(
            tailText = "Returns true when runtime validation performed by the instrument helpers is currently enabled",
            priority = PhelCompletionPriority.PREDICATE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns <code>true</code> when runtime validation performed by the instrument<br />
  helpers is currently enabled.
""",
            example = "(schema-check?) ; =&gt; true",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.39.0/src/phel/schema.phel#L154",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "schema",
        name = "schema/schema-head",
        signature = "(schema-head schema)",
        completion = CompletionInfo(
            tailText = "Returns the dispatch head (kind keyword) of schema",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns the dispatch head (kind keyword) of <code>schema</code>.
""",
            example = "(schema-head [:vector :int]) ; =&gt; :vector",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.39.0/src/phel/schema.phel#L37",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "schema",
        name = "schema/schema-options",
        signature = "(schema-options schema)",
        completion = CompletionInfo(
            tailText = "Returns the options map of schema or {}",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns the options map of <code>schema</code> or <code>{}</code>.
""",
            example = "(schema-options [:map {:closed true} [:k :int]]) ; =&gt; {:closed true}",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.39.0/src/phel/schema.phel#L50",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "schema",
        name = "schema/schema?",
        signature = "(schema? x)",
        completion = CompletionInfo(
            tailText = "Returns true if x has the shape of a schema value",
            priority = PhelCompletionPriority.PREDICATE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns <code>true</code> if <code>x</code> has the shape of a schema value.
""",
            example = "(schema? :int) ; =&gt; true",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.39.0/src/phel/schema.phel#L31",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "schema",
        name = "schema/set-schema-check!",
        signature = "(set-schema-check! enabled?)",
        completion = CompletionInfo(
            tailText = "Enables (true) or disables (false) runtime validation",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Enables (<code>true</code>) or disables (<code>false</code>) runtime validation. Returns<br />
  the new value.
""",
            example = "(set-schema-check! false)",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.39.0/src/phel/schema.phel#L162",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "schema",
        name = "schema/unregister!",
        signature = "(unregister! name)",
        completion = CompletionInfo(
            tailText = "Removes the schema bound to name",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Removes the schema bound to <code>name</code>. Returns <code>nil</code>.
""",
            example = "(unregister! :email)",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.39.0/src/phel/schema.phel#L131",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "schema",
        name = "schema/unstrument!",
        signature = "(unstrument! name)",
        completion = CompletionInfo(
            tailText = "Removes the instrumentation registered under name and returns the original, unwrapped function (o...",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Removes the instrumentation registered under <code>name</code> and returns the<br />
  original, unwrapped function (or <code>nil</code> if no entry).
""",
            example = "(unstrument! :add)",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.39.0/src/phel/schema.phel#L201",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "schema",
        name = "schema/validate",
        signature = "(validate schema value)",
        completion = CompletionInfo(
            tailText = "Returns true if value conforms to schema, otherwise false",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns <code>true</code> if <code>value</code> conforms to <code>schema</code>, otherwise <code>false</code>.
""",
            example = "(validate :int 1) ; =&gt; true",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.39.0/src/phel/schema.phel#L60",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "schema",
        name = "schema/with-schema-check",
        signature = "(with-schema-check enabled? f)",
        completion = CompletionInfo(
            tailText = "Invokes zero-arg thunk f with runtime validation forced to enabled",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Invokes zero-arg thunk <code>f</code> with runtime validation forced to<br />
  <code>enabled?</code>. The previous value is restored on return or exception.
""",
            example = "(with-schema-check false (fn [] (risky-fn)))",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.39.0/src/phel/schema.phel#L170",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "schema",
        name = "schema/wrap-with-function-schema",
        signature = "(wrap-with-function-schema f schema)",
        completion = CompletionInfo(
            tailText = "Wraps f with the [:=> args ret] function schema",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Wraps <code>f</code> with the <code>[:=> args ret]</code> function schema.
""",
            example = "(wrap-with-function-schema add [:=&gt; [:int :int] :int])",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.39.0/src/phel/schema.phel#L186",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "schema",
        name = "schema/wrap-with-schema",
        signature = "(wrap-with-schema f arg-schema return-schema)",
        completion = CompletionInfo(
            tailText = "Wraps f so calls validate arguments and return values against the supplied schemas",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Wraps <code>f</code> so calls validate arguments and return values against the<br />
  supplied schemas.
""",
            example = "(wrap-with-schema add [:int :int] :int)",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.39.0/src/phel/schema.phel#L178",
                docs = "",
            ),
        ),
    )
)
