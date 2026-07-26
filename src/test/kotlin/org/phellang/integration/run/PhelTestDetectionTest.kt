package org.phellang.integration.run

import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import org.phellang.integration.PhelIntegrationTestCase
import org.phellang.language.psi.files.PhelFile
import org.phellang.run.test.PhelTestDetection

/**
 * Detection is what decides whether a file gets a green bar at all, so the cases that matter are the
 * spellings real projects use: `phel.test` with dots as often as `phel\test` with backslashes.
 */
class PhelTestDetectionTest : PhelIntegrationTestCase() {

    private fun configure(text: String): PhelFile = myFixture.configureByText("html.phel", text) as PhelFile

    private fun leaves(file: PhelFile): List<PsiElement> =
        PsiTreeUtil.collectElements(file) { it.firstChild == null }.toList()

    private fun headedTests(file: PhelFile): List<String> = leaves(file).mapNotNull(PhelTestDetection::deftestHeadedBy)

    fun testRecognizesATestFileWrittenWithDots() {
        val file = configure("(ns test.html\n  (:require phel.test :refer [deftest is]))\n")

        assertTrue(PhelTestDetection.isTestFile(file))
    }

    fun testRecognizesATestFileWrittenWithBackslashes() {
        val file = configure("(ns test\\html\n  (:require phel\\test :refer [deftest is]))\n")

        assertTrue(PhelTestDetection.isTestFile(file))
    }

    fun testRecognizesATestFileThatAliasesTheRequire() {
        val file = configure("(ns test.html\n  (:require phel\\test :as t))\n")

        assertTrue(PhelTestDetection.isTestFile(file))
    }

    fun testDoesNotRecognizeAFileThatNeverRequiresPhelTest() {
        val file = configure("(ns app.html\n  (:require phel\\str :refer [split]))\n(defn greet [] \"hi\")\n")

        assertFalse(PhelTestDetection.isTestFile(file))
    }

    fun testDoesNotRecognizeAFileWithoutANamespace() {
        assertFalse(PhelTestDetection.isTestFile(configure("(defn greet [] \"hi\")\n")))
    }

    fun testFindsEveryTopLevelTest() {
        val file = configure(
            """
            (ns test.html
              (:require phel.test :refer [deftest is]))

            (deftest basic-tags
              (is (= "<div></div>" (html [:div]))))

            (deftest empty-tags
              (is (= "<h1></h1>" (html [:h1]))))
            """.trimIndent()
        )

        assertEquals(listOf("basic-tags", "empty-tags"), headedTests(file))
    }

    fun testFindsATestHeadedByAnAliasedDeftest() {
        val file = configure(
            "(ns test.html\n  (:require phel\\test :as t))\n\n(t/deftest basic-tags\n  (t/is true))\n"
        )

        assertEquals(listOf("basic-tags"), headedTests(file))
    }

    /** `#_` leaves the discarded form in the tree, so a naive `forms[1]` would report `old`. */
    fun testTakesTheNameAfterADiscardedForm() {
        val file = configure(
            "(ns test.html\n  (:require phel.test :refer [deftest is]))\n\n(deftest #_old new-name\n  (is true))\n"
        )

        assertEquals(listOf("new-name"), headedTests(file))
    }

    fun testDoesNotMarkTestsInAFileThatIsNotATestFile() {
        val file = configure("(ns app.html)\n\n(deftest basic-tags\n  (is true))\n")

        assertEmpty(headedTests(file))
    }

    fun testDoesNotMarkANestedDeftest() {
        val file = configure(
            "(ns test.html\n  (:require phel.test :refer [deftest is]))\n\n(comment (deftest inner (is true)))\n"
        )

        assertEmpty(headedTests(file))
    }

    fun testDoesNotMarkADeftestWithNoName() {
        val file = configure("(ns test.html\n  (:require phel.test :refer [deftest is]))\n\n(deftest)\n")

        assertEmpty(headedTests(file))
    }

    fun testFindsTheTestSurroundingAnInnerElement() {
        val file = configure(
            """
            (ns test.html
              (:require phel.test :refer [deftest is]))

            (deftest basic-tags
              (is (= "<div></div>" (html [:div]))))
            """.trimIndent()
        )

        val inner = leaves(file).single { it.text == "html" && it.textOffset > 60 }

        assertEquals("basic-tags", PhelTestDetection.enclosingDeftestName(inner))
    }

    fun testHasNoSurroundingTestOutsideOne() {
        val file = configure(
            "(ns test.html\n  (:require phel.test :refer [deftest is]))\n\n(defn helper [] 1)\n"
        )

        val inner = leaves(file).single { it.text == "helper" }

        assertNull(PhelTestDetection.enclosingDeftestName(inner))
    }
}
