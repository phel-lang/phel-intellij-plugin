package org.phellang.language.psi.analysis

import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import org.phellang.language.psi.PhelAccess
import org.phellang.language.psi.PhelForm
import org.phellang.language.psi.PhelKeyword
import org.phellang.language.psi.PhelList
import org.phellang.language.psi.PhelMap
import org.phellang.language.psi.PhelSpecialForms
import org.phellang.language.psi.PhelSymbol
import org.phellang.language.psi.PhelVec
import org.phellang.language.psi.utils.PhelPsiUtils

/**
 * The names a binding *pattern* introduces: a bare symbol, a vector pattern, or a map pattern.
 *
 * Mirrors the compiler's `Deconstructor` family (`SymbolBindingDeconstructor`,
 * `VectorBindingDeconstructor`, `MapBindingDeconstructor`). A pattern sits in the name half of a
 * let-like binding vector or in a function's parameter vector, and may nest arbitrarily:
 *
 * ```
 * (let [[a b & rest] xs
 *       {n :name {:keys [city]} :address :or {n "?"} :as user} m] ...)
 * (defn f [[x y] {:strs [id]}] ...)
 * ```
 *
 * Map patterns are read per pair, as `MapBindingDeconstructor::resolveNormalPair` reads them
 * (phel-lang #3115). Only a symbol, a vector or a map can be bound to, so which side of a pair is
 * the pattern follows from which side is bindable:
 *
 * | Pair          | Left binds | Right binds | Read as                          |
 * |---------------|------------|-------------|----------------------------------|
 * | `{local :kw}` | yes        | no          | binding-first (Phel 0.51+)       |
 * | `{:kw local}` | no         | yes         | key-first (deprecated in 0.51)   |
 * | `{k v}`       | yes        | yes         | key-first, ambiguous: `k` is read |
 * | `{:a :b}`     | no         | no          | rejected by the compiler         |
 *
 * `:keys` / `:strs` / `:syms` take a vector of symbols, `:as` a symbol, and `:or` a map whose keys
 * *name* bindings introduced elsewhere in the pattern and whose values are expressions, so `:or`
 * binds nothing.
 *
 * Every consumer that once read "the even entries, as symbols" or "the first symbol found inside
 * the entry" reads through here instead, so a destructured name resolves, completes, highlights and
 * is reported unused or shadowed exactly like a plain one.
 */
internal object PhelDestructuringAnalyzer {

    /** How a map-pattern pair is read. */
    enum class PairOrder { BINDING_FIRST, KEY_FIRST, AMBIGUOUS, UNBINDABLE }

    /** One `key value` pair of a map pattern that is not a `:keys`/`:strs`/`:syms`/`:as`/`:or` directive. */
    data class MapPair(val left: PhelForm, val right: PhelForm, val order: PairOrder)

    /** A vector that introduces names, and whether it pairs them with values. */
    data class BindingVector(val vector: PhelVec, val isLetLike: Boolean) {
        /** The symbols the vector binds, in source order. */
        val boundSymbols: List<PhelSymbol>
            get() = if (isLetLike) letBoundSymbols(vector) else parameterSymbols(vector)
    }

    private val DIRECTIVES_TAKING_SYMBOLS = setOf(":keys", ":strs", ":syms")
    private const val AS_DIRECTIVE = ":as"
    private const val OR_DIRECTIVE = ":or"
    private const val REST_MARKER = "&"

    /** Every symbol [pattern] binds, nested patterns included, in source order. */
    fun boundSymbols(pattern: PsiElement): List<PhelSymbol> {
        val result = ArrayList<PhelSymbol>()
        collect(pattern, result)
        return result
    }

    /** The symbols a let-like `[pattern value …]` vector binds: the patterns are the even entries. */
    fun letBoundSymbols(bindingVector: PhelVec): List<PhelSymbol> =
        letPatterns(bindingVector).flatMap(::boundSymbols)

