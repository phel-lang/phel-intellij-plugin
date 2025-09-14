package org.phellang.language.psi

import com.intellij.extapi.psi.PsiFileBase
import com.intellij.openapi.fileTypes.FileType
import com.intellij.psi.FileViewProvider
import org.phellang.PhelFileType
import org.phellang.PhelLanguage

class PhelFile(viewProvider: FileViewProvider) : PsiFileBase(viewProvider, PhelLanguage.INSTANCE) {
    override fun getFileType(): FileType {
        return PhelFileType.INSTANCE
    }

    override fun toString(): String {
        return "Phel File"
    }
}
