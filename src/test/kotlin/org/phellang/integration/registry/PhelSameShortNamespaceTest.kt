package org.phellang.integration.registry

import com.intellij.codeInspection.InspectionManager
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiRecursiveElementVisitor
import com.intellij.psi.util.PsiTreeUtil
import org.phellang.annotator.validators.PhelFunctionReferenceValidator
import org.phellang.annotator.validators.PhelReferSymbolValidator
import org.phellang.documentation.resolvers.PhelSymbolDocumentationResolver
import org.phellang.indexing.PhelProjectSymbolIndex
import org.phellang.integration.PhelIntegrationTestCase
import org.phellang.inspection.PhelArityMismatchInspection
import org.phellang.language.psi.PhelSymbol
import org.phellang.language.psi.files.PhelFile

/**
 * Two namespaces that end in the same segment, `app.util` and `lib.util`, are two namespaces.
 *
 * The project symbol index filed both under `util`, and its callers shortened the full namespace they
 * held before asking. So `lib.util :refer [x]` was satisfied by `app.util`'s `x`, hover could show the
 * other namespace's docstring, completion offered one namespace's names under the other's alias, and an
 * aliased call was checked against whichever same-named function came first: a stdlib `count`, or the
 * namesake's `helper`.
 */
class PhelSameShortNamespaceTest : PhelIntegrationTestCase() {

    private var fileIndex = 0

    override fun setUp() {
        super.setUp()
        // app.util first, so a lookup that merges the two namespaces finds app.util's entries first.
        index(myFixture.addFileToProject(
            "src/app/util.phel",
            "(ns app.util)\n(defn helper \"App helper.\" [x] x)\n(defn only-in-app [] 1)\n",
        ) as PhelFile)
        index(myFixture.addFileToProject(
            "src/lib/util.phel",
            "(ns lib.util)\n(defn helper \"Lib helper.\" [a b c] a)\n(defn count [a b] a)\n",
        ) as PhelFile)
        // Shares its short name with phel.string, whose registry entries are keyed by `string`.
        index(myFixture.addFileToProject(
            "src/app/string.phel",
            "(ns app.string)\n(defn join \"App join.\" [a b] a)\n",
        ) as PhelFile)
    }

    private fun index(file: PhelFile) = PhelProjectSymbolIndex.getInstance(project).refreshFileFromPsi(file)

    private fun main(source: String): PhelFile {
        val file = myFixture.configureByText("main${fileIndex++}.phel", source) as PhelFile
        index(file)
        return file
    }

    private fun symbolAtLast(file: PhelFile, needle: String): PhelSymbol =
        PsiTreeUtil.findElementOfClassAtOffset(file, file.text.lastIndexOf(needle), PhelSymbol::class.java, false)!!

    // The index

    fun testEachNamespaceKeepsItsOwnSymbols() {
        val index = PhelProjectSymbolIndex.getInstance(project)

        assertSameElements(index.symbolsInNamespace("app.util").map { it.name }, "helper", "only-in-app")
        assertSameElements(index.symbolsInNamespace("lib.util").map { it.name }, "helper", "count")
        assertEmpty("a short name is no namespace", index.symbolsInNamespace("util"))
    }

    fun testABackslashNamespaceIsTheSameNamespace() {
        val index = PhelProjectSymbolIndex.getInstance(project)

        assertEquals("Lib helper.", index.findInNamespace("lib\\util", "helper")?.docstring)
    }

    fun testRenamingTheNamespaceMovesItsSymbols() {
        val file = main("(ns old.place)\n(defn moved [] 1)\n")
        val documentManager = PsiDocumentManager.getInstance(project)
        val document = documentManager.getDocument(file)!!
        WriteCommandAction.runWriteCommandAction(project) { document.replaceString(4, 13, "new.place") }
        documentManager.commitDocument(document)
        index(file)

        val index = PhelProjectSymbolIndex.getInstance(project)
        assertEmpty(index.symbolsInNamespace("old.place"))
        assertEquals(listOf("moved"), index.symbolsInNamespace("new.place").map { it.name })
    }

    // :refer

    fun testAReferIsCheckedAgainstTheNamespaceItNames() {
        val file = main("(ns app.main\n  (:require lib.util :refer [helper only-in-app]))\n")

        assertEmpty(PhelReferSymbolValidator.validateReferSymbol(symbolAtLast(file, "helper")))
        assertEquals(
            listOf("Cannot resolve 'only-in-app' in namespace 'lib.util'"),
            PhelReferSymbolValidator.validateReferSymbol(symbolAtLast(file, "only-in-app")).map { it.message },
        )
    }

