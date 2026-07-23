package org.phellang.registry.data

import org.phellang.registry.CompletionInfo
import org.phellang.registry.DocumentationInfo
import org.phellang.registry.DocumentationLinks
import org.phellang.registry.DeprecationInfo
import org.phellang.registry.PhelFunction

import org.phellang.registry.PhelCompletionPriority

internal fun registerReflectFunctions(): List<PhelFunction> = listOf(
    PhelFunction(
        namespace = "reflect",
        name = "reflect/attributes",
        signature = "(attributes obj-or-class)",
        completion = CompletionInfo(
            tailText = "Returns the class-level attribute maps for obj-or-class; an instance uses its class",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns the class-level attribute maps for <code>obj-or-class</code>; an instance uses its class. Convenience alias of <code>class-attributes</code>.
""",
            example = "(attributes my-controller)",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/reflect.phel#L117",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "reflect",
        name = "reflect/class-attributes",
        signature = "(class-attributes class-or-name)",
        completion = CompletionInfo(
            tailText = "Returns a vector of attribute maps for the class class-or-name (string FQN or object)",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns a vector of attribute maps for the class <code>class-or-name</code> (string FQN or object). Each map is <code>{:name <attribute-fqcn> :args {...}}</code>, where <code>:args</code> keys named arguments as keywords and positional arguments by index.
""",
            example = "(class-attributes \\App\\Controller\\ProductController)",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/reflect.phel#L96",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "reflect",
        name = "reflect/class-info",
        signature = "(class-info class-or-name)",
        completion = CompletionInfo(
            tailText = "Returns a map describing class-or-name with :name, :methods, :properties, :parents, and :interfac...",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns a map describing <code>class-or-name</code> with <code>:name</code>, <code>:methods</code>,<br />
  <code>:properties</code>, <code>:parents</code>, and <code>:interfaces</code> keys.
""",
            example = "(class-info \\DateTime)",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/reflect.phel#L124",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "reflect",
        name = "reflect/enum->keyword",
        signature = "(enum->keyword enum-case)",
        completion = CompletionInfo(
            tailText = "Returns the keyword for a native PHP enum enum-case, using the case name verbatim (Status/Active ...",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns the keyword for a native PHP enum <code>enum-case</code>, using the case name<br />
  verbatim (<code>Status/Active</code> => <code>:Active</code>). Round-trips with <code>keyword->enum</code>.
""",
            example = "(enum-&gt;keyword Status/Active)",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/reflect.phel#L147",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "reflect",
        name = "reflect/enum-values",
        signature = "(enum-values enum-class)",
        completion = CompletionInfo(
            tailText = "Returns a vector of keywords, one per case of the native PHP enum enum-class (string FQN), in dec...",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns a vector of keywords, one per case of the native PHP enum<br />
  <code>enum-class</code> (string FQN), in declaration order.
""",
            example = "(enum-values \\App\\Status)",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/reflect.phel#L164",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "reflect",
        name = "reflect/keyword->enum",
        signature = "(keyword->enum enum-class kw)",
        completion = CompletionInfo(
            tailText = "Returns the case of the native PHP enum enum-class (string FQN) whose name matches keyword kw, or...",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns the case of the native PHP enum <code>enum-class</code> (string FQN) whose name matches keyword <code>kw</code>, or nil when no case matches. Inverse of <code>enum->keyword</code>.
""",
            example = "(keyword-&gt;enum \\App\\Status :Active)",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/reflect.phel#L155",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "reflect",
        name = "reflect/method-attributes",
        signature = "(method-attributes class-or-name method-name)",
        completion = CompletionInfo(
            tailText = "Returns a vector of attribute maps for method method-name on class-or-name",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns a vector of attribute maps for method <code>method-name</code> on <code>class-or-name</code>.
""",
            example = "(method-attributes \\App\\Foo \"handle\")",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/reflect.phel#L103",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "reflect",
        name = "reflect/methods",
        signature = "(methods class-or-name)",
        completion = CompletionInfo(
            tailText = "Returns a vector of method maps for class-or-name",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns a vector of method maps for <code>class-or-name</code>. Each map contains<br />
  <code>:name</code>, <code>:params</code>, <code>:return-type</code>, <code>:static?</code>, and <code>:visibility</code>.
""",
            example = "(methods DateTime)",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/reflect.phel#L46",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "reflect",
        name = "reflect/properties",
        signature = "(properties class-or-name)",
        completion = CompletionInfo(
            tailText = "Returns a vector of property maps for class-or-name",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns a vector of property maps for <code>class-or-name</code>. Each map contains<br />
  <code>:name</code>, <code>:type</code>, <code>:static?</code>, <code>:readonly?</code>, and <code>:visibility</code>.
""",
            example = "(properties \\DateInterval)",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/reflect.phel#L55",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "reflect",
        name = "reflect/property-attributes",
        signature = "(property-attributes class-or-name property-name)",
        completion = CompletionInfo(
            tailText = "Returns a vector of attribute maps for property property-name on class-or-name",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns a vector of attribute maps for property <code>property-name</code> on <code>class-or-name</code>.
""",
            example = "(property-attributes \\App\\Foo \"id\")",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/reflect.phel#L110",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "reflect",
        name = "reflect/supers",
        signature = "(supers class-or-name)",
        completion = CompletionInfo(
            tailText = "Returns a set of fully qualified parent classes and implemented interfaces for class-or-name",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns a set of fully qualified parent classes and implemented interfaces for <code>class-or-name</code>.
""",
            example = "(supers \\RuntimeException)",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/reflect.phel#L64",
                docs = "",
            ),
        ),
    )
)
