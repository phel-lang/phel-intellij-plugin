package org.phellang.integration.registry

import com.intellij.openapi.command.WriteCommandAction
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiManager
import com.intellij.testFramework.PlatformTestUtil
import org.phellang.indexing.PhelProjectSymbolIndex
import org.phellang.indexing.PhelPsiChangeListener
import org.phellang.integration.PhelIntegrationTestCase
import org.phellang.language.psi.files.PhelFile

/**
 * Live edits reach the symbol index, for every file edited rather than only the last one.
 *
 * The listener coalesces a typing burst into one pass on the EDT. It used to hold the pending file
 * in a single slot, so a second file edited before the pass ran overwrote the first — and because
 * the "already scheduled" flag was still set, no second pass was queued. The first file's symbols
 * stayed stale indefinitely.
 *
 * The listener is registered here rather than relied on through [PhelProjectSymbolIndex]'s
 * constructor: the light project is shared between test classes and the service is disposed in
 * tearDown, so by the time this class runs the service's own listeners may already be detached.
 */
class PhelPsiChangeListenerTest : PhelIntegrationTestCase() {

    fun testEveryEditedFileIsReindexed() {
        val index = PhelProjectSymbolIndex.getInstance(project)

        val first = myFixture.addFileToProject("src/first.phel", "(ns app\\first)\n") as PhelFile
        val second = myFixture.addFileToProject("src/second.phel", "(ns app\\second)\n") as PhelFile

        // Force the lazy full-project scan to completion *before* the edits, then start from empty.
        // Otherwise the first read below would rebuild from disk and find both definitions whether
        // the listener did its job or not, and the test would pass against the bug it guards.
        index.getAllSymbols()
        index.clear()

        PsiManager.getInstance(project).addPsiTreeChangeListener(PhelPsiChangeListener(project), testRootDisposable)

        // Both edits land before the scheduled pass runs, which is the case that used to lose one.
        appendDefinitions(first to "alpha-fn", second to "beta-fn")
        PlatformTestUtil.dispatchAllInvocationEventsInIdeEventQueue()

        assertEquals(
            "the first file's edit must survive a second file being edited in the same burst",
            listOf("first"),
            index.findByName("alpha-fn").map { it.shortNamespace },
        )
        assertEquals(
            listOf("second"),
            index.findByName("beta-fn").map { it.shortNamespace },
        )
    }

    private fun appendDefinitions(vararg edits: Pair<PhelFile, String>) {
        val documentManager = PsiDocumentManager.getInstance(project)

        WriteCommandAction.runWriteCommandAction(project) {
            for ((file, name) in edits) {
                val document = documentManager.getDocument(file)!!
                document.insertString(document.textLength, "(defn $name [] 1)\n")
                documentManager.commitDocument(document)
            }
        }
    }
}
