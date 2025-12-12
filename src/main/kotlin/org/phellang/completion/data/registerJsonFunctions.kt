package org.phellang.completion.data

import org.phellang.completion.infrastructure.PhelCompletionPriority

internal fun registerJsonFunctions(): List<PhelFunction> = listOf(
    PhelFunction(
        namespace = "json",
        name = "json/decode",
        signature = "(decode json & [{:flags flags, :depth depth}])",
        completion = CompletionInfo(
            tailText = "Decodes a JSON string to a Phel value",
            priority = PhelCompletionPriority.JSON_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """Decodes a JSON string to a Phel value.""",
            example = "(decode \"{\\\"name\\\":\\\"Alice\\\"}\") ; => {:name \"Alice\"}",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/json.phel#L55",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "json",
        name = "json/decode-value",
        signature = "(decode-value x)",
        completion = CompletionInfo(
            tailText = "Converts a JSON value to Phel format",
            priority = PhelCompletionPriority.JSON_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """Converts a JSON value to Phel format.""",
            example = "(decode-value [1 2 3]) ; => [1 2 3]",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/json.phel#L43",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "json",
        name = "json/encode",
        signature = "(encode value & [{:flags flags, :depth depth}])",
        completion = CompletionInfo(
            tailText = "Encodes a Phel value to a JSON string",
            priority = PhelCompletionPriority.JSON_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """Encodes a Phel value to a JSON string.""",
            example = "(encode {:name \"Alice\"}) ; => \"{\\\"name\\\":\\\"Alice\\\"}\"",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/json.phel#L31",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "json",
        name = "json/encode-value",
        signature = "(encode-value x)",
        completion = CompletionInfo(
            tailText = "Converts a Phel value to JSON-compatible format",
            priority = PhelCompletionPriority.JSON_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """Converts a Phel value to JSON-compatible format.""",
            example = "(encode-value :name) ; => \"name\"",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/json.phel#L20",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "json",
        name = "json/valid-key?",
        signature = "(valid-key? v)",
        completion = CompletionInfo(
            tailText = "Checks if a value can be used as a JSON key",
            priority = PhelCompletionPriority.PREDICATE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """Checks if a value can be used as a JSON key.""",
            example = "(valid-key? :name) ; => true",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/json.phel#L4",
                docs = "",
            ),
        ),
    ),
)
