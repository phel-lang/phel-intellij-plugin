package org.phellang.registry.data.core

import org.phellang.registry.CompletionInfo
import org.phellang.registry.DocumentationInfo
import org.phellang.registry.DocumentationLinks
import org.phellang.registry.DeprecationInfo
import org.phellang.registry.PhelFunction

import org.phellang.registry.PhelCompletionPriority

internal fun registerCoreDefsFunctions(): List<PhelFunction> = listOf(
    PhelFunction(
        namespace = "core",
        name = "comment",
        signature = "(comment &)",
        completion = CompletionInfo(
            tailText = "Ignores the body of the comment",
            priority = PhelCompletionPriority.MACROS,
        ),
        documentation = DocumentationInfo(
            summary = "Ignores the body of the comment.",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.52.0/src/phel/core/defs.phel#L212",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "def-",
        signature = "(def- name value)",
        completion = CompletionInfo(
            tailText = "Define a private value that will not be exported",
            priority = PhelCompletionPriority.MACROS,
        ),
        documentation = DocumentationInfo(
            summary = "Define a private value that will not be exported.",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.52.0/src/phel/core/defs.phel#L137",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "defenum",
        signature = "(defenum name & cases)",
        completion = CompletionInfo(
            tailText = "Defines a native PHP enum",
            priority = PhelCompletionPriority.MACROS,
        ),
        documentation = DocumentationInfo(
            summary = """
Defines a native PHP enum. Each case is named by a keyword followed by an<br />
  optional scalar value (all <code>int</code> or all <code>string</code>); value-less cases produce a<br />
  pure enum. After the cases, an optional implementations tail (interface<br />
  symbols with their methods, and a <code>:php</code> block of plain/magic methods) is<br />
  parsed like <code>defstruct</code>, so an enum can implement interfaces and carry<br />
  methods. Also defines a <code>Name?</code> predicate.<br /><br />
(defenum Status :active "active" :inactive "inactive")<br />
      (Status? Status/active) # => true<br /><br />
(defenum Suit<br />
        :hearts :spades :clubs :diamonds<br />
        Describable<br />
        (describe [this] (.-name this))<br />
        :php<br />
        (label [this] (.-name this)))
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.52.0/src/phel/core/defs.phel#L186",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "defexception",
        signature = "(defexception name & [parent])",
        completion = CompletionInfo(
            tailText = "Define a new exception",
            priority = PhelCompletionPriority.MACROS,
        ),
        documentation = DocumentationInfo(
            summary = """
Define a new exception. Optionally pass a parent class to extend (defaults to<br />
  <code>Exception</code>), so frameworks can catch it by type, e.g.<br />
  <code>(defexception ProductNotFound RuntimeException)</code>. The parent takes any class<br />
  spelling: bare, dotted (<code>My.Ns.Error</code>), or an alias brought in with <code>:use</code>.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.52.0/src/phel/core/defs.phel#L170",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "defmacro",
        signature = "(defmacro name & fdecl)",
        completion = CompletionInfo(
            tailText = "Define a macro",
            priority = PhelCompletionPriority.MACROS,
        ),
        documentation = DocumentationInfo(
            summary = "Define a macro.",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.52.0/src/phel/core/defs.phel#L142",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "defmacro-",
        signature = "(defmacro- name & fdecl)",
        completion = CompletionInfo(
            tailText = "Define a private macro that will not be exported",
            priority = PhelCompletionPriority.MACROS,
        ),
        documentation = DocumentationInfo(
            summary = "Define a private macro that will not be exported.",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.52.0/src/phel/core/defs.phel#L152",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "defn",
        signature = "(defn name & fdecl)",
        completion = CompletionInfo(
            tailText = "Define a new global function",
            priority = PhelCompletionPriority.MACROS,
        ),
        documentation = DocumentationInfo(
            summary = "Define a new global function.",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.52.0/src/phel/core/defs.phel#L132",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "defn-",
        signature = "(defn- name & fdecl)",
        completion = CompletionInfo(
            tailText = "Define a private function that will not be exported",
            priority = PhelCompletionPriority.MACROS,
        ),
        documentation = DocumentationInfo(
            summary = "Define a private function that will not be exported.",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.52.0/src/phel/core/defs.phel#L147",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "defstruct",
        signature = "(defstruct name keys & implementations)",
        completion = CompletionInfo(
            tailText = "A Struct is a special kind of Map",
            priority = PhelCompletionPriority.MACROS,
        ),
        documentation = DocumentationInfo(
            summary = """
A Struct is a special kind of Map. It only supports a predefined number of keys and is associated to a global name. The Struct not only defines itself but also a predicate function.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.52.0/src/phel/core/defs.phel#L157",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "to-array",
        signature = "(to-array coll)",
        completion = CompletionInfo(
            tailText = "Returns a PHP array containing the elements of coll",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns a PHP array containing the elements of <code>coll</code>. Accepts any collection (vector, list, set, map, PHP array) or <code>nil</code>, which yields an empty PHP array. A map yields an indexed array of <code>[key value]</code> pairs; <code>phel->php</code> is the conversion that yields an associative array. Matches Clojure's <code>to-array</code> for <code>.cljc</code> interop — in Phel the result is a plain PHP array since PHP has no <code>Object[]</code>.
""",
            example = "(to-array [1 2 3]) ; =&gt; &lt;PHP-Array [1, 2, 3]&gt;\n(to-array nil) ; =&gt; &lt;PHP-Array []&gt;",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.52.0/src/phel/core/defs.phel#L23",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "to-php-array",
        signature = "(to-php-array coll)",
        completion = CompletionInfo(
            tailText = "Deprecated alias of to-array",
            priority = PhelCompletionPriority.DEPRECATED_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Deprecated alias of <code>to-array</code>.
""",
            example = null,
            deprecation = DeprecationInfo(version = "0.51.0", replacement = "to-array"),
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.52.0/src/phel/core/defs.phel#L48",
                docs = "",
            ),
        ),
    )
)
