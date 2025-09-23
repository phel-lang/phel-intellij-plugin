package org.phellang.folding

import com.intellij.lang.ASTNode
import com.intellij.lang.folding.FoldingBuilder
import com.intellij.lang.folding.FoldingDescriptor
import com.intellij.openapi.editor.Document
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import org.phellang.language.psi.*

class PhelFoldingBuilder : FoldingBuilder {

    override fun buildFoldRegions(node: ASTNode, document: Document): Array<FoldingDescriptor> {
        val descriptors = mutableListOf<FoldingDescriptor>()
        collectFoldingDescriptors(node, document, descriptors)

        // Filter out conflicting folding regions - prioritize outer expressions
        val filteredDescriptors = removeConflictingDescriptors(descriptors)

        return filteredDescriptors.toTypedArray()
    }

    private fun collectFoldingDescriptors(
        node: ASTNode, document: Document, descriptors: MutableList<FoldingDescriptor>
    ) {
        val psi = node.psi

        when (psi) {
            is PhelList -> {
                handleListFolding(psi, document, descriptors)
            }

            is PhelVec -> {
                handleVectorFolding(psi, document, descriptors)
            }

            is PhelMap -> {
                handleMapFolding(psi, document, descriptors)
            }
        }

        if (psi is PhelFormCommentMacro) {
            handleCommentFolding(psi, document, descriptors)
        }

        // Always process children - let individual handlers decide what to fold
        for (child in node.getChildren(null)) {
            collectFoldingDescriptors(child, document, descriptors)
        }
    }

    private fun handleListFolding(list: PhelList, document: Document, descriptors: MutableList<FoldingDescriptor>) {
        val range = list.textRange
        if (!isValidFoldingRange(range, document)) return

        // Lisp philosophy: Fold any multi-line parenthetical expression
        val placeholderText = getListPlaceholderText(list)
        descriptors.add(FoldingDescriptor(list.node, range, null, placeholderText))
    }

    private fun handleVectorFolding(vector: PhelVec, document: Document, descriptors: MutableList<FoldingDescriptor>) {
        val range = vector.textRange
        if (!isValidFoldingRange(range, document)) return

        // Add all potential vector folding - conflicts will be resolved in post-processing
        descriptors.add(FoldingDescriptor(vector.node, range, null, "[...]"))
    }

    private fun handleMapFolding(map: PhelMap, document: Document, descriptors: MutableList<FoldingDescriptor>) {
        val range = map.textRange
        if (!isValidFoldingRange(range, document)) return

        // Add all potential map folding - conflicts will be resolved in post-processing
        descriptors.add(FoldingDescriptor(map.node, range, null, "{...}"))
    }

    private fun handleCommentFolding(
        commentForm: PhelFormCommentMacro, document: Document, descriptors: MutableList<FoldingDescriptor>
    ) {
        val range = commentForm.textRange
        if (!isValidFoldingRange(range, document)) return

        // Only fold multi-line commented forms
        val startLine = document.getLineNumber(range.startOffset)
        val endLine = document.getLineNumber(range.endOffset)
        if (endLine > startLine) {
            descriptors.add(FoldingDescriptor(commentForm.node, range, null, "#_..."))
        }
    }

    private fun isValidFoldingRange(range: TextRange, document: Document): Boolean {
        if (range.length < 15) return false // Don't fold very short ranges

        val startLine = document.getLineNumber(range.startOffset)
        val endLine = document.getLineNumber(range.endOffset)
        return endLine > startLine // Must span multiple lines
    }

