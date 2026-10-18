package org.phellang.integration.psi

import com.intellij.openapi.application.WriteAction
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiFile
import com.intellij.testFramework.DumbModeTestUtils
import org.phellang.integration.PhelIntegrationTestCase
import org.phellang.language.psi.PhelNamespaceFiles
import org.phellang.language.psi.files.PhelFile

/**
 * The project's namespace -> files lookup, and what keeps it current.
 *
 * Resolution asks it for the files of one namespace instead of sweeping every project file, so it
 * must notice a file appearing, moving to another namespace or disappearing, and must not be rebuilt
 * by edits that cannot change it.
 */
class PhelNamespaceFilesTest : PhelIntegrationTestCase() {

    private val prefix: String get() = getTestName(true).lowercase()

    private fun ns(short: String) = "$prefix.$short"

    private fun phel(short: String, text: String): PhelFile =
        myFixture.addFileToProject("src/$prefix/$short.phel", text) as PhelFile

    private fun paths(files: List<PsiFile>): Set<String> = files.mapNotNull { it.virtualFile?.path }.toSet()

    private fun edit(file: PsiFile, from: String, to: String) {
        val documentManager = PsiDocumentManager.getInstance(project)
        val document = documentManager.getDocument(file)!!
        WriteCommandAction.runWriteCommandAction(project) {
            val at = document.text.indexOf(from)
            document.replaceString(at, at + from.length, to)
        }
        documentManager.commitDocument(document)
    }

    fun testAnNsFormDeclaresTheNamespace() {
        assertEquals(ns("a"), PhelNamespaceFiles.declaredNamespace(phel("a", "(ns ${ns("a")})\n")))
    }

    /** `phel.core` itself is split this way: `(in-ns phel.core)` at the top of each bucket file under `core/`. */
    fun testInNsDeclaresTheNamespaceWhenThereIsNoNsForm() {
        assertEquals(ns("b"), PhelNamespaceFiles.declaredNamespace(phel("b", "(in-ns ${ns("b")})\n(defn x [] 1)\n")))
    }

    fun testAFileWithNeitherHasNoNamespace() {
        assertNull(PhelNamespaceFiles.declaredNamespace(phel("none", "(defn x [] 1)\n")))
    }

    fun testABackslashNamespaceIsNormalized() {
        assertEquals("$prefix.c", PhelNamespaceFiles.declaredNamespace(phel("c", "(ns $prefix\\c)\n")))
    }

    fun testEveryFileThatDeclaresANamespaceIsOneOfItsFiles() {
        val main = phel("d", "(ns ${ns("d")})\n")
        val extra = phel("d_extra", "(in-ns ${ns("d")})\n")
        phel("other", "(ns ${ns("other")})\n")

        assertEquals(paths(listOf(main, extra)), paths(PhelNamespaceFiles.filesOf(project, ns("d"))))
    }

    fun testANewFileJoinsItsNamespace() {
        val first = phel("e", "(ns ${ns("e")})\n")
        assertEquals(paths(listOf(first)), paths(PhelNamespaceFiles.filesOf(project, ns("e"))))

        val second = phel("e_extra", "(in-ns ${ns("e")})\n")

        assertEquals(paths(listOf(first, second)), paths(PhelNamespaceFiles.filesOf(project, ns("e"))))
    }

    fun testChangingTheNsFormMovesTheFile() {
        val file = phel("f", "(ns ${ns("f")})\n")
        assertEquals(paths(listOf(file)), paths(PhelNamespaceFiles.filesOf(project, ns("f"))))

        edit(file, ns("f"), ns("g"))

        assertEmpty(PhelNamespaceFiles.filesOf(project, ns("f")))
        assertEquals(paths(listOf(file)), paths(PhelNamespaceFiles.filesOf(project, ns("g"))))
    }

    fun testADeletedFileLeavesItsNamespace() {
        val file = phel("h", "(ns ${ns("h")})\n")
        assertEquals(paths(listOf(file)), paths(PhelNamespaceFiles.filesOf(project, ns("h"))))

        WriteAction.runAndWait<Throwable> { file.virtualFile.delete(this) }

        assertEmpty(PhelNamespaceFiles.filesOf(project, ns("h")))
    }

    /** Edits to other kinds of file cannot change which file declares which namespace. */
    fun testEditingANonPhelFileKeepsTheLookup() {
        phel("i", "(ns ${ns("i")})\n")
        val notes = myFixture.addFileToProject("src/$prefix/notes.txt", "draft\n")
        val before = PhelNamespaceFiles.snapshotForTest(project)

        edit(notes, "draft", "final")

        assertSame(before, PhelNamespaceFiles.snapshotForTest(project))
    }

