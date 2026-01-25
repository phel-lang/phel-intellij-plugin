package org.phellang.inspection.deprecated

import com.intellij.codeInspection.LocalQuickFix
import com.intellij.codeInspection.ProblemDescriptor
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
            // Parse the replacement to get namespace and function name
            val parsed = ReplacementParser.parse(replacement)

            // IMPORTANT: Get file BEFORE replacing element, as element becomes invalid after replace()
            val file = element.containingFile as? PhelFile

            // Replace the deprecated symbol with the new one
            val newElement = PhelPsiFactory.createSymbol(project, parsed.functionName)
            element.replace(newElement)

            // Ensure namespace is imported (if not core)
            if (file != null && parsed.namespace != null) {
                PhelNamespaceImporter.ensureNamespaceImported(file, parsed.namespace)
            }
        } catch (_: Exception) {
            // Silently fail if replacement cannot be created
        }
    }
}
