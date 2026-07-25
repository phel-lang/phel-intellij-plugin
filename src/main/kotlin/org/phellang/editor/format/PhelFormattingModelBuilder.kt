package org.phellang.editor.format

import com.intellij.formatting.FormattingContext
import com.intellij.formatting.FormattingModel
import com.intellij.formatting.FormattingModelBuilder
import com.intellij.formatting.FormattingModelProvider
import com.intellij.formatting.Indent
import org.phellang.editor.format.blocks.PhelBlock

/**
 * The built-in formatter, used when `phel format` is unavailable.
 *
 * [PhelExternalFormatter] is the preferred path and wins whenever the project has a `phel` binary,
 * because it is the canonical formatter. This one exists so Reformat Code, format-on-paste and
 * reindent-selection do something sensible before `composer install`, in a scratch file, or in a
 * layout where the binary is not where the locator looks.
 */
class PhelFormattingModelBuilder : FormattingModelBuilder {

    override fun createModel(formattingContext: FormattingContext): FormattingModel {
        val settings = formattingContext.codeStyleSettings
        val file = formattingContext.containingFile

        val root = PhelBlock(
            node = file.node,
            wrap = null,
            alignment = null,
            indent = Indent.getNoneIndent(),
        )

        return FormattingModelProvider.createFormattingModelForPsiFile(file, root, settings)
    }
}