    fun testTheImportersOfANamespaceAreTheFilesThatRequireIt() {
        val aliased = phel("j_alias", "(ns ${ns("j_alias")}\n  (:require ${ns("j")} :as j))\n")
        val referring = phel("j_refer", "(ns ${ns("j_refer")}\n  (:require ${ns("j")} :refer [x]))\n")
        phel("j_unrelated", "(ns ${ns("j_unrelated")}\n  (:require ${ns("k")} :as k))\n")

        assertEquals(paths(listOf(aliased, referring)), paths(PhelNamespaceFiles.importersOf(project, ns("j"))))
    }

    fun testFilesAreFoundByShortNamespace() {
        val one = phel("l1", "(ns $prefix.one.l$prefix)\n")
        val two = phel("l2", "(ns $prefix.two.l$prefix)\n")

        assertEquals(paths(listOf(one, two)), paths(PhelNamespaceFiles.filesWithShortNamespace(project, "l$prefix")))
    }

    /** The compiler reads the first top-level form; a discarded one is not a form at all. */
    fun testADiscardedNsFormIsNotTheNamespace() {
        val file = phel("old", "#_(ns ${ns("old")})\n(ns ${ns("new")})\n")

        assertEquals(ns("new"), PhelNamespaceFiles.declaredNamespace(file))
    }

    /** `(ns ~n)` in a macro template names whatever the macro is called with, not this file. */
    fun testAnNsFormInsideAMacroBodyIsNotTheNamespace() {
        val file = phel("macro", "(in-ns ${ns("macro")})\n(defmacro m [n] `(ns ~n))\n")

        assertEquals(ns("macro"), PhelNamespaceFiles.declaredNamespace(file))
    }

    /** Mid-typing: the name is missing, and the first symbol of the clause after it is no namespace. */
    fun testAnIncompleteNsFormHasNoNamespace() {
        assertNull(PhelNamespaceFiles.declaredNamespace(phel("incomplete", "(ns (:require ${ns("x")}))\n")))
    }

    /** `in-ns` also takes a string (`InNsSymbol.php`). */
    fun testAStringInNsDeclaresTheNamespace() {
        assertEquals(ns("s"), PhelNamespaceFiles.declaredNamespace(phel("s", "(in-ns \"${ns("s")}\")\n")))
    }

    /** `schema/coercer.phel` declares `phel.schema.coercer`: it is no bucket of `phel.schema`. */
    fun testAStandardLibraryNamespaceLeavesOutItsSubNamespaces() {
        myFixture.addFileToProject("composer.json", "{}")
        val schema = myFixture.addFileToProject("vendor/phel-lang/phel-lang/src/phel/schema.phel", "(ns phel.schema)\n")
        myFixture.addFileToProject("vendor/phel-lang/phel-lang/src/phel/schema/coercer.phel", "(ns phel.schema.coercer)\n")

        assertEquals(listOf(schema.virtualFile.path), PhelNamespaceFiles.filesOf(project, "phel.schema").map { it.virtualFile.path })
    }

    /** The `core/` buckets join `phel.core` with `(in-ns phel.core)`, so they stay. */
    fun testPhelCoreIncludesItsInNsBuckets() {
        myFixture.addFileToProject("composer.json", "{}")
        val core = myFixture.addFileToProject("vendor/phel-lang/phel-lang/src/phel/core.phel", "(ns phel.core)\n")
        val bucket = myFixture.addFileToProject("vendor/phel-lang/phel-lang/src/phel/core/defs.phel", "(in-ns phel.core)\n")

        val found = paths(PhelNamespaceFiles.filesOf(project, "phel.core"))
        assertTrue("both the root file and the in-ns bucket: $found", found.containsAll(paths(listOf(core, bucket))))
    }

    /**
     * The file name index answers from the VFS during indexing rather than throwing, so a lookup built
     * then may be partial. Leaving dumb mode must not let it outlive the indexing it was built during.
     */
    fun testLeavingDumbModeRebuildsTheLookup() {
        phel("dumb", "(ns ${ns("dumb")})\n")

        val builtWhileDumb = DumbModeTestUtils.computeInDumbModeSynchronously<Any>(project) {
            PhelNamespaceFiles.snapshotForTest(project)
        }

        assertNotSame(builtWhileDumb, PhelNamespaceFiles.snapshotForTest(project))
    }

    /** Found by path under `vendor/`, so a project that excludes `vendor/` still has them; listed once either way. */
    fun testAStandardLibraryNamespaceIncludesItsVendorFilesOnce() {
        myFixture.addFileToProject("composer.json", "{}")
        val string = myFixture.addFileToProject("vendor/phel-lang/phel-lang/src/phel/string.phel", "(ns phel.string)\n")

        assertEquals(listOf(string.virtualFile.path), PhelNamespaceFiles.filesOf(project, "phel.string").map { it.virtualFile.path })
    }
}
