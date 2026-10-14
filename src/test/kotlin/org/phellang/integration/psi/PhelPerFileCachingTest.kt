package org.phellang.integration.psi

import com.intellij.openapi.application.WriteAction
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.editor.Document
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.util.PsiTreeUtil
import org.phellang.integration.PhelIntegrationTestCase
import org.phellang.language.psi.PhelList
import org.phellang.language.psi.PhelNamespaceUtils
import org.phellang.language.psi.PhelVendorUtils
import org.phellang.language.psi.files.PhelFile
import org.phellang.language.psi.analysis.PhelDestructuringAnalyzer
import org.phellang.language.psi.analysis.PhelSymbolAnalyzer
import org.phellang.language.psi.references.PhelDefinitionFinder
import org.phellang.language.psi.references.PhelLocalScopeResolver

/**
 * What the per-file caches keep, and what invalidates them.
 *
 * Every per-file value used to depend on the global PSI modification count, so a keystroke in any
 * file threw away every other file's cached `ns` form, alias map and definitions, and the next
 * project-wide lookup rebuilt them all. They now depend on their own file only. These pin both
 * directions: another file's edit keeps the cache, the file's own edit refreshes it.
 */
class PhelPerFileCachingTest : PhelIntegrationTestCase() {

    private fun phelFile(path: String, text: String): PhelFile = myFixture.addFileToProject(path, text) as PhelFile

    private fun edit(file: PhelFile, change: (Document) -> Unit) {
        val documentManager = PsiDocumentManager.getInstance(project)
        val document = documentManager.getDocument(file)!!
        WriteCommandAction.runWriteCommandAction(project) { change(document) }
        documentManager.commitDocument(document)
    }

    /** A fresh map comes back from every compute, so the same instance means the cache survived. */
    fun testEditingOneFileKeepsAnotherFilesCaches() {
        val other = phelFile("src/other.phel", "(ns app.other\n  (:require app.util :as u))\n")
        val edited = phelFile("src/edited.phel", "(ns app.edited)\n(defn f [] 1)\n")
        val before = PhelNamespaceUtils.extractAliasMap(other)

        edit(edited) { it.insertString(it.textLength, "(defn g [] 2)\n") }

        assertSame(before, PhelNamespaceUtils.extractAliasMap(other))
    }

    fun testEditingAFileRefreshesItsOwnCaches() {
        val file = phelFile("src/own.phel", "(ns app.own\n  (:require app.util :as u))\n")
        assertEquals("util", PhelNamespaceUtils.extractAliasMap(file)["u"])

        edit(file) { document ->
            val at = document.text.indexOf(":as u") + ":as ".length
            document.replaceString(at, at + 1, "v")
        }

        val after = PhelNamespaceUtils.extractAliasMap(file)
        assertEquals("util", after["v"])
        assertNull("the old alias must not survive the edit", after["u"])
    }

    fun testTheDefinitionsOfAFileAreCollectedOnce() {
        val file = phelFile("src/defs.phel", "(ns app.defs)\n(defn helper [] 1)\n")

        assertSame(
            PhelDefinitionFinder.collectDefinitionsIn(file, "helper"),
            PhelDefinitionFinder.collectDefinitionsIn(file, "helper"),
        )
    }

    fun testADefinitionAddedByAnEditIsFound() {
        val file = phelFile("src/later.phel", "(ns app.later)\n(defn helper [] 1)\n")
        assertEmpty(PhelDefinitionFinder.collectDefinitionsIn(file, "later"))

        edit(file) { it.insertString(it.textLength, "(defn later [] 2)\n") }

        assertEquals(listOf("later"), PhelDefinitionFinder.collectDefinitionsIn(file, "later").map { it.text })
    }

    /** The cached lookup must answer exactly what walking every list did: nested forms too, in document order. */
    fun testTheCachedLookupMatchesTheFullWalk() {
        val file = phelFile(
            "src/walk.phel",
            "(ns app.walk)\n(def x 1)\n(comment (def x 2))\n(defn f [] (def x 3))\n(defn x [] 4)\n(def y 5)\n",
        )

        for (name in listOf("x", "y", "f", "missing")) {
            val walked = PsiTreeUtil.findChildrenOfType(file, PhelList::class.java)
                .mapNotNull { PhelDefinitionFinder.findDefinitionInList(it, name) }

            assertEquals("definitions of '$name'", walked, PhelDefinitionFinder.collectDefinitionsIn(file, name))
        }
        assertEquals(4, PhelDefinitionFinder.collectDefinitionsIn(file, "x").size)
    }

    fun testTheParametersOfAFileAreCollectedOnce() {
        val file = phelFile("src/params.phel", "(ns app.params)\n(defn f [x] x)\n")

        assertSame(
            PhelLocalScopeResolver.findAllParametersIn(file, "x"),
            PhelLocalScopeResolver.findAllParametersIn(file, "x"),
        )
    }

    /** Destructured and nested parameters too, in document order, exactly as the full walk found them. */
    fun testTheCachedParameterLookupMatchesTheFullWalk() {
        val file = phelFile(
            "src/paramwalk.phel",
            "(ns app.paramwalk)\n(defn f [x {:keys [y]}] (fn [x z] x))\n(defn g [[x & more]] more)\n(def h 1)\n",
        )

        for (name in listOf("x", "y", "z", "more", "h", "missing")) {
            val walked = PsiTreeUtil.findChildrenOfType(file, PhelList::class.java)
                .flatMap { list ->
                    PhelSymbolAnalyzer.findParameterVector(list)
                        ?.let(PhelDestructuringAnalyzer::parameterSymbols)
                        .orEmpty()
                }
                .filter { name == it.text }

            assertEquals("parameters named '$name'", walked, PhelLocalScopeResolver.findAllParametersIn(file, name))
        }
        assertEquals(3, PhelLocalScopeResolver.findAllParametersIn(file, "x").size)
    }

    fun testAParameterAddedByAnEditIsFound() {
        val file = phelFile("src/paramedit.phel", "(ns app.paramedit)\n(defn f [x] x)\n")
        assertEmpty(PhelLocalScopeResolver.findAllParametersIn(file, "fresh"))

        edit(file) { it.insertString(it.textLength, "(defn g [fresh] fresh)\n") }

        assertEquals(listOf("fresh"), PhelLocalScopeResolver.findAllParametersIn(file, "fresh").map { it.text })
    }

    fun testTheStdlibFolderIsFoundOnceItExists() {
        assertNull("precondition: no vendor folder yet", PhelVendorUtils.findPhelLibraryFolder(project))

        myFixture.addFileToProject("composer.json", "{}")
        myFixture.addFileToProject("vendor/phel-lang/phel-lang/src/phel/core.phel", "(ns phel.core)\n")

        assertEquals("phel", PhelVendorUtils.findPhelLibraryFolder(project)?.name)
    }

    fun testTheStdlibFolderIsForgottenOnceDeleted() {
        myFixture.addFileToProject("composer.json", "{}")
        val core = myFixture.addFileToProject("vendor/phel-lang/phel-lang/src/phel/core.phel", "(ns phel.core)\n")
        assertNotNull("precondition: vendor folder found", PhelVendorUtils.findPhelLibraryFolder(project))

        val vendor = generateSequence(core.virtualFile) { it.parent }.first { it.name == "vendor" }
        WriteAction.runAndWait<Throwable> { vendor.delete(this) }

        assertNull(PhelVendorUtils.findPhelLibraryFolder(project))
    }
}
