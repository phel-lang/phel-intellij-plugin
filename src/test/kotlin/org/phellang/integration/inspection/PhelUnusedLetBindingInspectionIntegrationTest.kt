package org.phellang.integration.inspection

import com.intellij.codeInspection.InspectionManager
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiRecursiveElementVisitor
import org.phellang.integration.PhelIntegrationTestCase
import org.phellang.inspection.PhelUnusedLetBindingInspection
import org.phellang.language.psi.files.PhelFile

/**
 * Fixture-level checks for the unused-let-binding inspection. In particular guards the
 * regression where a binding used only as a *bare symbol value* of a later binding —
 * `(let [x 1 y x] ...)` — was wrongly flagged as unused.
 */
class PhelUnusedLetBindingInspectionIntegrationTest : PhelIntegrationTestCase() {

    fun testBareSymbolValueCountsAsUsage() {
        val warnings = inspect("(ns app\\m)\n(defn f []\n  (let [x 1 y x]\n    (println y)))\n")
        assertTrue("x is used as y's value, so nothing should be flagged: $warnings", warnings.isEmpty())
    }

    fun testTrulyUnusedBindingIsFlagged() {
        val warnings = inspect("(ns app\\m)\n(defn f []\n  (let [unused 1]\n    42))\n")
        assertEquals(listOf("Binding 'unused' is never used."), warnings)
    }

    fun testUsedBindingIsNotFlagged() {
        val warnings = inspect("(ns app\\m)\n(defn f []\n  (let [used 1]\n    (println used)))\n")
        assertTrue("used binding should not be flagged: $warnings", warnings.isEmpty())
    }

    fun testUnderscorePrefixedBindingIsIgnored() {
        val warnings = inspect("(ns app\\m)\n(defn f []\n  (let [_ignored 1]\n    42))\n")
        assertTrue("intentionally-unused _-prefixed binding should be ignored: $warnings", warnings.isEmpty())
    }

    /** A `#_` before the binding vector must not hide the let form from the inspection. */
    fun testDiscardBeforeBindingVectorStillInspects() {
        val warnings = inspect("(ns app\\m)\n(defn f []\n  (let #_junk [unused 1]\n    42))\n")
        assertEquals(listOf("Binding 'unused' is never used."), warnings)
    }

    /** A `#_`-discarded binding must not shift the name/value pairing in the binding vector. */
    fun testDiscardInsideBindingVectorKeepsPairingAligned() {
        // Active bindings are `[used 1]`: `used` is the name, `1` the value, and it is used.
        val warnings = inspect("(ns app\\m)\n(defn f []\n  (let [#_gap used 1]\n    (println used)))\n")
        assertTrue("pairing must stay aligned past the discard: $warnings", warnings.isEmpty())
    }

    /** Runs the inspection's visitor over every element and returns the reported messages. */
    private fun inspect(text: String): List<String> {
        val file = myFixture.configureByText("a.phel", text) as PhelFile
        val holder = ProblemsHolder(InspectionManager.getInstance(project), file, true)
        val visitor = PhelUnusedLetBindingInspection().buildVisitor(holder, true)
        file.accept(object : PsiRecursiveElementVisitor() {
            override fun visitElement(element: PsiElement) {
                element.accept(visitor)
                super.visitElement(element)
            }
        })
        return holder.results.map { it.descriptionTemplate }
    }
}
