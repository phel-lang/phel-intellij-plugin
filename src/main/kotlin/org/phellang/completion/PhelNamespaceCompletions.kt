package org.phellang.completion

import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.icons.AllIcons

/**
 * Namespace-aware completions for Phel modules and functions
 */
object PhelNamespaceCompletions {
    private val MODULE_FUNCTION_ICON = AllIcons.Nodes.Method

    /**
     * Add namespace function completions
     */
    @JvmStatic
    fun addNamespaceFunctions(result: CompletionResultSet) {
        addCoreModuleFunctions(result) // Core Phel modules
        addStringModuleFunctions(result) // String module (str/)
        addHtmlModuleFunctions(result) // HTML module (html/)
        addHttpModuleFunctions(result) // HTTP module (http/)
        addJsonModuleFunctions(result) // JSON module (json/)
        addBase64ModuleFunctions(result) // Base64 module (base64/)
        addTestModuleFunctions(result) // Test module (test/)
        addReplModuleFunctions(result) // REPL module (repl/)
    }

    private fun addCoreModuleFunctions(result: CompletionResultSet) {
        // Core module is usually aliased or imported directly
        addModuleFunction(result, "core/+", "(core/+ & args)", "Addition")
        addModuleFunction(result, "core/-", "(core/- & args)", "Subtraction")
        addModuleFunction(result, "core/*", "(core/* & args)", "Multiplication")
        addModuleFunction(result, "core//", "(core// & args)", "Division")
        addModuleFunction(result, "core/map", "(core/map f coll)", "Map function over collection")
        addModuleFunction(result, "core/filter", "(core/filter pred coll)", "Filter collection")
        addModuleFunction(result, "core/reduce", "(core/reduce f coll)", "Reduce collection")
        addModuleFunction(result, "core/count", "(core/count coll)", "Count collection items")
        addModuleFunction(result, "core/get", "(core/get coll key)", "Get value by key")
        addModuleFunction(result, "core/put", "(core/put coll key val)", "Put value at key")
        addModuleFunction(result, "core/str", "(core/str & args)", "String concatenation")
        addModuleFunction(result, "core/print", "(core/print & args)", "Print to output")
        addModuleFunction(result, "core/println", "(core/println & args)", "Print line to output")
    }

    private fun addStringModuleFunctions(result: CompletionResultSet) {
        addModuleFunction(result, "str/join", "(str/join separator coll)", "Join collection with separator")
        addModuleFunction(result, "str/split", "(str/split string pattern)", "Split string by pattern")
        addModuleFunction(result, "str/trim", "(str/trim string)", "Trim whitespace")
        addModuleFunction(result, "str/trim-left", "(str/trim-left string)", "Trim left whitespace")
        addModuleFunction(result, "str/trim-right", "(str/trim-right string)", "Trim right whitespace")
        addModuleFunction(result, "str/upper-case", "(str/upper-case string)", "Convert to uppercase")
        addModuleFunction(result, "str/lower-case", "(str/lower-case string)", "Convert to lowercase")
        addModuleFunction(result, "str/capitalize", "(str/capitalize string)", "Capitalize first letter")
        addModuleFunction(result, "str/reverse", "(str/reverse string)", "Reverse string")
        addModuleFunction(result, "str/replace", "(str/replace string pattern replacement)", "Replace pattern")
        addModuleFunction(result, "str/replace-first", "(str/replace-first string pattern replacement)", "Replace first occurrence")
        addModuleFunction(result, "str/starts-with?", "(str/starts-with? string prefix)", "Check if starts with prefix")
        addModuleFunction(result, "str/ends-with?", "(str/ends-with? string suffix)", "Check if ends with suffix")
        addModuleFunction(result, "str/contains?", "(str/contains? string substring)", "Check if contains substring")
        addModuleFunction(result, "str/index-of", "(str/index-of string substring)", "Find index of substring")
        addModuleFunction(result, "str/last-index-of", "(str/last-index-of string substring)", "Find last index")
        addModuleFunction(result, "str/substring", "(str/substring string start end?)", "Get substring")
        addModuleFunction(result, "str/blank?", "(str/blank? string)", "Check if string is blank")
        addModuleFunction(result, "str/pad-left", "(str/pad-left string length pad)", "Pad string on left")
        addModuleFunction(result, "str/pad-right", "(str/pad-right string length pad)", "Pad string on right")
        addModuleFunction(result, "str/repeat", "(str/repeat string n)", "Repeat string n times")
    }

    private fun addHtmlModuleFunctions(result: CompletionResultSet) {
        addModuleFunction(result, "html/html", "(html/html & content)", "Generate HTML")
        addModuleFunction(result, "html/escape-html", "(html/escape-html string)", "Escape HTML entities")
        addModuleFunction(result, "html/raw-string", "(html/raw-string string)", "Create raw HTML string")
        addModuleFunction(result, "html/raw-string?", "(html/raw-string? x)", "Check if raw HTML string")
        addModuleFunction(result, "html/doctype", "(html/doctype type?)", "Generate DOCTYPE declaration")
        addModuleFunction(result, "html/head", "(html/head & content)", "Generate HTML head")
        addModuleFunction(result, "html/body", "(html/body & content)", "Generate HTML body")
        addModuleFunction(result, "html/div", "(html/div & content)", "Generate div element")
        addModuleFunction(result, "html/span", "(html/span & content)", "Generate span element")
        addModuleFunction(result, "html/p", "(html/p & content)", "Generate paragraph")
        addModuleFunction(result, "html/h1", "(html/h1 & content)", "Generate h1 heading")
        addModuleFunction(result, "html/h2", "(html/h2 & content)", "Generate h2 heading")
        addModuleFunction(result, "html/h3", "(html/h3 & content)", "Generate h3 heading")
        addModuleFunction(result, "html/a", "(html/a attrs & content)", "Generate link")
        addModuleFunction(result, "html/img", "(html/img attrs)", "Generate image")
        addModuleFunction(result, "html/form", "(html/form attrs & content)", "Generate form")
        addModuleFunction(result, "html/input", "(html/input attrs)", "Generate input")
        addModuleFunction(result, "html/textarea", "(html/textarea attrs content)", "Generate textarea")
        addModuleFunction(result, "html/button", "(html/button attrs & content)", "Generate button")
    }

