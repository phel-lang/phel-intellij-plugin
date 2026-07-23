package org.phellang.editor.paredit

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.util.PsiTreeUtil
import org.phellang.language.psi.PhelForm
import org.phellang.language.psi.PhelHashFn
import org.phellang.language.psi.PhelList
import org.phellang.language.psi.PhelMap
import org.phellang.language.psi.PhelReaderConditional
import org.phellang.language.psi.PhelReaderConditionalSplice
import org.phellang.language.psi.PhelSet
import org.phellang.language.psi.PhelVec

internal object PhelPareditContainers {

    fun findEnclosingContainer(file: PsiFile, offset: Int): PsiElement? {
        for (candidate in candidatesAt(file, offset)) {
            var current: PsiElement? = candidate
            while (current != null && current !is PsiFile) {
                if (isContainer(current)) return current
                current = current.parent
            }
        }
        return null
    }

    fun findFormAtCaret(file: PsiFile, offset: Int): PhelForm? {
        for (candidate in candidatesAt(file, offset)) {
            val form = PsiTreeUtil.getParentOfType(candidate, PhelForm::class.java)
            if (form != null) return form
        }
        return null
    }

    fun isContainer(element: PsiElement): Boolean = when (element) {
        is PhelList, is PhelVec, is PhelMap, is PhelSet,
        is PhelHashFn, is PhelReaderConditional, is PhelReaderConditionalSplice -> true
        else -> false
    }

    private fun candidatesAt(file: PsiFile, offset: Int): List<PsiElement> = listOfNotNull(
        file.findElementAt(offset),
        if (offset > 0) file.findElementAt(offset - 1) else null,
    )
}
