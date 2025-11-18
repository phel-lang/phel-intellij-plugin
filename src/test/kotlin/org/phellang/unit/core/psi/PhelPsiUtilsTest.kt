package org.phellang.unit.core.psi

import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
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
    fun `toString should return form text`() {
        val form = mock(PhelForm::class.java)
        `when`(form.text).thenReturn("(defn test [])")

        val result = PhelPsiUtils.toString(form)

        assertEquals("(defn test [])", result)
    }

    @Test
    fun `toString should return empty string for null form text`() {
        val form = mock(PhelForm::class.java)
        `when`(form.text).thenReturn(null)

        val result = PhelPsiUtils.toString(form)

        assertEquals("", result)
    }

    @Test
    fun `toString should return metadata text`() {
        val metadata = mock(PhelMetadata::class.java)
        `when`(metadata.text).thenReturn("^:private")

        val result = PhelPsiUtils.toString(metadata)

        assertEquals("^:private", result)
    }

    @Test
    fun `toString should return reader macro text`() {
        val readerMacro = mock(PhelReaderMacro::class.java)
        `when`(readerMacro.text).thenReturn("'")

        val result = PhelPsiUtils.toString(readerMacro)

        assertEquals("'", result)
    }

    @Test
    fun `getTextOffset should return list start offset`() {
        val list = mock(PhelList::class.java)
        val textRange = TextRange(15, 50)

        `when`(list.textRange).thenReturn(textRange)

        val result = PhelPsiUtils.getTextOffset(list)

        assertEquals(15, result)
    }

    @Test
    fun `getFirst should return first form in list`() {
        val list = mock(PhelList::class.java)
        val firstForm = mock(PhelForm::class.java)
        val secondForm = mock(PhelForm::class.java)

        `when`(list.children).thenReturn(arrayOf(firstForm, secondForm))

        val result = PhelPsiUtils.getFirst(list)

        assertEquals(firstForm, result)
    }

    @Test
    fun `getFirst should return null for empty list`() {
        val list = mock(PhelList::class.java)

        `when`(list.children).thenReturn(emptyArray())

        val result = PhelPsiUtils.getFirst(list)

        assertNull(result)
    }

    @Test
    fun `getLiteralType should recognize boolean literals`() {
        val literal = mock(PhelLiteral::class.java)

        `when`(literal.text).thenReturn("true")
        assertEquals("boolean", PhelPsiUtils.getLiteralType(literal))

        `when`(literal.text).thenReturn("false")
        assertEquals("boolean", PhelPsiUtils.getLiteralType(literal))
    }

    @Test
    fun `getLiteralType should recognize nil literal`() {
        val literal = mock(PhelLiteral::class.java)
        `when`(literal.text).thenReturn("nil")

        val result = PhelPsiUtils.getLiteralType(literal)

        assertEquals("nil", result)
    }

    @Test
    fun `getLiteralType should recognize nan literal`() {
        val literal = mock(PhelLiteral::class.java)
        `when`(literal.text).thenReturn("NAN")

        val result = PhelPsiUtils.getLiteralType(literal)

        assertEquals("nan", result)
    }

    @Test
    fun `getLiteralType should recognize string literals`() {
        val literal = mock(PhelLiteral::class.java)
        `when`(literal.text).thenReturn("\"hello world\"")

        val result = PhelPsiUtils.getLiteralType(literal)

        assertEquals("string", result)
    }

    @Test
    fun `getLiteralType should recognize character literals`() {
        val literal = mock(PhelLiteral::class.java)
        `when`(literal.text).thenReturn("\\a")

        val result = PhelPsiUtils.getLiteralType(literal)

        assertEquals("char", result)
    }

    @Test
    fun `getLiteralType should recognize integer literals`() {
        val literal = mock(PhelLiteral::class.java)

        `when`(literal.text).thenReturn("42")
        assertEquals("integer", PhelPsiUtils.getLiteralType(literal))

        `when`(literal.text).thenReturn("+100")
        assertEquals("integer", PhelPsiUtils.getLiteralType(literal))

        `when`(literal.text).thenReturn("-25")
        assertEquals("integer", PhelPsiUtils.getLiteralType(literal))
    }

    @Test
    fun `getLiteralType should recognize float literals`() {
        val literal = mock(PhelLiteral::class.java)

        `when`(literal.text).thenReturn("3.14")
        assertEquals("float", PhelPsiUtils.getLiteralType(literal))

        `when`(literal.text).thenReturn("1.5e10")
        assertEquals("float", PhelPsiUtils.getLiteralType(literal))

        `when`(literal.text).thenReturn("-2.5E-3")
        assertEquals("float", PhelPsiUtils.getLiteralType(literal))
    }

    @Test
    fun `getLiteralType should recognize hexnum literals`() {
        val literal = mock(PhelLiteral::class.java)

        `when`(literal.text).thenReturn("0xFF")
        assertEquals("hexnum", PhelPsiUtils.getLiteralType(literal))

        `when`(literal.text).thenReturn("+0x1A2B")
        assertEquals("hexnum", PhelPsiUtils.getLiteralType(literal))
    }

    @Test
    fun `getLiteralType should recognize binnum literals`() {
        val literal = mock(PhelLiteral::class.java)

        `when`(literal.text).thenReturn("0b1010")
        assertEquals("binnum", PhelPsiUtils.getLiteralType(literal))

        `when`(literal.text).thenReturn("-0b1111_0000")
        assertEquals("binnum", PhelPsiUtils.getLiteralType(literal))
    }

    @Test
    fun `getLiteralType should recognize octnum literals`() {
        val literal = mock(PhelLiteral::class.java)

        `when`(literal.text).thenReturn("0o755")
        assertEquals("octnum", PhelPsiUtils.getLiteralType(literal))

        `when`(literal.text).thenReturn("+0o644")
        assertEquals("octnum", PhelPsiUtils.getLiteralType(literal))
    }

    @Test
    fun `getLiteralType should return unknown for unrecognized literal`() {
        val literal = mock(PhelLiteral::class.java)
        `when`(literal.text).thenReturn("???")

        val result = PhelPsiUtils.getLiteralType(literal)

        assertEquals("unknown", result)
    }

    @Test
    fun `getLiteralText should return literal text`() {
        val literal = mock(PhelLiteral::class.java)
        `when`(literal.text).thenReturn("42")

        val result = PhelPsiUtils.getLiteralText(literal)

        assertEquals("42", result)
    }

    @Test
    fun `getLiteralText should return empty string for null text`() {
        val literal = mock(PhelLiteral::class.java)
        `when`(literal.text).thenReturn(null)

        val result = PhelPsiUtils.getLiteralText(literal)

        assertEquals("", result)
    }

    @Test
    fun `getFirst should skip non-form children`() {
        val list = mock(PhelList::class.java)
        val nonForm = mock(PsiElement::class.java)
        val firstForm = mock(PhelForm::class.java)

        `when`(list.children).thenReturn(arrayOf(nonForm, firstForm))

        val result = PhelPsiUtils.getFirst(list)

        assertEquals(firstForm, result)
    }

    @Test
    fun `utils methods should be callable from Java`() {
        // Verify @JvmStatic annotations work
        assertDoesNotThrow {
            PhelPsiUtils::class.java.getDeclaredMethod("getName", PhelSymbol::class.java)
            PhelPsiUtils::class.java.getDeclaredMethod("getQualifier", PhelSymbol::class.java)
            PhelPsiUtils::class.java.getDeclaredMethod("getNameTextOffset", PhelSymbol::class.java)
            PhelPsiUtils::class.java.getDeclaredMethod("toString", PhelForm::class.java)
            PhelPsiUtils::class.java.getDeclaredMethod("toString", PhelMetadata::class.java)
            PhelPsiUtils::class.java.getDeclaredMethod("toString", PhelReaderMacro::class.java)
            PhelPsiUtils::class.java.getDeclaredMethod("getTextOffset", PhelList::class.java)
            PhelPsiUtils::class.java.getDeclaredMethod("getFirst", PhelList::class.java)
            PhelPsiUtils::class.java.getDeclaredMethod("getLiteralType", PhelLiteral::class.java)
            PhelPsiUtils::class.java.getDeclaredMethod("getLiteralText", PhelLiteral::class.java)
        }
    }
}
