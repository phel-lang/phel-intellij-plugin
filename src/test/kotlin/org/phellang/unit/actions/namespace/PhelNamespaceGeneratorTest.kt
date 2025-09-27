package org.phellang.unit.actions.namespace

import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiDirectory
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import org.phellang.actions.namespace.PhelNamespaceGenerator

@ExtendWith(MockitoExtension::class)
class PhelNamespaceGeneratorTest {

    @Mock
    private lateinit var mockProject: Project

    @Mock
    private lateinit var mockDirectory: PsiDirectory

    @Mock
    private lateinit var mockVirtualFile: VirtualFile

    private lateinit var generator: PhelNamespaceGenerator

    @BeforeEach
    fun setUp() {
        generator = PhelNamespaceGenerator()
    }

    @Test
    fun `generateNamespace should integrate all components correctly`() {
        val projectBasePath = "/project/root"
        val directoryPath = "/project/root/src/main/my/module"
        val fileName = "MyFile.phel"

        `when`(mockDirectory.project).thenReturn(mockProject)
        `when`(mockProject.basePath).thenReturn(projectBasePath)
        `when`(mockDirectory.virtualFile).thenReturn(mockVirtualFile)
        `when`(mockVirtualFile.path).thenReturn(directoryPath)

        val result = generator.generateNamespace(mockDirectory, fileName)
        assertEquals("my\\module\\MyFile", result)
    }

    @Test
    fun `generateNamespace should handle null project base path`() {
        val fileName = "MyFile.phel"

        `when`(mockDirectory.project).thenReturn(mockProject)
        `when`(mockProject.basePath).thenReturn(null)

        val result = generator.generateNamespace(mockDirectory, fileName)
        assertEquals("MyFile", result)
    }

    @Test
    fun `generateNamespace should handle root directory`() {
        val projectBasePath = "/project/root"
        val directoryPath = "/project/root"
        val fileName = "App.phel"

        `when`(mockDirectory.project).thenReturn(mockProject)
        `when`(mockProject.basePath).thenReturn(projectBasePath)
        `when`(mockDirectory.virtualFile).thenReturn(mockVirtualFile)
        `when`(mockVirtualFile.path).thenReturn(directoryPath)

        val result = generator.generateNamespace(mockDirectory, fileName)
        assertEquals("App", result)
    }

    @Test
    fun `generateNamespace should handle deep nesting`() {
        val projectBasePath = "/project/root"
        val directoryPath = "/project/root/src/main/deep/nested/structure/module"
        val fileName = "DeepModule.phel"

        `when`(mockDirectory.project).thenReturn(mockProject)
        `when`(mockProject.basePath).thenReturn(projectBasePath)
        `when`(mockDirectory.virtualFile).thenReturn(mockVirtualFile)
        `when`(mockVirtualFile.path).thenReturn(directoryPath)

        val result = generator.generateNamespace(mockDirectory, fileName)
        assertEquals("deep\\nested\\structure\\module\\DeepModule", result)
    }
}
