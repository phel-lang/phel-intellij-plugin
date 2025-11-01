package org.phellang.editor.folding.resolvers

import com.intellij.lang.folding.FoldingDescriptor
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import org.phellang.language.psi.*

object PhelFoldingConflictResolver {

    private val BINDING_FORMS = setOf(
        "for", "let", "dofor", "if-let", "when-let", "binding"
    )

    fun removeConflictingDescriptors(descriptors: List<FoldingDescriptor>): List<FoldingDescriptor> {
        val result = mutableListOf<FoldingDescriptor>()

        for (descriptor in descriptors) {
            if (shouldKeepDescriptor(descriptor, descriptors)) {
                result.add(descriptor)
            }
        }

        return result
    }

    private fun shouldKeepDescriptor(
        descriptor: FoldingDescriptor, allDescriptors: List<FoldingDescriptor>
    ): Boolean {
        for (other in allDescriptors) {
            if (descriptor === other) continue

            val thisRange = descriptor.range
            val otherRange = other.range

            // If this descriptor is completely contained within another
            if (otherRange.contains(thisRange) && otherRange != thisRange) {
                // Check if the outer descriptor should take priority
                val outerPsi = other.element.psi
                val thisPsi = descriptor.element.psi

                if (shouldPrioritizeOuter(outerPsi, thisPsi)) {
                    return false
                }
            }
        }

        return true
    }

    private fun shouldPrioritizeOuter(outerPsi: PsiElement, innerPsi: PsiElement): Boolean {
        // Always prioritize lists over vectors/maps contained within them
        if (outerPsi is PhelList && (innerPsi is PhelVec || innerPsi is PhelMap)) {
            // Especially prioritize binding constructs
            if (isBindingConstruct(outerPsi)) {
                return true
            }

            // General priority for lists over inner structures  
            return true
        }

        return false
    }

    private fun isBindingConstruct(list: PhelList): Boolean {
        val forms = PsiTreeUtil.getChildrenOfType(list, PhelForm::class.java)
        if (forms?.isNotEmpty() == true) {
            val firstSymbol = PsiTreeUtil.findChildOfType(forms[0], PhelSymbol::class.java)
            val firstSymbolText = firstSymbol?.text
            return firstSymbolText in BINDING_FORMS
        }
        return false
    }
}
