package org.phellang.annotator.highlighters

import com.intellij.lang.annotation.AnnotationHolder
import org.phellang.annotator.infrastructure.PhelAnnotationConstants.COLLECTION_TYPE
import org.phellang.annotator.infrastructure.PhelAnnotationConstants.KEYWORD
import org.phellang.annotator.infrastructure.PhelAnnotationConstants.SHORT_FUNCTION
import org.phellang.annotator.infrastructure.PhelAnnotationUtils
import org.phellang.language.psi.*

object PhelElementHighlighter {

    fun annotateKeyword(keyword: PhelKeyword, holder: AnnotationHolder) {
        if (PhelAnnotationUtils.shouldAnnotate(keyword)) {
            PhelAnnotationUtils.createAnnotation(holder, keyword, KEYWORD)
        }
    }

    fun annotateShortFunction(shortFn: PhelShortFn, holder: AnnotationHolder) {
        if (PhelAnnotationUtils.shouldAnnotate(shortFn)) {
            PhelAnnotationUtils.createAnnotation(holder, shortFn, SHORT_FUNCTION)
        }
    }

    fun annotateSet(set: PhelSet, holder: AnnotationHolder) {
        if (PhelAnnotationUtils.shouldAnnotate(set)) {
            PhelAnnotationUtils.createAnnotation(holder, set, COLLECTION_TYPE)
        }
    }

    fun annotateHashBrace(element: com.intellij.psi.PsiElement, holder: AnnotationHolder) {
        if (PhelAnnotationUtils.shouldAnnotate(element)) {
            PhelAnnotationUtils.createAnnotation(holder, element, COLLECTION_TYPE)
        }
    }
}
