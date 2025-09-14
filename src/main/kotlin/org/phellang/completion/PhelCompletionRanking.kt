package org.phellang.completion

import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.completion.PrioritizedLookupElement
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.openapi.diagnostic.Logger
import com.intellij.psi.PsiElement
import org.phellang.language.psi.PhelList
import org.phellang.language.psi.PhelSymbol
import org.phellang.language.psi.impl.PhelAccessImpl
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import javax.swing.Icon

/**
 * Smart completion ranking system for Phel language
 * Provides context-aware prioritization of completion suggestions
 */
object PhelCompletionRanking {
    private val LOG = Logger.getInstance(PhelCompletionRanking::class.java)

    // Cache for symbol usage frequency (simple implementation)
    private val usageFrequency: MutableMap<String, Int> = ConcurrentHashMap<String, Int>()

    // Cache for context-relevant symbols
    private val contextRelevantSymbols: MutableMap<String, MutableSet<String>> = ConcurrentHashMap<String, MutableSet<String>>()

    init {
        initializeContextualSymbols()
    }

    /**
     * Create a ranked lookup element with priority and additional metadata
     */
    @JvmStatic
    fun createRankedElement(
        name: String,
        signature: String?,
        description: String?,
        icon: Icon?
    ): LookupElementBuilder {
        var builder = LookupElementBuilder.create(name)

        if (description != null) {
            builder = builder.withTypeText(description)
        }

        if (signature != null) {
            builder = builder.withTailText(" $signature", true)
        }

        if (icon != null) {
            builder = builder.withIcon(icon)
        }

        // Add usage-based bonus by modifying the presentation
        val usageCount = usageFrequency.getOrDefault(name, 0)
        if (usageCount > 0) {
            // Add usage indicator to help with visual prioritization
            builder = builder.withTailText(
                if (description != null) " - " + description + " (used " + usageCount + "x)" else " (used " + usageCount + "x)",
                true
            )
        }

        // Note: Priority will be applied when adding to result set
        return builder
    }

    /**
     * Add a ranked completion to the result set
     */
    @JvmStatic
    fun addRankedCompletion(
        result: CompletionResultSet,
        name: String,
        priority: Priority,
        signature: String?,
        description: String?,
        icon: Icon?
    ) {
        val element: LookupElement = createRankedElement(name, signature, description, icon)
        // Apply priority using IntelliJ's PrioritizedLookupElement
        result.addElement(PrioritizedLookupElement.withPriority(element, priority.value))
    }

    /**
     * Determine the appropriate priority for a symbol based on context
     */
    @JvmStatic
    fun getContextualPriority(
        symbolName: String,
        context: PsiElement,
        symbolType: String
    ): Priority {
        // Check for local scope symbols first (highest priority)

        if ("Parameter" == symbolType || "Local Variable" == symbolType) {
            return Priority.CURRENT_SCOPE_LOCALS
        }

        if ("Function" == symbolType && isInSameFile()) {
            return Priority.RECENT_DEFINITIONS
        }

        // Check for recursive function calls
        if (isRecursiveCall()) {
            return Priority.CURRENT_FUNCTION_RECURSIVE
        }

        // Context-aware prioritization based on expected argument types
        val functionContext = getCurrentFunctionContext(context)
        if (functionContext != null) {
            val contextPriority = getContextBasedPriority(symbolName, functionContext)
            if (contextPriority != null) {
                return contextPriority
            }
        }

        // Built-in function prioritization
        if (isCommonBuiltin(symbolName)) {
            return Priority.COMMON_BUILTINS
        }

        if (isSpecialForm(symbolName)) {
            return Priority.SPECIAL_FORMS
        }

        if (isControlFlow(symbolName)) {
            return Priority.CONTROL_FLOW
        }

        // API function categorization
        if (symbolName.endsWith("?")) {
            return Priority.API_FUNCTIONS // Predicate functions
        }

        if (isCollectionFunction(symbolName)) {
            return Priority.COLLECTION_FUNCTIONS
        }

        if (isStringFunction(symbolName)) {
            return Priority.STRING_FUNCTIONS
        }

        if (symbolName.contains("/")) {
            return Priority.NAMESPACE_FUNCTIONS
        }

        if (symbolName.startsWith("php/")) {
            return Priority.PHP_INTEROP
        }

        // Default to general API functions
        return Priority.API_FUNCTIONS
    }

