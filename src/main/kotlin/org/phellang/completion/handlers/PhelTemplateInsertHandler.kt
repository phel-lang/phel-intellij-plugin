package org.phellang.completion.handlers

import com.intellij.codeInsight.completion.InsertHandler
import com.intellij.codeInsight.completion.InsertionContext
import com.intellij.codeInsight.lookup.LookupElement
import org.phellang.core.utils.PhelErrorHandler

class PhelTemplateInsertHandler(
    private val template: String,
    private val caretOffset: Int,
    private val selectionLength: Int = 0,
) : InsertHandler<LookupElement?> {
    override fun handleInsert(context: InsertionContext, item: LookupElement) {
        PhelErrorHandler.safeOperation {
            val editor = context.editor
            editor.document.replaceString(context.startOffset, context.tailOffset, template)

            val caretAt = context.startOffset + caretOffset
            editor.caretModel.moveToOffset(caretAt)
            if (selectionLength > 0) {
                editor.selectionModel.setSelection(caretAt, caretAt + selectionLength)
            }
        }
    }

    companion object {
        val PARENTHESIS = PhelTemplateInsertHandler("()", caretOffset = 1)
        val DEFN = PhelTemplateInsertHandler("(defn name [])", caretOffset = 6, selectionLength = 4)
        val DEF = PhelTemplateInsertHandler("(def name)", caretOffset = 5, selectionLength = 4)
        val LET = PhelTemplateInsertHandler("(let [bindings])", caretOffset = 6, selectionLength = 8)
        val IF = PhelTemplateInsertHandler("(if condition)", caretOffset = 4, selectionLength = 9)
        val FN = PhelTemplateInsertHandler("(fn [args])", caretOffset = 5, selectionLength = 4)
    }
}
