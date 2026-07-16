package org.phellang.editor.commenting

import com.intellij.lang.Commenter

class PhelCommenter : Commenter {
    override fun getLineCommentPrefix(): String {
        return ";"
    }

    // Phel's `#| ... |#` block comments are deprecated, so none are offered here.
    override fun getBlockCommentPrefix(): String? = null
    override fun getBlockCommentSuffix(): String? = null
    override fun getCommentedBlockCommentPrefix(): String? = null
    override fun getCommentedBlockCommentSuffix(): String? = null
}
