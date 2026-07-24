package org.phellang.editor.folding.resolvers

import com.intellij.lang.folding.FoldingDescriptor
import com.intellij.psi.PsiElement
import org.phellang.language.psi.*

object PhelFoldingConflictResolver {

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
                val outerPsi = other.element.psi
                val thisPsi = descriptor.element.psi

                if (shouldPrioritizeOuter(outerPsi, thisPsi)) {
                    return false
                }
            }
        }

        return true
    }

    /**
     * A list always wins over a vector or map nested inside it, whatever head the list carries.
     * Folding the outer `(...)` already hides the inner `[...]` / `{...}`, so keeping both would
     * offer the reader a fold region they can never see the effect of.
     */
    private fun shouldPrioritizeOuter(outerPsi: PsiElement, innerPsi: PsiElement): Boolean =
        outerPsi is PhelList && (innerPsi is PhelVec || innerPsi is PhelMap)
}
