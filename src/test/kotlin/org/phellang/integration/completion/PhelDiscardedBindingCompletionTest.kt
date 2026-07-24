package org.phellang.integration.completion

import org.phellang.integration.PhelIntegrationTestCase

/**
 * `#_` discards the form after it but leaves that form in the PSI tree, so counting a binding
 * vector's name/value pairs over raw children puts every later pair on the wrong parity and
 * drops its name. Completion counted that way and silently lost bindings declared after a
 * discarded entry.
 */
class PhelDiscardedBindingCompletionTest : PhelIntegrationTestCase() {

    fun testBindingAfterDiscardedPairIsOffered() {
        assertLocalsOffered(
            binding = "[alpha 1 #_[beta 2] gamma 3]",
            expected = listOf("alpha", "gamma"),
        )
    }

    fun testBindingAfterDiscardedNameAndValueIsOffered() {
        // `#_#_` stacks: two discards remove the name and its value, keeping the pairing intact.
        assertLocalsOffered(
            binding = "[delta 1 #_#_ epsilon 2 zeta 3]",
            expected = listOf("delta", "zeta"),
        )
    }

    fun testUndiscardedBindingsStillOffered() {
        assertLocalsOffered(
            binding = "[eta 1 theta 2]",
            expected = listOf("eta", "theta"),
        )
    }

    fun testDiscardedParameterIsNotOffered() {
        myFixture.configureByText(
            "discarded_param_test.phel",
            "(ns app\\main)\n(defn f [iota #_kappa lambda]\n  <caret>)\n",
        )

        val offered = myFixture.completeBasic()?.map { it.lookupString }.orEmpty()

        assertTrue("expected `iota` to be offered, got: $offered", "iota" in offered)
        assertTrue("expected `lambda` to be offered, got: $offered", "lambda" in offered)
        assertFalse("`kappa` is discarded by #_ and must not be offered", "kappa" in offered)
    }

    private fun assertLocalsOffered(binding: String, expected: List<String>) {
        myFixture.configureByText(
            "discarded_binding_test.phel",
            "(ns app\\main)\n(defn f []\n  (let $binding\n    <caret>))\n",
        )

        val offered = myFixture.completeBasic()?.map { it.lookupString }.orEmpty()

        for (name in expected) {
            assertTrue("expected `$name` to be offered, got: $offered", name in offered)
        }
    }
}
