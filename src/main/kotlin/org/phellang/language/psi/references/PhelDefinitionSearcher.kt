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
            // Fast reject: a file whose text never mentions the name cannot define it, so skip the
            // PSI walk for the many files that don't reference this symbol.
            .filter { it.text.contains(symbolName) }
            .flatMap { PhelDefinitionFinder.collectDefinitionsIn(it, symbolName) }
    }
}
