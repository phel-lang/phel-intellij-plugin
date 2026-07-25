package org.phellang.integration.psi

import org.phellang.integration.PhelIntegrationTestCase

/**
 * What go-to-definition treats as a definition.
 *
 * Both directions are load-bearing. `PhelDefinitionFinder` used to fall back onto the SPECIAL_FORMS
 * and MACROS completion priorities, which are *ranking* buckets holding `do`, `try`, `throw`,
 * `when`, `cond`, `and`, `or` and `->` — so a bare symbol argued to one of those registered as a
 * definition of itself, and navigation landed on an arbitrary usage. Narrowing the gate has to keep
 * every real definition form resolving, which is why the first test enumerates all of them.
 */
class PhelDefinitionResolutionTest : PhelIntegrationTestCase() {

    private var fileIndex = 0

    /** The line the last occurrence of [needle] resolves to, or null when it resolves nowhere. */
    private fun resolvedLineOf(text: String, needle: String): Int? {
        // A fresh file per call: reusing one name across configureByText leaves a later file with
        // empty PSI, which reads as "unresolved" and would pass or fail for the wrong reason.
        myFixture.configureByText("r${fileIndex++}.phel", text)

        val reference = myFixture.file.findReferenceAt(myFixture.file.text.lastIndexOf(needle)) ?: return null
        val target = reference.resolve() ?: return null

        return myFixture.file.text.take(target.textOffset).count { it == '\n' } + 1
    }

    fun testEveryDefinitionFormResolves() {
        val cases = listOf(
            "(def alpha 1)" to "alpha",
            "(defn beta [] 1)" to "beta",
            "(defn- beta2 [] 1)" to "beta2",
            "(defmacro gamma [] 1)" to "gamma",
            "(defstruct delta [a])" to "delta",
            "(definterface epsilon (m [this]))" to "epsilon",
            "(defexception zeta)" to "zeta",
            "(declare eta)" to "eta",
            "(defonce theta 1)" to "theta",
            "(defenum iota :a :b)" to "iota",
            "(defrecord kappa [a])" to "kappa",
            "(deftype lambda [a])" to "lambda",
            "(defprotocol mu (m [this]))" to "mu",
            "(defmulti nu (fn [x] x))" to "nu",
        )

        for ((definition, name) in cases) {
            val line = resolvedLineOf("(ns my\\app)\n$definition\n(defn user [] ($name))", name)

            assertEquals("$definition should resolve to its own definition on line 2", 2, line)
        }
    }

    /**
     * `defonce` is the one that needed the named set rather than the fallback: the generated
     * registry classifies it CORE_FUNCTIONS while every other `def*` form is MACROS, so neither
     * the hand-written list nor the priority fallback reached it.
     */
    fun testDefonceResolves() {
        val line = resolvedLineOf("(ns my\\app)\n(defonce cached 1)\n(defn user [] (cached))", "cached")

        assertEquals(2, line)
    }

    fun testArgumentsOfNonDefiningFormsAreNotDefinitions() {
        for (form in listOf("do", "try", "throw", "recur", "when", "cond", "and", "or", "->", "let")) {
            val line = resolvedLineOf("(ns my\\app)\n(defn a [] ($form zzz))\n(defn b [] (zzz))", "zzz")

            assertNull("($form zzz) must not make `zzz` a definition, resolved to line $line", line)
        }
    }

    /** Narrowing the definition gate must not disturb the scope-based resolvers beside it. */
    fun testLocalAndSameFileResolutionAreUnaffected() {
        assertEquals(2, resolvedLineOf("(ns my\\app)\n(defn f [prm] (prm))", "prm"))
        assertEquals(2, resolvedLineOf("(ns my\\app)\n(defn f [] (let [lb 1] (lb)))", "lb"))
        assertEquals(2, resolvedLineOf("(ns my\\app)\n(defn helper [] 1)\n(defn g [] (helper))", "helper"))
    }
}
