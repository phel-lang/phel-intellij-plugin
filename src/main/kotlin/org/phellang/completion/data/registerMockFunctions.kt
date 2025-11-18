package org.phellang.completion.data

import org.phellang.completion.infrastructure.PhelCompletionPriority

internal fun registerMockFunctions(): List<DataFunction> = listOf(
    DataFunction("mock/call-count", "(call-count mock-fn)", PhelCompletionPriority.MOCK_FUNCTIONS, "mock", "Returns the number of times the mock was called.  Examples:    (def my-mock (mock :result))    (my-mock 1)    (my-mock 2)    (call-count my-mock)  # => 2", """
<br /><code>(call-count mock-fn)</code><br /><br />
Returns the number of times the mock was called.<br />
<br />
  Examples:<br />
    (def my-mock (mock :result))<br />
    (my-mock 1)<br />
    (my-mock 2)<br />
    (call-count my-mock)  # => 2<br />
<br />
"""),
    DataFunction("mock/called-once?", "(called-once? mock-fn)", PhelCompletionPriority.MOCK_FUNCTIONS, "mock", "Returns true if the mock was called exactly once.  Examples:    (def my-mock (mock :result))    (my-mock)    (called-once? my-mock)  # => true    (my-mock)    (called-once? my-mock)  # => false", """
<br /><code>(called-once? mock-fn)</code><br /><br />
Returns true if the mock was called exactly once.<br />
<br />
  Examples:<br />
    (def my-mock (mock :result))<br />
    (my-mock)<br />
    (called-once? my-mock)  # => true<br />
    (my-mock)<br />
    (called-once? my-mock)  # => false<br />
<br />
"""),
    DataFunction("mock/called-times?", "(called-times? mock-fn n)", PhelCompletionPriority.MOCK_FUNCTIONS, "mock", "Returns true if the mock was called exactly n times.  Examples:    (def my-mock (mock :result))    (my-mock)    (my-mock)    (called-times? my-mock 2)  # => true", """
<br /><code>(called-times? mock-fn n)</code><br /><br />
Returns true if the mock was called exactly n times.<br />
<br />
  Examples:<br />
    (def my-mock (mock :result))<br />
    (my-mock)<br />
    (my-mock)<br />
    (called-times? my-mock 2)  # => true<br />
<br />
"""),
    DataFunction("mock/called-with?", "(called-with? mock-fn & expected-args)", PhelCompletionPriority.MOCK_FUNCTIONS, "mock", "Returns true if the mock was called with the exact arguments.  Examples:    (def my-mock (mock :result))    (my-mock 1 2 3)    (called-with? my-mock 1 2 3)  # => true    (called-with? my-mock 1 2)    # => false", """
<br /><code>(called-with? mock-fn & expected-args)</code><br /><br />
Returns true if the mock was called with the exact arguments.<br />
<br />
  Examples:<br />
    (def my-mock (mock :result))<br />
    (my-mock 1 2 3)<br />
    (called-with? my-mock 1 2 3)  # => true<br />
    (called-with? my-mock 1 2)    # => false<br />
<br />
"""),
    DataFunction("mock/called?", "(called? mock-fn)", PhelCompletionPriority.MOCK_FUNCTIONS, "mock", "Returns true if the mock was called at least once.  Examples:    (def my-mock (mock :result))    (called? my-mock)     # => false    (my-mock)    (called? my-mock)     # => true", """
<br /><code>(called? mock-fn)</code><br /><br />
Returns true if the mock was called at least once.<br />
<br />
  Examples:<br />
    (def my-mock (mock :result))<br />
    (called? my-mock)     # => false<br />
    (my-mock)<br />
    (called? my-mock)     # => true<br />
<br />
"""),
    DataFunction("mock/calls", "(calls mock-fn)", PhelCompletionPriority.MOCK_FUNCTIONS, "mock", "Returns a list of all argument lists the mock was called with.  Examples:    (def my-mock (mock :result))    (my-mock 1 2)    (my-mock 3)    (calls my-mock)  # => [[1 2] [3]]", """
<br /><code>(calls mock-fn)</code><br /><br />
Returns a list of all argument lists the mock was called with.<br />
<br />
  Examples:<br />
    (def my-mock (mock :result))<br />
    (my-mock 1 2)<br />
    (my-mock 3)<br />
    (calls my-mock)  # => [[1 2] [3]]<br />
<br />
"""),
    DataFunction("mock/clear-all-mocks!", "(clear-all-mocks! )", PhelCompletionPriority.MOCK_FUNCTIONS, "mock", "Clears the entire mock registry.  Useful for cleanup between test suites in long-running processes.  Examples:    (clear-all-mocks!)  # All mocks removed from registry", """
<br /><code>(clear-all-mocks! )</code><br /><br />
Clears the entire mock registry.<br />
  Useful for cleanup between test suites in long-running processes.<br />
<br />
  Examples:<br />
    (clear-all-mocks!)  # All mocks removed from registry<br />
<br />
"""),
    DataFunction("mock/first-call", "(first-call mock-fn)", PhelCompletionPriority.MOCK_FUNCTIONS, "mock", "Returns the arguments from the first call.  Examples:    (def my-mock (mock :result))    (my-mock 1 2)    (my-mock 3 4)    (first-call my-mock)  # => [1 2]", """
<br /><code>(first-call mock-fn)</code><br /><br />
Returns the arguments from the first call.<br />
<br />
  Examples:<br />
    (def my-mock (mock :result))<br />
    (my-mock 1 2)<br />
    (my-mock 3 4)<br />
    (first-call my-mock)  # => [1 2]<br />
<br />
"""),
    DataFunction("mock/last-call", "(last-call mock-fn)", PhelCompletionPriority.MOCK_FUNCTIONS, "mock", "Returns the arguments from the most recent call.  Examples:    (def my-mock (mock :result))    (my-mock 1 2)    (my-mock 3 4)    (last-call my-mock)  # => [3 4]", """
<br /><code>(last-call mock-fn)</code><br /><br />
Returns the arguments from the most recent call.<br />
<br />
  Examples:<br />
    (def my-mock (mock :result))<br />
    (my-mock 1 2)<br />
    (my-mock 3 4)<br />
    (last-call my-mock)  # => [3 4]<br />
<br />
"""),
    DataFunction("mock/mock", "(mock return-value)", PhelCompletionPriority.MOCK_FUNCTIONS, "mock", "Creates a mock function that returns a fixed value and tracks all calls.  Examples:    (def my-mock (mock :return-value))    (my-mock 1 2 3)  # => :return-value    (calls my-mock)  # => [[1 2 3]]", """
<br /><code>(mock return-value)</code><br /><br />
Creates a mock function that returns a fixed value and tracks all calls.<br />
<br />
  Examples:<br />
    (def my-mock (mock :return-value))<br />
    (my-mock 1 2 3)  # => :return-value<br />
    (calls my-mock)  # => [[1 2 3]]<br />
<br />
"""),
    DataFunction("mock/mock-fn", "(mock-fn f)", PhelCompletionPriority.MOCK_FUNCTIONS, "mock", "Creates a mock function with custom behavior that tracks all calls.  Examples:    (def my-mock (mock-fn (fn [x] (* x 2))))    (my-mock 5)      # => 10    (calls my-mock)  # => [[5]]", """
<br /><code>(mock-fn f)</code><br /><br />
Creates a mock function with custom behavior that tracks all calls.<br />
<br />
  Examples:<br />
    (def my-mock (mock-fn (fn [x] (* x 2))))<br />
    (my-mock 5)      # => 10<br />
    (calls my-mock)  # => [[5]]<br />
<br />
"""),
    DataFunction("mock/mock-returning", "(mock-returning values)", PhelCompletionPriority.MOCK_FUNCTIONS, "mock", "Creates a mock that returns different values for consecutive calls.  After exhausting values, returns the last value.  Examples:    (def my-mock (mock-returning [1 2 3]))    (my-mock)  # => 1    (my-mock)  # => 2    (my-mock)  # => 3    (my-mock)  # => 3", """
<br /><code>(mock-returning values)</code><br /><br />
Creates a mock that returns different values for consecutive calls.<br />
  After exhausting values, returns the last value.<br />
<br />
  Examples:<br />
    (def my-mock (mock-returning [1 2 3]))<br />
    (my-mock)  # => 1<br />
    (my-mock)  # => 2<br />
    (my-mock)  # => 3<br />
    (my-mock)  # => 3<br />
<br />
"""),
    DataFunction("mock/mock-throwing", "(mock-throwing exception)", PhelCompletionPriority.MOCK_FUNCTIONS, "mock", "Creates a mock that throws an exception when called.  Examples:    (def my-mock (mock-throwing (php/new \\RuntimeException \"API unavailable\")))    (my-mock)  # throws RuntimeException", """
<br /><code>(mock-throwing exception)</code><br /><br />
Creates a mock that throws an exception when called.<br />
<br />
  Examples:<br />
    (def my-mock (mock-throwing (php/new \\RuntimeException \"API unavailable\")))<br />
    (my-mock)  # throws RuntimeException<br />
<br />
"""),
    DataFunction("mock/mock?", "(mock? f)", PhelCompletionPriority.MOCK_FUNCTIONS, "mock", "Returns true if the function is a mock", """
<br /><code>(mock? f)</code><br /><br />
Returns true if the function is a mock.<br />
<br />
"""),
    DataFunction("mock/never-called?", "(never-called? mock-fn)", PhelCompletionPriority.MOCK_FUNCTIONS, "mock", "Returns true if the mock was never called.  Examples:    (def my-mock (mock :result))    (never-called? my-mock)  # => true    (my-mock)    (never-called? my-mock)  # => false", """
<br /><code>(never-called? mock-fn)</code><br /><br />
Returns true if the mock was never called.<br />
<br />
  Examples:<br />
    (def my-mock (mock :result))<br />
    (never-called? my-mock)  # => true<br />
    (my-mock)<br />
    (never-called? my-mock)  # => false<br />
<br />
"""),
    DataFunction("mock/reset-mock!", "(reset-mock! mock-fn)", PhelCompletionPriority.MOCK_FUNCTIONS, "mock", "Resets the call history of a mock without removing it from the registry.  The mock can continue to be used and track new calls.  Examples:    (def my-mock (mock :result))    (my-mock 1)    (call-count my-mock)   # => 1    (reset-mock! my-mock)    (call-count my-mock)   # => 0    (my-mock 2)    (call-count my-mock)   # => 1", """
<br /><code>(reset-mock! mock-fn)</code><br /><br />
Resets the call history of a mock without removing it from the registry.<br />
  The mock can continue to be used and track new calls.<br />
<br />
  Examples:<br />
    (def my-mock (mock :result))<br />
    (my-mock 1)<br />
    (call-count my-mock)   # => 1<br />
    (reset-mock! my-mock)<br />
    (call-count my-mock)   # => 0<br />
    (my-mock 2)<br />
    (call-count my-mock)   # => 1<br />
<br />
"""),
    DataFunction("mock/spy", "(spy f)", PhelCompletionPriority.MOCK_FUNCTIONS, "mock", "Wraps an existing function to track calls while preserving original behavior.  Examples:    (def original-fn (fn [x] (* x 2)))    (def spied (spy original-fn))    (spied 5)        # => 10 (calls original)    (calls spied)    # => [[5]]", """
<br /><code>(spy f)</code><br /><br />
Wraps an existing function to track calls while preserving original behavior.<br />
<br />
  Examples:<br />
    (def original-fn (fn [x] (* x 2)))<br />
    (def spied (spy original-fn))<br />
    (spied 5)        # => 10 (calls original)<br />
    (calls spied)    # => [[5]]<br />
<br />
"""),
    DataFunction("mock/with-mock-wrapper", "(with-mock-wrapper bindings & body)", PhelCompletionPriority.MOCK_FUNCTIONS, "mock", "Like with-mocks but for wrapped mocks (interop scenarios).  Automatically resets the underlying mock even when wrapped in an adapter function.  Usage:    (with-mock-wrapper [fn-symbol underlying-mock wrapper-fn]      body...)", """
<br /><code>(with-mock-wrapper bindings & body)</code><br /><br />
Like with-mocks but for wrapped mocks (interop scenarios).<br />
  Automatically resets the underlying mock even when wrapped in an adapter function.<br />
<br />
  Usage:<br />
    (with-mock-wrapper [fn-symbol underlying-mock wrapper-fn]<br />
      body...)<br />
<br />
  <pre><code>(let [mock-http (mock <code>{:status 200}</code>)]<br />  (with-mock-wrapper [symfony-service mock-http<br />                      (fn [args] (mock-http (adapt-args args)))]<br />    (symfony-service <code>{:key \"value\"}</code>)<br />    (is (called-once? mock-http))))<br />  # mock-http is automatically reset here<br /><br />  Multiple wrappers:<br />(with-mock-wrapper [service-a mock-a (fn [x] (mock-a (inc x)))<br />                    service-b mock-b (fn [y] (mock-b (dec y)))]<br />  ...)</code></pre>
<br />
"""),
    DataFunction("mock/with-mocks", "(with-mocks bindings & body)", PhelCompletionPriority.MOCK_FUNCTIONS, "mock", "Temporarily replaces functions with mocks using binding.  Automatically resets mocks after the body executes.  Works with inline mock creation:    (with-mocks [http-get (mock {:status 200})]      (http-get)      # Mock is automatically reset after this block)  Also works with pre-defined mocks:    (let [my-mock (mock :result)]      (with-mocks [some-fn my-mock]        (some-fn)))  If you need to wrap the mock in a function (e.g., to adapt arguments),  you'll need to manually reset:    (with-mocks [some-fn (fn [& args] (my-mock (transform args)))]      (some-fn)      (reset-mock! my-mock))", """
<br /><code>(with-mocks bindings & body)</code><br /><br />
Temporarily replaces functions with mocks using binding.<br />
  Automatically resets mocks after the body executes.<br />
<br />
  Works with inline mock creation:<br />
    (with-mocks [http-get (mock <code>{:status 200}</code>)]<br />
      (http-get)<br />
      # Mock is automatically reset after this block)<br />
<br />
  Also works with pre-defined mocks:<br />
    (let [my-mock (mock :result)]<br />
      (with-mocks [some-fn my-mock]<br />
        (some-fn)))<br />
<br />
  If you need to wrap the mock in a function (e.g., to adapt arguments),<br />
  you'll need to manually reset:<br />
    (with-mocks [some-fn (fn [& args] (my-mock (transform args)))]<br />
      (some-fn)<br />
      (reset-mock! my-mock))<br />
<br />
"""),
)
