package org.phellang.completion;

import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.completion.PrioritizedLookupElement;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.phellang.language.psi.PhelList;
import org.phellang.language.psi.PhelSymbol;

import javax.swing.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Smart completion ranking system for Phel language
 * Provides context-aware prioritization of completion suggestions
 */
public class PhelCompletionRanking {
    
    private static final Logger LOG = Logger.getInstance(PhelCompletionRanking.class);

    /**
     * Priority levels for different types of completions
     * Higher values appear first in completion list
     */
    public enum Priority {
        // Highest priority - symbols in current scope
        CURRENT_SCOPE_LOCALS(100.0),     // Parameters, let bindings in immediate scope
        NESTED_SCOPE_LOCALS(95.0),       // Variables from parent scopes
        CURRENT_FUNCTION_RECURSIVE(90.0), // Current function name for recursive calls
        
        // High priority - recently used and contextually relevant
        RECENT_DEFINITIONS(85.0),         // Recently defined functions in current file
        CONTEXT_RELEVANT_PREDICATES(80.0), // Predicate functions when predicate expected
        CONTEXT_RELEVANT_COLLECTIONS(75.0), // Collection functions when collection expected
        CONTEXT_RELEVANT_NUMERIC(70.0),   // Numeric functions when number expected
        
        // Medium-high priority - essential language constructs
        COMMON_BUILTINS(60.0),            // Frequently used: map, filter, +, get, etc.
        SPECIAL_FORMS(55.0),              // defn, let, if, when, etc.
        CONTROL_FLOW(50.0),               // if, when, cond, case, etc.
        
        // Medium priority - general API functions
        API_FUNCTIONS(40.0),              // General Phel API functions
        COLLECTION_FUNCTIONS(35.0),       // Collection manipulation functions
        STRING_FUNCTIONS(30.0),           // String processing functions
        
        // Lower priority - project and external symbols
        PROJECT_SYMBOLS(25.0),            // Functions from other files in project
        NAMESPACE_FUNCTIONS(20.0),        // Functions requiring namespace qualification
        MACROS(15.0),                     // Macro definitions
        
        // Lowest priority - external integrations
        PHP_INTEROP(10.0),                // PHP interop functions
        DEPRECATED_FUNCTIONS(5.0);        // Deprecated or rarely used functions
        
        public final double value;
        
        Priority(double value) {
            this.value = value;
        }
    }
    
    // Cache for symbol usage frequency (simple implementation)
    private static final Map<String, Integer> usageFrequency = new ConcurrentHashMap<>();
    
    // Cache for context-relevant symbols
    private static final Map<String, Set<String>> contextRelevantSymbols = new ConcurrentHashMap<>();
    
    static {
        initializeContextualSymbols();
    }
    
    /**
     * Create a ranked lookup element with priority and additional metadata
     */
    public static LookupElementBuilder createRankedElement(@NotNull String name, 
                                                         @NotNull Priority priority, 
                                                         @Nullable String signature,
                                                         @Nullable String description,
                                                         @Nullable Icon icon) {
        LookupElementBuilder builder = LookupElementBuilder.create(name);
                
        if (signature != null) {
            builder = builder.withTypeText(description);
        }
        
        if (description != null) {
            builder = builder.withTailText(" " + signature, true);
        }
        
        if (icon != null) {
            builder = builder.withIcon(icon);
        }
        
        // Add usage-based bonus by modifying the presentation
        int usageCount = usageFrequency.getOrDefault(name, 0);
        if (usageCount > 0) {
            // Add usage indicator to help with visual prioritization
            builder = builder.withTailText(description != null ? 
                " - " + description + " (used " + usageCount + "x)" : 
                " (used " + usageCount + "x)", true);
        }
        
        // Note: Priority will be applied when adding to result set
        
        return builder;
    }
    
    /**
     * Add a ranked completion to the result set
     */
    public static void addRankedCompletion(@NotNull CompletionResultSet result,
                                         @NotNull String name,
                                         @NotNull Priority priority,
                                         @Nullable String signature,
                                         @Nullable String description,
                                         @Nullable Icon icon) {
        LookupElement element = createRankedElement(name, priority, signature, description, icon);
        // Apply priority using IntelliJ's PrioritizedLookupElement
        result.addElement(PrioritizedLookupElement.withPriority(element, priority.value));
    }
    
