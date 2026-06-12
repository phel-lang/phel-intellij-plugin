package org.phellang.core.psi

import com.intellij.openapi.util.Key
import com.intellij.psi.PsiElement
import com.intellij.psi.util.CachedValue
import com.intellij.psi.util.CachedValueProvider
import com.intellij.psi.util.CachedValuesManager
import com.intellij.psi.util.PsiModificationTracker
import com.intellij.psi.util.PsiTreeUtil
import org.phellang.completion.data.PhelFunctionRegistry
import org.phellang.completion.infrastructure.PhelCompletionPriority
import org.phellang.core.utils.PhelErrorHandler
import org.phellang.language.psi.*
import org.phellang.language.psi.files.PhelFile
import org.phellang.language.psi.utils.SymbolCategory

object PhelSymbolAnalyzer {

    private val LOCAL_FUNCTION_NAMES_KEY: Key<CachedValue<Set<String>>> =
        Key.create("phel.localFunctionNames")

    /** Forms that introduce a parameter vector — used by isFunctionParameter. */
    private val FUNCTION_DEFINING_FORMS = PhelSpecialForms.FUNCTION_DEFINING

    /** Forms that introduce a binding vector — used by isLetBinding. */
    private val LET_LIKE_FORMS = PhelSpecialForms.LET_LIKE

    /** Top-level forms that bind a name in their second position (the symbol being defined). */
    private val DEFINITION_FORMS = setOf(
        "def", "def-", "defn", "defn-", "defmacro", "defmacro-",
        "defstruct", "definterface", "defexception", "declare", "deftest",
    )

    @JvmStatic
    fun isSymbolType(symbolText: String?, category: SymbolCategory): Boolean {
        if (symbolText == null) return false

        val priority = when (category) {
            SymbolCategory.SPECIAL_FORMS -> PhelCompletionPriority.SPECIAL_FORMS
            SymbolCategory.CONTROL_FLOW -> PhelCompletionPriority.CONTROL_FLOW
            SymbolCategory.MACROS -> PhelCompletionPriority.MACROS
            SymbolCategory.CORE_FUNCTIONS -> PhelCompletionPriority.CORE_FUNCTIONS
            SymbolCategory.COLLECTION_FUNCTIONS -> PhelCompletionPriority.COLLECTION_FUNCTIONS
        }
        return PhelFunctionRegistry.hasFunctionWithName(priority, symbolText)
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
            if (firstSymbolText !in DEFINITION_FORMS) {
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
            val firstSymbol = (forms[0] as? PhelSymbol)
                ?: PsiTreeUtil.findChildOfType(forms[0], PhelSymbol::class.java)
                ?: return@safeOperation false
            if (firstSymbol.text !in LET_LIKE_FORMS) return@safeOperation false

            // Check if binding vector is at forms[1] — either directly or via a PhelForm wrapper.
            if (forms[1] !== bindingVec && forms[1] !== bindingVec.parent) return@safeOperation false

            // Check if symbol is in an even position (binding symbols are at even indices)
            val bindings = PsiTreeUtil.getChildrenOfType(bindingVec, PhelForm::class.java) ?: return@safeOperation false

            var i = 0
            while (i < bindings.size) {
                val bindingSymbol = (bindings[i] as? PhelSymbol)
                    ?: PsiTreeUtil.findChildOfType(bindings[i], PhelSymbol::class.java)
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

            // A binding/parameter *definition* is not a reference to one.
            if (isFunctionParameter(symbol) || isLetBinding(symbol)) {
                return@safeOperation false
            }

            isLocalReferenceOnly(symbol, symbolText)
        } ?: false
    }

    /**
     * True when [symbol] is a fn/let binding definition **or** a reference to one — i.e. any
     * occurrence highlighting paints as a local symbol. Computes each underlying check once;
     * prefer this over calling `isParameterReference`, `isFunctionParameter` and `isLetBinding`
     * separately, which re-walks the PSI for the two definition checks.
     */
    @JvmStatic
    fun isLocalBindingOrReference(symbol: PhelSymbol): Boolean {
        return PhelErrorHandler.safeOperation {
            val symbolText = symbol.text ?: return@safeOperation false
            if (isFunctionParameter(symbol) || isLetBinding(symbol)) {
                return@safeOperation true
            }
            isLocalReferenceOnly(symbol, symbolText)
        } ?: false
    }

    /** Reference-only lookup: assumes the caller already ruled out a binding/param definition. */
    private fun isLocalReferenceOnly(symbol: PhelSymbol, symbolText: String): Boolean {
        // Look for function parameters in the containing function
        val containingFunction = findContainingFunction(symbol)
        if (containingFunction != null) {
            val parameters = extractFunctionParameters(containingFunction)
            if (parameters.contains(symbolText)) {
                return true
            }
        }

        // Look for let bindings in the containing scopes
        if (isReferenceToLetBinding(symbol, symbolText)) {
            return true
        }

        // Look for references to locally defined functions in the same file
        return isReferenceToLocalFunction(symbol, symbolText)
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
            if (functionType in FUNCTION_DEFINING_FORMS) {
                return current
            }

            current = current.parent
        }
        return null
    }

