package org.phellang.integration.inspection

import com.intellij.codeInspection.InspectionManager
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiRecursiveElementVisitor
import org.phellang.inspection.deprecated.PhelSupersededFormInspection
import org.phellang.integration.PhelIntegrationTestCase
import org.phellang.language.psi.files.PhelFile

/**
 * The forms Phel v0.50.0 superseded (#2877, #2888), and — just as important — the rest of the
 * `php/` family, which it deliberately kept and which must stay silent.
 */
class PhelSupersededFormInspectionTest : PhelIntegrationTestCase() {

    private var fileIndex = 0

    private fun inspect(source: String): List<String> {
        val file = myFixture.configureByText("s${fileIndex++}.phel", "(ns app\\m)\n$source") as PhelFile
        val holder = ProblemsHolder(InspectionManager.getInstance(project), file, true)
        val visitor = PhelSupersededFormInspection().buildVisitor(holder, true)
        file.accept(object : PsiRecursiveElementVisitor() {
            override fun visitElement(element: PsiElement) {
                element.accept(visitor)
                super.visitElement(element)
            }
        })
        return holder.results.map { it.descriptionTemplate }
    }

    fun testPhpNewIsFlaggedWithItsClojureSpelling() {
        val warnings = inspect("(php/new Foo 1)")
        assertTrue("php/new should be flagged: $warnings", warnings.any { it.contains("'php/new'") })
        assertTrue("should name the replacement: $warnings", warnings.any { it.contains("(Foo. arg)") })
    }

    fun testPhpArrowIsFlagged() {
        val warnings = inspect("(php/-> obj (method 1))")
        assertTrue("php/-> should be flagged: $warnings", warnings.any { it.contains("'php/->'") })
    }

    fun testPhpStaticIsFlagged() {
        val warnings = inspect("(php/:: Foo (method 1))")
        assertTrue("php/:: should be flagged: $warnings", warnings.any { it.contains("'php/::'") })
    }

    fun testSetVarIsFlagged() {
        val warnings = inspect("(set-var v 1)")
        assertTrue("set-var should be flagged: $warnings", warnings.any { it.contains("'set-var'") })
        assertTrue("should name alter-var-root: $warnings", warnings.any { it.contains("alter-var-root") })
    }

    fun testInteropFormsWithNoClojureSpellingStaySilent() {
        // Each of these reaches a PHP capability Phel has no other word for, so none is superseded.
        for (form in listOf(
            "(php/aget arr 0)",
            "(php/aset arr 0 1)",
            "(php/apush arr 1)",
            "(php/aunset arr 0)",
            "(php/oset (php/:: Foo slot) 1)",
            "(php/ref x)",
            "(php/callable f)",
        )) {
            val warnings = inspect(form).filterNot { it.contains("'php/::'") }
            assertTrue("$form should stay silent: $warnings", warnings.isEmpty())
        }
    }

    fun testClojureStyleSpellingsStaySilent() {
        for (form in listOf(
            "(Foo. 1)",
            "(new Foo 1)",
            "(.method obj 1)",
            "(.-field obj)",
            "(Foo/method 1)",
            "(println Foo/CONST)",
            "(set! v 1)",
        )) {
            val warnings = inspect(form)
            assertTrue("$form is the current spelling and should stay silent: $warnings", warnings.isEmpty())
        }
    }

    fun testALocalNamedLikeASupersededFormIsNotFlagged() {
        val warnings = inspect("(defn f [set-var]\n  (set-var 1))")
        assertTrue("a local binding shadowing the name is not the form: $warnings", warnings.isEmpty())
    }
}
