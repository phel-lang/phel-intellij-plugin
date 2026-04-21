package org.phellang.completion.data.core

import org.phellang.completion.data.CompletionInfo
import org.phellang.completion.data.DocumentationInfo
import org.phellang.completion.data.DocumentationLinks
import org.phellang.completion.data.DeprecationInfo
import org.phellang.completion.data.PhelFunction

import org.phellang.completion.infrastructure.PhelCompletionPriority

internal fun registerCoreAtomsFunctions(): List<PhelFunction> = listOf(
    PhelFunction(
        namespace = "core",
        name = "add-watch",
        signature = "(add-watch variable key f)",
        completion = CompletionInfo(
            tailText = "Adds a watch function to a variable",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Adds a watch function to a variable. The watch fn is called when the variable<br />
  changes with four arguments: key, ref, old-value, new-value.
""",
            example = "(add-watch my-var :logger (fn [key ref old new] (println old \"-&gt;\" new)))",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/atoms.phel#L106",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "alter-var-root",
        signature = "(alter-var-root & _)",
        completion = CompletionInfo(
            tailText = "Clojure's alter-var-root is out of scope in Phel: there are no first-class vars whose root bindin...",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Clojure's <code>alter-var-root</code> is out of scope in Phel: there are no first-class<br />
  vars whose root binding could be swapped. Reaching for this function in<br />
  <code>.cljc</code> code is nearly always a bug; prefer an <code>atom</code> and <code>swap!</code> for mutable<br />
  state, or redefine the top-level binding with <code>def</code> if the intent was to<br />
  replace it at load time. Calling <code>alter-var-root</code> at runtime throws to make<br />
  the mismatch obvious instead of silently no-oping.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/atoms.phel#L115",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "atom",
        signature = "(atom value)",
        completion = CompletionInfo(
            tailText = "Creates a new atom with the given value",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Creates a new atom with the given value.<br /><br />
Atoms provide a way to manage mutable state. Use <code>reset!</code> to set a new value<br />
  and <code>swap!</code> to update based on the current value.
""",
            example = "(def counter (atom 0))",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/atoms.phel#L20",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "atom?",
        signature = "(atom? x)",
        completion = CompletionInfo(
            tailText = "Returns true if the given value is an atom",
            priority = PhelCompletionPriority.PREDICATE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Returns true if the given value is an atom.",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/atoms.phel#L39",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "deref",
        signature = "(deref variable & args)",
        completion = CompletionInfo(
            tailText = "Returns the current value inside the variable",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns the current value inside the variable.<br /><br />
With three arguments, and when <code>variable</code> is a future or promise, blocks<br />
  for at most <code>timeout-ms</code> milliseconds waiting for it to resolve. If the<br />
  awaitable has not completed within the timeout, returns <code>timeout-val</code>.
""",
            example = "(deref (atom 42)) ; =&gt; 42",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/atoms.phel#L67",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "dofor",
        signature = "(dofor head & body)",
        completion = CompletionInfo(
            tailText = "Repeatedly executes body for side effects with bindings and modifiers as provided by for",
            priority = PhelCompletionPriority.MACROS,
        ),
        documentation = DocumentationInfo(
            summary = """
Repeatedly executes body for side effects with bindings and modifiers as<br />
  provided by for. Returns nil.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/atoms.phel#L266",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "doseq",
        signature = "(doseq seq-exprs & body)",
        completion = CompletionInfo(
            tailText = "Repeatedly executes body for side effects with Clojure-style bindings",
            priority = PhelCompletionPriority.MACROS,
        ),
        documentation = DocumentationInfo(
            summary = """
Repeatedly executes body for side effects with Clojure-style bindings.<br />
  <code>(doseq [x coll] body)</code> runs <code>body</code> once per element of <code>coll</code>. When <code>coll</code><br />
  is a map, each iteration sees a <code>[k v]</code> entry pair, so destructuring works<br />
  just like in Clojure: <code>(doseq [[k v] m] ...)</code>. Supports <code>:when</code>, <code>:while</code>,<br />
  and <code>:let</code> modifiers between bindings.
""",
            example = "(doseq [x [1 2 3]] (println x))\n(doseq [[k v] {:a 1 :b 2}] (println k v))",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/atoms.phel#L320",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "doseq-iterable",
        signature = "(doseq-iterable coll)",
        completion = CompletionInfo(
            tailText = "Internal helper used by the doseq macro expansion",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Internal helper used by the <code>doseq</code> macro expansion. Returns a value<br />
  suitable for Clojure-style iteration: maps are expanded to a sequence of<br />
  <code>[k v]</code> pair vectors so destructuring binds entries as in Clojure. Every<br />
  other value is returned unchanged.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/atoms.phel#L279",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "for",
        signature = "(for head & body)",
        completion = CompletionInfo(
            tailText = "List comprehension",
            priority = PhelCompletionPriority.MACROS,
        ),
        documentation = DocumentationInfo(
            summary = """
List comprehension. The head of the loop is a vector that contains a<br />
  sequence of bindings modifiers and options. A binding is a sequence of three<br />
  values <code>binding :verb expr</code>. Where <code>binding</code> is a binding as<br />
  in let and <code>:verb</code> is one of the following keywords:<br /><br />
<em> <code>:range</code> loop over a range by using the range function.<br />
  </em> <code>:in</code> loops over all values of a collection (including strings).<br />
  <em> <code>:keys</code> loops over all keys/indexes of a collection.<br />
  </em> <code>:pairs</code> loops over all key-value pairs of a collection.<br /><br />
After each loop binding, additional modifiers can be applied. Modifiers<br />
  have the form <code>:modifier argument</code>. The following modifiers are supported:<br /><br />
<em> <code>:while</code> breaks the loop if the expression is falsy.<br />
  </em> <code>:let</code> defines additional bindings.<br />
  <em> <code>:when</code> only evaluates the loop body if the condition is true.<br /><br />
Finally, additional options can be set:<br /><br />
</em> <code>:reduce [accumulator initial-value]</code> Instead of returning a list,<br />
     it reduces the values into <code>accumulator</code>. Initially <code>accumulator</code><br />
     is bound to <code>initial-value</code>.
""",
            example = "(for [x :in [1 2 3]] (* x 2)) ; =&gt; [2 4 6]",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/atoms.phel#L226",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "get-validator",
        signature = "(get-validator variable)",
        completion = CompletionInfo(
            tailText = "Returns the validator function of a variable, or nil",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Returns the validator function of a variable, or nil.",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/atoms.phel#L144",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "identity",
        signature = "(identity x)",
        completion = CompletionInfo(
            tailText = "Returns its argument",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Returns its argument.",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/atoms.phel#L331",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "range",
        signature = "(range & args)",
        completion = CompletionInfo(
            tailText = "Creates a lazy sequence of numbers",
            priority = PhelCompletionPriority.COLLECTION_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Creates a lazy sequence of numbers. With no arguments returns an infinite<br />
  sequence starting at 0. With one argument returns (0..n). With two (start..end).<br />
  With three (start..end step). Note: the infinite sequence is bounded by PHP_INT_MAX.
""",
            example = "(range 5) ; =&gt; (0 1 2 3 4)",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/atoms.phel#L163",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "remove-watch",
        signature = "(remove-watch variable key)",
        completion = CompletionInfo(
            tailText = "Removes a watch function from a variable by key",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Removes a watch function from a variable by key.",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/atoms.phel#L127",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "reset!",
        signature = "(reset! variable value)",
        completion = CompletionInfo(
            tailText = "Sets a new value on the given atom",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Sets a new value on the given atom. Returns the new value.",
            example = "(def x (atom 10))",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/atoms.phel#L51",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "set!",
        signature = "(set! variable value)",
        completion = CompletionInfo(
            tailText = "Sets a new value to the given variable",
            priority = PhelCompletionPriority.DEPRECATED_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Sets a new value to the given variable.",
            example = "(def x (var 10))",
            deprecation = DeprecationInfo(version = "0.32.0", replacement = "reset!"),
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/atoms.phel#L58",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "set-validator!",
        signature = "(set-validator! variable f)",
        completion = CompletionInfo(
            tailText = "Sets a validator function on a variable",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Sets a validator function on a variable. The validator is called before any<br />
  state change with the proposed new value. If it returns a falsy value, an<br />
  exception is thrown and the state is not changed. Pass nil to remove.
""",
            example = "(set-validator! my-var pos?)",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/atoms.phel#L134",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "swap!",
        signature = "(swap! variable f & args)",
        completion = CompletionInfo(
            tailText = "Atomically swaps the value of the atom to (apply f current-value args)",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Atomically swaps the value of the atom to <code>(apply f current-value args)</code>.<br /><br />
Returns the new value after the swap.
""",
            example = "(def counter (atom 0))",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/atoms.phel#L91",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "var",
        signature = "(var value)",
        completion = CompletionInfo(
            tailText = "Creates a new variable with the given value",
            priority = PhelCompletionPriority.DEPRECATED_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Creates a new variable with the given value.",
            example = "(def counter (var 0))",
            deprecation = DeprecationInfo(version = "0.32.0", replacement = "atom"),
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/atoms.phel#L30",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "var?",
        signature = "(var? x)",
        completion = CompletionInfo(
            tailText = "Checks if the given value is a variable",
            priority = PhelCompletionPriority.DEPRECATED_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Checks if the given value is a variable.",
            example = null,
            deprecation = DeprecationInfo(version = "0.32.0", replacement = "atom?"),
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/atoms.phel#L44",
                docs = "",
            ),
        ),
    )
)
