package org.phellang.integration.registry

import org.phellang.integration.PhelIntegrationTestCase
import org.phellang.language.psi.files.PhelFile
import org.phellang.registry.indexing.PhelProjectSymbolScanner

/**
 * Privacy detection must read the definition's metadata, never its body. Scanning body text for
 * `:private` silently dropped public symbols from the index, taking completion, navigation, arity
 * checking and hover docs with them.
 *
 * Phel marks a definition private four ways — the `defn-` family, a `^` flag on the name, and a
 * keyword or map in the metadata slot between the name and the value (`DefSymbol.php`: "metadata
 * form (string `:doc`, keyword flag, or map), then folds in flags attached to the name symbol").
 */
class PhelProjectSymbolScannerPrivacyTest : PhelIntegrationTestCase() {

    // --- stays indexed: the body merely mentions the word ---

    fun testBodyReferencingPrivateKeywordStaysIndexed() =
        assertIndexed("(defn parse-flags [] (get opts :private-mode))", "parse-flags")

    fun testDocstringMentioningPrivateStaysIndexed() =
        assertIndexed("""(defn visibility-of [x] "Returns :public or :private for x." x)""", "visibility-of")

    /** The map is the value here, not a metadata slot — there is no form after it. */
    fun testMapValueMentioningPrivateStaysIndexed() =
        assertIndexed("(def defaults {:visibility :private-ish})", "defaults")

    fun testExplicitlyNonPrivateMetadataStaysIndexed() =
        assertIndexed("(def ^{:private false} z 1)", "z")

    fun testPlainDefinitionIsIndexed() = assertIndexed("(defn plain [x] x)", "plain")

    // --- excluded: genuinely private ---

    fun testCaretKeywordFlagIsPrivate() = assertIndexed("(def ^:private a 1)")

    fun testCaretMapFlagIsPrivate() = assertIndexed("(def ^{:private true} b 1)")

    fun testCaretFlagOnDefmacroIsPrivate() = assertIndexed("(defmacro ^:private m [] 1)")

    fun testDefnDashIsPrivate() = assertIndexed("(defn- c [] 1)")

    fun testDefDashIsPrivate() = assertIndexed("(def- d 1)")

    fun testDefmacroDashIsPrivate() = assertIndexed("(defmacro- e [] 1)")

    /** `(def x :private 1)` — bare keyword in the metadata slot, per Phel's own def fixtures. */
    fun testPositionalKeywordFlagIsPrivate() = assertIndexed("(def f :private 1)")

    /** `(def name {:private true :doc "..."} value)` — the form phel core itself uses. */
    fun testPositionalMapFlagIsPrivate() = assertIndexed("""(def g {:private true :doc "d"} 1)""")

    fun testPositionalMapFlagOnDefnIsPrivate() = assertIndexed("(defn h {:private true} [x] x)")

    // --- the index still sees everything else in the file ---

    fun testPrivateDefinitionDoesNotHideItsNeighbours() = assertIndexed(
        """
        (def ^:private secret 1)
        (defn public-one [] secret)
        (defn- hidden [] 1)
        (defn public-two [] 2)
        """.trimIndent(),
        "public-one", "public-two"
    )

    /** Asserts scanning [source] yields exactly [expected] symbol names. */
    private fun assertIndexed(source: String, vararg expected: String) {
        val file = myFixture.configureByText("a.phel", "(ns my\\app)\n$source") as PhelFile
        val actual = PhelProjectSymbolScanner.scanFile(file).map { it.name }
        assertEquals("indexed symbols for `$source`", expected.toList(), actual)
    }
}
