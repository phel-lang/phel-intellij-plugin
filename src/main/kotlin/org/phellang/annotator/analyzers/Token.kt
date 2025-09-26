package org.phellang.annotator.analyzers

data class Token(val type: TokenType, val start: Int, val end: Int, val text: String)
