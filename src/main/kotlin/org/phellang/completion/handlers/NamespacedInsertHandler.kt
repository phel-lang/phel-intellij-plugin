package org.phellang.completion.handlers

import com.intellij.codeInsight.completion.InsertHandler
import com.intellij.codeInsight.completion.InsertionContext
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.util.Key
import org.phellang.language.psi.PhelNamespaceImporter
import org.phellang.language.psi.PhelNamespaceUtils
import org.phellang.language.psi.files.PhelFile

class NamespacedInsertHandler : InsertHandler<LookupElement?> {
    companion object {
        val FULL_NAMESPACE_KEY: Key<String> = Key.create("PHEL_FULL_NAMESPACE")
    }

    override fun handleInsert(context: InsertionContext, item: LookupElement) {
        val editor = context.editor
        val document = editor.document
        val caretOffset = context.startOffset

        var symbolStart = caretOffset
        val text = document.charsSequence

        while (symbolStart > 0) {
            val c = text[symbolStart - 1]
            // Stop at whitespace, opening parenthesis, or other delimiters
            if (Character.isWhitespace(c) || c == '(' || c == '[' || c == '{') {
                break
            }
            symbolStart--
        }

        val symbolEnd = context.tailOffset

        // Already aliased at suggestion time if applicable.
        document.replaceString(symbolStart, symbolEnd, item.lookupString)
        editor.caretModel.moveToOffset(symbolStart + item.lookupString.length)

        val psiFile = context.file as? PhelFile
        autoImportNamespaceIfNeeded(context, psiFile, item)
    }

    private fun autoImportNamespaceIfNeeded(context: InsertionContext, psiFile: PhelFile?, item: LookupElement) {
        if (psiFile == null) return

        val lookupString = item.lookupString
        val qualifier = PhelNamespaceUtils.extractNamespace(lookupString) ?: return

        // Check if the qualifier is an alias (meaning namespace is already imported)
        val aliasMap = PhelNamespaceUtils.extractAliasMap(psiFile)
        if (aliasMap.containsKey(qualifier)) {
            // Qualifier is an alias, namespace is already imported
            return
        }

        val fullNamespace = item.getUserData(FULL_NAMESPACE_KEY)
        if (fullNamespace != null) {
            WriteCommandAction.runWriteCommandAction(context.project) {
                PhelNamespaceImporter.ensureNamespaceImportedByFullName(psiFile, fullNamespace)
            }
        } else if (!PhelNamespaceUtils.isNamespaceImportedOrAliased(psiFile, qualifier)) {
            WriteCommandAction.runWriteCommandAction(context.project) {
                PhelNamespaceImporter.ensureNamespaceImported(psiFile, qualifier)
            }
        }
    }
}
