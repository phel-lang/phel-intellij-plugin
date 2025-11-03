package org.phellang.language.psi.elements

import com.intellij.psi.tree.IElementType
import org.jetbrains.annotations.NonNls
import org.phellang.language.infrastructure.PhelLanguage

class PhelTokenType(debugName: @NonNls String) : IElementType(debugName, PhelLanguage) {
    override fun toString(): String {
        return "PhelTokenType." + super.toString()
    }
}
