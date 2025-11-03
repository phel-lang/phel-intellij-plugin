package org.phellang.language.infrastructure

import com.intellij.openapi.fileTypes.LanguageFileType
import javax.swing.Icon

class PhelFileType private constructor() : LanguageFileType(PhelLanguage) {
    // Note: This companion object MUST be placed here - ignore IDE suggestion
    companion object {
        @JvmField
        val INSTANCE = PhelFileType()
    }

    override fun getName(): String {
        return "Phel file"
    }

    override fun getDescription(): String {
        return "Phel language file"
    }

    override fun getDefaultExtension(): String {
        return "phel"
    }

    override fun getIcon(): Icon {
        return PhelIcons.FILE
    }
}
