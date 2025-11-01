package org.phellang.editor.folding.collectors

import com.intellij.lang.ASTNode
import com.intellij.lang.folding.FoldingDescriptor
import com.intellij.openapi.editor.Document
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

        // Handle different PSI element types
        when (psi) {
            is PhelList -> handleListFolding(psi, document, descriptors)
            is PhelVec -> handleVectorFolding(psi, document, descriptors)
            is PhelMap -> handleMapFolding(psi, document, descriptors)
        }

        // Handle comment folding separately
        if (psi is PhelFormCommentMacro) {
            handleCommentFolding(psi, document, descriptors)
        }

        // Always process children - let individual handlers decide what to fold
        for (child in node.getChildren(null)) {
            collectFoldingDescriptorsRecursive(child, document, descriptors)
        }
    }

    private fun handleListFolding(
        list: PhelList, document: Document, descriptors: MutableList<FoldingDescriptor>
    ) {
        val range = list.textRange
        if (!PhelFoldingValidator.isValidFoldingRange(range, document)) return

        // Lisp philosophy: Fold any multi-line parenthetical expression
        val placeholderText = PhelPlaceholderGenerator.generateListPlaceholder(list)
        descriptors.add(FoldingDescriptor(list.node, range, null, placeholderText))
    }

    private fun handleVectorFolding(
        vector: PhelVec, document: Document, descriptors: MutableList<FoldingDescriptor>
    ) {
        val range = vector.textRange
        if (!PhelFoldingValidator.isValidFoldingRange(range, document)) return

        // Add all potential vector folding - conflicts will be resolved in post-processing
        descriptors.add(FoldingDescriptor(vector.node, range, null, "[...]"))
    }

    private fun handleMapFolding(
        map: PhelMap, document: Document, descriptors: MutableList<FoldingDescriptor>
    ) {
        val range = map.textRange
        if (!PhelFoldingValidator.isValidFoldingRange(range, document)) return

        // Add all potential map folding - conflicts will be resolved in post-processing
        descriptors.add(FoldingDescriptor(map.node, range, null, "{...}"))
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
