package org.phellang.indexing

import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.util.PsiTreeUtil
import org.phellang.indexing.scanner.PhelDefinitionPrivacy
import org.phellang.indexing.scanner.PhelDocstringReader
import org.phellang.indexing.scanner.PhelSignatureBuilder
import org.phellang.language.psi.PhelList
import org.phellang.language.psi.PhelNamespaceUtils
import org.phellang.language.psi.PhelProjectNamespaceFinder
import org.phellang.language.psi.files.PhelFile
import org.phellang.language.psi.utils.PhelPsiUtils
import org.phellang.registry.PhelProjectSymbol
import org.phellang.registry.SymbolType

/**
 * Reads a file's public top-level definitions for the project symbol index.
 *
 * Privacy, signature rendering and docstring lookup each have their own rules and live beside this
 * in `scanner/`; what remains here is walking the top-level forms and assembling the symbol.
 */
object PhelProjectSymbolScanner {

    fun scanFile(psiFile: PhelFile): List<PhelProjectSymbol> {
        val namespace = PhelNamespaceUtils.extractNamespaceFromFile(psiFile) ?: return emptyList()
        val virtualFile = psiFile.virtualFile ?: return emptyList()
        val topLevelLists = PsiTreeUtil.getChildrenOfType(psiFile, PhelList::class.java) ?: return emptyList()

        val shortNamespace = PhelProjectNamespaceFinder.extractShortNamespace(namespace)

        return topLevelLists.mapNotNull { extractDefinition(it, namespace, shortNamespace, virtualFile) }
    }

    private fun extractDefinition(
        list: PhelList,
        namespace: String,
        shortNamespace: String,
        virtualFile: VirtualFile,
    ): PhelProjectSymbol? {
        // activeForms, not list.forms: a `#_`-discarded form must not shift the positional reads
        // below. `(defn #_old new [x] …)` has to index `new`, not `old`.
        val forms = PhelPsiUtils.activeForms(list)
        if (forms.size < 2) return null

        val keyword = PhelPsiUtils.asSymbol(forms[0])?.text ?: return null
        if (PhelDefinitionPrivacy.isPrivateKeyword(keyword)) return null

        val symbolType = SymbolType.fromKeyword(keyword) ?: return null
        val name = PhelPsiUtils.asSymbol(forms[1])?.text ?: return null

        if (PhelDefinitionPrivacy.isPrivate(forms)) return null

        return PhelProjectSymbol(
            namespace = namespace,
            shortNamespace = shortNamespace,
            name = name,
            qualifiedName = "$shortNamespace/$name",
            signature = PhelSignatureBuilder.signatureFor(keyword, name, forms),
            type = symbolType,
            file = virtualFile,
            docstring = PhelDocstringReader.docstringOf(forms),
        )
    }
}
