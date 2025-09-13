package org.phellang.language

import com.intellij.lexer.FlexAdapter

class PhelLexerAdapter : FlexAdapter(PhelLexer(null))
