package org.phellang.integration.completion

import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.psi.util.PsiTreeUtil
import org.phellang.annotator.validators.PhelFunctionReferenceValidator
import org.phellang.documentation.resolvers.PhelSymbolDocumentationResolver
import org.phellang.indexing.PhelProjectSymbolIndex
import org.phellang.integration.PhelIntegrationTestCase
import org.phellang.language.psi.PhelSymbol
import org.phellang.language.psi.files.PhelFile

/**
 * A stdlib name offered where its registry spelling, `string/upper-case`, means something else.
 *
 * With `(:require app.string)` the default alias `string` is the project's. Completion offered
 * `string/upper-case` anyway, and accepting it added `(:require phel.string)`, which takes `string`
 * over (the compiler keeps the last alias of a name) and re-points every `string/` call to
 * `app.string`. It is now offered under a qualifier that names `phel.string` here: its full name when
 * it is required, else an alias of its own that the accepted completion requires it under.
 */
class PhelStdlibQualifierCompletionTest : PhelIntegrationTestCase() {

    override fun setUp() {
        super.setUp()
        PhelProjectSymbolIndex.getInstance(project).refreshFileFromPsi(
            myFixture.addFileToProject("src/app/string.phel", "(ns app.string)\n(defn join \"App join.\" [a b] a)\n") as PhelFile
        )
    }

    private fun completed(source: String): String {
        myFixture.configureByText("stdlib_qualifier.phel", source)
        myFixture.completeBasic()
        return myFixture.file.text
    }

    fun testATakenShortNameGetsAnAliasOfItsOwn() {
        val text = completed("(ns app.main\n  (:require app.string))\n(phel-string/upper-ca<caret>)\n")

        assertTrue(text, text.contains("(phel-string/upper-case)"))
        assertTrue(text, text.contains("(:require phel.string :as phel-string)"))
        assertTrue(text, text.contains("(:require app.string)"))
    }

    fun testTheRegistrySpellingIsNotOfferedWhereItMeansTheProjectNamespace() {
        myFixture.configureByText("taken.phel", "(ns app.main\n  (:require app.string))\n(string/upper-ca<caret>)\n")
        myFixture.completeBasic()

        assertDoesntContain(myFixture.lookupElementStrings.orEmpty(), "string/upper-case")
        assertFalse(myFixture.file.text, myFixture.file.text.contains("phel.string"))
    }

    /** `phel.string` is required, but a later require took `string`: its full name still names it. */
    fun testARequiredStdlibNamespaceWhoseShortNameIsTakenIsOfferedByItsFullName() {
        val text = completed("(ns app.main\n  (:require phel.string)\n  (:require app.string))\n(phel.string/upper-ca<caret>)\n")

        assertTrue(text, text.contains("(phel.string/upper-case)"))
        assertEquals("no second require: $text", 1, Regex("\\(:require phel\\.string").findAll(text).count())
    }

    fun testAFreeShortNameIsStillTheRegistrySpelling() {
        val text = completed("(ns app.main)\n(string/upper-ca<caret>)\n")

        assertTrue(text, text.contains("(string/upper-case)"))
        assertTrue(text, text.contains("(:require phel.string)"))
    }

    // What completion now writes must be accepted.

    private fun configured(source: String): PhelFile {
        val file = myFixture.configureByText("full_name.phel", source) as PhelFile
        PhelProjectSymbolIndex.getInstance(project).refreshFileFromPsi(file)
        return file
    }

    private fun symbolAtLast(file: PhelFile, needle: String): PhelSymbol =
        PsiTreeUtil.findElementOfClassAtOffset(file, file.text.lastIndexOf(needle), PhelSymbol::class.java, false)!!

    fun testAStdlibCallByFullNameIsChecked() {
        val file = configured(
            "(ns app.main\n  (:require phel.string)\n  (:require app.string))\n(phel.string/upper-case \"a\")\n(phel.string/nope \"a\")\n"
        )

        assertEmpty(PhelFunctionReferenceValidator.validateFunctionReference(symbolAtLast(file, "phel.string/upper-case")))
        assertEquals(
            listOf("Cannot resolve function 'nope' in namespace 'phel.string'"),
            PhelFunctionReferenceValidator.validateFunctionReference(symbolAtLast(file, "phel.string/nope")).map { it.message },
        )
    }

    fun testAStdlibCallByFullNameIsNotReportedByTheHighlighter() {
        configured("(ns app.main\n  (:require phel.string)\n  (:require app.string))\n(phel.string/upper-case \"a\")\n")

        val problems = myFixture.doHighlighting().filter { it.severity >= HighlightSeverity.WARNING }.mapNotNull { it.description }
        assertEmpty(problems.filter { "phel.string" in it || "upper-case" in it })
    }

    fun testAStdlibCallByFullNameShowsItsDocumentation() {
        val file = configured("(ns app.main\n  (:require phel.string))\n(phel.string/upper-case \"a\")\n")
        val symbol = symbolAtLast(file, "phel.string/upper-case")

        val doc = PhelSymbolDocumentationResolver().resolveDocumentation(symbol, symbol).orEmpty()
        assertTrue(doc, doc.contains("Converts string to all upper-case."))
    }

    /** A subnamespace's registry name keeps its middle segment: `schema.coercer/coerce`, not `coercer/coerce`. */
    fun testAnAliasedStdlibSubnamespaceIsChecked() {
        val file = configured("(ns app.main\n  (:require phel.schema.coercer :as c))\n(c/coerce 1 2)\n(c/nope 1)\n")

        assertEmpty(PhelFunctionReferenceValidator.validateFunctionReference(symbolAtLast(file, "c/coerce")))
        assertEquals(
            listOf("Cannot resolve function 'nope' in namespace 'c'"),
            PhelFunctionReferenceValidator.validateFunctionReference(symbolAtLast(file, "c/nope")).map { it.message },
        )
    }
}
