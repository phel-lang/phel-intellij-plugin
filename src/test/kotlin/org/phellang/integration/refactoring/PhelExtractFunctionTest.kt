package org.phellang.integration.refactoring

import com.intellij.refactoring.util.CommonRefactoringUtil
import org.phellang.integration.PhelIntegrationTestCase
import org.phellang.refactoring.extract.PhelExtractFunctionHandler

/**
 * Extract Function.
 *
 * The interesting part is the **parameter list**, which is derived rather than asked for: the locals
 * the expression uses but does not bind become its parameters, and everything else it references —
 * globals, stdlib, required names — resolves the same from the new function's position.
 */
class PhelExtractFunctionTest : PhelIntegrationTestCase() {

    private val handler = PhelExtractFunctionHandler()
    private var fileIndex = 0

    private fun extracted(source: String): String {
        myFixture.configureByText("ef${fileIndex++}.phel", source)
        handler.invoke(project, myFixture.editor, myFixture.file, null)

        return myFixture.editor.document.text
    }

    // ---- the parameter list ----

    /** Nothing local is used, so the function takes nothing. */
    fun testTakesNoParametersWhenTheExpressionIsSelfContained() {
        val extracted = extracted("(ns app\\m)\n(defn f [] <selection>(* 2 3)</selection>)\n")

        assertEquals("(ns app\\m)\n(defn extracted []\n  (* 2 3))\n\n(defn f [] (extracted))\n", extracted)
    }

    /** A parameter of the enclosing function is a local the expression does not bind. */
    fun testTakesAnEnclosingParameterAsAParameter() {
        val extracted = extracted("(ns app\\m)\n(defn f [n] <selection>(* n 2)</selection>)\n")

        assertEquals("(ns app\\m)\n(defn extracted [n]\n  (* n 2))\n\n(defn f [n] (extracted n))\n", extracted)
    }

    fun testTakesALetBindingFromOutsideTheSelection() {
        val extracted = extracted("(ns app\\m)\n(defn f [] (let [a 1] <selection>(+ a 2)</selection>))\n")

        assertEquals("(ns app\\m)\n(defn extracted [a]\n  (+ a 2))\n\n(defn f [] (let [a 1] (extracted a)))\n", extracted)
    }

    /** In first-appearance order, so the signature reads like the expression does. */
    fun testTakesSeveralLocalsInTheOrderTheyAppear() {
        val extracted = extracted("(ns app\\m)\n(defn f [a b] <selection>(+ b a)</selection>)\n")

        assertEquals("(ns app\\m)\n(defn extracted [b a]\n  (+ b a))\n\n(defn f [a b] (extracted b a))\n", extracted)
    }

    /** A name bound *inside* the selection travels with it and is not passed in. */
    fun testDoesNotTakeALetBoundInsideTheSelection() {
        val extracted = extracted("(ns app\\m)\n(defn f [] <selection>(let [a 1] (+ a 2))</selection>)\n")

        assertEquals(
            "(ns app\\m)\n(defn extracted []\n  (let [a 1] (+ a 2)))\n\n(defn f [] (extracted))\n",
            extracted,
        )
    }

    /** But a free name used in that inner `let`'s initialiser still is: it comes from outside. */
    fun testStillTakesALocalUsedInsideAnInnerLetInitialiser() {
        val extracted = extracted("(ns app\\m)\n(defn f [n] <selection>(let [a (* n 2)] a)</selection>)\n")

        assertEquals(
            "(ns app\\m)\n(defn extracted [n]\n  (let [a (* n 2)] a))\n\n(defn f [n] (extracted n))\n",
            extracted,
        )
    }

    /** A stdlib call resolves from anywhere; passing it in would be nonsense. */
    fun testDoesNotTakeStdlibNamesAsParameters() {
        val extracted = extracted("(ns app\\m)\n(defn f [xs] <selection>(map inc xs)</selection>)\n")

        assertEquals("(ns app\\m)\n(defn extracted [xs]\n  (map inc xs))\n\n(defn f [xs] (extracted xs))\n", extracted)
    }

    /** So does another top-level definition in the same file. */
    fun testDoesNotTakeATopLevelDefinitionAsAParameter() {
        val extracted = extracted("(ns app\\m)\n(defn helper [] 1)\n(defn f [] <selection>(+ (helper) 2)</selection>)\n")

        assertTrue("helper must not become a parameter: $extracted", extracted.contains("(defn extracted []"))
    }

    // ---- placement ----

    /** Immediately above the form it came from, which is where a reader looks for it. */
    fun testPlacesTheNewFunctionAboveItsOrigin() {
        val extracted = extracted("(ns app\\m)\n(defn first-fn [] 1)\n(defn f [n] <selection>(* n 2)</selection>)\n")

        val definitionAt = extracted.indexOf("(defn extracted")
        val originAt = extracted.indexOf("(defn f [n]")
        assertTrue("expected the new defn above f: $extracted", definitionAt in 1..<originAt)
        assertTrue("and below first-fn: $extracted", extracted.indexOf("(defn first-fn") < definitionAt)
    }

    fun testAvoidsANameTheFileAlreadyUses() {
        val extracted = extracted("(ns app\\m)\n(defn extracted [] 0)\n(defn f [n] <selection>(* n 2)</selection>)\n")

        assertTrue("expected a fresh name: $extracted", extracted.contains("(defn extracted2 [n]"))
    }

    // ---- declining ----

    fun testDeclinesABareSymbol() {
        try {
            extracted("(ns app\\m)\n(defn f [n] (+ n <selection>n</selection>))\n")
            fail("expected the refactoring to decline a bare symbol")
        } catch (expected: CommonRefactoringUtil.RefactoringErrorHintException) {
            assertEquals("Select an expression to extract into a function.", expected.message)
        }
    }

    /** A whole top-level form is already a function; there is nothing to lift it out of. */
    fun testDeclinesAWholeTopLevelForm() {
        try {
            extracted("(ns app\\m)\n<selection>(defn f [] 1)</selection>\n")
            fail("expected the refactoring to decline a top-level form")
        } catch (expected: CommonRefactoringUtil.RefactoringErrorHintException) {
            assertEquals("This expression is already a top-level form.", expected.message)
        }
    }
}
