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

            // Check if this symbol is the name being defined in a special form
            val containingList = PsiTreeUtil.getParentOfType(symbol, PhelList::class.java) ?: return@safeOperation false
            
            val firstForm = PsiTreeUtil.findChildOfType(containingList, PhelForm::class.java) ?: return@safeOperation false
            
            val firstSymbol = PsiTreeUtil.findChildOfType(firstForm, PhelSymbol::class.java) ?: return@safeOperation false
            
            val firstSymbolText = firstSymbol.text
            if (!isSymbolType(firstSymbolText, PhelCompletionPriority.SPECIAL_FORMS)) {
                return@safeOperation false
            }

            // Check if this symbol is the second element (the name being defined)
            val forms = PsiTreeUtil.getChildrenOfType(containingList, PhelForm::class.java) ?: return@safeOperation false
            if (forms.size <= 1) return@safeOperation false
            
            val definedName = PsiTreeUtil.findChildOfType(forms[1], PhelSymbol::class.java) ?: return@safeOperation false
            
            symbol === definedName
        } ?: false
    }

    @JvmStatic
    fun isInParameterVector(symbol: PhelSymbol): Boolean {
        return isFunctionParameter(symbol, excludeSymbols = emptySet())
    }

    @JvmStatic
    fun isFunctionParameter(symbol: PhelSymbol, excludeSymbols: Set<String> = setOf("&")): Boolean {
        return PhelErrorHandler.safeOperation {
            val paramVec = findContainingParameterVector(symbol) ?: return@safeOperation false

            // Check if this vector is a parameter vector in a function definition
            if (!isParameterVectorInFunctionDefinition(paramVec)) {
                return@safeOperation false
            }

            // Additional check: exclude specified symbols
            val symbolText = symbol.text
            if (symbolText in excludeSymbols) {
                return@safeOperation false
            }

            true
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
            val isParamDef = isFunctionParameter(symbol)
            val isLetBindingDef = isLetBinding(symbol)

            if (isParamDef || isLetBindingDef) {
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
            val isLocalFuncRef = isReferenceToLocalFunction(symbol, symbolText)
            if (isLocalFuncRef) {
                return@safeOperation true
            }

            false
        } ?: false
    }

    private fun findContainingFunction(symbol: PhelSymbol): PhelList? {
        var current = symbol.parent
        while (current != null) {
            // Skip non-list elements
            if (current !is PhelList) {
                current = current.parent
                continue
            }

            val children = current.children
            if (children.isEmpty()) {
                current = current.parent
                continue
            }

            val firstChild = children[0]
            if (firstChild !is PhelSymbol && firstChild !is PhelAccess) {
                current = current.parent
                continue
            }

            val functionType = firstChild.text
            if (isSymbolType(functionType, PhelCompletionPriority.SPECIAL_FORMS)) {
                return current
            }

            current = current.parent
        }
        return null
    }

    private fun extractFunctionParameters(functionList: PhelList): Set<String> {
        val parameters = mutableSetOf<String>()

        // Find parameter vector dynamically
        val paramVec = findParameterVector(functionList) ?: return parameters
        for (paramChild in paramVec.children) {
            if (paramChild !is PhelSymbol && paramChild !is PhelAccess) continue
            val paramName = paramChild.text
            if (paramName == null || !paramName.isNotEmpty()) continue

            if (paramName == "&") continue

            parameters.add(paramName)
        }

        return parameters
    }

    private fun isReferenceToLetBinding(symbol: PhelSymbol, symbolText: String): Boolean {
        var current = symbol.parent
        while (current != null) {
            // Skip non-list elements
            if (current !is PhelList) {
                current = current.parent
                continue
            }

            val children = current.children
            if (children.isEmpty()) {
                current = current.parent
                continue
            }

            val firstChild = children[0]
            if (firstChild !is PhelSymbol && firstChild !is PhelAccess) {
                current = current.parent
                continue
            }

            val bindingType = firstChild.text
            if (!isSymbolType(bindingType, PhelCompletionPriority.CONTROL_FLOW)) {
                current = current.parent
                continue
            }

            // Check if symbolText is in the binding vector
            if (children.size <= 1) {
                current = current.parent
                continue
            }

            val bindingElement = children[1]
            if (bindingElement !is PhelVec) {
                current = current.parent
                continue
            }

            // Search for the symbol in binding positions (even indices)
            val bindingChildren = bindingElement.children
            for (i in bindingChildren.indices step 2) {
                val bindingChild = bindingChildren[i]
                if ((bindingChild is PhelSymbol || bindingChild is PhelAccess) && bindingChild.text == symbolText) {
                    return true
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
            if (child !is PhelList) continue
            
            val children = child.children
            if (children.size < 2) continue
            
            val firstChild = children[0]
            val secondChild = children[1]

            // Check if this is a function definition (defn, defn-, defmacro, etc.)
            if (firstChild !is PhelSymbol && firstChild !is PhelAccess) continue
            
            val functionType = firstChild.text
            if (!isSymbolType(functionType, PhelCompletionPriority.SPECIAL_FORMS)) continue

            // Check if the function name matches our symbol
            if ((secondChild is PhelSymbol || secondChild is PhelAccess) && secondChild.text == symbolText) {
                return true
            }
        }
        return false
    }

    private fun isLocalFunctionDefinition(symbol: PhelSymbol): Boolean {
        val parent = symbol.parent

        // Handle case where symbol is wrapped in PhelAccessImpl
        val listParent = if (parent is PhelAccess) {
            parent.parent as? PhelList
        } else {
            parent as? PhelList
        }

        listParent ?: return false
        
        val children = listParent.children
        if (children.size < 2) return false
        
        val firstChild = children[0]
        val secondChild = children[1]

        // Check if this is a function definition form
        if (firstChild !is PhelSymbol && firstChild !is PhelAccess) return false
        
        val functionType = firstChild.text
        if (!isSymbolType(functionType, PhelCompletionPriority.SPECIAL_FORMS)) return false

        // Check if the symbol is the function name (second child)
        // The symbol might be wrapped in PhelAccess, so check both direct and wrapped cases
        val isSecondChild = secondChild == symbol || (secondChild is PhelAccess && secondChild == parent)
        
        return isSecondChild
    }

    private fun findParameterVector(functionList: PhelList): PhelVec? {
        val children = functionList.children
        if (children.isEmpty()) return null

        val functionType = when (val firstChild = children[0]) {
            is PhelSymbol -> firstChild.text
            is PhelAccess -> firstChild.text
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

            // Check if this form IS our target vector
            if (form === targetVec) {
                return true
            }

            // Check if this form contains our target vector
            if (form === targetVec.parent) {
                return true
            }

            // If we encounter a vector that's not our target, it means our target
            // is not the parameter vector (it might be inside the function body)
            val foundVec = PsiTreeUtil.findChildOfType(form, PhelVec::class.java)
            if (foundVec != null && foundVec !== targetVec) {
                return false
            }
        }
        return false
    }

    private fun findContainingParameterVector(symbol: PhelSymbol): PhelVec? {
        var current = symbol.parent
        while (current != null) {
            if (current is PhelVec) {
                return current
            }
            current = current.parent
        }
        return null
    }

    private fun isParameterVectorInFunctionDefinition(paramVec: PhelVec): Boolean {
        // Check if this vector is a parameter vector in a function definition
        val containingList = paramVec.parent as? PhelList ?: return false
        val forms = containingList.children.filterIsInstance<PhelForm>()

        if (forms.size < 2) {
            return false
        }

        // Check if the first symbol is a function definition keyword
        val firstSymbol = when (val firstForm = forms[0]) {
            is PhelSymbol -> firstForm
            is PhelAccess -> {
                // Extract the symbol from PhelAccess
                firstForm.children.firstOrNull { it is PhelSymbol } as? PhelSymbol
            }

            else -> null
        }

        val firstText = firstSymbol?.text

        return when {
            isSymbolType(firstText, PhelCompletionPriority.SPECIAL_FORMS) -> {
                if (firstText == "fn") {
                    // For (fn [params] ...), parameter vector is at forms[1]
                    forms.size >= 2 && forms[1] === paramVec
                } else {
                    // For defn/defn-/defmacro/etc, find the first vector after the function name
                    findParameterVectorInDefn(forms.toTypedArray(), paramVec)
                }
            }

            else -> false
        }
    }
}
