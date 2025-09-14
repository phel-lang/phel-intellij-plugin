package org.phellang.completion

import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import org.phellang.language.psi.PhelList
import org.phellang.language.psi.PhelSymbol
import org.phellang.language.psi.PhelVec
import org.phellang.language.psi.impl.PhelAccessImpl

/**
 * Performance optimizations for Phel completion system
 * Includes caching, smart traversal limits, and efficient symbol lookup
 */
object PhelCompletionPerformanceOptimizer {
    // Maximum traversal depth to prevent performance issues in deeply nested code
    private const val MAX_TRAVERSAL_DEPTH = 20

    // Maximum number of symbols to process in one completion request
    private const val MAX_SYMBOLS_PER_REQUEST = 100

    /**
     * Check if we should skip expensive completion operations based on context
     */
    @JvmStatic
    fun shouldSkipExpensiveOperations(position: PsiElement): Boolean {
        // Skip in very large files
        if (position.containingFile.textLength > 100000) {
            return true
        }

        // Skip if deeply nested
        if (getDepth(position) > MAX_TRAVERSAL_DEPTH) {
            return true
        }

        return false
    }

    /**
     * Efficient local scope traversal - stops at function boundaries
     */
    @JvmStatic
    fun findLocalScopeRoot(position: PsiElement): PsiElement {
        var current: PsiElement? = position
        var depth = 0

        while (current != null && depth < MAX_TRAVERSAL_DEPTH) {
            if (current is PhelList) {
                val list = current
                val firstChild = list.firstChild
                if (firstChild is PhelSymbol) {
                    val symbolName = firstChild.text
                    // Stop at function/macro boundaries - don't look beyond current scope
                    if (symbolName == "defn" || symbolName == "defn-" ||
                        symbolName == "defmacro" || symbolName == "defmacro-" ||
                        symbolName == "fn"
                    ) {
                        return current
                    }
                }
            }
            current = current.parent
            depth++
        }

        // If no scope found, return the file
        return position.containingFile
    }

    /**
     * Optimized binding collection - limits scope and depth
     */
    fun collectBindingsEfficiently(
        position: PsiElement,
        bindings: MutableMap<String?, String?>
    ) {
        val scopeRoot = findLocalScopeRoot(position)
        collectBindingsInScope(scopeRoot, position, bindings, 0)
    }

