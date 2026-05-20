package org.phellang.completion.data.test

import org.phellang.completion.data.CompletionInfo
import org.phellang.completion.data.DocumentationInfo
import org.phellang.completion.data.DocumentationLinks
import org.phellang.completion.data.DeprecationInfo
import org.phellang.completion.data.PhelFunction

import org.phellang.completion.infrastructure.PhelCompletionPriority

internal fun registerTestFunctions(): List<PhelFunction> = listOf(
    PhelFunction(
        namespace = "test",
        name = "test/*testing-contexts*",
        signature = "",
        completion = CompletionInfo(
            tailText = "Stack of testing context strings, most recent first",
            priority = PhelCompletionPriority.TEST_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Stack of testing context strings, most recent first.",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.39.0/src/phel/test.phel#L21",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "test",
        name = "test/add-reporter!",
        signature = "(add-reporter! reporter-fn)",
        completion = CompletionInfo(
            tailText = "Appends reporter-fn to the active reporter set",
            priority = PhelCompletionPriority.TEST_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Appends <code>reporter-fn</code> to the active reporter set. Returns the updated<br />
  reporter list.
""",
            example = "(add-reporter! tap-reporter)",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.39.0/src/phel/test.phel#L53",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "test",
        name = "test/are",
        signature = "(are argv expr & args)",
        completion = CompletionInfo(
            tailText = "Checks multiple assertions with a template expression",
            priority = PhelCompletionPriority.MACROS,
        ),
        documentation = DocumentationInfo(
            summary = """
Checks multiple assertions with a template expression.<br />
  <code>argv</code> is a vector of template variables, <code>expr</code> is the assertion template,<br />
  and the remaining <code>args</code> are partitioned by <code>(count argv)</code> to fill the template.<br />
  Template variables are substituted lexically at macro-expansion time, so<br />
  literal collection cells (e.g. <code>()</code>, <code>[]</code>, <code>{}</code>) are preserved as data and<br />
  are not evaluated as code.
""",
            example = "(are [x y] (= x y)\n  2 (+ 1 1)\n  4 (* 2 2))",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.39.0/src/phel/test.phel#L446",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "test",
        name = "test/assert-expr",
        signature = "(assert-expr & __phel_4389)",
        completion = CompletionInfo(
            tailText = "",
            priority = PhelCompletionPriority.TEST_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.39.0/src/phel/test.phel#L330",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "test",
        name = "test/clear-reporters!",
        signature = "(clear-reporters!)",
        completion = CompletionInfo(
            tailText = "Removes every registered reporter",
            priority = PhelCompletionPriority.TEST_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Removes every registered reporter. Returns an empty vector.",
            example = "(clear-reporters!)",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.39.0/src/phel/test.phel#L61",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "test",
        name = "test/deftest",
        signature = "(deftest test-name & body)",
        completion = CompletionInfo(
            tailText = "Defines a test function",
            priority = PhelCompletionPriority.MACROS,
        ),
        documentation = DocumentationInfo(
            summary = """
Defines a test function.<br /><br />
Metadata attached to <code>test-name</code> is forwarded to the defined function<br />
  so selectors can inspect it at runtime. <code>^:integration</code> (shorthand<br />
  for <code>{:integration true}</code>) and <code>^{:tags [:integration :slow]}</code><br />
  multi-tag maps are both honoured.
""",
            example = "(deftest test-add)",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.39.0/src/phel/test.phel#L391",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "test",
        name = "test/do-report",
        signature = "(do-report m)",
        completion = CompletionInfo(
            tailText = "Add file and line information to a test result and call report",
            priority = PhelCompletionPriority.TEST_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Add file and line information to a test result and call report.<br />
  If you are writing a custom assert-expr method, call this function<br />
  to pass test results to report.
""",
            example = "(do-report {:state :pass :type :any :message \"ok\"})",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.39.0/src/phel/test.phel#L189",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "test",
        name = "test/get-failed-tests",
        signature = "(get-failed-tests)",
        completion = CompletionInfo(
            tailText = "Returns the names (ns/test-name) of tests that failed or errored in the last run",
            priority = PhelCompletionPriority.TEST_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns the names (<code>ns/test-name</code>) of tests that failed or errored<br />
  in the last run. Used by <code>--parallel</code> orchestration to aggregate<br />
  last-failed lists across worker processes.
""",
            example = "(get-failed-tests) ; =&gt; [\"my-app/foo-test\"]",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.39.0/src/phel/test.phel#L1484",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "test",
        name = "test/get-reporters",
        signature = "(get-reporters)",
        completion = CompletionInfo(
            tailText = "Returns the currently registered reporter functions as a vector",
            priority = PhelCompletionPriority.TEST_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Returns the currently registered reporter functions as a vector.",
            example = "(get-reporters)",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.39.0/src/phel/test.phel#L68",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "test",
        name = "test/get-stats",
        signature = "(get-stats)",
        completion = CompletionInfo(
            tailText = "Returns the current test statistics as a hash-map with :failed and :counts keys",
            priority = PhelCompletionPriority.TEST_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns the current test statistics as a hash-map with :failed and :counts keys.
""",
            example = "(get-stats) ; =&gt; {:failed [] :counts {:failed 0 :error 0 :pass 0 :total 0}}",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.39.0/src/phel/test.phel#L1472",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "test",
        name = "test/is",
        signature = "(is form & [message])",
        completion = CompletionInfo(
            tailText = "Asserts that an expression is true",
            priority = PhelCompletionPriority.MACROS,
        ),
        documentation = DocumentationInfo(
            summary = "Asserts that an expression is true.",
            example = "(is (= 4 (+ 2 2)))",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.39.0/src/phel/test.phel#L373",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "test",
        name = "test/print-summary",
        signature = "(print-summary)",
        completion = CompletionInfo(
            tailText = "Emits the :summary event to the active reporter set",
            priority = PhelCompletionPriority.TEST_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Emits the <code>:summary</code> event to the active reporter set.<br />
  Kept for backwards compatibility; prefer letting <code>run-tests</code> emit the<br />
  event at the end of the run.
""",
            example = "(print-summary)",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.39.0/src/phel/test.phel#L1095",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "test",
        name = "test/register-reporter!",
        signature = "(register-reporter! name reporter-fn)",
        completion = CompletionInfo(
            tailText = "Registers a custom reporter function under name (keyword or keyword-castable string)",
            priority = PhelCompletionPriority.TEST_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Registers a custom reporter function under <code>name</code> (keyword or<br />
  keyword-castable string). Returns <code>name</code>.
""",
            example = "(register-reporter! :my-reporter (fn [event] ...))",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.39.0/src/phel/test.phel#L1062",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "test",
        name = "test/report",
        signature = "(report & __phel_4331)",
        completion = CompletionInfo(
            tailText = "Records a test-framework event and dispatches it to the active reporters",
            priority = PhelCompletionPriority.TEST_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Records a test-framework event and dispatches it to the active<br />
  reporters. <code>data</code> must contain a <code>:type</code> key (<code>:pass</code>, <code>:failed</code>,<br />
  <code>:error</code>, <code>:begin-test-ns</code>, <code>:end-test-ns</code>, <code>:begin-test-run</code>,<br />
  <code>:summary</code>, or a user-defined event). The default methods for<br />
  assertion outcomes update the internal stats and invoke every<br />
  registered reporter. Other event types flow straight through to the<br />
  reporter set. Extend by registering<br />
  <code>(defmethod report :custom-type [event] ...)</code>.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.39.0/src/phel/test.phel#L128",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "test",
        name = "test/reset-stats",
        signature = "(reset-stats)",
        completion = CompletionInfo(
            tailText = "Resets the test statistics to their initial state",
            priority = PhelCompletionPriority.TEST_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Resets the test statistics to their initial state.<br />
  Call this before running a new batch of tests to get fresh results.
""",
            example = "(reset-stats)",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.39.0/src/phel/test.phel#L1458",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "test",
        name = "test/resolve-reporter",
        signature = "(resolve-reporter name)",
        completion = CompletionInfo(
            tailText = "Returns the reporter function registered for name (keyword or string)",
            priority = PhelCompletionPriority.TEST_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns the reporter function registered for <code>name</code> (keyword or<br />
  string). Checks user-registered reporters before the built-in set.<br />
  Returns <code>nil</code> if the name is unknown.
""",
            example = "(resolve-reporter :junit-xml)",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.39.0/src/phel/test.phel#L1051",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "test",
        name = "test/restore-stats",
        signature = "(restore-stats saved)",
        completion = CompletionInfo(
            tailText = "Restores test statistics from a previously saved state",
            priority = PhelCompletionPriority.TEST_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Restores test statistics from a previously saved state.",
            example = "(restore-stats saved)",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.39.0/src/phel/test.phel#L1478",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "test",
        name = "test/run-tests",
        signature = "(run-tests options & namespaces)",
        completion = CompletionInfo(
            tailText = "Runs all tests in the given namespaces",
            priority = PhelCompletionPriority.TEST_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Runs all tests in the given namespaces. When <code>:list-only</code> is true,<br />
  prints the discovered tests and skips execution.<br /><br />
Recognized option keys include <code>:filter</code>, <code>:filters</code>, <code>:include</code>,<br />
  <code>:exclude</code>, <code>:ns-patterns</code>, <code>:fail-fast</code>, <code>:stack-trace</code>, <code>:reporters</code>,<br />
  <code>:junit-output</code>, <code>:list-only</code>, <code>:only-tests</code>, <code>:last-failed-file</code>,<br />
  <code>:slowest</code>, <code>:repeat</code> (run the selected tests N times), <code>:seed</code><br />
  (integer seed for the order RNG), and <code>:random-order</code> (shuffle tests<br />
  per namespace).
""",
            example = "(run-tests {} 'my-app\\test)",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.39.0/src/phel/test.phel#L1439",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "test",
        name = "test/set-junit-output!",
        signature = "(set-junit-output! path)",
        completion = CompletionInfo(
            tailText = "Configures the output path the JUnit reporter writes to",
            priority = PhelCompletionPriority.TEST_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Configures the output path the JUnit reporter writes to. When <code>nil</code>,<br />
  the XML is printed to stdout.
""",
            example = "(set-junit-output! \"build/junit.xml\")",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.39.0/src/phel/test.phel#L1025",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "test",
        name = "test/set-reporters!",
        signature = "(set-reporters! reporters)",
        completion = CompletionInfo(
            tailText = "Replaces the active reporter set with reporters (a sequence of single-argument functions)",
            priority = PhelCompletionPriority.TEST_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Replaces the active reporter set with <code>reporters</code> (a sequence of<br />
  single-argument functions). Returns the new reporter list.
""",
            example = "(set-reporters! [my-reporter-fn])",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.39.0/src/phel/test.phel#L45",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "test",
        name = "test/successful?",
        signature = "(successful?)",
        completion = CompletionInfo(
            tailText = "Checks if all tests passed",
            priority = PhelCompletionPriority.PREDICATE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Checks if all tests passed.",
            example = "(successful?) # =&gt; true",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.39.0/src/phel/test.phel#L1465",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "test",
        name = "test/testing",
        signature = "(testing context & body)",
        completion = CompletionInfo(
            tailText = "Adds a testing context string",
            priority = PhelCompletionPriority.MACROS,
        ),
        documentation = DocumentationInfo(
            summary = """
Adds a testing context string. Used inside deftest to describe a group of assertions.<br />
  The context string is prepended to failure messages for better diagnostics.
""",
            example = "(deftest test-math\n  (testing \"addition\"\n    (is (= 2 (+ 1 1)))))",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.39.0/src/phel/test.phel#L411",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "test",
        name = "test/use-fixtures",
        signature = "(use-fixtures fixture-type & fns)",
        completion = CompletionInfo(
            tailText = "Registers fixture functions for the current namespace",
            priority = PhelCompletionPriority.TEST_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Registers fixture functions for the current namespace.<br /><br />
<code>fixture-type</code> is either <code>:each</code> (wraps every individual test) or<br />
  <code>:once</code> (wraps the whole run in a single function). Each fixture is<br />
  a function of one argument — a thunk <code>(fn [])</code> representing the tests<br />
  to run — and is expected to invoke that thunk somewhere in its body.<br /><br />
Calling <code>use-fixtures</code> with no fixture functions removes all fixtures<br />
  of that type previously registered on the namespace.
""",
            example = "(use-fixtures :once (fn [t] (setup) (t) (teardown)))",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.39.0/src/phel/test.phel#L1124",
                docs = "",
            ),
        ),
    )
)
