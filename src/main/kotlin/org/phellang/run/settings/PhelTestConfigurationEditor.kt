package org.phellang.run.settings

import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.components.JBTextArea
import com.intellij.ui.components.JBTextField
import com.intellij.openapi.project.Project
import com.intellij.util.ui.FormBuilder
import org.phellang.run.PhelTestConfiguration
import javax.swing.JPanel

class PhelTestConfigurationEditor(project: Project) :
    PhelWorkingDirectoryEditor<PhelTestConfiguration>(project, "Directory tests run in; defaults to the project root") {

    /**
     * A text area rather than a single-line field: paths are separated by line breaks, so the user
     * has to be able to type one. They used to be space-separated, which broke every path with a
     * space in it.
     */
    private val pathsField = JBTextArea(PATH_FIELD_ROWS, 0)

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
        .addLabeledComponent(JBLabel("Paths, one per line (blank runs everything):"), JBScrollPane(pathsField), true)
        .addLabeledComponent(JBLabel("Test name (blank runs every test in scope):"), testNameField, true)
        .addLabeledComponent(JBLabel("Working directory:"), workingDirectoryField, true)
        .panel

    private companion object {
        const val PATH_FIELD_ROWS = 3
    }
}
