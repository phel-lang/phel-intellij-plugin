package org.phellang.inspection.deprecated

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.LocalQuickFix
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.progress.ProcessCanceledException
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.util.PsiTreeUtil
import org.phellang.language.psi.PhelKeyword
import org.phellang.language.psi.PhelList
import org.phellang.language.psi.PhelSymbol
import org.phellang.language.psi.PhelVisitor

/**
 * Flags backslash-separated namespaces inside `(ns ...)`, `:require`, and `:use`
 * clauses. Phel 0.35 made dot-separation the canonical form and accepts backslashes
 * only for back-compat (with `PHEL_WARN_DEPRECATIONS=1`). This mirrors that warning
 * inside the IDE so projects can migrate.
 */
class PhelBackslashNamespaceInspection : LocalInspectionTool() {

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : PhelVisitor() {
            override fun visitSymbol(symbol: PhelSymbol) {
                val text = symbol.text ?: return
                if (!text.contains('\\')) return
                if (!isNamespaceContext(symbol)) return

                holder.registerProblem(
                    symbol,
                    "Backslash namespace separator is deprecated since Phel 0.35; use '.' instead",
                    ProblemHighlightType.LIKE_DEPRECATED,
                    ConvertToDotSeparatorQuickFix(),
                )
            }
        }
    }

    private fun isNamespaceContext(symbol: PhelSymbol): Boolean {
        val enclosingList = PsiTreeUtil.getParentOfType(symbol, PhelList::class.java) ?: return false
        val forms = enclosingList.forms
        if (forms.isEmpty()) return false

        // (:require ns ...) / (:use ns ...) — first form is the keyword.
        val firstKeyword = forms[0] as? PhelKeyword
            ?: PsiTreeUtil.findChildOfType(forms[0], PhelKeyword::class.java)
        if (firstKeyword != null && (firstKeyword.text == ":require" || firstKeyword.text == ":use")) {
            return true
        }

        // (ns my.ns ...) — top-level ns form.
        val firstSymbol = forms[0] as? PhelSymbol
            ?: PsiTreeUtil.findChildOfType(forms[0], PhelSymbol::class.java)
        return firstSymbol?.text == "ns" && forms.size >= 2 && PsiTreeUtil.isAncestor(forms[1], symbol, false)
    }

    private class ConvertToDotSeparatorQuickFix : LocalQuickFix {
        override fun getFamilyName(): String = "Convert to dot-separated namespace"

        override fun applyFix(project: Project, descriptor: ProblemDescriptor) {
            val symbol = descriptor.psiElement as? PhelSymbol ?: return
            if (!symbol.isValid) return
            val text = symbol.text ?: return
            if (!text.contains('\\')) return
            val replacement = text.replace('\\', '.')

            try {
                val file = symbol.containingFile ?: return
                val docManager = PsiDocumentManager.getInstance(project)
                val document = docManager.getDocument(file) ?: return
                val range = symbol.textRange ?: return
                document.replaceString(range.startOffset, range.endOffset, replacement)
                docManager.commitDocument(document)
            } catch (e: ProcessCanceledException) {
                throw e
            } catch (e: Exception) {
                LOG.warn("Failed to convert backslash namespace '$text' to dot form", e)
            }
        }
    }

    companion object {
        private val LOG = Logger.getInstance(PhelBackslashNamespaceInspection::class.java)
    }
}