    /**
     * Generate smart placeholder text for list expressions.
     * Uses Lisp philosophy: show first symbol + context when possible.
     */
    private fun getListPlaceholderText(list: PhelList): String {
        val forms = PsiTreeUtil.getChildrenOfType(list, PhelForm::class.java) ?: return "(...)"
        if (forms.isEmpty()) return "(...)"

        val firstSymbol = PsiTreeUtil.findChildOfType(forms[0], PhelSymbol::class.java)
        val firstText = firstSymbol?.text ?: return "(...)"

        return when {
            // Function definitions with names
            isDefiningForm(firstText) && forms.size >= 2 -> {
                val nameSymbol = PsiTreeUtil.findChildOfType(forms[1], PhelSymbol::class.java)
                val name = nameSymbol?.text
                if (name != null) "($firstText $name..." else "$firstText..."
            }

            // Namespace declarations
            firstText == "ns" && forms.size >= 2 -> {
                val nameSymbol = PsiTreeUtil.findChildOfType(forms[1], PhelSymbol::class.java)
                val name = nameSymbol?.text
                if (name != null) "(ns $name..." else "ns..."
            }

            // Generic forms - show first symbol
            else -> "($firstText..."
        }
    }

    private fun isDefiningForm(symbolText: String): Boolean {
        return symbolText in setOf(
            "defn",
            "defn-",
            "defmacro",
            "defmacro-",
            "def",
            "def-",
            "defstruct",
            "definterface",
            "defexception",
            "declare"
        )
    }

    /**
     * Remove conflicting folding descriptors by prioritizing outer expressions over inner ones.
     * This ensures binding constructs fold as complete units rather than their inner vectors.
     */
    private fun removeConflictingDescriptors(descriptors: List<FoldingDescriptor>): List<FoldingDescriptor> {
        val result = mutableListOf<FoldingDescriptor>()

        for (descriptor in descriptors) {
            var shouldKeep = true

            // Check if this descriptor conflicts with any other descriptor
            for (other in descriptors) {
                if (descriptor === other) continue

                val thisRange = descriptor.range
                val otherRange = other.range

                // If this descriptor is completely contained within another
                if (otherRange.contains(thisRange) && otherRange != thisRange) {
                    // Check if the outer descriptor should take priority
                    val outerPsi = other.element.psi
                    val thisPsi = descriptor.element.psi

                    if (shouldPrioritizeOuter(outerPsi, thisPsi)) {
                        shouldKeep = false
                        break
                    }
                }
            }

            if (shouldKeep) {
                result.add(descriptor)
            }
        }

        return result
    }

    /**
     * Determine if an outer PSI element should take priority over an inner one for folding.
     */
    private fun shouldPrioritizeOuter(outerPsi: PsiElement, innerPsi: PsiElement): Boolean {
        // Always prioritize lists over vectors/maps contained within them
        if (outerPsi is PhelList && (innerPsi is PhelVec || innerPsi is PhelMap)) {
            // Especially prioritize binding constructs
            val forms = PsiTreeUtil.getChildrenOfType(outerPsi, PhelForm::class.java)
            if (forms?.isNotEmpty() == true) {
                val firstSymbol = PsiTreeUtil.findChildOfType(forms[0], PhelSymbol::class.java)
                val firstSymbolText = firstSymbol?.text

                // Strong priority for known binding forms
                if (firstSymbolText in setOf("for", "let", "dofor", "if-let", "when-let", "binding")) {
                    return true
                }
            }

            // General priority for lists over inner structures  
            return true
        }

        return false
    }

    override fun getPlaceholderText(node: ASTNode): String? {
        // This method is called for nodes that don't have explicit placeholder text
        val psi = node.psi
        return when (psi) {
            is PhelList -> "(...)"
            is PhelVec -> "[...]"
            is PhelMap -> "{...}"
            is PhelFormCommentMacro -> "#_..."
            else -> "..."
        }
    }

    override fun isCollapsedByDefault(node: ASTNode): Boolean {
        val psi = node.psi

        // Auto-collapse namespace declarations by default
        if (psi is PhelList) {
            val forms = PsiTreeUtil.getChildrenOfType(psi, PhelForm::class.java) ?: return false
            if (forms.isNotEmpty()) {
                val firstSymbol = PsiTreeUtil.findChildOfType(forms[0], PhelSymbol::class.java)
                val firstSymbolText = firstSymbol?.text
                return firstSymbolText == "ns"
            }
        }

        return false
    }
}
