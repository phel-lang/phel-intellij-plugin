package org.phellang.actions.template

import com.intellij.ide.fileTemplates.FileTemplate
import com.intellij.ide.fileTemplates.FileTemplateUtil
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiFile
import java.util.*

class PhelFileTemplateHandler {

    fun getActionName(fileName: String): String {
        return "Create Phel File $fileName"
    }

    fun createFileFromTemplate(name: String, template: FileTemplate, dir: PsiDirectory, namespace: String): PsiFile? {
        val properties = buildTemplateProperties(name, namespace)

        return try {
            FileTemplateUtil.createFromTemplate(template, name, properties, dir) as? PsiFile
        } catch (_: Exception) {
            null
        }
    }

    private fun buildTemplateProperties(name: String, namespace: String): Properties {
        val properties = Properties()
        properties.setProperty("NAME", name)
        properties.setProperty("NAMESPACE", namespace)
        return properties
    }
}
