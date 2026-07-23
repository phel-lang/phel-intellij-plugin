package org.phellang.annotator.analyzers

import com.intellij.openapi.util.Key
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.util.CachedValue
import com.intellij.psi.util.CachedValueProvider
import com.intellij.psi.util.CachedValuesManager
import com.intellij.psi.util.PsiModificationTracker
import org.phellang.language.psi.*
import org.phellang.language.psi.utils.PhelPsiUtils

object PhelCommentAnalyzer {
    private const val FORM_COMMENT_MARKER = "#_"

    // Only `#(` opens an anonymous function (PhelHashFn); the deprecated `|(` is lexed as a
    // plain symbol and never produces one.
    private const val ANON_FN_MARKER = "#("

    private val HAS_FORM_COMMENT_KEY: Key<CachedValue<Boolean>> = Key.create("phel.hasFormComment")

    private val HAS_ANON_FN_KEY: Key<CachedValue<Boolean>> = Key.create("phel.hasAnonFn")

    fun isCommentedOutByFormComment(element: PsiElement): Boolean {
        // Fast path: a file with no `#_` marker anywhere can't comment out any form, so skip
        // the ancestor walk that otherwise runs for every element.
        val file = element.containingFile ?: return false
        if (!fileHasFormComment(file)) return false

        return PhelPsiUtils.isDiscardedByFormComment(element)
    }

    private fun fileHasFormComment(file: PsiFile): Boolean {
        return CachedValuesManager.getCachedValue(file, HAS_FORM_COMMENT_KEY) {
            CachedValueProvider.Result.create(
                file.text.contains(FORM_COMMENT_MARKER),
                PsiModificationTracker.MODIFICATION_COUNT,
            )
        }
    }

    fun isInsideAnonFunction(element: PsiElement): Boolean {
        // Fast path: a file with no `#(` can't contain an anonymous function, so skip the
        // ancestor walk that otherwise runs for every annotated symbol/keyword. When no
        // containing file is available we fall through to the walk.
        val file = element.containingFile
        if (file != null && !fileHasAnonFunction(file)) return false

        var current = element.parent
        while (current != null) {
            if (current is PhelHashFn) {
                return true
            }
            current = current.parent
        }
        return false
    }

    private fun fileHasAnonFunction(file: PsiFile): Boolean {
        return CachedValuesManager.getCachedValue(file, HAS_ANON_FN_KEY) {
            CachedValueProvider.Result.create(
                file.text.contains(ANON_FN_MARKER),
                PsiModificationTracker.MODIFICATION_COUNT,
            )
        }
    }
}
