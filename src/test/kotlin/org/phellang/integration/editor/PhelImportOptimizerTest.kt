package org.phellang.integration.editor

import com.intellij.codeInsight.actions.OptimizeImportsProcessor
import org.phellang.integration.PhelIntegrationTestCase

/**
 * Optimize Imports over a `.phel` file.
 *
 * Driven through [OptimizeImportsProcessor], the action's own entry point, so the registration is
 * exercised rather than the optimizer called directly.
 *
 * What counts as unused is `PhelUnusedImportFinder`, shared with `PhelUnusedImportInspection`, so
 * the cases here are about *removing several at once* and about leaving the file valid — the
 * judgement itself is already covered by `PhelUnusedImportTest`.
 */
class PhelImportOptimizerTest : PhelIntegrationTestCase() {

    private var fileIndex = 0

    private fun optimized(source: String): String {
        val file = myFixture.configureByText("opt${fileIndex++}.phel", source)
        OptimizeImportsProcessor(project, file).run()

        return file.text
    }

    fun testRemovesAnUnusedRequire() {
        val optimized = optimized(
            "(ns app\\m\n  (:require phel\\str :as str))\n\n(defn f [] 1)\n"
        )

        assertFalse("the unused require should be gone: $optimized", optimized.contains("phel\\str"))
    }

    fun testKeepsARequireThatIsUsed() {
        val source = "(ns app\\m\n  (:require phel\\str :as str))\n\n(defn f [] (str/join \",\" []))\n"

        assertEquals(source, optimized(source))
    }

    /** The whole point of the action over the per-import quick fix: several go in one pass. */
    fun testRemovesEveryUnusedRequireInOnePass() {
        val optimized = optimized(
            "(ns app\\m\n" +
                "  (:require phel\\str :as str)\n" +
                "  (:require phel\\json :as json)\n" +
                "  (:require phel\\html :as html))\n\n" +
                "(defn f [] 1)\n"
        )

        assertFalse(optimized, optimized.contains("phel\\str"))
        assertFalse(optimized, optimized.contains("phel\\json"))
        assertFalse(optimized, optimized.contains("phel\\html"))
    }

    fun testKeepsTheUsedOnesWhileRemovingTheRest() {
        val optimized = optimized(
            "(ns app\\m\n" +
                "  (:require phel\\str :as str)\n" +
                "  (:require phel\\json :as json))\n\n" +
                "(defn f [] (json/encode {}))\n"
        )

        assertTrue("the used require must survive: $optimized", optimized.contains("phel\\json"))
        assertFalse("the unused one must go: $optimized", optimized.contains("phel\\str"))
    }

    /** A `:refer` pulls names in unqualified, so no qualifier ever appears for it. */
    fun testKeepsAReferRequire() {
        val source = "(ns app\\m\n  (:require phel\\test :refer [deftest is]))\n\n(defn f [] 1)\n"

        assertEquals(source, optimized(source))
    }

    fun testLeavesAFileWithNoRequiresAlone() {
        val source = "(ns app\\m)\n\n(defn f [] 1)\n"

        assertEquals(source, optimized(source))
    }

    /** The namespace's own name is namespace-shaped and never a qualifier in its own file. */
    fun testNeverRemovesTheNamespaceDeclarationItself() {
        val optimized = optimized("(ns app\\m\n  (:require phel\\str :as str))\n\n(defn f [] 1)\n")

        assertTrue("the ns declaration must survive: $optimized", optimized.contains("(ns app\\m"))
    }
}
