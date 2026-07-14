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

    // Not guarded: the two steps below must succeed or fail together. Swallowing an exception
    // from ensureNamespaceImported left the call renamed with no `(:require ...)` added — code
    // that no longer compiles, and no indication why. Letting it out rolls the whole fix back.
    override fun applyFix(project: Project, descriptor: ProblemDescriptor) {
        val element = descriptor.psiElement
        val parsed = ReplacementParser.parse(replacement)

        // Grab the file before replace() invalidates the element.
        val file = element.containingFile as? PhelFile

        val newElement = PhelPsiFactory.createSymbol(project, parsed.functionName)
        element.replace(newElement)

        if (file != null && parsed.namespace != null) {
            PhelNamespaceImporter.ensureNamespaceImported(file, parsed.namespace)
        }
    }
}
