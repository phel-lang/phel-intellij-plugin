package org.phellang.core.psi

import com.intellij.psi.util.PsiTreeUtil
import org.phellang.completion.data.PhelFunctionRegistry
import org.phellang.completion.infrastructure.PhelCompletionPriority
import org.phellang.core.utils.PhelErrorHandler
import org.phellang.language.psi.*

object PhelSymbolAnalyzer {

    @JvmStatic
    fun isSymbolType(symbolText: String?, priority: PhelCompletionPriority): Boolean {
        if (symbolText == null) return false

        return symbolText in PhelFunctionRegistry.getFunctions(priority).map { it.name }
    }

    @JvmStatic
    fun isDefinition(symbol: PhelSymbol): Boolean {
        return PhelErrorHandler.safeOperation {
            // Check if this symbol is a function parameter
            if (isFunctionParameter(symbol)) {
                return@safeOperation true
            }

            // Check if this symbol is a let binding
            if (isLetBinding(symbol)) {
                return@safeOperation true
            }

            // Check if this symbol is the first element in a list starting with def, defn, etc.
            val containingList = PsiTreeUtil.getParentOfType(symbol, PhelList::class.java)
            if (containingList != null) {
                val firstForm = PsiTreeUtil.findChildOfType(containingList, PhelForm::class.java)
                if (firstForm != null) {
                    val firstSymbol = PsiTreeUtil.findChildOfType(firstForm, PhelSymbol::class.java)
                    if (firstSymbol != null) {
                        val firstSymbolText = firstSymbol.text
                        if (isSymbolType(firstSymbolText, PhelCompletionPriority.SPECIAL_FORMS)) {
                            // Check if this symbol is the second element (the name being defined)
                            val forms = PsiTreeUtil.getChildrenOfType(containingList, PhelForm::class.java)
                            if (forms != null && forms.size > 1) {
                                val definedName = PsiTreeUtil.findChildOfType(forms[1], PhelSymbol::class.java)
                                return@safeOperation symbol === definedName
                            }
                        }
                    }
                }
            }

            false
        } ?: false
    }

    @JvmStatic
    fun isFunctionParameter(symbol: PhelSymbol): Boolean {
        return PhelErrorHandler.safeOperation {
            // Check if symbol is inside a parameter vector
            val paramVec = PsiTreeUtil.getParentOfType(symbol, PhelVec::class.java) ?: return@safeOperation false

            // Check if the parameter vector is part of a function definition
            val containingList =
                PsiTreeUtil.getParentOfType(paramVec, PhelList::class.java) ?: return@safeOperation false

            val forms = PsiTreeUtil.getChildrenOfType(containingList, PhelForm::class.java)
            if (forms == null || forms.size < 2) return@safeOperation false

            // Check first symbol to see if it's a function-defining form
            val firstSymbol =
                PsiTreeUtil.findChildOfType(forms[0], PhelSymbol::class.java) ?: return@safeOperation false

            val keyword = firstSymbol.text

            if (isSymbolType(keyword, PhelCompletionPriority.SPECIAL_FORMS)) {
                // For defn/defn-/defmacro, find the first vector after the function name
                // This handles: (defn name [params]), (defn name "doc" [params]), 
                // (defn name {:meta} [params]), (defn name "doc" {:meta} [params])
                return@safeOperation findParameterVectorInDefn(forms, paramVec)
            } else if (keyword == "fn") {
                // For (fn [params] ...), parameter vector is at forms[1]
                return@safeOperation forms.size >= 2 && forms[1] === paramVec
            }

            false
        } ?: false
    }

