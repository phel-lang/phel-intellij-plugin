package org.phellang.language.lexer

import com.intellij.lexer.FlexAdapter
import org.phellang.language.PhelLexer

class PhelLexerAdapter : FlexAdapter(PhelLexer(null))
