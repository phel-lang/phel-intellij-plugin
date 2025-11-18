package org.phellang.completion.data

import org.phellang.completion.infrastructure.PhelCompletionPriority

internal fun registerJsonFunctions(): List<DataFunction> = listOf(
    DataFunction("json/decode", "(decode json & [{:flags flags, :depth depth}])", PhelCompletionPriority.JSON_FUNCTIONS, "json", "Decodes a JSON string to a Phel value", """
<br /><code>(decode json & [<code>{:flags flags, :depth depth}</code>])</code><br /><br />
Decodes a JSON string to a Phel value.<br />
<br />
  <pre><code>(decode \"{\\"name\\":\\"Alice\\"}\")<br /># => <code>{:name \"Alice\"}</code></code></pre>
<br />
"""),
    DataFunction("json/decode-value", "(decode-value x)", PhelCompletionPriority.JSON_FUNCTIONS, "json", "Converts a JSON value to Phel format", """
<br /><code>(decode-value x)</code><br /><br />
Converts a JSON value to Phel format.<br />
<br />
  <pre><code>(decode-value [1 2 3])<br /># => [1 2 3]</code></pre>
<br />
"""),
    DataFunction("json/encode", "(encode value & [{:flags flags, :depth depth}])", PhelCompletionPriority.JSON_FUNCTIONS, "json", "Encodes a Phel value to a JSON string", """
<br /><code>(encode value & [<code>{:flags flags, :depth depth}</code>])</code><br /><br />
Encodes a Phel value to a JSON string.<br />
<br />
  <pre><code>(encode <code>{:name \"Alice\"}</code>)<br /># => \"{\\"name\\":\\"Alice\\"}\"</code></pre>
<br />
"""),
    DataFunction("json/encode-value", "(encode-value x)", PhelCompletionPriority.JSON_FUNCTIONS, "json", "Converts a Phel value to JSON-compatible format", """
<br /><code>(encode-value x)</code><br /><br />
Converts a Phel value to JSON-compatible format.<br />
<br />
  <pre><code>(encode-value :name)<br /># => \"name\"</code></pre>
<br />
"""),
    DataFunction("json/valid-key?", "(valid-key? v)", PhelCompletionPriority.JSON_FUNCTIONS, "json", "Checks if a value can be used as a JSON key", """
<br /><code>(valid-key? v)</code><br /><br />
Checks if a value can be used as a JSON key.<br />
<br />
  <pre><code>(valid-key? :name)<br /># => true</code></pre>
<br />
"""),
)
