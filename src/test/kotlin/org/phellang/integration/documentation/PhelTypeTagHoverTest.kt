package org.phellang.integration.documentation

import com.intellij.psi.util.PsiTreeUtil
import org.phellang.documentation.providers.PhelQuickNavigateInfoProvider
import org.phellang.documentation.resolvers.PhelSymbolDocumentationResolver
import org.phellang.integration.PhelIntegrationTestCase
import org.phellang.language.psi.PhelSymbol
import org.phellang.language.psi.PhelTypeTags
import org.phellang.language.psi.files.PhelFile

/**
 * What hovering a type tag shows. `^map` names a type, so neither the quick info (Ctrl-hover) nor the
 * documentation popup may describe the `map` function it happens to spell.
 */
class PhelTypeTagHoverTest : PhelIntegrationTestCase() {

    private var fileIndex = 0

    fun testQuickInfoOnATagIsNotAFunctionSignature() {
        val tag = tagIn("(ns app\\t)\n(defn f [^map m] m)\n", "map")

        assertNull(PhelQuickNavigateInfoProvider().getQuickNavigateInfo(tag))
    }

    fun testQuickInfoOnACallOfTheSameNameIsStillTheSignature() {
        val call = symbolIn("(ns app\\t)\n(defn f [xs] (map inc xs))\n", "map")

        val info = PhelQuickNavigateInfoProvider().getQuickNavigateInfo(call)

        assertNotNull(info)
        assertTrue(info!!, info.startsWith("map "))
    }

    fun testHoverOnAValueTagNamesItsClassAndNotTheFunction() {
        val doc = hover("(ns app\\t)\n(defn f [^map m] m)\n", "map")

        assertTrue(doc, doc.contains("Phel\\Lang\\Collections\\Map\\PersistentMapInterface"))
        assertFalse(doc, doc.contains("(map f coll)"))
    }

    fun testHoverOnANullableScalarTag() {
        val doc = hover("(ns app\\t)\n(defn f [^?int n] n)\n", "?int")

        assertTrue(doc, doc.contains("<code>int</code>: nullable PHP type"))
    }

    fun testHoverOnAUnionTagDescribesEachMember() {
        val doc = hover("(ns app\\t)\n(defn f [^map|null m] m)\n", "map|null")

        assertTrue(doc, doc.contains("<code>map</code>: Phel value type"))
        assertTrue(doc, doc.contains("<code>null</code>: PHP type"))
    }

    fun testHoverOnAClassTag() {
        val doc = hover("(ns app\\t (:use DateTime))\n(defn f [^DateTime d] d)\n", "DateTime")

        assertTrue(doc, doc.contains("<code>DateTime</code>: PHP class"))
    }

    fun testHoverOnACallOfTheSameNameIsStillTheFunction() {
        val call = symbolIn("(ns app\\t)\n(defn f [xs] (map inc xs))\n", "map")

        val doc = PhelSymbolDocumentationResolver().resolveDocumentation(call, call)

        assertNotNull(doc)
        assertTrue(doc!!, doc.contains("(map f coll)"))
    }

    private fun hover(source: String, tagText: String): String {
        val tag = tagIn(source, tagText)
        return PhelSymbolDocumentationResolver().resolveDocumentation(tag, tag) ?: error("no hover for ^$tagText")
    }

    private fun configure(source: String): PhelFile =
        myFixture.configureByText("tag_hover${fileIndex++}.phel", source) as PhelFile

    private fun symbolIn(source: String, text: String): PhelSymbol =
        PsiTreeUtil.findChildrenOfType(configure(source), PhelSymbol::class.java).first { it.text == text }

    private fun tagIn(source: String, text: String): PhelSymbol =
        PsiTreeUtil.findChildrenOfType(configure(source), PhelSymbol::class.java)
            .single { it.text == text && PhelTypeTags.isTypeTag(it) }
}
