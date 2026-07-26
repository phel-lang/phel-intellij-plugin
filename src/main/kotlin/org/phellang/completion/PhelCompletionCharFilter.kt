package org.phellang.completion

import com.intellij.codeInsight.lookup.CharFilter
import com.intellij.codeInsight.lookup.Lookup
import org.phellang.language.infrastructure.PhelFileType
import org.phellang.language.psi.PhelSymbolChars

class PhelCompletionCharFilter : CharFilter() {

    override fun acceptChar(c: Char, prefixLength: Int, lookup: Lookup): Result? {
        val psiFile = lookup.psiFile ?: return null

        if (psiFile.fileType !is PhelFileType) {
            return null
        }

        // [PhelSymbolChars] rather than a list of its own: that list allowed `%` but not `.` or `\`,
        // so the lookup closed on the `.` of `.method` and the `\` of `\DateTime` — both of which the
        // completion offers in the first place.
        if (PhelSymbolChars.isSymbolPart(c)) {
            return Result.ADD_TO_PREFIX
        }

        return null
    }
}
