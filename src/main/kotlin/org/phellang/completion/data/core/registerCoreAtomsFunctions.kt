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
Adds a watch function to a variable. The watch fn is called when the variable changes with four arguments: key, ref, old-value, new-value.
""",
            example = "(add-watch my-var :logger (fn [key ref old new] (println old \"-&gt;\" new)))",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.42.0/src/phel/core/atoms.phel#L101",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "alter-meta!",
        signature = "(alter-meta! r f & args)",
        completion = CompletionInfo(
            tailText = "Replaces the metadata on r with (apply f current-meta args)",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Replaces the metadata on <code>r</code> with <code>(apply f current-meta args)</code>.<br />
  Works on <code>Var</code> handles (mutates per-var metadata, surviving subsequent<br />
  <code>def</code> redefinitions) and on atoms (mutates the atom's own meta map).<br />
  Returns the new metadata map.
""",
            example = "(alter-meta! #'my-var assoc :tag :int)",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.42.0/src/phel/core/atoms.phel#L148",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "alter-var-root",
        signature = "(alter-var-root v f & args)",
        completion = CompletionInfo(
            tailText = "Replaces the root binding of v with (apply f current-root args)",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Replaces the root binding of <code>v</code> with <code>(apply f current-root args)</code>. Returns the new root value. <code>v</code> must be a <code>Var</code>; pass an atom and you get a clear error pointing at <code>swap!</code> instead.
""",
            example = "(def counter 0)\n(alter-var-root #'counter inc) ; =&gt; 1",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.42.0/src/phel/core/atoms.phel#L109",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "atom",
        signature = "(atom value & opts)",
        completion = CompletionInfo(
            tailText = "Creates a new atom with the given value",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Creates a new atom with the given value.<br /><br />
Atoms provide a way to manage mutable state. Use <code>reset!</code> to set a new value and <code>swap!</code> to update based on the current value.<br /><br />
Optional <code>:meta</code> and <code>:validator</code> keyword arguments may follow the value in any order. The validator is applied to the initial value when set.
""",
            example = "(def counter (atom 0))\n(atom 0 :meta {:tag :counter} :validator number?)",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.42.0/src/phel/core/atoms.phel#L22",
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
                github = "https://github.com/phel-lang/phel-lang/blob/v0.42.0/src/phel/core/atoms.phel#L38",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "bound?",
        signature = "(bound? v)",
        completion = CompletionInfo(
            tailText = "Returns true when v has a current root binding in the namespace registry",
            priority = PhelCompletionPriority.PREDICATE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns true when <code>v</code> has a current root binding in the namespace registry. <code>v</code> must be a <code>Var</code>.
""",
            example = "(bound? #'phel.core/map) ; =&gt; true",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.42.0/src/phel/core/atoms.phel#L117",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "deref",
        signature = "(deref variable & args)",
        completion = CompletionInfo(
            tailText = "Returns the current value inside an atom or var",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns the current value inside an atom or var.<br /><br />
For atoms returns the current cell value; for <code>Var</code> instances returns the current root binding from the namespace registry.<br /><br />
With three arguments, and when <code>variable</code> is a future or promise, blocks for at most <code>timeout-ms</code> milliseconds waiting for it to resolve. If the awaitable has not completed within the timeout, returns <code>timeout-val</code>.
""",
            example = "(deref (atom 42)) ; =&gt; 42\n(deref #'phel.core/map) ; =&gt; &lt;function:map&gt;",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.42.0/src/phel/core/atoms.phel#L62",
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
Repeatedly executes body for side effects with bindings and modifiers as provided by for. Returns nil.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.42.0/src/phel/core/atoms.phel#L309",
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
                github = "https://github.com/phel-lang/phel-lang/blob/v0.42.0/src/phel/core/atoms.phel#L358",
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
                github = "https://github.com/phel-lang/phel-lang/blob/v0.42.0/src/phel/core/atoms.phel#L321",
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
                github = "https://github.com/phel-lang/phel-lang/blob/v0.42.0/src/phel/core/atoms.phel#L269",
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
                github = "https://github.com/phel-lang/phel-lang/blob/v0.42.0/src/phel/core/atoms.phel#L190",
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
                github = "https://github.com/phel-lang/phel-lang/blob/v0.42.0/src/phel/core/atoms.phel#L369",
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
Creates a lazy sequence of numbers. With no arguments returns an infinite sequence starting at 0. With one argument returns (0..n). With two (start..end). With three (start..end step). Note: the infinite sequence is bounded by PHP_INT_MAX.
""",
            example = "(range 5) ; =&gt; @[0 1 2 3 4]",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.42.0/src/phel/core/atoms.phel#L208",
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
                github = "https://github.com/phel-lang/phel-lang/blob/v0.42.0/src/phel/core/atoms.phel#L175",
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
                github = "https://github.com/phel-lang/phel-lang/blob/v0.42.0/src/phel/core/atoms.phel#L55",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "reset-meta!",
        signature = "(reset-meta! r meta-map)",
        completion = CompletionInfo(
            tailText = "Installs meta-map as the metadata on r, replacing any prior metadata",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Installs <code>meta-map</code> as the metadata on <code>r</code>, replacing any prior metadata. Works on <code>Var</code> handles and on atoms. Returns the installed map.
""",
            example = "(reset-meta! #'my-var {:tag :int})",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.42.0/src/phel/core/atoms.phel#L164",
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
Sets a validator function on a variable. The validator is called before any state change with the proposed new value. If it returns a falsy value, an exception is thrown and the state is not changed. Pass nil to remove.
""",
            example = "(set-validator! my-var pos?)",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.42.0/src/phel/core/atoms.phel#L182",
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
                github = "https://github.com/phel-lang/phel-lang/blob/v0.42.0/src/phel/core/atoms.phel#L86",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "thread-bound?",
        signature = "(thread-bound? v)",
        completion = CompletionInfo(
            tailText = "Returns true when a fiber-local binding (or with-bindings) frame currently overrides the value of...",
            priority = PhelCompletionPriority.PREDICATE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns true when a fiber-local <code>binding</code> (or <code>with-bindings</code>) frame currently overrides the value of <code>v</code> on the calling fiber.
""",
            example = "(binding [*foo* 1] (thread-bound? #'*foo*)) ; =&gt; true",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.42.0/src/phel/core/atoms.phel#L126",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "var-set",
        signature = "(var-set v val)",
        completion = CompletionInfo(
            tailText = "Sets the value of the topmost active fiber-local binding frame for v to val",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Sets the value of the topmost active fiber-local binding frame for<br />
  <code>v</code> to <code>val</code>. Throws when no <code>binding</code> (or <code>with-bindings</code>) frame is<br />
  currently overriding <code>v</code>. Returns <code>val</code>.
""",
            example = "(def ^:dynamic *x* 0)\n(binding [*x* 1] (var-set #'*x* 2) *x*) ; =&gt; 2",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.42.0/src/phel/core/atoms.phel#L136",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "var?",
        signature = "(var? x)",
        completion = CompletionInfo(
            tailText = "Returns true if the given value is a Var, the first-class handle to a global definition produced ...",
            priority = PhelCompletionPriority.PREDICATE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns true if the given value is a <code>Var</code>, the first-class handle to a global definition produced by <code>(var sym)</code> and <code>#'sym</code>.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.42.0/src/phel/core/atoms.phel#L43",
                docs = "",
            ),
        ),
    )
)
