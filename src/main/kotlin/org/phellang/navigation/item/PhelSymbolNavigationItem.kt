package org.phellang.navigation.item

import com.intellij.navigation.ItemPresentation
import com.intellij.navigation.NavigationItem
import com.intellij.openapi.fileEditor.OpenFileDescriptor
import com.intellij.openapi.project.Project
import org.phellang.language.infrastructure.PhelIcons
import org.phellang.registry.PhelProjectSymbol
import javax.swing.Icon

/**
 * One row in the Go to Symbol popup, backed by an indexed project symbol.
 *
 * Deliberately not a PSI element: the index stores a file and an offset, and resolving every
 * candidate to PSI just to populate the list would parse every Phel file in the project on each
 * keystroke. Navigation resolves the offset only when a row is actually chosen.
 */
class PhelSymbolNavigationItem(
    private val project: Project,
    private val symbol: PhelProjectSymbol,
) : NavigationItem {

    override fun getName(): String = symbol.name

    override fun getPresentation(): ItemPresentation = Presentation(symbol)

    override fun navigate(requestFocus: Boolean) {
        if (!canNavigate()) return
        OpenFileDescriptor(project, symbol.file, symbol.nameOffset).navigate(requestFocus)
    }

    override fun canNavigate(): Boolean = symbol.file.isValid

    override fun canNavigateToSource(): Boolean = canNavigate()

    private class Presentation(private val symbol: PhelProjectSymbol) : ItemPresentation {

        override fun getPresentableText(): String = symbol.name

        /** The namespace, so two definitions sharing a name can be told apart in the popup. */
        override fun getLocationString(): String = symbol.namespace

        override fun getIcon(unused: Boolean): Icon = PhelIcons.FILE
    }
}
