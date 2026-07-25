package org.phellang.integration.inspection

import org.phellang.integration.PhelIntegrationTestCase
import org.phellang.inspection.PhelUnresolvedSymbolInspection

/**
 * The quick fix that creates a missing function.
 *
 * Placement is the part that matters: Phel resolves a symbol against the definitions preceding it,
 * so `(defn caller [] (callee))` written above `(defn callee [] 42)` fails to compile with
 * `PHEL001 Cannot resolve symbol 'callee'`. A fix that appended would produce code as broken as the
 * code it was fixing.
 */
class PhelCreateFunctionQuickFixTest : PhelIntegrationTestCase() {

    private var fileIndex = 0

    private fun applyFix(source: String): String? {
        myFixture.enableInspections(PhelUnresolvedSymbolInspection())
        myFixture.configureByText("q${fileIndex++}.phel", "(ns my\\app)\n$source")

        val fix = myFixture.getAllQuickFixes().firstOrNull { it.familyName == "Create function" } ?: return null
        myFixture.launchAction(fix)

        return myFixture.file.text
    }

    fun testCreatesTheFunctionAboveItsCaller() {
        val result = applyFix("(defn demoing [] (asdfasdfasdf))")

        assertNotNull("expected a Create function fix", result)
        val definition = result!!.indexOf("(defn asdfasdfasdf")
        val caller = result.indexOf("(defn demoing")

        assertTrue("the new function must exist:\n$result", definition >= 0)
        assertTrue("it must precede its caller, or Phel cannot resolve it:\n$result", definition < caller)
    }

    fun testTakesParameterNamesFromTheCallArguments() {
        val result = applyFix("(defn demoing [user count] (greet user count))")

        assertTrue("expected [user count], got:\n$result", result!!.contains("(defn greet [user count]"))
    }

    /** An argument that is not a plain symbol has no name to borrow. */
    fun testFallsBackToPositionalNames() {
        val result = applyFix("(defn demoing [n] (compute (dec n) 42))")

        assertTrue("expected positional names, got:\n$result", result!!.contains("(defn compute [arg1 arg2]"))
    }

    fun testRepeatedArgumentDoesNotProduceDuplicateParameters() {
        val result = applyFix("(defn demoing [x] (pair x x))")

        assertTrue("parameters must be distinct, got:\n$result", result!!.contains("(defn pair [x arg2]"))
    }

    fun testCreatesANoArgumentFunction() {
        val result = applyFix("(defn demoing [] (setup))")

        assertTrue("expected an empty vector, got:\n$result", result!!.contains("(defn setup []"))
    }

    /** In an argument slot the name could want a `def`, so the fix must not guess. */
    fun testNoFixIsOfferedOutsideCallPosition() {
        myFixture.enableInspections(PhelUnresolvedSymbolInspection())
        myFixture.configureByText("q${fileIndex++}.phel", "(ns my\\app)\n(defn demoing [] (println asdfasdfasdf))")

        val fixes = myFixture.getAllQuickFixes().map { it.familyName }

        assertFalse("no Create function outside a call: $fixes", fixes.contains("Create function"))
    }
}
