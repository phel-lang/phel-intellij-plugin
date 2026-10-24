package org.phellang.integration.inspection

import com.intellij.codeInspection.InspectionManager
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiRecursiveElementVisitor
import org.phellang.indexing.PhelProjectSymbolIndex
import org.phellang.inspection.PhelArityMismatchInspection
import org.phellang.integration.PhelIntegrationTestCase
import org.phellang.language.psi.files.PhelFile

/**
 * The arity a bare call is checked against is the one of the definition the compiler would call: the
 * namespace the name is `:refer`'d from, else the current namespace (every file of it, private
 * definitions included), else `phel.core`.
 *
 * It used to be the stdlib's first, so a project `count` was checked as core's, and then whichever
 * same-named definition the project index listed first, from any namespace. Two test files that each
 * define their own `point` made the warnings change from one run to the next.
 */
class PhelBareNameArityTest : PhelIntegrationTestCase() {

    private var fileIndex = 0

    override fun setUp() {
        super.setUp()
        // Listed first, so a by-name lookup over the index meets lib.b's definitions before anyone's own.
        index(myFixture.addFileToProject(
            "src/lib/b.phel",
            "(ns lib.b)\n(defn helper [a b c] a)\n(defn only-in-lib [a b] a)\n(defn count [a b c] a)\n",
        ) as PhelFile)
    }

    private fun index(file: PhelFile) = PhelProjectSymbolIndex.getInstance(project).refreshFileFromPsi(file)

