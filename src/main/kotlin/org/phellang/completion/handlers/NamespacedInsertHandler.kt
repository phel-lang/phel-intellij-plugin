package org.phellang.completion.handlers

import com.intellij.codeInsight.completion.InsertHandler
import com.intellij.codeInsight.completion.InsertionContext
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.openapi.command.WriteCommandAction
import org.phellang.core.utils.PhelErrorHandler
import org.phellang.language.psi.PhelNamespaceImporter
import org.phellang.language.psi.PhelNamespaceUtils
import org.phellang.language.psi.files.PhelFile

class NamespacedInsertHandler : InsertHandler<LookupElement?> {
    override fun handleInsert(context: InsertionContext, item: LookupElement) {
        PhelErrorHandler.safeOperation {
            val editor = context.editor
            val document = editor.document
            val caretOffset = context.startOffset

            var symbolStart = caretOffset
            val text = document.charsSequence

            // Move back to find the beginning of the symbol
            while (symbolStart > 0) {
                val c = text[symbolStart - 1]
                // Stop at whitespace, opening parenthesis, or other delimiters
                if (Character.isWhitespace(c) || c == '(' || c == '[' || c == '{') {
                    break
                }
                symbolStart--
            }

            val symbolEnd = context.tailOffset
            val psiFile = context.file as? PhelFile

            // Determine what text to insert (may use alias if available)
            val textToInsert = resolveTextToInsert(psiFile, item.lookupString)
            document.replaceString(symbolStart, symbolEnd, textToInsert)

            editor.caretModel.moveToOffset(symbolStart + textToInsert.length)

            // Auto-import namespace if needed
            autoImportNamespaceIfNeeded(context, psiFile, item.lookupString)
        }
    }

    /**
     * Resolves the text to insert, using an existing alias if available.
     * e.g., "str/blank?" -> "s/blank?" if phel\str is imported as "s"
     */
    private fun resolveTextToInsert(psiFile: PhelFile?, lookupString: String): String {
        if (psiFile == null) return lookupString

        val namespace = PhelNamespaceUtils.extractNamespace(lookupString) ?: return lookupString
        val functionName = lookupString.substringAfter("/")

        // Check if there's an alias for this namespace
        val alias = PhelNamespaceUtils.findAliasForNamespace(psiFile, namespace)
        return if (alias != null) {
            "$alias/$functionName"
        } else {
            lookupString
        }
    }

    private fun autoImportNamespaceIfNeeded(context: InsertionContext, psiFile: PhelFile?, lookupString: String) {
        if (psiFile == null) return

        val namespace = PhelNamespaceUtils.extractNamespace(lookupString) ?: return

        if (!PhelNamespaceUtils.isNamespaceImportedOrAliased(psiFile, namespace)) {
            WriteCommandAction.runWriteCommandAction(context.project) {
                PhelNamespaceImporter.ensureNamespaceImported(psiFile, namespace)
            }
        }
    }
}
