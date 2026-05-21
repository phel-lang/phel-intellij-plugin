package org.phellang.unit.refactoring

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.phellang.language.psi.PhelList
import org.phellang.language.psi.PhelSymbol
import org.phellang.refactoring.PhelRefactoringSupportProvider

class PhelRefactoringSupportProviderTest {

    private val provider = PhelRefactoringSupportProvider()

    @Test
    fun `inplace rename enabled for symbols`() {
        val symbol = mock(PhelSymbol::class.java)
        assertTrue(provider.isInplaceRenameAvailable(symbol, null))
        assertTrue(provider.isMemberInplaceRenameAvailable(symbol, null))
    }

    @Test
    fun `inplace rename disabled for non-symbols`() {
        val list = mock(PhelList::class.java)
        assertFalse(provider.isInplaceRenameAvailable(list, null))
        assertFalse(provider.isMemberInplaceRenameAvailable(list, null))
    }
}
