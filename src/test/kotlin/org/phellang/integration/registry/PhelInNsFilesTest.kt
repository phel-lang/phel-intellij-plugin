package org.phellang.integration.registry

import com.intellij.codeInspection.InspectionManager
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiRecursiveElementVisitor
import com.intellij.psi.util.PsiTreeUtil
import org.phellang.annotator.validators.PhelFunctionReferenceValidator
import org.phellang.annotator.validators.PhelReferSymbolValidator
import org.phellang.documentation.resolvers.PhelSymbolDocumentationResolver
import org.phellang.indexing.PhelProjectSymbolIndex
import org.phellang.inspection.PhelArityMismatchInspection
import org.phellang.integration.PhelIntegrationTestCase
import org.phellang.language.psi.PhelSymbol
import org.phellang.language.psi.files.PhelFile

/**
 * A file that joins a namespace with `(in-ns ...)` holds that namespace's definitions, the way
 * `phel.core` spreads over the files under `core/`.
 *
 * The project symbol index read a file's namespace from its `ns` form only, so such a file was left
 * out: `(c/bucket-fn)` was reported as "Cannot resolve function", a `:refer` of it was rejected, and
 * neither completion nor hover knew it.
 */
class PhelInNsFilesTest : PhelIntegrationTestCase() {

    private var fileIndex = 0

    override fun setUp() {
        super.setUp()
        index(myFixture.addFileToProject("src/app/core.phel", "(ns app.core)\n(defn main-fn \"Main.\" [x] x)\n") as PhelFile)
        index(myFixture.addFileToProject(
            "src/app/core/bucket.phel",
            "(in-ns app.core)\n(defn bucket-fn \"From the bucket.\" [a b] a)\n",
        ) as PhelFile)
    }

    private fun index(file: PhelFile) = PhelProjectSymbolIndex.getInstance(project).refreshFileFromPsi(file)

    private fun main(source: String): PhelFile {
        val file = myFixture.configureByText("in_ns_main${fileIndex++}.phel", source) as PhelFile
        index(file)
        return file
    }

    private fun symbolAtLast(file: PhelFile, needle: String): PhelSymbol =
        PsiTreeUtil.findElementOfClassAtOffset(file, file.text.lastIndexOf(needle), PhelSymbol::class.java, false)!!

    fun testAnInNsFileIsIndexedUnderItsNamespace() {
        val names = PhelProjectSymbolIndex.getInstance(project).symbolsInNamespace("app.core").map { it.name }

        assertSameElements(names, "main-fn", "bucket-fn")
    }

    fun testAStringInNsIsAName() {
        index(myFixture.addFileToProject("src/app/core/quoted.phel", "(in-ns \"app.core\")\n(defn quoted-fn [] 1)\n") as PhelFile)

        assertNotNull(PhelProjectSymbolIndex.getInstance(project).findInNamespace("app.core", "quoted-fn"))
    }

    fun testAnAliasedCallIntoAnInNsFileResolves() {
        val file = main("(ns app.main\n  (:require app.core :as c))\n(c/bucket-fn 1 2)\n")

        assertEmpty(PhelFunctionReferenceValidator.validateFunctionReference(symbolAtLast(file, "c/bucket-fn")))
    }

    fun testAReferOfAnInNsDefinitionResolves() {
        val file = main("(ns app.main\n  (:require app.core :refer [bucket-fn]))\n")

        assertEmpty(PhelReferSymbolValidator.validateReferSymbol(symbolAtLast(file, "bucket-fn")))
    }

    fun testHoverShowsAnInNsDefinitionsDocs() {
        val file = main("(ns app.main\n  (:require app.core :as c))\n(c/bucket-fn 1 2)\n")
        val symbol = symbolAtLast(file, "c/bucket-fn")

        val doc = PhelSymbolDocumentationResolver().resolveDocumentation(symbol, symbol).orEmpty()
        assertTrue(doc, doc.contains("From the bucket."))
    }

    fun testCompletionOffersAnInNsDefinition() {
        myFixture.configureByText("in_ns_completion.phel", "(ns app.main\n  (:require app.core :as c))\n(c/buck<caret>)\n")
        myFixture.completeBasic()

        assertTrue(myFixture.file.text, myFixture.file.text.contains("(c/bucket-fn)"))
    }

    /** Another file of the namespace being edited: its names need no qualifier and no require. */
    fun testCompletionInTheNsFileOffersTheBucketsNamesBare() {
        myFixture.configureByText("in_ns_own.phel", "(ns app.core)\n(buck<caret>)\n")
        myFixture.completeBasic()

        val text = myFixture.file.text
        assertTrue(text, text.contains("(bucket-fn)"))
        assertFalse(text, text.contains(":require"))
    }

    fun testCompletionInABucketOffersTheNsFilesNamesBare() {
        myFixture.configureByText("in_ns_bucket.phel", "(in-ns app.core)\n(main-f<caret>)\n")
        myFixture.completeBasic()

        assertTrue(myFixture.file.text, myFixture.file.text.contains("(main-fn)"))
    }

    // A vendored stdlib joins its buckets with `in-ns` too, and now reaches the index.

    private fun vendoredCore(source: String) {
        myFixture.addFileToProject("composer.json", "{}")
        index(myFixture.addFileToProject("vendor/phel-lang/phel-lang/src/phel/core/extra.phel", source) as PhelFile)
    }

    /** Its functions come from the registry, written bare; the index must not add a `core/` copy of each. */
    fun testAVendoredStdlibIsNotOfferedFromTheIndex() {
        vendoredCore("(in-ns phel.core)\n(defn vendored-only-fn [x] x)\n")

        myFixture.configureByText("vendored.phel", "(ns app.main)\n(core/vendored-only<caret>)\n")
        myFixture.completeBasic()

        assertDoesntContain(myFixture.lookupElementStrings.orEmpty(), "core/vendored-only-fn")
        assertFalse(myFixture.file.text, myFixture.file.text.contains("core/vendored-only-fn"))
    }

    /** The registry's `count` takes one; a vendored copy that says otherwise does not change a call outside it. */
    fun testAQualifiedStdlibCallKeepsTheRegistrysArity() {
        vendoredCore("(in-ns phel.core)\n(defn count [a b c] a)\n")
        val file = main("(ns app.main)\n(core/count [1])\n")

        val holder = ProblemsHolder(InspectionManager.getInstance(project), file, true)
        val visitor = PhelArityMismatchInspection().buildVisitor(holder, true)
        file.accept(object : PsiRecursiveElementVisitor() {
            override fun visitElement(element: PsiElement) {
                element.accept(visitor)
                super.visitElement(element)
            }
        })
        assertEmpty(holder.results.map { it.descriptionTemplate })
    }
}
