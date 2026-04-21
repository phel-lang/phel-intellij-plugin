package org.phellang.annotator.highlighters

import com.intellij.lang.annotation.AnnotationHolder
import org.phellang.annotator.infrastructure.PhelAnnotationConstants.ANONYMOUS_FUNCTION
import org.phellang.annotator.infrastructure.PhelAnnotationConstants.KEYWORD
import org.phellang.annotator.infrastructure.PhelAnnotationUtils
import org.phellang.language.psi.*

object PhelElementHighlighter {

    fun annotateKeyword(keyword: PhelKeyword, holder: AnnotationHolder) {
        if (PhelAnnotationUtils.shouldAnnotate(keyword)) {
            PhelAnnotationUtils.createAnnotation(holder, keyword, KEYWORD)
        }
    }

    fun annotateAnonymousFunction(hashFn: PhelHashFn, holder: AnnotationHolder) {
        if (PhelAnnotationUtils.shouldAnnotate(hashFn)) {
            PhelAnnotationUtils.createAnnotation(holder, hashFn, ANONYMOUS_FUNCTION)
        }
    }
}
