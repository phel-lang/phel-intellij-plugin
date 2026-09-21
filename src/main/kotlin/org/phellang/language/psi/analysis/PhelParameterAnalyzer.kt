package org.phellang.language.psi.analysis

import com.intellij.openapi.util.Key
import com.intellij.psi.util.CachedValue
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.PsiElement
import org.phellang.language.psi.PhelLiteral
import org.phellang.language.psi.PhelMap
import org.phellang.language.psi.PhelForm
import org.phellang.language.psi.PhelList
import org.phellang.language.psi.PhelSpecialForms
import org.phellang.language.psi.PhelSymbol
import org.phellang.language.psi.PhelVec
import org.phellang.language.psi.utils.cachedPerPsi

/** Everything about function parameters: locating the parameter vector, and naming what it binds. */
internal object PhelParameterAnalyzer {

    private val FUNCTION_PARAMS_KEY: Key<CachedValue<Set<String>>> = Key.create("phel.functionParameters")

    private val FUNCTION_DEFINING_FORMS = PhelSpecialForms.FUNCTION_DEFINING

    /**
     * True when [symbol] is declared in the parameter vector of a function-defining form, at any
     * depth of destructuring — `x` in `(fn [x] …)`, `(fn [[x y]] …)` and `(fn [{x :x}] …)` alike.
     *
     * `&` is a marker rather than a name, so it is never *bound*; it still counts as being in the
     * parameter vector unless [excludeSymbols] rules it out, which is how the variadic-marker
     * highlighting finds it.
     */
    fun isFunctionParameter(symbol: PhelSymbol, excludeSymbols: Set<String>): Boolean {
        val text = symbol.text ?: return false
        if (text in excludeSymbols) return false

        val bindingVector = PhelDestructuringAnalyzer.enclosingBindingVector(symbol) ?: return false
        if (bindingVector.isLetLike) return false

        return text == REST_MARKER || PhelDestructuringAnalyzer.declares(bindingVector, symbol)
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
                children.drop(2).firstOrNull { !isSignaturePrelude(it) }?.let(PhelFormWalker::vectorOf)

            else -> null
        }
    }

    /**
     * A docstring or a metadata map may sit between the name and the parameter vector; nothing else
     * may. Skipping them explicitly is what keeps a vector *inside* the metadata — Phel's standard
     * library writes `{:see-also ["update-in" "assoc"]}` on most of its definitions — from being
     * taken for the parameter vector, or from ending the search before the real one is reached.
     *
     * Only the form's own node is judged, through [PhelFormWalker.unwrapped]. Judging a form by
     * what it *contains* took `[{:id id}]`, a parameter vector whose first entry destructures a
     * map, for the metadata map, and the function lost its parameters.
     */
    private fun isSignaturePrelude(form: PsiElement): Boolean {
        val node = PhelFormWalker.unwrapped(form)
        return node is PhelLiteral || node is PhelMap
    }

    /** Names bound by every arity of [functionList]. Cached: highlighting asks once per symbol. */
    private fun parameterNamesOf(functionList: PhelList): Set<String> =
        cachedPerPsi(functionList, FUNCTION_PARAMS_KEY) { computeParameterNames(functionList) }

    private fun computeParameterNames(functionList: PhelList): Set<String> =
        parameterVectorsOf(functionList).flatMapTo(mutableSetOf()) { namesIn(it) }

    /**
     * Every parameter vector [functionList] declares: the single-arity one sitting directly in the
     * list, and the head vector of each `([params] body)` arity list of a multi-arity form.
     */
    fun parameterVectorsOf(functionList: PhelList): List<PhelVec> {
        val vectors = ArrayList<PhelVec>()

        findParameterVector(functionList)?.let(vectors::add)

        // An arity list's *head* must itself be the vector: a deep search would also claim the
        // parameter vector of a call like `((fn [x] x) m)` sitting in the body.
        functionList.children
            .mapNotNull { PhelFormWalker.unwrapped(it) as? PhelList }
            .mapNotNull { arity -> arity.children.firstOrNull()?.let { PhelFormWalker.unwrapped(it) as? PhelVec } }
            .forEach(vectors::add)

        return vectors
    }

    /** Every name the vector binds, destructured ones included; `&` marks a rest parameter rather than naming one. */
    private fun namesIn(paramVec: PhelVec): List<String> =
        PhelDestructuringAnalyzer.parameterSymbols(paramVec).mapNotNull { it.text }.filter { it.isNotEmpty() }

    private const val REST_MARKER = "&"

    private fun enclosingFunction(symbol: PhelSymbol): PhelList? = PhelFormWalker.enclosingLists(symbol)
        .firstOrNull { PhelFormWalker.headText(it) in FUNCTION_DEFINING_FORMS }

    /**
     * True when [paramVec] is a function's parameter vector rather than an ordinary vector in its
     * body. Two shapes qualify: the vector of a single-arity form, and the head of an arity list
     * inside a multi-arity form — `(defn name ([] body) ([x] body))`.
     */
    fun isParameterVector(paramVec: PhelVec): Boolean {
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
     * The parameter vector of a `defn` is the first form after the name that is not a docstring or a
     * metadata map. Anything else standing there means [targetVec] lives in the body.
     *
     * This used to bail on meeting *any* vector among the descendants of an earlier form, so the
     * `["update-in" "assoc"]` inside a `{:see-also ...}` map ended the search and the parameters of
     * every documented `defn` in the standard library went unrecognised — unhighlighted, unresolved
     * and unrenameable.
     */
    private fun isDefnParameterVector(forms: Array<PhelForm>, targetVec: PhelVec): Boolean {
        for (form in forms.drop(2)) {
            if (PhelFormWalker.isSameOrWrapperOf(form, targetVec)) return true
            if (isSignaturePrelude(form)) continue

            return false
        }

        return false
    }
}
