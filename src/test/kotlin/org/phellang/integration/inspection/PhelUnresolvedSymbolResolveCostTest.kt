package org.phellang.integration.inspection

import com.intellij.codeInspection.InspectionManager
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiPolyVariantReference
import com.intellij.psi.PsiRecursiveElementVisitor
import com.intellij.psi.impl.source.resolve.ResolveCache
import com.intellij.psi.util.PsiTreeUtil
import org.phellang.inspection.PhelUnresolvedSymbolInspection
import org.phellang.integration.PhelIntegrationTestCase
import org.phellang.language.psi.PhelSymbol
import org.phellang.language.psi.analysis.PhelSymbolAnalyzer
import org.phellang.language.psi.files.PhelFile

/**
 * What the unresolved-symbol inspection costs, rather than what it reports.
 *
 * Resolving a reference scans every project file and walks the whole of `phel.core`: ~24 ms a symbol
 * on a realistic project. The inspection used to ask it first, for every bare symbol, so one file of
 * stdlib calls took ~11 s to highlight, again after every edit. A name the registry, the local scope
 * or the project index already answers must never reach it.
 *
 * Observed through the platform's [ResolveCache], which holds an entry for every reference that was
 * resolved since the last PSI change.
 */
class PhelUnresolvedSymbolResolveCostTest : PhelIntegrationTestCase() {

    private var fileIndex = 0

    private fun inspect(source: String): Pair<PhelFile, List<String>> {
        val file = myFixture.configureByText("c${fileIndex++}.phel", "(ns my\\app)\n$source") as PhelFile

        val holder = ProblemsHolder(InspectionManager.getInstance(project), file, true)
        val visitor = PhelUnresolvedSymbolInspection().buildVisitor(holder, true)
        file.accept(object : PsiRecursiveElementVisitor() {
            override fun visitElement(element: PsiElement) {
                element.accept(visitor)
                super.visitElement(element)
            }
        })

        return file to holder.results.map { it.descriptionTemplate }
    }

    /** The occurrences of [name] that read it, i.e. not the one that declares it. */
    private fun usagesOf(file: PhelFile, name: String): List<PhelSymbol> =
        PsiTreeUtil.findChildrenOfType(file, PhelSymbol::class.java)
            .filter { it.text == name && !PhelSymbolAnalyzer.isDefinition(it) }
            .also { assertTrue("no usage of '$name' in the fixture", it.isNotEmpty()) }

    private fun wasResolved(symbol: PhelSymbol): Boolean {
        val reference = symbol.reference as PsiPolyVariantReference
        return ResolveCache.getInstance(project)
            .getCachedResults(reference, symbol.isPhysical, /* incompleteCode = */ false, /* isPoly = */ true) != null
    }

    private fun assertNeverResolved(file: PhelFile, vararg names: String) {
        for (name in names) {
            usagesOf(file, name).forEach { assertFalse("'$name' reached the reference resolve", wasResolved(it)) }
        }
    }

    fun testStandardLibraryNamesNeverReachTheResolve() {
        val (file, problems) = inspect("(defn f [xs] (println (map inc (filter even? xs))))")

        assertEmpty(problems)
        assertNeverResolved(file, "println", "map", "inc", "filter", "even?")
    }

    fun testSpecialFormsNeverReachTheResolve() {
        val (file, problems) = inspect("(defn f [x] (if x (do 1) (when x 2)))")

        assertEmpty(problems)
        assertNeverResolved(file, "if", "do", "when")
    }

    fun testParametersNeverReachTheResolve() {
        val (file, problems) = inspect("(defn f [a b] (+ a b))")

        assertEmpty(problems)
        assertNeverResolved(file, "a", "b")
    }

    fun testFunctionsDefinedInTheFileNeverReachTheResolve() {
        val (file, problems) = inspect("(defn- helper [] 1)\n(defn f [] (helper))")

        assertEmpty(problems)
        assertNeverResolved(file, "helper")
    }

    /**
     * The resolve stays as the fallback: the project index skips private definitions and the local
     * function index covers only the `fn`/`defn` family, so a private value is answered by nothing
     * else. Also proves [wasResolved] can observe a resolve, so the assertions above are not vacuous.
     */
    fun testAPrivateValueStillResolvesThroughTheReference() {
        val (file, problems) = inspect("(def- secret 42)\n(defn f [] secret)")

        assertEmpty(problems)
        usagesOf(file, "secret").forEach { assertTrue("'secret' should resolve via the reference", wasResolved(it)) }
    }

    fun testAnUnknownNameIsStillReportedAfterTheResolve() {
        val (file, problems) = inspect("(defn f [] (nope))")

        assertEquals(listOf("Cannot resolve symbol 'nope'"), problems)
        usagesOf(file, "nope").forEach { assertTrue("'nope' should have been resolved before reporting", wasResolved(it)) }
    }
}
