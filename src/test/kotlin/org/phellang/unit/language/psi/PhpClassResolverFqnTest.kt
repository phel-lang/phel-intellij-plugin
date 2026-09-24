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

    // ---- type tags: which PHP classes `^tag` names ----

    private val useIndex = mapOf("DateTime" to "\\DateTime", "CompilerFacade" to "\\Phel.Compiler.CompilerFacade")

    @Test
    fun `a short value tag names the class Phel backs it with, nullable or not`() {
        val map = listOf("\\Phel\\Lang\\Collections\\Map\\PersistentMapInterface")
        assertEquals(map, PhpClassResolver.typeTagFqns("map", useIndex))
        assertEquals(map, PhpClassResolver.typeTagFqns("?map", useIndex))
        assertEquals(map, PhpClassResolver.typeTagFqns("map|null", useIndex))
    }

    @Test
    fun `a bare class name resolves through the use table`() {
        assertEquals(listOf("\\DateTime"), PhpClassResolver.typeTagFqns("DateTime", useIndex))
        assertEquals(listOf("\\Phel\\Compiler\\CompilerFacade"), PhpClassResolver.typeTagFqns("CompilerFacade", useIndex))
    }

    @Test
    fun `dotted and rooted class names are taken as written`() {
        assertEquals(listOf("\\Phel\\Lang\\Keyword"), PhpClassResolver.typeTagFqns("Phel.Lang.Keyword", useIndex))
        assertEquals(listOf("\\DateTimeImmutable"), PhpClassResolver.typeTagFqns("\\DateTimeImmutable", useIndex))
    }

    @Test
    fun `each class member of a union or intersection is named`() {
        assertEquals(
            listOf("\\DateTime", "\\Phel\\Compiler\\CompilerFacade"),
            PhpClassResolver.typeTagFqns("DateTime&CompilerFacade", useIndex),
        )
    }

    @Test
    fun `a PHP type or a bare name nothing imports names no class`() {
        assertEquals(emptyList<String>(), PhpClassResolver.typeTagFqns("int", useIndex))
        assertEquals(emptyList<String>(), PhpClassResolver.typeTagFqns("?string", useIndex))
        assertEquals(emptyList<String>(), PhpClassResolver.typeTagFqns("Unimported", useIndex))
    }

    @Test
    fun `blank input yields null`() {
        assertNull(PhpClassResolver.phpFqnFromUseEntry(""))
        assertNull(PhpClassResolver.phpFqnFromUseEntry("   "))
        assertNull(PhpClassResolver.phpFqnFromUseEntry("\\"))
    }
}
