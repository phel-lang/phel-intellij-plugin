package org.phellang.completion;

import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;
import org.phellang.language.psi.*;
import org.phellang.language.psi.impl.PhelAccessImpl;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Performance optimizations for Phel completion system
 * Includes caching, smart traversal limits, and efficient symbol lookup
 */
public class PhelCompletionPerformanceOptimizer {

    // Cache for symbol resolution to avoid repeated PSI tree traversals
    private static final Map<String, Boolean> symbolExistsCache = new ConcurrentHashMap<>();
    
    // Cache for function signatures to avoid repeated lookups
    private static final Map<String, String> signatureCache = new ConcurrentHashMap<>();
    
    // Maximum traversal depth to prevent performance issues in deeply nested code
    private static final int MAX_TRAVERSAL_DEPTH = 20;
    
    // Maximum number of symbols to process in one completion request
    private static final int MAX_SYMBOLS_PER_REQUEST = 100;

    /**
     * Check if we should skip expensive completion operations based on context
     */
    public static boolean shouldSkipExpensiveOperations(@NotNull PsiElement position) {
        // Skip in very large files
        if (position.getContainingFile().getTextLength() > 100000) {
            return true;
        }
        
        // Skip if deeply nested
        if (getDepth(position) > MAX_TRAVERSAL_DEPTH) {
            return true;
        }
        
        return false;
    }

    /**
     * Efficient local scope traversal - stops at function boundaries
     */
    public static PsiElement findLocalScopeRoot(@NotNull PsiElement position) {
        PsiElement current = position;
        int depth = 0;
        
        while (current != null && depth < MAX_TRAVERSAL_DEPTH) {
            if (current instanceof PhelList) {
                PhelList list = (PhelList) current;
                PsiElement firstChild = list.getFirstChild();
                if (firstChild instanceof PhelSymbol) {
                    String symbolName = firstChild.getText();
                    // Stop at function/macro boundaries - don't look beyond current scope
                    if (symbolName.equals("defn") || symbolName.equals("defn-") || 
                        symbolName.equals("defmacro") || symbolName.equals("defmacro-") ||
                        symbolName.equals("fn")) {
                        return current;
                    }
                }
            }
            current = current.getParent();
            depth++;
        }
        
        // If no scope found, return the file
        return position.getContainingFile();
    }

    /**
     * Fast symbol existence check with caching
     */
    public static boolean symbolExists(String symbolName) {
        return symbolExistsCache.computeIfAbsent(symbolName, name -> {
            // Only check against core function names for performance
            return isCoreFunction(name);
        });
    }

    /**
     * Cached function signature lookup
     */
    public static String getCachedSignature(String functionName) {
        return signatureCache.computeIfAbsent(functionName, name -> {
            // This could be expanded to load from documentation or PSI analysis
            return getBasicSignature(name);
        });
    }

    /**
     * Optimized binding collection - limits scope and depth
     */
    public static void collectBindingsEfficiently(@NotNull PsiElement position, 
                                                 @NotNull Map<String, String> bindings) {
        PsiElement scopeRoot = findLocalScopeRoot(position);
        collectBindingsInScope(scopeRoot, position, bindings, 0);
    }

    /**
     * Recursive binding collection with depth limits
     */
    private static void collectBindingsInScope(@NotNull PsiElement scope, 
                                             @NotNull PsiElement position,
                                             @NotNull Map<String, String> bindings,
                                             int depth) {
        if (depth >= MAX_TRAVERSAL_DEPTH || bindings.size() >= MAX_SYMBOLS_PER_REQUEST) {
            return;
        }


        if (scope instanceof PhelList) {
            PhelList list = (PhelList) scope;
            PsiElement[] children = list.getChildren();
            
            for (int i = 0; i < Math.min(3, children.length); i++) {
            }
            
            // Look for binding forms - check all children, not just first
            for (int i = 0; i < children.length; i++) {
                PsiElement child = children[i];
                if (child instanceof PhelSymbol || child instanceof PhelAccessImpl) {
                    String symbolName = child.getText();
                    
                    // Handle different binding forms efficiently
                    switch (symbolName) {
                        case "let":
                        case "binding":
                        case "loop":
                        case "if-let":
                        case "when-let":
                            String bindingType;
                            switch (symbolName) {
                                case "loop": bindingType = "Loop Binding"; break;
                                case "if-let": bindingType = "If-Let Binding"; break;
                                case "when-let": bindingType = "When-Let Binding"; break;
                                default: bindingType = "Let Binding"; break;
                            }
                            PhelVec bindingVec = PsiTreeUtil.findChildOfType(list, PhelVec.class);
                            if (bindingVec != null) {
                                // EDGE CASE FIX: Check for empty binding vector
                                PsiElement[] vecChildren = bindingVec.getChildren();
                                if (vecChildren.length == 0) {
                                } else {
                                    extractBindingsFromVector(bindingVec, bindings, bindingType);
                                }
                            } else {
                            }
                            break;
                        case "defn":
                        case "defn-":
                        case "fn":
                            PhelVec paramVec = PsiTreeUtil.findChildOfType(list, PhelVec.class);
                            if (paramVec != null) {
                                extractParametersFromVector(paramVec, bindings, "Function Parameter");
                            }
                            // Continue recursing to find let bindings inside function body
                            break;
                        case "for":
                        case "dofor":
                            PhelVec forVec = PsiTreeUtil.findChildOfType(list, PhelVec.class);
                            if (forVec != null) {
                                extractBindingsFromVector(forVec, bindings, "For Binding");
                            }
                            break;
                        case "catch":
                            // EDGE CASE FIX: Robust catch parsing (catch ExceptionType varname ...)
                            PsiElement[] catchChildren = list.getChildren();
                            if (catchChildren.length >= 3) {
                                for (int j = 0; j < catchChildren.length; j++) {
                                }
                                // Find the catch variable (should be after exception type)
                                boolean foundExceptionType = false;
                                for (int j = 0; j < catchChildren.length; j++) {
                                    PsiElement catchElement = catchChildren[j];
                                    if (catchElement instanceof PhelSymbol || catchElement instanceof PhelAccessImpl) {
                                        String elementText = catchElement.getText();
                                        if (elementText.equals("catch")) {
                                            continue; // Skip the 'catch' keyword
                                        }
                                        if (!foundExceptionType) {
                                            foundExceptionType = true; // This is the exception type
                                            continue;
                                        }
                                        // This should be the catch variable
                                        if (!elementText.startsWith("(") && !bindings.containsKey(elementText)) {
                                            bindings.put(elementText, "Catch Binding");
                                            break;
                                        }
                                    }
                                }
                            } else {
                            }
                            break;
                    }
                }
            }
        }
        
        // Recurse into children with depth limit
        for (PsiElement child : scope.getChildren()) {
            if (PsiTreeUtil.isAncestor(child, position, false)) {
                collectBindingsInScope(child, position, bindings, depth + 1);
                break; // Only process the path to our position
            }
        }
    }

