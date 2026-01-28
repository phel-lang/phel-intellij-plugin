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

            // Insert the lookup string as-is (already aliased at suggestion time if applicable)
            document.replaceString(symbolStart, symbolEnd, item.lookupString)
            editor.caretModel.moveToOffset(symbolStart + item.lookupString.length)

            // Auto-import namespace if needed
            val psiFile = context.file as? PhelFile
            autoImportNamespaceIfNeeded(context, psiFile, item.lookupString)
        }
    }

    private fun autoImportNamespaceIfNeeded(context: InsertionContext, psiFile: PhelFile?, lookupString: String) {
        if (psiFile == null) return

        val qualifier = PhelNamespaceUtils.extractNamespace(lookupString) ?: return

        // Check if the qualifier is an alias (meaning namespace is already imported)
        val aliasMap = PhelNamespaceUtils.extractAliasMap(psiFile)
        if (aliasMap.containsKey(qualifier)) {
            // Qualifier is an alias, namespace is already imported
            return
        }

        // Qualifier is a namespace name, check if it needs import
        if (!PhelNamespaceUtils.isNamespaceImportedOrAliased(psiFile, qualifier)) {
            WriteCommandAction.runWriteCommandAction(context.project) {
                PhelNamespaceImporter.ensureNamespaceImported(psiFile, qualifier)
            }
        }
    }
}
