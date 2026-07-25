package org.phellang.integration.editor

import com.intellij.codeInsight.template.TemplateActionContext
import com.intellij.codeInsight.template.impl.TemplateSettings
import org.phellang.editor.templates.PhelTemplateContextType
import org.phellang.integration.PhelIntegrationTestCase

/**
 * The bundled live templates and the context that scopes them.
 *
 * The abbreviation set matters as much as the templates: `PhelMainCompletionProvider.FORM_TEMPLATES`
 * already offers `()`, `defn`, `def`, `let`, `if` and `fn` through completion, and a live template
 * sharing one of those names would put two differently-behaving entries under it.
 */
class PhelLiveTemplatesTest : PhelIntegrationTestCase() {

    private val contextType = PhelTemplateContextType()

    /** The raw `<template …>` declarations from the shipped XML, one string each. */
    private fun declarations(): List<String> {
        val xml = javaClass.getResourceAsStream(TEMPLATE_RESOURCE)
            ?.bufferedReader()?.readText()
            ?: fail("$TEMPLATE_RESOURCE must be on the classpath")

        return xml.toString().split("<template ").drop(1)
    }

    private fun declaredAbbreviations(): List<String> =
        declarations().map { it.substringAfter("name=\"").substringBefore("\"") }

    private fun bundledAbbreviations(): List<String> =
        TemplateSettings.getInstance().templates
            .filter { it.groupName == "Phel" }
            .map { it.key }

    fun testBundlesThePhelTemplateGroup() {
        assertTrue("expected the Phel live template group to be registered", bundledAbbreviations().isNotEmpty())
    }

    fun testOffersTemplatesForFormsCompletionDoesNotCover() {
        val abbreviations = bundledAbbreviations()

        for (expected in listOf("defn-", "defmacro", "ns", "deftest", "when", "loop", "cond", "try")) {
            assertTrue("expected a '$expected' live template, got $abbreviations", expected in abbreviations)
        }
    }

    /** The six the completion contributor already provides; duplicating them would be confusing. */
    fun testDoesNotRedefineTheCompletionTemplates() {
        val abbreviations = bundledAbbreviations()

        for (owned in listOf("()", "defn", "def", "let", "if", "fn")) {
            assertFalse("'$owned' is already a completion template, so it must not also be a live template",
                owned in abbreviations)
        }
    }

    fun testTemplatesApplyInsidePhelFiles() {
        val file = myFixture.configureByText("t.phel", "\n")

        assertTrue(contextType.isInContext(TemplateActionContext.expanding(file, 0)))
    }

    fun testTemplatesDoNotApplyOutsidePhelFiles() {
        val file = myFixture.configureByText("t.txt", "\n")

        assertFalse(contextType.isInContext(TemplateActionContext.expanding(file, 0)))
    }

    /**
     * Every bundled template must declare the Phel context, or it is offered in every file type.
     *
     * Asserted against the shipped XML rather than the loaded `TemplateContext`: reaching the
     * registered context instance needs `contextId`, which the extension-point registration supplies
     * and a locally constructed instance lacks, and the platform asserts on its absence.
     */
    fun testEveryBundledTemplateDeclaresThePhelContext() {
        val declarations = declarations()
        assertTrue("expected bundled templates, found none", declarations.isNotEmpty())

        for (declaration in declarations) {
            val name = declaration.substringAfter("name=\"").substringBefore("\"")
            assertTrue(
                "template '$name' must declare <option name=\"Phel\" value=\"true\"/>",
                declaration.contains("<option name=\"Phel\" value=\"true\"/>"),
            )
        }
    }

    /** Guards the registration itself: a template in the XML that the IDE never loaded. */
    fun testBundlesEveryTemplateFromTheXml() {
        assertEquals(declaredAbbreviations().sorted(), bundledAbbreviations().sorted())
    }

    private companion object {
        const val TEMPLATE_RESOURCE = "/liveTemplates/Phel.xml"
    }
}
