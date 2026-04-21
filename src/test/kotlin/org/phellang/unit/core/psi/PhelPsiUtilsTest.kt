package org.phellang.unit.core.psi

import com.intellij.openapi.util.TextRange
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.phellang.core.psi.PhelPsiUtils
import org.phellang.language.psi.*

class PhelPsiUtilsTest {

    @Test
    fun `getName should return simple symbol name`() {
        val symbol = mock(PhelSymbol::class.java)
        `when`(symbol.text).thenReturn("my-symbol")

        val result = PhelPsiUtils.getName(symbol)

        assertEquals("my-symbol", result)
    }

    @Test
    fun `getName should return name part of qualified symbol`() {
        val symbol = mock(PhelSymbol::class.java)
        `when`(symbol.text).thenReturn("namespace/function-name")

        val result = PhelPsiUtils.getName(symbol)

        assertEquals("function-name", result)
    }

    @Test
    fun `getName should handle nested namespace`() {
        val symbol = mock(PhelSymbol::class.java)
        `when`(symbol.text).thenReturn("core\\str/upper-case")

        val result = PhelPsiUtils.getName(symbol)

        assertEquals("upper-case", result)
    }

    @Test
    fun `getName should return null for null text`() {
        val symbol = mock(PhelSymbol::class.java)
        `when`(symbol.text).thenReturn(null)

        val result = PhelPsiUtils.getName(symbol)

        assertNull(result)
    }

    @Test
    fun `getName should handle trailing slash`() {
        val symbol = mock(PhelSymbol::class.java)
        `when`(symbol.text).thenReturn("namespace/")

        val result = PhelPsiUtils.getName(symbol)

        assertEquals("namespace/", result)
    }

    @Test
    fun `getQualifier should return null for unqualified symbol`() {
        val symbol = mock(PhelSymbol::class.java)
        `when`(symbol.text).thenReturn("simple")

        val result = PhelPsiUtils.getQualifier(symbol)

        assertNull(result)
    }

    @Test
    fun `getQualifier should return namespace for qualified symbol`() {
        val symbol = mock(PhelSymbol::class.java)
        `when`(symbol.text).thenReturn("namespace/function")

        val result = PhelPsiUtils.getQualifier(symbol)

        assertEquals("namespace", result)
    }

    @Test
    fun `getQualifier should return full namespace for nested qualified symbol`() {
        val symbol = mock(PhelSymbol::class.java)
        `when`(symbol.text).thenReturn("core\\str/upper-case")

        val result = PhelPsiUtils.getQualifier(symbol)

        assertEquals("core\\str", result)
    }

    @Test
    fun `getQualifier should return null for leading slash`() {
        val symbol = mock(PhelSymbol::class.java)
        `when`(symbol.text).thenReturn("/symbol")

        val result = PhelPsiUtils.getQualifier(symbol)

        assertNull(result)
    }

    @Test
    fun `getNameTextOffset should return start offset for simple symbol`() {
        val symbol = mock(PhelSymbol::class.java)
        val textRange = TextRange(10, 20)

        `when`(symbol.text).thenReturn("simple")
        `when`(symbol.textRange).thenReturn(textRange)

        val result = PhelPsiUtils.getNameTextOffset(symbol)

        assertEquals(10, result)
    }

    @Test
    fun `getNameTextOffset should return offset after slash for qualified symbol`() {
        val symbol = mock(PhelSymbol::class.java)
        val textRange = TextRange(10, 30)

        `when`(symbol.text).thenReturn("namespace/function")
        `when`(symbol.textRange).thenReturn(textRange)

        val result = PhelPsiUtils.getNameTextOffset(symbol)

        assertEquals(20, result) // 10 + "namespace/".length
    }

    @Test
    fun `utils methods should be callable from Java`() {
        assertDoesNotThrow {
            PhelPsiUtils::class.java.getDeclaredMethod("findTopmostSymbol", com.intellij.psi.PsiElement::class.java)
            PhelPsiUtils::class.java.getDeclaredMethod("getName", PhelSymbol::class.java)
            PhelPsiUtils::class.java.getDeclaredMethod("getQualifier", PhelSymbol::class.java)
            PhelPsiUtils::class.java.getDeclaredMethod("getNameTextOffset", PhelSymbol::class.java)
        }
    }
}
