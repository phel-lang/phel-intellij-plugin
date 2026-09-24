package org.phellang.integration.psi

import com.intellij.psi.util.PsiTreeUtil
import org.phellang.integration.PhelIntegrationTestCase
import org.phellang.language.psi.PhelSymbol
import org.phellang.language.psi.PhelTypeTags

/**
 * [PhelTypeTags.isTypeTag] against real PSI: the symbol after `^` is a type tag, whatever it spells, and no
 * other symbol is, including one that merely shares a tag's name.
 */
class PhelTypeTagPsiTest : PhelIntegrationTestCase() {

    fun testShortValueTagOnAParameterIsATag() {
        assertTrue(isTypeTag("(defn f [^map m] m)", "map"))
    }

    fun testTheParameterItTagsIsNotATag() {
        assertFalse(isTypeTag("(defn f [^map m] m)", "m", occurrence = 0))
    }

    fun testNullableSpellingsAreTags() {
        assertTrue(isTypeTag("(defn f [^?map m] m)", "?map"))
        assertTrue(isTypeTag("(defn f [^map|null m] m)", "map|null"))
    }

    fun testReturnTagOnADefinitionNameIsATag() {
        assertTrue(isTypeTag("(defn ^vector f [] [])", "vector"))
    }

    fun testClassTagIsATag() {
        assertTrue(isTypeTag("(defn f [^DateTime d] d)", "DateTime"))
    }

    fun testTagWithNothingAfterItIsStillATag() {
        assertTrue(isTypeTag("(defn f [^map])", "map"))
    }

    fun testCallHeadSharingATagNameIsNotATag() {
        assertFalse(isTypeTag("(map inc [1 2])", "map"))
    }

    fun testSymbolInsideMapMetadataIsNotATag() {
        assertFalse(isTypeTag("(def ^{:tag map} x {})", "map"))
    }

    private fun isTypeTag(code: String, symbolText: String, occurrence: Int = 0): Boolean {
        val file = myFixture.configureByText("tags.phel", code)
        val symbol = PsiTreeUtil.findChildrenOfType(file, PhelSymbol::class.java)
            .filter { it.text == symbolText }
            .getOrNull(occurrence)
            ?: error("no symbol '$symbolText' (occurrence $occurrence) in: $code")

        return PhelTypeTags.isTypeTag(symbol)
    }
}
