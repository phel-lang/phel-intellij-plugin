package org.phellang.editor.paredit

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.actionSystem.DefaultActionGroup
import org.phellang.language.psi.files.PhelFile

/**
 * The Paredit submenu, shown only where it does anything.
 *
 * The group was declared in `plugin.xml` with no `<add-to-group>` at all, so nine registered actions
 * were reachable only by their `Ctrl+Alt+Shift+P` chords or through Find Action. Adding it to the
 * editor popup makes them discoverable.
 *
 * The visibility check is the group's own: [PhelPareditActionHandler] already disables each action
 * outside a Phel file, but a `popup="true"` group renders its own entry regardless, so without this
 * "Phel Paredit" would appear in the right-click menu of every file type in the IDE.
 */
class PhelPareditActionGroup : DefaultActionGroup() {

    override fun getActionUpdateThread(): ActionUpdateThread = ActionUpdateThread.BGT

    override fun update(e: AnActionEvent) {
        e.presentation.isEnabledAndVisible = e.getData(CommonDataKeys.PSI_FILE) is PhelFile
    }
}
