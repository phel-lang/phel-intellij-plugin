package org.phellang.editor.colorsettings

import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighter
import com.intellij.openapi.options.colors.ColorDescriptor
import org.phellang.PhelIcons
import org.phellang.syntax.PhelSyntaxHighlighter
import javax.swing.Icon

class PhelColorSettingsConfiguration {

    fun getDisplayName(): String {
        return "Phel Lang"
    }

    fun getIcon(): Icon {
        return PhelIcons.FILE
    }

    fun getHighlighter(): SyntaxHighlighter {
        return PhelSyntaxHighlighter()
    }

    fun getAdditionalHighlightingTagToDescriptorMap(): MutableMap<String?, TextAttributesKey?>? {
        return null
    }

    fun getColorDescriptors(): Array<ColorDescriptor> {
        return ColorDescriptor.EMPTY_ARRAY
    }
}
