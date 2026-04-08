package org.phellang.completion.data

import org.phellang.completion.infrastructure.PhelCompletionPriority

internal fun registerTestFunctions(): List<PhelFunction> = listOf(
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/test.phel#L210",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/test.phel#L369",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/test.phel#L193",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/test.phel#L301",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/test.phel#L40",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/test.phel#L351",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/test.phel#L375",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/test.phel#L339",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/test.phel#L362",
                docs = "",
            ),
        ),
    )
)
