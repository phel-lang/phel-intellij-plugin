package org.phellang.integration.psi

import com.intellij.psi.util.PsiTreeUtil
import org.phellang.integration.PhelIntegrationTestCase
import org.phellang.language.psi.PhelSymbol
import org.phellang.language.psi.analysis.PhelSymbolAnalyzer

/**
 * Locating a `defn`'s parameter vector past its docstring and metadata.
 *
 * Phel's standard library documents almost every definition with
 * `{:example "..." :see-also ["a" "b"]}`. The search used to stop at the first vector found anywhere
 * below an earlier form, so the vector inside `:see-also` ended it and the real parameters were never
 * seen — they were not highlighted as parameters, did not resolve, and could not be renamed, in most
 * of the standard library and in any project following the same convention.
 */
class PhelParameterVectorTest : PhelIntegrationTestCase() {

    private var fileIndex = 0

    /** Whether the last occurrence of [name] is recognised as a local binding or a reference to one. */
    private fun isLocal(source: String, name: String): Boolean {
        myFixture.configureByText("pv${fileIndex++}.phel", "(ns my\\app)\n$source")

        val offset = myFixture.file.text.lastIndexOf(name)
        val symbol = PsiTreeUtil.findElementOfClassAtOffset(myFixture.file, offset, PhelSymbol::class.java, false)

        return symbol != null && PhelSymbolAnalyzer.isLocalBindingOrReference(symbol)
    }

    private fun assertParameter(source: String, name: String) =
        assertTrue("`$name` should be a parameter in:\n$source", isLocal(source, name))

    private fun assertNotParameter(source: String, name: String) =
        assertFalse("`$name` should NOT be a parameter in:\n$source", isLocal(source, name))

    fun testPlainDefn() = assertParameter("(defn f [p] (println p))", "p")

    fun testDocstringBeforeTheVector() = assertParameter("(defn f \"doc\" [p] (println p))", "p")

    fun testMetadataMapBeforeTheVector() =
        assertParameter("(defn f \"doc\" {:example \"e\"} [p] (println p))", "p")

    /** The shape that was broken: a vector nested inside the metadata map. */
    fun testMetadataMapContainingAVector() =
        assertParameter("(defn f \"doc\" {:see-also [\"a\" \"b\"]} [p] (println p))", "p")

    fun testVerbatimStandardLibraryShape() {
        val source = """
            (defn update
              "Updates a value in a datastructure by applying `f` to the current value."
              {:example "(update {:count 5} :count inc)"
               :see-also ["update-in" "assoc"]}
              [ds k f & args]
              (assoc ds k (apply f (get ds k) args)))
        """.trimIndent()

        assertParameter(source, "ds")
        assertParameter(source, "args")
    }

    fun testVariadicNameAfterAmpersandIsAParameter() =
        assertParameter("(defn f \"doc\" {:see-also [\"x\"]} [& rest] (println rest))", "rest")

    /** A vector in the body is a literal, not a parameter list — the guard that made this fiddly. */
    fun testVectorInTheBodyIsNotAParameterVector() =
        assertNotParameter("(defn f [] (println [not-a-param]))", "not-a-param")

    fun testSecondVectorAfterTheParametersIsNotAParameterVector() =
        assertNotParameter("(defn f [p] [also-not-a-param])", "also-not-a-param")

    fun testMultiArityStillResolves() {
        val source = "(defn f \"doc\" {:see-also [\"x\"]} ([a] a) ([a b] (+ a b)))"

        assertParameter(source, "b")
    }

    fun testFnIsUnaffected() = assertParameter("(defn g [] (fn [p] (println p)))", "p")
}
