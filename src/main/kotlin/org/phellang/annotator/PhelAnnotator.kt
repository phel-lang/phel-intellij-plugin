package org.phellang.annotator

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.psi.PsiElement
import org.phellang.annotator.infrastructure.PhelAnnotationConstants.COMMENTED_OUT_FORM
import org.phellang.annotator.analyzers.PhelCommentAnalyzer
import org.phellang.annotator.highlighters.PhelElementHighlighter
import org.phellang.annotator.highlighters.PhelSymbolHighlighter
import org.phellang.annotator.infrastructure.PhelAnnotationUtils
import org.phellang.language.psi.*

class PhelAnnotator : Annotator {

    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        // Skip highlighting if this element is commented out by #_ form comment
        if (PhelCommentAnalyzer.isCommentedOutByFormComment(element)) {
            PhelAnnotationUtils.createAnnotation(holder, element, COMMENTED_OUT_FORM)
            return  // Don't apply other highlighting to commented-out forms
        }

        // Skip highlighting if element is inside a short function
        if (PhelCommentAnalyzer.isInsideShortFunction(element)) {
            return
        }

        when (element) {
            is PhelKeyword -> {
                PhelElementHighlighter.annotateKeyword(element, holder)
            }

            is PhelSymbol -> {
                val text = element.text
                if (text != null) {
                    PhelSymbolHighlighter.annotateSymbol(element, text, holder)
                }
            }

            is PhelShortFn -> {
                PhelElementHighlighter.annotateShortFunction(element, holder)
            }

            is PhelSet -> {
                PhelElementHighlighter.annotateSet(element, holder)
            }
        }
    }
}
