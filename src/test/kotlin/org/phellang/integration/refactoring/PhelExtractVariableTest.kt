package org.phellang.integration.refactoring

import com.intellij.refactoring.util.CommonRefactoringUtil
import org.phellang.integration.PhelIntegrationTestCase
import org.phellang.refactoring.extract.PhelIntroduceVariableHandler

/**
 * Extract Variable, driven through the handler so selection handling is exercised too.
 *
 * The interesting part is *where the binding goes*, and there are three answers: append to an
 * enclosing `let`, wrap a function body, or wrap the expression where it stands. Each is a separate
 * case below.
 */
class PhelExtractVariableTest : PhelIntegrationTestCase() {

    private val handler = PhelIntroduceVariableHandler()
    private var fileIndex = 0

    /** `<selection>`/`</selection>` marks the expression; a bare `<caret>` uses the form at the caret. */
    private fun extracted(source: String): String {
        myFixture.configureByText("ev${fileIndex++}.phel", source)
        handler.invoke(project, myFixture.editor, myFixture.file, null)

        return myFixture.editor.document.text
    }

    // ---- 1. append to an enclosing let ----

    /** Nesting a second `let` inside the first would say the same thing with more brackets. */
    fun testAppendsToAnEnclosingLet() {
        val extracted = extracted("(ns app\\m)\n(defn f [] (let [a 1] (+ a <selection>(* 2 3)</selection>)))\n")

        assertEquals("(ns app\\m)\n(defn f [] (let [a 1 x (* 2 3)] (+ a x)))\n", extracted)
    }

    fun testAppendsAfterSeveralExistingBindings() {
        val extracted = extracted("(ns app\\m)\n(defn f [] (let [a 1 b 2] <selection>(+ a b)</selection>))\n")

        assertEquals("(ns app\\m)\n(defn f [] (let [a 1 b 2 x (+ a b)] x))\n", extracted)
    }

    /** An expression in the *bindings* is not in the body, so that `let` is not the target. */
    fun testDoesNotAppendToALetWhoseBindingsHoldTheExpression() {
        val extracted = extracted("(ns app\\m)\n(defn f [] (let [a <selection>(* 2 3)</selection>] a))\n")

        assertEquals("(ns app\\m)\n(defn f [] (let [x (* 2 3)] (let [a x] a)))\n", extracted)
    }

    // ---- 2. wrap a function body ----

    /** Wrapping only the expression would give `(let [x expr] x)`, a binding nothing can reach. */
    fun testWrapsAFunctionBody() {
        val extracted = extracted("(ns app\\m)\n(defn f [] (+ 1 <selection>(* 2 3)</selection>))\n")

        assertEquals("(ns app\\m)\n(defn f [] (let [x (* 2 3)] (+ 1 x)))\n", extracted)
    }

    fun testWrapsEveryBodyFormNotJustTheOneHoldingTheExpression() {
        val extracted = extracted("(ns app\\m)\n(defn f [] (println 1) (+ 1 <selection>(* 2 3)</selection>))\n")

        assertEquals("(ns app\\m)\n(defn f [] (let [x (* 2 3)] (println 1) (+ 1 x)))\n", extracted)
    }

    /** A parameter vector is not a body, so the expression must be past it. */
    fun testWrapsTheBodyOfAFunctionWithParameters() {
        val extracted = extracted("(ns app\\m)\n(defn f [n] (+ n <selection>(* 2 3)</selection>))\n")

        assertEquals("(ns app\\m)\n(defn f [n] (let [x (* 2 3)] (+ n x)))\n", extracted)
    }

    // ---- 3. wrap where it stands ----

    /** A `def` value is not a body; wrapping in place is all it allows. */
    fun testWrapsADefValueWhereItStands() {
        val extracted = extracted("(ns app\\m)\n(def answer <selection>(+ 1 2)</selection>)\n")

        assertEquals("(ns app\\m)\n(def answer (let [x (+ 1 2)] x))\n", extracted)
    }

    // ---- naming ----

    /** The placeholder must not shadow something the expression itself depends on. */
    fun testAvoidsANameAlreadyInUse() {
        val extracted = extracted("(ns app\\m)\n(defn f [x] (+ x <selection>(* 2 3)</selection>))\n")

        assertEquals("(ns app\\m)\n(defn f [x] (let [x2 (* 2 3)] (+ x x2)))\n", extracted)
    }

    // ---- declining ----

    /**
     * A bare name is already a name; binding it to another buys nothing. The user is told rather
     * than left wondering why nothing happened.
     */
    fun testDeclinesASymbolAndSaysWhy() {
        try {
            extracted("(ns app\\m)\n(defn f [n] (+ n <selection>n</selection>))\n")
            fail("expected the refactoring to decline a bare symbol")
        } catch (expected: CommonRefactoringUtil.RefactoringErrorHintException) {
            assertEquals("Select an expression to extract.", expected.message)
        }
    }

    fun testUsesTheFormAtTheCaretWhenNothingIsSelected() {
        val extracted = extracted("(ns app\\m)\n(defn f [] (+ 1 (* 2<caret> 3)))\n")

        assertEquals("(ns app\\m)\n(defn f [] (let [x (* 2 3)] (+ 1 x)))\n", extracted)
    }
}
