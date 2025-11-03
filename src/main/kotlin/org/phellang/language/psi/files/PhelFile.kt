package org.phellang.language.psi.files

import com.intellij.extapi.psi.PsiFileBase
import com.intellij.openapi.fileTypes.FileType
import com.intellij.psi.FileViewProvider
import org.phellang.language.core.PhelFileType
import org.phellang.language.core.PhelLanguage

class PhelFile(viewProvider: FileViewProvider) : PsiFileBase(viewProvider, PhelLanguage) {
    override fun getFileType(): FileType {
        return PhelFileType.INSTANCE
    }

    override fun toString(): String {
        return "Phel File"
    }
}