    /** `phel.string` has a `split`; `app.string` does not. */
    fun testAReferFromAProjectNamespaceIsNotSatisfiedByTheStdlib() {
        val file = main("(ns app.main\n  (:require app.string :refer [join split]))\n")

        assertEmpty(PhelReferSymbolValidator.validateReferSymbol(symbolAtLast(file, "join")))
        assertEquals(
            listOf("Cannot resolve 'split' in namespace 'app.string'"),
            PhelReferSymbolValidator.validateReferSymbol(symbolAtLast(file, "split")).map { it.message },
        )
    }

    /** phel.string's `split`, `trim` and the rest are no names of `app.string`. */
    fun testReferCompletionOfAProjectNamespaceLeavesTheStdlibOut() {
        myFixture.configureByText("string_refer.phel", "(ns app.main\n  (:require app.string :refer [<caret>]))\n")
        myFixture.completeBasic()

        assertEquals(listOf("join"), myFixture.lookupElementStrings)
    }

    fun testReferCompletionOffersOnlyTheNamedNamespace() {
        myFixture.configureByText("refer_completion.phel", "(ns app.main\n  (:require lib.util :refer [<caret>]))\n")
        myFixture.completeBasic()

        val offered = myFixture.lookupElementStrings.orEmpty()
        assertContainsElements(offered, "helper", "count")
        assertDoesntContain(offered, "only-in-app")
    }

    /** One clause, several specs: `count` is referred from `lib.util`, not from the clause's first namespace. */
    fun testAReferIsCheckedAgainstItsOwnSpecInAClauseOfSeveral() {
        val file = main("(ns app.main\n  (:require app.util :as u lib.util :refer [count]))\n")

        assertEmpty(PhelReferSymbolValidator.validateReferSymbol(symbolAtLast(file, "count")))
    }

    fun testAReferInAVectorSpecIsChecked() {
        val file = main("(ns app.main\n  (:require [lib.util :refer [helper only-in-app]]))\n")

        assertEmpty(PhelReferSymbolValidator.validateReferSymbol(symbolAtLast(file, "helper")))
        assertEquals(
            listOf("Cannot resolve 'only-in-app' in namespace 'lib.util'"),
            PhelReferSymbolValidator.validateReferSymbol(symbolAtLast(file, "only-in-app")).map { it.message },
        )
    }

    // Qualified names

    private fun highlightingProblems(source: String): List<String> {
        main(source)
        return myFixture.doHighlighting()
            .filter { it.severity >= HighlightSeverity.WARNING }
            .mapNotNull { it.description }
    }

    /** Completion writes a full name when the short one is taken; the editor must accept what it writes. */
    fun testAFullyQualifiedCallIntoARequiredNamespaceIsNotReported() {
        val problems = highlightingProblems("(ns app.main\n  (:require app.util lib.util))\n(app.util/helper 1)\n")

        assertEmpty(problems.filter { "app.util" in it })
    }

    fun testAFullyQualifiedCallIntoAnUnrequiredNamespaceIsReportedAsNotImported() {
        val problems = highlightingProblems("(ns app.main)\n(app.util/helper 1)\n")

        assertContainsElements(problems, "Namespace 'app.util' is not imported")
    }

    fun testAnAliasedCallIsCheckedAgainstTheAliasedNamespace() {
        val file = main("(ns app.main\n  (:require lib.util :as l))\n(l/helper 1 2 3)\n(l/only-in-app)\n")

        assertEmpty(PhelFunctionReferenceValidator.validateFunctionReference(symbolAtLast(file, "l/helper")))
        assertEquals(
            listOf("Cannot resolve function 'only-in-app' in namespace 'l'"),
            PhelFunctionReferenceValidator.validateFunctionReference(symbolAtLast(file, "l/only-in-app")).map { it.message },
        )
    }

    fun testAnAliasedCallIntoAProjectNamespaceIsNotSatisfiedByTheStdlib() {
        val file = main("(ns app.main\n  (:require app.string :as s))\n(s/join 1 2)\n(s/split \"a\" \",\")\n")

        assertEmpty(PhelFunctionReferenceValidator.validateFunctionReference(symbolAtLast(file, "s/join")))
        assertEquals(
            listOf("Cannot resolve function 'split' in namespace 's'"),
            PhelFunctionReferenceValidator.validateFunctionReference(symbolAtLast(file, "s/split")).map { it.message },
        )
    }

