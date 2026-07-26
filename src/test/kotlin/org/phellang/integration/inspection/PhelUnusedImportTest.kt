package org.phellang.integration.inspection

import com.intellij.psi.util.PsiTreeUtil
import org.phellang.language.psi.analysis.PhelUnusedImportFinder
import org.phellang.integration.PhelIntegrationTestCase
import org.phellang.language.psi.PhelList
import org.phellang.language.psi.PhelSymbol
import org.phellang.language.psi.files.PhelFile

/**
 * Behavioural coverage for [PhelUnusedImportFinder.isUnusedImport], guarding the cached
 * used-qualifier scan: a required namespace is "unused" only when no `ns/...` call in the
 * file body references it.
 *
 * The detection moved here from `PhelImportValidator` when it became a switchable inspection.
 */
class PhelUnusedImportTest : PhelIntegrationTestCase() {

    fun testUsedImportIsNotUnused() {
        assertFalse(isUnused("(ns app\\m\n  (:require phel\\str))\n(str/join \", \" [1 2])\n", "phel\\str"))
    }

    fun testUnusedImportIsUnused() {
        assertTrue(isUnused("(ns app\\m\n  (:require phel\\str))\n(println \"hi\")\n", "phel\\str"))
    }

    /** Under `:as` the alias is the qualifier that counts, not the short namespace. */
    fun testImportUsedThroughItsAliasIsNotUnused() {
        assertFalse(isUnused("(ns app\\m\n  (:require phel\\str :as s))\n(s/join \", \" [1 2])\n", "phel\\str"))
    }

    fun testAliasedImportNeverUsedIsUnused() {
        assertTrue(isUnused("(ns app\\m\n  (:require phel\\str :as s))\n(println \"hi\")\n", "phel\\str"))
    }

    /** `:refer` brings names in unqualified, so no qualifier will ever appear for it. */
    fun testReferredImportIsNeverUnused() {
        assertFalse(isUnused("(ns app\\m\n  (:require phel\\str :refer [join]))\n(println \"hi\")\n", "phel\\str"))
    }

    private fun isUnused(text: String, requiredNamespace: String): Boolean {
        // Unique path per class. The index leak of #271 is fixed, but the shared light project
        // still keeps the *files* a class adds, so a shared path would collide across classes.
        val vf = myFixture.addFileToProject("src/unused_import_test.phel", text).virtualFile
        val phelFile = com.intellij.psi.PsiManager.getInstance(project).findFile(vf) as PhelFile
        val symbol = PsiTreeUtil.findChildrenOfType(phelFile, PhelSymbol::class.java)
            .first { it.text == requiredNamespace && PsiTreeUtil.getParentOfType(it, PhelList::class.java) != null }
        return PhelUnusedImportFinder.isUnusedImport(symbol)
    }
}
