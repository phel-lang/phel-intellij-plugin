package org.phellang.integration.psi

import com.intellij.psi.PsiManager
import org.phellang.integration.PhelIntegrationTestCase
import org.phellang.language.psi.files.PhelFile

/**
 * Ground-truth check that a namespace-qualified symbol — `util/helper`, `u/helper` via an
 * `:as` alias — resolves to its definition in the required module.
 */
class PhelQualifiedSymbolResolutionTest : PhelIntegrationTestCase() {

    fun testQualifiedSymbolResolvesThroughDirectRequire() {
        myFixture.addFileToProject("src/util.phel", "(ns app\\util)\n(defn helper [] 1)\n")
        val caller = myFixture.addFileToProject(
            "src/main.phel", "(ns app\\main\n  (:require app\\util))\n(util/helper)\n"
        )

        val target = resolveAtMarker(caller, "util/helper")
        assertNotNull("util/helper should resolve via the direct require", target)
        assertEquals("util.phel", target!!.containingFile.name)
    }

    fun testQualifiedSymbolResolvesThroughAlias() {
        myFixture.addFileToProject("src/util.phel", "(ns app\\util)\n(defn helper [] 1)\n")
        val caller = myFixture.addFileToProject(
            "src/main.phel", "(ns app\\main\n  (:require app\\util :as u))\n(u/helper)\n"
        )

        val target = resolveAtMarker(caller, "u/helper")
        assertNotNull("u/helper should resolve via the :as alias", target)
        assertEquals("util.phel", target!!.containingFile.name)
    }

    fun testUnknownQualifierDoesNotResolve() {
        val caller = myFixture.addFileToProject(
            "src/main.phel", "(ns app\\main)\n(nope/helper)\n"
        )

        assertNull(resolveAtMarker(caller, "nope/helper"))
    }

    private fun resolveAtMarker(file: com.intellij.psi.PsiFile, marker: String): com.intellij.psi.PsiElement? {
        val phelFile = PsiManager.getInstance(project).findFile(file.virtualFile) as PhelFile
        // A qualified symbol's reference range covers only the name after the `/`.
        val offset = phelFile.text.indexOf(marker) + marker.indexOf('/') + 2
        return phelFile.findReferenceAt(offset)?.resolve()
    }
}