    private fun extractFunctionParameters(functionList: PhelList): Set<String> {
        val parameters = mutableSetOf<String>()

        // Single-arity: paramVec lives directly in the function list.
        findParameterVector(functionList)?.let { collectParameterNames(it, parameters) }

        // Multi-arity: every child arity is a list `([params] body)` whose head is the param vec.
        for (child in functionList.children) {
            val arityList = (child as? PhelList)
                ?: PsiTreeUtil.findChildOfType(child, PhelList::class.java)
                ?: continue
            val arityChildren = arityList.children
            if (arityChildren.isEmpty()) continue
            val arityHeadVec = (arityChildren[0] as? PhelVec)
                ?: PsiTreeUtil.findChildOfType(arityChildren[0], PhelVec::class.java)
                ?: continue
            collectParameterNames(arityHeadVec, parameters)
        }

        return parameters
    }

    private fun collectParameterNames(paramVec: PhelVec, into: MutableSet<String>) {
        for (paramChild in paramVec.children) {
            if (paramChild !is PhelSymbol && paramChild !is PhelAccess) continue
            val paramName = paramChild.text
            if (paramName.isNullOrEmpty() || paramName == "&") continue
            into.add(paramName)
        }
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
            if (bindingType !in LET_LIKE_FORMS) {
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
        return symbolText in localFunctionNames(file)
    }

    /**
     * Names of top-level `fn`/`defn`-family definitions in [file]. Highlighting probes this
     * once per regular symbol, so the per-file scan is cached and invalidated by edits rather
     * than re-walking every top-level form for every symbol.
     */
    private fun localFunctionNames(file: PhelFile): Set<String> {
        return CachedValuesManager.getCachedValue(file, LOCAL_FUNCTION_NAMES_KEY) {
            CachedValueProvider.Result.create(
                computeLocalFunctionNames(file),
                PsiModificationTracker.MODIFICATION_COUNT,
            )
        }
    }

    private fun computeLocalFunctionNames(file: PhelFile): Set<String> {
        val names = HashSet<String>()
        for (child in file.children) {
            if (child !is PhelList) continue

            val children = child.children
            if (children.size < 2) continue

            val firstChild = children[0]
            if (firstChild !is PhelSymbol && firstChild !is PhelAccess) continue
            if (firstChild.text !in FUNCTION_DEFINING_FORMS) continue

            val secondChild = children[1]
            if (secondChild is PhelSymbol || secondChild is PhelAccess) {
                secondChild.text?.let { names.add(it) }
            }
        }
        return names
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
        if (functionType !in DEFINITION_FORMS) return false

        // Check if the symbol is the function name (second child)
        // The symbol might be wrapped in PhelAccess, so check both direct and wrapped cases
        val isSecondChild = secondChild == symbol || (secondChild is PhelAccess && secondChild == parent)
        
        return isSecondChild
    }

    @JvmStatic
    fun findParameterVector(functionList: PhelList): PhelVec? {
        val children = functionList.children
        if (children.isEmpty()) return null

        val functionType = symbolTextOf(children[0]) ?: return null

        return when (functionType) {
            "fn" -> {
                // For (fn [params] ...), parameter vector is at index 1
                if (children.size >= 2) vecOf(children[1]) else null
            }

            "defn", "defn-", "defmacro", "defmacro-" -> {
                // For defn forms, find the first vector after the function name
                // Skip docstring and metadata: (defn name "doc" {:meta} [params] ...)
                for (i in 2 until children.size) {
                    val vec = vecOf(children[i])
                    if (vec != null) return vec
                }
                null
            }

            else -> null
        }
    }

    /** Returns the symbol text of [child], whether [child] is a PhelSymbol/PhelAccess directly
     *  or a PhelForm wrapping one. */
    private fun symbolTextOf(child: PsiElement): String? = when (child) {
        is PhelSymbol -> child.text
        is PhelAccess -> child.text
        else -> PsiTreeUtil.findChildOfType(child, PhelSymbol::class.java)?.text
    }

    /** Returns the PhelVec at [child], whether [child] IS a PhelVec or wraps one in a PhelForm. */
    private fun vecOf(child: PsiElement): PhelVec? = when (child) {
        is PhelVec -> child
        else -> PsiTreeUtil.findChildOfType(child, PhelVec::class.java)
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
        val immediate = PsiTreeUtil.getParentOfType(paramVec, PhelList::class.java) ?: return false
        val immediateForms = PsiTreeUtil.getChildrenOfType(immediate, PhelForm::class.java) ?: return false
        val immediateHead = headSymbolText(immediateForms)

        // Direct case: paramVec lives in the function-defining list itself.
        //   (fn [params] body)            → paramVec at forms[1]
        //   (defn name [params] body)     → first vec after the name (skip doc/meta)
        //   (defmacro name [params] body) → ditto
        if (immediateHead in FUNCTION_DEFINING_FORMS) {
            return when (immediateHead) {
                "fn" -> immediateForms.size >= 2 && isSameOrWrapperOf(immediateForms[1], paramVec)
                else -> findParameterVectorInDefn(immediateForms, paramVec)
            }
        }

        // Multi-arity case: paramVec is the head of an arity list, and the arity list's
        // parent is a function-defining form.
        //   (defn name ([] body) ([x] body))
        //   (fn   ([] body) ([x] body))
        if (immediateForms.isNotEmpty() && isSameOrWrapperOf(immediateForms[0], paramVec)) {
            val outer = PsiTreeUtil.getParentOfType(immediate, PhelList::class.java) ?: return false
            val outerForms = PsiTreeUtil.getChildrenOfType(outer, PhelForm::class.java) ?: return false
            return headSymbolText(outerForms) in FUNCTION_DEFINING_FORMS
        }

        return false
    }

    /** First-position symbol text for a list's child forms, tolerant of flat/wrapped layouts. */
    private fun headSymbolText(forms: Array<PhelForm>): String? {
        if (forms.isEmpty()) return null
        return (forms[0] as? PhelSymbol)?.text
            ?: (forms[0] as? PhelAccess)?.text
            ?: PsiTreeUtil.findChildOfType(forms[0], PhelSymbol::class.java)?.text
    }

    /** True when [candidate] either IS [target] or is the PhelForm wrapper around it. */
    private fun isSameOrWrapperOf(candidate: PsiElement?, target: PhelVec): Boolean =
        candidate === target || candidate === target.parent
}
