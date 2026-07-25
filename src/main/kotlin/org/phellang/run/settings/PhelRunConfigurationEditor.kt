package org.phellang.run.settings

import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import com.intellij.openapi.options.SettingsEditor
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.TextFieldWithBrowseButton
import com.intellij.ui.components.JBLabel
import com.intellij.util.ui.FormBuilder
import org.phellang.run.PhelRunConfiguration
import javax.swing.JComponent
import javax.swing.JPanel

class PhelRunConfigurationEditor(private val project: Project) : SettingsEditor<PhelRunConfiguration>() {

    private val scriptPathField = TextFieldWithBrowseButton().apply {
        addBrowseFolderListener(
            "Phel File",
            "Choose the .phel file to run",
            project,
            FileChooserDescriptorFactory.createSingleFileDescriptor("phel"),
        )
    }

    private val workingDirectoryField = TextFieldWithBrowseButton().apply {
        addBrowseFolderListener(
            "Working Directory",
            "Directory phel runs in; defaults to the project root",
            project,
            FileChooserDescriptorFactory.createSingleFolderDescriptor(),
        )
    }

    override fun resetEditorFrom(configuration: PhelRunConfiguration) {
        scriptPathField.text = configuration.scriptPath
        workingDirectoryField.text = configuration.workingDirectory
    }

    override fun applyEditorTo(configuration: PhelRunConfiguration) {
        configuration.scriptPath = scriptPathField.text.trim()
        configuration.workingDirectory = workingDirectoryField.text.trim()
    }

    override fun createEditor(): JComponent = panel

    private val panel: JPanel = FormBuilder.createFormBuilder()
        .addLabeledComponent(JBLabel("Phel file:"), scriptPathField, true)
        .addLabeledComponent(JBLabel("Working directory:"), workingDirectoryField, true)
        .panel
}
