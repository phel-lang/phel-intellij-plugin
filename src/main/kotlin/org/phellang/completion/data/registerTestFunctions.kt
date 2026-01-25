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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/test.phel#L203",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/test.phel#L186",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/test.phel#L294",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/test.phel#L33",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/test.phel#L331",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/test.phel#L341",
                docs = "",
            ),
        ),
    )
)
