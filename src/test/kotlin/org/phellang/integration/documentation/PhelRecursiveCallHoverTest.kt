package org.phellang.integration.documentation

import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.util.PsiTreeUtil
import org.phellang.documentation.resolvers.PhelSymbolDocumentationResolver
import org.phellang.indexing.PhelProjectSymbolIndex
import org.phellang.integration.PhelIntegrationTestCase
import org.phellang.language.psi.PhelSymbol
import org.phellang.language.psi.files.PhelFile

/**
 * What hover says about a call to a function defined in the same file.
 *
 * `isLocalBindingOrReference` counts a same-file function reference as local, so the recursive
 * `factorial` in `(* n (factorial (dec n)))` took the local-symbol path and was described as a
 * "Function Argument" — hiding the very docstring the user hovered for.
 */
class PhelRecursiveCallHoverTest : PhelIntegrationTestCase() {

    private val indexed = mutableListOf<VirtualFile>()

    private val factorial = """
        (ns my\app)
        (defn factorial [n]
          "Calculates the factorial of n."
          (if (<= n 1)
            1
            (* n (factorial (dec n)))))
    """.trimIndent()

    /** Removes what this class indexed: the light project is shared, and a later class scans it. */
    override fun tearDown() {
        try {
            val index = project.getServiceIfCreated(PhelProjectSymbolIndex::class.java)
            indexed.forEach { file -> index?.removeFile(file) }
        } finally {
            super.tearDown()
        }
    }

    private fun hoverAtLast(source: String, needle: String): String {
        val file = myFixture.configureByText("hv.phel", source) as PhelFile
        file.virtualFile?.let { indexed += it }
        PhelProjectSymbolIndex.getInstance(project).refreshFileFromPsi(file)

        val offset = myFixture.file.text.lastIndexOf(needle)
        val symbol = PsiTreeUtil.findElementOfClassAtOffset(myFixture.file, offset, PhelSymbol::class.java, false)!!

        return PhelSymbolDocumentationResolver().resolveDocumentation(symbol, symbol).orEmpty()
            .replace(Regex("<[^>]*>"), " ").replace(Regex("\\s+"), " ").trim()
    }

    fun testRecursiveCallShowsTheFunctionDocumentation() {
        val doc = hoverAtLast(factorial, "factorial")

        assertTrue("expected the docstring, got: $doc", doc.contains("Calculates the factorial of n."))
        assertTrue("expected the signature, got: $doc", doc.contains("(factorial n)"))
    }

    fun testRecursiveCallIsNotDescribedAsAnArgument() {
        val doc = hoverAtLast(factorial, "factorial")

        assertFalse("a recursive call is a call, not an argument: $doc", doc.contains("Function Argument"))
    }

    fun testCallToASiblingDefinitionShowsItsDocumentation() {
        val source = "(ns my\\app)\n(defn helper [x] \"Helps with x.\" x)\n(defn g [] (helper 1))"

        val doc = hoverAtLast(source, "helper")

        assertTrue("expected helper's docstring, got: $doc", doc.contains("Helps with x."))
    }

    /** The narrowing must not swallow real parameters. */
    fun testParameterStillReadsAsAnArgument() {
        val doc = hoverAtLast(factorial, "n 1")

        assertTrue("a parameter is still an argument: $doc", doc.contains("Function Argument"))
    }

    fun testLetBindingStillReadsAsALetBinding() {
        val source = "(ns my\\app)\n(defn f [] (let [total 1] (+ total total)))"

        val doc = hoverAtLast(source, "total")

        assertFalse("a let binding is not a function: $doc", doc.contains("(total"))
    }
}
