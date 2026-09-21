package org.phellang.language.psi.analysis

import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import org.phellang.language.psi.PhelAccess
import org.phellang.language.psi.PhelForm
import org.phellang.language.psi.PhelList
import org.phellang.language.psi.PhelSymbol
import org.phellang.language.psi.PhelTypes
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

    /**
     * [form] with the reader's generic `form` wrapper peeled off: the node that decides what the
     * form *is*.
     *
     * A form carrying metadata or a reader macro (`^int x`, `'sym`) parses as a generic wrapper
     * around the real node; a bare vector, map, symbol or literal is that node already. Only the
     * wrapper is looked through, never a container: peeking into a vector's children to decide
     * what it is made `[{:id id}]` read as a metadata *map*, so a function whose first parameter
     * destructures a map had no parameter vector at all.
     */
    fun unwrapped(form: PsiElement): PsiElement {
        if (form.node?.elementType == PhelTypes.FORM) {
            form.children.firstOrNull { it is PhelForm }?.let { return unwrapped(it) }
        }

        return form
    }
}
