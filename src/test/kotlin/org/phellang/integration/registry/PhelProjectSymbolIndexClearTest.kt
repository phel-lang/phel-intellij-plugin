package org.phellang.integration.registry

import com.intellij.openapi.util.Disposer
import org.phellang.indexing.PhelProjectSymbolIndex
import org.phellang.integration.PhelIntegrationTestCase
import org.phellang.language.psi.files.PhelFile

/**
 * Guards the isolation fix from #271.
 *
 * The light project, and the index service on it, outlive every test class in the run. Clearing the
 * index used to be left to `Disposer.dispose`, which will not run `dispose()` a second time on an
 * instance it has already disposed — so from the second test method onward nothing was cleared, and
 * one class's definitions surfaced in the next class's `getAllSymbols()`.
 */
class PhelProjectSymbolIndexClearTest : PhelIntegrationTestCase() {

    private fun index() = PhelProjectSymbolIndex.getInstance(project)

    /**
     * Uses `configureByText`, not `addFileToProject`. Clearing resets the built flag, so the next
     * read rebuilds from the project — and a file left on disk would be found again, making
     * "empty after clear" unobservable. An in-memory file is not discovered by that scan, which is
     * what keeps these assertions deterministic.
     */
    private fun givenIndexed(path: String, name: String): PhelFile {
        val file = myFixture.configureByText(path, "(ns app\\clearing)\n(defn $name [] 1)\n") as PhelFile
        index().refreshFileFromPsi(file)
        return file
    }

    fun testClearEmptiesTheIndex() {
        givenIndexed("clear-one.phel", "clear-one")
        assertFalse("precondition: the index must hold something", index().getAllSymbols().isEmpty())

        index().clear()

        assertEmpty(index().getAllSymbols())
    }

    /** The case `Disposer` skips, and the one that made definitions leak between test classes. */
    fun testClearStillWorksOnAnAlreadyDisposedIndex() {
        val index = index()
        givenIndexed("clear-two.phel", "clear-two")
        Disposer.dispose(index)

        // Repopulate the disposed-but-still-returned instance, exactly as a later test method would.
        givenIndexed("clear-three.phel", "clear-three")
        assertFalse(index.getAllSymbols().isEmpty())

        index.clear()

        assertEmpty(index.getAllSymbols())
    }

    fun testClearedIndexCanBeRepopulated() {
        givenIndexed("clear-four.phel", "clear-four")
        index().clear()

        givenIndexed("clear-five.phel", "clear-five")

        assertEquals(listOf("clear-five"), index().getAllSymbols().map { it.name })
    }

    fun testFindByNameSeesNothingAfterAClear() {
        givenIndexed("clear-six.phel", "clear-six")
        assertFalse(index().findByName("clear-six").isEmpty())

        index().clear()

        assertEmpty(index().findByName("clear-six"))
    }
}
