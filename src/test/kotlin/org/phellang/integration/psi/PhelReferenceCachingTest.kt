package org.phellang.integration.psi

import com.intellij.openapi.command.WriteCommandAction
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.util.PsiTreeUtil
import org.phellang.integration.PhelIntegrationTestCase
import org.phellang.language.psi.PhelSymbol
import org.phellang.language.psi.files.PhelFile

/**
 * Resolution is cached, and the caching is invalidated by editing.
 *
 * Resolving one symbol walks local scope, the file's definitions, the project index, vendor
 * `phel/core` and the PHP index — around 1.3 ms each, paid by the annotator, the inspections and the
 * documentation provider on every highlighting pass. It now goes through `ResolveCache`.
 *
 * That only works because `getReference()` hands back the *same* reference instance each time:
 * `ResolveCache` keys on the reference, so rebuilding one per call left the cache permanently cold.
 * Both halves are pinned here, since either alone silently restores the original cost.
 */
class PhelReferenceCachingTest : PhelIntegrationTestCase() {

    private fun symbolNamed(file: PhelFile, name: String): PhelSymbol =
        PsiTreeUtil.findChildrenOfType(file, PhelSymbol::class.java).last { it.text == name }

    fun testTheSameElementKeepsTheSameReference() {
        val file = myFixture.configureByText(
            "caching.phel", "(ns app\\caching)\n(defn helper [x] x)\n(defn caller [] (helper 1))\n"
        ) as PhelFile
        val usage = symbolNamed(file, "helper")

        assertSame("a fresh reference per call leaves ResolveCache permanently cold", usage.reference, usage.reference)
    }

    fun testResolutionIsStableAcrossRepeatedCalls() {
        val file = myFixture.configureByText(
            "stable.phel", "(ns app\\stable)\n(defn helper [x] x)\n(defn caller [] (helper 1))\n"
        ) as PhelFile
        val usage = symbolNamed(file, "helper")

        val first = usage.reference?.resolve()
        val second = usage.reference?.resolve()

        assertNotNull("the usage should resolve to its definition", first)
        assertSame(first, second)
    }

    /**
     * The cache must not outlive the PSI it describes. Renaming the definition has to make the usage
     * stop resolving to it, or a stale target survives every later edit.
     */
    fun testEditingInvalidatesTheCachedResolution() {
        val file = myFixture.configureByText(
            "invalidate.phel", "(ns app\\invalidate)\n(defn helper [x] x)\n(defn caller [] (helper 1))\n"
        ) as PhelFile

        assertNotNull("precondition: resolves before the edit", symbolNamed(file, "helper").reference?.resolve())

        val documentManager = PsiDocumentManager.getInstance(project)
        WriteCommandAction.runWriteCommandAction(project) {
            val document = documentManager.getDocument(file)!!
            document.setText(document.text.replace("(defn helper [x] x)", "(defn renamed [x] x)"))
            documentManager.commitDocument(document)
        }

        assertNull(
            "the definition is gone, so the cached target must have gone with it",
            symbolNamed(file, "helper").reference?.resolve(),
        )
    }
}
