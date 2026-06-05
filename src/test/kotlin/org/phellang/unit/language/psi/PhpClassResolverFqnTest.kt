package org.phellang.unit.language.psi

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.phellang.language.psi.references.PhpClassResolver

/**
 * Pure-string checks for [PhpClassResolver.phpFqnFromUseEntry] — the normalisation
 * that powers go-to-definition from a `(:use ...)` class entry to its PHP class.
 */
class PhpClassResolverFqnTest {

    @Test
    fun `dot-separated entry folds to backslash absolute FQN`() {
        assertEquals(
            "\\Phel\\Compiler\\CompilerFacade",
            PhpClassResolver.phpFqnFromUseEntry("Phel.Compiler.CompilerFacade")
        )
    }

    @Test
    fun `legacy backslash entry becomes absolute FQN`() {
        assertEquals(
            "\\Phel\\Compiler\\CompilerFacade",
            PhpClassResolver.phpFqnFromUseEntry("Phel\\Compiler\\CompilerFacade")
        )
    }

    @Test
    fun `already-absolute entry is left intact`() {
        assertEquals(
            "\\Phel\\Compiler\\CompilerFacade",
            PhpClassResolver.phpFqnFromUseEntry("\\Phel\\Compiler\\CompilerFacade")
        )
    }

    @Test
    fun `bare class name gets a leading backslash`() {
        assertEquals("\\Countable", PhpClassResolver.phpFqnFromUseEntry("Countable"))
        assertEquals("\\InvalidArgumentException", PhpClassResolver.phpFqnFromUseEntry("InvalidArgumentException"))
    }

    @Test
    fun `blank input yields null`() {
        assertNull(PhpClassResolver.phpFqnFromUseEntry(""))
        assertNull(PhpClassResolver.phpFqnFromUseEntry("   "))
        assertNull(PhpClassResolver.phpFqnFromUseEntry("\\"))
    }
}
