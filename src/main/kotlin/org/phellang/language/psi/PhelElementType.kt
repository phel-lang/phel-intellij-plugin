package org.phellang.language.psi

import com.intellij.psi.tree.IElementType
import org.jetbrains.annotations.NonNls
import org.phellang.PhelLanguage

class PhelElementType(debugName: @NonNls String) : IElementType(debugName, PhelLanguage.INSTANCE)
