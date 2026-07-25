package org.phellang.integration.annotator

import com.intellij.psi.PsiManager
import com.intellij.psi.util.PsiTreeUtil
import org.phellang.annotator.validators.PhelImportValidator
import org.phellang.integration.PhelIntegrationTestCase
import org.phellang.language.psi.PhelList
import org.phellang.language.psi.PhelSymbol
import org.phellang.language.psi.files.PhelFile

/**
 * What the annotator still reports about an import, and what it must no longer report.
 *
 * The unused-import check became `PhelUnusedImportInspection` so it could be switched off from
 * Settings. If the annotator kept reporting it too, every unused import would be flagged twice, on
 * the same symbol, with only one of the two responding to the setting.
 */
class PhelImportValidatorTest : PhelIntegrationTestCase() {

    private var fileIndex = 0

    private fun problemsFor(text: String, namespace: String, occurrence: Int = 0): List<String> {
        val file = myFixture.configureByText("v${fileIndex++}.phel", text) as PhelFile
        val symbol = PsiTreeUtil.findChildrenOfType(file, PhelSymbol::class.java)
            .filter { it.text == namespace && PsiTreeUtil.getParentOfType(it, PhelList::class.java) != null }[occurrence]

        return PhelImportValidator.validateImport(symbol).map { it.message }
    }

    fun testDoesNotReportAnUnusedImport() {
        val problems = problemsFor("(ns app\\a\n  (:require phel\\string))\n(println \"hi\")\n", "phel\\string")

        assertEmpty("the unused-import check belongs to PhelUnusedImportInspection now", problems)
    }

    /** Reported on the second occurrence: the first one is the original, not the duplicate. */
    fun testStillReportsADuplicateImport() {
        val source = "(ns app\\a\n  (:require phel\\string)\n  (:require phel\\string))\n(string/join \"\" [])\n"

        val duplicate = problemsFor(source, "phel\\string", occurrence = 1)

        assertTrue(duplicate.toString(), duplicate.any { it.startsWith("Duplicate import") })
    }

    fun testDoesNotReportTheOriginalAsADuplicate() {
        val source = "(ns app\\a\n  (:require phel\\string)\n  (:require phel\\string))\n(string/join \"\" [])\n"

        val original = problemsFor(source, "phel\\string", occurrence = 0)

        assertEmpty(original)
    }

    fun testStillReportsANamespaceThatDoesNotExist() {
        val problems = problemsFor("(ns app\\a\n  (:require totally\\missing))\n(missing/go)\n", "totally\\missing")

        assertTrue(problems.toString(), problems.any { it.contains("does not exist") })
    }

    fun testSaysNothingAboutAUsedStandardLibraryImport() {
        val problems = problemsFor(
            "(ns app\\a\n  (:require phel\\string))\n(string/join \", \" [1 2])\n",
            "phel\\string",
        )

        assertEmpty(problems)
    }

    /** Regression guard: the file used to also be reachable through PsiManager in this shape. */
    fun testWorksOnAFileOpenedThroughPsiManager() {
        val vf = myFixture.addFileToProject(
            "src/validator_probe.phel",
            "(ns app\\probe\n  (:require phel\\string))\n(println \"hi\")\n",
        ).virtualFile
        val file = PsiManager.getInstance(project).findFile(vf) as PhelFile
        val symbol = PsiTreeUtil.findChildrenOfType(file, PhelSymbol::class.java).first { it.text == "phel\\string" }

        assertEmpty(PhelImportValidator.validateImport(symbol).map { it.message })
    }
}
