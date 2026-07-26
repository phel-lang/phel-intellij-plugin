package org.phellang.unit.completion

import com.intellij.codeInsight.lookup.CharFilter
import com.intellij.codeInsight.lookup.Lookup
import com.intellij.openapi.fileTypes.FileType
import com.intellij.openapi.fileTypes.PlainTextFileType
import com.intellij.psi.PsiFile
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.phellang.completion.PhelCompletionCharFilter
import org.phellang.language.infrastructure.PhelFileType

/**
 * Which keystrokes keep a lookup open.
 *
 * Character membership itself belongs to `PhelSymbolChars` and is pinned by `PhelSymbolCharsTest`;
 * what is asserted here is the filter's own behaviour — the file-type gate, and that a symbol
 * character extends the prefix rather than committing the lookup.
 */
class PhelCompletionCharFilterTest {

    private val charFilter = PhelCompletionCharFilter()

    private fun acceptCharInPhelFile(c: Char): CharFilter.Result? =
        charFilter.acceptChar(c, 0, lookupIn(PhelFileType.INSTANCE))

    private fun lookupIn(fileType: FileType): Lookup {
        val file = mock(PsiFile::class.java)
        `when`(file.fileType).thenReturn(fileType)

        val lookup = mock(Lookup::class.java)
        `when`(lookup.psiFile).thenReturn(file)

        return lookup
    }

    @ParameterizedTest
    @ValueSource(chars = ['a', 'Z', '0', '/', ':', '-', '_', '?', '!', '*', '+', '<', '>', '=', '&', '%', '$'])
    fun `a symbol character extends the prefix`(c: Char) {
        assertEquals(CharFilter.Result.ADD_TO_PREFIX, acceptCharInPhelFile(c))
    }

    /**
     * The two the filter's own character list left out, so the lookup closed on the first keystroke
     * of the very shorthands completion offers: `.method` / `.-field`, and `\DateTime` from `(:use)`.
     */
    @ParameterizedTest
    @ValueSource(chars = ['.', '\\'])
    fun `the interop shorthand characters keep the lookup open`(c: Char) {
        assertEquals(CharFilter.Result.ADD_TO_PREFIX, acceptCharInPhelFile(c))
    }

    /** Null defers to the platform, which commits the lookup and types the character. */
    @ParameterizedTest
    @ValueSource(chars = ['(', ')', '[', ']', '{', '}', ' ', '\n', '\t', ',', '"', ';', '`', '@', '~', '^'])
    fun `a delimiter defers to the platform`(c: Char) {
        assertNull(acceptCharInPhelFile(c))
    }

    /** The filter is registered application-wide, so it must decline every other language. */
    @Test
    fun `a non-Phel file is left alone`() {
        assertNull(charFilter.acceptChar('a', 0, lookupIn(PlainTextFileType.INSTANCE)))
    }

    @Test
    fun `a lookup with no file is left alone`() {
        val lookup = mock(Lookup::class.java)
        `when`(lookup.psiFile).thenReturn(null)

        assertNull(charFilter.acceptChar('a', 0, lookup))
    }
}
