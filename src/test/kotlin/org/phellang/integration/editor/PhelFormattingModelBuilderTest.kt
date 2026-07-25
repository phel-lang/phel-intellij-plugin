package org.phellang.integration.editor

import com.intellij.openapi.command.WriteCommandAction
import com.intellij.psi.codeStyle.CodeStyleManager
import org.phellang.integration.PhelIntegrationTestCase

/**
 * The built-in formatter, used when the project has no `phel` binary.
 *
 * Asserts indentation and spacing, not a full canonical layout: `phel format` remains the canonical
 * formatter and this is the fallback, so it aims to be predictable rather than to reproduce the CLI
 * byte for byte. Line breaks the author chose are preserved throughout.
 */
class PhelFormattingModelBuilderTest : PhelIntegrationTestCase() {

    private fun reformatted(text: String): String {
        val file = myFixture.configureByText("core.phel", text)
        WriteCommandAction.runWriteCommandAction(project) {
            CodeStyleManager.getInstance(project).reformat(file)
        }
        return file.text
    }

    fun testIndentsABodyByOneLevel() {
        val formatted = reformatted("(defn greet []\n\"hi\")\n")

        assertEquals("(defn greet []\n  \"hi\")\n", formatted)
    }

    fun testIndentsNestedFormsCumulatively() {
        val formatted = reformatted("(defn greet []\n(let [a 1]\n(println a)))\n")

        assertEquals("(defn greet []\n  (let [a 1]\n    (println a)))\n", formatted)
    }

    fun testRemovesPaddingInsideParentheses() {
        assertEquals("(println \"hi\")\n", reformatted("( println \"hi\" )\n"))
    }

    fun testRemovesPaddingInsideVectorsAndMaps() {
        assertEquals("[1 2 3]\n", reformatted("[ 1 2 3 ]\n"))
        assertEquals("{:a 1}\n", reformatted("{ :a 1 }\n"))
    }

    fun testCollapsesRunsOfSpacesBetweenForms() {
        assertEquals("(println 1 2)\n", reformatted("(println   1     2)\n"))
    }

    /** A deliberate line break is the author's; collapsing it would reflow the file onto one line. */
    fun testKeepsAuthoredLineBreaks() {
        val formatted = reformatted("(println\n  1\n  2)\n")

        assertEquals("(println\n  1\n  2)\n", formatted)
    }

    fun testLeavesTopLevelFormsUnindented() {
        val formatted = reformatted("(ns app\\core)\n(defn greet [] 1)\n")

        assertEquals("(ns app\\core)\n(defn greet [] 1)\n", formatted)
    }

    fun testDedentsAnOverIndentedBody() {
        val formatted = reformatted("(defn greet []\n        \"hi\")\n")

        assertEquals("(defn greet []\n  \"hi\")\n", formatted)
    }

    fun testLeavesAWellFormattedFileUnchanged() {
        val source = "(ns app\\core)\n\n(defn greet [name]\n  (let [a 1]\n    (println name a)))\n"

        assertEquals(source, reformatted(source))
    }
}
