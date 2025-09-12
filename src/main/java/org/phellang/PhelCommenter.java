package org.phellang;

import com.intellij.lang.Commenter;
import org.jetbrains.annotations.Nullable;

/**
 * Provides comment handling for Phel language.
 * Supports line comments starting with '#' or ';' characters.
 * Uses '#' as the default prefix for new comments (consistent with Clojure convention).
 */
public class PhelCommenter implements Commenter {

    @Override
    public @Nullable String getLineCommentPrefix() {
        return "#";
    }

    @Override
    public @Nullable String getBlockCommentPrefix() {
        // Phel doesn't have block comments
        return null;
    }

    @Override
    public @Nullable String getBlockCommentSuffix() {
        // Phel doesn't have block comments
        return null;
    }

    @Override
    public @Nullable String getCommentedBlockCommentPrefix() {
        // Not applicable for Phel
        return null;
    }

    @Override
    public @Nullable String getCommentedBlockCommentSuffix() {
        // Not applicable for Phel
        return null;
    }
}
