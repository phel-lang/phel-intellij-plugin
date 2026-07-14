package org.phellang.language.psi.references

import com.intellij.openapi.roots.ProjectRootManager
import com.intellij.openapi.util.TextRange
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiManager
import com.intellij.psi.PsiReference
import com.intellij.psi.PsiReferenceBase
import com.intellij.psi.PsiReferenceContributor
import com.intellij.psi.PsiReferenceProvider
import com.intellij.psi.PsiReferenceRegistrar
import com.intellij.psi.impl.source.resolve.reference.impl.providers.FileReferenceSet
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.util.ProcessingContext
import org.phellang.language.psi.PhelKeyword
import org.phellang.language.psi.PhelList
import org.phellang.language.psi.PhelLiteral
import org.phellang.language.psi.PhelSymbol
import org.phellang.language.psi.utils.PhelPsiUtils

/**
 * Turns the string path argument of file-loading forms into a real file reference so
 * `Cmd/Ctrl+B` (and completion) jump to the loaded file:
 *
 * * `(load "core/meta")`              — compiler special form; **no** `.phel` extension,
 *                                       resolved relative to the calling file's directory
 *                                       (with project source roots as a classpath fallback)
 * * `(load-file "src/module.phel")`   — runtime file loader; full path with extension
 * * `(:require-file "../legacy.php")` — the `ns` clause that loads PHP files
 *
 * @see <a href="https://github.com/phel-lang/phel-lang/pull/2335">phel-lang/phel-lang#2335</a>
 */
class PhelFilePathReferenceContributor : PsiReferenceContributor() {
    override fun registerReferenceProviders(registrar: PsiReferenceRegistrar) {
        registrar.registerReferenceProvider(
            PlatformPatterns.psiElement(PhelLiteral::class.java),
            PhelFilePathReferenceProvider()
        )
    }
}

/** Which file-loading form a string literal belongs to, if any. */
private enum class LoadFormKind {
    /** `(load "core/meta")` — extension-less, caller-relative + classpath. */
    LOAD,

    /** `(load-file "…")` / `(:require-file "…")` — literal path with extension. */
    PATH_WITH_EXTENSION,
}

private class PhelFilePathReferenceProvider : PsiReferenceProvider() {
    override fun getReferencesByElement(element: PsiElement, context: ProcessingContext): Array<PsiReference> {
        val literal = element as? PhelLiteral ?: return PsiReference.EMPTY_ARRAY

        val raw = literal.text ?: return PsiReference.EMPTY_ARRAY
        // Only string literals carry a path; a string always opens with a double quote.
        if (raw.length < 2 || raw[0] != '"') return PsiReference.EMPTY_ARRAY

        val kind = loadFormKind(literal) ?: return PsiReference.EMPTY_ARRAY

        // Strip the surrounding quotes; tolerate an unterminated string while typing.
        val hasClosingQuote = raw.length >= 2 && raw.last() == '"'
        val contentEnd = if (hasClosingQuote) raw.length - 1 else raw.length
        if (contentEnd <= 1) return PsiReference.EMPTY_ARRAY
        val content = raw.substring(1, contentEnd)

        return when (kind) {
            // `load` paths omit the `.phel` extension and resolve caller-relative (or against
            // a source root), so the platform's FileReferenceSet can't match them directly.
            LoadFormKind.LOAD ->
                arrayOf(PhelLoadReference(literal, TextRange(1, contentEnd), content))

            // `load-file` / `:require-file` paths are written verbatim with their extension;
            // FileReferenceSet resolves them against the containing file's directory and gives
            // path-segment completion for free.
            LoadFormKind.PATH_WITH_EXTENSION -> {
                val referenceSet = FileReferenceSet(content, literal, 1, this, true)
                referenceSet.allReferences.toList().toTypedArray()
            }
        }
    }

    private fun loadFormKind(literal: PhelLiteral): LoadFormKind? {
        val list = literal.parent as? PhelList ?: return null
        val forms = list.forms
        if (forms.size < 2) return null

        val head = forms[0]

        val headSymbol = PhelPsiUtils.asSymbol(head)
        when (headSymbol?.text) {
            // (load "core/meta") — path is the first (and only) argument.
            "load" -> if (forms[1] === literal) return LoadFormKind.LOAD
            // (load-file "src/module.phel") — path is the first argument.
            "load-file" -> if (forms[1] === literal) return LoadFormKind.PATH_WITH_EXTENSION
        }

        // (:require-file "path" …) — any string after the keyword is a path.
        val headKeyword = head as? PhelKeyword ?: PsiTreeUtil.findChildOfType(head, PhelKeyword::class.java)
        if (headKeyword?.text == ":require-file" && head !== literal) {
            return LoadFormKind.PATH_WITH_EXTENSION
        }

        return null
    }
}

/**
 * Resolves a `(load "<path>")` argument to its `.phel` file. Phel appends the `.phel`
 * extension implicitly and resolves the path relative to the calling file's directory;
 * project source roots are tried as a classpath-style fallback for absolute-looking paths.
 */
private class PhelLoadReference(
    literal: PhelLiteral,
    rangeInElement: TextRange,
    private val path: String,
) : PsiReferenceBase<PhelLiteral>(literal, rangeInElement) {

    override fun resolve(): PsiElement? {
        val targetFile = resolveTargetFile() ?: return null
        return PsiManager.getInstance(element.project).findFile(targetFile)
    }

    private fun resolveTargetFile(): VirtualFile? {
        if (path.isEmpty()) return null
        val relativePath = if (path.endsWith(".phel")) path else "$path.phel"

        // 1. Caller-relative: relative to the directory of the file holding the (load …) form.
        element.containingFile?.virtualFile?.parent
            ?.findFileByRelativePath(relativePath)
            ?.takeIf { !it.isDirectory }
            ?.let { return it }

        // 2. Classpath-absolute: relative to any of the project's source roots.
        for (sourceRoot in ProjectRootManager.getInstance(element.project).contentSourceRoots) {
            sourceRoot.findFileByRelativePath(relativePath)
                ?.takeIf { !it.isDirectory }
                ?.let { return it }
        }

        return null
    }
}
