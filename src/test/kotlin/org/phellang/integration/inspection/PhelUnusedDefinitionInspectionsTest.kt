package org.phellang.integration.inspection

import com.intellij.codeInspection.InspectionManager
import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiRecursiveElementVisitor
import org.phellang.inspection.PhelUnusedImportInspection
import org.phellang.inspection.PhelUnusedParameterInspection
import org.phellang.inspection.PhelUnusedPrivateDefinitionInspection
import org.phellang.integration.PhelIntegrationTestCase
import org.phellang.language.psi.files.PhelFile

/**
 * The three "never used" inspections.
 *
 * All three ship at WEAK WARNING because a false report here is worse than a missed one: it teaches
 * the user to ignore the plugin. The silent cases below outnumber the reported ones for that reason.
 */
class PhelUnusedDefinitionInspectionsTest : PhelIntegrationTestCase() {

    private var fileIndex = 0

    private fun inspect(tool: LocalInspectionTool, source: String): List<String> {
        // Does not prime the project index: everything here resolves from the file's own PSI. Since
        // #271 the index is cleared between classes, so this is no longer load-bearing for isolation.
        val file = myFixture.configureByText("u${fileIndex++}.phel", source) as PhelFile

        val holder = ProblemsHolder(InspectionManager.getInstance(project), file, true)
        val visitor = tool.buildVisitor(holder, true)
        file.accept(object : PsiRecursiveElementVisitor() {
            override fun visitElement(element: PsiElement) {
                element.accept(visitor)
                super.visitElement(element)
            }
        })

        return holder.results.map { it.descriptionTemplate }
    }

    // ---- unused private definitions ----

    private fun privateDefinitions(source: String) = inspect(PhelUnusedPrivateDefinitionInspection(), source)

    fun testReportsAPrivateDefinitionNothingCalls() {
        val problems = privateDefinitions("(ns app\\a)\n(defn- helper [x] (* x 2))\n")

        assertEquals(listOf("Private definition 'helper' is never used."), problems)
    }

    fun testDoesNotReportAPrivateDefinitionThatIsCalled() {
        assertEmpty(privateDefinitions("(ns app\\a)\n(defn- helper [x] (* x 2))\n(defn run [] (helper 1))\n"))
    }

    /** `^vector` names a type, so it is no call of a private definition called `vector`. */
    fun testReportsAPrivateDefinitionOnlyATypeTagSpells() {
        val problems = privateDefinitions("(ns app\\a)\n(defn- vector [] 1)\n(defn run [^vector v] v)\n")

        assertEquals(listOf("Private definition 'vector' is never used."), problems)
    }

    /**
     * A return tag on the name, upstream's own spelling (`docs/examples/04_functions-recursion.phel`). The name
     * is `shout`, not the tag in front of it, so the call counts and nothing is reported.
     */
    fun testDoesNotReportACalledPrivateDefinitionWhoseNameCarriesATag() {
        assertEmpty(privateDefinitions("(ns app\\a)\n(defn- ^string shout [^string m] (str m \"!\"))\n(defn run [] (shout \"hi\"))\n"))
    }

    fun testReportsAnUncalledPrivateDefinitionByItsNameNotItsTag() {
        val problems = privateDefinitions("(ns app\\a)\n(defn- ^string shout [^string m] (str m \"!\"))\n")

        assertEquals(listOf("Private definition 'shout' is never used."), problems)
    }

    /** A public definition may be called from another namespace, so its own file cannot judge it. */
    fun testDoesNotReportAPublicDefinition() {
        assertEmpty(privateDefinitions("(ns app\\a)\n(defn helper [x] (* x 2))\n"))
    }

    fun testRecognisesThePrivateMetadataFlag() {
        val problems = privateDefinitions("(ns app\\a)\n(def ^:private limit 10)\n")

        assertEquals(listOf("Private definition 'limit' is never used."), problems)
    }

    /** Reporting a recursive helper as unused would be wrong more often than the reverse. */
    fun testDoesNotReportARecursivePrivateDefinition() {
        assertEmpty(privateDefinitions("(ns app\\a)\n(defn- countdown [n] (if (zero? n) 0 (countdown (dec n))))\n"))
    }

    fun testDoesNotReportANestedDefinition() {
        assertEmpty(privateDefinitions("(ns app\\a)\n(defn run [] (let [helper 1] helper))\n"))
    }

    // ---- unused parameters ----

    private fun parameters(source: String) = inspect(PhelUnusedParameterInspection(), source)

    fun testReportsAParameterTheBodyNeverReads() {
        val problems = parameters("(defn greet [name greeting] (println name))\n")

        assertEquals(listOf("Parameter 'greeting' is never used."), problems)
    }

    fun testDoesNotReportAParameterTheBodyReads() {
        assertEmpty(parameters("(defn greet [name] (println name))\n"))
    }

    fun testReportsAParameterOnlyATypeTagSpells() {
        val problems = parameters("(defn greet [vector] (fn [^vector v] (println v)))\n")

        assertEquals(listOf("Parameter 'vector' is never used."), problems)
    }

    fun testDoesNotReportAnUnderscoreParameter() {
        assertEmpty(parameters("(defn greet [_ignored name] (println name))\n"))
    }

    fun testDoesNotReportARestMarker() {
        assertEmpty(parameters("(defn greet [name & rest] (println name rest))\n"))
    }

    fun testReportsInsideAnAnonymousFunction() {
        val problems = parameters("(defn run [] (map (fn [x y] x) []))\n")

        assertEquals(listOf("Parameter 'y' is never used."), problems)
    }

    /** No body means nothing reads anything; every parameter would otherwise look unused. */
    fun testDoesNotReportWhenThereIsNoBody() {
        assertEmpty(parameters("(defn greet [name])\n"))
    }

    fun testReportsEveryUnusedParameterInOrder() {
        val problems = parameters("(defn greet [a b c] (println b))\n")

        assertEquals(
            listOf("Parameter 'a' is never used.", "Parameter 'c' is never used."),
            problems,
        )
    }

    // ---- unused imports, now an inspection rather than an annotation ----

    private fun imports(source: String) = inspect(PhelUnusedImportInspection(), source)

    fun testReportsAnUnusedImport() {
        val problems = imports("(ns app\\a\n  (:require phel\\str))\n(println \"hi\")\n")

        assertEquals(listOf("Unused import"), problems)
    }

    fun testDoesNotReportAUsedImport() {
        assertEmpty(imports("(ns app\\a\n  (:require phel\\str))\n(str/join \", \" [1 2])\n"))
    }

    /** A namespace-shaped symbol in ordinary code is not an import. */
    fun testDoesNotReportOutsideTheNamespaceDeclaration() {
        assertEmpty(imports("(ns app\\a)\n(println \"phel\\str\")\n"))
    }
}
