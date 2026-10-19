package org.phellang.refactoring.safedelete

import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.util.text.StringUtil
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiManager
import com.intellij.psi.PsiPolyVariantReference
import com.intellij.psi.search.FilenameIndex
import com.intellij.psi.search.GlobalSearchScope
import org.phellang.language.psi.PhelNamespaceUtils
import org.phellang.language.psi.PhelSymbol
import org.phellang.language.psi.analysis.PhelSymbolAnalyzer
import org.phellang.language.psi.files.PhelFile
import org.phellang.language.psi.references.PhelUsageFinder
import org.phellang.language.psi.utils.PhelPsiUtils

/**
 * Same-named symbols that resolve to nothing: possible uses of a definition that the plugin cannot see.
 *
 * Safe Delete counts as usages what resolution links to the definition, and resolution follows the
 * compiler's rules: same namespace, `:refer`, an alias or the full name. A use reached any other way
 * (an `in-ns` file relying on its namespace's refers, an import shape the plugin misreads) would be a
 * silent "no usages", and the function would be deleted while still called. A symbol of the same name
 * that resolves to *nothing* is the tell, so it is reported too. One that resolves to some other
 * definition is not: it plainly means something else.
 *
 * Runs only when Safe Delete is invoked, in its background usage search, and reads only files whose text
 * spells the name.
 */
internal object PhelUnseenUseFinder {

    fun find(definition: PhelSymbol, isInsideDeleted: (PsiElement) -> Boolean): List<PhelSymbol> {
        val name = PhelPsiUtils.getName(definition) ?: return emptyList()
        val project = definition.project
        val psiManager = PsiManager.getInstance(project)

        return FilenameIndex.getAllFilesByExt(project, "phel", GlobalSearchScope.projectScope(project))
            .asSequence()
            .onEach { ProgressManager.checkCanceled() }
            .mapNotNull { psiManager.findFile(it) as? PhelFile }
            .filter { StringUtil.contains(it.viewProvider.contents, name) }
            // The symbols spelling the name, type tags left out: the usage search's own cached lookup.
            .flatMap { PhelUsageFinder.symbolsNamed(it, name).asSequence() }
            .filter { it !== definition }
            .filterNot { PhelSymbolAnalyzer.isDefinition(it) || isInsideDeleted(it) }
            // `php/name` is PHP interop, never a Phel definition.
            .filterNot { PhelPsiUtils.getQualifier(it) == PhelNamespaceUtils.PHP_INTEROP_QUALIFIER }
            .filter(::resolvesToNothing)
            .toList()
    }

    private fun resolvesToNothing(symbol: PhelSymbol): Boolean =
        (symbol.reference as? PsiPolyVariantReference)?.multiResolve(false)?.isEmpty() == true
}
