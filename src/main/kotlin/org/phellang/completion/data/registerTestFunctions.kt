package org.phellang.completion.data

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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/test.phel#L18",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/test.phel#L321",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "test",
        name = "test/assert-expr",
        signature = "(assert-expr & args)",
        completion = CompletionInfo(
            tailText = "",
            priority = PhelCompletionPriority.TEST_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/test.phel#L214",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "test",
        name = "test/assert-expr-methods",
        signature = "",
        completion = CompletionInfo(
            tailText = "",
            priority = PhelCompletionPriority.TEST_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/test.phel#L214",
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
            summary = "Defines a test function.",
            example = "(deftest test-add)",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/test.phel#L274",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/test.phel#L73",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/test.phel#L546",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/test.phel#L257",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "test",
        name = "test/print-summary",
        signature = "(print-summary)",
        completion = CompletionInfo(
            tailText = "Prints test results summary",
            priority = PhelCompletionPriority.TEST_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Prints test results summary.",
            example = "(print-summary)",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/test.phel#L421",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "test",
        name = "test/report",
        signature = "(report data)",
        completion = CompletionInfo(
            tailText = "Records test results and prints status indicators",
            priority = PhelCompletionPriority.TEST_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Records test results and prints status indicators.",
            example = "(report {:state :pass})",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/test.phel#L46",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/test.phel#L528",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/test.phel#L552",
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
            summary = "Runs all tests in the given namespaces.",
            example = "(run-tests {} 'my-app\test)",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/test.phel#L516",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/test.phel#L539",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/test.phel#L286",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/test.phel#L462",
                docs = "",
            ),
        ),
    )
)
