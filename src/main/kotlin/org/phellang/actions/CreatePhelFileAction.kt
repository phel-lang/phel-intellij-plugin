package org.phellang.actions

import com.intellij.ide.actions.CreateFileFromTemplateAction
import com.intellij.ide.actions.CreateFileFromTemplateDialog
import com.intellij.ide.fileTemplates.FileTemplate
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiFile
import org.phellang.PhelIcons
import org.phellang.actions.namespace.PhelNamespaceGenerator
import org.phellang.actions.template.PhelFileTemplateHandler

class CreatePhelFileAction : CreateFileFromTemplateAction("Phel File", "Create new Phel file", PhelIcons.FILE),
    DumbAware {

    private val namespaceGenerator = PhelNamespaceGenerator()
    private val templateHandler = PhelFileTemplateHandler()

    override fun getActionName(directory: PsiDirectory?, newName: String, templateName: String?): String {
        return templateHandler.getActionName(newName)
    }

    override fun buildDialog(project: Project, directory: PsiDirectory, builder: CreateFileFromTemplateDialog.Builder) {
        builder.setTitle("New Phel File").addKind("Phel file", PhelIcons.FILE, "Phel_File.phel")
    }

    override fun createFileFromTemplate(name: String, template: FileTemplate, dir: PsiDirectory): PsiFile? {
        val namespace = namespaceGenerator.generateNamespace(dir, name)

        return templateHandler.createFileFromTemplate(name, template, dir, namespace)
    }
}
