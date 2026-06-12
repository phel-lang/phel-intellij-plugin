package org.phellang.integration.inspection

import com.intellij.codeInspection.InspectionManager
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiRecursiveElementVisitor
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import org.phellang.inspection.deprecated.PhelDeprecatedFunctionInspection
import org.phellang.language.psi.files.PhelFile

/**
 * Confirms the deprecated-function inspection actually fires against parsed PSI (it visits
 * symbols, so PhelAccess wrapping doesn't hide them) and skips local bindings that shadow a
 * deprecated core name.
 */
class PhelDeprecatedFunctionInspectionFiringTest : BasePlatformTestCase() {

    fun testDeprecatedCallIsFlagged() {
        val warnings = inspect("(ns app\\m)\n(put {} :a 1)\n")
        assertTrue("deprecated 'put' should be flagged, got $warnings", warnings.any { it.contains("put") })
    }

    fun testLocallyBoundNameShadowingDeprecatedIsNotFlagged() {
        val warnings = inspect("(ns app\\m)\n(defn f [put]\n  (put 1))\n")
        assertTrue("local param shadowing 'put' should not be flagged: $warnings", warnings.isEmpty())
    }

    private fun inspect(text: String): List<String> {
        val file = myFixture.configureByText("a.phel", text) as PhelFile
        val holder = ProblemsHolder(InspectionManager.getInstance(project), file, true)
        val visitor = PhelDeprecatedFunctionInspection().buildVisitor(holder, true)
        file.accept(object : PsiRecursiveElementVisitor() {
            override fun visitElement(element: PsiElement) {
                element.accept(visitor)
                super.visitElement(element)
            }
        })
        return holder.results.map { it.descriptionTemplate }
    }
}
