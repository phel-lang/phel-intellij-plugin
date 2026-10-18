package org.phellang.integration.documentation

import com.intellij.psi.util.PsiTreeUtil
import org.phellang.documentation.resolvers.PhelSymbolDocumentationResolver
import org.phellang.indexing.PhelProjectSymbolIndex
import org.phellang.integration.PhelIntegrationTestCase
import org.phellang.language.psi.PhelSymbol
import org.phellang.language.psi.files.PhelFile

/**
 * Hover on a bare name that a `(:require ... :refer [...])` brought in.
 *
 * The docs come from the namespace it is referred from, which hover finds through the same
 * `findReferSource` resolution uses. That lookup returned the clause's `:require` head rather than the
 * referring spec, so it never matched, and a referred `join` showed no documentation at all.
 */
class PhelReferHoverTest : PhelIntegrationTestCase() {

    private fun hoverAtLast(source: String, needle: String): String {
        val file = myFixture.configureByText("refer_hover.phel", source) as PhelFile
        val offset = file.text.lastIndexOf(needle)
        val symbol = PsiTreeUtil.findElementOfClassAtOffset(file, offset, PhelSymbol::class.java, false)!!

        return PhelSymbolDocumentationResolver().resolveDocumentation(symbol, symbol).orEmpty()
            .replace(Regex("<[^>]*>"), " ").replace(Regex("\\s+"), " ").trim()
    }

    fun testAReferredStandardLibraryFunctionShowsItsDocumentation() {
        val doc = hoverAtLast("(ns my\\app\n  (:require phel.string :refer [join]))\n(defn f [] (join \",\" [1 2]))\n", "join")

        assertTrue("expected string/join's summary, got: $doc", doc.contains("Returns a string of all elements in coll"))
    }

    /** A project namespace that shares a stdlib short name (`xhover.string`) is not `phel.string`. */
    fun testAReferredProjectFunctionShowsItsOwnDocumentation() {
        val util = myFixture.addFileToProject(
            "src/xhover/string.phel", "(ns xhover.string)\n(defn join \"Project join.\" [a b] a)\n"
        ) as PhelFile
        PhelProjectSymbolIndex.getInstance(project).refreshFileFromPsi(util)

        val doc = hoverAtLast("(ns my\\app\n  (:require xhover.string :refer [join]))\n(defn f [] (join 1 2))\n", "join")

        assertTrue("expected the project docstring, got: $doc", doc.contains("Project join."))
        assertFalse("not phel.string's docs: $doc", doc.contains("Returns a string of all elements in coll"))
    }
}
