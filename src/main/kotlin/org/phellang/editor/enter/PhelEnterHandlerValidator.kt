package org.phellang.editor.enter

import com.intellij.psi.PsiFile
import org.phellang.language.infrastructure.PhelFileType

class PhelEnterHandlerValidator {

    fun shouldHandleFile(file: PsiFile): Boolean {
        return file.fileType == PhelFileType.INSTANCE
    }
}
