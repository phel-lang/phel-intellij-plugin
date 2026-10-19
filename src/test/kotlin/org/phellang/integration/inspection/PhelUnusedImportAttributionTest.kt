package org.phellang.integration.inspection

import com.intellij.psi.PsiManager
import com.intellij.psi.util.PsiTreeUtil
import org.phellang.integration.PhelIntegrationTestCase
import org.phellang.language.psi.PhelList
import org.phellang.language.psi.PhelSymbol
import org.phellang.language.psi.analysis.PhelUnusedImportFinder
import org.phellang.language.psi.files.PhelFile

/**
 * Which uses belong to which require, read the way the compiler reads a qualifier: an import's alias,
 * or its short name for a plain require, while no later require takes that name over; and its full
 * name, always (`SymbolResolver::resolveWithAlias`).
 *
 * The finder looked an import's alias up by short namespace, so with `app.util :as u` and a plain
 * `lib.util`, `u/x` counted as a use of `lib.util`, and with the aliases swapped `app.util` was
 * reported unused while `util/x` called it. A call by full name counted for nothing, and one `:refer`
 * anywhere in a clause spared every namespace in it.
 */
class PhelUnusedImportAttributionTest : PhelIntegrationTestCase() {

    private var fileIndex = 0

    private fun unused(source: String, namespace: String): Boolean {
        val vf = myFixture.addFileToProject("src/attribution${fileIndex++}.phel", source).virtualFile
        val file = PsiManager.getInstance(project).findFile(vf) as PhelFile
        val symbol = PsiTreeUtil.findChildrenOfType(file, PhelSymbol::class.java)
            .first { it.text == namespace && PsiTreeUtil.getParentOfType(it, PhelList::class.java) != null }

        return PhelUnusedImportFinder.isUnusedImport(symbol)
    }

    fun testAnotherImportsAliasIsNoUseOfANamesake() {
        val source = "(ns app.main\n  (:require app.util :as u)\n  (:require lib.util))\n(u/x)\n"

        assertFalse(unused(source, "app.util"))
        assertTrue("u/ is app.util's", unused(source, "lib.util"))
    }

    fun testAPlainImportIsUsedByItsShortNameWhateverANamesakesAlias() {
        val source = "(ns app.main\n  (:require app.util)\n  (:require lib.util :as l))\n(util/x)\n"

        assertFalse("util/ is app.util's", unused(source, "app.util"))
        assertTrue(unused(source, "lib.util"))
    }

    fun testACallByFullNameIsAUse() {
        assertFalse(unused("(ns app.main\n  (:require app.util))\n(app.util/x)\n", "app.util"))
        assertFalse(unused("(ns app.main\n  (:require app.util :as u))\n(app.util/x)\n", "app.util"))
    }

    /** The compiler keeps the last alias of a name: `util/` is lib.util's. */
    fun testAShortNameTakenOverByALaterRequireIsNoLongerTheFirstsUse() {
        val source = "(ns app.main\n  (:require app.util)\n  (:require lib.util))\n(util/x)\n"

        assertTrue(unused(source, "app.util"))
        assertFalse(unused(source, "lib.util"))
    }

    /** One clause, several specs: only the spec that refers is spared. */
    fun testAReferSparesOnlyItsOwnSpec() {
        val source = "(ns app.main\n  (:require app.util :as u lib.util :refer [x]))\n(x)\n"

        assertTrue(unused(source, "app.util"))
        assertFalse(unused(source, "lib.util"))
    }

    fun testAVectorSpecThatRefersIsSpared() {
        assertFalse(unused("(ns app.main\n  (:require [app.util :refer [x]]))\n(x)\n", "app.util"))
    }

    fun testABackslashNamespaceIsUsedByItsShortName() {
        assertFalse(unused("(ns app\\main\n  (:require app\\util))\n(util/x)\n", "app\\util"))
    }
}
