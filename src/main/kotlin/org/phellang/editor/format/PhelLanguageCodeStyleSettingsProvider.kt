package org.phellang.editor.format

import com.intellij.application.options.IndentOptionsEditor
import com.intellij.application.options.SmartIndentOptionsEditor
import com.intellij.lang.Language
import com.intellij.psi.codeStyle.CommonCodeStyleSettings
import com.intellij.psi.codeStyle.LanguageCodeStyleSettingsProvider
import org.phellang.language.infrastructure.PhelLanguage

/** Gives Phel a Code Style page, so indentation is configurable and persists per project. */
class PhelLanguageCodeStyleSettingsProvider : LanguageCodeStyleSettingsProvider() {

    override fun getLanguage(): Language = PhelLanguage

    override fun getIndentOptionsEditor(): IndentOptionsEditor = SmartIndentOptionsEditor()

    override fun customizeDefaults(
        commonSettings: CommonCodeStyleSettings,
        indentOptions: CommonCodeStyleSettings.IndentOptions,
    ) {
        // Two spaces per level, matching the Enter handler and Lisp convention generally.
        indentOptions.INDENT_SIZE = DEFAULT_INDENT
        indentOptions.CONTINUATION_INDENT_SIZE = DEFAULT_INDENT
        indentOptions.TAB_SIZE = DEFAULT_INDENT
        indentOptions.USE_TAB_CHARACTER = false
    }

    override fun getCodeSample(settingsType: SettingsType): String = CODE_SAMPLE

    private companion object {
        const val DEFAULT_INDENT = 2

        val CODE_SAMPLE = """
            (ns app\example
              (:require phel\str :as str))

            (defn greet
              "Returns a greeting for the given name."
              [name]
              (let [trimmed (str/trim name)]
                (when (pos? (php/strlen trimmed))
                  (str "Hello, " trimmed))))
        """.trimIndent()
    }
}
