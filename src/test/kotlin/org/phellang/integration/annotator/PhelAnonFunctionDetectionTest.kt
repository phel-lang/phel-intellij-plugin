package org.phellang.integration.annotator

import com.intellij.psi.util.PsiTreeUtil
import org.phellang.annotator.analyzers.PhelCommentAnalyzer
import org.phellang.integration.PhelIntegrationTestCase
import org.phellang.language.psi.PhelSymbol

/**
 * Exercises `isInsideAnonFunction` against real PSI: the anonymous-function walk over an actual
 * `PhelHashFn`, and the `#(`-in-file fast path — the branch the mock-based unit test cannot reach
 * because a mocked element has no containing file.
 */
class PhelAnonFunctionDetectionTest : PhelIntegrationTestCase() {

    private fun insideAnonFn(source: String): Map<String, Boolean> {
        val file = myFixture.configureByText("a.phel", source)
        return PsiTreeUtil.findChildrenOfType(file, PhelSymbol::class.java)
            .associate { it.text to PhelCommentAnalyzer.isInsideAnonFunction(it) }
    }

    fun testSymbolsInsideAnonFunctionAreDetected() {
        val result = insideAnonFn("(mapv #(strcat aa bb) coll)")

        assertEquals(true, result["strcat"])
        assertEquals(true, result["aa"])
        assertEquals(true, result["bb"])
        // Outside the #() — the walk runs (the file contains `#(`) but finds no PhelHashFn ancestor.
        assertEquals(false, result["mapv"])
        assertEquals(false, result["coll"])
    }

    fun testFastPathWhenFileHasNoAnonFunction() {
        // No `#(` anywhere, so isInsideAnonFunction short-circuits before walking ancestors.
        val result = insideAnonFn("(mapv inc coll)")

        assertEquals(false, result["mapv"])
        assertEquals(false, result["inc"])
        assertEquals(false, result["coll"])
    }
}
