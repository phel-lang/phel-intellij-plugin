package org.phellang.completion

import com.intellij.lang.documentation.AbstractDocumentationProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import org.phellang.language.psi.*

/**
 * Documentation provider for Phel functions and symbols
 * Provides rich documentation for API functions, special forms, and macros
 */
class PhelDocumentationProvider : AbstractDocumentationProvider() {
    override fun generateDoc(element: PsiElement?, originalElement: PsiElement?): String? {
        // Use originalElement (what user hovered over) for categorization when available
        // This prevents issues where reference resolution changes the context
        val elementToClassify = originalElement as? PhelSymbol ?: if (originalElement != null) {
            // originalElement is often a LeafPsiElement, find its PhelSymbol parent
            val parentSymbol = PsiTreeUtil.getParentOfType(originalElement, PhelSymbol::class.java)
            // Fallback: couldn't find proper original element
            parentSymbol ?: element
        } else {
            element
        }

        if (elementToClassify is PhelSymbol) {
            val symbolName = elementToClassify.text
            if (symbolName != null && !symbolName.isEmpty()) {
                val doc: String? = FUNCTION_DOCS[symbolName]
                if (doc != null) {
                    return wrapInHtml(doc)
                }

                // Generate basic documentation for unknown symbols  
                return generateBasicDocForElement(elementToClassify, symbolName)
            }
        }

        return super.generateDoc(element, originalElement)
    }

    override fun getQuickNavigateInfo(element: PsiElement?, originalElement: PsiElement?): String? {
        if (element is PhelSymbol) {
            val symbolName = element.text
            if (symbolName != null && !symbolName.isEmpty()) {
                val signature = getSignature(symbolName)
                if (signature != null) {
                    return "$symbolName $signature"
                }
            }
        }

        return super.getQuickNavigateInfo(element, originalElement)
    }

    private fun generateBasicDocForElement(element: PsiElement?, symbolName: String): String {
        val category = categorizeSymbol(element, symbolName)

        return wrapInHtml(
            "<h3>$symbolName</h3><p><b>Type:</b> $category</p>"
        )
    }

    private fun categorizeSymbol(element: PsiElement?, symbolName: String): String {
        // First check if this is a definition and determine its type
        if (element is PhelSymbol) {
            if (PhelPsiUtil.isDefinition(element)) {
                // Check if it's a function parameter or let binding first
                if (isInParameterVector(element)) {
                    return "Function Parameter"
                } else if (isInLetBinding(element)) {
                    return "Let Binding"
                } else if (isInIfLetBinding(element)) {
                    return "If-Let Binding"
                } else if (isInForBinding(element)) {
                    return "For Binding"
                } else if (isInBindingForm(element)) {
                    return "Binding"
                } else if (isInLoopForm(element)) {
                    return "Loop Binding"
                } else if (isInForeachForm(element)) {
                    return "Foreach Binding"
                } else if (isInDoforForm(element)) {
                    return "Dofor Binding"
                } else if (isInCatchBinding(element)) {
                    return "Exception Binding"
                }

                // Otherwise, check the defining keyword for top-level definitions
                val definingKeyword = getDefiningKeyword(element)
                when (definingKeyword) {
                    "def", "def-" -> return "Variable"
                    "defn", "defn-" -> return "Function"
                    "defmacro", "defmacro-" -> return "Macro"
                    "defstruct" -> return "Struct"
                    "definterface" -> return "Interface"
                    "defexception" -> return "Exception"
                    "declare" -> return "Declaration"
                }
            }
        }

        // Fall back to pattern-based classification for usages and unknown symbols
        // For symbol usages, first check if it might be referencing a local binding
        if (element is PhelSymbol) {
            val localBindingType = getLocalBindingType(element, symbolName)
            if (localBindingType != null) {
                return localBindingType
            }
        }

        // Check core functions (but only after checking for local bindings)
        if (isCoreFunction(symbolName)) {
            return "Core Function"
        } else if (symbolName.endsWith("?")) {
            return "Predicate Function"
        } else if (symbolName.endsWith("!")) {
            return "Side-Effect Function"
        } else if (symbolName.startsWith("php/")) {
            return "PHP Interop Function"
        } else if (symbolName.contains("/")) {
            return "Namespaced Function"
        } else if (symbolName.startsWith(":")) {
            return "Keyword"
        } else if (isSpecialForm(symbolName)) {
            return "Special Form"
        } else if (isMacro(symbolName)) {
            return "Macro"
        } else {
            return "Symbol" // Changed from "Function" to more generic "Symbol"
        }
    }

    /**
     * Get the defining keyword (def, defn, defmacro, etc.) for a symbol definition.
     */
    private fun getDefiningKeyword(symbol: PhelSymbol): String? {
        val containingList = PsiTreeUtil.getParentOfType(symbol, PhelList::class.java)
        if (containingList != null) {
            val firstForm = PsiTreeUtil.findChildOfType(containingList, PhelForm::class.java)
            if (firstForm != null) {
                val firstSymbol = PsiTreeUtil.findChildOfType(firstForm, PhelSymbol::class.java)
                if (firstSymbol != null) {
                    return firstSymbol.text
                }
            }
        }
        return null
    }

    private fun isSpecialForm(name: String?): Boolean {
        // Use exact matching for special forms
        val specialForms = arrayOf<String?>(
            "def",
            "defn",
            "defn-",
            "defmacro",
            "defmacro-",
            "let",
            "if",
            "when",
            "cond",
            "case",
            "do",
            "loop",
            "recur",
            "fn",
            "quote",
            "var",
            "ns",
            "try",
            "catch",
            "finally",
            "throw",
            "for",
            "dofor",
            "foreach"
        )
        for (form in specialForms) {
            if (form == name) {
                return true
            }
        }
        return false
    }

    private fun isMacro(name: String?): Boolean {
        // Use exact matching to avoid false positives (e.g., "-" matching in "->")
        val macros = arrayOf<String?>("->", "->>", "as->", "and", "or", "not", "comment", "declare")
        for (macro in macros) {
            if (macro == name) {
                return true
            }
        }
        return false
    }

