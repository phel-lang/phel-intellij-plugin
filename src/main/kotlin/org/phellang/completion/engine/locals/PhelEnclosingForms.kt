package org.phellang.completion.engine.locals

import com.intellij.psi.PsiElement
import org.phellang.language.psi.PhelAccess
import org.phellang.language.psi.PhelList
import org.phellang.language.psi.PhelSymbol

/** An enclosing list form paired with the text of its head symbol, e.g. `let` in `(let [x 1] ...)`. */
internal data class PhelEnclosingForm(val list: PhelList, val head: String)

/**
 * Walks the list forms enclosing a completion position, innermost first.
 *
 * Both the parameter and the binding collectors need this walk but consume it differently: a
 * parameter list is found once and the search stops, whereas nested `let` forms all contribute, so
 * the walk is a lazy sequence and the caller decides when to stop.
 */
internal object PhelEnclosingForms {

    /** Matches the depth cap the collectors have always used: deep enough for real code, bounded. */
    private const val MAX_DEPTH = 10

    fun from(position: PsiElement): Sequence<PhelEnclosingForm> = sequence {
        var current: PsiElement? = position.parent
        var depth = 0

        while (current != null && depth < MAX_DEPTH) {
            headedList(current)?.let { yield(it) }
            current = current.parent
            depth++
        }
    }

    /** A list whose first child is a symbol; anything else heads no form we can name. */
    private fun headedList(element: PsiElement): PhelEnclosingForm? {
        val list = element as? PhelList ?: return null
        val head = list.children.firstOrNull() ?: return null
        if (head !is PhelSymbol && head !is PhelAccess) return null

        return PhelEnclosingForm(list, head.text)
    }
}
