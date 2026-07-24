package org.phellang.editor.folding.collectors

import com.intellij.lang.ASTNode
import com.intellij.lang.folding.FoldingDescriptor
import com.intellij.openapi.editor.Document
import com.intellij.psi.PsiElement
import org.phellang.editor.folding.validators.PhelFoldingValidator
import org.phellang.editor.folding.placeholders.PhelPlaceholderGenerator
import org.phellang.language.psi.*

class PhelFoldingCollector {
    fun collectFoldingDescriptors(node: ASTNode, document: Document): List<FoldingDescriptor> {
        val descriptors = mutableListOf<FoldingDescriptor>()
        collectFoldingDescriptorsRecursive(node, document, descriptors)
        return descriptors
    }

    private fun collectFoldingDescriptorsRecursive(
        node: ASTNode, document: Document, descriptors: MutableList<FoldingDescriptor>
    ) {
        val psi = node.psi

        when (psi) {
            // Lisp philosophy: fold any multi-line bracketed expression. Conflicts between an outer
            // form and the vectors/maps inside it are resolved in post-processing, not here.
            is PhelList -> addFold(psi, document, descriptors) { PhelPlaceholderGenerator.generateListPlaceholder(psi) }
            is PhelVec -> addFold(psi, document, descriptors) { "[...]" }
            is PhelMap -> addFold(psi, document, descriptors) { "{...}" }
        }

        if (psi is PhelFormCommentMacro) {
            handleCommentFolding(psi, document, descriptors)
        }

        // Always process children - let individual handlers decide what to fold
        for (child in node.getChildren(null)) {
            collectFoldingDescriptorsRecursive(child, document, descriptors)
        }
    }

    /**
     * Adds a fold for [element] when its range is foldable. [placeholder] is a lambda so the list
     * placeholder is not built for a range that will be rejected.
     */
    private fun addFold(
        element: PsiElement, document: Document, descriptors: MutableList<FoldingDescriptor>, placeholder: () -> String
    ) {
        val range = element.textRange
        if (!PhelFoldingValidator.isValidFoldingRange(range, document)) return

        descriptors.add(FoldingDescriptor(element.node, range, null, placeholder()))
    }

    private fun handleCommentFolding(
        commentForm: PhelFormCommentMacro, document: Document, descriptors: MutableList<FoldingDescriptor>
    ) {
        val range = commentForm.textRange
        if (!PhelFoldingValidator.isValidFoldingRange(range, document)) return

        // Only fold multi-line commented forms
        if (PhelFoldingValidator.isMultiLine(range, document)) {
            descriptors.add(FoldingDescriptor(commentForm.node, range, null, "#_..."))
        }
    }
}
