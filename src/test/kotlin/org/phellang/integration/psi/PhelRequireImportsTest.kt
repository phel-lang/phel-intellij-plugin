package org.phellang.integration.psi

import org.phellang.integration.PhelIntegrationTestCase
import org.phellang.language.psi.PhelNamespaceUtils
import org.phellang.language.psi.files.PhelFile

/**
 * The namespaces a file's `(:require ...)` clauses import, and their aliases.
 *
 * A clause holds flat specs (`a.b :as x :refer [y]`) and vector specs (`[a.b :as x :refer [y]]`),
 * with the options in any order. A `:refer [...]` vector is an option of the spec before it, never a
 * namespace of its own.
 */
class PhelRequireImportsTest : PhelIntegrationTestCase() {

    private var fileIndex = 0

    private fun file(ns: String): PhelFile =
        myFixture.configureByText("imports${fileIndex++}.phel", "$ns\n(defn f [] 1)\n") as PhelFile

    private fun imported(ns: String): List<String> = PhelNamespaceUtils.extractImports(file(ns)).map { it.fullNamespace }

    fun testAVectorEntryKeepsItsAlias() {
        assertEquals("util", PhelNamespaceUtils.extractAliasMap(file("(ns a.main\n  (:require [b.util :as u]))"))["u"])
    }

    fun testAnAliasAfterARefer() {
        assertEquals("d", PhelNamespaceUtils.extractAliasMap(file("(ns a.main\n  (:require c.d :refer [x] :as cd))"))["cd"])
    }

    fun testAReferVectorIsNotAnImportedNamespace() {
        assertEquals(listOf("c.d"), imported("(ns a.main\n  (:require c.d :refer [x]))"))
    }

    fun testFlatAndVectorEntriesInOneClause() {
        assertEquals(listOf("a.b", "c.d", "e.f"), imported("(ns a.main\n  (:require [a.b :as ab] c.d :refer [x] e.f))"))
    }

    fun testADiscardedSpecIsNotImported() {
        assertEquals(listOf("c.d"), imported("(ns a.main\n  (:require #_a.b c.d))"))
    }

    fun testTheImportsOfAFileAreComputedOnce() {
        val file = file("(ns a.main\n  (:require c.d))")

        assertSame(PhelNamespaceUtils.extractImports(file), PhelNamespaceUtils.extractImports(file))
    }
}
