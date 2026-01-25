package org.phellang.tools.model

import org.phellang.completion.infrastructure.PhelCompletionPriority

object PriorityRules {

    private val arithmeticOperators = setOf(
        "%",
        "*",
        "**",
        "+",
        "-",
        "/",
        "<",
        "<=",
        "<=>",
        "=",
        ">",
        ">=",
        ">=<",
        "not=",
        "bit-and",
        "bit-or",
        "bit-xor",
        "bit-not",
        "bit-shift-left",
        "bit-shift-right",
        "bit-set",
        "bit-clear",
        "bit-flip",
        "bit-test",
        "inc",
        "dec"
    )

    private val specialForms = setOf(
        "def",
        "def-",
        "defn",
        "defn-",
        "defmacro",
        "defmacro-",
        "defstruct",
        "defstruct*",
        "definterface",
        "definterface*",
        "defexception",
        "defexception*",
        "fn",
        "let",
        "do",
        "quote",
        "unquote",
        "unquote-splicing",
        "loop",
        "recur",
        "throw",
        "try",
        "catch",
        "finally",
        "ns",
        "require",
        "use",
        "php/new",
        "php/->",
        "php/::",
        "php/aget",
        "php/aset",
        "php/apush",
        "php/aunset",
        "php/aget-in",
        "php/aset-in",
        "php/apush-in",
        "php/aunset-in",
        "php/oset",
        "set!",
        "binding",
        "declare"
    )

    private val controlFlow = setOf(
        "if",
        "if-not",
        "if-let",
        "when",
        "when-not",
        "when-let",
        "cond",
        "case",
        "and",
        "or",
        "not",
        "foreach",
        "for",
        "dofor",
        "doseq"
    )

    private val collectionOps = setOf(
        "map",
        "filter",
        "reduce",
        "take",
        "drop",
        "first",
        "rest",
        "next",
        "last",
        "butlast",
        "cons",
        "conj",
        "into",
        "concat",
        "flatten",
        "reverse",
        "sort",
        "sort-by",
        "group-by",
        "partition",
        "partition-by",
        "partition-all",
        "frequencies",
        "distinct",
        "dedupe",
        "interleave",
        "interpose",
        "zipmap",
        "zipcoll",
        "merge",
        "merge-with",
        "deep-merge",
        "assoc",
        "assoc-in",
        "dissoc",
        "dissoc-in",
        "update",
        "update-in",
        "get",
        "get-in",
        "keys",
        "values",
        "pairs",
        "kvs",
        "select-keys",
        "contains?",
        "contains-value?",
        "count",
        "empty?",
        "not-empty",
        "seq",
        "vec",
        "set",
        "hash-map",
        "list",
        "vector",
        "range",
        "repeat",
        "repeatedly",
        "iterate",
        "cycle",
        "take-while",
        "drop-while",
        "take-last",
        "drop-last",
        "take-nth",
        "split-at",
        "split-with",
        "find",
        "find-index",
        "keep",
        "keep-indexed",
        "remove",
        "slice",
        "peek",
        "pop",
        "push",
        "put",
        "put-in",
        "unset",
        "unset-in",
        "mapcat",
        "doall",
        "dorun",
        "realized?",
        "lazy-seq",
        "lazy-cat",
        "union",
        "intersection",
        "difference",
        "symmetric-difference",
        "tree-seq",
        "file-seq",
        "line-seq",
        "csv-seq",
        "read-file-lazy"
    )

    private val coreFunctions = setOf(
        "str",
        "print",
        "println",
        "printf",
        "print-str",
        "format",
        "apply",
        "identity",
        "constantly",
        "comp",
        "complement",
        "partial",
        "juxt",
        "memoize",
        "memoize-lru",
        "compare",
        "type",
        "symbol",
        "keyword",
        "name",
        "namespace",
        "full-name",
        "gensym",
        "eval",
        "compile",
        "read-string",
        "macroexpand",
        "macroexpand-1",
        "slurp",
        "spit",
        "var",
        "deref",
        "swap!",
        "transient",
        "persistent",
        "comment",
        "time",
        "with-output-buffer"
    )

    private val namespacePriority = mapOf(
        "base64" to PhelCompletionPriority.BASE64_FUNCTIONS,
        "core" to PhelCompletionPriority.CORE_FUNCTIONS,
        "debug" to PhelCompletionPriority.DEBUG_FUNCTIONS,
        "html" to PhelCompletionPriority.HTML_FUNCTIONS,
        "http" to PhelCompletionPriority.HTTP_FUNCTIONS,
        "json" to PhelCompletionPriority.JSON_FUNCTIONS,
        "mock" to PhelCompletionPriority.MOCK_FUNCTIONS,
        "php" to PhelCompletionPriority.PHP_INTEROP,
        "repl" to PhelCompletionPriority.REPL_FUNCTIONS,
        "str" to PhelCompletionPriority.STRING_FUNCTIONS,
        "test" to PhelCompletionPriority.TEST_FUNCTIONS
    )

    /**
     * Determines the appropriate completion priority for a function.
     *
     * Priority is determined in this order:
     * 1. Deprecated functions get lowest priority
     * 2. Macros get MACROS priority
     * 3. Pattern-based detection (arithmetic, special forms, control flow, etc.)
     * 4. Namespace-based fallback
     */
    fun determinePriority(apiFunction: ApiFunction): PhelCompletionPriority {
        val shortName = extractShortName(apiFunction.name)
        val namespace = apiFunction.namespace

        // Check for deprecated functions first
        if (apiFunction.meta.deprecated != null) {
            return PhelCompletionPriority.DEPRECATED_FUNCTIONS
        }

        // Check for macros
        if (apiFunction.meta.macro == true) {
            return PhelCompletionPriority.MACROS
        }

        // Check for predicates (functions ending with ?)
        if (shortName.endsWith("?")) {
            return PhelCompletionPriority.PREDICATE_FUNCTIONS
        }

        // Check for arithmetic operators
        if (shortName in arithmeticOperators) {
            return PhelCompletionPriority.ARITHMETIC_FUNCTIONS
        }

        // Check for special forms
        if (shortName in specialForms || apiFunction.name in specialForms) {
            return PhelCompletionPriority.SPECIAL_FORMS
        }

        // Check for control flow
        if (shortName in controlFlow) {
            return PhelCompletionPriority.CONTROL_FLOW
        }

        // Check for collection operations (core namespace only)
        if (namespace == "core" && shortName in collectionOps) {
            return PhelCompletionPriority.COLLECTION_FUNCTIONS
        }

        // Check for core essential functions
        if (namespace == "core" && shortName in coreFunctions) {
            return PhelCompletionPriority.CORE_FUNCTIONS
        }

        // String functions get special priority
        if (namespace == "str") {
            return PhelCompletionPriority.STRING_FUNCTIONS
        }

        // Fall back to namespace-based priority
        return namespacePriority[namespace] ?: PhelCompletionPriority.CORE_FUNCTIONS
    }

    /**
     * Extracts the short name from a fully qualified function name.
     * e.g., "core/map" -> "map", "str/join" -> "join"
     */
    private fun extractShortName(fullName: String): String {
        return if (fullName.contains("/")) {
            fullName.substringAfter("/")
        } else {
            fullName
        }
    }
}
