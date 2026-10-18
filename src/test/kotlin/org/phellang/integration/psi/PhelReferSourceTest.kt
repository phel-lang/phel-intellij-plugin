package org.phellang.integration.psi

import org.phellang.integration.PhelIntegrationTestCase
import org.phellang.language.psi.PhelNamespaceUtils
import org.phellang.language.psi.files.PhelFile

/**
 * Which namespace a bare name is `:refer`'d from.
 *
 * Resolution and hover docs both ask this. It returns the namespace spec whose `:refer [...]` lists
 * the name, within a clause that may hold several specs, so the clause's `:require` head is never
 * the answer.
 */
class PhelReferSourceTest : PhelIntegrationTestCase() {

    private var fileIndex = 0

    private fun referSource(ns: String, name: String): String? {
        val file = myFixture.configureByText("refer${fileIndex++}.phel", "$ns\n(defn f [] 1)\n") as PhelFile
        return PhelNamespaceUtils.findReferSource(file, name)
    }

    fun testASingleReferringSpec() {
        assertEquals("b.util", referSource("(ns a.main\n  (:require b.util :refer [helper]))", "helper"))
    }

    fun testTheSpecThatRefersTheNameAmongSeveralInOneClause() {
        val ns = "(ns a.main\n  (:require b.x :as x\n            c.y :refer [one two]\n            d.z :refer [three]))"

        assertEquals("c.y", referSource(ns, "two"))
        assertEquals("d.z", referSource(ns, "three"))
        assertNull("an alias is not a refer", referSource(ns, "x"))
    }

    fun testALaterRequireClause() {
        val ns = "(ns a.main\n  (:require b.x :refer [p])\n  (:require c.y :refer [q]))"

        assertEquals("c.y", referSource(ns, "q"))
    }

    fun testANameThatIsNotReferred() {
        assertNull(referSource("(ns a.main\n  (:require b.util :refer [helper]))", "other"))
    }

    fun testABackslashNamespaceIsReturnedAsWritten() {
        assertEquals("b\\util", referSource("(ns a\\main\n  (:require b\\util :refer [helper]))", "helper"))
    }

    /** The compiler also takes a vector entry, `[ns :refer [...]]` (`NsSymbol::analyzeRequireVectorEntry`). */
    fun testAVectorEntry() {
        assertEquals("b.util", referSource("(ns a.main\n  (:require [b.util :refer [helper]]))", "helper"))
    }

    /** A vector entry must not swallow the flat spec after it. */
    fun testAVectorEntryBeforeAFlatOne() {
        assertEquals("c.d", referSource("(ns a.main\n  (:require [a.b :as ab] c.d :refer [x]))", "x"))
    }

    /** `:refer` and `:as` combine in any order (the namespaces page of the Phel docs). */
    fun testReferBeforeAlias() {
        assertEquals("c.d", referSource("(ns a.main\n  (:require c.d :refer [x] :as cd))", "x"))
    }

    fun testADiscardedReferredNameIsNotReferred() {
        val ns = "(ns a.main\n  (:require b.util :refer [x #_y]))"

        assertEquals("b.util", referSource(ns, "x"))
        assertNull(referSource(ns, "y"))
    }

    fun testADiscardedClauseIsIgnored() {
        val ns = "(ns a.main\n  #_(:require b.util :refer [y])\n  (:require c.d :refer [z]))"

        assertNull(referSource(ns, "y"))
        assertEquals("c.d", referSource(ns, "z"))
    }
}
