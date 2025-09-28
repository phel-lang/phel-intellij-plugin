package org.phellang.editor

import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighter
import com.intellij.openapi.options.colors.AttributesDescriptor
import com.intellij.openapi.options.colors.ColorDescriptor
import com.intellij.openapi.options.colors.ColorSettingsPage
import org.phellang.editor.colorsettings.PhelColorSettingsConfiguration
import org.phellang.editor.colorsettings.PhelColorSettingsProvider
import org.phellang.editor.colorsettings.PhelDemoTextProvider
import javax.swing.Icon

class PhelColorSettingsPage : ColorSettingsPage {

    private val configuration = PhelColorSettingsConfiguration()
    private val attributesProvider = PhelColorSettingsProvider()
    private val demoTextProvider = PhelDemoTextProvider()

    override fun getIcon(): Icon = configuration.getIcon()

    override fun getHighlighter(): SyntaxHighlighter = configuration.getHighlighter()

    override fun getDemoText(): String = demoTextProvider.getDemoText()

    override fun getAdditionalHighlightingTagToDescriptorMap(): MutableMap<String?, TextAttributesKey?>? = configuration.getAdditionalHighlightingTagToDescriptorMap()

    override fun getAttributeDescriptors(): Array<AttributesDescriptor> = attributesProvider.getAttributeDescriptors()

    override fun getColorDescriptors(): Array<ColorDescriptor> = configuration.getColorDescriptors()

    override fun getDisplayName(): String = configuration.getDisplayName()
}
