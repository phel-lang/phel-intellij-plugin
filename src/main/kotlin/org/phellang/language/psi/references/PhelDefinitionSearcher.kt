package org.phellang.language.psi.references

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiManager
import com.intellij.psi.search.FilenameIndex
import com.intellij.psi.search.GlobalSearchScope
import org.phellang.language.psi.PhelSymbol

/** Finds unqualified definitions of a name: first in the file being edited, then across the project. */
internal object PhelDefinitionSearcher {

    fun findInCurrentFile(symbol: PhelSymbol, symbolName: String): List<PsiElement> {
        val file = symbol.containingFile ?: return emptyList()
        return PhelDefinitionFinder.collectDefinitionsIn(file, symbolName)
    }

    fun findInProject(symbol: PhelSymbol, symbolName: String): List<PsiElement> {
        val project = symbol.project
        val currentFile = symbol.containingFile
        val psiManager = PsiManager.getInstance(project)

        return FilenameIndex.getAllFilesByExt(project, "phel", GlobalSearchScope.projectScope(project))
            .mapNotNull { psiManager.findFile(it) }
            .filter { it != currentFile }
            // No `text.contains(symbolName)` pre-check: each file's definitions are a cached lookup
            // now, and the check copied the whole text of every project file once per symbol.
            .flatMap { PhelDefinitionFinder.collectDefinitionsIn(it, symbolName) }
    }
}