    @JvmStatic
    fun isLetBinding(symbol: PhelSymbol): Boolean {
        return PhelErrorHandler.safeOperation {
            // Check if symbol is inside a binding vector
            val bindingVec = PsiTreeUtil.getParentOfType(symbol, PhelVec::class.java) ?: return@safeOperation false

            // Check if the binding vector is part of a let form
            val containingList =
                PsiTreeUtil.getParentOfType(bindingVec, PhelList::class.java) ?: return@safeOperation false

            val forms = PsiTreeUtil.getChildrenOfType(containingList, PhelForm::class.java)
            if (forms == null || forms.size < 2) return@safeOperation false

            // Check if first symbol is a binding form
            val firstSymbol =
                PsiTreeUtil.findChildOfType(forms[0], PhelSymbol::class.java) ?: return@safeOperation false
            val formType = firstSymbol.text
            if (!isSymbolType(formType, PhelCompletionPriority.CONTROL_FLOW)) return@safeOperation false

            // Check if binding vector is at forms[1]
            if (forms[1] !== bindingVec) return@safeOperation false

            // Check if symbol is in an even position (binding symbols are at even indices)
            val bindings = PsiTreeUtil.getChildrenOfType(bindingVec, PhelForm::class.java) ?: return@safeOperation false

            var i = 0
            while (i < bindings.size) {
                val bindingSymbol = PsiTreeUtil.findChildOfType(bindings[i], PhelSymbol::class.java)
                if (bindingSymbol === symbol) {
                    return@safeOperation true // Found symbol at even index (binding position)
                }
                i += 2
            }

            false
        } ?: false
    }

    @JvmStatic
    fun isParameterReference(symbol: PhelSymbol): Boolean {
        return PhelErrorHandler.safeOperation {
            val symbolText = symbol.text ?: return@safeOperation false

            // Don't highlight if this is the parameter definition itself
            if (isFunctionParameter(symbol) || isLetBinding(symbol)) {
                return@safeOperation false
            }

            // Look for function parameters in the containing function
            val containingFunction = findContainingFunction(symbol)
            if (containingFunction != null) {
                val parameters = extractFunctionParameters(containingFunction)
                if (parameters.contains(symbolText)) {
                    return@safeOperation true
                }
            }

            // Look for let bindings in the containing scopes
            if (isReferenceToLetBinding(symbol, symbolText)) {
                return@safeOperation true
            }

            // Look for references to locally defined functions in the same file
            if (isReferenceToLocalFunction(symbol, symbolText)) {
                return@safeOperation true
            }

            false
        } ?: false
    }

    /**
     * Find the containing function definition for a symbol
     */
    private fun findContainingFunction(symbol: PhelSymbol): PhelList? {
        var current = symbol.parent
        while (current != null) {
            if (current is PhelList) {
                val children = current.children
                if (children.isNotEmpty()) {
                    val firstChild = children[0]
                    if (firstChild is PhelSymbol || firstChild is PhelAccess) {
                        val functionType = firstChild.text
                        if (functionType == "defn" || functionType == "defn-" || functionType == "defmacro" || functionType == "defmacro-" || functionType == "fn") {
                            return current
                        }
                    }
                }
            }
            current = current.parent
        }
        return null
    }

    private fun extractFunctionParameters(functionList: PhelList): Set<String> {
        val parameters = mutableSetOf<String>()

        // Find parameter vector dynamically
        val paramVec = findParameterVector(functionList)
        if (paramVec != null) {
            for (paramChild in paramVec.children) {
                if (paramChild is PhelSymbol || paramChild is PhelAccess) {
                    val paramName = paramChild.text
                    if (paramName != null && paramName.isNotEmpty()) {
                        parameters.add(paramName)
                    }
                }
            }
        }

        return parameters
    }