    private fun isCoreFunction(name: String?): Boolean {
        // Core functions from phel/core.phel - use exact matching
        val coreFunctions = arrayOf<String?>( // Collection operations
            "map",
            "filter",
            "reduce",
            "apply",
            "count",
            "first",
            "rest",
            "cons",
            "conj",
            "get",
            "get-in",
            "assoc",
            "dissoc",
            "update",
            "merge",
            "keys",
            "values",
            "peek",
            "pop",
            "push",
            "remove",
            "second",
            "ffirst",
            "next",
            "nnext",
            "nfirst",  // Sequence operations

            "take",
            "drop",
            "take-while",
            "drop-while",
            "take-nth",
            "take-last",
            "distinct",
            "reverse",
            "sort",
            "sort-by",
            "shuffle",
            "flatten",
            "range",
            "repeat",
            "interleave",
            "interpose",
            "partition",
            "split-at",
            "split-with",  // String operations

            "str",
            "print",
            "println",
            "print-str",
            "format",
            "printf",
            "str-contains?",
            "slurp",
            "spit",  // Function operations

            "identity",
            "comp",
            "partial",
            "constantly",
            "complement",
            "juxt",
            "memoize",  // Math operations

            "inc",
            "dec",
            "sum",
            "mean",
            "extreme",
            "+",
            "-",
            "*",
            "/",
            "mod",
            "rem",
            "quot",
            "=",
            "not=",
            "<",
            "<=",
            ">",
            ">=",
            "min",
            "max",
            "abs",  // Predicates

            "pos?",
            "neg?",
            "zero?",
            "one?",
            "even?",
            "odd?",
            "nil?",
            "some?",
            "all?",
            "true?",
            "false?",
            "truthy?",
            "boolean?",
            "number?",
            "string?",
            "keyword?",
            "symbol?",
            "list?",
            "vector?",
            "map?",
            "set?",
            "seq?",
            "coll?",
            "empty?",
            "not-empty",
            "float?",
            "int?",
            "function?",
            "struct?",
            "hash-map?",
            "php-array?",
            "php-resource?",
            "php-object?",
            "indexed?",
            "associative?",
            "var?",  // Random functions

            "rand",
            "rand-int",
            "rand-nth",  // Meta and symbol operations

            "meta",
            "name",
            "namespace",
            "full-name",
            "symbol",
            "keyword",
            "gensym",  // Evaluation and compilation

            "eval",
            "compile",
            "macroexpand",
            "macroexpand-1",
            "read-string",  // Bit operations

            "bit-and",
            "bit-or",
            "bit-xor",
            "bit-not",
            "bit-shift-left",
            "bit-shift-right",
            "bit-set",
            "bit-clear",
            "bit-flip",
            "bit-test",  // Other core functions

            "not",
            "compare",
            "type",
            "contains?",
            "find",
            "find-index",
            "frequencies",
            "group-by",
            "union",
            "intersection",
            "difference",
            "symmetric-difference",
            "select-keys",
            "invert",
            "merge-with",
            "deep-merge",  // String module functions (phel\str)

            "split",
            "join",
            "replace",
            "replace-first",
            "trim",
            "triml",
            "trimr",
            "capitalize",
            "lower-case",
            "upper-case",
            "starts-with?",
            "ends-with?",
            "blank?",
            "includes?",
            "index-of",
            "last-index-of",
            "split-lines",
            "pad-left",
            "pad-right",
            "trim-newline",
            "escape",
            "re-quote-replacement",  // JSON module functions (phel\json)

            "encode",
            "decode",  // Base64 module functions (phel\base64)

            "encode-url",
            "decode-url",  // HTML module functions (phel\html)

            "escape-html",
            "doctype",  // Test module functions (phel\test)

            "report",
            "print-summary",
            "run-tests",
            "successful?",  // REPL module functions (phel\repl)

            "loaded-namespaces",
            "resolve",
            "print-colorful",
            "println-colorful",
            "compile-str",  // Trace module functions (phel\trace)

            "dotrace",
            "reset-trace-state!",
            "set-trace-id-padding!",  // HTTP module functions (phel\http) - most commonly used

            "request-from-globals",
            "response-from-map",
            "response-from-string",
            "emit-response"
        )
        for (func in coreFunctions) {
            if (func == name) {
                return true
            }
        }
        return false
    }

    private fun getSignature(symbolName: String): String? {
        // Extract signature from documentation
        val doc: String? = FUNCTION_DOCS[symbolName]
        if (doc != null && doc.contains("<b>Signature:</b>")) {
            val start = doc.indexOf("<code>") + 6
            val end = doc.indexOf("</code>")
            if (start in 6..<end) {
                return doc.substring(start, end)
            }
        }
        return null
    }

    private fun wrapInHtml(content: String): String {
        return "<html><body>$content</body></html>"
    }

    /**
     * Check if symbol is in a function parameter vector
     */
    private fun isInParameterVector(symbol: PhelSymbol): Boolean {
        val paramVec = PsiTreeUtil.getParentOfType(symbol, PhelVec::class.java) ?: return false

        val containingList = PsiTreeUtil.getParentOfType(paramVec, PhelList::class.java) ?: return false

        val forms = PsiTreeUtil.getChildrenOfType(containingList, PhelForm::class.java)
        if (forms == null || forms.size < 2) return false

        val firstSymbol = PsiTreeUtil.findChildOfType(forms[0], PhelSymbol::class.java) ?: return false

        val keyword = firstSymbol.text
        if (keyword == "fn") {
            return forms[1] === paramVec
        } else if (keyword == "defn" || keyword == "defmacro") {
            return forms.size >= 3 && forms[2] === paramVec
        }

        return false
    }

    /**
     * Check if symbol is in a let binding vector
     */
    private fun isInLetBinding(symbol: PhelSymbol): Boolean {
        val bindingVec = PsiTreeUtil.getParentOfType(symbol, PhelVec::class.java) ?: return false

        val containingList = PsiTreeUtil.getParentOfType(bindingVec, PhelList::class.java) ?: return false

        val firstForm = PsiTreeUtil.findChildOfType(containingList, PhelForm::class.java) ?: return false

        val firstSymbol = PsiTreeUtil.findChildOfType(firstForm, PhelSymbol::class.java) ?: return false

        return "let" == firstSymbol.text
    }

