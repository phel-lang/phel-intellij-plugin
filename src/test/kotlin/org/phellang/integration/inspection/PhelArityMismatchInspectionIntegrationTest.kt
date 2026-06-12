package org.phellang.integration.inspection

import com.intellij.codeInspection.InspectionManager
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiRecursiveElementVisitor
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import org.phellang.inspection.PhelArityMismatchInspection
import org.phellang.language.psi.files.PhelFile

class PhelArityMismatchInspectionIntegrationTest : BasePlatformTestCase() {

    fun testWrongArityIsFlagged() {
        val warnings = inspect("(ns app\\m)\n(defn f [x] x)\n(f 1 2)\n")
        assertTrue("calling f/1 with 2 args should be flagged, got $warnings", warnings.any { it.contains("'f'") })
    }

    fun testCorrectArityIsNotFlagged() {
        val warnings = inspect("(ns app\\m)\n(defn f [x] x)\n(f 1)\n")
        assertTrue("correct arity should not be flagged: $warnings", warnings.isEmpty())
    }

    fun testVariadicAcceptsExtraArgs() {
        val warnings = inspect("(ns app\\m)\n(defn h [a & rest] a)\n(h 1 2 3)\n")
        assertTrue("variadic call should not be flagged: $warnings", warnings.isEmpty())
    }

    fun testThreadingMacroArgIsNotFlagged() {
        // (g 2) textually has one arg, but `->` threads `1` in as the first — the check
        // must skip threaded call sites rather than miscount.
        val warnings = inspect("(ns app\\m)\n(defn g [a b] a)\n(-> 1 (g 2))\n")
        assertTrue("threaded call should not be flagged: $warnings", warnings.isEmpty())
    }

    fun testCommonStdlibCallsAreNotFlagged() {
        // A realistic mix of correct-arity core calls must produce zero warnings,
        // guarding the now-active inspection against false positives on the stdlib.
        val warnings = inspect(
            """
            (ns app\m)
            (defn run [xs m]
              (map inc xs)
              (filter even? xs)
              (reduce + 0 xs)
              (println "hi" xs)
              (str "a" "b" "c")
              (get m :k)
              (assoc m :k 1)
              (count xs))
            """.trimIndent() + "\n"
        )
        assertTrue("common stdlib calls should not be flagged, got $warnings", warnings.isEmpty())
    }

    fun testLocallyShadowedNameIsNotFlagged() {
        // `inc` is locally rebound, so the registry arity for core/inc must not apply.
        val warnings = inspect("(ns app\\m)\n(defn f []\n  (let [inc (fn [a b] a)]\n    (inc 1 2)))\n")
        assertTrue("locally shadowed name should not be flagged: $warnings", warnings.isEmpty())
    }

    private fun inspect(text: String): List<String> {
        val file = myFixture.configureByText("a.phel", text) as PhelFile
        val holder = ProblemsHolder(InspectionManager.getInstance(project), file, true)
        val visitor = PhelArityMismatchInspection().buildVisitor(holder, true)
        file.accept(object : PsiRecursiveElementVisitor() {
            override fun visitElement(element: PsiElement) {
                element.accept(visitor)
                super.visitElement(element)
            }
        })
        return holder.results.map { it.descriptionTemplate }
    }
}
