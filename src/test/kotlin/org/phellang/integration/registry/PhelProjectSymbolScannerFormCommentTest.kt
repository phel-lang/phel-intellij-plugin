package org.phellang.integration.registry

import org.phellang.integration.PhelIntegrationTestCase
import org.phellang.language.psi.files.PhelFile
import org.phellang.registry.indexing.PhelProjectSymbolScanner

/**
 * The scanner must read a definition's forms with `#_`-discarded forms stripped. Reading the raw
 * list shifts every positional slot, so a discarded form before the name indexes the wrong symbol
 * and misreads the signature — a silent wrong answer.
 */
class PhelProjectSymbolScannerFormCommentTest : PhelIntegrationTestCase() {

    private fun scan(def: String): List<PhelProjectSymbolLite> {
        val file = myFixture.configureByText("a.phel", "(ns my\\app)\n$def") as PhelFile
        return PhelProjectSymbolScanner.scanFile(file).map { PhelProjectSymbolLite(it.name, it.signature) }
    }

    private data class PhelProjectSymbolLite(val name: String, val signature: String)

    fun testDiscardedFormBeforeNameIndexesTheRealName() {
        val symbols = scan("(defn #_old-name new-name [x] x)")

        assertEquals(listOf("new-name"), symbols.map { it.name })
    }

    fun testStackedDiscardsBeforeNameAreSkipped() {
        val symbols = scan("(defn #_#_a b real [x] x)")

        assertEquals(listOf("real"), symbols.map { it.name })
    }

    fun testSignatureReadsCorrectSlotsPastADiscard() {
        val symbols = scan("(defn #_junk foo [a b] \"doc\" body)")

        assertEquals("foo", symbols.single().name)
        assertEquals("(foo a b)", symbols.single().signature)
    }

    fun testDiscardedParameterIsNotCountedInTheSignature() {
        val symbols = scan("(defn f [a #_b c] body)")

        assertEquals("(f a c)", symbols.single().signature)
    }

    fun testPlainDefinitionIsUnaffected() {
        val symbols = scan("(defn plain [x] x)")

        assertEquals("plain", symbols.single().name)
        assertEquals("(plain x)", symbols.single().signature)
    }
}