    /**
     * Get priority based on function context (what function we're inside)
     */
    private fun getContextBasedPriority(symbolName: String, functionContext: String): Priority? {
        val contextSymbols = contextRelevantSymbols[functionContext]

        if (contextSymbols != null && contextSymbols.contains(symbolName)) {
            // Determine specific context type
            when (functionContext) {
                "filter", "remove",
                "every?", "some"
                    -> {
                    return Priority.CONTEXT_RELEVANT_PREDICATES
                }
                "map", "reduce", "apply"
                    -> {
                    return Priority.CONTEXT_RELEVANT_COLLECTIONS
                }
                "+", "-", "*", "/"
                    -> {
                    return Priority.CONTEXT_RELEVANT_NUMERIC
                }
            }
        }
        return null
    }

    /**
     * Initialize context-relevant symbol mappings
     */
    private fun initializeContextualSymbols() {
        // Predicate context - suggest predicate functions
        val predicateContext: MutableSet<String> = HashSet<String>()
        Collections.addAll(
            predicateContext,
            "nil?", "empty?", "even?", "odd?", "pos?", "neg?", "zero?",
            "true?", "false?", "some?", "every?", "string?", "number?"
        )
        contextRelevantSymbols["filter"] = predicateContext
        contextRelevantSymbols["remove"] = predicateContext
        contextRelevantSymbols["every?"] = predicateContext
        contextRelevantSymbols["some"] = predicateContext

        // Collection context - suggest collection functions
        val collectionContext: MutableSet<String> = HashSet<String>()
        Collections.addAll(
            collectionContext,
            "inc", "dec", "str", "count", "first", "rest", "get", "identity"
        )
        contextRelevantSymbols["map"] = collectionContext
        contextRelevantSymbols["reduce"] = collectionContext
        contextRelevantSymbols["apply"] = collectionContext

        // Numeric context - suggest numeric functions
        val numericContext: MutableSet<String> = HashSet<String>()
        Collections.addAll(
            numericContext,
            "inc", "dec", "abs", "max", "min", "mod", "quot", "rem"
        )
        contextRelevantSymbols["+"] = numericContext
        contextRelevantSymbols["-"] = numericContext
        contextRelevantSymbols["*"] = numericContext
        contextRelevantSymbols["/"] = numericContext
    }

    /**
     * Check if symbol is a commonly used builtin function
     */
    private fun isCommonBuiltin(name: String): Boolean {
        return mutableListOf<String?>(
            "map", "filter", "reduce", "get", "put", "count", "first", "rest",
            "+", "-", "*", "/", "=", "<", ">", "<=", ">=",
            "str", "print", "println", "nil?", "empty?", "some", "cons", "conj"
        ).contains(name)
    }

    /**
     * Check if symbol is a special form
     */
    private fun isSpecialForm(name: String): Boolean {
        return mutableListOf<String?>(
            "def", "defn", "defn-", "defmacro", "defmacro-", "defstruct",
            "let", "fn", "quote", "var", "ns"
        ).contains(name)
    }

    /**
     * Check if symbol is a control flow construct
     */
    private fun isControlFlow(name: String): Boolean {
        return mutableListOf<String?>(
            "if", "when", "when-not", "when-let", "if-let", "cond", "case",
            "do", "loop", "recur", "try", "catch", "finally", "throw"
        ).contains(name)
    }

    /**
     * Check if symbol is a collection function
     */
    private fun isCollectionFunction(name: String): Boolean {
        return mutableListOf<String?>(
            "conj", "cons", "concat", "reverse", "sort", "sort-by", "group-by",
            "partition", "take", "drop", "take-while", "drop-while", "take-last", "take-nth",
            "assoc", "dissoc", "keys", "values", "merge", "select-keys", "zipmap", "zipcoll",
            "shuffle", "frequencies", "invert", "split-at", "split-with", "partition-by"
        ).contains(name)
    }

    /**
     * Check if symbol is a string function
     */
    private fun isStringFunction(name: String): Boolean {
        return name.startsWith("str/") || mutableListOf<String?>(
            "str", "subs", "format", "split", "join", "trim", "replace",
            "name", "namespace", "full-name", "print-str"
        ).contains(name)
    }

