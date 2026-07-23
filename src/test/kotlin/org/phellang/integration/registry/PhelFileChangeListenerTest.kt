package org.phellang.integration.registry

import com.intellij.openapi.application.ApplicationManager
import com.intellij.testFramework.LightVirtualFile
import org.phellang.integration.PhelIntegrationTestCase
import org.phellang.language.psi.files.PhelFile
import org.phellang.registry.indexing.PhelFileChangeListener

/**
 * VFS_CHANGES is an application-level topic, so the listener sees `.phel` events for files anywhere
 * on disk. Only files that belong to this project's content may feed its symbol index; a `.phel`
 * file elsewhere must not leak into completion or go-to-definition.
 */
class PhelFileChangeListenerTest : PhelIntegrationTestCase() {

    private fun listener() = PhelFileChangeListener(project)

    private fun <T> read(block: () -> T): T =
        ApplicationManager.getApplication().runReadAction<T> { block() }

    fun testInProjectPhelFileIsIndexed() {
        val file = myFixture.addFileToProject("src/a.phel", "(ns app\\a)\n(defn foo [] 1)\n") as PhelFile
        val vf = file.virtualFile

        assertTrue(read { listener().shouldIndex(vf) })
    }

    fun testPhelFileOutsideProjectContentIsExcluded() {
        // Not attached to any content root — stands in for a .phel file in another project or a
        // temp/scratch directory that VFS_CHANGES still delivers.
        val foreign = LightVirtualFile("foreign.phel", "(ns other)\n(defn bar [] 1)\n")

        assertFalse(read { listener().shouldIndex(foreign) })
    }

    fun testNonPhelFileInProjectIsExcluded() {
        val vf = myFixture.addFileToProject("src/notes.txt", "not phel").virtualFile

        assertFalse(read { listener().shouldIndex(vf) })
    }
}
