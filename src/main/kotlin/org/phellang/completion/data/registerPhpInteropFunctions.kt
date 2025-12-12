package org.phellang.completion.data

import org.phellang.completion.infrastructure.PhelCompletionPriority

internal fun registerPhpInteropFunctions(): List<PhelFunction> = listOf(
    PhelFunction(
        namespace = "php",
        name = "php/->",
        signature = "(php/-> object call*)",
        completion = CompletionInfo(
            tailText = "Access to an object property or result of chained calls",
            priority = PhelCompletionPriority.MACROS,
        ),
        documentation = DocumentationInfo(
            summary = """Access to an object property or result of chained calls.""",
            example = null,
            links = DocumentationLinks(
                github = "",
                docs = "/documentation/php-interop/#php-set-object-properties",
            ),
        ),
    ),
    PhelFunction(
        namespace = "php",
        name = "php/::",
        signature = "(php/:: class call*)",
        completion = CompletionInfo(
            tailText = "Calls a static method or property from a PHP class. Both methodname and property must be symbols and cannot be an evaluated value",
            priority = PhelCompletionPriority.PHP_INTEROP,
        ),
        documentation = DocumentationInfo(
            summary = """Calls a static method or property from a PHP class. Both methodname and property must be symbols and cannot be an evaluated value.""",
            example = null,
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
            tailText = "Equivalent to PHP's arr[index] ?? null",
            priority = PhelCompletionPriority.PHP_INTEROP,
        ),
        documentation = DocumentationInfo(
            summary = """Equivalent to PHP's <b>arr[index] ?? null</b>.""",
            example = null,
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
            tailText = "Equivalent to PHP's arr[k1][k2][k...] ?? null",
            priority = PhelCompletionPriority.PHP_INTEROP,
        ),
        documentation = DocumentationInfo(
            summary = """Equivalent to PHP's <b>arr[k1][k2][k...] ?? null</b>.""",
            example = null,
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
            priority = PhelCompletionPriority.PHP_INTEROP,
        ),
        documentation = DocumentationInfo(
            summary = """Equivalent to PHP's <b>arr[] = value</b>.""",
            example = null,
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
            tailText = "Equivalent to PHP's arr[k1][k2][k...][] = value",
            priority = PhelCompletionPriority.PHP_INTEROP,
        ),
        documentation = DocumentationInfo(
            summary = """Equivalent to PHP's <b>arr[k1][k2][k...][] = value</b>.""",
            example = null,
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
            priority = PhelCompletionPriority.PHP_INTEROP,
        ),
        documentation = DocumentationInfo(
            summary = """Equivalent to PHP's <b>arr[index] = value</b>.""",
            example = null,
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
            tailText = "Equivalent to PHP's arr[k1][k2][k...] = value",
            priority = PhelCompletionPriority.PHP_INTEROP,
        ),
        documentation = DocumentationInfo(
            summary = """Equivalent to PHP's <b>arr[k1][k2][k...] = value</b>.""",
            example = null,
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
            priority = PhelCompletionPriority.PHP_INTEROP,
        ),
        documentation = DocumentationInfo(
            summary = """Equivalent to PHP's <b>unset(arr[index])</b>.""",
            example = null,
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
            tailText = "Equivalent to PHP's unset(arr[k1][k2][k...])",
            priority = PhelCompletionPriority.PHP_INTEROP,
        ),
        documentation = DocumentationInfo(
            summary = """Equivalent to PHP's <b>unset(arr[k1][k2][k...])</b>.""",
            example = null,
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
            tailText = "Evaluates expr and creates a new PHP class using the arguments. The instance of the class is returned",
            priority = PhelCompletionPriority.PHP_INTEROP,
        ),
        documentation = DocumentationInfo(
            summary = """Evaluates expr and creates a new PHP class using the arguments. The instance of the class is returned.""",
            example = null,
            links = DocumentationLinks(
                github = "",
                docs = "/documentation/php-interop/#php-class-instantiation",
            ),
        ),
    ),
    PhelFunction(
        namespace = "php",
        name = "php/oset",
        signature = "(php/oset (php/-> object prop) val)",
        completion = CompletionInfo(
            tailText = "Use php/oset to set a value to a class/object property",
            priority = PhelCompletionPriority.PHP_INTEROP,
        ),
        documentation = DocumentationInfo(
            summary = """Use <b>php/oset</b> to set a value to a class/object property.""",
            example = null,
            links = DocumentationLinks(
                github = "",
                docs = "/documentation/php-interop/#php-set-object-properties",
            ),
        ),
    ),
)
