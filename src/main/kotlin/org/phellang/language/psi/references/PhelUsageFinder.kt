package org.phellang.language.psi.references

import com.intellij.openapi.util.text.StringUtil
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import org.phellang.language.psi.PhelForm
import org.phellang.language.psi.PhelList
import org.phellang.language.psi.PhelNamespaceFiles
import org.phellang.language.psi.PhelSymbol
import org.phellang.language.psi.PhelTypeTags
import org.phellang.language.psi.PhelVec
import org.phellang.language.psi.analysis.PhelSymbolAnalyzer
import org.phellang.language.psi.files.PhelFile
import org.phellang.language.psi.utils.PhelPsiUtils
import org.phellang.language.psi.utils.SymbolCategory

/**
 * Finds the usages of a definition — the reverse direction of [PhelReference], taken when the
 * clicked symbol *is* the definition.
 *
 * A local binding (a parameter or `let` name) can only be used inside the form that introduces it,
 * so its search stops there. A top-level definition is searched for in the files that can reach it.
 *
 * Usages are matched by name, so every walk drops type tags: `^map` spells `map` but names a type, and is
 * never a usage of a definition or binding called `map`.
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
            .filter { it !== symbol && isUsageNamed(it, symbolName) }
    }

    /**
     * The uses in other files: those of the definition's own namespace, and those that require it,
     * whether qualified, through an alias or `:refer`'d. No other file can reach the definition, so
     * a namespace that merely has a same-named symbol of its own is never searched; Rename used to
     * rewrite those too. A definition in a file with no namespace has no uses elsewhere.
     *
     * The narrowing is per file: within a file that does require the namespace, every symbol of the
     * name still counts, as before.
     */
    private fun findAcrossProject(symbol: PhelSymbol, symbolName: String): List<PsiElement> {
        val file = symbol.containingFile as? PhelFile ?: return emptyList()
        val namespace = PhelNamespaceFiles.declaredNamespace(file) ?: return emptyList()
        val project = symbol.project

        return (PhelNamespaceFiles.filesOf(project, namespace) + PhelNamespaceFiles.importersOf(project, namespace))
            .distinct()
            .filter { it != file }
            // Fast reject without copying the text: a file that never spells the name cannot use it.
            .filter { StringUtil.contains(it.viewProvider.contents, symbolName) }
            .flatMap { PsiTreeUtil.findChildrenOfType(it, PhelSymbol::class.java) }
            .filter { isUsageNamed(it, symbolName) }
    }

    /** Usages only — other definitions of the same name in scope are not usages of this one. */
    private fun findInLocalScope(symbol: PhelSymbol, symbolName: String): List<PsiElement> {
        val containingForm = findContainingForm(symbol) ?: return emptyList()

        return PsiTreeUtil.findChildrenOfType(containingForm, PhelSymbol::class.java)
            .filter { it !== symbol }
            .filter { isUsageNamed(it, symbolName) }
            .filterNot { PhelSymbolAnalyzer.isDefinition(it) }
    }

    private fun isUsageNamed(candidate: PhelSymbol, symbolName: String): Boolean =
        symbolName == PhelPsiUtils.getName(candidate) && !PhelTypeTags.isTypeTag(candidate)

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
