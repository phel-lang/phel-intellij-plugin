package org.phellang.integration.actions

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
import org.phellang.actions.CreatePhelFileAction
import org.phellang.actions.namespace.PhelNamespaceGenerator
import org.phellang.actions.template.PhelFileTemplateHandler

@ExtendWith(MockitoExtension::class)
class CreatePhelFileActionTest {

    @Mock
    private lateinit var mockProject: Project

    @Mock
    private lateinit var mockDirectory: PsiDirectory

    @Mock
    private lateinit var mockVirtualFile: VirtualFile

    private lateinit var namespaceGenerator: PhelNamespaceGenerator
    private lateinit var templateHandler: PhelFileTemplateHandler
    private lateinit var action: CreatePhelFileAction

    @BeforeEach
    fun setUp() {
        namespaceGenerator = PhelNamespaceGenerator()
        templateHandler = PhelFileTemplateHandler()
        action = CreatePhelFileAction()
    }

    @Test
    fun `complete workflow should work with real components`() {
        val fileName = "UserService.phel"
        val projectBasePath = "/project/root"
        val directoryPath = "/project/root/src/main/services"

        `when`(mockDirectory.project).thenReturn(mockProject)
        `when`(mockProject.basePath).thenReturn(projectBasePath)
        `when`(mockDirectory.virtualFile).thenReturn(mockVirtualFile)
        `when`(mockVirtualFile.path).thenReturn(directoryPath)

        val expectedNamespace = "services\\UserService"

        val actualNamespace = namespaceGenerator.generateNamespace(mockDirectory, fileName)
        assertEquals(expectedNamespace, actualNamespace)
    }

    @Test
    fun `workflow should work with modules directory`() {
        val fileName = "SimpleModule.phel"
        val projectBasePath = "/project/root"
        val directoryPath = "/project/root/modules"

        `when`(mockDirectory.project).thenReturn(mockProject)
        `when`(mockProject.basePath).thenReturn(projectBasePath)
        `when`(mockDirectory.virtualFile).thenReturn(mockVirtualFile)
        `when`(mockVirtualFile.path).thenReturn(directoryPath)

        val actualNamespace = namespaceGenerator.generateNamespace(mockDirectory, fileName)
        assertEquals("modules\\SimpleModule", actualNamespace)
    }

    @Test
    fun `complete end-to-end workflow with various directory structures`() {
        val testCases = listOf(
            Triple("/project/root/src/main/services", "UserService.phel", "services\\UserService"),
            Triple("/project/root/public/assets", "Style.phel", "assets\\Style"),
            Triple("/project/root/tests/unit", "TestHelper.phel", "unit\\TestHelper"),
            Triple("/project/root", "App.phel", "App")
        )

        testCases.forEach { (directoryPath, fileName, expectedNamespace) ->
            `when`(mockDirectory.project).thenReturn(mockProject)
            `when`(mockProject.basePath).thenReturn("/project/root")
            `when`(mockDirectory.virtualFile).thenReturn(mockVirtualFile)
            `when`(mockVirtualFile.path).thenReturn(directoryPath)

            val actualNamespace = namespaceGenerator.generateNamespace(mockDirectory, fileName)
            assertEquals(expectedNamespace, actualNamespace, "Failed for directory: $directoryPath, file: $fileName")
        }
    }
}
