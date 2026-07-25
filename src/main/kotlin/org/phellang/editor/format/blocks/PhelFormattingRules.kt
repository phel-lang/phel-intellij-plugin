package org.phellang.editor.format.blocks

import com.intellij.psi.codeStyle.CodeStyleSettings
import org.phellang.language.infrastructure.PhelLanguage

/**
 * The configurable part of the built-in formatter, resolved once per format request.
 *
 * These were constants inside the block, which meant the Code Style page could show a value the
 * formatter then ignored — the page offered only indentation precisely because nothing else was
 * wired to it.
 */
data class PhelFormattingRules(
    /** Consecutive blank lines kept rather than collapsed. */
    val keepBlankLines: Int,
    /** Blank lines forced between two top-level forms. Zero leaves the author's spacing alone. */
    val blankLinesBetweenTopLevelForms: Int,
) {
    companion object {
        fun from(settings: CodeStyleSettings): PhelFormattingRules {
            val common = settings.getCommonSettings(PhelLanguage)

            return PhelFormattingRules(
                keepBlankLines = common.KEEP_BLANK_LINES_IN_CODE,
                blankLinesBetweenTopLevelForms = common.BLANK_LINES_AROUND_METHOD,
            )
        }
    }
}