    /**
     * Check if symbol is in a for binding vector
     */
    private fun isInForBinding(symbol: PhelSymbol): Boolean {
        return isInBindingFormOfType(symbol, "for")
    }

    /**
     * Check if symbol is in a binding form vector
     */
    private fun isInBindingForm(symbol: PhelSymbol): Boolean {
        return isInBindingFormOfType(symbol, "binding")
    }

    /**
     * Check if symbol is in a loop binding vector
     */
    private fun isInLoopForm(symbol: PhelSymbol): Boolean {
        return isInBindingFormOfType(symbol, "loop")
    }

    /**
     * Check if symbol is in a foreach binding vector
     */
    private fun isInForeachForm(symbol: PhelSymbol): Boolean {
        return isInBindingFormOfType(symbol, "foreach")
    }

    /**
     * Check if symbol is in a dofor binding vector
     */
    private fun isInDoforForm(symbol: PhelSymbol): Boolean {
        return isInBindingFormOfType(symbol, "dofor")
    }

    /**
     * Check if symbol is in an if-let binding
     */
    private fun isInIfLetBinding(symbol: PhelSymbol): Boolean {
        return isInBindingFormOfType(symbol, "if-let")
    }

    /**
     * Check if symbol is in a catch exception binding
     */
    private fun isInCatchBinding(symbol: PhelSymbol): Boolean {
        return isInBindingFormOfType(symbol, "catch")
    }

    /**
     * Generic helper to check if symbol is in a binding form of a specific type
     */
    private fun isInBindingFormOfType(symbol: PhelSymbol, formType: String): Boolean {
        val bindingVec = PsiTreeUtil.getParentOfType(symbol, PhelVec::class.java) ?: return false

        val containingList = PsiTreeUtil.getParentOfType(bindingVec, PhelList::class.java) ?: return false

        val firstForm = PsiTreeUtil.findChildOfType(containingList, PhelForm::class.java) ?: return false

        val firstSymbol = PsiTreeUtil.findChildOfType(firstForm, PhelSymbol::class.java) ?: return false

        return formType == firstSymbol.text
    }

    /**
     * Check if a symbol usage is referencing a local binding (parameter, let binding, etc.)
     * Returns the type of local binding, or null if not a local reference
     */
    private fun getLocalBindingType(symbol: PhelSymbol, symbolName: String): String? {
        // Look for containing defn/fn forms
        var current: PsiElement? = symbol
        while (current != null) {
            if (current is PhelList) {
                val list = current
                val forms = PsiTreeUtil.getChildrenOfType(list, PhelForm::class.java)

                if (forms != null && forms.size >= 2) {
                    val firstSymbol = PsiTreeUtil.findChildOfType(forms[0], PhelSymbol::class.java)
                    if (firstSymbol != null) {
                        val keyword = firstSymbol.text

                        // Check defn/defmacro parameters
                        if ((keyword == "defn" || keyword == "defmacro" || keyword == "defn-" || keyword == "defmacro-") && forms.size >= 3) {
                            // Parameter vector is in forms[2]
                            val paramVec = if (forms[2] is PhelVec) {
                                forms[2] as PhelVec
                            } else {
                                PsiTreeUtil.findChildOfType(forms[2], PhelVec::class.java)
                            }

                            if (paramVec != null && hasParameterWithName(paramVec, symbolName)) {
                                return "Function Parameter"
                            }
                        } else if (keyword == "fn") {
                            // Parameter vector is in forms[1]
                            val paramVec = if (forms[1] is PhelVec) {
                                forms[1] as PhelVec
                            } else {
                                PsiTreeUtil.findChildOfType(forms[1], PhelVec::class.java)
                            }

                            if (paramVec != null && hasParameterWithName(paramVec, symbolName)) {
                                return "Function Parameter"
                            }
                        } else if (keyword == "let") {
                            val bindingVec = if (forms[1] is PhelVec) {
                                forms[1] as PhelVec
                            } else {
                                PsiTreeUtil.findChildOfType(forms[1], PhelVec::class.java)
                            }

                            if (bindingVec != null && hasBindingWithName(bindingVec, symbolName)) {
                                return "Let Binding"
                            }
                        } else if (keyword == "for") {
                            val bindingVec = if (forms[1] is PhelVec) {
                                forms[1] as PhelVec
                            } else {
                                PsiTreeUtil.findChildOfType(forms[1], PhelVec::class.java)
                            }

                            if (bindingVec != null && hasBindingWithName(bindingVec, symbolName)) {
                                return "For Binding"
                            }
                        } else if (keyword == "if-let") {
                            val bindingVec = if (forms[1] is PhelVec) {
                                forms[1] as PhelVec
                            } else {
                                PsiTreeUtil.findChildOfType(forms[1], PhelVec::class.java)
                            }

                            if (bindingVec != null && hasBindingWithName(bindingVec, symbolName)) {
                                return "If-Let Binding"
                            }
                        } else if (keyword == "catch" && forms.size >= 3) {
                            // For catch, the binding is typically forms[2]: (catch ExceptionType binding body)
                            val catchBinding = PsiTreeUtil.findChildOfType(forms[2], PhelSymbol::class.java)
                            if (catchBinding != null && symbolName == catchBinding.text) {
                                return "Exception Binding"
                            }
                        }
                    }
                }
            }
            current = current.parent
        }

        return null
    }

    /**
     * Check if parameter vector has a parameter with the given name
     */
    private fun hasParameterWithName(paramVec: PhelVec, symbolName: String): Boolean {
        val forms = PsiTreeUtil.getChildrenOfType(paramVec, PhelForm::class.java) ?: return false

        for (form in forms) {
            val symbol = PsiTreeUtil.findChildOfType(form, PhelSymbol::class.java)
            if (symbol != null && symbolName == symbol.text) {
                return true
            }
        }
        return false
    }