    /** The symbols a parameter vector binds: every entry is a pattern, and `&` marks the rest one. */
    fun parameterSymbols(parameterVector: PhelVec): List<PhelSymbol> =
        parameterPatterns(parameterVector).flatMap(::boundSymbols)

    /** The pattern entries of a let-like binding vector, counted over active forms so `#_` keeps the pairing. */
    fun letPatterns(bindingVector: PhelVec): List<PhelForm> {
        val entries = PhelPsiUtils.activeForms(bindingVector)
        return entries.filterIndexed { index, _ -> index % 2 == 0 }
    }

    /** The pattern entries of a parameter vector. */
    fun parameterPatterns(parameterVector: PhelVec): List<PhelForm> =
        PhelPsiUtils.activeForms(parameterVector).filterNot { isRestMarker(it) }

    /** Every map pattern within [pattern], itself included, outermost first. */
    fun mapPatterns(pattern: PsiElement): List<PhelMap> {
        val result = ArrayList<PhelMap>()
        collectMaps(pattern, result)
        return result
    }

    /** The non-directive pairs of a map pattern, each classified. A trailing key with no value is dropped. */
    fun pairsOf(map: PhelMap): List<MapPair> {
        val entries = PhelPsiUtils.activeForms(map)

        return (0 until entries.size - 1 step 2).mapNotNull { i ->
            val left = entries[i]
            val right = entries[i + 1]
            if (directiveOf(left) != null) return@mapNotNull null

            MapPair(left, right, orderOf(left, right))
        }
    }

    /**
     * The innermost vector enclosing [element] that introduces names, or null when [element] is not
     * inside one.
     *
     * A nested pattern is itself a vector, so the *nearest* `PhelVec` is the wrong one to ask: for
     * `x` in `(fn [[x y]] …)` it is `[x y]`, which declares nothing on its own. The walk climbs
     * until it reaches a vector that is a let-like form's binding vector or a function's parameter
     * vector, passing through ordinary body vectors on the way.
     */
    fun enclosingBindingVector(element: PsiElement): BindingVector? {
        var vector = PsiTreeUtil.getParentOfType(element, PhelVec::class.java)

        while (vector != null) {
            classifyVector(vector)?.let { return it }
            vector = PsiTreeUtil.getParentOfType(vector, PhelVec::class.java)
        }

        return null
    }

    /** True when [symbol] is one of the names [vector] introduces — a declaration, not a read. */
    fun declares(vector: BindingVector, symbol: PhelSymbol): Boolean = vector.boundSymbols.any { it === symbol }

    /** True when [form] can be bound to: a symbol, a vector or a map, per `Deconstructor::createDeconstructorForBinding`. */
    fun isBindingForm(form: PsiElement): Boolean {
        val node = patternNode(form)
        return node is PhelVec || node is PhelMap || symbolOf(node) != null
    }

    private fun classifyVector(vector: PhelVec): BindingVector? {
        val list = PsiTreeUtil.getParentOfType(vector, PhelList::class.java) ?: return null
        val forms = PhelPsiUtils.activeForms(list)
        val head = PhelPsiUtils.asSymbol(forms.firstOrNull())?.text

        if (head in PhelSpecialForms.LET_LIKE && PhelFormWalker.isSameOrWrapperOf(forms.getOrNull(1), vector)) {
            return BindingVector(vector, isLetLike = true)
        }
        if (PhelParameterAnalyzer.isParameterVector(vector)) {
            return BindingVector(vector, isLetLike = false)
        }

        return null
    }

    private fun collect(pattern: PsiElement, into: MutableList<PhelSymbol>) {
        when (val node = patternNode(pattern)) {
            is PhelVec -> PhelPsiUtils.activeForms(node)
                .filterNot { isRestMarker(it) }
                .forEach { collect(it, into) }

            is PhelMap -> collectFromMap(node, into)

            else -> symbolOf(node)?.takeUnless { it.text == REST_MARKER }?.let(into::add)
        }
    }

