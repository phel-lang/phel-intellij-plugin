package org.phellang.core.utils

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile

object PhelPerformanceUtils {

    const val MAX_PSI_TREE_DEPTH = 50
    const val MAX_FILE_SIZE_KB = 100

    fun shouldSkipExpensiveOperations(element: PsiElement): Boolean {
        return PhelErrorHandler.safeOperation({
            // Skip if file is too large
            val file = element.containingFile
            if (isFileTooLarge(file)) {
                return@safeOperation true
            }

            // Skip if PSI tree is too deep (malformed code)
            if (isPsiTreeTooDeep(element)) {
                return@safeOperation true
            }

            false
        }) ?: true // Skip on error to be safe
    }

    private fun isFileTooLarge(file: PsiFile?): Boolean {
        if (file == null) return false
        
        val virtualFile = file.virtualFile ?: return false
        val fileSizeKB = virtualFile.length / 1024
        
        return fileSizeKB > MAX_FILE_SIZE_KB
    }

    private fun isPsiTreeTooDeep(element: PsiElement): Boolean {
        var current: PsiElement? = element
        var depth = 0
        
        while (current != null && depth < MAX_PSI_TREE_DEPTH) {
            current = current.parent
            depth++
        }
        
        return depth >= MAX_PSI_TREE_DEPTH
    }
}
