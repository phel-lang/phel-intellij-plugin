package org.phellang.language.psi.references

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiManager
import com.intellij.psi.search.FilenameIndex
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.util.PsiTreeUtil
import org.phellang.core.psi.PhelSymbolAnalyzer
import org.phellang.language.psi.PhelForm
import org.phellang.language.psi.PhelList
import org.phellang.language.psi.PhelSymbol
import org.phellang.language.psi.PhelVec
import org.phellang.language.psi.files.PhelFile
import org.phellang.language.psi.utils.PhelPsiUtils
import org.phellang.language.psi.utils.SymbolCategory

/**
 * Finds the usages of a definition — the reverse direction of [PhelReference], taken when the
 * clicked symbol *is* the definition.
 *
 * A local binding (a parameter or `let` name) can only be used inside the form that introduces it,
 * so its search stops there. Only top-level definitions justify the project-wide scan.
 */
internal object PhelUsageFinder {

    fun findUsages(symbol: PhelSymbol, symbolName: String): List<PsiElement> {
        if (isLocalBinding(symbol)) {
            val localUsages = findInLocalScope(symbol, symbolName)
            if (localUsages.isNotEmpty()) return localUsages
        }

        val usages = mutableListOf<PsiElement>()
        usages += findInCurrentFile(symbol, symbolName)

        if (!isLocalBinding(symbol)) {
            usages += findAcrossProject(symbol, symbolName)
        }

        return usages
    }

    /** Both usages *and* other definitions in this file — but never the symbol that was clicked. */
    private fun findInCurrentFile(symbol: PhelSymbol, symbolName: String): List<PsiElement> {
        val containingFile = symbol.containingFile as? PhelFile ?: return emptyList()

        return PsiTreeUtil.findChildrenOfType(containingFile, PhelSymbol::class.java)
            .filter { it !== symbol && symbolName == PhelPsiUtils.getName(it) }
    }

    private fun findAcrossProject(symbol: PhelSymbol, symbolName: String): List<PsiElement> {
        val project = symbol.project
        val currentFile = symbol.containingFile?.virtualFile
        val psiManager = PsiManager.getInstance(project)

        return FilenameIndex.getAllFilesByExt(project, "phel", GlobalSearchScope.projectScope(project))
            .filter { it != currentFile }
            .mapNotNull { psiManager.findFile(it) as? PhelFile }
            // Fast reject: a file whose text never mentions the name cannot use it, so skip the
            // PSI walk for the many files that don't.
            .filter { it.text.contains(symbolName) }
            .flatMap { PsiTreeUtil.findChildrenOfType(it, PhelSymbol::class.java) }
            .filter { symbolName == PhelPsiUtils.getName(it) }
    }

    /** Usages only — other definitions of the same name in scope are not usages of this one. */
    private fun findInLocalScope(symbol: PhelSymbol, symbolName: String): List<PsiElement> {
        val containingForm = findContainingForm(symbol) ?: return emptyList()

        return PsiTreeUtil.findChildrenOfType(containingForm, PhelSymbol::class.java)
            .filter { it !== symbol }
            .filter { symbolName == PhelPsiUtils.getName(it) }
            .filterNot { PhelSymbolAnalyzer.isDefinition(it) }
    }

    /** The nearest enclosing special form or control-flow form — the scope a binding is confined to. */
    private fun findContainingForm(symbol: PhelSymbol): PhelList? {
        var current = symbol.parent

        while (current != null) {
            if (current is PhelList && introducesScope(current)) return current
            current = current.parent
        }

        return null
    }

    private fun introducesScope(list: PhelList): Boolean {
        val firstForm = PsiTreeUtil.findChildOfType(list, PhelForm::class.java) ?: return false
        val keyword = PsiTreeUtil.findChildOfType(firstForm, PhelSymbol::class.java)?.text ?: return false

        return PhelSymbolAnalyzer.isSymbolType(keyword, SymbolCategory.SPECIAL_FORMS) ||
                PhelSymbolAnalyzer.isSymbolType(keyword, SymbolCategory.CONTROL_FLOW)
    }

    /**
     * A definition sitting inside a vector is a parameter or a `let` name — i.e. local. A top-level
     * `(def x …)` has no enclosing vector.
     */
    private fun isLocalBinding(symbol: PhelSymbol): Boolean {
        if (!PhelSymbolAnalyzer.isDefinition(symbol)) return false
        return PsiTreeUtil.getParentOfType(symbol, PhelVec::class.java) != null
    }
}