    /**
     * Determine the appropriate priority for a symbol based on context
     */
    public static Priority getContextualPriority(@NotNull String symbolName, 
                                                @NotNull PsiElement context,
                                                @NotNull String symbolType) {
        
        // Check for local scope symbols first (highest priority)
        if ("Parameter".equals(symbolType) || "Local Variable".equals(symbolType)) {
            return Priority.CURRENT_SCOPE_LOCALS;
        }
        
        if ("Function".equals(symbolType) && isInSameFile(symbolName, context)) {
            return Priority.RECENT_DEFINITIONS;
        }
        
        // Check for recursive function calls
        if (isRecursiveCall(symbolName, context)) {
            return Priority.CURRENT_FUNCTION_RECURSIVE;
        }
        
        // Context-aware prioritization based on expected argument types
        String functionContext = getCurrentFunctionContext(context);
        if (functionContext != null) {
            Priority contextPriority = getContextBasedPriority(symbolName, functionContext);
            if (contextPriority != null) {
                return contextPriority;
            }
        }
        
        // Built-in function prioritization
        if (isCommonBuiltin(symbolName)) {
            return Priority.COMMON_BUILTINS;
        }
        
        if (isSpecialForm(symbolName)) {
            return Priority.SPECIAL_FORMS;
        }
        
        if (isControlFlow(symbolName)) {
            return Priority.CONTROL_FLOW;
        }
        
        // API function categorization
        if (symbolName.endsWith("?")) {
            return Priority.API_FUNCTIONS; // Predicate functions
        }
        
        if (isCollectionFunction(symbolName)) {
            return Priority.COLLECTION_FUNCTIONS;
        }
        
        if (isStringFunction(symbolName)) {
            return Priority.STRING_FUNCTIONS;
        }
        
        if (symbolName.contains("/")) {
            return Priority.NAMESPACE_FUNCTIONS;
        }
        
        if (symbolName.startsWith("php/")) {
            return Priority.PHP_INTEROP;
        }
        
        // Default to general API functions
        return Priority.API_FUNCTIONS;
    }
    
    /**
     * Get priority based on function context (what function we're inside)
     */
    @Nullable
    private static Priority getContextBasedPriority(@NotNull String symbolName, @NotNull String functionContext) {
        Set<String> contextSymbols = contextRelevantSymbols.get(functionContext);
        
        if (contextSymbols != null && contextSymbols.contains(symbolName)) {
            // Determine specific context type
            if (functionContext.equals("filter") || functionContext.equals("remove") || 
                functionContext.equals("every?") || functionContext.equals("some")) {
                return Priority.CONTEXT_RELEVANT_PREDICATES;
            } else if (functionContext.equals("map") || functionContext.equals("reduce") || 
                       functionContext.equals("apply")) {
                return Priority.CONTEXT_RELEVANT_COLLECTIONS;
            } else if (functionContext.equals("+") || functionContext.equals("-") || 
                       functionContext.equals("*") || functionContext.equals("/")) {
                return Priority.CONTEXT_RELEVANT_NUMERIC;
            }
        }
        return null;
    }
    
    /**
     * Initialize context-relevant symbol mappings
     */
    private static void initializeContextualSymbols() {
        // Predicate context - suggest predicate functions
        Set<String> predicateContext = new HashSet<>();
        Collections.addAll(predicateContext, 
            "nil?", "empty?", "even?", "odd?", "pos?", "neg?", "zero?", 
            "true?", "false?", "some?", "every?", "string?", "number?");
        contextRelevantSymbols.put("filter", predicateContext);
        contextRelevantSymbols.put("remove", predicateContext);
        contextRelevantSymbols.put("every?", predicateContext);
        contextRelevantSymbols.put("some", predicateContext);
        
        // Collection context - suggest collection functions
        Set<String> collectionContext = new HashSet<>();
        Collections.addAll(collectionContext,
            "inc", "dec", "str", "count", "first", "rest", "get", "identity");
        contextRelevantSymbols.put("map", collectionContext);
        contextRelevantSymbols.put("reduce", collectionContext);
        contextRelevantSymbols.put("apply", collectionContext);
        
        // Numeric context - suggest numeric functions
        Set<String> numericContext = new HashSet<>();
        Collections.addAll(numericContext,
            "inc", "dec", "abs", "max", "min", "mod", "quot", "rem");
        contextRelevantSymbols.put("+", numericContext);
        contextRelevantSymbols.put("-", numericContext);
        contextRelevantSymbols.put("*", numericContext);
        contextRelevantSymbols.put("/", numericContext);
    }
    
    /**
     * Check if symbol is a commonly used builtin function
     */
    private static boolean isCommonBuiltin(@NotNull String name) {
        return Arrays.asList(
            "map", "filter", "reduce", "get", "put", "count", "first", "rest",
            "+", "-", "*", "/", "=", "<", ">", "<=", ">=",
            "str", "print", "println", "nil?", "empty?"
        ).contains(name);
    }
    
    /**
     * Check if symbol is a special form
     */
    private static boolean isSpecialForm(@NotNull String name) {
        return Arrays.asList(
            "def", "defn", "defn-", "defmacro", "defmacro-", "defstruct",
            "let", "fn", "quote", "var", "ns"
        ).contains(name);
    }
    
    /**
     * Check if symbol is a control flow construct
     */
    private static boolean isControlFlow(@NotNull String name) {
        return Arrays.asList(
            "if", "when", "when-not", "when-let", "if-let", "cond", "case",
            "do", "loop", "recur", "try", "catch", "finally", "throw"
        ).contains(name);
    }
    
