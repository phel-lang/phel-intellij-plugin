package org.phellang.integration.psi

import com.intellij.openapi.command.WriteCommandAction
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import org.phellang.annotator.validators.PhelFunctionReferenceValidator
import org.phellang.annotator.validators.PhelNamespaceValidator
import org.phellang.indexing.PhelProjectSymbolIndex
import org.phellang.integration.PhelIntegrationTestCase
import org.phellang.language.psi.PhelNamespaceUtils
import org.phellang.language.psi.PhelSymbol
import org.phellang.language.psi.analysis.PhelUnusedImportFinder
import org.phellang.language.psi.files.PhelFile

/**
 * A file that joins a namespace with `(in-ns ...)` works under that namespace's requires, refers and
 * `:use` classes, which the compiler keeps per namespace (`GlobalEnvironment::addRequireAlias`,
 * `addRefers`), not per file.
 *
 * They were read from the file's own `ns` form, which an `in-ns` file does not have: `(s/join ...)`
 * there was reported as "Namespace 's' does not exist", and a require used only from such a file was
 * reported unused in the `ns` file, where Optimize Imports would have deleted it.
 */
class PhelInNsEnvironmentTest : PhelIntegrationTestCase() {

    private lateinit var nsFile: PhelFile
    private var fileIndex = 0

    override fun setUp() {
        super.setUp()
        index(myFixture.addFileToProject("src/app/util.phel", "(ns app.util)\n(defn helper [x] x)\n(defn other [x] x)\n") as PhelFile)
        nsFile = myFixture.addFileToProject(
            "src/app/core.phel",
            "(ns app.core\n  (:require phel.string :as s)\n  (:require app.util :as u :refer [helper])\n  (:use DateTime))\n" +
                "(defn main [] 1)\n",
        ) as PhelFile
        index(nsFile)
    }

    private fun index(file: PhelFile) = PhelProjectSymbolIndex.getInstance(project).refreshFileFromPsi(file)

    private fun bucket(body: String): PhelFile {
        val file = myFixture.addFileToProject("src/app/core/bucket${fileIndex++}.phel", "(in-ns app.core)\n$body\n") as PhelFile
        index(file)
        return file
    }

    private fun symbolAtLast(file: PhelFile, needle: String): PhelSymbol =
        PsiTreeUtil.findElementOfClassAtOffset(file, file.text.lastIndexOf(needle), PhelSymbol::class.java, false)!!

    private fun namespaceOf(target: PsiElement?): String? =
        (target?.containingFile as? PhelFile)?.let(PhelNamespaceUtils::extractNamespaceFromFile)

    private fun requireSymbol(namespace: String): PhelSymbol =
        PsiTreeUtil.findChildrenOfType(nsFile, PhelSymbol::class.java).first { it.text == namespace }

    // Reading the namespace's environment

    fun testAnAliasOfTheNamespaceIsInForceInItsInNsFile() {
        val file = bucket("(defn f [] (s/join [1]))")
        val call = symbolAtLast(file, "s/join")

        assertEmpty(PhelNamespaceValidator.validateNamespace(call))
        assertEmpty(PhelFunctionReferenceValidator.validateFunctionReference(call))
    }

    fun testAProjectAliasResolvesInAnInNsFile() {
        val file = bucket("(defn f [] (u/other 1))")

        assertEmpty(PhelNamespaceValidator.validateNamespace(symbolAtLast(file, "u/other")))
        assertEquals("app.util", namespaceOf(symbolAtLast(file, "u/other").reference?.resolve()))
    }

    fun testAReferredNameResolvesInAnInNsFile() {
        val file = bucket("(defn f [] (helper 1))")

        assertEquals("app.util", namespaceOf(symbolAtLast(file, "helper").reference?.resolve()))
    }

    fun testAUsedClassIsKnownInAnInNsFile() {
        val file = bucket("(defn f [] (DateTime/createFromFormat \"Y\" \"2020\"))")

        assertTrue(PhelNamespaceUtils.extractUsedClasses(file).contains("DateTime"))
    }

    fun testAFileWithItsOwnNsKeepsItsOwnRequires() {
        val other = myFixture.addFileToProject("src/app/other.phel", "(ns app.other)\n(defn g [] (s/join [1]))\n") as PhelFile

        assertNull(PhelNamespaceUtils.extractAliasMap(other)["s"])
    }

    /** The `in-ns` file reads another file's `ns` form, so an edit there must reach it. */
    fun testEditingTheNsFileReachesTheInNsFile() {
        val file = bucket("(defn f [] (t/join [1]))")
        assertNull(PhelNamespaceUtils.extractAliasMap(file)["t"])

        val documentManager = PsiDocumentManager.getInstance(project)
        val document = documentManager.getDocument(nsFile)!!
        WriteCommandAction.runWriteCommandAction(project) {
            val at = document.text.indexOf(":as s") + ":as ".length
            document.replaceString(at, at + 1, "t")
        }
        documentManager.commitDocument(document)

        assertEquals("string", PhelNamespaceUtils.extractAliasMap(file)["t"])
    }

    // Unused imports across the namespace's files

    fun testARequireUsedOnlyFromAnInNsFileIsUsed() {
        bucket("(defn f [] (s/join [1]))")

        assertFalse(PhelUnusedImportFinder.isUnusedImport(requireSymbol("phel.string")))
    }

    fun testARequireNoFileOfTheNamespaceUsesIsUnused() {
        bucket("(defn f [] 1)")

        assertTrue(PhelUnusedImportFinder.isUnusedImport(requireSymbol("phel.string")))
    }
}
