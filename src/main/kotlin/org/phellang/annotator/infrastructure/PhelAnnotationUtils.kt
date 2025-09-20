package org.phellang.annotator.infrastructure

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement

object PhelAnnotationUtils {

    fun createAnnotation(
        holder: AnnotationHolder, element: PsiElement, textAttributes: TextAttributesKey
    ) {
        createAnnotation(holder, element.textRange, textAttributes)
    }

    fun createAnnotation(
        holder: AnnotationHolder, range: TextRange, textAttributes: TextAttributesKey
    ) {
        holder.newSilentAnnotation(HighlightSeverity.INFORMATION).range(range).textAttributes(textAttributes).create()
    }

    fun shouldAnnotate(element: PsiElement?): Boolean {
        return element != null && element.textRange != null && element.textRange.length > 0
    }

    fun isValidText(text: String?): Boolean {
        return !text.isNullOrBlank()
    }
}
