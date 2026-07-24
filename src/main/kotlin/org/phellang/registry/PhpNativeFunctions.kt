package org.phellang.registry

/**
 * Curated documentation for common native PHP functions called through the `php/` prefix
 * (e.g. `(php/strlen s)`). PHP's function reference is not part of Phel's `api.json`, so these
 * are hand-authored rather than produced by `updatePhelRegistry`; that is also why this file
 * lives at the registry root instead of the generated `registry/data/`.
 *
 * This is the phase-1 seed of a native-PHP registry (issue #240). Phase 2 would replace it with a
 * build-time generator over the full set (e.g. from phpstorm-stubs).
 *
 * Each entry links to its php.net page. The manual's function URLs are `function.<name>.php` with
 * underscores turned into hyphens (`array_map` -> `function.array-map.php`); see [phpDoc].
 */

/** The php.net manual URL for a bare PHP function name (`array_map` -> `.../function.array-map.php`). */
private fun phpDoc(function: String): String =
    "https://www.php.net/manual/en/function.${function.replace('_', '-')}.php"

private fun php(
    name: String,
    signature: String,
    summary: String,
): PhelFunction {
    val bareName = name.removePrefix("php/")
    return PhelFunction(
        namespace = "php",
        name = name,
        signature = signature,
        completion = CompletionInfo(tailText = summary, priority = PhelCompletionPriority.PHP_INTEROP),
        documentation = DocumentationInfo(
            summary = summary,
            links = DocumentationLinks(docs = phpDoc(bareName)),
        ),
    )
}

/** Native PHP library functions surfaced through the `php/` prefix. */
internal fun phpNativeFunctions(): List<PhelFunction> = listOf(
    // Strings
    php("php/strlen", "(php/strlen str)", "Returns the length of the given string in bytes."),
    php("php/str_replace", "(php/str_replace search replace subject)", "Replaces all occurrences of the search string with the replacement string."),
    php("php/substr", "(php/substr str offset & [length])", "Returns the portion of a string starting at offset."),
    php("php/strpos", "(php/strpos haystack needle & [offset])", "Returns the position of the first occurrence of a substring, or false if not found."),
    php("php/strtolower", "(php/strtolower str)", "Returns the string with all alphabetic characters converted to lowercase."),
    php("php/strtoupper", "(php/strtoupper str)", "Returns the string with all alphabetic characters converted to uppercase."),
    php("php/trim", "(php/trim str & [characters])", "Strips whitespace (or the given characters) from the start and end of a string."),
    php("php/sprintf", "(php/sprintf format & args)", "Returns a string produced according to the given format specification."),
    php("php/str_repeat", "(php/str_repeat str times)", "Returns the string repeated the given number of times."),
    php("php/ucfirst", "(php/ucfirst str)", "Returns the string with its first character uppercased."),

    // Arrays
    php("php/count", "(php/count value)", "Counts the elements of an array or a Countable."),
    php("php/array_map", "(php/array_map callback & arrays)", "Applies the callback to the elements of the given arrays and returns the results."),
    php("php/array_filter", "(php/array_filter array & [callback])", "Returns the array elements for which the callback returns true."),
    php("php/array_merge", "(php/array_merge & arrays)", "Merges the elements of one or more arrays together."),
    php("php/array_keys", "(php/array_keys array)", "Returns all the keys of an array."),
    php("php/array_values", "(php/array_values array)", "Returns all the values of an array, reindexed numerically."),
    php("php/in_array", "(php/in_array needle haystack & [strict])", "Returns true if the value exists in the array."),
    php("php/array_reduce", "(php/array_reduce array callback & [initial])", "Iteratively reduces the array to a single value using the callback."),
    php("php/array_slice", "(php/array_slice array offset & [length])", "Extracts a slice of the array."),
    php("php/array_reverse", "(php/array_reverse array & [preserve-keys])", "Returns the array with its elements in reverse order."),

    // Math
    php("php/abs", "(php/abs number)", "Returns the absolute value of a number."),
    php("php/max", "(php/max & values)", "Returns the highest of the given values."),
    php("php/min", "(php/min & values)", "Returns the lowest of the given values."),
    php("php/round", "(php/round number & [precision])", "Rounds a float to the given precision."),
    php("php/floor", "(php/floor number)", "Rounds a float down to the nearest integer."),
    php("php/ceil", "(php/ceil number)", "Rounds a float up to the nearest integer."),

    // JSON
    php("php/json_encode", "(php/json_encode value & [flags])", "Returns the JSON representation of a value."),
    php("php/json_decode", "(php/json_decode json & [associative])", "Decodes a JSON string into a PHP value."),

    // Types and conversions
    php("php/gettype", "(php/gettype value)", "Returns the type of a variable as a string."),
    php("php/intval", "(php/intval value & [base])", "Returns the integer value of a variable."),
    php("php/is_array", "(php/is_array value)", "Returns true if the value is an array."),
    php("php/is_string", "(php/is_string value)", "Returns true if the value is a string."),
)