    /** A plain require is reached by its short name, Phel's default alias. */
    fun testADefaultAliasIsCheckedAgainstItsNamespace() {
        val file = main("(ns app.main\n  (:require lib.util))\n(util/only-in-app)\n")

        assertEquals(
            listOf("Cannot resolve function 'only-in-app' in namespace 'util'"),
            PhelFunctionReferenceValidator.validateFunctionReference(symbolAtLast(file, "util/only-in-app")).map { it.message },
        )
    }

    fun testAliasCompletionOffersOnlyTheAliasedNamespace() {
        myFixture.configureByText("alias_completion.phel", "(ns app.main\n  (:require lib.util :as l))\n(l/<caret>)\n")
        myFixture.completeBasic()

        val offered = myFixture.lookupElementStrings.orEmpty()
        assertContainsElements(offered, "l/helper", "l/count")
        assertDoesntContain(offered, "l/only-in-app")
    }

    /**
     * `util` already names `lib.util` here. Phel keeps the last alias of a name, so a plain
     * `(:require app.util)` would take `util` over and re-point the file's existing `util/` calls.
     */
    fun testANamesakeOfARequiredNamespaceIsRequiredUnderAnAliasOfItsOwn() {
        myFixture.configureByText(
            "namesake_completion.phel",
            "(ns app.main\n  (:require lib.util))\n(app-util/only<caret>)\n",
        )
        myFixture.completeBasic()

        val text = myFixture.file.text
        assertTrue(text, text.contains("(app-util/only-in-app)"))
        assertTrue(text, text.contains("(:require app.util :as app-util)"))
        assertTrue(text, text.contains("(:require lib.util)"))
    }

    /** `u` is `lib.util`'s now: the compiler keeps the last alias of a name. */
    fun testAnAliasTakenOverByALaterRequireIsNotOffered() {
        myFixture.configureByText(
            "retaken_alias.phel",
            "(ns app.main\n  (:require app.util :as u)\n  (:require lib.util :as u))\n(u/<caret>)\n",
        )
        myFixture.completeBasic()

        val offered = myFixture.lookupElementStrings.orEmpty()
        assertDoesntContain(offered, "u/only-in-app")
        assertEquals(offered.toString(), 1, offered.count { it == "u/helper" })
    }

    /** `phel.string`'s names are not `app.string`'s, whatever alias `app.string` goes by. */
    fun testStdlibCompletionUnderAProjectAliasLeavesTheStdlibOut() {
        myFixture.configureByText("project_alias.phel", "(ns app.main\n  (:require app.string :as s))\n(s/<caret>)\n")
        myFixture.completeBasic()

        val offered = myFixture.lookupElementStrings
        if (offered == null) {
            assertTrue("the sole candidate was inserted", myFixture.file.text.contains("(s/join)"))
        } else {
            assertDoesntContain(offered, "s/split")
            assertEquals(offered.toString(), 1, offered.count { it == "s/join" })
        }
    }

    /** Editing `x.util` used to hide every other `*.util` namespace from completion. */
    fun testAnotherNamespaceWithTheCurrentShortNameIsOffered() {
        myFixture.configureByText("x_util.phel", "(ns x.util)\n(util/only<caret>)\n")
        myFixture.completeBasic()

        assertTrue(myFixture.file.text, myFixture.file.text.contains("(util/only-in-app)"))
    }

    // Arity

    private fun arityWarnings(source: String): List<String> {
        val file = main(source)
        val holder = ProblemsHolder(InspectionManager.getInstance(project), file, true)
        val visitor = PhelArityMismatchInspection().buildVisitor(holder, true)
        file.accept(object : PsiRecursiveElementVisitor() {
            override fun visitElement(element: PsiElement) {
                element.accept(visitor)
                super.visitElement(element)
            }
        })
        return holder.results.map { it.descriptionTemplate }
    }

    /** `count` is a one-argument stdlib function; `l/count` is lib.util's, which takes two. */
    fun testAnAliasedCallUsesTheAliasedNamespacesArity() {
        assertEmpty(arityWarnings("(ns app.main\n  (:require lib.util :as l))\n(l/count 1 2)\n(l/helper 1 2 3)\n"))
    }

    fun testAnAliasedCallWithTheWrongArityIsFlagged() {
        val warnings = arityWarnings("(ns app.main\n  (:require lib.util :as l))\n(l/helper 1)\n")

        assertEquals(warnings.toString(), 1, warnings.size)
        assertTrue(warnings.toString(), warnings.single().contains("'l/helper'"))
    }

