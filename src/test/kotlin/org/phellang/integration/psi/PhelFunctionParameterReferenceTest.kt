package org.phellang.integration.psi

import com.intellij.psi.PsiManager
import org.phellang.integration.PhelIntegrationTestCase
import org.phellang.language.psi.PhelSymbol
import org.phellang.language.psi.files.PhelFile

/**
 * Ground-truth checks that go-to-declaration on a function parameter resolves to the
 * parameter vector that introduces it.
 *
 * The vector's position varies by form: `fn` carries it at index 1, while `defn` and friends
 * carry it after the name and past any docstring or metadata, so the resolver scans rather
 * than indexes. Nothing pinned that scan before, which left the whole
 * `findParameterVector` path uncovered.
 */
class PhelFunctionParameterReferenceTest : PhelIntegrationTestCase() {

    fun testDefnParameterResolves() {
        assertResolvesToParameter(
            form = "(defn f [alpha]\n  (println alpha))",
            usageMarker = "println alpha",
            paramMarker = "[alpha",
        )
    }

    fun testDefnParameterResolvesPastDocstring() {
        assertResolvesToParameter(
            form = "(defn f \"doc\" [beta]\n  (println beta))",
            usageMarker = "println beta",
            paramMarker = "[beta",
        )
    }

    fun testDefnParameterResolvesPastDocstringAndMetadata() {
        assertResolvesToParameter(
            form = "(defn f \"doc\" {:private true} [gamma]\n  (println gamma))",
            usageMarker = "println gamma",
            paramMarker = "[gamma",
        )
    }

    fun testFnParameterResolves() {
        assertResolvesToParameter(
            form = "(def g (fn [delta]\n  (println delta)))",
            usageMarker = "println delta",
            paramMarker = "[delta",
        )
    }

    fun testDefmacroParameterResolves() {
        assertResolvesToParameter(
            form = "(defmacro m [epsilon]\n  (println epsilon))",
            usageMarker = "println epsilon",
            paramMarker = "[epsilon",
        )
    }

    fun testSecondParameterResolves() {
        assertResolvesToParameter(
            form = "(defn f [zeta eta]\n  (println eta))",
            usageMarker = "println eta",
            paramMarker = "zeta eta",
            paramMarkerOffset = "zeta ".length,
        )
    }

    private fun assertResolvesToParameter(
        form: String,
        usageMarker: String,
        paramMarker: String,
        paramMarkerOffset: Int = 1, // default: skip the '[' in "[name"
    ) {
        // Unique path per class: the shared test project does not reliably clean files
        // between classes, so a shared path lets stale content leak in.
        val file = myFixture.addFileToProject("src/fn_param_test.phel", "(ns app\\main)\n$form\n")
        val phelFile = PsiManager.getInstance(project).findFile(file.virtualFile) as PhelFile
        val text = phelFile.text

        val resolveOffset = text.indexOf(usageMarker) + usageMarker.lastIndexOf(' ') + 1
        val resolved = phelFile.findReferenceAt(resolveOffset)?.resolve()

        assertTrue("usage should resolve to a symbol", resolved is PhelSymbol)
        assertEquals(
            "usage should resolve to the parameter declaration, not itself",
            text.indexOf(paramMarker) + paramMarkerOffset,
            (resolved as PhelSymbol).textOffset,
        )
    }
}
