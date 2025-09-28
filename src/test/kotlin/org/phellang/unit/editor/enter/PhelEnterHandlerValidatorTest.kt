package org.phellang.unit.editor.enter

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import org.phellang.PhelFileType
import org.phellang.editor.enter.PhelEnterHandlerValidator
import com.intellij.psi.PsiFile
import com.intellij.openapi.fileTypes.FileType

@ExtendWith(MockitoExtension::class)
class PhelEnterHandlerValidatorTest {

    @Mock
    private lateinit var mockPsiFile: PsiFile

    @Mock
    private lateinit var mockFileType: FileType

    private lateinit var validator: PhelEnterHandlerValidator

    @BeforeEach
    fun setUp() {
        validator = PhelEnterHandlerValidator()
    }

    @Test
    fun `should return true for Phel files`() {
        `when`(mockPsiFile.fileType).thenReturn(PhelFileType.INSTANCE)

        val result = validator.shouldHandleFile(mockPsiFile)

        assertTrue(result)
    }

    @Test
    fun `should return false for non-Phel files`() {
        `when`(mockPsiFile.fileType).thenReturn(mockFileType)

        val result = validator.shouldHandleFile(mockPsiFile)

        assertFalse(result)
    }
}
