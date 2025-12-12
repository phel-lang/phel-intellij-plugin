package org.phellang.completion.data

import org.phellang.completion.infrastructure.PhelCompletionPriority

internal fun registerBase64Functions(): List<PhelFunction> = listOf(
    PhelFunction(
        namespace = "base64",
        name = "base64/decode",
        signature = "(decode s & [strict?])",
        completion = CompletionInfo(
            tailText = "Decodes a Base64 string. Optional strict? flag validates characters",
            priority = PhelCompletionPriority.BASE64_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """Decodes a Base64 string. Optional <b>strict?</b> flag validates characters.""",
            example = "(decode \"SGVsbG8=\") ; => \"Hello\"",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/base64.phel#L10",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "base64",
        name = "base64/decode-url",
        signature = "(decode-url s & [strict?])",
        completion = CompletionInfo(
            tailText = "Decodes a URL-safe Base64 string. Adds padding automatically",
            priority = PhelCompletionPriority.BASE64_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """Decodes a URL-safe Base64 string. Adds padding automatically.""",
            example = "(decode-url \"SGVsbG8\") ; => \"Hello\"",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/base64.phel#L25",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "base64",
        name = "base64/encode",
        signature = "(encode s)",
        completion = CompletionInfo(
            tailText = "Encodes a string to Base64",
            priority = PhelCompletionPriority.BASE64_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """Encodes a string to Base64.""",
            example = "(encode \"Hello\") ; => \"SGVsbG8=\"",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/base64.phel#L4",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "base64",
        name = "base64/encode-url",
        signature = "(encode-url s)",
        completion = CompletionInfo(
            tailText = "Encodes a string to URL-safe Base64 (no padding)",
            priority = PhelCompletionPriority.BASE64_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """Encodes a string to URL-safe Base64 (no padding).""",
            example = "(encode-url \"Hello\") ; => \"SGVsbG8\"",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/base64.phel#L16",
                docs = "",
            ),
        ),
    ),
)
