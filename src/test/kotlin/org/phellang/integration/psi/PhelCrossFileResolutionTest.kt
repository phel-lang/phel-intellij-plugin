package org.phellang.integration.psi

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiPolyVariantReference
import com.intellij.psi.util.PsiTreeUtil
import org.phellang.integration.PhelIntegrationTestCase
import org.phellang.language.psi.PhelSymbol

/**
 * Which file an unqualified or qualified name resolves into, across files.
 *
 * The plugin follows the Phel compiler (`SymbolResolver::resolveWithoutAlias`): a bare name is a
 * local, then a `:refer`'d definition, or else one from the current namespace, and otherwise
 * `phel.core`. It never reaches into a namespace the file did not refer from. It used to match the
 * name in every project file, which both cost a sweep per symbol and offered targets the compiler
 * rejects.
 *
 * Every test builds its own namespaces (prefixed by the test name) and paths, since the shared light
 * project does not reliably clean files between tests and the namespace lookup is project-wide.
 */
class PhelCrossFileResolutionTest : PhelIntegrationTestCase() {

    private val prefix: String get() = getTestName(true).lowercase()

    private fun ns(short: String) = "$prefix.$short"

    private fun file(short: String, text: String): PsiFile =
        myFixture.addFileToProject("src/$prefix/$short.phel", text)

    private fun vendorFile(relative: String, text: String): PsiFile {
        myFixture.addFileToProject("composer.json", "{}")
        return myFixture.addFileToProject("vendor/phel-lang/phel-lang/src/phel/$relative", text)
    }

    /** The last occurrence of [text] in [file]: in every fixture here, the use rather than the definition. */
    private fun use(file: PsiFile, text: String): PhelSymbol =
        PsiTreeUtil.findChildrenOfType(file, PhelSymbol::class.java).last { it.text == text }

    private fun targets(symbol: PhelSymbol): List<PsiElement> =
        (symbol.reference as PsiPolyVariantReference).multiResolve(false).mapNotNull { it.element }

    private fun fileOf(element: PsiElement?): String? = element?.containingFile?.virtualFile?.path

    fun testANameDefinedOnlyInAnUnrequiredNamespaceHasNoTarget() {
        file("util", "(ns ${ns("util")})\n(defn helper [] 1)\n")
        val main = file("main", "(ns ${ns("main")})\n(defn f [] (helper))\n")

        assertEmpty(targets(use(main, "helper")))
    }

    fun testAReferredNameResolvesOnlyIntoTheReferredNamespace() {
        val util = file("util", "(ns ${ns("util")})\n(defn helper [] 1)\n")
        file("other", "(ns ${ns("other")})\n(defn helper [] 2)\n")
        val main = file("main", "(ns ${ns("main")}\n  (:require ${ns("util")} :refer [helper]))\n(defn f [] (helper))\n")

        val found = targets(use(main, "helper"))

        assertEquals(1, found.size)
        assertEquals(util.virtualFile.path, fileOf(found.single()))
    }

    /** The vector entry the compiler also accepts (`NsSymbol::analyzeRequireVectorEntry`). */
    fun testAVectorEntryReferResolvesIntoTheReferredNamespace() {
        val util = file("util", "(ns ${ns("util")})\n(defn helper [] 1)\n")
        val main = file("main", "(ns ${ns("main")}\n  (:require [${ns("util")} :refer [helper]]))\n(defn f [] (helper))\n")

        assertEquals(util.virtualFile.path, fileOf(use(main, "helper").reference?.resolve()))
    }

    fun testAReferredPrivateDefinitionHasNoTarget() {
        file("util", "(ns ${ns("util")})\n(defn- secret [] 1)\n")
        val main = file("main", "(ns ${ns("main")}\n  (:require ${ns("util")} :refer [secret]))\n(defn f [] (secret))\n")

        assertEmpty(targets(use(main, "secret")))
    }

    fun testACoreNameResolvesToPhelCoreEvenWhenAnotherNamespaceDefinesIt() {
        val core = vendorFile("core.phel", "(ns phel.core)\n(defn map [f xs] xs)\n")
        file("other", "(ns ${ns("other")})\n(defn map [x] x)\n")
        val main = file("main", "(ns ${ns("main")})\n(defn f [] (map inc [1]))\n")

        val found = targets(use(main, "map"))

        assertEquals(1, found.size)
        assertEquals(core.virtualFile.path, fileOf(found.single()))
    }

    /** The compiler checks refers before the current namespace (`SymbolResolver.php:273`). */
    fun testAReferWinsOverASameFileDefinition() {
        val util = file("util", "(ns ${ns("util")})\n(defn helper [] 1)\n")
        val main = file(
            "main",
            "(ns ${ns("main")}\n  (:require ${ns("util")} :refer [helper]))\n(defn helper [] 2)\n(defn f [] (helper))\n",
        )

        assertEquals(util.virtualFile.path, fileOf(use(main, "helper").reference?.resolve()))
    }

    fun testAQualifiedNameReachesAFileThatJoinsTheNamespaceWithInNs() {
        file("util", "(ns ${ns("util")})\n(defn base [] 1)\n")
        val extra = file("util_extra", "(in-ns ${ns("util")})\n(defn extra [] 2)\n")
        val main = file("main", "(ns ${ns("main")}\n  (:require ${ns("util")} :as u))\n(defn f [] (u/extra))\n")

        assertEquals(extra.virtualFile.path, fileOf(use(main, "u/extra").reference?.resolve()))
    }

    fun testAPrivateDefinitionInAnotherFileOfTheSameNamespaceResolves() {
        val util = file("util", "(ns ${ns("util")})\n(defn f [] (hidden))\n")
        val extra = file("util_extra", "(in-ns ${ns("util")})\n(defn- hidden [] 1)\n")

        assertEquals(extra.virtualFile.path, fileOf(use(util, "hidden").reference?.resolve()))
    }

    fun testAStandardLibraryReferResolvesToTheStandardLibraryFile() {
        val string = vendorFile("string.phel", "(ns phel.string)\n(defn join [sep xs] xs)\n")
        val main = file("main", "(ns ${ns("main")}\n  (:require phel.string :refer [join]))\n(defn f [] (join \",\" [1]))\n")

        assertEquals(string.virtualFile.path, fileOf(use(main, "join").reference?.resolve()))
    }

    fun testACurrentFileDefinitionResolvesFirst() {
        file("other", "(ns ${ns("other")})\n(defn helper [] 2)\n")
        val main = file("main", "(ns ${ns("main")})\n(defn helper [] 1)\n(defn f [] (helper))\n")

        assertEquals(main.virtualFile.path, fileOf(use(main, "helper").reference?.resolve()))
    }

    /** No require: the qualifier is matched against short namespace names, as before (spec D2). */
    fun testAnUnrequiredQualifierStillResolvesByShortNamespace() {
        // A dot-free namespace, so its short name is unique to this test too.
        val lib = "${prefix}lib"
        val util = file("lib", "(ns $lib)\n(defn helper [] 1)\n")
        val main = file("main", "(ns ${ns("main")})\n(defn f [] ($lib/helper))\n")

        assertEquals(util.virtualFile.path, fileOf(use(main, "$lib/helper").reference?.resolve()))
    }
}
