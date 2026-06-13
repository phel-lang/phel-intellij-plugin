package org.phellang.language.psi.utils

import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import org.phellang.core.utils.PhelErrorHandler
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
     * The forms of [list] with `#_`-discarded forms removed. A `#_` (FORM_COMMENT) leaf
     * discards the next form, and they stack (`#_#_` drops two), so callers that count
     * call arguments must not treat a discarded form as a real argument. Without this,
     * `(push #_skip coll x)` reads as three args instead of two.
     */
    @JvmStatic
    fun activeForms(list: PhelList): List<PhelForm> {
        val result = mutableListOf<PhelForm>()
        var pendingDiscards = 0
        var child = list.firstChild
        while (child != null) {
            when {
                child.node?.elementType == PhelTypes.FORM_COMMENT -> pendingDiscards++
                child is PhelForm -> {
                    if (pendingDiscards > 0) pendingDiscards-- else result.add(child)
                }
            }
            child = child.nextSibling
        }
        return result
    }

    @JvmStatic
    fun getName(symbol: PhelSymbol): String? {
        return PhelErrorHandler.safeOperation {
            val text = symbol.text ?: return@safeOperation null

            val slashIndex = text.lastIndexOf('/')
            if (slashIndex > 0 && slashIndex < text.length - 1) {
                text.substring(slashIndex + 1)
            } else {
                text
            }
        }
    }

    @JvmStatic
    fun getQualifier(symbol: PhelSymbol): String? {
        return PhelErrorHandler.safeOperation {
            val text = symbol.text ?: return@safeOperation null

            val slashIndex = text.lastIndexOf('/')
            if (slashIndex > 0) text.take(slashIndex) else null
        }
    }

    @JvmStatic
    fun getNameTextOffset(symbol: PhelSymbol): Int {
        return PhelErrorHandler.safeOperation {
            val text = symbol.text ?: return@safeOperation symbol.textRange.startOffset

            val slashIndex = text.lastIndexOf('/')
            if (slashIndex > 0 && slashIndex < text.length - 1) {
                symbol.textRange.startOffset + slashIndex + 1
            } else {
                symbol.textRange.startOffset
            }
        } ?: 0
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