    /**
     * Check if symbol is a collection function
     */
    private static boolean isCollectionFunction(@NotNull String name) {
        return Arrays.asList(
            "conj", "cons", "concat", "reverse", "sort", "sort-by", "group-by",
            "partition", "take", "drop", "take-while", "drop-while",
            "assoc", "dissoc", "keys", "vals", "merge", "select-keys"
        ).contains(name);
    }
    
    /**
     * Check if symbol is a string function  
     */
    private static boolean isStringFunction(@NotNull String name) {
        return name.startsWith("str/") || Arrays.asList(
            "str", "subs", "format", "split", "join", "trim", "replace"
        ).contains(name);
    }
    
    /**
     * Get current function context (what function call we're inside)
     */
    @Nullable
    private static String getCurrentFunctionContext(@NotNull PsiElement element) {
        try {
            PhelList parentList = findParentFunctionCall(element);
            if (parentList == null) {
                return null;
            }
            
            PsiElement[] children = PhelCompletionErrorHandler.safeGetChildren(parentList);
            
            for (int i = 0; i < children.length; i++) {
                PsiElement child = PhelCompletionErrorHandler.safeArrayAccess(children, i);
                if (child == null) {
                    continue;
                }
                
                // Skip opening parenthesis, look for actual function name
                if (child instanceof PhelSymbol) {
                    String context = PhelCompletionErrorHandler.safeGetText(child);
                    if (!context.isEmpty() && !context.equals("(") && !context.equals(")")) {
                        return context;
                    }
                }
                // Try PhelAccessImpl as well
                if (child instanceof org.phellang.language.psi.impl.PhelAccessImpl) {
                    String context = PhelCompletionErrorHandler.safeGetText(child);
                    if (!context.isEmpty() && !context.equals("(") && !context.equals(")")) {
                        return context;
                    }
                }
            }
            
        } catch (Exception e) {
            LOG.warn("Error getting current function context", e);
        }
        
        return null;
    }
    
    /**
     * Find the parent function call list
     */
    @Nullable
    private static PhelList findParentFunctionCall(@NotNull PsiElement element) {
        try {
            PsiElement current = element.getParent();
            int depth = 0;
            final int MAX_DEPTH = 50; // Prevent infinite loops in malformed PSI trees
            
            while (current != null && depth < MAX_DEPTH) {
                if (current instanceof PhelList) {
                    PhelList list = (PhelList) current;
                    PsiElement[] children = PhelCompletionErrorHandler.safeGetChildren(list);
                    
                    // Make sure this list has a function name (not just an empty parenthesis)
                    if (children.length > 0) {
                        PsiElement firstChild = PhelCompletionErrorHandler.safeArrayAccess(children, 0);
                        if (firstChild != null) {
                            String firstChildText = PhelCompletionErrorHandler.safeGetText(firstChild);
                            // Skip lists that start with just "(" - look for actual function calls
                            if (!(firstChildText.equals("(") && children.length <= 2)) {
                                return list;
                            }
                        }
                    }
                }
                current = current.getParent();
                depth++;
            }
            
            if (depth >= MAX_DEPTH) {
                LOG.warn("Maximum PSI tree traversal depth reached while finding parent function call");
            }
            
        } catch (Exception e) {
            LOG.warn("Error finding parent function call", e);
        }
        return null;
    }
    
    /**
     * Check if this is a recursive function call
     */
    private static boolean isRecursiveCall(@NotNull String symbolName, @NotNull PsiElement context) {
        // This would need to check if we're inside a function definition with the same name
        // For now, return false - this could be enhanced later
        return false;
    }
    
    /**
     * Check if symbol is defined in the same file
     */
    private static boolean isInSameFile(@NotNull String symbolName, @NotNull PsiElement context) {
        // Simple heuristic - this could be enhanced with actual symbol resolution
        return true;
    }
    
    /**
     * Record usage of a symbol for frequency-based ranking
     */
    public static void recordUsage(@NotNull String symbolName) {
        usageFrequency.merge(symbolName, 1, Integer::sum);
    }
    
    /**
     * Clear usage statistics (useful for testing or reset)
     */
    public static void clearUsageStats() {
        usageFrequency.clear();
    }
    
    /**
     * Get current usage statistics for debugging
     */
    public static Map<String, Integer> getUsageStats() {
        return new HashMap<>(usageFrequency);
    }
    
    /**
     * Get priority prefix for sorting - higher priority items sort first
     * Using ASCII characters where lower values sort first
     */
    private static String getPriorityPrefix(@NotNull Priority priority) {
        // Convert priority to sortable prefix (inverse so higher priorities come first)
        int sortValue = (int)(110.0 - priority.value);  // Invert so higher priority = lower sort value
        char prefixChar = (char)Math.max(32, Math.min(126, sortValue)); // Keep in printable ASCII range
        return String.valueOf(prefixChar) + "|"; // Add separator
    }
}
