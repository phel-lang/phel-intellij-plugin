package org.phellang.run

import com.intellij.execution.lineMarker.ExecutorAction
import com.intellij.execution.lineMarker.RunLineMarkerContributor
import com.intellij.icons.AllIcons
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import org.phellang.language.psi.PhelNamespaceUtils
import org.phellang.language.psi.files.PhelFile
import org.phellang.language.psi.utils.PhelPsiUtils
import org.phellang.run.test.PhelTestDetection

/**
 * Puts a Run gutter icon on the `ns` of a file's namespace declaration, and on each `deftest`.
 *
 * Still no marker on a top-level `defn`: that would suggest the CLI can run a single function, which
 * `phel run <path>` cannot, since it takes a file or a namespace. A `deftest` is different because
 * `phel test --filter` genuinely can run one test, so the icon offers something that exists.
 */
class PhelRunLineMarkerContributor : RunLineMarkerContributor() {

    override fun getInfo(element: PsiElement): Info? {
        // Called for every element in the file, so reject non-leaves before touching the tree.
        if (element.firstChild != null) return null

        return namespaceInfo(element) ?: testInfo(element)
    }

    private fun namespaceInfo(element: PsiElement): Info? {
        if (element.text != NS_KEYWORD) return null

        val file = element.containingFile as? PhelFile ?: return null
        val declaration = PhelNamespaceUtils.findNamespaceDeclaration(file) ?: return null
        val head = PhelPsiUtils.asSymbol(declaration.forms.firstOrNull()) ?: return null
        // The `ns` text sits on a leaf beneath the symbol; `strict = false` also accepts the symbol
        // itself, so this covers both shapes without a separate identity check.
        if (!PsiTreeUtil.isAncestor(head, element, false)) return null

        return info("Run ${file.name}")
    }

    private fun testInfo(element: PsiElement): Info? {
        val name = PhelTestDetection.deftestHeadedBy(element) ?: return null

        return info("Run $name")
    }

    private fun info(tooltip: String): Info =
        Info(AllIcons.RunConfigurations.TestState.Run, { tooltip }, *ExecutorAction.getActions(0))

    private companion object {
        const val NS_KEYWORD = "ns"
    }
}
