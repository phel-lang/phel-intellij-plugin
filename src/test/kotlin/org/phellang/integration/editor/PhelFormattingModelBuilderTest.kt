package org.phellang.integration.editor

import com.intellij.openapi.command.WriteCommandAction
import com.intellij.application.options.CodeStyle
import com.intellij.psi.codeStyle.CodeStyleManager
import org.phellang.integration.PhelIntegrationTestCase
import org.phellang.language.infrastructure.PhelLanguage

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

    // ---- Every bracketed container indents, not only the four obvious ones ----

    /**
     * `#(...)` is a container like any other. `CONTAINERS` listed only LIST/VEC/MAP/SET, so an
     * anonymous function's body stayed flush left while the Enter handler indented it — the two
     * disagreeing about the same line.
     */
    fun testIndentsAnAnonymousFunctionBody() {
        assertEquals("#(* %\n  2)\n", reformatted("#(* %\n2)\n"))
    }

    fun testIndentsAReaderConditionalBody() {
        assertEquals("#?(:a 1\n  :b 2)\n", reformatted("#?(:a 1\n:b 2)\n"))
    }

    fun testIndentsASplicingReaderConditionalBody() {
        assertEquals("#?@(:a 1\n  :b 2)\n", reformatted("#?@(:a 1\n:b 2)\n"))
    }

    // ---- Code Style options the formatter honours ----

    private fun reformattedWith(text: String, configure: (com.intellij.psi.codeStyle.CommonCodeStyleSettings) -> Unit): String {
        val file = myFixture.configureByText("core.phel", text)
        val settings = CodeStyle.getSettings(project).getCommonSettings(PhelLanguage)
        val keepBlankLines = settings.KEEP_BLANK_LINES_IN_CODE
        val aroundTopLevel = settings.BLANK_LINES_AROUND_METHOD
        try {
            configure(settings)
            WriteCommandAction.runWriteCommandAction(project) {
                CodeStyleManager.getInstance(project).reformat(file)
            }
            return file.text
        } finally {
            settings.KEEP_BLANK_LINES_IN_CODE = keepBlankLines
            settings.BLANK_LINES_AROUND_METHOD = aroundTopLevel
        }
    }

    /** The formatter used to hard-code this to 1, silently overriding whatever the page showed. */
    fun testHonoursKeepMaximumBlankLines() {
        val source = "(def a 1)\n\n\n\n(def b 2)\n"

        val collapsedToOne = reformattedWith(source) { it.KEEP_BLANK_LINES_IN_CODE = 1 }

        assertEquals("(def a 1)\n\n(def b 2)\n", collapsedToOne)
    }

    fun testKeepsMoreBlankLinesWhenTheSettingAllowsIt() {
        val source = "(def a 1)\n\n\n\n(def b 2)\n"

        val keptTwo = reformattedWith(source) { it.KEEP_BLANK_LINES_IN_CODE = 2 }

        assertEquals("(def a 1)\n\n\n(def b 2)\n", keptTwo)
    }

    fun testForcesBlankLinesBetweenTopLevelFormsWhenAsked() {
        val source = "(def a 1)\n(def b 2)\n"

        val spaced = reformattedWith(source) { it.BLANK_LINES_AROUND_METHOD = 1 }

        assertEquals("(def a 1)\n\n(def b 2)\n", spaced)
    }

    /** Zero, the default, must leave the author's spacing alone rather than reflow every file. */
    fun testLeavesTopLevelSpacingAloneByDefault() {
        val source = "(def a 1)\n(def b 2)\n"

        val untouched = reformattedWith(source) { it.BLANK_LINES_AROUND_METHOD = 0 }

        assertEquals(source, untouched)
    }

    fun testPutsTopLevelFormsOnSeparateLines() {
        val joined = reformattedWith("(def a 1) (def b 2)\n") { it.BLANK_LINES_AROUND_METHOD = 0 }

        assertEquals("(def a 1)\n(def b 2)\n", joined)
    }
}
