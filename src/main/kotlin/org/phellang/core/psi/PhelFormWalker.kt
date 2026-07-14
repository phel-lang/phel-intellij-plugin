package org.phellang.core.psi

import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import org.phellang.language.psi.PhelAccess
import org.phellang.language.psi.PhelList
import org.phellang.language.psi.PhelSymbol
import org.phellang.language.psi.PhelVec

/**
 * Walking the enclosing forms of a symbol, and reading a form's head.
 *
 * Both are fiddly because a head or a parameter may be a bare [PhelSymbol] or the same symbol
 * wrapped in a [PhelAccess] (the reader's interop-access form), and because `.children` on a list
 * mixes forms with wrappers. Every analyzer needs this, and each used to re-derive it.
 */
internal object PhelFormWalker {

    /** The [PhelList] ancestors of [element], innermost first. */
    fun enclosingLists(element: PsiElement): Sequence<PhelList> = generateSequence(element.parent) { it.parent }
        .filterIsInstance<PhelList>()

    /** The text of a list's first element — `defn` for `(defn foo [])` — or null when it has no head symbol. */
    fun headText(list: PhelList): String? = list.children.firstOrNull()?.let(::symbolTextOf)

    /** The symbol text of [element], whether it is a symbol, an access wrapper, or a form around one. */
    fun symbolTextOf(element: PsiElement): String? = when (element) {
        is PhelSymbol -> element.text
        is PhelAccess -> element.text
        else -> PsiTreeUtil.findChildOfType(element, PhelSymbol::class.java)?.text
    }

    /** True when [element] can name a symbol — i.e. it is a symbol or the access wrapper around one. */
    fun isSymbolLike(element: PsiElement): Boolean = element is PhelSymbol || element is PhelAccess

    /** The vector at [element], whether it IS one or wraps one. */
    fun vectorOf(element: PsiElement): PhelVec? = when (element) {
        is PhelVec -> element
        else -> PsiTreeUtil.findChildOfType(element, PhelVec::class.java)
    }

    /** True when [candidate] either IS [target] or is the form wrapper around it. */
    fun isSameOrWrapperOf(candidate: PsiElement?, target: PhelVec): Boolean =
        candidate === target || candidate === target.parent
}
