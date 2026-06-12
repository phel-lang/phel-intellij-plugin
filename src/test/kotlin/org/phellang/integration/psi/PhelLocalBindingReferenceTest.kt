package org.phellang.integration.psi

import com.intellij.psi.PsiManager
import org.phellang.integration.PhelIntegrationTestCase
import org.phellang.language.psi.PhelSymbol
import org.phellang.language.psi.files.PhelFile

/**
 * Ground-truth checks that go-to-declaration on a symbol bound by a let-like form
 * resolves to its binding. Covers `when-let`, whose bindings previously failed to
 * resolve because the resolver's binding-form list omitted it.
 */
class PhelLocalBindingReferenceTest : PhelIntegrationTestCase() {

    fun testWhenLetBindingResolves() {
        assertResolvesToBinding(
            body = "(when-let [foo 1]\n    (println foo))",
            usageMarker = "println foo",
            bindingMarker = "[foo",
        )
    }

    fun testIfLetBindingResolves() {
        assertResolvesToBinding(
            body = "(if-let [bar 1]\n    (println bar)\n    nil)",
            usageMarker = "println bar",
            bindingMarker = "[bar",
        )
    }

    fun testLetBindingResolves() {
        assertResolvesToBinding(
            body = "(let [baz 1]\n    (println baz))",
            usageMarker = "println baz",
            bindingMarker = "[baz",
        )
    }

    fun testLoopBindingResolves() {
        assertResolvesToBinding(
            body = "(loop [qux 1]\n    (println qux))",
            usageMarker = "println qux",
            bindingMarker = "[qux",
        )
    }

    private fun assertResolvesToBinding(body: String, usageMarker: String, bindingMarker: String) {
        // Unique path per class: the shared test project does not reliably clean files
        // between classes, so a shared path (src/main.phel) lets stale content leak in.
        val file = myFixture.addFileToProject("src/local_binding_test.phel", "(ns app\\main)\n(defn f []\n  $body)\n")
        val phelFile = PsiManager.getInstance(project).findFile(file.virtualFile) as PhelFile
        val text = phelFile.text

        // Resolve at the symbol used in the body (the word after the last space in the marker,
        // e.g. the `foo` in `(println foo)`).
        val resolveOffset = text.indexOf(usageMarker) + usageMarker.lastIndexOf(' ') + 1
        val resolved = phelFile.findReferenceAt(resolveOffset)?.resolve()

        assertTrue("usage should resolve to a symbol", resolved is PhelSymbol)
        val bindingOffset = text.indexOf(bindingMarker) + 1 // skip the '['
        assertEquals(
            "usage should resolve to the binding occurrence, not itself",
            bindingOffset,
            (resolved as PhelSymbol).textOffset,
        )
    }
}