    /**
     * Check if binding vector has a binding with the given name (binding pairs: [name value name value...])
     */
    private fun hasBindingWithName(bindingVec: PhelVec, symbolName: String): Boolean {
        val forms = PsiTreeUtil.getChildrenOfType(bindingVec, PhelForm::class.java) ?: return false

        // Check even indices for binding names
        var i = 0
        while (i < forms.size) {
            val symbol = PsiTreeUtil.findChildOfType(forms[i], PhelSymbol::class.java)
            if (symbol != null && symbolName == symbol.text) {
                return true
            }
            i += 2
        }
        return false
    }

    companion object {
        init {
            // ===== ARITHMETIC OPERATIONS =====
            FUNCTION_DOCS["+"] =
                "<h3>+</h3>" + "<p><b>Signature:</b> <code>(+ & xs)</code></p>" + "<p>Returns the sum of all elements in xs. All elements must be numbers. If xs is empty, return 0.</p>"

            FUNCTION_DOCS["-"] =
                "<h3>-</h3>" + "<p><b>Signature:</b> <code>(- & xs)</code></p>" + "<p>Returns the difference of all elements in xs. If xs is empty, return 0. If xs has one element, return the negative value of that element.</p>"

            FUNCTION_DOCS["*"] =
                "<h3>*</h3>" + "<p><b>Signature:</b> <code>(* & xs)</code></p>" + "<p>Returns the product of all elements in xs. All elements must be numbers. If xs is empty, return 1.</p>"

            FUNCTION_DOCS["/"] =
                "<h3>/</h3>" + "<p><b>Signature:</b> <code>(/ & xs)</code></p>" + "<p>Returns the nominator divided by all the denominators. If xs is empty, returns 1. If xs has one value, returns the reciprocal of x.</p>"

            FUNCTION_DOCS["%"] =
                "<h3>%</h3>" + "<p><b>Signature:</b> <code>(% dividend divisor)</code></p>" + "<p>Return the remainder of dividend/divisor.</p>"

            FUNCTION_DOCS["**"] =
                "<h3>**</h3>" + "<p><b>Signature:</b> <code>(** a x)</code></p>" + "<p>Return a to the power of x.</p>"

            FUNCTION_DOCS["inc"] =
                "<h3>inc</h3>" + "<p><b>Signature:</b> <code>(inc x)</code></p>" + "<p>Increments x by one.</p>"

            FUNCTION_DOCS["dec"] =
                "<h3>dec</h3>" + "<p><b>Signature:</b> <code>(dec x)</code></p>" + "<p>Decrements x by one.</p>"

            // ===== COMPARISON OPERATIONS =====
            FUNCTION_DOCS["="] =
                "<h3>=</h3>" + "<p><b>Signature:</b> <code>(= a & more)</code></p>" + "<p>Checks if all values are equal. Same as a == b in PHP.</p>"

            FUNCTION_DOCS["not="] =
                "<h3>not=</h3>" + "<p><b>Signature:</b> <code>(not= a & more)</code></p>" + "<p>Checks if all values are unequal. Same as a != b in PHP.</p>"

            FUNCTION_DOCS["<"] =
                "<h3><</h3>" + "<p><b>Signature:</b> <code>(< a & more)</code></p>" + "<p>Checks if each argument is strictly less than the following argument. Returns a boolean.</p>"

            FUNCTION_DOCS["<="] =
                "<h3><=</h3>" + "<p><b>Signature:</b> <code>(<= a & more)</code></p>" + "<p>Checks if each argument is less than or equal to the following argument. Returns a boolean.</p>"

            FUNCTION_DOCS[">"] =
                "<h3>></h3>" + "<p><b>Signature:</b> <code>(> a & more)</code></p>" + "<p>Checks if each argument is strictly greater than the following argument. Returns a boolean.</p>"

            FUNCTION_DOCS[">="] =
                "<h3>>=</h3>" + "<p><b>Signature:</b> <code>(>= a & more)</code></p>" + "<p>Checks if each argument is greater than or equal to the following argument. Returns a boolean.</p>"

            FUNCTION_DOCS["max"] =
                "<h3>max</h3>" + "<p><b>Signature:</b> <code>(max & numbers)</code></p>" + "<p>Returns the numeric maximum of all numbers.</p>"

            FUNCTION_DOCS["min"] =
                "<h3>min</h3>" + "<p><b>Signature:</b> <code>(min & numbers)</code></p>" + "<p>Returns the numeric minimum of all numbers.</p>"

            // ===== COLLECTION OPERATIONS =====
            FUNCTION_DOCS["map"] =
                "<h3>map</h3>" + "<p><b>Signature:</b> <code>(map f & xs)</code></p>" + "<p>Returns an array consisting of the result of applying f to all of the first items in each xs, followed by applying f to all the second items in each xs until anyone of the xs is exhausted.</p>"

            FUNCTION_DOCS["filter"] =
                "<h3>filter</h3>" + "<p><b>Signature:</b> <code>(filter pred xs)</code></p>" + "<p>Returns all elements of xs where (pred x) is true.</p>"

            FUNCTION_DOCS["reduce"] =
                "<h3>reduce</h3>" + "<p><b>Signature:</b> <code>(reduce f & xs)</code></p>" + "<p>(reduce f coll) (reduce f val coll) f should be a function of 2 arguments. If val is not supplied, returns the result of applying f to the first 2 items in coll, then applying f to that result and the 3rd item, etc.</p>"

            FUNCTION_DOCS["count"] =
                "<h3>count</h3>" + "<p><b>Signature:</b> <code>(count xs)</code></p>" + "<p>Counts the number of elements in a sequence. Can be used on everything that implements the PHP Countable interface.</p>"

            FUNCTION_DOCS["get"] =
                "<h3>get</h3>" + "<p><b>Signature:</b> <code>(get ds k & [opt])</code></p>" + "<p>Get the value mapped to key from the datastructure ds. Returns opt or nil if the value cannot be found.</p>"

            FUNCTION_DOCS["get-in"] =
                "<h3>get-in</h3>" + "<p><b>Signature:</b> <code>(get-in ds ks & [opt])</code></p>" + "<p>Access a value in a nested data structure. Looks into the data structure via a sequence of keys.</p>"

            FUNCTION_DOCS["put"] =
                "<h3>put</h3>" + "<p><b>Signature:</b> <code>(put ds key value)</code></p>" + "<p>Puts value mapped to key on the datastructure ds. Returns ds.</p>"

            FUNCTION_DOCS["put-in"] =
                "<h3>put-in</h3>" + "<p><b>Signature:</b> <code>(put-in ds [k & ks] v)</code></p>" + "<p>Puts a value into a nested data structure.</p>"

            FUNCTION_DOCS["assoc"] =
                "<h3>assoc</h3>" + "<p><b>Signature:</b> <code>(assoc ds key value)</code></p>" + "<p>Alias for put.</p>"

            FUNCTION_DOCS["dissoc"] =
                "<h3>dissoc</h3>" + "<p><b>Signature:</b> <code>(dissoc ds key)</code></p>" + "<p>Alias for unset.</p>"

            FUNCTION_DOCS["first"] =
                "<h3>first</h3>" + "<p><b>Signature:</b> <code>(first xs)</code></p>" + "<p>Returns the first element of an indexed sequence or nil.</p>"

            FUNCTION_DOCS["rest"] =
                "<h3>rest</h3>" + "<p><b>Signature:</b> <code>(rest xs)</code></p>" + "<p>Returns the sequence of elements after the first element. If there are no elements, returns an empty sequence.</p>"

            FUNCTION_DOCS["last"] =
                "<h3>last</h3>" + "<p><b>Signature:</b> <code>(last xs)</code></p>" + "<p>Returns the last element of xs or nil if xs is empty or nil.</p>"

            FUNCTION_DOCS["cons"] =
                "<h3>cons</h3>" + "<p><b>Signature:</b> <code>(cons x xs)</code></p>" + "<p>Prepends x to the beginning of xs.</p>"

            FUNCTION_DOCS["push"] =
                "<h3>push</h3>" + "<p><b>Signature:</b> <code>(push xs x)</code></p>" + "<p>Inserts x at the end of the sequence xs.</p>"

            FUNCTION_DOCS["keys"] =
                "<h3>keys</h3>" + "<p><b>Signature:</b> <code>(keys xs)</code></p>" + "<p>Gets the keys of an associative data structure.</p>"

            FUNCTION_DOCS["values"] =
                "<h3>values</h3>" + "<p><b>Signature:</b> <code>(values xs)</code></p>" + "<p>Gets the values of an associative data structure.</p>"

            // ===== SEQUENCE OPERATIONS =====
            FUNCTION_DOCS["take"] =
                "<h3>take</h3>" + "<p><b>Signature:</b> <code>(take n xs)</code></p>" + "<p>Takes the first n elements of xs.</p>"

            FUNCTION_DOCS["drop"] =
                "<h3>drop</h3>" + "<p><b>Signature:</b> <code>(drop n xs)</code></p>" + "<p>Drops the first n elements of xs.</p>"

            FUNCTION_DOCS["take-while"] =
                "<h3>take-while</h3>" + "<p><b>Signature:</b> <code>(take-while pred xs)</code></p>" + "<p>Takes all elements at the front of xs where (pred x) is true.</p>"

            FUNCTION_DOCS["drop-while"] =
                "<h3>drop-while</h3>" + "<p><b>Signature:</b> <code>(drop-while pred xs)</code></p>" + "<p>Drops all elements at the front xs where (pred x) is true.</p>"

            FUNCTION_DOCS["reverse"] =
                "<h3>reverse</h3>" + "<p><b>Signature:</b> <code>(reverse xs)</code></p>" + "<p>Reverses the order of the elements in the given sequence.</p>"

            FUNCTION_DOCS["sort"] =
                "<h3>sort</h3>" + "<p><b>Signature:</b> <code>(sort xs & [comp])</code></p>" + "<p>Returns a sorted vector. If no comparator is supplied compare is used.</p>"

            FUNCTION_DOCS["distinct"] =
                "<h3>distinct</h3>" + "<p><b>Signature:</b> <code>(distinct xs)</code></p>" + "<p>Returns a vector with duplicated values removed in xs.</p>"

            FUNCTION_DOCS["range"] =
                "<h3>range</h3>" + "<p><b>Signature:</b> <code>(range a & rest)</code></p>" + "<p>Create an array of values [start, end). If the function has one argument then the range [0, end) is returned. With two arguments, returns [start, end). The third argument is an optional step width (default 1).</p>"

            // ===== STRING OPERATIONS =====
            FUNCTION_DOCS["str"] =
                "<h3>str</h3>" + "<p><b>Signature:</b> <code>(str & args)</code></p>" + "<p>Creates a string by concatenating values together. If no arguments are provided an empty string is returned. Nil and false are represented as an empty string. True is represented as 1.</p>"

            FUNCTION_DOCS["print"] =
                "<h3>print</h3>" + "<p><b>Signature:</b> <code>(print & xs)</code></p>" + "<p>Prints the given values to the default output stream. Returns nil.</p>"

            FUNCTION_DOCS["println"] =
                "<h3>println</h3>" + "<p><b>Signature:</b> <code>(println & xs)</code></p>" + "<p>Same as print followed by a newline.</p>"

            FUNCTION_DOCS["format"] =
                "<h3>format</h3>" + "<p><b>Signature:</b> <code>(format fmt & xs)</code></p>" + "<p>Returns a formatted string. See PHP's sprintf for more information.</p>"

            // ===== LOGIC OPERATIONS =====
            FUNCTION_DOCS["and"] =
                "<h3>and</h3>" + "<p><b>Signature:</b> <code>(and & args)</code></p>" + "<p>Evaluates each expression one at a time, from left to right. If a form returns logical false, and returns that value and doesn't evaluate any of the other expressions, otherwise, it returns the value of the last expression.</p>"

            FUNCTION_DOCS["or"] =
                "<h3>or</h3>" + "<p><b>Signature:</b> <code>(or & args)</code></p>" + "<p>Evaluates each expression one at a time, from left to right. If a form returns a logical true value, or returns that value and doesn't evaluate any of the other expressions, otherwise, it returns the value of the last expression.</p>"

            FUNCTION_DOCS["not"] =
                "<h3>not</h3>" + "<p><b>Signature:</b> <code>(not x)</code></p>" + "<p>The not function returns true if the given value is logical false and false otherwise.</p>"

            // ===== TYPE PREDICATES =====
            FUNCTION_DOCS["nil?"] =
                "<h3>nil?</h3>" + "<p><b>Signature:</b> <code>(nil? x)</code></p>" + "<p>Returns true if x is nil, false otherwise.</p>"

            FUNCTION_DOCS["string?"] =
                "<h3>string?</h3>" + "<p><b>Signature:</b> <code>(string? x)</code></p>" + "<p>Returns true if x is a string, false otherwise.</p>"

            FUNCTION_DOCS["number?"] =
                "<h3>number?</h3>" + "<p><b>Signature:</b> <code>(number? x)</code></p>" + "<p>Returns true if x is a number, false otherwise.</p>"

            FUNCTION_DOCS["boolean?"] =
                "<h3>boolean?</h3>" + "<p><b>Signature:</b> <code>(boolean? x)</code></p>" + "<p>Returns true if x is a boolean, false otherwise.</p>"

            FUNCTION_DOCS["keyword?"] =
                "<h3>keyword?</h3>" + "<p><b>Signature:</b> <code>(keyword? x)</code></p>" + "<p>Returns true if x is a keyword, false otherwise.</p>"

            FUNCTION_DOCS["symbol?"] =
                "<h3>symbol?</h3>" + "<p><b>Signature:</b> <code>(symbol? x)</code></p>" + "<p>Returns true if x is a symbol, false otherwise.</p>"

            FUNCTION_DOCS["list?"] =
                "<h3>list?</h3>" + "<p><b>Signature:</b> <code>(list? x)</code></p>" + "<p>Returns true if x is a list, false otherwise.</p>"

            FUNCTION_DOCS["vector?"] =
                "<h3>vector?</h3>" + "<p><b>Signature:</b> <code>(vector? x)</code></p>" + "<p>Returns true if x is a vector, false otherwise.</p>"

            FUNCTION_DOCS["hash-map?"] =
                "<h3>hash-map?</h3>" + "<p><b>Signature:</b> <code>(hash-map? x)</code></p>" + "<p>Returns true if x is a hash map, false otherwise.</p>"

            FUNCTION_DOCS["empty?"] =
                "<h3>empty?</h3>" + "<p><b>Signature:</b> <code>(empty? x)</code></p>" + "<p>Returns true if x would be 0, \"\" or empty collection, false otherwise.</p>"

            FUNCTION_DOCS["even?"] =
                "<h3>even?</h3>" + "<p><b>Signature:</b> <code>(even? x)</code></p>" + "<p>Checks if x is even.</p>"

            FUNCTION_DOCS["odd?"] =
                "<h3>odd?</h3>" + "<p><b>Signature:</b> <code>(odd? x)</code></p>" + "<p>Checks if x is odd.</p>"

            // ===== CONTROL FLOW =====
            FUNCTION_DOCS["if"] =
                "<h3>if</h3>" + "<p><b>Special Form</b></p>" + "<p><b>Signature:</b> <code>(if test then else?)</code></p>" + "<p>A control flow structure. First evaluates test. If test evaluates to true, only the then form is evaluated and the result is returned. If test evaluates to false only the else form is evaluated and the result is returned.</p>"

            FUNCTION_DOCS["when"] =
                "<h3>when</h3>" + "<p><b>Signature:</b> <code>(when test & body)</code></p>" + "<p>Evaluates test and if that is logical true, evaluates body.</p>"

            FUNCTION_DOCS["cond"] =
                "<h3>cond</h3>" + "<p><b>Signature:</b> <code>(cond & pairs)</code></p>" + "<p>Takes a set of test/expression pairs. Evaluates each test one at a time. If a test returns logically true, the expression is evaluated and returned.</p>"

            FUNCTION_DOCS["case"] =
                "<h3>case</h3>" + "<p><b>Signature:</b> <code>(case e & pairs)</code></p>" + "<p>Takes an expression e and a set of test-content/expression pairs. First evaluates e and then finds the first pair where the test-constant matches the result of e.</p>"

            // ===== SPECIAL FORMS =====
            FUNCTION_DOCS["def"] =
                "<h3>def</h3>" + "<p><b>Special Form</b></p>" + "<p><b>Signature:</b> <code>(def name meta? value)</code></p>" + "<p>This special form binds a value to a global symbol.</p>"

            FUNCTION_DOCS["defn"] =
                "<h3>defn</h3>" + "<p><b>Special Form</b></p>" + "<p><b>Signature:</b> <code>(defn name & fdecl)</code></p>" + "<p>Define a new global function.</p>"

            FUNCTION_DOCS["defmacro"] =
                "<h3>defmacro</h3>" + "<p><b>Special Form</b></p>" + "<p><b>Signature:</b> <code>(defmacro name & fdecl)</code></p>" + "<p>Define a macro.</p>"

            FUNCTION_DOCS["let"] =
                "<h3>let</h3>" + "<p><b>Special Form</b></p>" + "<p><b>Signature:</b> <code>(let [bindings*] expr*)</code></p>" + "<p>Creates a new lexical context with assignments defined in bindings. Afterwards the list of expressions is evaluated and the value of the last expression is returned.</p>"

            FUNCTION_DOCS["fn"] =
                "<h3>fn</h3>" + "<p><b>Special Form</b></p>" + "<p><b>Signature:</b> <code>(fn [params*] expr*)</code></p>" + "<p>Defines a function. A function consists of a list of parameters and a list of expression. The value of the last expression is returned as the result of the function.</p>"

            FUNCTION_DOCS["for"] =
                "<h3>for</h3>" + "<p><b>Special Form</b></p>" + "<p><b>Signature:</b> <code>(for head & body)</code></p>" + "<p>List comprehension. The head of the loop is a vector that contains a sequence of bindings modifiers and options. Supports :range, :in, :keys, :pairs verbs and :while, :let, :when modifiers.</p>"

            FUNCTION_DOCS["loop"] =
                "<h3>loop</h3>" + "<p><b>Special Form</b></p>" + "<p><b>Signature:</b> <code>(loop [bindings*] expr*)</code></p>" + "<p>Creates a new lexical context with variables defined in bindings and defines a recursion point at the top of the loop.</p>"

            FUNCTION_DOCS["binding"] =
                "<h3>binding</h3>" + "<p><b>Special Form</b></p>" + "<p><b>Signature:</b> <code>(binding bindings & body)</code></p>" + "<p>Temporary redefines definitions while executing the body. The value will be reset after the body was executed.</p>"

            FUNCTION_DOCS["dofor"] =
                "<h3>dofor</h3>" + "<p><b>Special Form</b></p>" + "<p><b>Signature:</b> <code>(dofor head & body)</code></p>" + "<p>Repeatedly executes body for side effects with bindings and modifiers as provided by for. Returns nil.</p>"

            FUNCTION_DOCS["foreach"] =
                "<h3>foreach</h3>" + "<p><b>Special Form</b></p>" + "<p><b>Signature:</b> <code>(foreach [value valueExpr] expr*)</code> or <code>(foreach [key value valueExpr] expr*)</code></p>" + "<p>The foreach special form can be used to iterate over all kind of PHP datastructures. The return value of foreach is always nil.</p>"

            // ===== THREADING MACROS =====
            FUNCTION_DOCS["->"] =
                "<h3>-></h3>" + "<p><b>Threading Macro</b></p>" + "<p><b>Signature:</b> <code>(-> x & forms)</code></p>" + "<p>Threads the expr through the forms. Inserts x as the second item in the first form, making a list of it if it is not a list already.</p>"

            FUNCTION_DOCS["->>"] =
                "<h3>->></h3>" + "<p><b>Threading Macro</b></p>" + "<p><b>Signature:</b> <code>(->> x & forms)</code></p>" + "<p>Threads the expr through the forms. Inserts x as the last item in the first form, making a list of it if it is not a list already.</p>"

            FUNCTION_DOCS["as->"] =
                "<h3>as-></h3>" + "<p><b>Threading Macro</b></p>" + "<p><b>Signature:</b> <code>(as-> expr name & forms)</code></p>" + "<p>Binds name to expr, evaluates the first form in the lexical context of that binding, then binds name to that result, repeating for each successive form.</p>"

            // ===== PHP INTEROP =====
            FUNCTION_DOCS["php/new"] =
                "<h3>php/new</h3>" + "<p><b>PHP Interop</b></p>" + "<p><b>Signature:</b> <code>(php/new expr args*)</code></p>" + "<p>Evaluates expr and creates a new PHP class using the arguments. The instance of the class is returned.</p>"

            FUNCTION_DOCS["php/->"] =
                "<h3>php/-></h3>" + "<p><b>PHP Interop</b></p>" + "<p><b>Signature:</b> <code>(php/-> object call*)</code></p>" + "<p>Access to an object property or result of chained calls.</p>"

            FUNCTION_DOCS["php/::"] =
                "<h3>php/::</h3>" + "<p><b>PHP Interop</b></p>" + "<p><b>Signature:</b> <code>(php/:: class call*)</code></p>" + "<p>Calls a static method or property from a PHP class. Both methodname and property must be symbols and cannot be an evaluated value.</p>"

            // ===== ADDITIONAL CORE FUNCTIONS =====
            FUNCTION_DOCS["apply"] =
                "<h3>apply</h3>" + "<p><b>Signature:</b> <code>(apply f & args)</code></p>" + "<p>Calls the function with the given arguments. The last argument must be a list of values, which are passed as separate arguments, rather than a single list.</p>"

            FUNCTION_DOCS["partial"] =
                "<h3>partial</h3>" + "<p><b>Signature:</b> <code>(partial f & args)</code></p>" + "<p>Takes a function f and fewer than normal arguments of f and returns a function that takes a variable number of additional arguments.</p>"

            FUNCTION_DOCS["comp"] =
                "<h3>comp</h3>" + "<p><b>Signature:</b> <code>(comp & fs)</code></p>" + "<p>Takes a list of functions and returns a function that is the composition of those functions.</p>"

            FUNCTION_DOCS["identity"] =
                "<h3>identity</h3>" + "<p><b>Signature:</b> <code>(identity x)</code></p>" + "<p>Returns its argument.</p>"

            FUNCTION_DOCS["merge"] =
                "<h3>merge</h3>" + "<p><b>Signature:</b> <code>(merge & maps)</code></p>" + "<p>Merges multiple maps into one new map. If a key appears in more than one collection, then later values replace any previous ones.</p>"

            FUNCTION_DOCS["contains?"] =
                "<h3>contains?</h3>" + "<p><b>Signature:</b> <code>(contains? coll key)</code></p>" + "<p>Returns true if key is present in the given collection, otherwise returns false.</p>"

            FUNCTION_DOCS["find"] =
                "<h3>find</h3>" + "<p><b>Signature:</b> <code>(find coll key)</code></p>" + "<p>Returns the key-value pair for key in coll.</p>"

            // ===== JSON FUNCTIONS =====
            FUNCTION_DOCS["json/encode"] =
                "<h3>json/encode</h3>" + "<p><b>JSON Module</b></p>" + "<p><b>Signature:</b> <code>(json/encode value & options)</code></p>" + "<p>Returns the JSON representation of a value.</p>"

            FUNCTION_DOCS["json/decode"] =
                "<h3>json/decode</h3>" + "<p><b>JSON Module</b></p>" + "<p><b>Signature:</b> <code>(json/decode json & options)</code></p>" + "<p>Decodes a JSON string.</p>"

            // ===== BASE64 FUNCTIONS =====
            FUNCTION_DOCS["base64/encode"] =
                "<h3>base64/encode</h3>" + "<p><b>Base64 Module</b></p>" + "<p><b>Signature:</b> <code>(base64/encode s)</code></p>" + "<p>Returns the Base64 representation of s.</p>"

            FUNCTION_DOCS["base64/decode"] =
                "<h3>base64/decode</h3>" + "<p><b>Base64 Module</b></p>" + "<p><b>Signature:</b> <code>(base64/decode s & [strict?])</code></p>" + "<p>Decodes the Base64 encoded string s. If strict? is true invalid characters trigger an error.</p>"

            // ===== STRING MODULE FUNCTIONS =====
            FUNCTION_DOCS["str/join"] =
                "<h3>str/join</h3>" + "<p><b>String Module</b></p>" + "<p><b>Signature:</b> <code>(str/join separator & [coll])</code></p>" + "<p>Returns a string of all elements in coll, as returned by (seq coll), separated by an optional separator.</p>"

            FUNCTION_DOCS["str/split"] =
                "<h3>str/split</h3>" + "<p><b>String Module</b></p>" + "<p><b>Signature:</b> <code>(str/split s re & [limit])</code></p>" + "<p>Splits string on a regular expression. Optional argument limit is the maximum number of parts.</p>"

            FUNCTION_DOCS["str/trim"] =
                "<h3>str/trim</h3>" + "<p><b>String Module</b></p>" + "<p><b>Signature:</b> <code>(str/trim s)</code></p>" + "<p>Removes whitespace from both ends of string.</p>"

            FUNCTION_DOCS["str/upper-case"] =
                "<h3>str/upper-case</h3>" + "<p><b>String Module</b></p>" + "<p><b>Signature:</b> <code>(str/upper-case s)</code></p>" + "<p>Converts string to all upper-case. Handles multibyte characters.</p>"

            FUNCTION_DOCS["str/lower-case"] =
                "<h3>str/lower-case</h3>" + "<p><b>String Module</b></p>" + "<p><b>Signature:</b> <code>(str/lower-case s)</code></p>" + "<p>Converts string to all lower-case. Handles multibyte characters.</p>"

            // ===== HTML FUNCTIONS =====
            FUNCTION_DOCS["html/escape-html"] =
                "<h3>html/escape-html</h3>" + "<p><b>HTML Module</b></p>" + "<p><b>Signature:</b> <code>(html/escape-html s)</code></p>" + "<p>Escapes the string that may contain HTML.</p>"

            FUNCTION_DOCS["html/html"] =
                "<h3>html/html</h3>" + "<p><b>HTML Module</b></p>" + "<p><b>Signature:</b> <code>(html/html & content)</code></p>" + "<p>Generates HTML content from Phel data structures.</p>"

            // ===== HTTP FUNCTIONS =====
            FUNCTION_DOCS["http/request-from-globals"] =
                "<h3>http/request-from-globals</h3><p><b>HTTP Module</b></p><p><b>Signature:</b> <code>(http/request-from-globals)</code></p><p>Extracts a request from \$_SERVER, \$_GET, \$_POST, \$_COOKIE and \$_FILES.</p>"

            FUNCTION_DOCS["http/response-from-map"] =
                "<h3>http/response-from-map</h3>" + "<p><b>HTTP Module</b></p>" + "<p><b>Signature:</b> <code>(http/response-from-map response-map)</code></p>" + "<p>Creates a response struct from a map. The map can have :status, :headers, :body, :version, :reason keys.</p>"

            FUNCTION_DOCS["keep"] =
                "<h3>keep</h3>" + "<p><b>Signature:</b> <code>(keep pred xs)</code></p>" + "<p>Returns a collection of non-nil results of (pred x) applied to items in xs.</p>"

            FUNCTION_DOCS["keep-indexed"] =
                "<h3>keep-indexed</h3>" + "<p><b>Signature:</b> <code>(keep-indexed pred xs)</code></p>" + "<p>Like keep but pred takes index and item as arguments.</p>"

            FUNCTION_DOCS["butlast"] =
                "<h3>butlast</h3>" + "<p><b>Signature:</b> <code>(butlast xs)</code></p>" + "<p>Returns all but the last element of xs.</p>"

            FUNCTION_DOCS["dedupe"] =
                "<h3>dedupe</h3>" + "<p><b>Signature:</b> <code>(dedupe xs)</code></p>" + "<p>Returns collection with consecutive duplicates removed.</p>"

            FUNCTION_DOCS["every?"] =
                "<h3>every?</h3>" + "<p><b>Signature:</b> <code>(every? pred xs)</code></p>" + "<p>Returns true if pred is true for every element.</p>"

            FUNCTION_DOCS["all?"] =
                "<h3>all?</h3>" + "<p><b>Signature:</b> <code>(all? pred xs)</code></p>" + "<p>Returns true if pred is true for every element.</p>"

            FUNCTION_DOCS["mapcat"] =
                "<h3>mapcat</h3>" + "<p><b>Signature:</b> <code>(mapcat f & xs)</code></p>" + "<p>Maps f over collections then concatenates results.</p>"

            FUNCTION_DOCS["bit-and"] =
                "<h3>bit-and</h3>" + "<p><b>Signature:</b> <code>(bit-and x y & args)</code></p>" + "<p>Bitwise AND operation. Can take multiple arguments.</p>"

            FUNCTION_DOCS["bit-or"] =
                "<h3>bit-or</h3>" + "<p><b>Signature:</b> <code>(bit-or x y & args)</code></p>" + "<p>Bitwise OR operation. Can take multiple arguments.</p>"

            FUNCTION_DOCS["bit-xor"] =
                "<h3>bit-xor</h3>" + "<p><b>Signature:</b> <code>(bit-xor x y & args)</code></p>" + "<p>Bitwise XOR operation. Can take multiple arguments.</p>"

            FUNCTION_DOCS["bit-not"] =
                "<h3>bit-not</h3>" + "<p><b>Signature:</b> <code>(bit-not x)</code></p>" + "<p>Bitwise NOT operation.</p>"

            FUNCTION_DOCS["bit-shift-left"] =
                "<h3>bit-shift-left</h3>" + "<p><b>Signature:</b> <code>(bit-shift-left x n)</code></p>" + "<p>Bitwise left shift operation.</p>"

            FUNCTION_DOCS["bit-shift-right"] =
                "<h3>bit-shift-right</h3>" + "<p><b>Signature:</b> <code>(bit-shift-right x n)</code></p>" + "<p>Bitwise right shift operation.</p>"
        }
    }
}

// Documentation for core API functions
private val FUNCTION_DOCS: MutableMap<String?, String?> = HashMap()