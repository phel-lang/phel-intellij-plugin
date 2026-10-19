package org.phellang.integration.refactoring

import com.intellij.psi.util.PsiTreeUtil
import com.intellij.refactoring.BaseRefactoringProcessor
import com.intellij.refactoring.safeDelete.SafeDeleteProcessor
import com.intellij.refactoring.safeDelete.usageInfo.SafeDeleteReferenceSimpleDeleteUsageInfo
import com.intellij.usageView.UsageInfo
import org.phellang.indexing.PhelProjectSymbolIndex
import org.phellang.integration.PhelIntegrationTestCase
import org.phellang.language.psi.PhelSymbol
import org.phellang.language.psi.files.PhelFile
import org.phellang.refactoring.PhelRefactoringSupportProvider
import org.phellang.refactoring.safedelete.PhelSafeDeleteProcessor
import org.phellang.refactoring.safedelete.PhelSafeDeleteTarget

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
        // Primed so index-backed lookups see this file. PhelReference itself reads no index: it
        // resolves within this file and the files of its namespace (PhelNamespaceFiles).
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

    /**
     * The recursive call must be reported as *safe*, not merely survive the deletion.
     *
     * Asserting the resulting text is not enough: the test harness confirms the "usages found"
     * dialog automatically, so a call wrongly marked unsafe still deleted here while the IDE stopped
     * and asked. That is exactly what shipped — the condition tested the name's range rather than
     * the enclosing form's, and a name can never contain a call to itself.
     */
    fun testARecursiveCallIsReportedAsSafeToDelete() {
        val file = configure("(ns app\\m)\n(defn countdown [n] (if (= n 0) 0 (countdown (- n 1))))\n")
        val name = definitionNamed(file, "countdown")

        val usages = mutableListOf<UsageInfo>()
        PhelSafeDeleteProcessor().findUsages(name, arrayOf(name), usages)

        assertEquals("the recursive call should be the only usage found", 1, usages.size)
        val recursive = usages.single() as SafeDeleteReferenceSimpleDeleteUsageInfo
        assertTrue(
            "a call inside the form being deleted must not prompt the user",
            recursive.isSafeDelete,
        )
    }

    /** A call from elsewhere is genuinely unsafe, and must still be reported that way. */
    fun testACallFromAnotherFormIsReportedAsUnsafe() {
        val file = configure("(ns app\\m)\n(defn helper [] 1)\n(defn caller [] (helper))\n")
        val name = definitionNamed(file, "helper")

        val usages = mutableListOf<UsageInfo>()
        PhelSafeDeleteProcessor().findUsages(name, arrayOf(name), usages)

        assertEquals(1, usages.size)
        assertFalse(
            "a usage outside the deleted form must still stop the refactoring",
            (usages.single() as SafeDeleteReferenceSimpleDeleteUsageInfo).isSafeDelete,
        )
    }

    /** A type tag spelling the name is not a usage: `^vector` names a type, not this definition. */
    fun testATypeTagOfTheSameNameIsNotAUsage() {
        val file = configure("(ns app\\m)\n(defn- vector [] 1)\n(defn f [^vector v] v)\n")
        val name = definitionNamed(file, "vector")

        val usages = mutableListOf<UsageInfo>()
        PhelSafeDeleteProcessor().findUsages(name, arrayOf(name), usages)

        assertEmpty(usages)
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

    // ---- uses the plugin cannot link (each test gets its own namespaces: the lookup is project-wide) ----

    private val prefix: String get() = getTestName(true).lowercase()

    private fun projectFile(short: String, text: String): PhelFile =
        myFixture.addFileToProject("src/sd_$prefix/$short.phel", text) as PhelFile

    /** A fully qualified call is a real use, so Safe Delete must refuse rather than delete a live function. */
    fun testAFullyQualifiedUseStopsTheDeletion() {
        val util = projectFile("util", "(ns $prefix.util)\n(defn helper [] 1)\n")
        projectFile("main", "(ns $prefix.main)\n(defn f [] ($prefix.util/helper))\n")

        try {
            safeDelete(util, "helper")
            fail("expected safe delete to report the fully qualified use, util is now: ${util.text}")
        } catch (expected: BaseRefactoringProcessor.ConflictsInTestsException) {
            assertTrue(expected.messages.toString(), expected.messages.isNotEmpty())
        }

        assertTrue("nothing should have been deleted: ${util.text}", util.text.contains("(defn helper [] 1)"))
    }

    /**
     * A same-named symbol that resolves to nothing may be a use the plugin cannot see: here an
     * `in-ns` file calling a name its namespace's `ns` file refers. It is reported as a usage that
     * stops the deletion, so the user decides, instead of a silent "no usages".
     */
    fun testAnUnresolvedSameNamedSymbolIsReportedAsAPossibleUse() {
        val util = projectFile("util", "(ns $prefix.util)\n(defn helper [] 1)\n")
        projectFile("main", "(ns $prefix.main\n  (:require $prefix.util :refer [helper]))\n")
        val extra = projectFile("main_extra", "(in-ns $prefix.main)\n(defn g [] (helper))\n")
        val name = definitionNamed(util, "helper")

        val usages = mutableListOf<UsageInfo>()
        PhelSafeDeleteProcessor().findUsages(name, arrayOf(name), usages)

        val inExtra = usages.filter { it.element?.containingFile == extra }
        assertEquals("the in-ns file's call: $usages", 1, inExtra.size)
        assertFalse("it must stop the deletion", (inExtra.single() as SafeDeleteReferenceSimpleDeleteUsageInfo).isSafeDelete)
    }

    /** Every same-named symbol resolves somewhere else: nothing more to report. */
    fun testNoPossibleUseWhenEverySameNamedSymbolResolves() {
        val util = projectFile("util", "(ns $prefix.util)\n(defn helper [] 1)\n")
        projectFile("other", "(ns $prefix.other)\n(defn helper [] 2)\n(defn k [] (helper))\n")
        val name = definitionNamed(util, "helper")

        val usages = mutableListOf<UsageInfo>()
        PhelSafeDeleteProcessor().findUsages(name, arrayOf(name), usages)

        assertEmpty(usages)
    }

    /**
     * Inside the deleted form means inside it *in its own file*. A use in another file whose offsets
     * happen to fall within the form's range was counted as harmless, so the function was deleted.
     */
    fun testAUseInAnotherFileAtOverlappingOffsetsStillStopsTheDeletion() {
        val util = projectFile("util", "(ns $prefix.util)\n(defn helper [] 1)\n")
        // A second file of the same namespace: same `(ns ...)` line, so `(helper)` starts where `(defn` does.
        val main = projectFile("main", "(ns $prefix.util)\n(helper)\n")
        val name = definitionNamed(util, "helper")
        val formRange = PhelSafeDeleteTarget.enclosingFormOf(name)!!.textRange
        val use = PsiTreeUtil.findChildrenOfType(main, PhelSymbol::class.java).last { it.text == "helper" }
        assertTrue("precondition: the use's offsets fall inside the form's range", formRange.contains(use.textRange))

        val usages = mutableListOf<UsageInfo>()
        PhelSafeDeleteProcessor().findUsages(name, arrayOf(name), usages)

        val fromMain = usages.single { it.element === use } as SafeDeleteReferenceSimpleDeleteUsageInfo
        assertFalse("a use in another file is never inside the deleted form", fromMain.isSafeDelete)
    }
}