    private fun isReferenceToLetBinding(symbol: PhelSymbol, symbolText: String): Boolean {
        var current = symbol.parent
        while (current != null) {
            if (current is PhelList) {
                val children = current.children
                if (children.isNotEmpty()) {
                    val firstChild = children[0]
                    if (firstChild is PhelSymbol || firstChild is PhelAccess) {
                        val bindingType = firstChild.text
                        if (bindingType == "let" || bindingType == "for" || bindingType == "loop" || bindingType == "binding") {

                            // Check if symbolText is in the binding vector
                            if (children.size > 1) {
                                val bindingElement = children[1]
                                if (bindingElement is PhelVec) {
                                    val bindingChildren = bindingElement.children
                                    for (i in bindingChildren.indices step 2) {
                                        val bindingChild = bindingChildren[i]
                                        if ((bindingChild is PhelSymbol || bindingChild is PhelAccess) && bindingChild.text == symbolText) {
                                            return true
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            current = current.parent
        }
        return false
    }

    private fun isReferenceToLocalFunction(symbol: PhelSymbol, symbolText: String): Boolean {
        // Don't highlight if this is the function definition itself
        if (isLocalFunctionDefinition(symbol)) {
            return false
        }

        val file = symbol.containingFile as? PhelFile ?: return false

        // Search through all top-level forms in the file
        for (child in file.children) {
            if (child is PhelList) {
                val children = child.children
                if (children.size >= 2) {
                    val firstChild = children[0]
                    val secondChild = children[1]

                    // Check if this is a function definition (defn, defn-, defmacro, etc.)
                    if (firstChild is PhelSymbol || firstChild is PhelAccess) {
                        val functionType = firstChild.text
                        if (functionType == "defn" || functionType == "defn-" || functionType == "defmacro" || functionType == "defmacro-") {

                            // Check if the function name matches our symbol
                            if ((secondChild is PhelSymbol || secondChild is PhelAccess) && secondChild.text == symbolText) {
                                return true
                            }
                        }
                    }
                }
            }
        }
        return false
    }

    private fun isLocalFunctionDefinition(symbol: PhelSymbol): Boolean {
        val parent = symbol.parent
        if (parent is PhelList) {
            val children = parent.children
            if (children.size >= 2) {
                val firstChild = children[0]
                val secondChild = children[1]
                // Check if this is a defn form and the symbol is the function name
                if ((firstChild is PhelSymbol || firstChild is PhelAccess) && (firstChild.text == "defn" || firstChild.text == "defn-" || firstChild.text == "defmacro" || firstChild.text == "defmacro-") && secondChild == symbol) {
                    return true
                }
            }
        }
        return false
    }

    /**
     * Find the parameter vector in a function definition.
     * Handles both fn and defn forms with optional docstrings and metadata.
     */
    private fun findParameterVector(functionList: PhelList): PhelVec? {
        val children = functionList.children
        if (children.isEmpty()) return null

        val firstChild = children[0]
        val functionType = when {
            firstChild is PhelSymbol -> firstChild.text
            firstChild is PhelAccess -> firstChild.text
            else -> return null
        }

        return when (functionType) {
            "fn" -> {
                // For (fn [params] ...), parameter vector is at index 1
                if (children.size >= 2 && children[1] is PhelVec) {
                    children[1] as PhelVec
                } else null
            }

            "defn", "defn-", "defmacro", "defmacro-" -> {
                // For defn forms, find the first vector after the function name
                // Skip docstring and metadata: (defn name "doc" {:meta} [params] ...)
                for (i in 2 until children.size) {
                    if (children[i] is PhelVec) {
                        return children[i] as PhelVec
                    }
                }
                null
            }

            else -> null
        }
    }

    /**
     * Find the parameter vector in a defn/defn-/defmacro form.
     * The parameter vector is the first PhelVec after the function name,
     * skipping any docstring or metadata map.
     */
    private fun findParameterVectorInDefn(forms: Array<PhelForm>, targetVec: PhelVec): Boolean {
        // Start from index 2 (after defn and function name)
        for (i in 2 until forms.size) {
            val form = forms[i]

            // Check if this form contains our target vector
            if (form === targetVec.parent) {
                // This is the first vector we encounter, so it should be the parameter vector
                return true
            }

            // If we encounter a vector that's not our target, it means our target
            // is not the parameter vector (it might be inside the function body)
            if (PsiTreeUtil.findChildOfType(form, PhelVec::class.java) != null) {
                return false
            }
        }
        return false
    }
}
