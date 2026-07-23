package org.phellang.language.psi.utils

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.util.PsiTreeUtil
import org.phellang.language.psi.*

object PhelPsiUtils {
    @JvmStatic
    fun findTopmostSymbol(element: PsiElement?): PhelSymbol? {
        if (element == null) return null

        var symbol: PhelSymbol? = when (element) {
            is PhelSymbol -> element
            else -> PsiTreeUtil.getParentOfType(element, PhelSymbol::class.java)
        }

        while (symbol != null) {
            val parentSymbol = PsiTreeUtil.getParentOfType(symbol, PhelSymbol::class.java) ?: break
            symbol = parentSymbol
        }

        return symbol
    }

    /**
     * Resolves a form to its [PhelSymbol]. Simple symbols frequently parse as a [PhelAccess]
     * wrapper — e.g. the `let` head of a list or a `[name ...]` binding entry — so a plain
     * `as? PhelSymbol` cast misses them. Unwrap via the access's symbol or the first child symbol.
     */
    @JvmStatic
    fun asSymbol(form: PsiElement?): PhelSymbol? {
        return when (form) {
            null -> null
            is PhelSymbol -> form
            is PhelAccess -> form.symbol ?: PsiTreeUtil.findChildOfType(form, PhelSymbol::class.java)
            else -> PsiTreeUtil.findChildOfType(form, PhelSymbol::class.java)
        }
    }

    /**
     * Resolves a form to its [PhelKeyword] — the keyword analog of [asSymbol]. Clause keywords
     * like `:require` may sit inside a wrapper form, so a plain `as? PhelKeyword` cast misses
     * them; fall back to the first child keyword.
     */
    @JvmStatic
    fun asKeyword(form: PsiElement?): PhelKeyword? {
        return when (form) {
            null -> null
            is PhelKeyword -> form
            else -> PsiTreeUtil.findChildOfType(form, PhelKeyword::class.java)
        }
    }

    /**
     * The forms of [container] with `#_`-discarded forms removed. A `#_` (FORM_COMMENT) leaf
     * discards the next form, and they stack (`#_#_` drops two), so callers that count
     * call arguments must not treat a discarded form as a real argument. Without this,
     * `(push #_skip coll x)` reads as three args instead of two.
     *
     * [container] is any form container — a list, vector, map, or the file itself for a
     * top-level `#_`.
     */
    @JvmStatic
    fun activeForms(container: PsiElement): List<PhelForm> {
        val result = mutableListOf<PhelForm>()
        forEachChildForm(container) { form, discarded -> if (!discarded) result.add(form) }
        return result
    }

    /**
     * True when [element] is inside a form discarded by `#_`. Walks the ancestor chain, so a
     * symbol nested anywhere inside a discarded compound form (`#_(alpha beta)`) is reported
     * too, not just the discarded form itself.
     *
     * This is the same language rule as [activeForms] over the same PSI walk — the `#_` is a
     * leaf sibling of the form it discards, never its parent.
     */
    @JvmStatic
    fun isDiscardedByFormComment(element: PsiElement): Boolean {
        var current: PsiElement? = element
        while (current != null && current !is PsiFile) {
            if (current is PhelForm && isDiscarded(current)) return true
            current = current.parent
        }
        return false
    }

    private fun isDiscarded(form: PhelForm): Boolean {
        val parent = form.parent ?: return false
        var result = false
        forEachChildForm(parent) { child, discarded -> if (child === form) result = discarded }
        return result
    }

    /** Walks [container]'s direct children, reporting each form and whether `#_` discarded it. */
    private inline fun forEachChildForm(container: PsiElement, action: (PhelForm, Boolean) -> Unit) {
        var pendingDiscards = 0
        var child = container.firstChild
        while (child != null) {
            when {
                child.node?.elementType == PhelTypes.FORM_COMMENT -> pendingDiscards++
                child is PhelForm -> {
                    val discarded = pendingDiscards > 0
                    if (discarded) pendingDiscards--
                    action(child, discarded)
                }
            }
            child = child.nextSibling
        }
    }

    @JvmStatic
    fun getName(symbol: PhelSymbol): String? {
        val text = symbol.text ?: return null

        val slashIndex = text.lastIndexOf('/')
        return if (slashIndex > 0 && slashIndex < text.length - 1) {
            text.substring(slashIndex + 1)
        } else {
            text
        }
    }

    @JvmStatic
    fun getQualifier(symbol: PhelSymbol): String? {
        val text = symbol.text ?: return null

        val slashIndex = text.lastIndexOf('/')
        return if (slashIndex > 0) text.take(slashIndex) else null
    }

    @JvmStatic
    fun getNameTextOffset(symbol: PhelSymbol): Int {
        val text = symbol.text ?: return symbol.textRange.startOffset

        val slashIndex = text.lastIndexOf('/')
        return if (slashIndex > 0 && slashIndex < text.length - 1) {
            symbol.textRange.startOffset + slashIndex + 1
        } else {
            symbol.textRange.startOffset
        }
    }

    /** The first symbol inside the enclosing list — e.g. `defn` for `(defn foo [])`. */
    @JvmStatic
    fun getDefiningKeyword(symbol: PhelSymbol): String? {
        val containingList = PsiTreeUtil.getParentOfType(symbol, PhelList::class.java) ?: return null
        val firstForm = PsiTreeUtil.findChildOfType(containingList, PhelForm::class.java) ?: return null
        return PsiTreeUtil.findChildOfType(firstForm, PhelSymbol::class.java)?.text
    }

    /** 1-based line number of [element] within its containing file, or 0 if unknown. */
    @JvmStatic
    fun getLineNumber(element: PsiElement): Int {
        val fileText = element.containingFile?.text ?: return 0
        val offset = element.textOffset
        var lineNumber = 1
        var i = 0
        while (i < offset && i < fileText.length) {
            if (fileText[i] == '\n') lineNumber++
            i++
        }
        return lineNumber
    }

    /** `(file.phel:42)` location suffix, or empty string when the file is unavailable. */
    @JvmStatic
    fun getLocationString(element: PsiElement): String {
        val containingFile = element.containingFile ?: return ""
        val line = getLineNumber(element)
        return if (line > 0) "(${containingFile.name}:$line)" else "(${containingFile.name})"
    }
}
