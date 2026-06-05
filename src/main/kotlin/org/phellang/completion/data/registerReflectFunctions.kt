package org.phellang.completion.data

import org.phellang.completion.infrastructure.PhelCompletionPriority

internal fun registerReflectFunctions(): List<PhelFunction> = listOf(
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
                github = "https://github.com/phel-lang/phel-lang/blob/v0.41.0/src/phel/reflect.phel#L82",
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
                github = "https://github.com/phel-lang/phel-lang/blob/v0.41.0/src/phel/reflect.phel#L47",
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
                github = "https://github.com/phel-lang/phel-lang/blob/v0.41.0/src/phel/reflect.phel#L56",
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
Returns a set of fully qualified parent classes and implemented<br />
  interfaces for <code>class-or-name</code>.
""",
            example = "(supers \\RuntimeException)",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.41.0/src/phel/reflect.phel#L65",
                docs = "",
            ),
        ),
    )
)
