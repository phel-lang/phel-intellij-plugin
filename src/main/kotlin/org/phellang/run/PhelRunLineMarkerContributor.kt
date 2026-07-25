package org.phellang.run

import com.intellij.execution.lineMarker.ExecutorAction
import com.intellij.execution.lineMarker.RunLineMarkerContributor
import com.intellij.icons.AllIcons
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import org.phellang.language.psi.PhelNamespaceUtils
import org.phellang.language.psi.PhelSymbol
import org.phellang.language.psi.files.PhelFile
import org.phellang.language.psi.utils.PhelPsiUtils

/**
 * Puts a Run gutter icon on the `ns` of a file's namespace declaration.
 *
 * One marker per file, on the form that names it. Marking every top-level `defn` would suggest the
 * CLI can run a single function, which `phel run <path>` cannot: it takes a file or a namespace.
 */
class PhelRunLineMarkerContributor : RunLineMarkerContributor() {

    override fun getInfo(element: PsiElement): Info? {
        // Called for every element in the file, so reject non-leaves before touching the tree.
        if (element.firstChild != null) return null
        if (element.text != NS_KEYWORD) return null

        val file = element.containingFile as? PhelFile ?: return null
        val declaration = PhelNamespaceUtils.findNamespaceDeclaration(file) ?: return null
        val head = PhelPsiUtils.asSymbol(declaration.forms.firstOrNull()) ?: return null
        if (!isLeafOf(element, head)) return null

        return Info(
            AllIcons.RunConfigurations.TestState.Run,
            { "Run ${file.name}" },
            *ExecutorAction.getActions(0),
        )
    }

    /** The `ns` text lives on a leaf beneath the symbol, not on the symbol element itself. */
    private fun isLeafOf(element: PsiElement, symbol: PhelSymbol): Boolean =
        element === symbol || PsiTreeUtil.isAncestor(symbol, element, false)

    private companion object {
        const val NS_KEYWORD = "ns"
    }
}
