package org.phellang.integration.inspection

import com.intellij.codeInspection.InspectionManager
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiRecursiveElementVisitor
import org.phellang.integration.PhelIntegrationTestCase
import org.phellang.registry.indexing.PhelProjectSymbolIndex
import org.phellang.inspection.PhelArityMismatchInspection
import org.phellang.language.psi.files.PhelFile

class PhelArityMismatchInspectionIntegrationTest : PhelIntegrationTestCase() {

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

    fun testQualifiedUserlandCallPrefersOwnNamespaceOverStdlib() {
        // `count` is a 1-arg stdlib fn, but here `m/count` is a userland 3-arg fn in this
        // file's namespace. The qualified call must resolve to the userland arity, not the
        // same-named stdlib function -- otherwise we'd wrongly flag a correct call.
        val warnings = inspect("(ns app\\m)\n(defn count [a b c] a)\n(m/count 1 2 3)\n")
        assertTrue("qualified userland call should resolve to its own namespace: $warnings", warnings.isEmpty())
    }

    fun testQualifiedUserlandCallWithWrongArityIsFlagged() {
        val warnings = inspect("(ns app\\m)\n(defn ff [a b] a)\n(m/ff 1)\n")
        assertTrue("wrong-arity qualified userland call should be flagged: $warnings", warnings.any { it.contains("'m/ff'") })
    }

    fun testVariadicStdlibApplyIsNotFlagged() {
        // `apply` is variadic ((apply f expr*)); both the 2-arg and 4-arg forms are valid
        // and must not be flagged. Regression guard for the `*`-suffix variadic notation.
        val warnings = inspect(
            "(ns app\\m)\n(apply + [1 2 3])\n(apply + 1 2 [3])\n(apply + 1 2 3 4 [5])\n"
        )
        assertTrue("variadic apply calls should not be flagged: $warnings", warnings.isEmpty())
    }

    fun testVariadicStdlibUpdateIsNotFlagged() {
        // `update` is (update ds k f & args): 3 required + variadic rest. Both the docs'
        // 3-arg form and an extra-arg form are valid.
        val warnings = inspect(
            "(ns app\\m)\n(update {:count 5} :count inc)\n(update m :k + 1 2)\n"
        )
        assertTrue("variadic update calls should not be flagged: $warnings", warnings.isEmpty())
    }

    fun testVariadicStdlibUpdateBelowMinArityIsFlagged() {
        // Fewer than the 3 required fixed args (ds k f) must still be flagged.
        val warnings = inspect("(ns app\\m)\n(update {:count 5} :count)\n")
        assertTrue("update with too few args should be flagged: $warnings", warnings.any { it.contains("'update'") })
    }

    fun testMultiArityStdlibConjIsNotFlagged() {
        // conj has 4 arities: (conj) (conj coll) (conj coll value) (conj coll value & more).
        // Every shape must be accepted -- previously only the first arity was stored.
        val warnings = inspect(
            """
            (ns app\m)
            (conj)
            (conj [])
            (conj [1 2] 3)
            (conj [1 2] 3 4 5)
            """.trimIndent() + "\n"
        )
        assertTrue("all conj arities should be accepted: $warnings", warnings.isEmpty())
    }

    fun testMultiArityStdlibMergeIsNotFlagged() {
        // merge: (merge) (merge map) (merge map & more)
        val warnings = inspect("(ns app\\m)\n(merge)\n(merge {:a 1})\n(merge {:a 1} {:b 2} {:c 3})\n")
        assertTrue("all merge arities should be accepted: $warnings", warnings.isEmpty())
    }

    fun testDiscardedArgIsNotCounted() {
        // `#_skip` discards the next form, so the real call is (push coll x) -- 2 args.
        // The discarded form must not be counted as an argument.
        val warnings = inspect("(ns app\\m)\n(push #_skip coll x)\n")
        assertTrue("discarded arg before should not be counted: $warnings", warnings.isEmpty())
    }

    fun testTrailingDiscardedArgIsNotCounted() {
        val warnings = inspect("(ns app\\m)\n(push coll x #_extra)\n")
        assertTrue("trailing discarded arg should not be counted: $warnings", warnings.isEmpty())
    }

    fun testStackedDiscardsAreNotCounted() {
        // `#_#_` discards the next two forms; (conj coll a b) survives with a 4-arity match.
        val warnings = inspect("(ns app\\m)\n(conj coll a b #_#_ x y)\n")
        assertTrue("stacked discards should not be counted: $warnings", warnings.isEmpty())
    }

    fun testDiscardTurningValidCallInvalidIsFlagged() {
        // Discarding a required arg makes (push coll) a 1-arg call -- still wrong.
        val warnings = inspect("(ns app\\m)\n(push coll #_x)\n")
        assertTrue("discard dropping a required arg should be flagged: $warnings", warnings.any { it.contains("'push'") })
    }

    fun testShortFnArgIsNotMiscounted() {
        // Phel's deprecated `|(...)` short-fn is a single anonymous-function argument.
        // (some |(> $ 10) coll) is a 2-arg call, not 3.
        val warnings = inspect("(ns app\\m)\n(some |(> $ 10) coll)\n")
        assertTrue("|(...) short-fn arg should count as one form: $warnings", warnings.isEmpty())
    }

    fun testHashFnArgIsNotMiscounted() {
        // The modern `#(...)` short-fn must likewise count as a single argument.
        val warnings = inspect("(ns app\\m)\n(some #(when (> % 10) %) [5 15 8])\n")
        assertTrue("#(...) short-fn arg should count as one form: $warnings", warnings.isEmpty())
    }

    fun testGenuineExtraArgIsStillFlagged() {
        // some is 2-arg; a real 3-arg call must still be flagged.
        val warnings = inspect("(ns app\\m)\n(some pred coll extra)\n")
        assertTrue("genuine 3-arg call to 2-arg fn should be flagged: $warnings", warnings.any { it.contains("'some'") })
    }

    fun testUserlandFnWithVectorHeadedBodyKeepsItsArity() {
        // `([10 20 30] i)` is a vector used as a function in the body of a 1-arg fn.
        // The scanner must not mistake that body list for a multi-arity clause, which
        // would make `idx` look like a 3-arg fn and flag the correct 1-arg call.
        val warnings = inspect("(ns app\\m)\n(defn idx [i] ([10 20 30] i))\n(idx 1)\n")
        assertTrue("single-arity fn with vector-headed body should not be flagged: $warnings", warnings.isEmpty())
    }

    fun testUserlandMultiArityFnAcceptsAllArities() {
        // Genuine multi-arity userland fn: both 1-arg and 2-arg calls are valid.
        val warnings = inspect("(ns app\\m)\n(defn f ([x] x) ([x y] y))\n(f 1)\n(f 1 2)\n")
        assertTrue("both arities of a multi-arity userland fn should be accepted: $warnings", warnings.isEmpty())
    }

    private fun inspect(text: String): List<String> {
        val file = myFixture.configureByText("a.phel", text) as PhelFile
        // Populate the project symbol index directly from this file's PSI. The lazy
        // FilenameIndex-based build is order-sensitive across the shared light-fixture
        // project (only the first index-dependent test in a class sees it built),
        // so priming here keeps namespace-qualified resolution deterministic.
        PhelProjectSymbolIndex.getInstance(project).refreshFileFromPsi(file)
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
