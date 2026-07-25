package org.phellang.editor.format

import com.intellij.application.options.IndentOptionsEditor
import com.intellij.application.options.SmartIndentOptionsEditor
import com.intellij.lang.Language
import com.intellij.psi.codeStyle.CodeStyleSettingsCustomizable
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

        // Zero leaves the author's spacing between top-level forms alone. Forcing one by default
        // would reflow every existing file the first time it is formatted.
        commonSettings.BLANK_LINES_AROUND_METHOD = 0
        commonSettings.KEEP_BLANK_LINES_IN_CODE = DEFAULT_KEEP_BLANK_LINES
    }

    /**
     * Only the options the formatter actually honours are shown. A page offering spacing or wrapping
     * toggles with no rules behind them would be worse than a short page: the setting would appear
     * to work and change nothing.
     */
    override fun customizeSettings(consumer: CodeStyleSettingsCustomizable, settingsType: SettingsType) {
        if (settingsType != SettingsType.BLANK_LINES_SETTINGS) return

        consumer.showStandardOptions("KEEP_BLANK_LINES_IN_CODE", "BLANK_LINES_AROUND_METHOD")
        consumer.renameStandardOption("BLANK_LINES_AROUND_METHOD", "Between top-level forms")
    }

    override fun getCodeSample(settingsType: SettingsType): String = CODE_SAMPLE

    private companion object {
        const val DEFAULT_INDENT = 2

        /** The platform's own default; the formatter used to override it with a hard-coded 1. */
        const val DEFAULT_KEEP_BLANK_LINES = 2

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
