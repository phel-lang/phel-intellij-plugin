package org.phellang.integration.refactoring

import com.intellij.psi.util.PsiTreeUtil
import com.intellij.refactoring.BaseRefactoringProcessor
import com.intellij.refactoring.safeDelete.SafeDeleteProcessor
import org.phellang.indexing.PhelProjectSymbolIndex
import org.phellang.integration.PhelIntegrationTestCase
import org.phellang.language.psi.PhelSymbol
import org.phellang.language.psi.files.PhelFile
import org.phellang.refactoring.PhelRefactoringSupportProvider

/**
 * Safe Delete over a Phel definition.
 *
 * Two properties matter. It removes the *whole form*, because a name is not a standalone thing in a
 * Lisp — deleting `greet` out of `(defn greet [name] …)` and stopping would leave `(defn  [name] …)`.
 * And it refuses when something still refers to the name, which is the "safe" part.
 */
class PhelSafeDeleteTest : PhelIntegrationTestCase() {

    private var fileIndex = 0

    private fun configure(source: String): PhelFile {
        val file = myFixture.configureByText("sd${fileIndex++}.phel", source) as PhelFile
        // PhelReference resolves project-wide through the index; without priming, whether a usage is
        // found would depend on which test ran first.
        PhelProjectSymbolIndex.getInstance(project).refreshFileFromPsi(file)

        return file
    }

    private fun definitionNamed(file: PhelFile, name: String): PhelSymbol =
        PsiTreeUtil.findChildrenOfType(file, PhelSymbol::class.java).first { it.text == name }

    private fun safeDelete(file: PhelFile, name: String) {
        val target = definitionNamed(file, name)
        SafeDeleteProcessor.createInstance(project, null, arrayOf(target), false, false, true).run()
    }

    // ---- availability ----

    private val provider = PhelRefactoringSupportProvider()

    fun testOfferedForATopLevelDefinition() {
        val file = configure("(ns app\\m)\n(defn greet [name] name)\n")

        assertTrue(provider.isSafeDeleteAvailable(definitionNamed(file, "greet")))
    }

    /**
     * Not offered for a `let` binding or a parameter. Both are definitions to
     * `PhelSymbolAnalyzer`, but removing one means editing a binding vector in place — a different
     * operation, and one the unused-binding quick fixes already cover.
     */
    fun testNotOfferedForALetBinding() {
        val file = configure("(ns app\\m)\n(defn f [] (let [x 1] x))\n")

        assertFalse(provider.isSafeDeleteAvailable(definitionNamed(file, "x")))
    }

    fun testNotOfferedForAFunctionParameter() {
        val file = configure("(ns app\\m)\n(defn f [param] param)\n")

        assertFalse(provider.isSafeDeleteAvailable(definitionNamed(file, "param")))
    }

    /** A name used inside a body is not that form's own name. */
    fun testNotOfferedForACallInsideABody() {
        val file = configure("(ns app\\m)\n(defn helper [] 1)\n(defn f [] (helper))\n")

        val usage = PsiTreeUtil.findChildrenOfType(file, PhelSymbol::class.java).last { it.text == "helper" }
        assertFalse(provider.isSafeDeleteAvailable(usage))
    }

    // ---- deletion ----

    fun testDeletesTheWholeDefinitionForm() {
        val file = configure("(ns app\\m)\n(defn unused [x] x)\n(defn kept [] 1)\n")

        safeDelete(file, "unused")

        assertEquals("(ns app\\m)\n(defn kept [] 1)\n", file.text)
    }

    fun testDeletesADefRatherThanJustItsName() {
        val file = configure("(ns app\\m)\n(def answer 42)\n(defn kept [] 1)\n")

        safeDelete(file, "answer")

        assertEquals("(ns app\\m)\n(defn kept [] 1)\n", file.text)
    }

    /** A recursive call is inside the form being removed, so it must not block the deletion. */
    fun testDeletesARecursiveFunction() {
        val file = configure("(ns app\\m)\n(defn countdown [n] (if (= n 0) 0 (countdown (- n 1))))\n")

        safeDelete(file, "countdown")

        assertEquals("(ns app\\m)\n", file.text)
    }

    // ---- safety ----

    /** The point of the refactoring: a live usage stops it. */
    fun testRefusesWhenTheNameIsStillUsed() {
        val file = configure("(ns app\\m)\n(defn helper [] 1)\n(defn caller [] (helper))\n")

        try {
            safeDelete(file, "helper")
            fail("expected safe delete to report the remaining usage, file is now: ${file.text}")
        } catch (expected: BaseRefactoringProcessor.ConflictsInTestsException) {
            assertTrue(expected.messages.toString(), expected.messages.isNotEmpty())
        }

        assertTrue("nothing should have been deleted: ${file.text}", file.text.contains("(defn helper [] 1)"))
    }
}
