package org.phellang.integration.inspection

import com.intellij.psi.util.PsiTreeUtil
import org.phellang.integration.PhelIntegrationTestCase
import org.phellang.annotator.validators.PhelImportValidator
import org.phellang.language.psi.PhelList
import org.phellang.language.psi.PhelSymbol
import org.phellang.language.psi.files.PhelFile

/**
 * Behavioural coverage for [PhelImportValidator.isUnusedImport], guarding the cached
 * used-qualifier scan: a required namespace is "unused" only when no `ns/...` call in the
 * file body references it.
 */
class PhelUnusedImportTest : PhelIntegrationTestCase() {

    fun testUsedImportIsNotUnused() {
        assertFalse(isUnused("(ns app\\m\n  (:require phel\\str))\n(str/join \", \" [1 2])\n", "phel\\str"))
    }

    fun testUnusedImportIsUnused() {
        assertTrue(isUnused("(ns app\\m\n  (:require phel\\str))\n(println \"hi\")\n", "phel\\str"))
    }

    private fun isUnused(text: String, requiredNamespace: String): Boolean {
        // Unique path per class — the shared test project does not reliably clean files
        // between classes, so a shared path would risk cross-test interference.
        val vf = myFixture.addFileToProject("src/unused_import_test.phel", text).virtualFile
        val phelFile = com.intellij.psi.PsiManager.getInstance(project).findFile(vf) as PhelFile
        val symbol = PsiTreeUtil.findChildrenOfType(phelFile, PhelSymbol::class.java)
            .first { it.text == requiredNamespace && PsiTreeUtil.getParentOfType(it, PhelList::class.java) != null }
        return PhelImportValidator.isUnusedImport(symbol)
    }
}
