package org.phellang.editor.folding

import com.intellij.lang.ASTNode
import org.phellang.language.psi.PhelFormCommentMacro
import org.phellang.language.psi.PhelList
import org.phellang.language.psi.PhelMap
import org.phellang.language.psi.PhelVec

object PhelFoldingDefaults {

    fun isCollapsedByDefault(): Boolean = false

    fun getDefaultPlaceholderText(node: ASTNode): String? {
        val psi = node.psi
        return when (psi) {
            is PhelList -> "(...)"
            is PhelVec -> "[...]"
            is PhelMap -> "{...}"
            is PhelFormCommentMacro -> "#_..."
            else -> "..."
        }
    }

}
