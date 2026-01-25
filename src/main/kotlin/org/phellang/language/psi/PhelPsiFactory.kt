package org.phellang.language.psi

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFileFactory
import com.intellij.psi.PsiWhiteSpace
import com.intellij.psi.util.PsiTreeUtil
import org.phellang.language.infrastructure.PhelFileType
import org.phellang.language.psi.files.PhelFile

/**
 * Factory for creating Phel PSI elements programmatically.
 */
object PhelPsiFactory {

    /**
     * Creates a PhelSymbol from text.
     */
    fun createSymbol(project: Project, text: String): PhelSymbol {
        val file = createFile(project, "($text)")
        return PsiTreeUtil.findChildOfType(file, PhelSymbol::class.java)
            ?: throw IllegalStateException("Failed to create symbol from text: $text")
    }

    /**
     * Creates a PhelList from text.
     */
    fun createList(project: Project, text: String): PhelList {
        val file = createFile(project, text)
        return PsiTreeUtil.findChildOfType(file, PhelList::class.java)
            ?: throw IllegalStateException("Failed to create list from text: $text")
    }

    /**
     * Creates a whitespace element.
     */
    fun createWhitespace(project: Project, text: String): PsiWhiteSpace {
        // Create a file with two symbols separated by the desired whitespace
        val file = createFile(project, "a${text}b")
        
        // Find the whitespace element between the two symbols
        var child = file.firstChild
        while (child != null) {
            if (child is PsiWhiteSpace) {
                return child
            }
            child = child.nextSibling
        }
        
        throw IllegalStateException("Failed to create whitespace from text: '$text'")
    }

    /**
     * Creates a temporary Phel file with the given text.
     */
    private fun createFile(project: Project, text: String): PhelFile {
        return PsiFileFactory.getInstance(project)
            .createFileFromText("dummy.phel", PhelFileType.INSTANCE, text) as PhelFile
    }
}
