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

    /**
     * Not guarded: swallowing a template failure and returning null made "New > Phel File" do
     * nothing at all — no file, no error, no hint as to why. Letting it out means the platform
     * reports the failure, which is the only way the user finds out.
     */
    fun createFileFromTemplate(name: String, template: FileTemplate, dir: PsiDirectory, namespace: String): PsiFile? {
        val properties = buildTemplateProperties(name, namespace)

        return FileTemplateUtil.createFromTemplate(template, name, properties, dir) as? PsiFile
    }

    private fun buildTemplateProperties(name: String, namespace: String): Properties {
        val properties = Properties()
        properties.setProperty("NAME", name)
        properties.setProperty("NAMESPACE", namespace)
        return properties
    }

}
