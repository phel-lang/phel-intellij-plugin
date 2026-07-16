package org.phellang.annotator

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.psi.PsiElement
import org.phellang.core.highlighting.PhelAnnotationConstants.COMMENTED_OUT_FORM
import org.phellang.annotator.analyzers.PhelCommentAnalyzer
import org.phellang.annotator.highlighters.PhelElementHighlighter
import org.phellang.annotator.highlighters.PhelRequireHighlighter
import org.phellang.annotator.highlighters.PhelSymbolHighlighter
import org.phellang.annotator.infrastructure.PhelAnnotationUtils
import org.phellang.language.psi.*

class PhelAnnotator : Annotator {
    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        if (PhelCommentAnalyzer.isCommentedOutByFormComment(element)) {
            PhelAnnotationUtils.createAnnotation(holder, element, COMMENTED_OUT_FORM)
            return  // Don't apply other highlighting to commented-out forms
        }

        // The anonymous-function check (an ancestor walk) only matters for the element types
        // handled below, so it runs inside the dispatch rather than for every PSI element
        // (whitespace, punctuation, literals, lists, …), which are the majority.
        when (element) {
            is PhelKeyword -> {
                if (PhelCommentAnalyzer.isInsideAnonFunction(element)) return
                PhelElementHighlighter.annotateKeyword(element, holder)
            }

            is PhelSymbol -> {
                if (PhelCommentAnalyzer.isInsideAnonFunction(element)) return
                val text = element.text ?: return
                if (PhelRequireHighlighter.annotateRequireNamespace(element, holder)) {
                    return  // Handled as require namespace
                }
                PhelSymbolHighlighter.annotateSymbol(element, text, holder)
            }

            is PhelHashFn -> {
                if (PhelCommentAnalyzer.isInsideAnonFunction(element)) return
                PhelElementHighlighter.annotateAnonymousFunction(element, holder)
            }
        }
    }
}
