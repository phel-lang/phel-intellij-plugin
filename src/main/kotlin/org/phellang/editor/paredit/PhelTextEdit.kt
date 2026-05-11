package org.phellang.editor.paredit

import com.intellij.openapi.util.TextRange

data class PhelTextEdit(val range: TextRange, val replacement: String) {

    companion object {
        fun insert(offset: Int, text: String): PhelTextEdit =
            PhelTextEdit(TextRange(offset, offset), text)

        fun delete(range: TextRange): PhelTextEdit =
            PhelTextEdit(range, "")
    }
}
