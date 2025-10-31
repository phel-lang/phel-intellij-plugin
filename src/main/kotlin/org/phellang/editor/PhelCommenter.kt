package org.phellang.editor

import com.intellij.lang.Commenter

class PhelCommenter : Commenter {
    override fun getLineCommentPrefix(): String {
        return ";"
    }

    override fun getBlockCommentPrefix(): String? {
        return "#|"
    }

    override fun getBlockCommentSuffix(): String? {
        return "|#"
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
