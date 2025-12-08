package org.phellang.completion.data

import org.phellang.completion.infrastructure.PhelCompletionPriority

internal fun registerMockFunctions(): List<DataFunction> = listOf(
    DataFunction(
        namespace = "mock",
        name = "mock/call-count",
        doc = """Returns the number of times the mock was called.""",
        signature = "(call-count mock-fn)",
        description = """Returns the number of times the mock was called""",
        githubUrl = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/mock.phel#L106",
        docUrl = "",
        meta = FunctionMeta(
            example = "(def my-mock (mock :result))",
            deprecatedVersion = null,
            supersededBy = null,
        ),
        priority = PhelCompletionPriority.MOCK_FUNCTIONS,
    ),
    DataFunction(
        namespace = "mock",
        name = "mock/called-once?",
        doc = """Returns true if the mock was called exactly once.""",
        signature = "(called-once? mock-fn)",
        description = """Returns true if the mock was called exactly once""",
        githubUrl = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/mock.phel#L125",
        docUrl = "",
        meta = FunctionMeta(
            example = "(def my-mock (mock :result))",
            deprecatedVersion = null,
            supersededBy = null,
        ),
        priority = PhelCompletionPriority.PREDICATE_FUNCTIONS,
    ),
    DataFunction(
        namespace = "mock",
        name = "mock/called-times?",
        doc = """Returns true if the mock was called exactly n times.""",
        signature = "(called-times? mock-fn n)",
        description = """Returns true if the mock was called exactly n times""",
        githubUrl = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/mock.phel#L131",
        docUrl = "",
        meta = FunctionMeta(
            example = "(def my-mock (mock :result))",
            deprecatedVersion = null,
            supersededBy = null,
        ),
        priority = PhelCompletionPriority.PREDICATE_FUNCTIONS,
    ),
    DataFunction(
        namespace = "mock",
        name = "mock/called-with?",
        doc = """Returns true if the mock was called with the exact arguments.""",
        signature = "(called-with? mock-fn & expected-args)",
        description = """Returns true if the mock was called with the exact arguments""",
        githubUrl = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/mock.phel#L118",
        docUrl = "",
        meta = FunctionMeta(
            example = "(def my-mock (mock :result))",
            deprecatedVersion = null,
            supersededBy = null,
        ),
        priority = PhelCompletionPriority.PREDICATE_FUNCTIONS,
    ),
    DataFunction(
        namespace = "mock",
        name = "mock/called?",
        doc = """Returns true if the mock was called at least once.""",
        signature = "(called? mock-fn)",
        description = """Returns true if the mock was called at least once""",
        githubUrl = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/mock.phel#L112",
        docUrl = "",
        meta = FunctionMeta(
            example = "(def my-mock (mock :result))",
            deprecatedVersion = null,
            supersededBy = null,
        ),
        priority = PhelCompletionPriority.PREDICATE_FUNCTIONS,
    ),
    DataFunction(
        namespace = "mock",
        name = "mock/calls",
        doc = """Returns a list of all argument lists the mock was called with.""",
        signature = "(calls mock-fn)",
        description = """Returns a list of all argument lists the mock was called with""",
        githubUrl = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/mock.phel#L97",
        docUrl = "",
        meta = FunctionMeta(
            example = "(def my-mock (mock :result))",
            deprecatedVersion = null,
            supersededBy = null,
        ),
        priority = PhelCompletionPriority.MOCK_FUNCTIONS,
    ),
    DataFunction(
        namespace = "mock",
        name = "mock/clear-all-mocks!",
        doc = """Clears the entire mock registry. Useful for cleanup between test suites in long-running processes.""",
        signature = "(clear-all-mocks! )",
        description = """Clears the entire mock registry.  Useful for cleanup between test suites in long-running processes""",
        githubUrl = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/mock.phel#L168",
        docUrl = "",
        meta = FunctionMeta(
            example = "(clear-all-mocks!) ; All mocks removed from registry",
            deprecatedVersion = null,
            supersededBy = null,
        ),
        priority = PhelCompletionPriority.MOCK_FUNCTIONS,
    ),
    DataFunction(
        namespace = "mock",
        name = "mock/first-call",
        doc = """Returns the arguments from the first call.""",
        signature = "(first-call mock-fn)",
        description = """Returns the arguments from the first call""",
        githubUrl = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/mock.phel#L149",
        docUrl = "",
        meta = FunctionMeta(
            example = "(def my-mock (mock :result))",
            deprecatedVersion = null,
            supersededBy = null,
        ),
        priority = PhelCompletionPriority.MOCK_FUNCTIONS,
    ),
    DataFunction(
        namespace = "mock",
        name = "mock/last-call",
        doc = """Returns the arguments from the most recent call.""",
        signature = "(last-call mock-fn)",
        description = """Returns the arguments from the most recent call""",
        githubUrl = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/mock.phel#L143",
        docUrl = "",
        meta = FunctionMeta(
            example = "(def my-mock (mock :result))",
            deprecatedVersion = null,
            supersededBy = null,
        ),
        priority = PhelCompletionPriority.MOCK_FUNCTIONS,
    ),
    DataFunction(
        namespace = "mock",
        name = "mock/mock",
        doc = """Creates a mock function that returns a fixed value and tracks all calls.""",
        signature = "(mock return-value)",
        description = """Creates a mock function that returns a fixed value and tracks all calls""",
        githubUrl = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/mock.phel#L29",
        docUrl = "",
        meta = FunctionMeta(
            example = "(def my-mock (mock :return-value))",
            deprecatedVersion = null,
            supersededBy = null,
        ),
        priority = PhelCompletionPriority.MOCK_FUNCTIONS,
    ),
    DataFunction(
        namespace = "mock",
        name = "mock/mock-fn",
        doc = """Creates a mock function with custom behavior that tracks all calls.""",
        signature = "(mock-fn f)",
        description = """Creates a mock function with custom behavior that tracks all calls""",
        githubUrl = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/mock.phel#L40",
        docUrl = "",
        meta = FunctionMeta(
            example = "(def my-mock (mock-fn (fn [x] (* x 2))))",
            deprecatedVersion = null,
            supersededBy = null,
        ),
        priority = PhelCompletionPriority.MOCK_FUNCTIONS,
    ),
    DataFunction(
        namespace = "mock",
        name = "mock/mock-returning",
        doc = """Creates a mock that returns different values for consecutive calls. After exhausting values, returns the last value.""",
        signature = "(mock-returning values)",
        description = """Creates a mock that returns different values for consecutive calls.  After exhausting values, returns the last value""",
        githubUrl = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/mock.phel#L57",
        docUrl = "",
        meta = FunctionMeta(
            example = "(def my-mock (mock-returning [1 2 3]))",
            deprecatedVersion = null,
            supersededBy = null,
        ),
        priority = PhelCompletionPriority.MOCK_FUNCTIONS,
    ),
    DataFunction(
        namespace = "mock",
        name = "mock/mock-throwing",
        doc = """Creates a mock that throws an exception when called.""",
        signature = "(mock-throwing exception)",
        description = """Creates a mock that throws an exception when called""",
        githubUrl = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/mock.phel#L77",
        docUrl = "",
        meta = FunctionMeta(
            example = "(def my-mock (mock-throwing (php/new \\RuntimeException \"API unavailable\")))",
            deprecatedVersion = null,
            supersededBy = null,
        ),
        priority = PhelCompletionPriority.MOCK_FUNCTIONS,
    ),
    DataFunction(
        namespace = "mock",
        name = "mock/mock?",
        doc = """Returns true if the function is a mock.""",
        signature = "(mock? f)",
        description = """Returns true if the function is a mock""",
        githubUrl = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/mock.phel#L92",
        docUrl = "",
        meta = FunctionMeta(
            example = null,
            deprecatedVersion = null,
            supersededBy = null,
        ),
        priority = PhelCompletionPriority.PREDICATE_FUNCTIONS,
    ),
    DataFunction(
        namespace = "mock",
        name = "mock/never-called?",
        doc = """Returns true if the mock was never called.""",
        signature = "(never-called? mock-fn)",
        description = """Returns true if the mock was never called""",
        githubUrl = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/mock.phel#L137",
        docUrl = "",
        meta = FunctionMeta(
            example = "(def my-mock (mock :result))",
            deprecatedVersion = null,
            supersededBy = null,
        ),
        priority = PhelCompletionPriority.PREDICATE_FUNCTIONS,
    ),
    DataFunction(
        namespace = "mock",
        name = "mock/reset-mock!",
        doc = """Resets the call history of a mock without removing it from the registry. The mock can continue to be used and track new calls.""",
        signature = "(reset-mock! mock-fn)",
        description = """Resets the call history of a mock without removing it from the registry.  The mock can continue to be used and track new calls""",
        githubUrl = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/mock.phel#L155",
        docUrl = "",
        meta = FunctionMeta(
            example = "(def my-mock (mock :result))",
            deprecatedVersion = null,
            supersededBy = null,
        ),
        priority = PhelCompletionPriority.MOCK_FUNCTIONS,
    ),
    DataFunction(
        namespace = "mock",
        name = "mock/spy",
        doc = """Wraps an existing function to track calls while preserving original behavior.""",
        signature = "(spy f)",
        description = """Wraps an existing function to track calls while preserving original behavior""",
        githubUrl = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/mock.phel#L51",
        docUrl = "",
        meta = FunctionMeta(
            example = "(def original-fn (fn [x] (* x 2)))",
            deprecatedVersion = null,
            supersededBy = null,
        ),
        priority = PhelCompletionPriority.MOCK_FUNCTIONS,
    ),
    DataFunction(
        namespace = "mock",
        name = "mock/with-mock-wrapper",
        doc = """
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
        signature = "(with-mock-wrapper bindings & body)",
        description = """Like with-mocks but for wrapped mocks (interop scenarios).  Automatically resets the underlying mock even when wrapped in an adapter function.  Usage:    ``phel    (with-mock-wrapper [fn-symbol underlying-mock wrapper-fn]      body...)    `  Multiple wrappers:    `phel    (with-mock-wrapper [service-a mock-a (fn [x] (mock-a (inc x)))                        service-b mock-b (fn [y] (mock-b (dec y)))]      ...)    ``""",
        githubUrl = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/mock.phel#L215",
        docUrl = "",
        meta = FunctionMeta(
            example = "(with-mock-wrapper [http mock-http identity] (http \"test\"))",
            deprecatedVersion = null,
            supersededBy = null,
        ),
        priority = PhelCompletionPriority.MOCK_FUNCTIONS,
    ),
    DataFunction(
        namespace = "mock",
        name = "mock/with-mocks",
        doc = """
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
        signature = "(with-mocks bindings & body)",
        description = """Temporarily replaces functions with mocks using binding.  Automatically resets mocks after the body executes.  Works with inline mock creation:    ``phel    (with-mocks [http-get (mock {:status 200})]      (http-get)      # Mock is automatically reset after this block)    `  Also works with pre-defined mocks:    `phel    (let [my-mock (mock :result)]      (with-mocks [some-fn my-mock]        (some-fn)))    `  If you need to wrap the mock in a function (e.g., to adapt arguments),  you'll need to manually reset:    `phel    (with-mocks [some-fn (fn [& args] (my-mock (transform args)))]      (some-fn)      (reset-mock! my-mock))    ``""",
        githubUrl = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/mock.phel#L179",
        docUrl = "",
        meta = FunctionMeta(
            example = null,
            deprecatedVersion = null,
            supersededBy = null,
        ),
        priority = PhelCompletionPriority.MOCK_FUNCTIONS,
    ),
)
