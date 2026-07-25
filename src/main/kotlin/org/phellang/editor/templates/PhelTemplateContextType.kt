package org.phellang.editor.templates

import com.intellij.codeInsight.template.TemplateActionContext
import com.intellij.codeInsight.template.TemplateContextType
import org.phellang.language.psi.files.PhelFile

/** Scopes the bundled live templates to Phel files, so they are not offered everywhere else. */
class PhelTemplateContextType : TemplateContextType("Phel") {

    override fun isInContext(templateActionContext: TemplateActionContext): Boolean =
        templateActionContext.file is PhelFile
}
