package org.phellang.annotator.infrastructure

import com.intellij.codeInsight.intention.IntentionAction
import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import org.phellang.annotator.validators.PhelValidationProblem

object PhelAnnotationUtils {

    fun createAnnotation(holder: AnnotationHolder, element: PsiElement, textAttributes: TextAttributesKey) {
        createAnnotation(holder, element.textRange, textAttributes)
    }

    private fun createAnnotation(holder: AnnotationHolder, range: TextRange, textAttributes: TextAttributesKey) {
        holder.newSilentAnnotation(HighlightSeverity.INFORMATION).range(range).textAttributes(textAttributes).create()
    }

    fun createWarningAnnotation(holder: AnnotationHolder, element: PsiElement, message: String) {
        holder.newAnnotation(HighlightSeverity.WARNING, message).range(element.textRange).create()
    }

    /** Renders whatever a validator reported: severity, message, and a fix when one applies. */
    fun report(holder: AnnotationHolder, element: PsiElement, problem: PhelValidationProblem) {
        val severity = when (problem.severity) {
            PhelValidationProblem.Severity.WARNING -> HighlightSeverity.WARNING
            PhelValidationProblem.Severity.WEAK_WARNING -> HighlightSeverity.WEAK_WARNING
        }

        val annotation = holder.newAnnotation(severity, problem.message).range(element.textRange)
        problem.quickFix?.let { annotation.withFix(it) }
        annotation.create()
    }

    fun createWarningAnnotationWithFix(holder: AnnotationHolder, element: PsiElement, message: String, quickFix: IntentionAction) {
        holder.newAnnotation(HighlightSeverity.WARNING, message).range(element.textRange).withFix(quickFix).create()
    }

    fun createWeakWarningAnnotationWithFix(holder: AnnotationHolder, element: PsiElement, message: String, quickFix: IntentionAction) {
        holder.newAnnotation(HighlightSeverity.WEAK_WARNING, message).range(element.textRange).withFix(quickFix).create()
    }

    fun shouldAnnotate(element: PsiElement?): Boolean {
        return element != null && element.textRange != null && element.textRange.length > 0
    }

    fun isValidText(text: String?): Boolean {
        return !text.isNullOrBlank()
    }
}
