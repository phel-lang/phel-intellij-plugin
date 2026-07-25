package org.phellang.editor.format.blocks

import com.intellij.formatting.Alignment
import com.intellij.formatting.Block
import com.intellij.formatting.Indent
import com.intellij.formatting.Spacing
import com.intellij.formatting.Wrap
import com.intellij.lang.ASTNode
import com.intellij.psi.TokenType
import com.intellij.psi.tree.IFileElementType
import com.intellij.psi.formatter.common.AbstractBlock
import org.phellang.language.psi.PhelTypes

/**
 * One node in the formatting tree.
 *
 * Deliberately simple: every form nested inside a bracket indents by exactly one level, matching
 * what the Enter handler already does (`PhelEnterHandlerIndentationCalculator`, one level per open
 * paren). Reformat and Enter disagreeing about the same line would be worse than either rule alone.
 *
 * No call-argument alignment. Clojure-family formatters align arguments under the first one for
 * calls while keeping a fixed body indent for special forms, which needs a per-head rule table and
 * gets the remaining cases visibly wrong. `phel format` is the canonical formatter and stays the
 * preferred path; this is the fallback for projects that have no `phel` binary yet, so predictable
 * beats clever.
 */
class PhelBlock(
    node: ASTNode,
    wrap: Wrap?,
    alignment: Alignment?,
    private val indent: Indent,
    private val rules: PhelFormattingRules,
) : AbstractBlock(node, wrap, alignment) {

    /**
     * Resolved once per block rather than per sibling pair. [getSpacing] runs for every pair, and
     * reaching through `ASTNode.psi` there is what the element-type matching below already avoids.
     */
    private val isFileRoot: Boolean = node.elementType is IFileElementType

    override fun buildChildren(): List<Block> {
        val childIndent = if (myNode.isBracketed()) Indent.getNormalIndent() else Indent.getNoneIndent()

        val children = mutableListOf<Block>()
        var child = myNode.firstChildNode

        while (child != null) {
            if (!child.isWhitespaceOrEmpty()) {
                // Brackets sit at the enclosing level; applying the body indent to the closing one
                // would push it off the end of the body it closes.
                val indentForChild = if (child.elementType in BRACKETS) Indent.getNoneIndent() else childIndent
                children += PhelBlock(child, null, null, indentForChild, rules)
            }
            child = child.treeNext
        }

        return children
    }

    override fun getIndent(): Indent = indent

    /**
     * One space between forms, none against a bracket.
     *
     * `keepLineBreaks = true` throughout: a line break the author put between two forms is a
     * deliberate layout choice, and collapsing it would reflow the file onto one line.
     */
    override fun getSpacing(child1: Block?, child2: Block): Spacing? {
        if (child1 == null) return null

        // Top-level forms each start their own line, with however many blank lines between them the
        // Code Style page asks for. Zero, the default, leaves the author's spacing untouched.
        if (isFileRoot) {
            val lineFeeds = rules.blankLinesBetweenTopLevelForms + 1
            return Spacing.createSpacing(0, 0, lineFeeds, true, rules.keepBlankLines)
        }

        val hugsBracket = child1.isBracket() || child2.isBracket()
        val spaces = if (hugsBracket) 0 else 1

        return Spacing.createSpacing(spaces, spaces, 0, true, rules.keepBlankLines)
    }

    override fun isLeaf(): Boolean = myNode.firstChildNode == null

    private fun Block.isBracket(): Boolean = this is PhelBlock && node?.elementType in BRACKETS

    /**
     * Only a bracketed container introduces an indent level.
     *
     * The tree also holds wrapper nodes — the file itself, and the `form` rule around every element
     * — which are structural, not visual. Indenting inside those too gave three levels of
     * indentation for two levels of parentheses.
     */
    private fun ASTNode.isBracketed(): Boolean = elementType in CONTAINERS

    private fun ASTNode.isWhitespaceOrEmpty(): Boolean =
        elementType == TokenType.WHITE_SPACE || textRange.isEmpty

    private companion object {
        /** The bracketed containers. Matched on element type: `getSpacing` runs per sibling pair. */
        val CONTAINERS = setOf(PhelTypes.LIST, PhelTypes.VEC, PhelTypes.MAP, PhelTypes.SET)

        val BRACKETS = setOf(
            PhelTypes.PAREN1, PhelTypes.PAREN2,
            PhelTypes.BRACKET1, PhelTypes.BRACKET2,
            PhelTypes.BRACE1, PhelTypes.BRACE2,
            PhelTypes.HASH_BRACE, PhelTypes.HASH_PAREN,
        )
    }
}
