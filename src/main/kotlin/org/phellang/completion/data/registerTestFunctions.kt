package org.phellang.completion.data

import org.phellang.completion.infrastructure.PhelCompletionPriority

internal fun registerTestFunctions(): List<DataFunction> = listOf(
    DataFunction("test/deftest", "(deftest test-name & body)", PhelCompletionPriority.TEST_FUNCTIONS, "test", "Defines a test function", """
<br /><code>(deftest test-name & body)</code><br /><br />
Defines a test function.<br />
<br />
  <pre><code>(deftest test-add<br />  (is (= 4 (+ 2 2))))</code></pre>
<br />
"""),
    DataFunction("test/is", "(is form & [message])", PhelCompletionPriority.TEST_FUNCTIONS, "test", "Asserts that an expression is true", """
<br /><code>(is form & [message])</code><br /><br />
Asserts that an expression is true.<br />
<br />
  <pre><code>(is (= 4 (+ 2 2)))<br /># Passes if equal</code></pre>
<br />
"""),
    DataFunction("test/print-summary", "(print-summary )", PhelCompletionPriority.TEST_FUNCTIONS, "test", "Prints test results summary", """
<br /><code>(print-summary )</code><br /><br />
Prints test results summary.<br />
<br />
  <pre><code>(print-summary)<br /># Prints: Passed: 10, Failed: 2</code></pre>
<br />
"""),
    DataFunction("test/report", "(report data)", PhelCompletionPriority.TEST_FUNCTIONS, "test", "Records test results and prints status indicators", """
<br /><code>(report data)</code><br /><br />
Records test results and prints status indicators.<br />
<br />
  <pre><code>(report <code>{:state :pass}</code>)<br /># Prints: .</code></pre>
<br />
"""),
    DataFunction("test/run-tests", "(run-tests options & namespaces)", PhelCompletionPriority.TEST_FUNCTIONS, "test", "Runs all tests in the given namespaces", """
<br /><code>(run-tests options & namespaces)</code><br /><br />
Runs all tests in the given namespaces.<br />
<br />
  <pre><code>(run-tests {} 'my-app	est)<br /># Runs all tests</code></pre>
<br />
"""),
    DataFunction("test/successful?", "(successful? )", PhelCompletionPriority.TEST_FUNCTIONS, "test", "Checks if all tests passed", """
<br /><code>(successful? )</code><br /><br />
Checks if all tests passed.<br />
<br />
  <pre><code>(successful?)<br /># => true</code></pre>
<br />
"""),
)
