package org.phellang.refactoring

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import org.phellang.language.psi.files.PhelFile
import org.phellang.refactoring.extract.PhelExtractFunctionHandler

/**
 * The Refactor menu entry for Extract Function.
 *
 * An action of its own because the platform has no language-agnostic extract-method extension point:
 * `RefactoringSupportProvider` offers hooks for introducing variables, constants, fields and
 * parameters, but the Extract Method action is Java's. Registering here gives Phel the same menu
 * position without pretending to be that.
 */
class PhelExtractFunctionAction : AnAction() {

    private val handler = PhelExtractFunctionHandler()

    override fun getActionUpdateThread(): ActionUpdateThread = ActionUpdateThread.BGT

    override fun update(e: AnActionEvent) {
        e.presentation.isEnabledAndVisible = e.getData(CommonDataKeys.PSI_FILE) is PhelFile &&
                e.getData(CommonDataKeys.EDITOR) != null
    }

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.getData(CommonDataKeys.PROJECT) ?: return
        val editor = e.getData(CommonDataKeys.EDITOR) ?: return
        val file = e.getData(CommonDataKeys.PSI_FILE) ?: return

        handler.invoke(project, editor, file, e.dataContext)
    }
}