    /**
     * Fast parameter extraction from vector  
     * EDGE CASE FIXES: Handles shadowing, empty vectors, special symbols
     */
    private static void extractParametersFromVector(@NotNull PhelVec vector, 
                                                  @NotNull Map<String, String> bindings, 
                                                  String type) {
        PsiElement[] children = vector.getChildren();
        
        if (children.length == 0) {
            return;
        }
        
        for (PsiElement child : children) {
            if ((child instanceof PhelSymbol || child instanceof PhelAccessImpl) && bindings.size() < MAX_SYMBOLS_PER_REQUEST) {
                String paramName = child.getText();
                
                // EDGE CASE FIX: Skip special parameter symbols 
                if (paramName.equals("&") || paramName.equals("...")) {
                    continue;
                }
                
                // EDGE CASE FIX: Handle shadowing - innermost scope wins
                if (!bindings.containsKey(paramName)) {
                    bindings.put(paramName, type);
                } else {
                }
            }
        }
    }

    /**
     * Fast binding extraction from vector (every other element)
     */
    private static void extractBindingsFromVector(@NotNull PhelVec vector, 
                                                @NotNull Map<String, String> bindings, 
                                                String type) {
        PsiElement[] children = vector.getChildren();
        
        for (int i = 0; i < Math.min(10, children.length); i++) {
        }
        
        // EDGE CASE FIX: Proper bounds checking for malformed binding vectors
        for (int i = 0; i < children.length - 1 && bindings.size() < MAX_SYMBOLS_PER_REQUEST; i += 2) {
            PsiElement child = children[i];
            if (child instanceof PhelSymbol || child instanceof PhelAccessImpl) {
                String symbolName = child.getText();
                
                // EDGE CASE FIX: Variable shadowing - use most recent binding (closest scope)
                // Only add if not already present (first occurrence wins = innermost scope)
                if (!bindings.containsKey(symbolName)) {
                    bindings.put(symbolName, type);
                } else {
                }
            } else {
            }
        }
        
        // EDGE CASE FIX: Check for odd number of bindings (malformed)
        if (children.length % 2 != 0) {
        }
    }

    /**
     * Clear caches when file changes
     */
    public static void clearCache() {
        symbolExistsCache.clear();
        signatureCache.clear();
    }

    /**
     * Get element depth in PSI tree
     */
    private static int getDepth(@NotNull PsiElement element) {
        int depth = 0;
        PsiElement current = element.getParent();
        while (current != null && depth < MAX_TRAVERSAL_DEPTH) {
            depth++;
            current = current.getParent();
        }
        return depth;
    }

    /**
     * Fast core function check
     */
    private static boolean isCoreFunction(String name) {
        // Quick hash-based lookup for common core functions
        switch (name) {
            case "map": case "filter": case "reduce": case "get": case "put": 
            case "count": case "first": case "rest": case "str": case "print":
            case "println": case "+": case "-": case "*": case "/": case "=":
            case "if": case "let": case "defn": case "fn": case "when":
                return true;
            default:
                return name.endsWith("?") || // predicates
                       name.startsWith("def") || // definitions
                       name.length() <= 3; // operators
        }
    }

    /**
     * Basic signature for common functions
     */
    private static String getBasicSignature(String name) {
        switch (name) {
            case "get": return "coll key";
            case "map": return "f coll";
            case "filter": return "pred coll";
            case "reduce": return "f coll";
            case "if": return "test then else?";
            case "let": return "[bindings*] body*";
            default: return null;
        }
    }
}
