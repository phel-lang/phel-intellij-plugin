package org.phellang.integration.registry

import org.phellang.indexing.PhelProjectSymbolScanner
import org.phellang.integration.PhelIntegrationTestCase
import org.phellang.language.psi.files.PhelFile

/**
 * Signature rendering and docstring lookup, the two things an indexed symbol shows in completion and
 * hover. Both read positional slots of a definition, so the cases that matter are the ones where a
 * slot is optional: metadata maps, multi-arity bodies, and a vector used as a function.
 */
class PhelProjectSymbolSignatureTest : PhelIntegrationTestCase() {

    private fun scanOne(def: String) =
        PhelProjectSymbolScanner
            .scanFile(myFixture.configureByText("a.phel", "(ns my\\app)\n$def") as PhelFile)
            .single()

    private fun signatureOf(def: String) = scanOne(def).signature

    private fun docstringOf(def: String) = scanOne(def).docstring

    fun testNoArgumentFunctionRendersBareName() {
        assertEquals("(f)", signatureOf("(defn f [] 1)"))
    }

    fun testVariadicMarkerIsKeptInTheSignature() {
        assertEquals("(f a & rest)", signatureOf("(defn f [a & rest] rest)"))
    }

    fun testMultiArityRendersOneLinePerArity() {
        assertEquals("(f a)\n(f a b)", signatureOf("(defn f ([a] a) ([a b] b))"))
    }

    /**
     * `([10 20 30] i)` is a vector used as a function, not an arity clause. Reading it as one would
     * render `(idx 10 20 30)`, so the direct parameter vector has to win.
     */
    fun testVectorCalledAsAFunctionIsNotMistakenForAnArityClause() {
        assertEquals("(idx i)", signatureOf("(defn idx [i] ([10 20 30] i))"))
    }

    fun testMetadataMapDoesNotShiftTheParameterVector() {
        assertEquals("(f x)", signatureOf("(defn f {:added \"1.0\"} [x] x)"))
    }

    fun testStructRendersItsFields() {
        assertEquals("(point x y)", signatureOf("(defstruct point [x y])"))
    }

    fun testInterfaceRendersAnEllipsis() {
        assertEquals("(shape ...)", signatureOf("(definterface shape (area [this]))"))
    }

    fun testDefRendersNameOnly() {
        assertEquals("(answer)", signatureOf("(def answer 42)"))
    }

    fun testDocstringInTheCanonicalSlot() {
        assertEquals("what it does", docstringOf("(defn f \"what it does\" [x] x)"))
    }

    fun testDocstringAfterTheParameterVector() {
        assertEquals("body doc", docstringOf("(defn f [x] \"body doc\" x)"))
    }

    fun testDocstringFromAMetadataMap() {
        assertEquals("from meta", docstringOf("(def x {:doc \"from meta\"} 1)"))
    }

    fun testDefinitionWithoutADocstringHasNone() {
        assertNull(docstringOf("(defn f [x] x)"))
    }

    /** A string in the body is the docstring; a numeric body is not a string and must not be read as one. */
    fun testNonStringBodyIsNotReadAsADocstring() {
        assertNull(docstringOf("(def x 42)"))
    }
}
