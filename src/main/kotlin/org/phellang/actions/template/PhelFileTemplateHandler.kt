package org.phellang.actions.template

import com.intellij.ide.fileTemplates.FileTemplate
import com.intellij.ide.fileTemplates.FileTemplateUtil
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.progress.ProcessCanceledException
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
        } catch (e: ProcessCanceledException) {
            throw e
        } catch (e: Exception) {
            LOG.warn("Failed to create file from template '$name'", e)
            null
        }
    }

    private fun buildTemplateProperties(name: String, namespace: String): Properties {
        val properties = Properties()
        properties.setProperty("NAME", name)
        properties.setProperty("NAMESPACE", namespace)
        return properties
    }

    companion object {
        private val LOG = Logger.getInstance(PhelFileTemplateHandler::class.java)
    }
}
