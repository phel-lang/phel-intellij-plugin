package org.phellang.integration.editor

import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import org.phellang.editor.breadcrumbs.PhelBreadcrumbsProvider
import org.phellang.integration.PhelIntegrationTestCase
import org.phellang.language.psi.PhelList

class PhelBreadcrumbsProviderTest : PhelIntegrationTestCase() {

    private val provider = PhelBreadcrumbsProvider()

    /** The crumbs the platform would build for the innermost form, outermost first. */
    private fun trailAt(text: String, marker: String): List<String> {
        val file = myFixture.configureByText("core.phel", text)
        val offset = text.indexOf(marker)
        val leaf = file.findElementAt(offset) ?: error("no element at $marker")

        return generateSequence<PsiElement>(leaf) { it.parent }
            .filter { provider.acceptElement(it) }
            .map { provider.getElementInfo(it) }
            .toList()
            .reversed()
    }

    fun testNamesTheEnclosingDefinition() {
        assertEquals(listOf("defn greet"), trailAt("(defn greet [] 42)\n", "42"))
    }

    fun testShowsNestedBindingForms() {
        val trail = trailAt("(defn greet []\n  (let [a 1]\n    (when a 42)))\n", "42")

        assertEquals(listOf("defn greet", "let", "when"), trail)
    }

    fun testDoesNotCrumbAPlainCall() {
        assertEquals(listOf("defn greet"), trailAt("(defn greet [] (println 42))\n", "42"))
    }

    fun testNamesADefWithoutAParameterVector() {
        assertEquals(listOf("def limit"), trailAt("(def limit 42)\n", "42"))
    }

    fun testRendersAnAnonymousFunctionWithoutAName() {
        val trail = trailAt("(defn greet [] (map (fn [x] 42) []))\n", "42")

        assertEquals(listOf("defn greet", "fn"), trail)
    }

    fun testAcceptsOnlyLists() {
        val file = myFixture.configureByText("core.phel", "(defn greet [] 42)\n")
        val nonLists = PsiTreeUtil.collectElements(file) { it !is PhelList && provider.acceptElement(it) }

        assertEmpty(nonLists.toList())
    }

    fun testDeclaresThePhelLanguage() {
        assertEquals(1, provider.languages.size)
        assertEquals("Phel", provider.languages.single().id)
    }
}
