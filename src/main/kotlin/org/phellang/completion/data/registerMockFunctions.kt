package org.phellang.completion.data

import org.phellang.completion.infrastructure.PhelCompletionPriority

internal fun registerMockFunctions(): List<PhelFunction> = listOf(
    PhelFunction(
        namespace = "mock",
        name = "mock/call-count",
        signature = "(call-count mock-fn)",
        completion = CompletionInfo(
            tailText = "Returns the number of times the mock was called",
            priority = PhelCompletionPriority.MOCK_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """Returns the number of times the mock was called.""",
            example = "(def my-mock (mock :result))",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/mock.phel#L106",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "mock",
        name = "mock/called-once?",
        signature = "(called-once? mock-fn)",
        completion = CompletionInfo(
            tailText = "Returns true if the mock was called exactly once",
            priority = PhelCompletionPriority.PREDICATE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """Returns true if the mock was called exactly once.""",
            example = "(def my-mock (mock :result))",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/mock.phel#L125",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "mock",
        name = "mock/called-times?",
        signature = "(called-times? mock-fn n)",
        completion = CompletionInfo(
            tailText = "Returns true if the mock was called exactly n times",
            priority = PhelCompletionPriority.PREDICATE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """Returns true if the mock was called exactly n times.""",
            example = "(def my-mock (mock :result))",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/mock.phel#L131",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "mock",
        name = "mock/called-with?",
        signature = "(called-with? mock-fn & expected-args)",
        completion = CompletionInfo(
            tailText = "Returns true if the mock was called with the exact arguments",
            priority = PhelCompletionPriority.PREDICATE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """Returns true if the mock was called with the exact arguments.""",
            example = "(def my-mock (mock :result))",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/mock.phel#L118",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "mock",
        name = "mock/called?",
        signature = "(called? mock-fn)",
        completion = CompletionInfo(
            tailText = "Returns true if the mock was called at least once",
            priority = PhelCompletionPriority.PREDICATE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """Returns true if the mock was called at least once.""",
            example = "(def my-mock (mock :result))",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/mock.phel#L112",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "mock",
        name = "mock/calls",
        signature = "(calls mock-fn)",
        completion = CompletionInfo(
            tailText = "Returns a list of all argument lists the mock was called with",
            priority = PhelCompletionPriority.MOCK_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """Returns a list of all argument lists the mock was called with.""",
            example = "(def my-mock (mock :result))",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/mock.phel#L97",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "mock",
        name = "mock/clear-all-mocks!",
        signature = "(clear-all-mocks! )",
        completion = CompletionInfo(
            tailText = "Clears the entire mock registry.  Useful for cleanup between test suites in long-running processes",
            priority = PhelCompletionPriority.MOCK_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """Clears the entire mock registry. Useful for cleanup between test suites in long-running processes.""",
            example = "(clear-all-mocks!) ; All mocks removed from registry",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/mock.phel#L168",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "mock",
        name = "mock/first-call",
        signature = "(first-call mock-fn)",
        completion = CompletionInfo(
            tailText = "Returns the arguments from the first call",
            priority = PhelCompletionPriority.MOCK_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """Returns the arguments from the first call.""",
            example = "(def my-mock (mock :result))",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/mock.phel#L149",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "mock",
        name = "mock/last-call",
        signature = "(last-call mock-fn)",
        completion = CompletionInfo(
            tailText = "Returns the arguments from the most recent call",
            priority = PhelCompletionPriority.MOCK_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """Returns the arguments from the most recent call.""",
            example = "(def my-mock (mock :result))",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/mock.phel#L143",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "mock",
        name = "mock/mock",
        signature = "(mock return-value)",
        completion = CompletionInfo(
            tailText = "Creates a mock function that returns a fixed value and tracks all calls",
            priority = PhelCompletionPriority.MOCK_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """Creates a mock function that returns a fixed value and tracks all calls.""",
            example = "(def my-mock (mock :return-value))",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/mock.phel#L29",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "mock",
        name = "mock/mock-fn",
        signature = "(mock-fn f)",
        completion = CompletionInfo(
            tailText = "Creates a mock function with custom behavior that tracks all calls",
            priority = PhelCompletionPriority.MOCK_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """Creates a mock function with custom behavior that tracks all calls.""",
            example = "(def my-mock (mock-fn (fn [x] (* x 2))))",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/mock.phel#L40",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "mock",
        name = "mock/mock-returning",
        signature = "(mock-returning values)",
        completion = CompletionInfo(
            tailText = "Creates a mock that returns different values for consecutive calls.  After exhausting values, returns the last value",
            priority = PhelCompletionPriority.MOCK_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """Creates a mock that returns different values for consecutive calls. After exhausting values, returns the last value.""",
            example = "(def my-mock (mock-returning [1 2 3]))",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/mock.phel#L57",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "mock",
        name = "mock/mock-throwing",
        signature = "(mock-throwing exception)",
        completion = CompletionInfo(
            tailText = "Creates a mock that throws an exception when called",
            priority = PhelCompletionPriority.MOCK_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """Creates a mock that throws an exception when called.""",
            example = "(def my-mock (mock-throwing (php/new \\RuntimeException \"API unavailable\")))",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/mock.phel#L77",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "mock",
        name = "mock/mock?",
        signature = "(mock? f)",
        completion = CompletionInfo(
            tailText = "Returns true if the function is a mock",
            priority = PhelCompletionPriority.PREDICATE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """Returns true if the function is a mock.""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/mock.phel#L92",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "mock",
        name = "mock/never-called?",
        signature = "(never-called? mock-fn)",
        completion = CompletionInfo(
            tailText = "Returns true if the mock was never called",
            priority = PhelCompletionPriority.PREDICATE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """Returns true if the mock was never called.""",
            example = "(def my-mock (mock :result))",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/mock.phel#L137",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "mock",
        name = "mock/reset-mock!",
        signature = "(reset-mock! mock-fn)",
        completion = CompletionInfo(
            tailText = "Resets the call history of a mock without removing it from the registry.  The mock can continue to be used and track new calls",
            priority = PhelCompletionPriority.MOCK_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """Resets the call history of a mock without removing it from the registry. The mock can continue to be used and track new calls.""",
            example = "(def my-mock (mock :result))",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/mock.phel#L155",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "mock",
        name = "mock/spy",
        signature = "(spy f)",
        completion = CompletionInfo(
            tailText = "Wraps an existing function to track calls while preserving original behavior",
            priority = PhelCompletionPriority.MOCK_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """Wraps an existing function to track calls while preserving original behavior.""",
            example = "(def original-fn (fn [x] (* x 2)))",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/mock.phel#L51",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "mock",
        name = "mock/with-mock-wrapper",
        signature = "(with-mock-wrapper bindings & body)",
        completion = CompletionInfo(
            tailText = "Like with-mocks but for wrapped mocks (interop scenarios). Automatically resets the underlying mock even when wrapped in an adapter function",
            priority = PhelCompletionPriority.MOCK_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Like with-mocks but for wrapped mocks (interop scenarios). Automatically resets the underlying mock even when wrapped in an adapter function.
<br /></br />
Usage:
<br /><pre><code>
(with-mock-wrapper [fn-symbol underlying-mock wrapper-fn]
  body...)
</code></pre><br />
Multiple wrappers: 
<br /><pre><code>
(with-mock-wrapper [service-a mock-a (fn [x] (mock-a (inc x)))
  service-b mock-b (fn [y] (mock-b (dec y)))]
  ...)
</code></pre>
""",
            example = "(with-mock-wrapper [http mock-http identity] (http \"test\"))",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/mock.phel#L215",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "mock",
        name = "mock/with-mocks",
        signature = "(with-mocks bindings & body)",
        completion = CompletionInfo(
            tailText = "Temporarily replaces functions with mocks using binding. Automatically resets mocks after the body executes",
            priority = PhelCompletionPriority.MOCK_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Temporarily replaces functions with mocks using binding. Automatically resets mocks after the body executes.
<br />
Works with inline mock creation:
<pre><code>
(with-mocks [http-get (mock {:status 200})]
  (http-get)
  # Mock is automatically reset after this block)
</code></pre><br />
Also works with pre-defined mocks:
<pre><code>
(let [my-mock (mock :result)]
  (with-mocks [some-fn my-mock]
  (some-fn)))
</code></pre><br />
If you need to wrap the mock in a function (e.g., to adapt arguments), you'll need to manually reset: 
<pre><code>
(with-mocks [some-fn (fn [& args] (my-mock (transform args)))]
  (some-fn)
  (reset-mock! my-mock))
</code></pre>
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/mock.phel#L179",
                docs = "",
            ),
        ),
    ),
)
