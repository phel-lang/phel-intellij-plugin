package org.phellang.editor

import com.intellij.lang.Commenter

class PhelCommenter : Commenter {
    override fun getLineCommentPrefix(): String {
        return ";"
    }

    override fun getBlockCommentPrefix(): String? {
        // Block comments (#| ... |#) are deprecated since v0.31
        return null
    }

    override fun getBlockCommentSuffix(): String? {
        return null
    }

    override fun getCommentedBlockCommentPrefix(): String? {
        // Not applicable for Phel
        return null
    }

    override fun getCommentedBlockCommentSuffix(): String? {
        // Not applicable for Phel
        return null
    }
}
