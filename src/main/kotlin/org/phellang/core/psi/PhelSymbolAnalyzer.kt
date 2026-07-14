package org.phellang.core.psi

import com.intellij.psi.util.PsiTreeUtil
import org.phellang.registry.PhelCompletionPriority
import org.phellang.registry.PhelFunctionRegistry
import org.phellang.language.psi.PhelForm
import org.phellang.language.psi.PhelList
import org.phellang.language.psi.PhelSymbol
import org.phellang.language.psi.PhelVec
import org.phellang.language.psi.utils.SymbolCategory

/**
 * What a Phel symbol *is* — a special form, a definition, a parameter, a binding, or a reference to
 * one. The questions highlighting, completion and resolution all ask.
 *
 * The analysis itself lives in focused collaborators; this is the API they are reached through:
 *
 * * [PhelParameterAnalyzer]   — parameter vectors and the names they bind
 * * [PhelLetBindingAnalyzer]  — `let`-like binding vectors
 * * [PhelLocalFunctionIndex]  — functions defined in the current file
 * * [PhelFormWalker]          — walking enclosing forms and reading their heads
 */
object PhelSymbolAnalyzer {

    /** Top-level forms that bind a name in their second position (the symbol being defined). */
    private val DEFINITION_FORMS = setOf(
        "def", "def-", "defn", "defn-", "defmacro", "defmacro-",
        "defstruct", "definterface", "defexception", "declare", "deftest",
    )

    /** True when [symbolText] belongs to [category] according to the generated function registry. */
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

    /** True when [symbol] declares a name: a parameter, a `let` binding, or a top-level definition. */
    @JvmStatic
    fun isDefinition(symbol: PhelSymbol): Boolean {
        if (isFunctionParameter(symbol) || isLetBinding(symbol)) return true

        return isDefinedName(symbol)
    }

    /** True when [symbol] is the name in `(def <symbol> …)` / `(defn <symbol> …)`. */
    private fun isDefinedName(symbol: PhelSymbol): Boolean {
        val containingList = PsiTreeUtil.getParentOfType(symbol, PhelList::class.java) ?: return false

        val firstForm = PsiTreeUtil.findChildOfType(containingList, PhelForm::class.java) ?: return false
        val head = PsiTreeUtil.findChildOfType(firstForm, PhelSymbol::class.java) ?: return false
        if (head.text !in DEFINITION_FORMS) return false

        val forms = PsiTreeUtil.getChildrenOfType(containingList, PhelForm::class.java) ?: return false
        if (forms.size <= 1) return false

        val definedName = PsiTreeUtil.findChildOfType(forms[1], PhelSymbol::class.java) ?: return false
        return symbol === definedName
    }

    @JvmStatic
    fun isInParameterVector(symbol: PhelSymbol): Boolean =
        PhelParameterAnalyzer.isFunctionParameter(symbol, excludeSymbols = emptySet())

    /** `&` introduces a rest parameter rather than naming one, so it is excluded by default. */
    @JvmStatic
    fun isFunctionParameter(symbol: PhelSymbol, excludeSymbols: Set<String> = setOf("&")): Boolean =
        PhelParameterAnalyzer.isFunctionParameter(symbol, excludeSymbols)

    @JvmStatic
    fun isLetBinding(symbol: PhelSymbol): Boolean = PhelLetBindingAnalyzer.isLetBinding(symbol)

    @JvmStatic
    fun findParameterVector(functionList: PhelList): PhelVec? =
        PhelParameterAnalyzer.findParameterVector(functionList)

    /** True when [symbol] *uses* a local binding — a binding's own declaration is not a reference. */
    @JvmStatic
    fun isParameterReference(symbol: PhelSymbol): Boolean {
        val symbolText = symbol.text ?: return false
        if (isFunctionParameter(symbol) || isLetBinding(symbol)) return false

        return isLocalReference(symbol, symbolText)
    }

    /**
     * True when [symbol] is a local binding *or* a reference to one — i.e. anything occurrence
     * highlighting paints as local.
     *
     * Prefer this over calling [isParameterReference], [isFunctionParameter] and [isLetBinding]
     * separately: it computes each underlying check once instead of re-walking the PSI for them.
     */
    @JvmStatic
    fun isLocalBindingOrReference(symbol: PhelSymbol): Boolean {
        val symbolText = symbol.text ?: return false
        if (isFunctionParameter(symbol) || isLetBinding(symbol)) return true

        return isLocalReference(symbol, symbolText)
    }

    /** Assumes the caller has already ruled out [symbol] being a binding's own declaration. */
    private fun isLocalReference(symbol: PhelSymbol, symbolText: String): Boolean {
        if (symbolText in PhelParameterAnalyzer.parametersInScopeOf(symbol)) return true
        if (PhelLetBindingAnalyzer.isReferenceToLetBinding(symbol, symbolText)) return true

        return PhelLocalFunctionIndex.isReferenceToLocalFunction(symbol, symbolText, DEFINITION_FORMS)
    }
}
