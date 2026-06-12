package org.phellang.integration.psi

import com.intellij.psi.PsiManager
import org.phellang.integration.PhelIntegrationTestCase
import org.phellang.language.psi.files.PhelFile

/**
 * Ground-truth check that `(load "…")`, `(load-file "…")` and `(:require-file "…")`
 * string arguments produce a file reference that resolves, mirroring the user's
 * `(load "main")` / `(load "core/meta")` scenarios.
 */
class PhelLoadReferenceTest : PhelIntegrationTestCase() {

    fun testLoadResolvesCallerRelativeSiblingWithoutExtension() {
        myFixture.addFileToProject("src/main.phel", "(ns app\\main)\n")
        val caller = myFixture.addFileToProject("src/comments.phel", "(ns app\\comments)\n(load \"main\")\n")

        val target = resolveAtMarker(caller, "\"main\"")
        assertNotNull("(load \"main\") should resolve to a file", target)
        assertEquals("main.phel", (target as? PhelFile)?.name)
    }

    fun testLoadResolvesCallerRelativeSubdirectory() {
        myFixture.addFileToProject("src/phel/core/meta.phel", "(ns phel\\core\\meta)\n")
        val caller = myFixture.addFileToProject("src/phel/core.phel", "(ns phel\\core)\n(load \"core/meta\")\n")

        val target = resolveAtMarker(caller, "\"core/meta\"")
        assertNotNull("(load \"core/meta\") should resolve", target)
        assertEquals("meta.phel", (target as? PhelFile)?.name)
    }

    fun testLoadFileResolvesPathWithExtension() {
        myFixture.addFileToProject("src/module.phel", "(ns app\\module)\n")
        val caller = myFixture.addFileToProject("src/app.phel", "(ns app\\app)\n(load-file \"module.phel\")\n")

        val target = resolveAtMarker(caller, "\"module.phel\"")
        assertNotNull("(load-file \"module.phel\") should resolve", target)
        assertEquals("module.phel", (target as? PhelFile)?.name)
    }

    fun testLoadDoesNotResolveWhenTargetMissing() {
        val caller = myFixture.addFileToProject("src/lonely.phel", "(ns app\\lonely)\n(load \"ghost\")\n")
        val target = resolveAtMarker(caller, "\"ghost\"")
        assertNull("missing target should not resolve", target)
    }

    /** Finds the string literal [marker] in [file] and resolves the reference at it. */
    private fun resolveAtMarker(file: com.intellij.psi.PsiFile, marker: String): Any? {
        val phelFile = PsiManager.getInstance(project).findFile(file.virtualFile) as PhelFile
        val offset = phelFile.text.indexOf(marker) + 1 // inside the opening quote
        return phelFile.findReferenceAt(offset)?.resolve()
    }
}
