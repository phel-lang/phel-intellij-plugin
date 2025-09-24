package org.phellang.actions

import com.intellij.ide.actions.CreateFileFromTemplateAction
import com.intellij.ide.actions.CreateFileFromTemplateDialog
import com.intellij.ide.fileTemplates.FileTemplate
import com.intellij.ide.fileTemplates.FileTemplateUtil
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiFile
import org.phellang.PhelIcons
import java.util.*

class CreatePhelFileAction : CreateFileFromTemplateAction("Phel File", "Create new Phel file", PhelIcons.FILE),
    DumbAware {

    override fun getActionName(directory: PsiDirectory?, newName: String, templateName: String?): String {
        return "Create Phel File $newName"
    }

    override fun buildDialog(project: Project, directory: PsiDirectory, builder: CreateFileFromTemplateDialog.Builder) {
        builder.setTitle("New Phel File").addKind("Phel file", PhelIcons.FILE, "Phel_File.phel")
    }

    override fun createFileFromTemplate(name: String, template: FileTemplate, dir: PsiDirectory): PsiFile? {
        val properties = Properties()
        properties.setProperty("NAME", name)
        properties.setProperty("NAMESPACE", generateNamespace(dir, name))

        return try {
            FileTemplateUtil.createFromTemplate(template, name, properties, dir) as? PsiFile
        } catch (_: Exception) {
            null
        }
    }

    private fun generateNamespace(dir: PsiDirectory, fileName: String): String {
        val project = dir.project
        val projectBasePath = project.basePath

        if (projectBasePath == null) {
            val nameWithoutExt = fileName.substringBeforeLast('.')
            return nameWithoutExt
        }

        val mainNamespace = readMainNamespaceFromConfig(project)

        val currentPath = dir.virtualFile.path
        val relativePath = if (currentPath.startsWith(projectBasePath)) {
            currentPath.substring(projectBasePath.length).trimStart('/')
        } else {
            dir.name
        }

        val pathParts = relativePath.split('/').filter { it.isNotEmpty() }.toMutableList()

        val skipCommonRootDirectories = listOf("src", "main", "resources", "phel", "public", "root", "test")
        while (pathParts.isNotEmpty() && pathParts[0] in skipCommonRootDirectories) {
            pathParts.removeAt(0)
        }

        val nameWithoutExt = fileName.substringBeforeLast('.')
        pathParts.add(nameWithoutExt)

        return if (mainNamespace != null) {
            val mainParts = mainNamespace.split('\\')
            (mainParts + pathParts).joinToString("\\")
        } else {
            pathParts.joinToString("\\")
        }
    }

    private fun readMainNamespaceFromConfig(project: Project): String? {
        val basePath = project.basePath ?: return null
        val projectRoot = LocalFileSystem.getInstance().findFileByPath(basePath) ?: return null
        val configFile = projectRoot.findChild("phel-config.php") ?: return null

        try {
            val content = String(configFile.contentsToByteArray())
            val regex = Regex("""setMainPhelNamespace\s*\(\s*['"]([^'"]+)['"]\s*\)""", RegexOption.MULTILINE)
            val match = regex.find(content)
            return match?.groupValues?.get(1)
        } catch (_: Exception) {
            return null
        }
    }
}
