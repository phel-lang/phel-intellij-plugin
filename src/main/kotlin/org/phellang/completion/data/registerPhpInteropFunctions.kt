package org.phellang.completion.data

import org.phellang.completion.infrastructure.PhelCompletionPriority

internal fun registerPhpInteropFunctions(): List<DataFunction> = listOf(
    DataFunction("php/->", "(php/-> object call*)", PhelCompletionPriority.PHP_INTEROP, "php", "Access to an object property or result of chained calls", """
<br /><code>(php/-> object call*)<br />
(php/:: class call*)</code><br /><br />
Access to an object property or result of chained calls.<br />
<br />
"""),
    DataFunction("php/::", "(php/:: class call*)", PhelCompletionPriority.PHP_INTEROP, "php", "Calls a static method or property from a PHP class. Both methodname and property must be symbols and cannot be an evaluated value", """
<br /><code>(php/:: class (methodname expr*))<br />
(php/:: class call*)</code><br /><br />
<br />
Calls a static method or property from a PHP class. Both methodname and property must be symbols and cannot be an evaluated value.<br />
<br />
"""),
    DataFunction("php/aget", "(php/aget arr index)", PhelCompletionPriority.PHP_INTEROP, "php", "Equivalent to PHP's `arr[index] ?? null`", """
<br /><code>(php/aget arr index)</code><br /><br />
Equivalent to PHP's <b>arr[index] ?? null</b>.<br />
<br />
"""),
    DataFunction("php/aget-in", "(php/aget-in arr ks)", PhelCompletionPriority.PHP_INTEROP, "php", "Equivalent to PHP's `arr[k1][k2][k...] ?? null`", """
<br /><code>(php/aget-in arr ks)</code><br /><br />
Equivalent to PHP's <b>arr[k1][k2][k...] ?? null</b>.<br />
<br />
"""),
    DataFunction("php/apush", "(php/apush arr value)", PhelCompletionPriority.PHP_INTEROP, "php", "Equivalent to PHP's arr[] = value", """
<br /><code>(php/apush arr value)</code><br /><br />
Equivalent to PHP's <b>arr[] = value</b>.<br />
<br />
"""),
    DataFunction("php/apush-in", "(php/apush-in arr ks value)", PhelCompletionPriority.PHP_INTEROP, "php", "Equivalent to PHP's `arr[k1][k2][k...][] = value`", """
<br /><code>(php/apush-in arr ks value)</code><br /><br />
Equivalent to PHP's <b>arr[k1][k2][k...][] = value</b>.<br />
<br />
"""),
    DataFunction("php/aset", "(php/aset arr index value)", PhelCompletionPriority.PHP_INTEROP, "php", "Equivalent to PHP's `arr[index] = value`", """
<br /><code>(php/aset arr index value)</code><br /><br />
Equivalent to PHP's <b>arr[index] = value</b>.<br />
<br />
"""),
    DataFunction("php/aset-in", "(php/aset-in arr ks value)", PhelCompletionPriority.PHP_INTEROP, "php", "Equivalent to PHP's `arr[k1][k2][k...] = value`", """
<br /><code>(php/aset-in arr ks value)</code><br /><br />
Equivalent to PHP's <b>arr[k1][k2][k...] = value</b>.<br />
<br />
"""),
    DataFunction("php/aunset", "(php/aunset arr index)", PhelCompletionPriority.PHP_INTEROP, "php", "Equivalent to PHP's `unset(arr[index])`", """
<br /><code>(php/aunset arr index)</code><br /><br />
Equivalent to PHP's <b>unset(arr[index])</b>.<br />
<br />
"""),
    DataFunction("php/aunset-in", "(php/aunset-in arr ks)", PhelCompletionPriority.PHP_INTEROP, "php", "Equivalent to PHP's `unset(arr[k1][k2][k...])`", """
<br /><code>(php/aunset-in arr ks)</code><br /><br />
Equivalent to PHP's <b>unset(arr[k1][k2][k...])</b>.<br />
<br />
"""),
    DataFunction("php/new", "(php/new expr args*)", PhelCompletionPriority.PHP_INTEROP, "php", "Evaluates expr and creates a new PHP class using the arguments. The instance of the class is returned", """
<br /><code>(php/new expr args*)</code><br /><br />
Evaluates expr and creates a new PHP class using the arguments. The instance of the class is returned.<br />
<br />
"""),
    DataFunction("php/oset", "(php/oset (php/-> object prop) val)", PhelCompletionPriority.PHP_INTEROP, "php", "Use `php/oset` to set a value to a class/object property", """
<br /><code>(php/oset (php/-> object property) value)<br />
(php/oset (php/:: class property) value)</code><br /><br />
Use <b>php/oset</b> to set a value to a class/object property.<br />
<br />
"""),
)
