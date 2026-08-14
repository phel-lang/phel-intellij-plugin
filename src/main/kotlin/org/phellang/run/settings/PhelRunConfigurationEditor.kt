package org.phellang.run.settings

import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import com.intellij.openapi.options.SettingsEditor
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.TextFieldWithBrowseButton
import com.intellij.ui.RawCommandLineEditor
import com.intellij.ui.components.JBLabel
import com.intellij.util.ui.FormBuilder
import org.phellang.run.PhelRunConfiguration
import javax.swing.JComponent
import javax.swing.JPanel

class PhelRunConfigurationEditor(private val project: Project) : SettingsEditor<PhelRunConfiguration>() {

    private val scriptPathField = TextFieldWithBrowseButton().apply {
        addBrowseFolderListener(
            project,
            FileChooserDescriptorFactory.createSingleFileDescriptor("phel")
                .withTitle("Phel File")
                .withDescription("Choose the .phel file to run"),
        )
    }

    /**
     * The platform's own program-arguments component: a single line that expands, parsing and
     * quoting with the same [com.intellij.util.execution.ParametersListUtil] encoding the
     * configuration stores, so what is typed and what is stored cannot diverge.
     */
    private val programArgumentsField = RawCommandLineEditor()

    private val workingDirectoryField = TextFieldWithBrowseButton().apply {
        addBrowseFolderListener(
            project,
            FileChooserDescriptorFactory.createSingleFolderDescriptor()
                .withTitle("Working Directory")
                .withDescription("Directory phel runs in; defaults to the project root"),
        )
    }

    override fun resetEditorFrom(configuration: PhelRunConfiguration) {
        scriptPathField.text = configuration.scriptPath
        programArgumentsField.text = configuration.programArguments
        workingDirectoryField.text = configuration.workingDirectory
    }

    override fun applyEditorTo(configuration: PhelRunConfiguration) {
        configuration.scriptPath = scriptPathField.text.trim()
        configuration.programArguments = programArgumentsField.text.trim()
        configuration.workingDirectory = workingDirectoryField.text.trim()
    }

    override fun createEditor(): JComponent = panel

    private val panel: JPanel = FormBuilder.createFormBuilder()
        .addLabeledComponent(JBLabel("Phel file:"), scriptPathField, true)
        .addLabeledComponent(JBLabel("Program arguments:"), programArgumentsField, true)
        .addLabeledComponent(JBLabel("Working directory:"), workingDirectoryField, true)
        .panel
}
