package org.phellang.language.psi

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFileFactory
import com.intellij.psi.PsiWhiteSpace
import com.intellij.psi.util.PsiTreeUtil
import org.phellang.language.infrastructure.PhelFileType
import org.phellang.language.psi.files.PhelFile

object PhelPsiFactory {

    fun createSymbol(project: Project, text: String): PhelSymbol {
        val file = createFile(project, "($text)")
        return PsiTreeUtil.findChildOfType(file, PhelSymbol::class.java)
            ?: throw IllegalStateException("Failed to create symbol from text: $text")
    }

    fun createList(project: Project, text: String): PhelList {
        val file = createFile(project, text)
        return PsiTreeUtil.findChildOfType(file, PhelList::class.java)
            ?: throw IllegalStateException("Failed to create list from text: $text")
    }

    fun createWhitespace(project: Project, text: String): PsiWhiteSpace {
        // Wrap the requested text between two symbols so the parser emits a whitespace token.
        val file = createFile(project, "a${text}b")
        var child = file.firstChild
        while (child != null) {
            if (child is PsiWhiteSpace) return child
            child = child.nextSibling
        }
        throw IllegalStateException("Failed to create whitespace from text: '$text'")
    }

    private fun createFile(project: Project, text: String): PhelFile {
        return PsiFileFactory.getInstance(project)
            .createFileFromText("dummy.phel", PhelFileType.INSTANCE, text) as PhelFile
    }
}
