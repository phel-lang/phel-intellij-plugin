package org.phellang.integration.annotator

import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.psi.util.PsiTreeUtil
import org.phellang.core.highlighting.PhelAnnotationConstants.FUNCTION_CALL
import org.phellang.core.highlighting.PhelAnnotationConstants.FUNCTION_PARAMETER
import org.phellang.core.highlighting.PhelTextAttributesRegistry.METADATA
import org.phellang.integration.PhelIntegrationTestCase
import org.phellang.language.psi.PhelSymbol
import org.phellang.language.psi.PhelTypeTags

/**
 * A type tag is painted as metadata, the colour its `^` already has, whatever it spells. `^map` used to be
 * painted as a call to the `map` function it shares a name with.
 */
class PhelTypeTagHighlightingTest : PhelIntegrationTestCase() {

    private var fileIndex = 0

    fun testShortValueTagIsPaintedAsMetadata() {
        assertEquals(METADATA, colourOfTag("(defn f [^map m] m)", "map"))
    }

    fun testNullableAndUnionTagsArePaintedAsMetadata() {
        assertEquals(METADATA, colourOfTag("(defn f [^?vector v] v)", "?vector"))
        assertEquals(METADATA, colourOfTag("(defn f [^map|null m] m)", "map|null"))
    }

    fun testClassTagIsPaintedAsMetadata() {
        assertEquals(METADATA, colourOfTag("(ns app\\t (:use DateTime))\n(defn f [^DateTime d] d)", "DateTime"))
    }

    fun testTagOnADefinitionNameIsPaintedAsMetadata() {
        assertEquals(METADATA, colourOfTag("(defn ^map f [] {})", "map"))
    }

    fun testCallOfTheSameNameIsStillAFunctionCall() {
        val colours = coloursIn("(defn f [^map m] (map inc m))")

        assertEquals(FUNCTION_CALL, colours.single { (symbol, _) -> symbol.text == "map" && !PhelTypeTags.isTypeTag(symbol) }.second)
    }

    fun testTheTaggedParameterKeepsTheParameterColour() {
        val colours = coloursIn("(defn f [^map m] m)")

        assertEquals(FUNCTION_PARAMETER, colours.first { (symbol, _) -> symbol.text == "m" }.second)
    }

    private fun colourOfTag(source: String, tagText: String): TextAttributesKey? =
        coloursIn(source).single { (symbol, _) -> symbol.text == tagText && PhelTypeTags.isTypeTag(symbol) }.second

    /** Every symbol in [source] with the attributes the annotator forced on exactly its range, if any. */
    private fun coloursIn(source: String): List<Pair<PhelSymbol, TextAttributesKey?>> {
        val file = myFixture.configureByText("tag_colour${fileIndex++}.phel", source)
        val highlights = myFixture.doHighlighting().filter { it.forcedTextAttributesKey != null }

        return PsiTreeUtil.findChildrenOfType(file, PhelSymbol::class.java).map { symbol ->
            val range = symbol.textRange
            symbol to highlights
                .firstOrNull { it.startOffset == range.startOffset && it.endOffset == range.endOffset }
                ?.forcedTextAttributesKey
        }
    }
}
