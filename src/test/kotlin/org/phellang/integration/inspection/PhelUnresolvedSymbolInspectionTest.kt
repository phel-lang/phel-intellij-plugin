package org.phellang.integration.inspection

import com.intellij.codeInspection.InspectionManager
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiRecursiveElementVisitor
import org.phellang.inspection.PhelUnresolvedSymbolInspection
import org.phellang.integration.PhelIntegrationTestCase
import org.phellang.language.psi.files.PhelFile

/**
 * The unresolved-symbol inspection, weighted towards what it must *not* report.
 *
 * A false "cannot resolve" on working code is worse than a missed one — it teaches the user to
 * ignore the plugin's warnings — so the silent cases outnumber the reported ones here on purpose.
 */
class PhelUnresolvedSymbolInspectionTest : PhelIntegrationTestCase() {

    private var fileIndex = 0

    private fun inspect(source: String): List<String> {
        // Does not prime the project index: everything asserted here resolves from the file's own
        // PSI, so priming would add nothing. Since #271 the index is cleared between classes, so
        // this is no longer load-bearing for isolation.
        val file = myFixture.configureByText("i${fileIndex++}.phel", "(ns my\\app)\n$source") as PhelFile

        val holder = ProblemsHolder(InspectionManager.getInstance(project), file, true)
        val visitor = PhelUnresolvedSymbolInspection().buildVisitor(holder, true)
        file.accept(object : PsiRecursiveElementVisitor() {
            override fun visitElement(element: PsiElement) {
                element.accept(visitor)
                super.visitElement(element)
            }
        })

        return holder.results.map { it.descriptionTemplate }
    }

    private fun assertSilent(source: String) {
        val problems = inspect(source)

        assertTrue("expected no report for:\n$source\ngot $problems", problems.isEmpty())
    }

    fun testTheAskedAboutCaseIsReported() {
        val problems = inspect("(defn my-function [a b]\n  (+ a b inexistent-param))")

        assertEquals(listOf("Cannot resolve symbol 'inexistent-param'"), problems)
    }

    fun testParametersAndBindingsAreSilent() {
        assertSilent("(defn f [a b] (+ a b))")
        assertSilent("(defn f [] (let [x 1 y 2] (+ x y)))")
        assertSilent("(defn f [] (loop [acc 0] (recur acc)))")
        assertSilent("(defn f [] (foreach [v [1 2]] v))")
        assertSilent("(defn f [] (if-some [v 1] v 2))")
    }

    fun testStandardLibraryIsSilent() {
        assertSilent("(defn f [] (map inc [1 2 3]))")
        assertSilent("(defn f [] (str/join \",\" [1 2]))")
        assertSilent("(defn f [] (println (count [1 2])))")
    }

    fun testSpecialFormsAndDefinitionNamesAreSilent() {
        assertSilent("(defn f [] (if true 1 2))")
        assertSilent("(defn f [] (do (when true 1)))")
        assertSilent("(def answer 42)\n(defn f [] answer)")
        assertSilent("(declare later)\n(defn f [] (later))\n(defn later [] 1)")
    }

    fun testForwardAndBackwardFileReferencesAreSilent() {
        assertSilent("(defn f [] (helper))\n(defn helper [] 1)")
        assertSilent("(defn helper [] 1)\n(defn f [] (helper))")
    }

    fun testQuotedDataIsSilent() {
        assertSilent("(defn f [] 'not-a-reference)")
        assertSilent("(defn f [] `(some-template thing))")
    }

    /** A macro's expansion can bind names that appear nowhere in the source. */
    fun testAnythingInsideAProjectMacroCallIsSilent() {
        assertSilent("(defmacro with-it [& body] `(let [it 1] ~@body))\n(defn f [] (with-it (+ it 1)))")
    }

    fun testPhpInteropIsSilent() {
        assertSilent("(defn f [] (php/strlen \"s\"))")
        assertSilent("(defn f [o] (.method o))")
        assertSilent("(defn f [o] (.-field o))")
    }

    fun testRestMarkerAndShortFunctionParametersAreSilent() {
        assertSilent("(defn f [& rest] rest)")
        assertSilent("(defn f [] (map #(+ $ 1) [1 2]))")
    }

    /**
     * Every case below was found by running this over `phel-lang/src/phel` and grouping the reports
     * by the shape each symbol sat in. None of them is a Phel name being read.
     */

    fun testPhpInteropCallsAreSilent() {
        assertSilent("(defn f [p] (php/-> p (getName)))")
        assertSilent("(defn f [] (php/:: SomeClass (create 1)))")
        assertSilent("(defn f [p] (php/-> p (getNamespace) (decodeNs)))")
    }

    /** The `ns` form is import syntax, not expressions. */
    fun testTheNsFormIsSilent() {
        assertSilent("(ns other\\app (:require phel\\str :refer [split join]))")
    }

    /** Field vectors and the protocol method implementations that follow them are declarations. */
    fun testTypeDeclarationsAreSilent() {
        assertSilent("(defstruct point [x y])")
        assertSilent("(defstruct router [routes]\n  Router\n  (match-by-path [this path] (get routes path)))")
        assertSilent("(definterface Shape (area [this]))")
    }

    fun testCatchBindingsAreSilent() {
        assertSilent("(defn g [] 1)\n(defn f [] (try (g) (catch \\Exception e (println e))))")
    }

    /**
     * `&form` and `&env` are injected into every macro body by the compiler.
     *
     * Asserted by name rather than with assertSilent: the surrounding call in the real source uses a
     * helper from a sibling file, which this fixture cannot see and correctly reports.
     */
    fun testMacroImplicitParametersAreNeverReported() {
        val problems = inspect("(defn build [& xs] xs)\n(defmacro m [name & fdecl] (apply build &form &env name fdecl))")

        assertTrue("&form/&env must not be reported, got $problems", problems.none { "&" in it })
    }

    /** `%`, `%1`, `%2` are the short-fn anaphors — not `$`, as the first draft assumed. */
    fun testShortFunctionAnaphorsAreSilent() {
        assertSilent("(defn f [] (map #(+ % 1) [1 2]))")
        assertSilent("(defn f [] (reduce #(union (difference %1 %2) %2) [] []))")
    }

    /**
     * `foreach` binds three slots rather than pairs, `for` and `dofor` take verbs, `doseq` was
     * missing from the shared binding-form set entirely, and any of them may destructure.
     */
    fun testBindingFormsWithNonPairShapesAreSilent() {
        assertSilent("(defn f [m] (foreach [k v m] (println k v)))")
        assertSilent("(defn f [m] (for [[k v] :pairs m] (println k v)))")
        assertSilent("(defn f [xs] (doseq [x xs] (println x)))")
        assertSilent("(defn f [xs] (dofor [x :in xs] (println x)))")
    }

    fun testQualifiedSymbolsAreLeftToTheOtherValidators() {
        assertSilent("(defn f [] (nope/missing 1))")
    }
}
