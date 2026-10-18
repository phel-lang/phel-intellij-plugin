package org.phellang.integration.psi

import com.intellij.psi.PsiFile
import com.intellij.psi.PsiPolyVariantReference
import com.intellij.psi.util.PsiTreeUtil
import org.phellang.integration.PhelIntegrationTestCase
import org.phellang.language.psi.PhelSymbol

/**
 * Where the uses of a definition are looked for: its own namespace and the namespaces that require
 * it, never a namespace that merely has a same-named symbol.
 *
 * Ctrl-click on a definition lists its usages, and Rename rewrites them. Both used to match the name
 * in every project file, so renaming `app.a/helper` also renamed `app.c`'s unrelated `helper`.
 */
class PhelCrossFileUsagesTest : PhelIntegrationTestCase() {

    private val prefix: String get() = getTestName(true).lowercase()

    private fun ns(short: String) = "$prefix.$short"

    private fun file(short: String, text: String): PsiFile =
        myFixture.addFileToProject("src/$prefix/$short.phel", text)

    private fun definitionIn(file: PsiFile, name: String): PhelSymbol =
        PsiTreeUtil.findChildrenOfType(file, PhelSymbol::class.java).first { it.text == name }

    private class Fixture(val a: PsiFile, val b: PsiFile, val c: PsiFile, val d: PsiFile)

    /** `b` uses `helper` qualified, `d` refers it, `c` has an unrelated `helper` of its own. */
    private fun fixture(): Fixture = Fixture(
        a = file("a", "(ns ${ns("a")})\n(defn helper [] 1)\n"),
        b = file("b", "(ns ${ns("b")}\n  (:require ${ns("a")} :as a))\n(defn g [] (a/helper))\n"),
        c = file("c", "(ns ${ns("c")})\n(defn helper [] 3)\n(defn k [] (helper))\n"),
        d = file("d", "(ns ${ns("d")}\n  (:require ${ns("a")} :refer [helper]))\n(defn h [] (helper))\n"),
    )

    fun testUsagesComeFromTheNamespaceAndItsImportersOnly() {
        val files = fixture()
        val definition = definitionIn(files.a, "helper")

        val usageFiles = (definition.reference as PsiPolyVariantReference).multiResolve(false)
            .mapNotNull { it.element?.containingFile?.virtualFile?.path }
            .toSet()

        assertTrue("qualified use in b: $usageFiles", files.b.virtualFile.path in usageFiles)
        assertTrue("referred use in d: $usageFiles", files.d.virtualFile.path in usageFiles)
        assertFalse("c has its own helper: $usageFiles", files.c.virtualFile.path in usageFiles)
    }

    /** A vector-entry refer is a use too; missing it would let Safe Delete remove a function still called. */
    fun testRenameReachesAVectorEntryRefer() {
        val a = file("a", "(ns ${ns("a")})\n(defn helper [] 1)\n")
        val e = file("e", "(ns ${ns("e")}\n  (:require [${ns("a")} :refer [helper]]))\n(defn m [] (helper))\n")

        myFixture.renameElement(definitionIn(a, "helper"), "assist")

        assertEquals("(ns ${ns("e")}\n  (:require [${ns("a")} :refer [assist]]))\n(defn m [] (assist))\n", e.text)
    }

    fun testRenameLeavesAnUnrelatedNamespaceAlone() {
        val files = fixture()

        myFixture.renameElement(definitionIn(files.a, "helper"), "assist")

        assertTrue(files.b.text, files.b.text.contains("(a/assist)"))
        assertTrue(files.d.text, files.d.text.contains("(assist)"))
        assertTrue("the :refer vector itself: ${files.d.text}", files.d.text.contains(":refer [assist]"))
        assertEquals("(ns ${ns("c")})\n(defn helper [] 3)\n(defn k [] (helper))\n", files.c.text)
    }
}
