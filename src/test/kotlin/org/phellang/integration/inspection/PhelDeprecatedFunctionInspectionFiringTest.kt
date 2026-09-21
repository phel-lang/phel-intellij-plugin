package org.phellang.integration.inspection

import com.intellij.codeInspection.InspectionManager
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiRecursiveElementVisitor
import org.phellang.fixtures.PhelDeprecatedFunctionFixtures
import org.phellang.integration.PhelIntegrationTestCase
import org.phellang.inspection.deprecated.PhelDeprecatedFunctionInspection
import org.phellang.language.psi.files.PhelFile
import org.phellang.registry.PhelFunctionRegistry

/**
 * Confirms the deprecated-function inspection actually fires against parsed PSI (it visits
 * symbols, so PhelAccess wrapping doesn't hide them) and skips local bindings that shadow a
 * deprecated core name.
 */
class PhelDeprecatedFunctionInspectionFiringTest : PhelIntegrationTestCase() {

    // Phel v0.50.0 deleted every deprecated function, so 'put' is supplied as a fixture; the
    // inspection under test still reaches it through the ordinary registry lookup.
    override fun setUp() {
        super.setUp()
        PhelFunctionRegistry.installTestFunctions(PhelDeprecatedFunctionFixtures.ALL)
    }

    override fun tearDown() {
        try {
            PhelFunctionRegistry.clearTestFunctions()
        } finally {
            super.tearDown()
        }
    }

    fun testDeprecatedCallIsFlagged() {
        val warnings = inspect("(ns app\\m)\n(put {} :a 1)\n")
        assertTrue("deprecated 'put' should be flagged, got $warnings", warnings.any { it.contains("put") })
    }

    /**
     * The one deprecation the 0.52.0 registry carries for real: Phel 0.51.0 deprecated
     * `to-php-array` in favour of `to-array` (phel-lang #3076). It comes from the generated data,
     * not from the fixtures, so this pins that the regenerated registry still feeds the inspection.
     */
    fun testToPhpArrayIsReportedDeprecatedWithItsReplacement() {
        val warnings = inspect("(ns app\\m)\n(to-php-array [1 2 3])\n")
        assertEquals(listOf("'to-php-array' is deprecated since 0.51.0. Use 'to-array' instead"), warnings)
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
