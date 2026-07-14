package org.phellang.core.psi

import com.intellij.openapi.util.Key
import com.intellij.psi.util.CachedValue
import com.intellij.psi.util.CachedValueProvider
import com.intellij.psi.util.CachedValuesManager
import com.intellij.psi.util.PsiModificationTracker
import com.intellij.psi.util.PsiTreeUtil
import org.phellang.language.psi.PhelForm
import org.phellang.language.psi.PhelList
import org.phellang.language.psi.PhelSpecialForms
import org.phellang.language.psi.PhelSymbol
import org.phellang.language.psi.PhelVec

/** Everything about function parameters: locating the parameter vector, and naming what it binds. */
internal object PhelParameterAnalyzer {

    private val FUNCTION_PARAMS_KEY: Key<CachedValue<Set<String>>> = Key.create("phel.functionParameters")

    private val FUNCTION_DEFINING_FORMS = PhelSpecialForms.FUNCTION_DEFINING

    /** True when [symbol] is declared in the parameter vector of a function-defining form. */
    fun isFunctionParameter(symbol: PhelSymbol, excludeSymbols: Set<String>): Boolean {
        val paramVec = enclosingVector(symbol) ?: return false
        if (!isParameterVector(paramVec)) return false

        return symbol.text !in excludeSymbols
    }

    /** The parameter names visible to [symbol] from its enclosing function, empty when outside one. */
    fun parametersInScopeOf(symbol: PhelSymbol): Set<String> {
        val function = enclosingFunction(symbol) ?: return emptySet()
        return parameterNamesOf(function)
    }

    /**
     * The parameter vector a function-defining form declares.
     *
     * `fn` carries it at index 1; `defn` and friends carry the first vector after the name, past any
     * docstring or metadata — `(defn name "doc" {:meta} [params] …)`.
     */
    @JvmStatic
    fun findParameterVector(functionList: PhelList): PhelVec? {
        val children = functionList.children
        val functionType = children.firstOrNull()?.let(PhelFormWalker::symbolTextOf) ?: return null

        return when (functionType) {
            "fn" -> children.getOrNull(1)?.let(PhelFormWalker::vectorOf)
            "defn", "defn-", "defmacro", "defmacro-" ->
                children.drop(2).firstNotNullOfOrNull(PhelFormWalker::vectorOf)

            else -> null
        }
    }

    /** Names bound by every arity of [functionList]. Cached: highlighting asks once per symbol. */
    private fun parameterNamesOf(functionList: PhelList): Set<String> =
        CachedValuesManager.getCachedValue(functionList, FUNCTION_PARAMS_KEY) {
            CachedValueProvider.Result.create(
                computeParameterNames(functionList),
                PsiModificationTracker.MODIFICATION_COUNT,
            )
        }

    private fun computeParameterNames(functionList: PhelList): Set<String> {
        val names = mutableSetOf<String>()

        // Single arity: the vector sits directly in the function list.
        findParameterVector(functionList)?.let { names += namesIn(it) }

        // Multi-arity: each child is an arity list `([params] body)` whose head is the vector.
        functionList.children
            .mapNotNull { it as? PhelList ?: PsiTreeUtil.findChildOfType(it, PhelList::class.java) }
            .mapNotNull { arity -> arity.children.firstOrNull()?.let(PhelFormWalker::vectorOf) }
            .forEach { names += namesIn(it) }

        return names
    }

    /** `&` marks a rest parameter rather than naming one, so it is never a binding. */
    private fun namesIn(paramVec: PhelVec): List<String> = paramVec.children
        .filter(PhelFormWalker::isSymbolLike)
        .mapNotNull { it.text }
        .filter { it.isNotEmpty() && it != "&" }

    fun enclosingVector(symbol: PhelSymbol): PhelVec? =
        PsiTreeUtil.getParentOfType(symbol, PhelVec::class.java)

    private fun enclosingFunction(symbol: PhelSymbol): PhelList? = PhelFormWalker.enclosingLists(symbol)
        .firstOrNull { PhelFormWalker.headText(it) in FUNCTION_DEFINING_FORMS }

    /**
     * True when [paramVec] is a function's parameter vector rather than an ordinary vector in its
     * body. Two shapes qualify: the vector of a single-arity form, and the head of an arity list
     * inside a multi-arity form — `(defn name ([] body) ([x] body))`.
     */
    private fun isParameterVector(paramVec: PhelVec): Boolean {
        val immediate = PsiTreeUtil.getParentOfType(paramVec, PhelList::class.java) ?: return false
        val immediateForms = PsiTreeUtil.getChildrenOfType(immediate, PhelForm::class.java) ?: return false
        val head = immediateForms.firstOrNull()?.let(PhelFormWalker::symbolTextOf)

        if (head in FUNCTION_DEFINING_FORMS) {
            return when (head) {
                "fn" -> PhelFormWalker.isSameOrWrapperOf(immediateForms.getOrNull(1), paramVec)
                else -> isDefnParameterVector(immediateForms, paramVec)
            }
        }

        // Multi-arity: paramVec heads an arity list whose parent is the function-defining form.
        if (PhelFormWalker.isSameOrWrapperOf(immediateForms.firstOrNull(), paramVec)) {
            val outer = PsiTreeUtil.getParentOfType(immediate, PhelList::class.java) ?: return false
            val outerForms = PsiTreeUtil.getChildrenOfType(outer, PhelForm::class.java) ?: return false
            return outerForms.firstOrNull()?.let(PhelFormWalker::symbolTextOf) in FUNCTION_DEFINING_FORMS
        }

        return false
    }

    /**
     * The parameter vector of a `defn` is the *first* vector after the name. Meeting a different
     * vector first means [targetVec] lives in the body, not the signature.
     */
    private fun isDefnParameterVector(forms: Array<PhelForm>, targetVec: PhelVec): Boolean {
        for (form in forms.drop(2)) {
            if (PhelFormWalker.isSameOrWrapperOf(form, targetVec)) return true

            val other = PsiTreeUtil.findChildOfType(form, PhelVec::class.java)
            if (other != null && other !== targetVec) return false
        }
        return false
    }
}
