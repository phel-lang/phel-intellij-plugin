package org.phellang.language.psi.analysis

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.util.PsiTreeUtil
import org.phellang.language.psi.PhelForm
import org.phellang.language.psi.PhelHashFn
import org.phellang.language.psi.PhelList
import org.phellang.language.psi.PhelSymbol
import org.phellang.language.psi.utils.PhelPsiUtils

/**
 * The `|(...)` short function, which Phel 0.50 removed in favor of `#(...)` (phel-lang #2827).
 *
 * The grammar never had it: `|` lexes as a symbol and the list after it as a form of its own, which
 * is how Phel 0.50 and later read it too. So it is found by shape, a `|` symbol with a list right
 * after it, no space between, as the old reader's `|(` token was. Its placeholders were `$` (the
 * first argument), `$1`, `$2`, ... and `$&` (the rest), which `#(...)` spells with `%`.
 */
object PhelPipeShortFn {

    private const val MARKER = "|"
    private val PLACEHOLDER = Regex("""\$(&|[1-9]\d*)?""")

    /** The `(...)` of the `|(...)` that [marker] opens, or null when [marker] opens none. */
    fun bodyOf(marker: PhelSymbol): PhelList? {
        if (marker.text != MARKER) return null
        val next = outermostForm(marker).nextSibling ?: return null

        return PhelFormWalker.unwrapped(next) as? PhelList
    }

    fun isMarker(symbol: PhelSymbol): Boolean = bodyOf(symbol) != null

    /**
     * [forms] without the `|` of each `|(...)` among them, so the short function counts as the one
     * argument it was written as: `(f a |(g $))` passes two, not three.
     */
    fun withoutMarkers(forms: List<PhelForm>): List<PhelForm> =
        forms.filterNot { form -> PhelPsiUtils.asSymbol(form)?.let(::isMarker) == true }

    /** The `$`, `$1` and `$&` placeholders of [body]'s own short function, not of one nested in it. */
    fun placeholdersOf(body: PhelList): List<PhelSymbol> =
        PsiTreeUtil.findChildrenOfType(body, PhelSymbol::class.java)
            .filter { PLACEHOLDER.matches(it.text.orEmpty()) && enclosingShortFnBody(it) === body }

    /** True when [symbol] is a `$` placeholder of the `|(...)` it sits in. */
    fun isPlaceholder(symbol: PhelSymbol): Boolean =
        PLACEHOLDER.matches(symbol.text.orEmpty()) && enclosingShortFnBody(symbol) != null

    /**
     * The body of the innermost `|(...)` around [element], or null when the innermost function
     * literal is a `#(...)`, whose own placeholders are `%`, or there is none.
     */
    private fun enclosingShortFnBody(element: PsiElement): PhelList? {
        var current = element.parent
        while (current != null && current !is PsiFile) {
            if (current is PhelHashFn) return null
            if (current is PhelList && isBody(current)) return current
            current = current.parent
        }
        return null
    }

    private fun isBody(list: PhelList): Boolean {
        val previous = outermostForm(list).prevSibling ?: return false
        val marker = previous as? PhelSymbol ?: PsiTreeUtil.findChildOfType(previous, PhelSymbol::class.java) ?: return false

        return marker.text == MARKER && previous.textLength == MARKER.length
    }

    /** [element] with any wrapper of the same extent around it (an access form, a form node). */
    private fun outermostForm(element: PsiElement): PsiElement {
        var form = element
        while (true) {
            val parent = form.parent ?: return form
            if (parent is PsiFile || parent.textRange != form.textRange) return form
            form = parent
        }
    }
}
