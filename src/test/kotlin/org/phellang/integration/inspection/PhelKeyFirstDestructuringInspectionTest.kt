package org.phellang.integration.inspection

import com.intellij.codeInspection.InspectionManager
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiRecursiveElementVisitor
import org.phellang.inspection.deprecated.PhelKeyFirstDestructuringInspection
import org.phellang.integration.PhelIntegrationTestCase
import org.phellang.language.psi.files.PhelFile

/**
 * The key-first map destructuring order Phel 0.51 deprecated (phel-lang #3115), and — just as
 * important — everything that looks like it but is not a pattern, which must stay silent.
 */
class PhelKeyFirstDestructuringInspectionTest : PhelIntegrationTestCase() {

    private var fileIndex = 0

    private fun problems(source: String): List<ProblemDescriptor> {
        val file = myFixture.configureByText("k${fileIndex++}.phel", "(ns app\\m)\n$source") as PhelFile
        val holder = ProblemsHolder(InspectionManager.getInstance(project), file, true)
        val visitor = PhelKeyFirstDestructuringInspection().buildVisitor(holder, true)
        file.accept(object : PsiRecursiveElementVisitor() {
            override fun visitElement(element: PsiElement) {
                element.accept(visitor)
                super.visitElement(element)
            }
        })
        return holder.results
    }

    private fun inspect(source: String): List<String> = problems(source).map { it.descriptionTemplate }

    fun testKeywordKeyFirstPairInLetIsFlagged() {
        val warnings = inspect("(defn f [m] (let [{:name n} m] n))")
        assertEquals(1, warnings.size)
        assertTrue("should name the pair: $warnings", warnings[0].contains("{:name n}"))
        assertTrue("should say deprecated since 0.51: $warnings", warnings[0].contains("deprecated since Phel 0.51"))
        assertTrue("should name the flipped spelling: $warnings", warnings[0].contains("{n :name}"))
    }

    fun testIndexAndStringKeysAreFlaggedToo() {
        assertEquals(1, inspect("(defn f [v] (let [{0 first-one} v] first-one))").size)
        assertEquals(1, inspect("(defn f [m] (let [{\"name\" n} m] n))").size)
    }

    fun testKeyFirstPairInParametersIsFlagged() {
        assertEquals(1, inspect("(defn f [{:id id}] id)").size)
        assertEquals(1, inspect("(fn [{:id id}] id)").size)
    }

    fun testKeyFirstPairInAMultiArityFunctionIsFlagged() {
        assertEquals(1, inspect("(defn f ([x] x) ([x {:id id}] id))").size)
    }

    fun testNestedKeyFirstPairIsFlagged() {
        // The outer pair is binding-first; the inner one is not.
        val warnings = inspect("(defn f [m] (let [{{:city c} :address} m] c))")
        assertEquals(1, warnings.size)
        assertTrue(warnings[0].contains("{:city c}"))
    }

    fun testEveryKeyFirstPairIsReported() {
        assertEquals(2, inspect("(defn f [m] (let [{:a a :b b} m] (+ a b)))").size)
    }

    fun testBindingFirstPairsAreSilent() {
        assertTrue(inspect("(defn f [m] (let [{n :name a :age} m] (str n a)))").isEmpty())
        assertTrue(inspect("(defn f [{id :id}] id)").isEmpty())
        assertTrue(inspect("(defn f [m] (let [{{:keys [c]} :inner} m] c))").isEmpty())
        assertTrue(inspect("(defn f [m] (let [{[a b] :pair} m] (+ a b)))").isEmpty())
    }

    fun testDirectivesAreSilent() {
        assertTrue(inspect("(defn f [m] (let [{:keys [a b] :strs [c] :syms [d] :as all} m] (str a b c d all)))").isEmpty())
    }

    /** The `:or` map pairs a bound name with a default expression; it is not a pattern. */
    fun testOrDefaultsAreSilent() {
        assertTrue(inspect("(defn f [m] (let [{:keys [a] :or {a 1}} m] a))").isEmpty())
    }

    /** A map literal in a value slot, an argument, a definition or metadata is never a pattern. */
    fun testOrdinaryMapLiteralsAreSilent() {
        assertTrue(inspect("(defn f [n] (let [m {:name n}] m))").isEmpty())
        assertTrue(inspect("(defn f [n] (g {:name n}))").isEmpty())
        assertTrue(inspect("(def cfg {:debug true})").isEmpty())
        assertTrue(inspect("(defn f {:private true} [x] x)").isEmpty())
    }

    /**
     * `for`, `dofor` and `foreach` heads are not pattern/value pairs, so a map literal in their even
     * slots is the collection being iterated, never a pattern. A warning there would come with a
     * quick fix that corrupts working code.
     */
    fun testIteratedMapLiteralsInLoopHeadsAreSilent() {
        assertTrue(inspect("(defn f [x y] (for [[k v] :pairs {:a x :b y}] k))").isEmpty())
        assertTrue(inspect("(defn f [x] (dofor [[k v] :pairs {:a x}] k))").isEmpty())
        assertTrue(inspect("(defn f [x] (foreach [k v {:a x}] (println k v)))").isEmpty())
    }

    /** A parameter vector of a call in the body is not an arity of the enclosing function; the pair is reported once. */
    fun testAnInnerFnInTheBodyIsNotReportedTwice() {
        assertEquals(1, inspect("(defn f [m] ((fn [{:id id}] id) m))").size)
    }

    /** `{k v}` binds on both sides: reported without a fix, since a swap would change which side is looked up. */
    fun testAmbiguousPairIsReportedWithoutAQuickFix() {
        val found = problems("(defn f [m k] (let [{k v} m] v))")
        assertEquals(1, found.size)
        assertTrue("should say ambiguous: ${found[0].descriptionTemplate}", found[0].descriptionTemplate.contains("Ambiguous"))
        assertTrue("no fix for an ambiguous pair", found[0].fixes.isNullOrEmpty())
    }

    fun testQuickFixSwapsThePairKeepingItsSpacing() {
        val found = problems("(defn f [m] (let [{:name  n :age a} m] n))")
        val keyFirst = found.single { it.descriptionTemplate.contains("{:name n}") }
        val fix = keyFirst.fixes!!.single()

        WriteCommandAction.runWriteCommandAction(project) { fix.applyFix(project, keyFirst) }

        assertTrue(
            "expected the pair swapped in place: ${myFixture.file.text}",
            myFixture.file.text.contains("(let [{n  :name :age a} m] n)"),
        )
    }
}
