package org.phellang.run.settings

import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import com.intellij.openapi.options.SettingsEditor
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.TextFieldWithBrowseButton
import com.intellij.ui.components.JBLabel
import com.intellij.util.ui.FormBuilder
import org.phellang.run.PhelCliRunConfiguration
import javax.swing.JComponent
import javax.swing.JPanel

/** The one field every `phel` configuration has. Enough on its own for the REPL. */
open class PhelWorkingDirectoryEditor<T : PhelCliRunConfiguration>(
    project: Project,
    description: String,
) : SettingsEditor<T>() {

    protected val workingDirectoryField: TextFieldWithBrowseButton = TextFieldWithBrowseButton().apply {
        addBrowseFolderListener(
            "Working Directory",
            description,
            project,
            FileChooserDescriptorFactory.createSingleFolderDescriptor(),
        )
    }

    override fun resetEditorFrom(configuration: T) {
        workingDirectoryField.text = configuration.workingDirectory
    }

    override fun applyEditorTo(configuration: T) {
        configuration.workingDirectory = workingDirectoryField.text.trim()
    }

    override fun createEditor(): JComponent = buildPanel()

    protected open fun buildPanel(): JPanel = FormBuilder.createFormBuilder()
        .addLabeledComponent(JBLabel("Working directory:"), workingDirectoryField, true)
        .panel
}