    /**
     * Get current function context (what function call we're inside)
     */
    private fun getCurrentFunctionContext(element: PsiElement): String? {
        try {
            val parentList = findParentFunctionCall(element) ?: return null

            val children: Array<PsiElement> = PhelCompletionErrorHandler.safeGetChildren(parentList)

            for (i in children.indices) {
                val child = PhelCompletionErrorHandler.safeArrayAccess(children, i) ?: continue

                // Skip opening parenthesis, look for actual function name
                if (child is PhelSymbol) {
                    val context = PhelCompletionErrorHandler.safeGetText(child)
                    if (!context.isEmpty() && (context != "(") && (context != ")")) {
                        return context
                    }
                }
                // Try PhelAccessImpl as well
                if (child is PhelAccessImpl) {
                    val context = PhelCompletionErrorHandler.safeGetText(child)
                    if (!context.isEmpty() && (context != "(") && (context != ")")) {
                        return context
                    }
                }
            }
        } catch (e: Exception) {
            LOG.warn("Error getting current function context", e)
        }

        return null
    }

    /**
     * Find the parent function call list
     */
    private fun findParentFunctionCall(element: PsiElement): PhelList? {
        try {
            var current = element.parent
            var depth = 0
            val maxDepth = 50 // Prevent infinite loops in malformed PSI trees

            while (current != null && depth < maxDepth) {
                if (current is PhelList) {
                    val list = current
                    val children: Array<PsiElement> = PhelCompletionErrorHandler.safeGetChildren(list)

                    // Make sure this list has a function name (not just an empty parenthesis)
                    if (children.isNotEmpty()) {
                        val firstChild = PhelCompletionErrorHandler.safeArrayAccess(children, 0)
                        if (firstChild != null) {
                            val firstChildText = PhelCompletionErrorHandler.safeGetText(firstChild)
                            // Skip lists that start with just "(" - look for actual function calls
                            if (!(firstChildText == "(" && children.size <= 2)) {
                                return list
                            }
                        }
                    }
                }
                current = current.parent
                depth++
            }

            if (depth >= maxDepth) {
                LOG.warn("Maximum PSI tree traversal depth reached while finding parent function call")
            }
        } catch (e: Exception) {
            LOG.warn("Error finding parent function call", e)
        }
        return null
    }

    /**
     * Check if this is a recursive function call
     */
    private fun isRecursiveCall(): Boolean {
        // This would need to check if we're inside a function definition with the same name
        // For now, return false - this could be enhanced later
        return false
    }

    /**
     * Check if symbol is defined in the same file
     */
    private fun isInSameFile(): Boolean {
        // Simple heuristic - this could be enhanced with actual symbol resolution
        return true
    }

    /**
     * Priority levels for different types of completions
     * Higher values appear first in completion list
     */
    enum class Priority(@JvmField val value: Double) {
        // Highest priority - symbols in current scope
        CURRENT_SCOPE_LOCALS(100.0),  // Parameters, let bindings in immediate scope
        NESTED_SCOPE_LOCALS(95.0),  // Variables from parent scopes
        CURRENT_FUNCTION_RECURSIVE(90.0),  // Current function name for recursive calls

        // High priority - recently used and contextually relevant
        RECENT_DEFINITIONS(85.0),  // Recently defined functions in current file
        CONTEXT_RELEVANT_PREDICATES(80.0),  // Predicate functions when predicate expected
        CONTEXT_RELEVANT_COLLECTIONS(75.0),  // Collection functions when collection expected
        CONTEXT_RELEVANT_NUMERIC(70.0),  // Numeric functions when number expected

        // Medium-high priority - essential language constructs
        COMMON_BUILTINS(60.0),  // Frequently used: map, filter, +, get, etc.
        SPECIAL_FORMS(55.0),  // defn, let, if, when, etc.
        CONTROL_FLOW(50.0),  // if, when, cond, case, etc.

        // Medium priority - general API functions
        API_FUNCTIONS(40.0),  // General Phel API functions
        COLLECTION_FUNCTIONS(35.0),  // Collection manipulation functions
        STRING_FUNCTIONS(30.0),  // String processing functions

        // Lower priority - project and external symbols
        PROJECT_SYMBOLS(25.0),  // Functions from other files in project
        NAMESPACE_FUNCTIONS(20.0),  // Functions requiring namespace qualification
        MACROS(15.0),  // Macro definitions

        // Lowest priority - external integrations
        PHP_INTEROP(10.0),  // PHP interop functions
        DEPRECATED_FUNCTIONS(5.0) // Deprecated or rarely used functions
    }
}
