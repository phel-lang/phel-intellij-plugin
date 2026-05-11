package org.phellang.editor.structure

import com.intellij.icons.AllIcons
import javax.swing.Icon

enum class PhelStructuralForm(val keyword: String, val icon: Icon) {
    NAMESPACE("ns", AllIcons.Nodes.Package),
    DEF("def", AllIcons.Nodes.Field),
    DEF_PRIVATE("def-", AllIcons.Nodes.Field),
    DEFN("defn", AllIcons.Nodes.Function),
    DEFN_PRIVATE("defn-", AllIcons.Nodes.Function),
    DEFMACRO("defmacro", AllIcons.Nodes.AbstractMethod),
    DEFMACRO_PRIVATE("defmacro-", AllIcons.Nodes.AbstractMethod),
    DEFSTRUCT("defstruct", AllIcons.Nodes.Class),
    DEFINTERFACE("definterface", AllIcons.Nodes.Interface),
    DEFEXCEPTION("defexception", AllIcons.Nodes.ExceptionClass),
    DECLARE("declare", AllIcons.Nodes.Static),
    DEFTEST("deftest", AllIcons.Nodes.Test);

    companion object {
        private val byKeyword = entries.associateBy { it.keyword }

        fun fromKeyword(keyword: String?): PhelStructuralForm? = keyword?.let { byKeyword[it] }
    }
}
