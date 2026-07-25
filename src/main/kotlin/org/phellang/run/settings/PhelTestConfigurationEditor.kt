package org.phellang.run.settings

import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBTextField
import com.intellij.openapi.project.Project
import com.intellij.util.ui.FormBuilder
import org.phellang.run.PhelTestConfiguration
import javax.swing.JPanel

class PhelTestConfigurationEditor(project: Project) :
    PhelWorkingDirectoryEditor<PhelTestConfiguration>(project, "Directory tests run in; defaults to the project root") {

    private val pathsField = JBTextField()

    override fun resetEditorFrom(configuration: PhelTestConfiguration) {
        super.resetEditorFrom(configuration)
        pathsField.text = configuration.testPaths
    }

    override fun applyEditorTo(configuration: PhelTestConfiguration) {
        super.applyEditorTo(configuration)
        configuration.testPaths = pathsField.text.trim()
    }

    override fun buildPanel(): JPanel = FormBuilder.createFormBuilder()
        .addLabeledComponent(JBLabel("Paths (blank runs everything):"), pathsField, true)
        .addLabeledComponent(JBLabel("Working directory:"), workingDirectoryField, true)
        .panel
}