    private fun warnings(source: String): List<String> {
        val file = myFixture.configureByText("bare${fileIndex++}.phel", source) as PhelFile
        index(file)
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

    // The current namespace

    fun testTheFilesOwnDefinitionIsUsedNotANamesakes() {
        assertEmpty(warnings("(ns app.a)\n(defn helper [x] x)\n(helper 1)\n"))
    }

    fun testTheFilesOwnDefinitionFlagsAWrongCall() {
        val warnings = warnings("(ns app.a)\n(defn helper [x] x)\n(helper 1 2)\n")

        assertEquals(listOf("Wrong number of args (2) passed to 'helper'. Expected: 1."), warnings)
    }

    /** The index holds public definitions only; a private one is still the call's target. */
    fun testAPrivateDefinitionIsChecked() {
        val warnings = warnings("(ns app.a)\n(defn- helper [x] x)\n(helper 1 2)\n")

        assertEquals(listOf("Wrong number of args (2) passed to 'helper'. Expected: 1."), warnings)
    }

    /** A type tag is metadata on the parameter after it, not a parameter (phel-lang's own tests do both). */
    fun testTaggedParametersCountOnce() {
        val source = "(ns app.a)\n(defn- tagged [^string s] s)\n(defstruct point [^{:tag int} x ^{:tag int} y])\n" +
            "(tagged \"a\")\n(point 1 2)\n"

        assertEmpty(warnings(source))
    }

    /** A definition in the current namespace shadows `phel.core`'s. */
    fun testAnOwnDefinitionShadowsCore() {
        assertEmpty(warnings("(ns app.a)\n(defn count [a b c] a)\n(count 1 2 3)\n"))
    }

    fun testAnotherFileOfTheSameNamespaceIsUsed() {
        index(myFixture.addFileToProject("src/app/a_more.phel", "(ns app.a)\n(defn pair [x y] x)\n") as PhelFile)

        assertEmpty(warnings("(ns app.a)\n(pair 1 2)\n"))
        assertEquals(
            listOf("Wrong number of args (1) passed to 'pair'. Expected: 2."),
            warnings("(ns app.a)\n(pair 1)\n"),
        )
    }

    /** A file joining the namespace with `in-ns` is one of its files too. */
    fun testAnInNsFileOfTheSameNamespaceIsUsed() {
        myFixture.addFileToProject("src/app/a_bucket.phel", "(in-ns app.a)\n(defn triple [x y z] x)\n")

        assertEmpty(warnings("(ns app.a)\n(triple 1 2 3)\n"))
    }

    // :refer

    fun testAReferredNameUsesTheReferringNamespace() {
        val source = "(ns app.main\n  (:require lib.b :refer [helper]))\n(defn f [] (helper 1 2 3))\n"

        assertEmpty(warnings(source))
    }

    fun testAReferredNameFlagsAWrongCall() {
        val warnings = warnings("(ns app.main\n  (:require lib.b :refer [helper]))\n(helper 1)\n")

        assertEquals(listOf("Wrong number of args (1) passed to 'helper'. Expected: 3."), warnings)
    }

    /** `phel.string/join` takes one or two; `join` is no core function. */
    fun testANameReferredFromTheStdlibUsesItsArity() {
        val source = "(ns app.main\n  (:require phel.string :refer [join]))\n(join [1])\n(join \",\" [1])\n(join)\n"

        assertEquals(listOf("Wrong number of args (0) passed to 'join'. Expected: 1 or 2."), warnings(source))
    }

    // Nothing to borrow

    /** Not referred, not defined here: the compiler cannot call lib.b's, so its arity is no measure. */
    fun testAnotherNamespacesDefinitionIsNotBorrowed() {
        assertEmpty(warnings("(ns app.main)\n(only-in-lib 1)\n"))
    }

    /** A `def` gives no parameter list to check against; it used to read as zero parameters. */
    fun testAFunctionHeldByADefIsNotChecked() {
        assertEmpty(warnings("(ns app.a)\n(def add (fn [a b] a))\n(add 1 2)\n"))
    }

    fun testAQualifiedCallToAFunctionHeldByADefIsNotChecked() {
        index(myFixture.addFileToProject("src/app/vals.phel", "(ns app.vals)\n(def add (fn [a b] a))\n") as PhelFile)

        assertEmpty(warnings("(ns app.main\n  (:require app.vals :as v))\n(v/add 1 2)\n"))
    }

    // A type tag on the vector, `(defn count ^int [xs] ...)`, as Phel's own core writes `count` and `str`.

    fun testATagOnTheParameterVectorKeepsItsParameters() {
        val source = "(ns app.a)\n(defn- fmt ^string [row] row)\n(defn nullable ^{:tag ?int} [b] b)\n" +
            "(fmt 1)\n(nullable true)\n"

        assertEmpty(warnings(source))
    }

    fun testTaggedArityClausesKeepTheirParameters() {
        val source = "(ns app.a)\n(defn g (^int [x] x) (^int [x y] x))\n(g 1)\n(g 1 2)\n(g 1 2 3)\n"

        assertEquals(listOf("Wrong number of args (3) passed to 'g'. Expected: 1 or 2."), warnings(source))
    }

    /** No parameter vector is no zero-argument function: `(defn h` is still being typed. */
    fun testADefinitionWithoutParametersIsNotChecked() {
        assertEmpty(warnings("(ns app.a)\n(defn h)\n(h 1)\n"))
    }

    // Definitions the compiler never sees

    fun testADraftInACommentIsNoDefinition() {
        val source = "(ns app.a)\n(comment (defn helper [a b c] a))\n(defn helper [x] x)\n(helper 1)\n"

        assertEmpty(warnings(source))
    }

    fun testADiscardedDraftIsNoDefinition() {
        assertEmpty(warnings("(ns app.a)\n#_(defn helper [a b c] a)\n(defn helper [x] x)\n(helper 1)\n"))
    }

    /** The template defines whatever `~name` is at expansion; core's `name` is still the one called here. */
    fun testAMacroTemplateIsNoDefinition() {
        val source = "(ns app.a)\n(defmacro defpage [name & body] `(defn ~name [] ~@body))\n(defn f [k] (name k))\n"

        assertEmpty(warnings(source))
    }

    // :refer from a stdlib namespace whose registry name is not its last segment

    /** `phel.http-client`'s functions are filed as `http_client/...`; core's `get` takes two or three. */
    fun testANameReferredFromAHyphenatedStdlibNamespaceUsesItsArity() {
        val source = "(ns app.main\n  (:require phel.http-client :refer [get]))\n(get \"https://example.com\")\n"

        assertEmpty(warnings(source))
    }

    // PHP method calls

    /** `(count)` here is the PHP object's `count()`, not core's one-argument `count`. */
    fun testAMethodInsidePhpArrowIsNoPhelCall() {
        val source = "(ns app.main)\n(defn f [obj m] (php/-> obj (count)) (php/-> m (put :doc 1)) (php/:: Foo (get 1)))\n"

        assertEmpty(warnings(source))
    }

    /** The object itself is Phel code, and still checked. */
    fun testTheObjectOfAPhpArrowIsStillChecked() {
        val warnings = warnings("(ns app.main)\n(php/-> (inc 1 2) (format))\n")

        assertEquals(warnings.toString(), 1, warnings.size)
        assertTrue(warnings.toString(), warnings.single().contains("'inc'"))
    }

    /** Core is still the last resort. */
    fun testACoreFunctionIsStillChecked() {
        val warnings = warnings("(ns app.main)\n(inc 1 2)\n")

        assertEquals(warnings.toString(), 1, warnings.size)
        assertTrue(warnings.toString(), warnings.single().contains("'inc'"))
    }
}
