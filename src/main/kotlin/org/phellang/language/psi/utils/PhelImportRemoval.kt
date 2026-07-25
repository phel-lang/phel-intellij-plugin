package org.phellang.language.psi.utils

import com.intellij.psi.util.PsiTreeUtil
import org.phellang.language.psi.PhelList
import org.phellang.language.psi.PhelSymbol

/**
 * Deletes an import entry from an `(ns …)` declaration.
 *
 * Lives in `language` because two quick fixes in different feature packages need it — the
 * annotator's duplicate-import fix and the inspection's unused-import fix — and a feature package
 * may not reach into another's internals.
 *
 * The caller supplies the write action: an `IntentionAction` opens its own, while a `LocalQuickFix`
 * is already running inside one.
 */
object PhelImportRemoval {

    /** Removes the require entry containing [symbol], along with the whitespace that preceded it. */
    fun removeEnclosingImport(symbol: PhelSymbol) {
        val requireList = PsiTreeUtil.getParentOfType(symbol, PhelList::class.java) ?: return

        val previous = requireList.prevSibling
        if (previous != null && previous.text.isBlank()) {
            previous.delete()
        }
        requireList.delete()
    }
}
