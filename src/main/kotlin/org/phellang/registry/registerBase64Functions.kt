package org.phellang.registry

import org.phellang.registry.PhelCompletionPriority

internal fun registerBase64Functions(): List<PhelFunction> = listOf(
    PhelFunction(
        namespace = "base64",
        name = "base64/decode",
        signature = "(decode s & [strict?])",
        completion = CompletionInfo(
            tailText = "Decodes a Base64 string",
            priority = PhelCompletionPriority.BASE64_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Decodes a Base64 string. When <code>strict?</code> is true, validates that the input contains only characters from the Base64 alphabet and returns false on invalid input (defaults to false).
""",
            example = "(decode \"SGVsbG8=\") ; =&gt; \"Hello\"",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.47.0/src/phel/base64.phel#L10",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "base64",
        name = "base64/decode-url",
        signature = "(decode-url s & [strict?])",
        completion = CompletionInfo(
            tailText = "Decodes a URL-safe Base64 string, adding padding automatically",
            priority = PhelCompletionPriority.BASE64_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Decodes a URL-safe Base64 string, adding padding automatically. When<br />
  <code>strict?</code> is true, validates that the input contains only valid Base64<br />
  characters (defaults to false).
""",
            example = "(decode-url \"SGVsbG8\") ; =&gt; \"Hello\"",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.47.0/src/phel/base64.phel#L25",
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
            summary = "Encodes a string to Base64.",
            example = "(encode \"Hello\") ; =&gt; \"SGVsbG8=\"",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.47.0/src/phel/base64.phel#L4",
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
            summary = "Encodes a string to URL-safe Base64 (no padding).",
            example = "(encode-url \"Hello\") ; =&gt; \"SGVsbG8\"",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.47.0/src/phel/base64.phel#L16",
                docs = "",
            ),
        ),
    )
)
