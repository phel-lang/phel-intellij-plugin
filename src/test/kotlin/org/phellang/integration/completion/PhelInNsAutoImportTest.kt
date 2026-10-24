package org.phellang.integration.completion

import com.intellij.openapi.application.WriteAction
import com.intellij.openapi.command.impl.UndoManagerImpl
import com.intellij.openapi.command.undo.UndoManager
import com.intellij.openapi.fileEditor.impl.text.TextEditorProvider
import com.intellij.psi.PsiDocumentManager
import org.phellang.indexing.PhelProjectSymbolIndex
import org.phellang.integration.PhelIntegrationTestCase
import org.phellang.language.psi.files.PhelFile

/**
 * Auto-import from a file that joins a namespace with `(in-ns ...)`. Such a file has no `ns` form;
 * its requires are its namespace's, in the `ns` file, so that is where a new one goes.
 *
 * Completion inserted `util/helper` and required nothing, and "Import 'app.util'" did nothing,
 * leaving a call the editor then reported as not imported.
 */
class PhelInNsAutoImportTest : PhelIntegrationTestCase() {

    private lateinit var nsFile: PhelFile

    override fun setUp() {
        super.setUp()
        PhelProjectSymbolIndex.getInstance(project).refreshFileFromPsi(
            myFixture.addFileToProject("src/app/util.phel", "(ns app.util)\n(defn helper [x] x)\n") as PhelFile
        )
    }

    private fun namespaceFile(requires: String = "") {
        nsFile = myFixture.addFileToProject("src/app/core.phel", "(ns app.core$requires)\n(defn main [] 1)\n") as PhelFile
    }

    private fun bucket(body: String) {
        myFixture.configureByText("bucket.phel", "(in-ns app.core)\n$body\n")
    }

    private fun complete(body: String) {
        bucket(body)
        myFixture.completeBasic()
    }

    fun testCompletionRequiresTheNamespaceInTheNsFile() {
        namespaceFile()
        complete("(defn f [] (util/hel<caret>))")

        assertTrue(myFixture.file.text, myFixture.file.text.contains("(util/helper)"))
        assertTrue(nsFile.text, nsFile.text.contains("(:require app.util)"))
        assertFalse("the in-ns file gets no ns form: ${myFixture.file.text}", myFixture.file.text.contains(":require"))
    }

    fun testCompletionOfAStdlibNameRequiresItInTheNsFile() {
        namespaceFile()
        complete("(defn f [] (string/upper-ca<caret>))")

        assertTrue(myFixture.file.text, myFixture.file.text.contains("(string/upper-case)"))
        assertTrue(nsFile.text, nsFile.text.contains("(:require phel.string)"))
    }

    fun testANamespaceTheNsFileAlreadyRequiresIsNotRequiredAgain() {
        namespaceFile("\n  (:require app.util :as u)")
        complete("(defn f [] (u/hel<caret>))")

        assertTrue(myFixture.file.text, myFixture.file.text.contains("(u/helper)"))
        assertEquals(nsFile.text, 1, Regex(":require").findAll(nsFile.text).count())
    }

    fun testTheImportQuickFixRequiresTheNamespaceInTheNsFile() {
        namespaceFile()
        bucket("(defn f [] (app.util/hel<caret>per 1))")

        myFixture.launchAction(myFixture.findSingleIntention("Import 'app.util'"))

        assertTrue(nsFile.text, nsFile.text.contains("(:require app.util)"))
    }

    fun testTheImportQuickFixIsNotOfferedForWhatTheNsFileRequires() {
        namespaceFile("\n  (:require app.util)")
        bucket("(defn f [] (app.util/hel<caret>per 1))")

        assertEmpty(myFixture.filterAvailableIntentions("Import 'app.util'"))
    }

    /**
     * The require lands in another file, but in the same command as the completion, so one undo in
     * the file being edited takes back both. Undoing across files asks for confirmation in the IDE;
     * the test answers it.
     */
    fun testOneUndoTakesBackTheCompletionAndTheRequire() {
        namespaceFile()
        val nsBefore = nsFile.text
        complete("(defn f [] (util/hel<caret>))")
        assertTrue("precondition: ${nsFile.text}", nsFile.text.contains("(:require app.util)"))

        val undoManager = UndoManager.getInstance(project) as UndoManagerImpl
        val fileEditor = TextEditorProvider.getInstance().getTextEditor(myFixture.editor)
        val neverAsk = UndoManagerImpl.ourNeverAskUser
        UndoManagerImpl.ourNeverAskUser = true
        try {
            assertTrue("undo available after the completion", undoManager.isUndoAvailable(fileEditor))
            undoManager.undo(fileEditor)
        } finally {
            UndoManagerImpl.ourNeverAskUser = neverAsk
        }
        PsiDocumentManager.getInstance(project).commitAllDocuments()

        assertEquals("(in-ns app.core)\n(defn f [] (util/hel))\n", myFixture.file.text)
        assertEquals(nsBefore, nsFile.text)
    }

    /** A namespace whose `ns` file cannot be edited, a vendored one say, is left as it is. */
    fun testAReadOnlyNsFileIsNotEdited() {
        namespaceFile()
        WriteAction.runAndWait<Throwable> { nsFile.virtualFile.isWritable = false }
        try {
            complete("(defn f [] (util/hel<caret>))")

            assertFalse(nsFile.text, nsFile.text.contains(":require"))
        } finally {
            WriteAction.runAndWait<Throwable> { nsFile.virtualFile.isWritable = true }
        }
    }
}
