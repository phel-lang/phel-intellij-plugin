package org.phellang.completion.data

import org.phellang.completion.infrastructure.PhelCompletionPriority

internal fun registerReplFunctions(): List<DataFunction> = listOf(
    DataFunction("repl/build-facade", "", PhelCompletionPriority.REPL_FUNCTIONS, "repl", "", """
<br />
<br />
"""),
    DataFunction("repl/compile-str", "(compile-str s)", PhelCompletionPriority.REPL_FUNCTIONS, "repl", "Compiles a Phel expression string to PHP code", """
<br /><code>(compile-str s)</code><br /><br />
Compiles a Phel expression string to PHP code.<br />
<br />
  <pre><code>(compile-str \"(+ 1 2)\")<br /># => \"(1 + 2)\"</code></pre>
<br />
"""),
    DataFunction("repl/doc", "(doc sym)", PhelCompletionPriority.REPL_FUNCTIONS, "repl", "Prints the documentation for the given symbol", """
<br /><code>(doc sym)</code><br /><br />
Prints the documentation for the given symbol.<br />
<br />
  <pre><code>(doc map)<br /># Prints: \"Applies function f to each element...\"</code></pre>
<br />
"""),
    DataFunction("repl/loaded-namespaces", "(loaded-namespaces )", PhelCompletionPriority.REPL_FUNCTIONS, "repl", "Returns all namespaces currently loaded in the REPL", """
<br /><code>(loaded-namespaces )</code><br /><br />
Returns all namespaces currently loaded in the REPL.<br />
<br />
  <pre><code>(loaded-namespaces)<br /># => [\"phel\core\" \"phel\repl\"]</code></pre>
<br />
"""),
    DataFunction("repl/print-colorful", "(print-colorful & xs)", PhelCompletionPriority.REPL_FUNCTIONS, "repl", "Prints arguments with colored output", """
<br /><code>(print-colorful & xs)</code><br /><br />
Prints arguments with colored output.<br />
<br />
  <pre><code>(print-colorful [1 2 3])<br /># Prints: [1 2 3] (with color)</code></pre>
<br />
"""),
    DataFunction("repl/println-colorful", "(println-colorful & xs)", PhelCompletionPriority.REPL_FUNCTIONS, "repl", "Prints arguments with colored output followed by a newline", """
<br /><code>(println-colorful & xs)</code><br /><br />
Prints arguments with colored output followed by a newline.<br />
<br />
  <pre><code>(println-colorful [1 2 3])<br /># Prints: [1 2 3]<br /> (with color)</code></pre>
<br />
"""),
    DataFunction("repl/require", "(require sym & args)", PhelCompletionPriority.REPL_FUNCTIONS, "repl", "Requires a Phel module into the environment", """
<br /><code>(require sym & args)</code><br /><br />
Requires a Phel module into the environment.<br />
<br />
  <pre><code>(require phel\http :as http :refer [request])<br /># => phel\http</code></pre>
<br />
"""),
    DataFunction("repl/resolve", "(resolve sym)", PhelCompletionPriority.REPL_FUNCTIONS, "repl", "Resolves the given symbol in the current environment and returns a resolved Symbol with the absolute namespace or nil if it cannot be resolved", """
<br /><code>(resolve sym)</code><br /><br />
Resolves the given symbol in the current environment and returns a resolved Symbol with the absolute namespace or nil if it cannot be resolved.<br />
<br />
  <pre><code>(resolve 'map)<br /># => phel\core/map</code></pre>
<br />
"""),
    DataFunction("repl/use", "(use sym & args)", PhelCompletionPriority.REPL_FUNCTIONS, "repl", "Adds a use statement to the environment", """
<br /><code>(use sym & args)</code><br /><br />
Adds a use statement to the environment.<br />
<br />
  <pre><code>(use DateTime :as DT)<br /># => DateTime</code></pre>
<br />
"""),
)
