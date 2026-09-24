package org.phellang.integration.documentation

import com.intellij.psi.util.PsiTreeUtil
import org.phellang.documentation.providers.PhelQuickNavigateInfoProvider
import org.phellang.integration.PhelIntegrationTestCase
import org.phellang.language.psi.PhelSymbol
import org.phellang.language.psi.PhelTypeTags
import org.phellang.language.psi.files.PhelFile

/**
 * What hovering a type tag shows. `^map` names a type, so the quick info (Ctrl-hover) may not describe the
 * `map` function it happens to spell.
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

    private fun configure(source: String): PhelFile =
        myFixture.configureByText("tag_hover${fileIndex++}.phel", source) as PhelFile

    private fun symbolIn(source: String, text: String): PhelSymbol =
        PsiTreeUtil.findChildrenOfType(configure(source), PhelSymbol::class.java).first { it.text == text }

    private fun tagIn(source: String, text: String): PhelSymbol =
        PsiTreeUtil.findChildrenOfType(configure(source), PhelSymbol::class.java)
            .single { it.text == text && PhelTypeTags.isTypeTag(it) }
}
