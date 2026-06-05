package org.phellang.integration.psi

import com.intellij.psi.PsiManager
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import org.phellang.language.psi.PhelNamespaceUtils
import org.phellang.language.psi.PhelSymbol
import org.phellang.language.psi.files.PhelFile

/**
 * Verifies the PSI shape and detection that wire go-to-definition from PHP class
 * entries inside `(:use ...)` to their PHP class declaration (issue #120).
 *
 * The PHP plugin isn't on the test classpath, so the actual jump into PHP source
 * can't be asserted here — these tests pin the two load-bearing facts instead:
 *  1. each `:use` entry (bare / dot / backslash) lexes as a single [PhelSymbol]
 *     carrying the full class path as its text;
 *  2. [PhelNamespaceUtils.isUseClassSymbol] recognises those entries and nothing else.
 */
class PhelUseClassNavigationTest : BasePlatformTestCase() {

    fun testDotFormUseEntryIsSingleSymbol() {
        val symbol = symbolFor(
            "(ns app\\main\n  (:use Phel.Compiler.CompilerFacade))\n",
            "Phel.Compiler.CompilerFacade"
        )
        assertEquals("Phel.Compiler.CompilerFacade", symbol.text)
        assertTrue("dot-form :use entry should be a use-class symbol", PhelNamespaceUtils.isUseClassSymbol(symbol))
    }

    fun testBackslashFormUseEntryIsSingleSymbol() {
        val symbol = symbolFor(
            "(ns app\\main\n  (:use Phel\\Compiler\\CompilerFacade))\n",
            "Phel\\Compiler\\CompilerFacade"
        )
        assertEquals("Phel\\Compiler\\CompilerFacade", symbol.text)
        assertTrue(PhelNamespaceUtils.isUseClassSymbol(symbol))
    }

    fun testBareClassUseEntryIsRecognised() {
        val symbol = symbolFor(
            "(ns app\\main\n  (:use Countable Traversable))\n",
            "Countable"
        )
        assertEquals("Countable", symbol.text)
        assertTrue(PhelNamespaceUtils.isUseClassSymbol(symbol))
    }

    fun testMultipleEntriesInOneUseClauseAllRecognised() {
        val phelFile = phelFileFor("(ns app\\main\n  (:use Countable Traversable InvalidArgumentException))\n")
        for (entry in listOf("Countable", "Traversable", "InvalidArgumentException")) {
            assertTrue(
                "$entry should be a use-class symbol",
                PhelNamespaceUtils.isUseClassSymbol(symbolIn(phelFile, entry))
            )
        }
    }

    fun testRequireNamespaceIsNotAUseClassSymbol() {
        val symbol = symbolFor(
            "(ns app\\main\n  (:require app\\util))\n",
            "app\\util"
        )
        assertFalse(":require namespace must not be treated as a :use class", PhelNamespaceUtils.isUseClassSymbol(symbol))
    }

    fun testBareUseEntryDoesNotResolveToSameNamedInFileUsage() {
        // A bare `(:use Foo)` shares its text with the interop usage `(php/new Foo)`.
        // The reference must resolve only to a PHP class (absent here → no target), never
        // to the in-file usage symbol — otherwise Cmd+B would jump to the usage instead.
        val file = myFixture.addFileToProject(
            "src/main.phel",
            "(ns app\\main\n  (:use Foo))\n(defn bar [] (php/new Foo))\n"
        )
        val phelFile = PsiManager.getInstance(project).findFile(file.virtualFile) as PhelFile
        val useOffset = phelFile.text.indexOf("Foo))") // the `:use Foo` entry
        val resolved = phelFile.findReferenceAt(useOffset + 1)?.resolve()
        assertNull("(:use Foo) must not resolve to an in-file usage of Foo", resolved)
    }

    private fun symbolFor(source: String, entry: String): PhelSymbol = symbolIn(phelFileFor(source), entry)

    private fun phelFileFor(source: String): PhelFile {
        val file = myFixture.addFileToProject("src/main.phel", source)
        return PsiManager.getInstance(project).findFile(file.virtualFile) as PhelFile
    }

    private fun symbolIn(phelFile: PhelFile, entry: String): PhelSymbol {
        val offset = phelFile.text.indexOf(entry)
        assertTrue("entry '$entry' not found in source", offset >= 0)
        val element = phelFile.findElementAt(offset + 1)
        return PsiTreeUtil.getParentOfType(element, PhelSymbol::class.java, false)
            ?: error("no PhelSymbol at '$entry'")
    }
}
