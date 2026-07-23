package org.phellang.integration.registry

import com.intellij.openapi.progress.ProcessCanceledException
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.progress.util.ProgressIndicatorBase
import org.phellang.integration.PhelIntegrationTestCase
import org.phellang.language.psi.files.PhelFile
import org.phellang.registry.indexing.PhelProjectSymbolIndex

/**
 * The lazy full-project scan must be cancellable and idempotent: a scan abandoned mid-way (the user
 * keeps typing) has to leave the index in a state a later call rebuilds cleanly, never duplicating
 * the files it already visited.
 *
 * The scan loop is exercised directly via [PhelProjectSymbolIndex.indexFiles] because the
 * FilenameIndex-driven discovery in `buildIndex` finds nothing under the light test fixture — which
 * is exactly why the rest of the suite primes the index with `refreshFileFromPsi`.
 */
class PhelProjectSymbolIndexBuildTest : PhelIntegrationTestCase() {

    private fun index() = PhelProjectSymbolIndex.getInstance(project)

    fun testScanIndexesSymbolsFromEveryFile() {
        val a = myFixture.addFileToProject("src/a.phel", "(ns app\\a)\n(defn foo [] 1)\n") as PhelFile
        val b = myFixture.addFileToProject("src/b.phel", "(ns app\\b)\n(defn bar [] 2)\n") as PhelFile

        index().indexFiles(listOf(a, b))

        assertEquals(listOf("bar", "foo"), index().getAllSymbols().map { it.name }.sorted())
    }

    fun testReScanningTheSameFilesDoesNotDuplicate() {
        val a = myFixture.addFileToProject("src/a.phel", "(ns app\\a)\n(defn foo [] 1)\n") as PhelFile

        index().indexFiles(listOf(a))
        index().indexFiles(listOf(a))

        assertEquals("re-scanning must replace, not append", 1, index().findByName("foo").size)
    }

    /** Underpins safe rebuild-after-cancel: a single file's re-scan is idempotent. */
    fun testRefreshingTheSameFileIsIdempotent() {
        val file = myFixture.addFileToProject("src/a.phel", "(ns app\\a)\n(defn foo [] 1)\n") as PhelFile

        index().refreshFileFromPsi(file)
        index().refreshFileFromPsi(file)

        assertEquals(1, index().findByName("foo").size)
    }

    fun testScanHonorsCancellationAndLeavesFilesRescannable() {
        val a = myFixture.addFileToProject("src/a.phel", "(ns app\\a)\n(defn foo [] 1)\n") as PhelFile
        val b = myFixture.addFileToProject("src/b.phel", "(ns app\\b)\n(defn bar [] 2)\n") as PhelFile

        val cancelled = ProgressIndicatorBase().apply { start(); cancel() }
        try {
            ProgressManager.getInstance().runProcess({ index().indexFiles(listOf(a, b)) }, cancelled)
            fail("the scan must honor cancellation")
        } catch (expected: ProcessCanceledException) {
            // expected: checkCanceled aborted before touching any file
        }

        // A later, uncancelled scan rebuilds the whole set without duplication.
        index().indexFiles(listOf(a, b))
        assertEquals(listOf("bar", "foo"), index().getAllSymbols().map { it.name }.sorted())
        assertEquals(1, index().findByName("foo").size)
    }
}
