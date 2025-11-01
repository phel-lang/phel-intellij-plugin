package org.phellang.editor

import com.intellij.lang.ASTNode
import com.intellij.lang.folding.FoldingBuilder
import com.intellij.lang.folding.FoldingDescriptor
import com.intellij.openapi.editor.Document
import org.phellang.editor.folding.PhelFoldingDefaults
import org.phellang.editor.folding.collectors.PhelFoldingCollector
import org.phellang.editor.folding.resolvers.PhelFoldingConflictResolver

class PhelFoldingBuilder : FoldingBuilder {

    private val collector = PhelFoldingCollector()

    override fun buildFoldRegions(node: ASTNode, document: Document): Array<FoldingDescriptor> {
        // Collect all potential folding descriptors
        val descriptors = collector.collectFoldingDescriptors(node, document)

        // Filter out conflicting folding regions - prioritize outer expressions
        val filteredDescriptors = PhelFoldingConflictResolver.removeConflictingDescriptors(descriptors)

        return filteredDescriptors.toTypedArray()
    }

    override fun getPlaceholderText(node: ASTNode): String? {
        // This method is called for nodes that don't have explicit placeholder text
        return PhelFoldingDefaults.getDefaultPlaceholderText(node)
    }

    override fun isCollapsedByDefault(node: ASTNode): Boolean {
        return PhelFoldingDefaults.isCollapsedByDefault(node)
    }
}
