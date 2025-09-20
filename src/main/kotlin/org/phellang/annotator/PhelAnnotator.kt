package org.phellang.annotator

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.psi.PsiElement
import org.phellang.annotator.infrastructure.PhelAnnotationConstants.COMMENTED_OUT_FORM
import org.phellang.annotator.infrastructure.PhelAnnotationConstants.COLLECTION_TYPE
import org.phellang.annotator.analyzers.PhelCommentAnalyzer
import org.phellang.annotator.highlighters.PhelElementHighlighter
import org.phellang.annotator.highlighters.PhelSymbolHighlighter
import org.phellang.annotator.infrastructure.PhelAnnotationUtils
import org.phellang.language.psi.*

class PhelAnnotator : Annotator {

    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        // Check if this element is commented out by #_ form comment
        if (PhelCommentAnalyzer.isCommentedOutByFormComment(element)) {
            PhelAnnotationUtils.createAnnotation(holder, element, COMMENTED_OUT_FORM)
            return  // Don't apply other highlighting to commented-out forms
        }

        // Check if this element is inside a short function - if so, skip highlighting
        if (PhelCommentAnalyzer.isInsideShortFunction(element)) {
            return
        }

        // Delegate to specialized highlighters based on element type
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

            else -> {
                when (element.node.elementType) {
                    PhelTypes.HASH_BRACE -> {
                        // Highlight the #{ token as part of a set
                        PhelAnnotationUtils.createAnnotation(holder, element, COLLECTION_TYPE)
                    }

                    PhelTypes.BRACE2 -> {
                        if (PhelCommentAnalyzer.isInsideSet(element)) {
                            // Highlight the } token as part of a set (not a map)
                            PhelAnnotationUtils.createAnnotation(holder, element, COLLECTION_TYPE)
                        }
                    }
                }
            }
        }
    }
}
