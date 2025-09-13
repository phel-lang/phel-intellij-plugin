package org.phellang.completion

import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.icons.AllIcons

/**
 * PHP interoperability completions
 */
object PhelPhpInteropCompletions {
    private val PHP_FUNCTION_ICON = AllIcons.Nodes.Method
    private val PHP_VARIABLE_ICON = AllIcons.Nodes.Variable

    /**
     * Add PHP interop completions
     */
    @JvmStatic
    fun addPhpInterop(result: CompletionResultSet) {
        // Core PHP interop functions
        addPhpFunction(result, "php/new", "(php/new class & args)", "Create PHP object instance")
        addPhpFunction(result, "php/->", "(php/-> object method & args)", "Call PHP object method")
        addPhpFunction(result, "php/oget", "(php/oget object property)", "Get PHP object property")
        addPhpFunction(result, "php/oset", "(php/oset object property value)", "Set PHP object property")
        addPhpFunction(result, "php/::", "(php/:: class method & args)", "Call PHP static method")
        addPhpFunction(result, "php/::get", "(php/::get class property)", "Get PHP static property")
        addPhpFunction(result, "php/::set", "(php/::set class property value)", "Set PHP static property")

        // PHP array functions
        addPhpFunction(result, "php/aget", "(php/aget array key)", "Get PHP array value")
        addPhpFunction(result, "php/aset", "(php/aset array key value)", "Set PHP array value")
        addPhpFunction(result, "php/apush", "(php/apush array value)", "Append to PHP array")
        addPhpFunction(result, "php/aunset", "(php/aunset array key)", "Unset PHP array key")
        addPhpFunction(result, "php/array", "(php/array & values)", "Create PHP array")

        // PHP superglobals
        addPhpVariable(result, "php/\$_SERVER", "PHP \$_SERVER superglobal")
        addPhpVariable(result, "php/\$_GET", "PHP \$_GET superglobal")
        addPhpVariable(result, "php/\$_POST", "PHP \$_POST superglobal")
        addPhpVariable(result, "php/\$_COOKIE", "PHP \$_COOKIE superglobal")
        addPhpVariable(result, "php/\$_FILES", "PHP \$_FILES superglobal")
        addPhpVariable(result, "php/\$_SESSION", "PHP \$_SESSION superglobal")
        addPhpVariable(result, "php/\$_ENV", "PHP \$_ENV superglobal")
        addPhpVariable(result, "php/\$GLOBALS", "PHP \$GLOBALS superglobal")

        // Common PHP functions (prefixed with php/)
        addPhpFunction(result, "php/strlen", "(php/strlen string)", "Get string length")
        addPhpFunction(result, "php/substr", "(php/substr string start length?)", "Get substring")
        addPhpFunction(result, "php/strpos", "(php/strpos string needle offset?)", "Find string position")
        addPhpFunction(result, "php/str_replace", "(php/str_replace search replace string)", "String replacement")
        addPhpFunction(result, "php/explode", "(php/explode delimiter string)", "Split string into array")
        addPhpFunction(result, "php/implode", "(php/implode delimiter array)", "Join array into string")
        addPhpFunction(result, "php/trim", "(php/trim string)", "Trim whitespace")
        addPhpFunction(result, "php/strtolower", "(php/strtolower string)", "Convert to lowercase")
        addPhpFunction(result, "php/strtoupper", "(php/strtoupper string)", "Convert to uppercase")

        // PHP array functions
        addPhpFunction(result, "php/count", "(php/count array)", "Count array elements")
        addPhpFunction(result, "php/array_merge", "(php/array_merge array1 array2)", "Merge arrays")
        addPhpFunction(result, "php/array_keys", "(php/array_keys array)", "Get array keys")
        addPhpFunction(result, "php/array_values", "(php/array_values array)", "Get array values")
        addPhpFunction(result, "php/array_map", "(php/array_map callback array)", "Map function over array")
        addPhpFunction(result, "php/array_filter", "(php/array_filter array callback?)", "Filter array")
        addPhpFunction(result, "php/array_reduce", "(php/array_reduce array callback initial?)", "Reduce array")
        addPhpFunction(result, "php/in_array", "(php/in_array needle haystack)", "Check if value in array")
        addPhpFunction(result, "php/array_search", "(php/array_search needle haystack)", "Search for value")

        // PHP date/time functions
        addPhpFunction(result, "php/date", "(php/date format timestamp?)", "Format date")
        addPhpFunction(result, "php/time", "(php/time)", "Current timestamp")
        addPhpFunction(result, "php/microtime", "(php/microtime get_as_float?)", "Microtime")
        addPhpFunction(result, "php/strtotime", "(php/strtotime string)", "Parse time string")

        // PHP file functions
        addPhpFunction(result, "php/file_get_contents", "(php/file_get_contents filename)", "Read file contents")
        addPhpFunction(result, "php/file_put_contents", "(php/file_put_contents filename data)", "Write file contents")
        addPhpFunction(result, "php/file_exists", "(php/file_exists filename)", "Check if file exists")
        addPhpFunction(result, "php/is_file", "(php/is_file filename)", "Check if is file")
        addPhpFunction(result, "php/is_dir", "(php/is_dir filename)", "Check if is directory")

        // PHP utility functions
        addPhpFunction(result, "php/isset", "(php/isset var)", "Check if variable is set")
        addPhpFunction(result, "php/empty", "(php/empty var)", "Check if variable is empty")
        addPhpFunction(result, "php/is_null", "(php/is_null var)", "Check if variable is null")
        addPhpFunction(result, "php/gettype", "(php/gettype var)", "Get variable type")
        addPhpFunction(result, "php/var_dump", "(php/var_dump var)", "Dump variable information")
        addPhpFunction(result, "php/print_r", "(php/print_r var)", "Print variable")

        // PHP regex functions
        addPhpFunction(result, "php/preg_match", "(php/preg_match pattern string)", "Regex match")
        addPhpFunction(result, "php/preg_match_all", "(php/preg_match_all pattern string)", "Regex match all")
        addPhpFunction(result, "php/preg_replace", "(php/preg_replace pattern replacement string)", "Regex replace")
        addPhpFunction(result, "php/preg_split", "(php/preg_split pattern string)", "Regex split")

        // PHP URL functions
        addPhpFunction(result, "php/urlencode", "(php/urlencode string)", "URL encode")
        addPhpFunction(result, "php/urldecode", "(php/urldecode string)", "URL decode")
        addPhpFunction(result, "php/parse_url", "(php/parse_url url)", "Parse URL")
        addPhpFunction(result, "php/http_build_query", "(php/http_build_query data)", "Build query string")

        // PHP JSON functions
        addPhpFunction(result, "php/json_encode", "(php/json_encode value)", "Encode to JSON")
        addPhpFunction(result, "php/json_decode", "(php/json_decode json)", "Decode from JSON")

        // PHP hash functions
        addPhpFunction(result, "php/md5", "(php/md5 string)", "MD5 hash")
        addPhpFunction(result, "php/sha1", "(php/sha1 string)", "SHA1 hash")
        addPhpFunction(result, "php/hash", "(php/hash algo string)", "Generic hash")

        // PHP include/require
        addPhpFunction(result, "php/require", "(php/require filename)", "Require PHP file")
        addPhpFunction(result, "php/require_once", "(php/require_once filename)", "Require PHP file once")
        addPhpFunction(result, "php/include", "(php/include filename)", "Include PHP file")
        addPhpFunction(result, "php/include_once", "(php/include_once filename)", "Include PHP file once")

        // Type conversion functions
        addPhpFunction(result, "php->phel", "(php->phel value)", "Convert PHP value to Phel")
        addPhpFunction(result, "phel->php", "(phel->php value)", "Convert Phel value to PHP")
        addPhpFunction(result, "php-array-to-map", "(php-array-to-map array)", "Convert PHP array to Phel map")

        // PHP constants
        addPhpVariable(result, "php/PHP_VERSION", "PHP version constant")
        addPhpVariable(result, "php/PHP_OS", "PHP operating system constant")
        addPhpVariable(result, "php/TRUE", "PHP TRUE constant")
        addPhpVariable(result, "php/FALSE", "PHP FALSE constant")
        addPhpVariable(result, "php/NULL", "PHP NULL constant")
    }

    private fun addPhpFunction(result: CompletionResultSet, name: String, signature: String?, description: String?) {
        result.addElement(
            createNamespacedLookupElement(
                name, PHP_FUNCTION_ICON, signature, " - $description"
            )
        )
    }

    private fun addPhpVariable(result: CompletionResultSet, name: String, description: String?) {
        result.addElement(
            createNamespacedLookupElement(
                name, PHP_VARIABLE_ICON, "PHP variable", " - $description"
            )
        )
    }
}