    fun testADefaultAliasUsesItsNamespacesArity() {
        assertEmpty(arityWarnings("(ns app.main\n  (:require lib.util))\n(util/helper 1 2 3)\n"))
    }

    fun testAFullyQualifiedCallUsesThatNamespacesArity() {
        assertEmpty(arityWarnings("(ns app.main\n  (:require lib.util))\n(lib.util/helper 1 2 3)\n(app.util/helper 1)\n"))
    }

    /** `app.string/join` takes two; `phel.string/join` takes one or two. */
    fun testAStdlibAliasedCallUsesTheStdlibsArity() {
        assertEmpty(arityWarnings("(ns app.main\n  (:require phel.string :as s))\n(s/join [\"a\"])\n(s/join \",\" [\"a\"])\n"))
    }

    fun testAStdlibAliasedCallWithTheWrongArityIsFlagged() {
        val warnings = arityWarnings("(ns app.main\n  (:require phel.string :as s))\n(s/join)\n")

        assertEquals(warnings.toString(), 1, warnings.size)
        assertTrue(warnings.toString(), warnings.single().contains("'s/join'"))
    }

    /** Checking qualified stdlib calls is new: common, correct ones must stay quiet. */
    fun testCommonQualifiedStdlibCallsAreNotFlagged() {
        val source = """
            (ns app.main
              (:require phel.string :as s)
              (:require phel.json))
            (defn run [x]
              (s/split "a,b" ",")
              (s/split "a,b" "," 2)
              (s/trim " a ")
              (s/replace "a" "a" "b")
              (s/starts-with? "ab" "a")
              (s/upper-case "a")
              (s/blank? "")
              (json/encode {:a 1})
              (json/decode "{}")
              (core/count x))
        """.trimIndent() + "\n"

        assertEmpty(arityWarnings(source))
    }

    /** A name `lib.util` lacks: the validator reports it; arity must not borrow `app.util`'s. */
    fun testAQualifiedCallToAMissingNameTakesNoOtherNamespacesArity() {
        assertEmpty(arityWarnings("(ns app.main\n  (:require lib.util :as l))\n(l/only-in-app 1)\n"))
    }

    // Hover

    private fun hover(file: PhelFile, needle: String): String {
        val symbol = symbolAtLast(file, needle)
        return PhelSymbolDocumentationResolver().resolveDocumentation(symbol, symbol).orEmpty()
    }

    fun testHoverOnAReferredNameShowsTheReferringNamespacesDocs() {
        val file = main("(ns app.main\n  (:require lib.util :refer [helper]))\n(helper 1 2 3)\n")

        val doc = hover(file, "helper")
        assertTrue(doc, doc.contains("Lib helper."))
    }

    fun testHoverInsideTheReferVectorShowsTheNamedNamespacesDocs() {
        val file = main("(ns app.main\n  (:require lib.util :refer [helper]))\n")

        val doc = hover(file, "helper")
        assertTrue(doc, doc.contains("Lib helper."))
    }

    fun testHoverOnAnAliasedNameShowsTheAliasedNamespacesDocs() {
        val file = main("(ns app.main\n  (:require lib.util :as l))\n(l/helper 1 2 3)\n")

        val doc = hover(file, "l/helper")
        assertTrue(doc, doc.contains("Lib helper."))
    }

    fun testHoverOnAnAliasedProjectNameShowsItsOwnDocsNotTheStdlibs() {
        val file = main("(ns app.main\n  (:require app.string :as s))\n(s/join 1 2)\n")

        val doc = hover(file, "s/join")
        assertTrue(doc, doc.contains("App join."))
        assertFalse(doc, doc.contains("Returns a string of all elements in coll"))
    }

    fun testHoverInsideAProjectReferVectorShowsItsOwnDocsNotTheStdlibs() {
        val file = main("(ns app.main\n  (:require app.string :refer [join]))\n")

        val doc = hover(file, "join")
        assertTrue(doc, doc.contains("App join."))
        assertFalse(doc, doc.contains("Returns a string of all elements in coll"))
    }

    /** Another file of `lib.util` calling its own namespace's `helper`. */
    fun testHoverInTheSameNamespaceShowsItsOwnDocs() {
        val file = main("(ns lib.util)\n(defn g [] (helper 1 2 3))\n")

        val doc = hover(file, "helper")
        assertTrue(doc, doc.contains("Lib helper."))
    }
}
