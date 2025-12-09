package org.phellang.completion.data

import org.phellang.completion.infrastructure.PhelCompletionPriority

internal fun registerJsonFunctions(): List<DataFunction> = listOf(
    DataFunction(
        namespace = "json",
        name = "json/decode",
        doc = """Decodes a JSON string to a Phel value.""",
        signature = "(decode json & [{:flags flags, :depth depth}])",
        description = """Decodes a JSON string to a Phel value""",
        githubUrl = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/json.phel#L55",
        docUrl = "",
        meta = FunctionMeta(
            example = "(decode \"{\\\"name\\\":\\\"Alice\\\"}\") ; => {:name \"Alice\"}",
            deprecatedVersion = null,
            supersededBy = null,
        ),
        priority = PhelCompletionPriority.JSON_FUNCTIONS,
    ),
    DataFunction(
        namespace = "json",
        name = "json/decode-value",
        doc = """Converts a JSON value to Phel format.""",
        signature = "(decode-value x)",
        description = """Converts a JSON value to Phel format""",
        githubUrl = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/json.phel#L43",
        docUrl = "",
        meta = FunctionMeta(
            example = "(decode-value [1 2 3]) ; => [1 2 3]",
            deprecatedVersion = null,
            supersededBy = null,
        ),
        priority = PhelCompletionPriority.JSON_FUNCTIONS,
    ),
    DataFunction(
        namespace = "json",
        name = "json/encode",
        doc = """Encodes a Phel value to a JSON string.""",
        signature = "(encode value & [{:flags flags, :depth depth}])",
        description = """Encodes a Phel value to a JSON string""",
        githubUrl = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/json.phel#L31",
        docUrl = "",
        meta = FunctionMeta(
            example = "(encode {:name \"Alice\"}) ; => \"{\\\"name\\\":\\\"Alice\\\"}\"",
            deprecatedVersion = null,
            supersededBy = null,
        ),
        priority = PhelCompletionPriority.JSON_FUNCTIONS,
    ),
    DataFunction(
        namespace = "json",
        name = "json/encode-value",
        doc = """Converts a Phel value to JSON-compatible format.""",
        signature = "(encode-value x)",
        description = """Converts a Phel value to JSON-compatible format""",
        githubUrl = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/json.phel#L20",
        docUrl = "",
        meta = FunctionMeta(
            example = "(encode-value :name) ; => \"name\"",
            deprecatedVersion = null,
            supersededBy = null,
        ),
        priority = PhelCompletionPriority.JSON_FUNCTIONS,
    ),
    DataFunction(
        namespace = "json",
        name = "json/valid-key?",
        doc = """Checks if a value can be used as a JSON key.""",
        signature = "(valid-key? v)",
        description = """Checks if a value can be used as a JSON key""",
        githubUrl = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/json.phel#L4",
        docUrl = "",
        meta = FunctionMeta(
            example = "(valid-key? :name) ; => true",
            deprecatedVersion = null,
            supersededBy = null,
        ),
        priority = PhelCompletionPriority.PREDICATE_FUNCTIONS,
    ),
)
