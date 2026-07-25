package org.phellang.navigation

import com.intellij.navigation.ChooseByNameContributorEx
import com.intellij.navigation.NavigationItem
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.util.Processor
import com.intellij.util.indexing.FindSymbolParameters
import com.intellij.util.indexing.IdFilter
import org.phellang.indexing.PhelProjectSymbolIndex
import org.phellang.navigation.item.PhelSymbolNavigationItem

/**
 * Backs Go to Symbol (Navigate > Symbol) with the project's Phel definitions.
 *
 * The index this reads was already built and kept current for completion and arity resolution; it
 * simply had no route into the platform's name popups.
 */
class PhelGotoSymbolContributor : ChooseByNameContributorEx {

    override fun processNames(
        processor: Processor<in String>,
        scope: GlobalSearchScope,
        filter: IdFilter?,
    ) {
        val project = scope.project ?: return
        // Distinct: two namespaces may define the same name, and the popup's name list would
        // otherwise carry it twice. Both definitions still surface — processElementsWithName
        // returns one item each.
        val seen = HashSet<String>()
        for (symbol in PhelProjectSymbolIndex.getInstance(project).getAllSymbols()) {
            if (!seen.add(symbol.name)) continue
            if (!processor.process(symbol.name)) return
        }
    }

    override fun processElementsWithName(
        name: String,
        processor: Processor<in NavigationItem>,
        parameters: FindSymbolParameters,
    ) {
        val project = parameters.project
        val scope = parameters.searchScope
        for (symbol in PhelProjectSymbolIndex.getInstance(project).findByName(name)) {
            // A symbol whose file falls outside the requested scope is not a match: Go to Symbol
            // honours the "project vs all places" toggle through this.
            if (!scope.contains(symbol.file)) continue
            if (!processor.process(PhelSymbolNavigationItem(project, symbol))) return
        }
    }
}
