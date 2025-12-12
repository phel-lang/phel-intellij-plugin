package org.phellang.completion

import com.intellij.codeInsight.completion.*
import com.intellij.patterns.PlatformPatterns
import org.phellang.language.infrastructure.PhelLanguage
import org.phellang.completion.engine.PhelMainCompletionProvider
import org.phellang.language.psi.PhelTypes

class PhelCompletionContributor : CompletionContributor() {

    init {
        extend(
            CompletionType.BASIC,
            PlatformPatterns.psiElement(PhelTypes.SYM).withLanguage(PhelLanguage),
            PhelMainCompletionProvider()
        )
    }
}
