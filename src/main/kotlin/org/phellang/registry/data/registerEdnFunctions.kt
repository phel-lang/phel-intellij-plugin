package org.phellang.registry.data

import org.phellang.registry.CompletionInfo
import org.phellang.registry.DocumentationInfo
import org.phellang.registry.DocumentationLinks
import org.phellang.registry.DeprecationInfo
import org.phellang.registry.PhelFunction

import org.phellang.registry.PhelCompletionPriority

internal fun registerEdnFunctions(): List<PhelFunction> = listOf(
    PhelFunction(
        namespace = "edn",
        name = "edn/read-string",
        signature = "(read-string s)\n(read-string s opts)",
        completion = CompletionInfo(
            tailText = "Reads one EDN value from string s",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Reads one EDN value from string <code>s</code>.<br /><br />
When <code>s</code> is empty or only whitespace/comments, returns the <code>:eof</code> value<br />
  from <code>opts</code> (default nil).<br /><br />
Options:<br />
    :readers  map of tag (symbol, keyword, or string) to 1-arg handler fn<br />
    :eof      value returned for empty input
""",
            example = "(read-string \"{:a 1 :b 2}\") ; =&gt; {:a 1, :b 2}\n(read-string \"\" {:eof :default}) ; =&gt; :default",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.48.0/src/phel/edn.phel#L105",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "edn",
        name = "edn/read-string-all",
        signature = "(read-string-all s)\n(read-string-all s opts)",
        completion = CompletionInfo(
            tailText = "Reads every EDN value from string s and returns them as a vector",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Reads every EDN value from string <code>s</code> and returns them as a vector.<br /><br />
Options:<br />
    :readers  map of tag (symbol, keyword, or string) to 1-arg handler fn
""",
            example = "(read-string-all \"1 2 3\") ; =&gt; [1 2 3]",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.48.0/src/phel/edn.phel#L123",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "edn",
        name = "edn/write-string",
        signature = "(write-string value)",
        completion = CompletionInfo(
            tailText = "Serialises value to its EDN string representation",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Serialises <code>value</code> to its EDN string representation. Uses Phel's readable printer so the output round-trips through <code>read-string</code> for all EDN-compatible values (nil, booleans, numbers, strings, keywords, symbols, lists, vectors, maps, sets, and built-in tagged literals such as <code>#uuid</code>).
""",
            example = "(write-string {:a 1}) ; =&gt; \"{:a 1}\"",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.48.0/src/phel/edn.phel#L137",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "edn",
        name = "edn/write-string-all",
        signature = "(write-string-all xs)",
        completion = CompletionInfo(
            tailText = "Serialises a sequence of values to EDN, separating top-level forms with a single space",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Serialises a sequence of values to EDN, separating top-level forms with a single space. Inverse of <code>read-string-all</code>.
""",
            example = "(write-string-all [1 2 3]) ; =&gt; \"1 2 3\"\n(write-string-all [1 \"hi\" :kw]) ; =&gt; \"1 \\\"hi\\\" :kw\"",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.48.0/src/phel/edn.phel#L144",
                docs = "",
            ),
        ),
    )
)
