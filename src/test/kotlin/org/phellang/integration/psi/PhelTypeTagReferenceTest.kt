package org.phellang.integration.psi

import com.intellij.psi.PsiPolyVariantReference
import com.intellij.psi.util.PsiTreeUtil
import org.phellang.indexing.PhelProjectSymbolIndex
import org.phellang.integration.PhelIntegrationTestCase
import org.phellang.language.psi.PhelSymbol
import org.phellang.language.psi.PhelTypeTags
import org.phellang.language.psi.analysis.PhelSymbolAnalyzer
import org.phellang.language.psi.files.PhelFile

/**
 * A type tag names a type, so it resolves only to PHP (the PHP plugin is not on the test classpath, so here
 * that means to nothing) and is never a usage of a Phel name it happens to spell. Both directions go through
 * `PhelReference`: a tag resolving to a definition, and a definition listing a tag among its usages.
 */
class PhelTypeTagReferenceTest : PhelIntegrationTestCase() {

    private var fileIndex = 0

    fun testTagDoesNotResolveToADefinitionOfTheSameName() {
        val file = configure("(ns app\\t)\n(defn map [] 1)\n(defn f [^map m] m)\n")

        assertEmpty(targetsOf(tag(file, "map")))
    }

    fun testCallOfTheSameNameStillResolvesToTheDefinition() {
        val file = configure("(ns app\\t)\n(defn map [] 1)\n(defn f [^map m] (map))\n")
        val call = symbols(file, "map").last()

        assertFalse(PhelTypeTags.isTypeTag(call))
        assertEquals(listOf(definition(file, "map")), targetsOf(call))
    }

    fun testTagDoesNotResolveToALetBindingOfTheSameName() {
        val file = configure("(ns app\\t)\n(defn f [] (let [atom 1] (fn [^atom x] x)))\n")

        assertEmpty(targetsOf(tag(file, "atom")))
    }

    fun testDefinitionUsagesLeaveTheTagOut() {
        val file = configure("(ns app\\t)\n(defn map [] 1)\n(defn f [^map m] (map))\n")
        val usages = targetsOf(definition(file, "map"))

        assertEquals(listOf(symbols(file, "map").last()), usages)
    }

    fun testBindingUsagesLeaveTheTagOut() {
        val file = configure("(ns app\\t)\n(defn f [] (let [atom 1] (fn [^atom x] (+ atom x))))\n")
        val binding = symbols(file, "atom").first()
        val usages = targetsOf(binding)

        assertEquals(listOf(symbols(file, "atom").last()), usages)
    }

    /**
     * A return tag on a definition's name, `(defn ^map build ...)`. The name is `build`; reading the tag as
     * the name made `map` a definition, so every `(map ...)` call resolved to the tag and `(build)` to nothing.
     */
    fun testTagOnADefinitionNameIsNotTheDefinedName() {
        val file = configure("(ns app\\t)\n(defn ^map build [] {})\n(defn g [xs] [(build) (map inc xs)])\n")

        assertFalse(PhelSymbolAnalyzer.isDefinition(tag(file, "map")))
        assertTrue(PhelSymbolAnalyzer.isDefinition(definition(file, "build")))
        assertEquals(listOf(definition(file, "build")), targetsOf(symbols(file, "build").last()))
        assertFalse(targetsOf(symbols(file, "map").last()).contains(tag(file, "map")))
    }

    fun testTagOffersNoVariants() {
        val file = configure("(ns app\\t)\n(defn map [] 1)\n(defn f [^map m] m)\n")

        assertEmpty(tag(file, "map").reference!!.variants)
    }

    private fun configure(source: String): PhelFile {
        val file = myFixture.configureByText("tag_ref${fileIndex++}.phel", source) as PhelFile
        // PhelReference resolves project-wide through the index; prime it so the result does not depend
        // on which test ran first.
        PhelProjectSymbolIndex.getInstance(project).refreshFileFromPsi(file)

        return file
    }

    private fun symbols(file: PhelFile, text: String): List<PhelSymbol> =
        PsiTreeUtil.findChildrenOfType(file, PhelSymbol::class.java).filter { it.text == text }

    private fun tag(file: PhelFile, text: String): PhelSymbol =
        symbols(file, text).single(PhelTypeTags::isTypeTag)

    /** The name symbol of the definition form, i.e. the first occurrence. */
    private fun definition(file: PhelFile, text: String): PhelSymbol = symbols(file, text).first()

    private fun targetsOf(symbol: PhelSymbol): List<Any?> =
        (symbol.reference as PsiPolyVariantReference).multiResolve(false).map { it.element }
}
