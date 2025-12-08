package org.phellang.completion.data

import org.phellang.completion.infrastructure.PhelCompletionPriority

internal fun registerBase64Functions(): List<DataFunction> = listOf(
    DataFunction(
        namespace = "base64",
        name = "base64/decode",
        doc = """Decodes a Base64 string. Optional <b>strict?</b> flag validates characters.""",
        signature = "(decode s & [strict?])",
        description = """Decodes a Base64 string. Optional strict? flag validates characters""",
        githubUrl = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/base64.phel#L10",
        docUrl = "",
        meta = FunctionMeta(
            example = "(decode \"SGVsbG8=\") ; => \"Hello\"",
            deprecatedVersion = null,
            supersededBy = null,
        ),
        priority = PhelCompletionPriority.BASE64_FUNCTIONS,
    ),
    DataFunction(
        namespace = "base64",
        name = "base64/decode-url",
        doc = """Decodes a URL-safe Base64 string. Adds padding automatically.""",
        signature = "(decode-url s & [strict?])",
        description = """Decodes a URL-safe Base64 string. Adds padding automatically""",
        githubUrl = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/base64.phel#L25",
        docUrl = "",
        meta = FunctionMeta(
            example = "(decode-url \"SGVsbG8\") ; => \"Hello\"",
            deprecatedVersion = null,
            supersededBy = null,
        ),
        priority = PhelCompletionPriority.BASE64_FUNCTIONS,
    ),
    DataFunction(
        namespace = "base64",
        name = "base64/encode",
        doc = """Encodes a string to Base64.""",
        signature = "(encode s)",
        description = """Encodes a string to Base64""",
        githubUrl = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/base64.phel#L4",
        docUrl = "",
        meta = FunctionMeta(
            example = "(encode \"Hello\") ; => \"SGVsbG8=\"",
            deprecatedVersion = null,
            supersededBy = null,
        ),
        priority = PhelCompletionPriority.BASE64_FUNCTIONS,
    ),
    DataFunction(
        namespace = "base64",
        name = "base64/encode-url",
        doc = """Encodes a string to URL-safe Base64 (no padding).""",
        signature = "(encode-url s)",
        description = """Encodes a string to URL-safe Base64 (no padding)""",
        githubUrl = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/base64.phel#L16",
        docUrl = "",
        meta = FunctionMeta(
            example = "(encode-url \"Hello\") ; => \"SGVsbG8\"",
            deprecatedVersion = null,
            supersededBy = null,
        ),
        priority = PhelCompletionPriority.BASE64_FUNCTIONS,
    ),
)
