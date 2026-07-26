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

    /** Editable so a configuration created from a `deftest` gutter icon survives a trip through the dialog. */
    private val testNameField = JBTextField()

    override fun resetEditorFrom(configuration: PhelTestConfiguration) {
        super.resetEditorFrom(configuration)
        pathsField.text = configuration.testPaths
        testNameField.text = configuration.testName
    }

    override fun applyEditorTo(configuration: PhelTestConfiguration) {
        super.applyEditorTo(configuration)
        configuration.testPaths = pathsField.text.trim()
        configuration.testName = testNameField.text.trim()
    }

    override fun buildPanel(): JPanel = FormBuilder.createFormBuilder()
        .addLabeledComponent(JBLabel("Paths (blank runs everything):"), pathsField, true)
        .addLabeledComponent(JBLabel("Test name (blank runs every test in scope):"), testNameField, true)
        .addLabeledComponent(JBLabel("Working directory:"), workingDirectoryField, true)
        .panel
}
