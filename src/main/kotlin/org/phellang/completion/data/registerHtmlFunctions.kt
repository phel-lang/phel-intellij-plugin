package org.phellang.completion.data

import org.phellang.completion.infrastructure.PhelCompletionPriority

internal fun registerHtmlFunctions(): List<DataFunction> = listOf(
    DataFunction("html/doctype", "(doctype type)", PhelCompletionPriority.HTML_FUNCTIONS, "html", "Returns an HTML doctype declaration", """
<br /><code>(doctype type)</code><br /><br />
Returns an HTML doctype declaration.<br />
<br />
  <pre><code>(doctype :html5)<br /># => \"<!DOCTYPE html>\n\"</code></pre>
<br />
"""),
    DataFunction("html/escape-html", "(escape-html s)", PhelCompletionPriority.HTML_FUNCTIONS, "html", "Escapes HTML special characters to prevent XSS", """
<br /><code>(escape-html s)</code><br /><br />
Escapes HTML special characters to prevent XSS.<br />
<br />
  <pre><code>(escape-html \"<div>\")<br /># => \"&lt;div&gt;\"</code></pre>
<br />
"""),
    DataFunction("html/html", "(html & content)", PhelCompletionPriority.HTML_FUNCTIONS, "html", "Compiles Phel vectors to HTML strings", """
<br /><code>(html & content)</code><br /><br />
Compiles Phel vectors to HTML strings.<br />
<br />
  <pre><code>(html [:div \"Hello\"])<br /># => \"<div>Hello</div>\"</code></pre>
<br />
"""),
    DataFunction("html/raw-string", "(raw-string s)", PhelCompletionPriority.HTML_FUNCTIONS, "html", "Creates a new raw-string struct", """
<br /><code>(raw-string s)</code><br /><br />
Creates a new raw-string struct.<br />
<br />
"""),
    DataFunction("html/raw-string?", "(raw-string? x)", PhelCompletionPriority.HTML_FUNCTIONS, "html", "Checks if `x` is an instance of the raw-string struct", """
<br /><code>(raw-string? x)</code><br /><br />
Checks if <b>x</b> is an instance of the raw-string struct.<br />
<br />
"""),
)
