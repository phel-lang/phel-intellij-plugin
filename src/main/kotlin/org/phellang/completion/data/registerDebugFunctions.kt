package org.phellang.completion.data

import org.phellang.completion.infrastructure.PhelCompletionPriority

internal fun registerDebugFunctions(): List<DataFunction> = listOf(
    DataFunction("debug/dbg", "(dbg expr)", PhelCompletionPriority.DEBUG_FUNCTIONS, "debug", "Evaluates an expression and prints it with its result", """
<br /><code>(dbg expr)</code><br /><br />
Evaluates an expression and prints it with its result.<br />
<br />
  <pre><code>(dbg (+ 1 2))<br /># Prints: (+ 1 2) => 3<br /># => 3</code></pre>
<br />
"""),
    DataFunction("debug/dotrace", "(dotrace name f)", PhelCompletionPriority.DEBUG_FUNCTIONS, "debug", "Wraps a function to print each call and result with indentation", """
<br /><code>(dotrace name f)</code><br /><br />
Wraps a function to print each call and result with indentation.<br />
<br />
  <pre><code>(def add (dotrace \"add\" +))<br />(add 2 3)<br /># Prints: TRACE t01: (add 2 3)<br /># Prints: TRACE t01: => 5</code></pre>
<br />
"""),
    DataFunction("debug/reset-trace-state!", "(reset-trace-state! )", PhelCompletionPriority.DEBUG_FUNCTIONS, "debug", "Resets trace counters to initial values", """
<br /><code>(reset-trace-state! )</code><br /><br />
Resets trace counters to initial values.<br />
<br />
  <pre><code>(reset-trace-state!)<br /># Trace IDs restart from 01</code></pre>
<br />
"""),
    DataFunction("debug/set-trace-id-padding!", "(set-trace-id-padding! estimated-id-padding)", PhelCompletionPriority.DEBUG_FUNCTIONS, "debug", "Sets the number of digits for trace ID padding", """
<br /><code>(set-trace-id-padding! estimated-id-padding)</code><br /><br />
Sets the number of digits for trace ID padding.<br />
<br />
  <pre><code>(set-trace-id-padding! 3)<br /># Uses 3 digits: 001, 002, etc.</code></pre>
<br />
"""),
    DataFunction("debug/spy", "(spy expr)", PhelCompletionPriority.DEBUG_FUNCTIONS, "debug", "Evaluates an expression and prints it with an optional label", """
<br /><code>(spy expr)</code><br /><br />
Evaluates an expression and prints it with an optional label.<br />
<br />
  <pre><code>(spy (+ 1 2))<br /># Prints: SPY (+ 1 2) => 3<br /># => 3</code></pre>
<br />
"""),
    DataFunction("debug/tap", "(tap value)", PhelCompletionPriority.DEBUG_FUNCTIONS, "debug", "Prints a value and returns it unchanged. Useful in pipelines", """
<br /><code>(tap value)</code><br /><br />
Prints a value and returns it unchanged. Useful in pipelines.<br />
<br />
  <pre><code>(-> 5 (tap) (* 2))<br /># Prints: TAP => 5<br /># => 10</code></pre>
<br />
"""),
)