    private fun collectFromMap(map: PhelMap, into: MutableList<PhelSymbol>) {
        val entries = PhelPsiUtils.activeForms(map)

        for (i in 0 until entries.size - 1 step 2) {
            val left = entries[i]
            val right = entries[i + 1]

            when (directiveOf(left)) {
                in DIRECTIVES_TAKING_SYMBOLS -> symbolsListedIn(right).forEach(into::add)
                AS_DIRECTIVE -> symbolOf(patternNode(right))?.let(into::add)
                OR_DIRECTIVE -> Unit
                else -> when (orderOf(left, right)) {
                    PairOrder.BINDING_FIRST -> collect(left, into)
                    PairOrder.KEY_FIRST, PairOrder.AMBIGUOUS -> collect(right, into)
                    PairOrder.UNBINDABLE -> Unit
                }
            }
        }
    }

    private fun collectMaps(pattern: PsiElement, into: MutableList<PhelMap>) {
        when (val node = patternNode(pattern)) {
            is PhelVec -> PhelPsiUtils.activeForms(node).forEach { collectMaps(it, into) }

            is PhelMap -> {
                into.add(node)
                val entries = PhelPsiUtils.activeForms(node)

                for (i in 0 until entries.size - 1 step 2) {
                    val left = entries[i]
                    val right = entries[i + 1]
                    if (directiveOf(left) != null) continue

                    when (orderOf(left, right)) {
                        PairOrder.BINDING_FIRST -> collectMaps(left, into)
                        PairOrder.KEY_FIRST, PairOrder.AMBIGUOUS -> collectMaps(right, into)
                        PairOrder.UNBINDABLE -> Unit
                    }
                }
            }

            else -> Unit
        }
    }

    /** The order of one pair, per `MapBindingDeconstructor::resolveNormalPair`. */
    private fun orderOf(left: PhelForm, right: PhelForm): PairOrder {
        val leftBinds = isBindingForm(left)
        val rightBinds = isBindingForm(right)

        return when {
            leftBinds && !rightBinds -> PairOrder.BINDING_FIRST
            !leftBinds && rightBinds -> PairOrder.KEY_FIRST
            leftBinds -> PairOrder.AMBIGUOUS
            else -> PairOrder.UNBINDABLE
        }
    }

    /** `:keys`, `:strs`, `:syms`, `:as` or `:or` when [form] is that directive keyword, else null. */
    private fun directiveOf(form: PhelForm): String? {
        val keyword = patternNode(form) as? PhelKeyword ?: return null
        val text = keyword.text

        return text.takeIf { it in DIRECTIVES_TAKING_SYMBOLS || it == AS_DIRECTIVE || it == OR_DIRECTIVE }
    }

    /** The symbols of a `:keys [a b]`-style vector. Anything that is not a symbol is ignored, as the compiler ignores it. */
    private fun symbolsListedIn(form: PhelForm): List<PhelSymbol> {
        val vector = patternNode(form) as? PhelVec ?: return emptyList()

        return PhelPsiUtils.activeForms(vector).mapNotNull { symbolOf(patternNode(it)) }
    }

    private fun isRestMarker(form: PhelForm): Boolean = symbolOf(patternNode(form))?.text == REST_MARKER

    /**
     * The element that decides what [form] is, with the reader's generic `form` wrapper peeled off.
     *
     * Only the wrapper is looked through, never a container: a deep search would make `{:keys [a]}`
     * read as a vector because of what it happens to contain, which is exactly how
     * `PhelPsiUtils.asSymbol` misread patterns before.
     */
    private fun patternNode(form: PsiElement): PsiElement = PhelFormWalker.unwrapped(form)

    /** The symbol [node] is, whether bare or behind the reader's access wrapper; null for anything else. */
    private fun symbolOf(node: PsiElement): PhelSymbol? = when (node) {
        is PhelSymbol -> node
        is PhelAccess -> node.symbol
        else -> null
    }
}
