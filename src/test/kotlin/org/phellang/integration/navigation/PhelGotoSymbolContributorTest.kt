package org.phellang.integration.navigation

import com.intellij.navigation.NavigationItem
import com.intellij.openapi.application.WriteAction
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.util.CommonProcessors
import com.intellij.util.indexing.FindSymbolParameters
import org.phellang.indexing.PhelProjectSymbolIndex
import org.phellang.integration.PhelIntegrationTestCase
import org.phellang.language.psi.files.PhelFile
import org.phellang.navigation.PhelGotoSymbolContributor

/**
 * Go to Symbol over the project index.
 *
 * The index is primed with `refreshFileFromPsi`: the FilenameIndex-driven discovery in `buildIndex`
 * finds nothing under the light test fixture, as [org.phellang.integration.registry.PhelProjectSymbolIndexBuildTest]
 * documents.
 */
class PhelGotoSymbolContributorTest : PhelIntegrationTestCase() {

    private val contributor = PhelGotoSymbolContributor()

    private fun index() = PhelProjectSymbolIndex.getInstance(project)

    private val created = mutableListOf<VirtualFile>()

    private fun givenIndexed(path: String, text: String): PhelFile {
        val file = myFixture.addFileToProject(path, text) as PhelFile
        file.virtualFile?.let { created += it }
        index().refreshFileFromPsi(file)
        return file
    }

    /**
     * The light fixture's project, and its symbol index, outlive this class.
     *
     * The index is dropped from the index explicitly rather than left to `super.tearDown()`: that
     * disposes the service, and `Disposer` will not run `dispose()` a second time on an instance it
     * has already disposed, so from the second test method onward the maps are never cleared. Every
     * definition named here would otherwise turn up in the next class's `getAllSymbols()`.
     */
    override fun tearDown() {
        try {
            created.forEach { index().removeFile(it) }
            WriteAction.runAndWait<Throwable> {
                created.filter { it.isValid }.forEach { it.delete(this) }
            }
            created.clear()
        } finally {
            super.tearDown()
        }
    }

    private fun names(): List<String> {
        val collector = CommonProcessors.CollectProcessor<String>()
        contributor.processNames(collector, GlobalSearchScope.allScope(project), null)
        return collector.results.toList()
    }

    private fun itemsNamed(name: String): List<NavigationItem> {
        val collector = CommonProcessors.CollectProcessor<NavigationItem>()
        contributor.processElementsWithName(
            name,
            collector,
            FindSymbolParameters.simple(project, true),
        )
        return collector.results.toList()
    }

    fun testOffersEveryIndexedDefinition() {
        givenIndexed("src/offers.phel", "(ns app\\offers)\n(defn offered-fn [] 1)\n(def offered-val 2)\n")

        val offered = names().filter { it.startsWith("offered-") }

        assertEquals(listOf("offered-fn", "offered-val"), offered.sorted())
    }

    fun testFindsADefinitionByName() {
        givenIndexed("src/found.phel", "(ns app\\found)\n(defn found-fn [] 1)\n")

        val items = itemsNamed("found-fn")

        assertEquals(1, items.size)
        assertEquals("found-fn", items.single().name)
    }

    fun testNavigatesToTheDefinitionNotTheTopOfTheFile() {
        val file = givenIndexed("src/offset.phel", "(ns app\\offset)\n(defn offset-fn [] 1)\n")
        val expected = file.text.indexOf("offset-fn")

        val symbol = index().findByName("offset-fn").single { it.file == file.virtualFile }

        assertEquals(expected, symbol.nameOffset)
    }

    /** Two namespaces may define the same name; the popup has to show both, told apart by location. */
    fun testKeepsBothDefinitionsOfASharedName() {
        givenIndexed("src/dup-a.phel", "(ns app\\dupa)\n(defn dup-fn [] 1)\n")
        givenIndexed("src/dup-b.phel", "(ns app\\dupb)\n(defn dup-fn [] 2)\n")

        val items = itemsNamed("dup-fn")

        assertEquals(2, items.size)
        assertEquals(
            listOf("app\\dupa", "app\\dupb"),
            items.mapNotNull { it.presentation?.locationString }.sorted(),
        )
    }

    fun testReturnsNothingForAnUnknownName() {
        givenIndexed("src/unknown.phel", "(ns app\\unknown)\n(defn unknown-fn [] 1)\n")

        assertEmpty(itemsNamed("definitely-not-defined"))
    }

    /** Private definitions are not indexed, so they must not surface in the popup either. */
    fun testOmitsPrivateDefinitions() {
        givenIndexed("src/privacy.phel", "(ns app\\privacy)\n(defn- privacy-hidden [] 1)\n(defn privacy-shown [] 2)\n")

        assertEquals(listOf("privacy-shown"), names().filter { it.startsWith("privacy-") })
    }

    /** The name list is a set of names; the two definitions behind a shared one come from the lookup. */
    fun testListsASharedNameOnlyOnce() {
        givenIndexed("src/once-a.phel", "(ns app\\oncea)\n(defn once-fn [] 1)\n")
        givenIndexed("src/once-b.phel", "(ns app\\onceb)\n(defn once-fn [] 2)\n")

        assertEquals(listOf("once-fn"), names().filter { it == "once-fn" })
    }

    fun testItemCanNavigate() {
        givenIndexed("src/nav.phel", "(ns app\\nav)\n(defn nav-fn [] 1)\n")

        assertTrue(itemsNamed("nav-fn").single().canNavigateToSource())
    }
}
