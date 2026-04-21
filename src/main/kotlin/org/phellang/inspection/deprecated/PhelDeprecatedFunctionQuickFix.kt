package org.phellang.inspection.deprecated

import com.intellij.codeInspection.LocalQuickFix
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.progress.ProcessCanceledException
import com.intellij.openapi.project.Project
import org.phellang.language.psi.PhelNamespaceImporter
import org.phellang.language.psi.PhelPsiFactory
import org.phellang.language.psi.files.PhelFile

class PhelDeprecatedFunctionQuickFix(private val replacement: String) : LocalQuickFix {

    override fun getFamilyName(): String = "Replace with suggested alternative"

    override fun getName(): String {
        val displayName = ReplacementParser.formatForDisplay(replacement)
        return "Replace with '$displayName'"
    }

    override fun applyFix(project: Project, descriptor: ProblemDescriptor) {
        val element = descriptor.psiElement
        try {
            val parsed = ReplacementParser.parse(replacement)

            // Grab the file before replace() invalidates the element.
            val file = element.containingFile as? PhelFile

            val newElement = PhelPsiFactory.createSymbol(project, parsed.functionName)
            element.replace(newElement)

            if (file != null && parsed.namespace != null) {
                PhelNamespaceImporter.ensureNamespaceImported(file, parsed.namespace)
            }
        } catch (e: ProcessCanceledException) {
            throw e
        } catch (e: Exception) {
            LOG.warn("Failed to apply deprecated function replacement: $replacement", e)
        }
    }

    companion object {
        private val LOG = Logger.getInstance(PhelDeprecatedFunctionQuickFix::class.java)
    }
}
