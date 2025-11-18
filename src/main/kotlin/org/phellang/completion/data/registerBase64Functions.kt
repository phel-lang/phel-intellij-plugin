package org.phellang.completion.data

import org.phellang.completion.infrastructure.PhelCompletionPriority

internal fun registerBase64Functions(): List<DataFunction> = listOf(
    DataFunction("base64/decode", "(decode s & [strict?])", PhelCompletionPriority.BASE64_FUNCTIONS, "base64", "Decodes a Base64 string. Optional `strict?` flag validates characters", """
<br /><code>(decode s & [strict?])</code><br /><br />
Decodes a Base64 string. Optional <b>strict?</b> flag validates characters.<br />
<br />
  <pre><code>(decode \"SGVsbG8=\")<br /># => \"Hello\"</code></pre>
<br />
"""),
    DataFunction("base64/decode-url", "(decode-url s & [strict?])", PhelCompletionPriority.BASE64_FUNCTIONS, "base64", "Decodes a URL-safe Base64 string. Adds padding automatically", """
<br /><code>(decode-url s & [strict?])</code><br /><br />
Decodes a URL-safe Base64 string. Adds padding automatically.<br />
<br />
  <pre><code>(decode-url \"SGVsbG8\")<br /># => \"Hello\"</code></pre>
<br />
"""),
    DataFunction("base64/encode", "(encode s)", PhelCompletionPriority.BASE64_FUNCTIONS, "base64", "Encodes a string to Base64", """
<br /><code>(encode s)</code><br /><br />
Encodes a string to Base64.<br />
<br />
  <pre><code>(encode \"Hello\")<br /># => \"SGVsbG8=\"</code></pre>
<br />
"""),
    DataFunction("base64/encode-url", "(encode-url s)", PhelCompletionPriority.BASE64_FUNCTIONS, "base64", "Encodes a string to URL-safe Base64 (no padding)", """
<br /><code>(encode-url s)</code><br /><br />
Encodes a string to URL-safe Base64 (no padding).<br />
<br />
  <pre><code>(encode-url \"Hello\")<br /># => \"SGVsbG8\"</code></pre>
<br />
"""),
)
