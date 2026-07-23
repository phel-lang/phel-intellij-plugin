package org.phellang.registry.data.core

import org.phellang.registry.CompletionInfo
import org.phellang.registry.DocumentationInfo
import org.phellang.registry.DocumentationLinks
import org.phellang.registry.DeprecationInfo
import org.phellang.registry.PhelFunction

import org.phellang.registry.PhelCompletionPriority

internal fun registerCoreProtocolsFunctions(): List<PhelFunction> = listOf(
    PhelFunction(
        namespace = "core",
        name = "ancestors",
        signature = "(ancestors tag)\n(ancestors h tag)",
        completion = CompletionInfo(
            tailText = "Returns the set of all transitive ancestors of tag, or nil",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns the set of all transitive ancestors of tag, or nil. When a hierarchy is provided, consults it; otherwise uses the global hierarchy.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/protocols.phel#L318",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "assert",
        signature = "(assert expr & [message])",
        completion = CompletionInfo(
            tailText = "Throws an exception if expr is falsy",
            priority = PhelCompletionPriority.MACROS,
        ),
        documentation = DocumentationInfo(
            summary = """
Throws an exception if expr is falsy. Optional message string. Used for precondition checking in application code. When <code><em>assert</em></code> is logical false at macroexpansion time, <code>assert</code> expands to nil and performs no runtime check.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/protocols.phel#L943",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "compile",
        signature = "(compile form)",
        completion = CompletionInfo(
            tailText = "Returns the compiled PHP code string for the given form",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Returns the compiled PHP code string for the given form.",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/protocols.phel#L984",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "definterface",
        signature = "(definterface name & fns)",
        completion = CompletionInfo(
            tailText = "An interface in Phel defines an abstract set of functions",
            priority = PhelCompletionPriority.MACROS,
        ),
        documentation = DocumentationInfo(
            summary = """
An interface in Phel defines an abstract set of functions. It is directly mapped to a PHP interface. An interface can be defined by using the definterface macro.
""",
            example = "(definterface name &amp; fns)",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/protocols.phel#L24",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "defmethod",
        signature = "(defmethod multi-name dispatch-val & fn-tail)",
        completion = CompletionInfo(
            tailText = "Registers a method implementation for a multimethod",
            priority = PhelCompletionPriority.MACROS,
        ),
        documentation = DocumentationInfo(
            summary = """
Registers a method implementation for a multimethod.<br /><br />
<code>multi-name</code> is the name of the multimethod defined by <code>defmulti</code>.<br />
  When extending a multimethod from a different namespace, fully qualify<br />
  the multi-name (e.g. <code>phel\test/assert-expr</code>) so the methods table is<br />
  resolved in the multimethod's home namespace.<br />
  <code>dispatch-val</code> is the value that triggers this method.<br />
  <code>args</code> and <code>body</code> define the function implementation.
""",
            example = "(defmethod area :circle [{:radius r}] (* 3.14159 r r))",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/protocols.phel#L763",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "defmulti",
        signature = "(defmulti name & args)",
        completion = CompletionInfo(
            tailText = "Defines a multimethod",
            priority = PhelCompletionPriority.MACROS,
        ),
        documentation = DocumentationInfo(
            summary = """
Defines a multimethod. <code>dispatch-fn</code> is called on the arguments to<br />
  produce a dispatch value, which is then used to select the appropriate<br />
  method registered via <code>defmethod</code>.<br /><br />
An optional docstring may be provided between <code>name</code> and <code>dispatch-fn</code>.<br /><br />
If no method matches the dispatch value, the <code>:default</code> method is used<br />
  (if defined), otherwise an error is thrown.
""",
            example = "(defmulti area \"Area of shape.\" :shape)",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/protocols.phel#L709",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "defprotocol",
        signature = "(defprotocol protocol-name & method-specs)",
        completion = CompletionInfo(
            tailText = "Defines a protocol with the given method signatures",
            priority = PhelCompletionPriority.MACROS,
        ),
        documentation = DocumentationInfo(
            summary = """
Defines a protocol with the given method signatures. Each method signature is a list of (method-name [args]).<br /><br />
Creates a dispatching function for each method that dispatches on the type of the first argument. Use <code>extend-type</code> to add implementations.<br /><br />
A <code>:default</code> type can be registered via <code>extend-type</code> as a fallback when no specific type implementation is found.
""",
            example = "(defprotocol Stringable (to-string [this]))",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/protocols.phel#L430",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "defrecord",
        signature = "(defrecord name fields & impls)",
        completion = CompletionInfo(
            tailText = "Defines a record type with the given fields, matching Clojure's defrecord",
            priority = PhelCompletionPriority.MACROS,
        ),
        documentation = DocumentationInfo(
            summary = """
Defines a record type with the given fields, matching Clojure's <code>defrecord</code>.<br /><br />
Expands to a <code>defstruct</code> plus Clojure-style factory functions:<br />
  - <code>Name</code> — positional constructor (from <code>defstruct</code>)<br />
  - <code>Name?</code> — type predicate (from <code>defstruct</code>)<br />
  - <code>->Name</code> — positional factory, identical to <code>Name</code><br />
  - <code>map->Name</code> — map factory that takes <code>{:field value ...}</code><br /><br />
An optional tail of protocol/method forms is spliced into an <code>extend-type</code><br />
  call, so inline protocol implementations work exactly like Clojure's<br />
  <code>defrecord</code> body. Only Phel protocols are supported in the inline tail;<br />
  PHP interface implementations remain on <code>defstruct</code>.
""",
            example = "(defrecord Point [x y] Drawable (draw [this canvas] ...))",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/protocols.phel#L646",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "deftype",
        signature = "(deftype name fields & impls)",
        completion = CompletionInfo(
            tailText = "Defines a type with the given fields, matching Clojure's deftype syntax",
            priority = PhelCompletionPriority.MACROS,
        ),
        documentation = DocumentationInfo(
            summary = """
Defines a type with the given fields, matching Clojure's <code>deftype</code> syntax.<br /><br />
Expands to a <code>defstruct</code> plus a Clojure-style positional factory:<br />
  - <code>Name</code> — positional constructor (from <code>defstruct</code>)<br />
  - <code>Name?</code> — type predicate (from <code>defstruct</code>)<br />
  - <code>->Name</code> — positional factory, identical to <code>Name</code><br /><br />
Unlike <code>defrecord</code>, no <code>map->Name</code> factory is generated.<br /><br />
An optional tail of protocol/method forms is spliced into an <code>extend-type</code><br />
  call. Only Phel protocols are supported in the inline tail.<br /><br />
Deviation from Clojure: Phel's <code>deftype</code> shares the map-backed<br />
  <code>defstruct</code> infrastructure, so instances remain map-like (keys are<br />
  accessible via <code>get</code>). Clojure's <code>deftype</code> produces a non-map type;<br />
  if you need that semantic, use native PHP interop.
""",
            example = "(deftype PointT [x y] Drawable (draw [this canvas] ...))",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/protocols.phel#L676",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "derive",
        signature = "(derive child parent)\n(derive h child parent)",
        completion = CompletionInfo(
            tailText = "Establishes a parent/child relationship between child and parent keywords",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Establishes a parent/child relationship between child and parent keywords. With two arguments, mutates the global hierarchy and returns nil. With a hierarchy <code>h</code>, returns a new hierarchy with the relationship added; the original hierarchy is not modified. Throws on cyclic derivation.
""",
            example = "(derive ::square ::shape)",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/protocols.phel#L255",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "descendants",
        signature = "(descendants tag)\n(descendants h tag)",
        completion = CompletionInfo(
            tailText = "Returns the set of all descendants of tag, or nil",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns the set of all descendants of tag, or nil.<br />
  Only <code>derive</code>-recorded relationships are walked: type inheritance<br />
  (PHP class hierarchy, protocol extensions) is not surfaced, matching<br />
  Clojure's contract.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/protocols.phel#L329",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "eval",
        signature = "(eval form)",
        completion = CompletionInfo(
            tailText = "Evaluates a form and return the evaluated results",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Evaluates a form and return the evaluated results.",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/protocols.phel#L978",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "extend-protocol",
        signature = "(extend-protocol protocol-name & specs)",
        completion = CompletionInfo(
            tailText = "Convenience macro that extends a single protocol to multiple types",
            priority = PhelCompletionPriority.MACROS,
        ),
        documentation = DocumentationInfo(
            summary = """
Convenience macro that extends a single protocol to multiple types. Alternates type-specs and method implementations.<br /><br />
Equivalent to multiple <code>extend-type</code> calls.
""",
            example = "(extend-protocol Describable\n  :string (describe [s] s)\n  :int (describe [n] (str n)))",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/protocols.phel#L576",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "extend-type",
        signature = "(extend-type type-spec & specs)",
        completion = CompletionInfo(
            tailText = "Extends a type with protocol method implementations",
            priority = PhelCompletionPriority.MACROS,
        ),
        documentation = DocumentationInfo(
            summary = """
Extends a type with protocol method implementations.<br /><br />
type-spec can be:<br />
  - <code>nil</code> for the nil type<br />
  - a type keyword matching what <code>type</code> returns: <code>:string</code>, <code>:int</code>, <code>:float</code>,<br />
    <code>:boolean</code>, <code>:keyword</code>, <code>:symbol</code>, <code>:vector</code>, <code>:list</code>, <code>:hash-map</code>, <code>:set</code>,<br />
    <code>:atom</code>, <code>:var</code>, <code>:function</code>, <code>:php/array</code><br />
  - a symbol for struct names (resolved in current namespace)<br />
  - a string for explicit PHP class names (cross-namespace structs)<br /><br />
Note: <code>:struct</code> and <code>:php/object</code> cannot be used as type-specs because<br />
  protocol dispatch resolves these to their specific PHP class names.<br />
  Use a struct symbol or PHP class name string instead.
""",
            example = "(extend-type :string Stringable (to-string [s] s))",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/protocols.phel#L498",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "extends?",
        signature = "(extends? protocol type-key)",
        completion = CompletionInfo(
            tailText = "Returns true if the given type-key has implementations for all methods of the protocol",
            priority = PhelCompletionPriority.PREDICATE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns true if the given type-key has implementations for all methods of the protocol. type-key should match what protocol-type-key returns.
""",
            example = "(extends? Stringable :string)",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/protocols.phel#L566",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "find-hierarchy-method",
        signature = "(find-hierarchy-method methods dispatch-val)\n(find-hierarchy-method methods dispatch-val prefers-map)",
        completion = CompletionInfo(
            tailText = "Finds the best matching method for dispatch-val using the global hierarchy",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Finds the best matching method for dispatch-val using the global hierarchy.<br />
  Returns the method function or nil. Used internally by defmulti. When<br />
  multiple methods match and none is more specific, consults <code>prefers-map</code><br />
  (built by <code>prefer-method</code>); if still ambiguous, throws.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/protocols.phel#L368",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "full-name",
        signature = "(full-name x)",
        completion = CompletionInfo(
            tailText = "Return the namespace and name string of a string, keyword or symbol",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Return the namespace and name string of a string, keyword or symbol.",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/protocols.phel#L964",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "if-let",
        signature = "(if-let bindings then & [else])",
        completion = CompletionInfo(
            tailText = "If test is true, evaluates then with binding-form bound to the value of test, if not, yields else",
            priority = PhelCompletionPriority.MACROS,
        ),
        documentation = DocumentationInfo(
            summary = """
If test is true, evaluates then with binding-form bound to the value of test, if not, yields else
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/protocols.phel#L820",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "if-some",
        signature = "(if-some bindings then & [else])",
        completion = CompletionInfo(
            tailText = "Binds name to the value of test",
            priority = PhelCompletionPriority.MACROS,
        ),
        documentation = DocumentationInfo(
            summary = """
Binds name to the value of test. If test is not nil, evaluates then with binding-form bound to the value of test, if not, yields else. Unlike if-let, false and 0 are not treated as falsy — only nil triggers the else branch.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/protocols.phel#L859",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "isa?",
        signature = "(isa? child parent)\n(isa? h child parent)",
        completion = CompletionInfo(
            tailText = "Returns true if child equals parent, or child is a descendant of parent",
            priority = PhelCompletionPriority.PREDICATE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns true if child equals parent, or child is a descendant of parent. When a hierarchy is provided, the check is performed against it; otherwise the global hierarchy is used.
""",
            example = "(do (derive ::square ::shape) (isa? ::square ::shape)) ; =&gt; true",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/protocols.phel#L245",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "letfn",
        signature = "(letfn bindings & body)",
        completion = CompletionInfo(
            tailText = "Defines mutually recursive local functions",
            priority = PhelCompletionPriority.MACROS,
        ),
        documentation = DocumentationInfo(
            summary = """
Defines mutually recursive local functions.<br /><br />
bindings is a vector of function specs: (letfn [(f [params] body) (g [params] body)] expr) All function names are in scope within all function bodies and the body expression, enabling mutual recursion.
""",
            example = "(letfn [(my-even? [n] (if (zero? n) true (my-odd? (dec n))))\n        (my-odd? [n] (if (zero? n) false (my-even? (dec n))))]\n  (my-even? 10))",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/protocols.phel#L907",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "make-hierarchy",
        signature = "(make-hierarchy)",
        completion = CompletionInfo(
            tailText = "Creates a fresh, empty hierarchy",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Creates a fresh, empty hierarchy.<br /><br />
Returns a map with <code>:parents</code>, <code>:descendants</code>, and <code>:ancestors</code> keys, each holding an empty map. Matches Clojure's hierarchy shape so consumers can destructure any of the three relationship views.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/protocols.phel#L54",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "name",
        signature = "(name x)",
        completion = CompletionInfo(
            tailText = "Returns the name string of a string, keyword or symbol",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Returns the name string of a string, keyword or symbol.",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/protocols.phel#L954",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "namespace",
        signature = "(namespace x)",
        completion = CompletionInfo(
            tailText = "Return the namespace string of a symbol or keyword",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Return the namespace string of a symbol or keyword. Nil if not present.",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/protocols.phel#L959",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "parents",
        signature = "(parents tag)\n(parents h tag)",
        completion = CompletionInfo(
            tailText = "Returns the set of immediate parents of tag, or nil",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns the set of immediate parents of tag, or nil. When a hierarchy is provided, consults it; otherwise uses the global hierarchy.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/protocols.phel#L307",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "prefer-method",
        signature = "(prefer-method multi-name dispatch-val-x dispatch-val-y)",
        completion = CompletionInfo(
            tailText = "Causes the multimethod to prefer matches of dispatch-val-x over dispatch-val-y when there is an a...",
            priority = PhelCompletionPriority.MACROS,
        ),
        documentation = DocumentationInfo(
            summary = """
Causes the multimethod to prefer matches of <code>dispatch-val-x</code> over<br />
  <code>dispatch-val-y</code> when there is an ambiguity (both match via the hierarchy<br />
  and neither is more specific). Preferences are transitive.<br /><br />
Throws if the new preference would create a cycle, i.e. <code>y</code> is already<br />
  (transitively) preferred over <code>x</code>.
""",
            example = "(prefer-method draw :drawable :shape)",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/protocols.phel#L780",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "prefers",
        signature = "(prefers multi-name)",
        completion = CompletionInfo(
            tailText = "Returns the preference map for a multimethod (built by prefer-method)",
            priority = PhelCompletionPriority.MACROS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns the preference map for a multimethod (built by <code>prefer-method</code>). Keys are dispatch values; values are sets of dispatch values they are preferred over (directly; preference is transitive when consulted).
""",
            example = "(prefers draw)",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/protocols.phel#L809",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "prefers?",
        signature = "(prefers? prefers-map x y)",
        completion = CompletionInfo(
            tailText = "Returns true if dispatch value x is preferred over y in prefers-map, considering transitive prefe...",
            priority = PhelCompletionPriority.PREDICATE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns true if dispatch value <code>x</code> is preferred over <code>y</code> in <code>prefers-map</code>, considering transitive preferences. Used by multimethod dispatch when multiple methods match and the hierarchy does not pick a unique winner.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/protocols.phel#L350",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "protocol-type-key",
        signature = "(protocol-type-key x)",
        completion = CompletionInfo(
            tailText = "Returns the dispatch key for protocol dispatch",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns the dispatch key for protocol dispatch. Returns a type keyword<br />
  for primitive types, or the PHP class name string for objects/structs.<br /><br />
Optimized to avoid the full <code>type</code> cond chain: checks scalars first<br />
  (most common in tight loops), then objects.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/protocols.phel#L409",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "read-string",
        signature = "(read-string s)",
        completion = CompletionInfo(
            tailText = "Reads the first phel expression from the string s",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Reads the first phel expression from the string s.",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/protocols.phel#L969",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "register-protocol-implementor!",
        signature = "(register-protocol-implementor! type-key protocol-value protocol-tag)",
        completion = CompletionInfo(
            tailText = "Records that type-key implements the protocol identified by both protocol-value (the defprotocol ...",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Records that <code>type-key</code> implements the protocol identified by both<br />
  <code>protocol-value</code> (the <code>defprotocol</code> map) and <code>protocol-tag</code> (its FQN<br />
  string). Called from <code>extend-type</code>'s expansion.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/protocols.phel#L126",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "reify",
        signature = "(reify & specs)",
        completion = CompletionInfo(
            tailText = "Creates an anonymous object implementing one or more protocols",
            priority = PhelCompletionPriority.MACROS,
        ),
        documentation = DocumentationInfo(
            summary = """
Creates an anonymous object implementing one or more protocols.<br />
  Method bodies close over local bindings. Each instance carries its<br />
  own captured state, so reify works correctly inside loops.<br /><br />
Syntax:<br />
    (reify<br />
      ProtocolName<br />
      (method-name [this arg1] body)<br />
      AnotherProtocol<br />
      (another-method [this] body))
""",
            example = "(reify Speakable (speak [this] \"hello\"))",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/protocols.phel#L590",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "resolve",
        signature = "(resolve sym)",
        completion = CompletionInfo(
            tailText = "Resolves the given symbol in the current environment and returns a resolved Symbol with the absol...",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Resolves the given symbol in the current environment and returns a resolved Symbol with the absolute namespace or nil if it cannot be resolved.
""",
            example = "(resolve 'map) ; =&gt; phel.core/map",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/protocols.phel#L996",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "satisfies?",
        signature = "(satisfies? protocol x)",
        completion = CompletionInfo(
            tailText = "Returns true if x's type implements all methods of the given protocol",
            priority = PhelCompletionPriority.PREDICATE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Returns true if x's type implements all methods of the given protocol.",
            example = "(satisfies? Stringable \"hello\")",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/protocols.phel#L555",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "time",
        signature = "(time expr)",
        completion = CompletionInfo(
            tailText = "Evaluates expr and prints the time it took",
            priority = PhelCompletionPriority.MACROS,
        ),
        documentation = DocumentationInfo(
            summary = "Evaluates expr and prints the time it took. Returns the value of expr.",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/protocols.phel#L935",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "underive",
        signature = "(underive child parent)\n(underive h child parent)",
        completion = CompletionInfo(
            tailText = "Removes a parent/child relationship",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Removes a parent/child relationship. With two arguments, mutates the global hierarchy and returns nil. With a hierarchy <code>h</code>, returns a new hierarchy without the relationship; the original is unchanged.
""",
            example = "(underive :square :shape)",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/protocols.phel#L282",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "when-first",
        signature = "(when-first bindings & body)",
        completion = CompletionInfo(
            tailText = "Binds name to the first element of coll",
            priority = PhelCompletionPriority.MACROS,
        ),
        documentation = DocumentationInfo(
            summary = """
Binds name to the first element of coll. When the collection is non-empty<br />
  (first returns non-nil), evaluates body with the binding.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/protocols.phel#L886",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "when-let",
        signature = "(when-let bindings & body)",
        completion = CompletionInfo(
            tailText = "When test is true, evaluates body with binding-form bound to the value of test",
            priority = PhelCompletionPriority.MACROS,
        ),
        documentation = DocumentationInfo(
            summary = "When test is true, evaluates body with binding-form bound to the value of test",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/protocols.phel#L840",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "when-some",
        signature = "(when-some bindings & body)",
        completion = CompletionInfo(
            tailText = "Binds name to the value of test",
            priority = PhelCompletionPriority.MACROS,
        ),
        documentation = DocumentationInfo(
            summary = """
Binds name to the value of test. When test is not nil, evaluates body with binding-form bound to the value of test. Unlike when-let, false and 0 are not treated as falsy — only nil causes the body to be skipped.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/protocols.phel#L880",
                docs = "",
            ),
        ),
    )
)
