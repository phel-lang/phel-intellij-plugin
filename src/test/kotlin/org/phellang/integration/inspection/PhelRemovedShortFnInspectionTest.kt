package org.phellang.integration.inspection

import com.intellij.codeInspection.InspectionManager
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiRecursiveElementVisitor
import org.phellang.indexing.PhelProjectSymbolIndex
import org.phellang.inspection.PhelArityMismatchInspection
import org.phellang.inspection.PhelUnresolvedSymbolInspection
import org.phellang.inspection.deprecated.PhelRemovedShortFnInspection
import org.phellang.integration.PhelIntegrationTestCase
import org.phellang.language.psi.files.PhelFile

/**
 * The `|(...)` short function Phel 0.50 removed (phel-lang #2827). The plugin read it as a `|`
 * symbol and a separate list, as Phel itself does since: the user saw "Cannot resolve symbol '|'"
 * and, around it, a call counted one argument too many.
 */
class PhelRemovedShortFnInspectionTest : PhelIntegrationTestCase() {

    private var fileIndex = 0

    private fun configured(source: String): PhelFile {
        val file = myFixture.configureByText("pipe${fileIndex++}.phel", "(ns app.m)\n$source\n") as PhelFile
        PhelProjectSymbolIndex.getInstance(project).refreshFileFromPsi(file)
        return file
    }

    private fun problems(file: PhelFile, inspection: com.intellij.codeInspection.LocalInspectionTool): List<ProblemDescriptor> {
        val holder = ProblemsHolder(InspectionManager.getInstance(project), file, true)
        val visitor = inspection.buildVisitor(holder, true)
        file.accept(object : PsiRecursiveElementVisitor() {
            override fun visitElement(element: PsiElement) {
                element.accept(visitor)
                super.visitElement(element)
            }
        })
        return holder.results
    }

    private fun removed(source: String) = problems(configured(source), PhelRemovedShortFnInspection())

    fun testAPipeShortFunctionIsReported() {
        val problems = removed("(defn f [xs] (map |(inc $) xs))")

        assertEquals(1, problems.size)
        assertEquals("'|(...)' short functions were removed in Phel 0.50; write #(...) with % placeholders", problems.single().descriptionTemplate)
    }

    fun testWhatOnlyLooksLikeOneIsNotReported() {
        assertEmpty(removed("(defn f [xs] (map #(inc %) xs))"))
        assertEmpty(removed("(defn f [xs] (map | (inc 1) xs))"))
        assertEmpty(removed("(defn f [] (php/| 1 2))"))
    }

    // The quick fix

    private fun fixed(source: String): String {
        val file = configured(source)
        val problem = problems(file, PhelRemovedShortFnInspection()).single()
        WriteCommandAction.runWriteCommandAction(project) { problem.fixes!!.single().applyFix(project, problem) }
        return myFixture.file.text.substringAfter("(ns app.m)\n").trimEnd()
    }

    fun testTheQuickFixRespellsThePlaceholders() {
        assertEquals("(map #(inc %) xs)", fixed("(map |(inc $) xs)"))
        assertEquals("(map #(+ %1 %2) xs ys)", fixed("(map |(+ $1 $2) xs ys)"))
        assertEquals("(map #(apply str %&) xs)", fixed("(map |(apply str $&) xs)"))
    }

    fun testTheQuickFixLeavesStringsAndOtherNamesAlone() {
        assertEquals("(map #(str \"\$\" % \$x) xs)", fixed("(map |(str \"\$\" $ \$x) xs)"))
    }

    fun testNoQuickFixWhenTheBodyHoldsAHashFn() {
        val problem = removed("(map |(map #(inc %) $) xss)").single()

        assertTrue(problem.fixes.isNullOrEmpty())
    }

    // Around it

    /** `match-with` takes three: `|` and its list are one argument, as they were written. */
    fun testTheCallAroundItCountsItAsOneArgument() {
        val file = configured("(defn- match-with [m p f] m)\n(defn g [m p xs] (match-with m p |(get xs $)))")

        assertEmpty(problems(file, PhelArityMismatchInspection()).map { it.descriptionTemplate })
    }

    fun testItsPartsAreNotReportedAsUnresolved() {
        val file = configured("(defn g [xs] (map |(+ $1 $2) xs xs))")

        assertEmpty(problems(file, PhelUnresolvedSymbolInspection()).map { it.descriptionTemplate })
    }
}
