package org.phellang.editor.enter

data class LineInformation(
    val currentLineNumber: Int,
    val textBeforeCaret: String,
    val currentLineText: String
)
