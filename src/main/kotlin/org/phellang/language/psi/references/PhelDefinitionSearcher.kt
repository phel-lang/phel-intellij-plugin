package org.phellang.language.psi.references

import com.intellij.psi.PsiElement
import org.phellang.language.psi.PhelNamespaceFiles
import org.phellang.language.psi.PhelSymbol
import org.phellang.language.psi.analysis.PhelDefinitionPrivacy

/**
 * Finds unqualified definitions of a name: in the file being edited, and in the other files of one
 * namespace.
 *
 * Which namespace is the caller's question, answered the way the Phel compiler does: the current one,
 * or the one the name is `:refer`'d from. There is no project-wide search. An unqualified name never
 * reaches a namespace the file neither belongs to nor refers from.
 */
internal object PhelDefinitionSearcher {

    fun findInCurrentFile(symbol: PhelSymbol, symbolName: String): List<PsiElement> {
        val file = symbol.containingFile ?: return emptyList()
        return PhelDefinitionFinder.collectDefinitionsIn(file, symbolName)
    }

    /**
     * The definitions of [symbolName] in the files of [namespace] other than [symbol]'s own.
     *
     * [publicOnly] is for a name reached from another namespace through `:refer`: the compiler
     * rejects a private definition there (`SymbolResolver::isPrivateDefinitionAllowed`), so it is no
     * target.
     */
    fun findInNamespace(symbol: PhelSymbol, namespace: String, symbolName: String, publicOnly: Boolean): List<PsiElement> {
        val currentFile = symbol.containingFile

        return PhelNamespaceFiles.filesOf(symbol.project, namespace)
            .filter { it != currentFile }
            .flatMap { PhelDefinitionFinder.collectDefinitionsIn(it, symbolName) }
            .filterNot { publicOnly && PhelDefinitionPrivacy.isPrivateDefinition(it) }
    }
}