    private fun addHttpModuleFunctions(result: CompletionResultSet) {
        addModuleFunction(result, "http/request", "(http/request method url options?)", "Make HTTP request")
        addModuleFunction(result, "http/get", "(http/get url options?)", "Make GET request")
        addModuleFunction(result, "http/post", "(http/post url body options?)", "Make POST request")
        addModuleFunction(result, "http/put", "(http/put url body options?)", "Make PUT request")
        addModuleFunction(result, "http/delete", "(http/delete url options?)", "Make DELETE request")
        addModuleFunction(result, "http/response", "(http/response status body headers?)", "Create HTTP response")
        addModuleFunction(result, "http/response-from-map", "(http/response-from-map map)", "Response from map")
        addModuleFunction(result, "http/response-from-string", "(http/response-from-string string)", "Response from string")
        addModuleFunction(result, "http/request-from-globals", "(http/request-from-globals)", "Request from PHP globals")
        addModuleFunction(result, "http/emit-response", "(http/emit-response response)", "Emit HTTP response")
        addModuleFunction(result, "http/uri", "(http/uri & parts)", "Create URI")
        addModuleFunction(result, "http/uri-from-string", "(http/uri-from-string string)", "Parse URI from string")
        addModuleFunction(result, "http/uri-from-globals", "(http/uri-from-globals)", "URI from PHP globals")
        addModuleFunction(result, "http/uploaded-file", "(http/uploaded-file & args)", "Create uploaded file")
        addModuleFunction(result, "http/headers-from-server", "(http/headers-from-server server)", "Extract headers")
        addModuleFunction(result, "http/files-from-globals", "(http/files-from-globals files)", "Process uploaded files")
    }

    private fun addJsonModuleFunctions(result: CompletionResultSet) {
        addModuleFunction(result, "json/encode", "(json/encode value options?)", "Encode to JSON")
        addModuleFunction(result, "json/decode", "(json/decode json options?)", "Decode from JSON")
        addModuleFunction(result, "json/encode-value", "(json/encode-value value)", "Encode single value")
        addModuleFunction(result, "json/decode-value", "(json/decode-value json)", "Decode single value")
        addModuleFunction(result, "json/valid-key?", "(json/valid-key? key)", "Check if valid JSON key")
    }

    private fun addBase64ModuleFunctions(result: CompletionResultSet) {
        addModuleFunction(result, "base64/encode", "(base64/encode string)", "Base64 encode")
        addModuleFunction(result, "base64/decode", "(base64/decode string)", "Base64 decode")
        addModuleFunction(result, "base64/encode-url", "(base64/encode-url string)", "URL-safe base64 encode")
        addModuleFunction(result, "base64/decode-url", "(base64/decode-url string)", "URL-safe base64 decode")
    }

    private fun addTestModuleFunctions(result: CompletionResultSet) {
        addModuleFunction(result, "test/deftest", "(test/deftest name & body)", "Define test")
        addModuleFunction(result, "test/is", "(test/is form message?)", "Test assertion")
        addModuleFunction(result, "test/are", "(test/are template & forms)", "Multiple assertions")
        addModuleFunction(result, "test/testing", "(test/testing string & body)", "Test group")
        addModuleFunction(result, "test/run-tests", "(test/run-tests & namespaces)", "Run tests")
        addModuleFunction(result, "test/run-all-tests", "(test/run-all-tests)", "Run all tests")
        addModuleFunction(result, "test/successful?", "(test/successful? results)", "Check if tests successful")
        addModuleFunction(result, "test/thrown?", "(test/thrown? exception-class & body)", "Test exception thrown")
        addModuleFunction(result, "test/thrown-with-msg?", "(test/thrown-with-msg? exception-class pattern & body)", "Test exception with message")
    }

    private fun addReplModuleFunctions(result: CompletionResultSet) {
        addModuleFunction(result, "repl/doc", "(repl/doc name)", "Show documentation")
        addModuleFunction(result, "repl/require", "(repl/require namespace & options)", "Require namespace")
        addModuleFunction(result, "repl/use", "(repl/use class & options)", "Use PHP class")
        addModuleFunction(result, "repl/resolve", "(repl/resolve symbol)", "Resolve symbol")
        addModuleFunction(result, "repl/loaded-namespaces", "(repl/loaded-namespaces)", "List loaded namespaces")
        addModuleFunction(result, "repl/build-facade", "(repl/build-facade)", "Create build facade")
        addModuleFunction(result, "repl/compile-str", "(repl/compile-str string)", "Compile string")
        addModuleFunction(result, "repl/print-colorful", "(repl/print-colorful & args)", "Colorful print")
        addModuleFunction(result, "repl/println-colorful", "(repl/println-colorful & args)", "Colorful println")
    }

    private fun addModuleFunction(
        result: CompletionResultSet, name: String, signature: String?, description: String?
    ) {
        result.addElement(
            createNamespacedLookupElement(
                name, MODULE_FUNCTION_ICON, signature, " - $description"
            )
        )
    }
}
