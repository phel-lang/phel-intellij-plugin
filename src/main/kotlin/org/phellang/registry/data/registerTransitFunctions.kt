package org.phellang.registry.data

import org.phellang.registry.CompletionInfo
import org.phellang.registry.DocumentationInfo
import org.phellang.registry.DocumentationLinks
import org.phellang.registry.DeprecationInfo
import org.phellang.registry.PhelFunction

import org.phellang.registry.PhelCompletionPriority

internal fun registerTransitFunctions(): List<PhelFunction> = listOf(
    PhelFunction(
        namespace = "transit",
        name = "transit/read-string",
        signature = "(read-string s)\n(read-string s opts)",
        completion = CompletionInfo(
            tailText = "Reads one Transit+JSON-Verbose value from string s",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Reads one Transit+JSON-Verbose value from string <code>s</code>.<br /><br />
Options:<br />
    :handlers         map of tag-string to 1-arg fn for <code>~#tag</code> extensions<br />
    :default-handler  2-arg fn <code>(fn [tag rep] ...)</code> invoked for any<br />
                      unknown tag (both scalar and array forms). Receives<br />
                      the bare tag name (e.g. "point", ":", "~") and<br />
                      the already-decoded representation.
""",
            example = "(read-string \"[\\\"~:foo\\\", 1]\") ; =&gt; [:foo 1]\n(read-string \"[\\\"~#point\\\", [1, 2]]\" {:handlers {\"point\" (fn [v] v)}}) ; =&gt; [1 2]",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/transit.phel#L182",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "transit",
        name = "transit/write-string",
        signature = "(write-string value)",
        completion = CompletionInfo(
            tailText = "Serialises value to a Transit+JSON-Verbose string",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Serialises <code>value</code> to a Transit+JSON-Verbose string.
""",
            example = "(write-string {:a 1}) ; =&gt; \"[\\\"~#cmap\\\",[\\\"~:a\\\",1]]\"",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/transit.phel#L283",
                docs = "",
            ),
        ),
    )
)
