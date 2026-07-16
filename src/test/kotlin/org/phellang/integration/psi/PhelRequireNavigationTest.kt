package org.phellang.integration.psi

import com.intellij.psi.PsiManager
import org.phellang.integration.PhelIntegrationTestCase
import org.phellang.language.psi.PhelSymbol
import org.phellang.language.psi.files.PhelFile

/**
 * Ground-truth check that go-to-declaration on a namespace inside `(:require …)`
 * jumps to the required module's `(ns …)` declaration.
 */
class PhelRequireNavigationTest : PhelIntegrationTestCase() {
    fun testRequireResolvesToTargetNamespaceDeclaration() {
        myFixture.addFileToProject("src/util.phel", "(ns app\\util)\n(defn helper [] 1)\n")
        val caller = myFixture.addFileToProject(
            "src/main.phel", "(ns app\\main\n  (:require app\\util))\n"
        )

        val target = resolveAtMarker(caller, "app\\util)")
        assertTrue("require should resolve to the ns name symbol", target is PhelSymbol)
        assertEquals("app\\util", (target as PhelSymbol).text)
        assertEquals("util.phel", target.containingFile.name)
    }

    fun testRequireWithVectorSpecResolves() {
        myFixture.addFileToProject("src/math.phel", "(ns app\\math)\n")
        val caller = myFixture.addFileToProject(
            "src/main.phel", "(ns app\\main\n  (:require [app\\math :as m]))\n"
        )

        val target = resolveAtMarker(caller, "app\\math :as")
        assertTrue(target is PhelSymbol)
        assertEquals("math.phel", (target as PhelSymbol).containingFile.name)
    }

    fun testRequireVendorNamespaceDotForm() {
        myFixture.addFileToProject("composer.json", "{}")
        myFixture.addFileToProject("vendor/phel-lang/phel-lang/src/phel/string.phel", "(ns phel\\string)\n")
        val caller = myFixture.addFileToProject(
            "src/main.phel", "(ns app\\main\n  (:require phel.string))\n"
        )

        val target = resolveAtMarker(caller, "phel.string)")
        assertTrue("(:require phel.string) should resolve to the vendor file", target is PhelSymbol)
        assertEquals("string.phel", (target as PhelSymbol).containingFile.name)
    }

    fun testRequireVendorNamespaceBackslashForm() {
        myFixture.addFileToProject("composer.json", "{}")
        myFixture.addFileToProject("vendor/phel-lang/phel-lang/src/phel/string.phel", "(ns phel\\string)\n")
        val caller = myFixture.addFileToProject(
            "src/main.phel", "(ns app\\main\n  (:require phel\\string))\n"
        )

        val target = resolveAtMarker(caller, "phel\\string)")
        assertTrue(target is PhelSymbol)
        assertEquals("string.phel", (target as PhelSymbol).containingFile.name)
    }

    private fun resolveAtMarker(file: com.intellij.psi.PsiFile, marker: String): com.intellij.psi.PsiElement? {
        val phelFile = PsiManager.getInstance(project).findFile(file.virtualFile) as PhelFile
        val offset = phelFile.text.indexOf(marker) + 1 // inside the namespace symbol
        return phelFile.findReferenceAt(offset)?.resolve()
    }
}
