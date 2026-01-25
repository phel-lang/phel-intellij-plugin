package org.phellang.completion.data

import org.phellang.completion.infrastructure.PhelCompletionPriority

internal fun registerPhpInteropFunctions(): List<PhelFunction> = listOf(
    PhelFunction(
        namespace = "php",
        name = "php/->",
        signature = "(php/-> object call*)",
        completion = CompletionInfo(
            tailText = "Access to an object property or result of chained calls",
            priority = PhelCompletionPriority.SPECIAL_FORMS,
        ),
        documentation = DocumentationInfo(
            summary = "Access to an object property or result of chained calls.",
            example = "(php/-&gt; date (format \"Y-m-d\"))",
            links = DocumentationLinks(
                github = "",
                docs = "/documentation/php-interop/#php-set-object-properties",
            ),
        ),
    ),
    PhelFunction(
        namespace = "php",
        name = "php/::",
        signature = "(php/:: class (method-name expr*))",
        completion = CompletionInfo(
            tailText = "Calls a static method or property from a PHP class",
            priority = PhelCompletionPriority.SPECIAL_FORMS,
        ),
        documentation = DocumentationInfo(
            summary = """
Calls a static method or property from a PHP class. Both method-name and property must be symbols and cannot be an evaluated value.
""",
            example = "(php/:: DateTime (createFromFormat \"Y-m-d\" \"2024-01-01\"))",
            links = DocumentationLinks(
                github = "",
                docs = "/documentation/php-interop/#php-static-method-and-property-call",
            ),
        ),
    ),
    PhelFunction(
        namespace = "php",
        name = "php/aget",
        signature = "(php/aget arr index)",
        completion = CompletionInfo(
            tailText = "Equivalent to PHP's arr[index]",
            priority = PhelCompletionPriority.SPECIAL_FORMS,
        ),
        documentation = DocumentationInfo(
            summary = """
Equivalent to PHP's <code>arr[index] ?? null</code>.
""",
            example = "(php/aget (php/array \"a\" \"b\" \"c\") 1) ; =&gt; \"b\"",
            links = DocumentationLinks(
                github = "",
                docs = "/documentation/php-interop/#get-php-array-value",
            ),
        ),
    ),
    PhelFunction(
        namespace = "php",
        name = "php/aget-in",
        signature = "(php/aget-in arr ks)",
        completion = CompletionInfo(
            tailText = "Equivalent to PHP's arr[k1][k2][k",
            priority = PhelCompletionPriority.SPECIAL_FORMS,
        ),
        documentation = DocumentationInfo(
            summary = """
Equivalent to PHP's <code>arr[k1][k2][k...] ?? null</code>.
""",
            example = "(php/aget-in nested-arr [\"users\" 0 \"name\"])",
            links = DocumentationLinks(
                github = "",
                docs = "/documentation/php-interop/#get-php-array-value",
            ),
        ),
    ),
    PhelFunction(
        namespace = "php",
        name = "php/apush",
        signature = "(php/apush arr value)",
        completion = CompletionInfo(
            tailText = "Equivalent to PHP's arr[] = value",
            priority = PhelCompletionPriority.SPECIAL_FORMS,
        ),
        documentation = DocumentationInfo(
            summary = "Equivalent to PHP's arr[] = value.",
            example = "(php/apush arr \"new-item\")",
            links = DocumentationLinks(
                github = "",
                docs = "/documentation/php-interop/#append-php-array-value",
            ),
        ),
    ),
    PhelFunction(
        namespace = "php",
        name = "php/apush-in",
        signature = "(php/apush-in arr ks value)",
        completion = CompletionInfo(
            tailText = "Equivalent to PHP's arr[k1][k2][k",
            priority = PhelCompletionPriority.SPECIAL_FORMS,
        ),
        documentation = DocumentationInfo(
            summary = """
Equivalent to PHP's <code>arr[k1][k2][k...][] = value</code>.
""",
            example = "(php/apush-in arr [\"users\"] {:name \"Bob\"})",
            links = DocumentationLinks(
                github = "",
                docs = "/documentation/php-interop/#append-php-array-value",
            ),
        ),
    ),
    PhelFunction(
        namespace = "php",
        name = "php/aset",
        signature = "(php/aset arr index value)",
        completion = CompletionInfo(
            tailText = "Equivalent to PHP's arr[index] = value",
            priority = PhelCompletionPriority.SPECIAL_FORMS,
        ),
        documentation = DocumentationInfo(
            summary = """
Equivalent to PHP's <code>arr[index] = value</code>.
""",
            example = "(php/aset arr 0 \"new-value\")",
            links = DocumentationLinks(
                github = "",
                docs = "/documentation/php-interop/#set-php-array-value",
            ),
        ),
    ),
    PhelFunction(
        namespace = "php",
        name = "php/aset-in",
        signature = "(php/aset-in arr ks value)",
        completion = CompletionInfo(
            tailText = "Equivalent to PHP's arr[k1][k2][k",
            priority = PhelCompletionPriority.SPECIAL_FORMS,
        ),
        documentation = DocumentationInfo(
            summary = """
Equivalent to PHP's <code>arr[k1][k2][k...] = value</code>.
""",
            example = "(php/aset-in arr [\"users\" 0 \"name\"] \"Alice\")",
            links = DocumentationLinks(
                github = "",
                docs = "/documentation/php-interop/#set-php-array-value",
            ),
        ),
    ),
    PhelFunction(
        namespace = "php",
        name = "php/aunset",
        signature = "(php/aunset arr index)",
        completion = CompletionInfo(
            tailText = "Equivalent to PHP's unset(arr[index])",
            priority = PhelCompletionPriority.SPECIAL_FORMS,
        ),
        documentation = DocumentationInfo(
            summary = """
Equivalent to PHP's <code>unset(arr[index])</code>.
""",
            example = "(php/aunset arr \"key-to-remove\")",
            links = DocumentationLinks(
                github = "",
                docs = "/documentation/php-interop/#unset-php-array-value",
            ),
        ),
    ),
    PhelFunction(
        namespace = "php",
        name = "php/aunset-in",
        signature = "(php/aunset-in arr ks)",
        completion = CompletionInfo(
            tailText = "Equivalent to PHP's unset(arr[k1][k2][k",
            priority = PhelCompletionPriority.SPECIAL_FORMS,
        ),
        documentation = DocumentationInfo(
            summary = """
Equivalent to PHP's <code>unset(arr[k1][k2][k...])</code>.
""",
            example = "(php/aunset-in arr [\"users\" 0])",
            links = DocumentationLinks(
                github = "",
                docs = "/documentation/php-interop/#unset-php-array-value",
            ),
        ),
    ),
    PhelFunction(
        namespace = "php",
        name = "php/new",
        signature = "(php/new expr args*)",
        completion = CompletionInfo(
            tailText = "Evaluates expr and creates a new PHP class using the arguments",
            priority = PhelCompletionPriority.SPECIAL_FORMS,
        ),
        documentation = DocumentationInfo(
            summary = """
Evaluates expr and creates a new PHP class using the arguments. The instance of the class is returned.
""",
            example = "(php/new DateTime \"2024-01-01\")",
            links = DocumentationLinks(
                github = "",
                docs = "/documentation/php-interop/#php-class-instantiation",
            ),
        ),
    ),
    PhelFunction(
        namespace = "php",
        name = "php/oset",
        signature = "(php/oset (php/-> object property) value)",
        completion = CompletionInfo(
            tailText = "Use php/oset to set a value to a class/object property",
            priority = PhelCompletionPriority.SPECIAL_FORMS,
        ),
        documentation = DocumentationInfo(
            summary = """
Use <code>php/oset</code> to set a value to a class/object property.
""",
            example = "(php/oset (php/-&gt; obj name) \"Alice\")",
            links = DocumentationLinks(
                github = "",
                docs = "/documentation/php-interop/#php-set-object-properties",
            ),
        ),
    )
)
