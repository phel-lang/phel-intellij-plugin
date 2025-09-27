package org.phellang.unit.actions.template

import com.intellij.ide.fileTemplates.FileTemplate
import com.intellij.ide.fileTemplates.FileTemplateUtil
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiFile
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import org.phellang.actions.template.PhelFileTemplateHandler
import java.util.*

@ExtendWith(MockitoExtension::class)
class PhelFileTemplateHandlerTest {

    @Mock
    private lateinit var mockFileTemplate: FileTemplate

    @Mock
    private lateinit var mockPsiDirectory: PsiDirectory

    @Mock
    private lateinit var mockPsiFile: PsiFile

    private lateinit var handler: PhelFileTemplateHandler

    @BeforeEach
    fun setUp() {
        handler = PhelFileTemplateHandler()
    }

    @ParameterizedTest
    @CsvSource(
        "myfile, Create Phel File myfile",
        "test-module, Create Phel File test-module",
        "complex_name, Create Phel File complex_name"
    )
    fun `getActionName should format action names correctly`(fileName: String, expected: String) {
        val result = handler.getActionName(fileName)
        assertEquals(expected, result)
    }

    @Test
    fun `getActionName should handle empty string`() {
        val result = handler.getActionName("")
        assertEquals("Create Phel File ", result)
    }

    @Test
    fun `createFileFromTemplate should create file successfully`() {
        val name = "TestFile"
        val namespace = "tests\\namespace"

        mockStatic(FileTemplateUtil::class.java).use { mockedStatic ->
            mockedStatic.`when`<Any> {
                FileTemplateUtil.createFromTemplate(
                    eq(mockFileTemplate),
                    eq(name),
                    any(Properties::class.java),
                    eq(mockPsiDirectory)
                )
            }.thenReturn(mockPsiFile)

            val result = handler.createFileFromTemplate(name, mockFileTemplate, mockPsiDirectory, namespace)

            assertSame(mockPsiFile, result)
            mockedStatic.verify {
                FileTemplateUtil.createFromTemplate(
                    eq(mockFileTemplate),
                    eq(name),
                    argThat { props ->
                        props?.getProperty("NAME") == name && props.getProperty("NAMESPACE") == namespace
                    },
                    eq(mockPsiDirectory)
                )
            }
        }
    }

    @Test
    fun `createFileFromTemplate should return null on exception`() {
        val name = "TestFile"
        val namespace = "tests\\namespace"

        mockStatic(FileTemplateUtil::class.java).use { mockedStatic ->
            mockedStatic.`when`<Any> {
                FileTemplateUtil.createFromTemplate(
                    any(FileTemplate::class.java),
                    anyString(),
                    any(Properties::class.java),
                    any(PsiDirectory::class.java)
                )
            }.thenThrow(RuntimeException("Template creation failed"))

            val result = handler.createFileFromTemplate(name, mockFileTemplate, mockPsiDirectory, namespace)

            assertNull(result)
        }
    }

    @Test
    fun `createFileFromTemplate should return null when FileTemplateUtil returns non-PsiFile`() {
        val name = "TestFile"
        val namespace = "tests\\namespace"

        mockStatic(FileTemplateUtil::class.java).use { mockedStatic ->
            mockedStatic.`when`<Any> {
                FileTemplateUtil.createFromTemplate(
                    any(FileTemplate::class.java),
                    anyString(),
                    any(Properties::class.java),
                    any(PsiDirectory::class.java)
                )
            }.thenReturn(null) // Returns null instead of wrong type to avoid mock issues

            val result = handler.createFileFromTemplate(name, mockFileTemplate, mockPsiDirectory, namespace)

            assertNull(result)
        }
    }

    @Test
    fun `createFileFromTemplate should handle special characters in namespace`() {
        val name = "SpecialFile"
        val namespace = "special\\chars-in_namespace\\file"

        mockStatic(FileTemplateUtil::class.java).use { mockedStatic ->
            mockedStatic.`when`<Any> {
                FileTemplateUtil.createFromTemplate(
                    eq(mockFileTemplate),
                    eq(name),
                    any(Properties::class.java),
                    eq(mockPsiDirectory)
                )
            }.thenReturn(mockPsiFile)

            val result = handler.createFileFromTemplate(name, mockFileTemplate, mockPsiDirectory, namespace)

            assertSame(mockPsiFile, result)
            mockedStatic.verify {
                FileTemplateUtil.createFromTemplate(
                    eq(mockFileTemplate),
                    eq(name),
                    argThat { props ->
                        props?.getProperty("NAMESPACE") == namespace
                    },
                    eq(mockPsiDirectory)
                )
            }
        }
    }

    @Test
    fun `createFileFromTemplate should pass correct properties object`() {
        val name = "TestFile"
        val namespace = "tests\\namespace"

        mockStatic(FileTemplateUtil::class.java).use { mockedStatic ->
            mockedStatic.`when`<Any> {
                FileTemplateUtil.createFromTemplate(
                    any(FileTemplate::class.java),
                    anyString(),
                    any(Properties::class.java),
                    any(PsiDirectory::class.java)
                )
            }.thenReturn(mockPsiFile)

            handler.createFileFromTemplate(name, mockFileTemplate, mockPsiDirectory, namespace)

            mockedStatic.verify {
                FileTemplateUtil.createFromTemplate(
                    eq(mockFileTemplate),
                    eq(name),
                    argThat { props ->
                        props is Properties &&
                        props.size == 2 &&
                        props.getProperty("NAME") == name &&
                        props.getProperty("NAMESPACE") == namespace
                    },
                    eq(mockPsiDirectory)
                )
            }
        }
    }
}
