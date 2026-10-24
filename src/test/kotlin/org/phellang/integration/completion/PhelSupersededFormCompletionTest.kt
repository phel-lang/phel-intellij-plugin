package org.phellang.integration.completion

import org.phellang.integration.PhelIntegrationTestCase

/**
 * Completion leaves out the source forms Phel 0.52 removed: `php/new`, `php/->`, `php/::` and
 * `set-var`. The compiler rejects each with `PHEL012`, and the Superseded form inspection flags it,
 * yet completion still offered them, the `php/` ones labelled "Deprecated" (#350).
 */
class PhelSupersededFormCompletionTest : PhelIntegrationTestCase() {

    private var fileIndex = 0

    /** What completion offers at the caret; a sole candidate is inserted, so its text counts too. */
    private fun offered(source: String): List<String> {
        myFixture.configureByText("superseded${fileIndex++}.phel", "(ns app.m)\n$source\n")
        myFixture.completeBasic()

        return myFixture.lookupElementStrings ?: listOf(myFixture.file.text)
    }

    fun testPhpNewIsNotOffered() {
        assertTrue(offered("(php/ne<caret>)").none { "php/new" in it })
    }

    fun testPhpArrowIsNotOffered() {
        assertTrue(offered("(php/-<caret>)").none { "php/->" in it })
    }

    fun testPhpStaticCallIsNotOffered() {
        assertTrue(offered("(php/:<caret>)").none { "php/::" in it })
    }

    fun testSetVarIsNotOffered() {
        assertTrue(offered("(set-va<caret>)").none { "set-var" in it })
    }

    /** The rest of the `php/` family is current: each reaches a PHP capability Phel has no other word for. */
    fun testCurrentPhpFormsAreStillOffered() {
        assertContainsElements(offered("(php/age<caret>)"), "php/aget", "php/aget-in")
    }
}
