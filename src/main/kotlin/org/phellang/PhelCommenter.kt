package org.phellang

import com.intellij.lang.Commenter

/**
 * Provides comment handling for Phel language.
 * Supports line comments starting with '#' or ';' characters.
 */
class PhelCommenter : Commenter {
    override fun getLineCommentPrefix(): String {
        return "#"
    }

    override fun getBlockCommentPrefix(): String? {
        // Phel doesn't have block comments
        return null
    }

    override fun getBlockCommentSuffix(): String? {
        // Phel doesn't have block comments
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
