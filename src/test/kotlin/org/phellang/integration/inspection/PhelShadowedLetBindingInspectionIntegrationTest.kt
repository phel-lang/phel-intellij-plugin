package org.phellang.integration.inspection

import com.intellij.codeInspection.InspectionManager
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiRecursiveElementVisitor
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import org.phellang.inspection.PhelShadowedLetBindingInspection
import org.phellang.language.psi.files.PhelFile

/**
 * Fixture-level checks for the shadowed-let-binding inspection. Guards against the
 * inspection silently doing nothing — simple symbols parse as PhelAccess, so the head
 * and binding casts have to unwrap them for the inspection to fire at all.
 */
class PhelShadowedLetBindingInspectionIntegrationTest : BasePlatformTestCase() {

    fun testInnerLetShadowsOuterLet() {
        val warnings = inspect("(ns app\\m)\n(defn f []\n  (let [x 1]\n    (let [x 2]\n      x)))\n")
        assertEquals(listOf("Binding 'x' shadows an outer binding."), warnings)
    }

    fun testLetShadowsFunctionParameter() {
        val warnings = inspect("(ns app\\m)\n(defn f [x]\n  (let [x 2]\n    x))\n")
        assertEquals(listOf("Binding 'x' shadows an outer binding."), warnings)
    }

    fun testNoShadowingWhenNamesDiffer() {
        val warnings = inspect("(ns app\\m)\n(defn f []\n  (let [x 1]\n    (let [y 2]\n      y)))\n")
        assertTrue("distinct names should not be flagged: $warnings", warnings.isEmpty())
    }

    private fun inspect(text: String): List<String> {
        val file = myFixture.configureByText("a.phel", text) as PhelFile
        val holder = ProblemsHolder(InspectionManager.getInstance(project), file, true)
        val visitor = PhelShadowedLetBindingInspection().buildVisitor(holder, true)
        file.accept(object : PsiRecursiveElementVisitor() {
            override fun visitElement(element: PsiElement) {
                element.accept(visitor)
                super.visitElement(element)
            }
        })
        return holder.results.map { it.descriptionTemplate }
    }
}