    /**
     * Recursive binding collection with depth limits
     */
    private fun collectBindingsInScope(
        scope: PsiElement,
        position: PsiElement,
        bindings: MutableMap<String?, String?>,
        depth: Int
    ) {
        if (depth >= MAX_TRAVERSAL_DEPTH || bindings.size >= MAX_SYMBOLS_PER_REQUEST) {
            return
        }

        if (scope is PhelList) {
            val children: Array<PsiElement> = scope.children

            // Look for binding forms - check all children, not just first
            for (i in children.indices) {
                val child = children[i]
                if (child is PhelSymbol || child is PhelAccessImpl) {
                    // Handle different binding forms efficiently
                    when (val symbolName = child.text) {
                        "let", "binding", "loop", "if-let", "when-let" -> {
                            val bindingType = when (symbolName) {
                                "loop" -> "Loop Binding"
                                "if-let" -> "If-Let Binding"
                                "when-let" -> "When-Let Binding"
                                else -> "Let Binding"
                            }
                            val bindingVec = PsiTreeUtil.findChildOfType(scope, PhelVec::class.java)
                            if (bindingVec != null) {
                                // EDGE CASE FIX: Check for empty binding vector
                                val vecChildren: Array<PsiElement> = bindingVec.children
                                if (vecChildren.isNotEmpty()) {
                                    extractBindingsFromVector(bindingVec, bindings, bindingType)
                                }
                            }
                        }

                        "defn", "defn-", "fn" -> {
                            val paramVec = PsiTreeUtil.findChildOfType(scope, PhelVec::class.java)
                            if (paramVec != null) {
                                extractParametersFromVector(paramVec, bindings, "Function Parameter")
                            }
                        }

                        "for", "dofor" -> {
                            val forVec = PsiTreeUtil.findChildOfType(scope, PhelVec::class.java)
                            if (forVec != null) {
                                extractBindingsFromVector(forVec, bindings, "For Binding")
                            }
                        }

                        "catch" -> {
                            // EDGE CASE FIX: Robust catch parsing (catch ExceptionType varname ...)
                            val catchChildren: Array<PsiElement> = scope.children
                            if (catchChildren.size >= 3) {
                                run {
                                    var j = 0
                                    while (j < catchChildren.size) {
                                        j++
                                    }
                                }
                                // Find the catch variable (should be after exception type)
                                var foundExceptionType = false
                                var j = 0
                                while (j < catchChildren.size) {
                                    val catchElement = catchChildren[j]
                                    if (catchElement is PhelSymbol || catchElement is PhelAccessImpl) {
                                        val elementText = catchElement.text
                                        if (elementText == "catch") {
                                            j++
                                            continue  // Skip the 'catch' keyword
                                        }
                                        if (!foundExceptionType) {
                                            foundExceptionType = true // This is the exception type
                                            j++
                                            continue
                                        }
                                        // This should be the catch variable
                                        if (!elementText.startsWith("(") && !bindings.containsKey(elementText)) {
                                            bindings[elementText] = "Catch Binding"
                                            break
                                        }
                                    }
                                    j++
                                }
                            }
                        }
                    }
                }
            }
        }

        // Recurse into children with depth limit
        for (child in scope.children) {
            if (PsiTreeUtil.isAncestor(child, position, false)) {
                collectBindingsInScope(child, position, bindings, depth + 1)
                break // Only process the path to our position
            }
        }
    }

    /**
     * Fast parameter extraction from vector
     * EDGE CASE FIXES: Handles shadowing, empty vectors, special symbols
     */
    private fun extractParametersFromVector(
        vector: PhelVec,
        bindings: MutableMap<String?, String?>,
        type: String?
    ) {
        val children: Array<PsiElement> = vector.children

        if (children.isEmpty()) {
            return
        }

        for (child in children) {
            if ((child is PhelSymbol || child is PhelAccessImpl) && bindings.size < MAX_SYMBOLS_PER_REQUEST) {
                val paramName = child.text

                // EDGE CASE FIX: Skip special parameter symbols 
                if (paramName == "&" || paramName == "...") {
                    continue
                }

                // EDGE CASE FIX: Handle shadowing - innermost scope wins
                if (!bindings.containsKey(paramName)) {
                    bindings[paramName] = type
                }
            }
        }
    }

    /**
     * Fast binding extraction from vector (every other element)
     */
    private fun extractBindingsFromVector(
        vector: PhelVec,
        bindings: MutableMap<String?, String?>,
        type: String?
    ) {
        val children: Array<PsiElement> = vector.children

        // EDGE CASE FIX: Proper bounds checking for malformed binding vectors
        var i = 0
        while (i < children.size - 1 && bindings.size < MAX_SYMBOLS_PER_REQUEST) {
            val child = children[i]
            if (child is PhelSymbol || child is PhelAccessImpl) {
                val symbolName = child.text

                // EDGE CASE FIX: Variable shadowing - use most recent binding (closest scope)
                // Only add if not already present (first occurrence wins = innermost scope)
                if (!bindings.containsKey(symbolName)) {
                    bindings[symbolName] = type
                }
            }
            i += 2
        }
    }

    /**
     * Get element depth in PSI tree
     */
    private fun getDepth(element: PsiElement): Int {
        var depth = 0
        var current = element.parent
        while (current != null && depth < MAX_TRAVERSAL_DEPTH) {
            depth++
            current = current.parent
        }
        return depth
    }
}
