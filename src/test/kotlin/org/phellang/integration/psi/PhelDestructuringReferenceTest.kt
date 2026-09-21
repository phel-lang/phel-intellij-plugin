package org.phellang.integration.psi

import com.intellij.psi.PsiManager
import org.phellang.integration.PhelIntegrationTestCase
import org.phellang.language.psi.PhelSymbol
import org.phellang.language.psi.files.PhelFile

/**
 * Go-to-declaration on a name bound by a destructuring pattern resolves to the symbol inside the
 * pattern, in `let` and in parameter vectors alike.
 *
 * The map cases pin the Clojure order Phel 0.51 adopted (phel-lang #3115): in `{n :name}` the
 * *left* side is the binding and the right side is the lookup key. Before the destructuring
 * analyzer existed the resolver took the first symbol found inside an entry, so `[a b]` resolved
 * only `a`, and a map pattern resolved whichever symbol happened to come first in the text.
 */
class PhelDestructuringReferenceTest : PhelIntegrationTestCase() {

    private var fileIndex = 0

    fun testBindingFirstMapPatternInLetResolves() {
        assertResolvesTo(
            body = "(let [{name :name} m]\n    (println name))",
            usage = "println name",
            declaration = "{name",
        )
    }

    fun testBindingFirstMapPatternInParametersResolves() {
        assertResolvesTo(
            body = "(fn [{id :id}] (println id))",
            usage = "println id",
            declaration = "{id",
        )
    }

    fun testNestedMapPatternResolves() {
        assertResolvesTo(
            body = "(let [{{:keys [city]} :address} m]\n    (println city))",
            usage = "println city",
            declaration = "[city",
        )
    }

    fun testNestedVectorInsideMapPatternResolves() {
        assertResolvesTo(
            body = "(let [{[lat lng] :coords} m]\n    (println lng))",
            usage = "println lng",
            declaration = "lat lng",
            declarationOffsetInMarker = 4,
        )
    }

    fun testSecondNameOfAVectorPatternResolves() {
        assertResolvesTo(
            body = "(let [[first-item second-item & others] xs]\n    (println second-item))",
            usage = "println second-item",
            declaration = "first-item second-item",
            declarationOffsetInMarker = "first-item ".length,
        )
    }

    fun testRestNameOfAVectorPatternResolves() {
        assertResolvesTo(
            body = "(let [[head & tail] xs]\n    (println tail))",
            usage = "println tail",
            declaration = "& tail",
            declarationOffsetInMarker = 2,
        )
    }

    fun testAsDirectiveResolves() {
        assertResolvesTo(
            body = "(let [{n :name :as whole} m]\n    (println whole))",
            usage = "println whole",
            declaration = ":as whole",
            declarationOffsetInMarker = 4,
        )
    }

    fun testStrsDirectiveResolves() {
        assertResolvesTo(
            body = "(defn g [{:strs [token]}] (println token))",
            usage = "println token",
            declaration = "[token",
        )
    }

    /** An `:or` default names a binding introduced elsewhere; the usage resolves to that binding, not to the default. */
    fun testUsageResolvesToTheBindingNotToItsOrDefault() {
        assertResolvesTo(
            body = "(let [{:keys [port] :or {port 80}} m]\n    (println port))",
            usage = "println port",
            declaration = "[port",
        )
    }

    /** Key-first still compiles in 0.52, so `{:name n}` must keep resolving `n` while it is reported deprecated. */
    fun testDeprecatedKeyFirstPatternStillResolves() {
        assertResolvesTo(
            body = "(let [{:name legacy} m]\n    (println legacy))",
            usage = "println legacy",
            declaration = ":name legacy",
            declarationOffsetInMarker = ":name ".length,
        )
    }

    /** `{k v}` binds on both sides, so the compiler reads it key-first: `v` is the binding and `k` is evaluated. */
    fun testAmbiguousPairBindsItsRightSide() {
        assertResolvesTo(
            body = "(let [{k v} m]\n    (println v))",
            usage = "println v",
            declaration = "{k v",
            declarationOffsetInMarker = 3,
        )
    }

    fun testTheLookupKeyOfABindingFirstPairIsNotADeclaration() {
        val file = load("(defn f [m]\n  (let [{n :name} m]\n    (println n)))\n")
        val text = file.text
        val keyOffset = text.indexOf(":name") + 1

        val symbols = com.intellij.psi.util.PsiTreeUtil.findChildrenOfType(file, PhelSymbol::class.java)
        assertTrue(
            "the keyword :name must not be a symbol declaration",
            symbols.none { it.textOffset == keyOffset },
        )
    }

    private fun assertResolvesTo(body: String, usage: String, declaration: String, declarationOffsetInMarker: Int = 1) {
        val file = load("(defn f [m xs]\n  $body)\n")
        val text = file.text

        val resolveOffset = text.indexOf(usage) + usage.lastIndexOf(' ') + 1
        val resolved = file.findReferenceAt(resolveOffset)?.resolve()

        assertTrue("usage should resolve to a symbol, got $resolved", resolved is PhelSymbol)
        val declarationOffset = text.indexOf(declaration) + declarationOffsetInMarker
        assertEquals(
            "usage should resolve to the pattern's binding occurrence",
            declarationOffset,
            (resolved as PhelSymbol).textOffset,
        )
    }

    private fun load(source: String): PhelFile {
        // Unique path per test: the shared light project does not reliably clean files between tests.
        val file = myFixture.addFileToProject("src/destructuring_${fileIndex++}.phel", "(ns app\\main)\n$source")
        return PsiManager.getInstance(project).findFile(file.virtualFile) as PhelFile
    }
}
