package org.phellang.integration.completion

import com.intellij.psi.PsiManager
import org.phellang.integration.PhelIntegrationTestCase
import org.phellang.language.psi.files.PhelFile

/**
 * `php/` is the PHP interop prefix the compiler understands directly, not a namespace: there is no
 * `phel.php` to require. Completing a `php/` name used to auto-import `(:require phel.php)`, which
 * the namespace validator then flagged as non-existent, so a correct completion produced a broken
 * file and an error on hover.
 */
class PhelPhpInteropAutoImportTest : PhelIntegrationTestCase() {

    fun testCompletingPhpNativeFunctionAddsNoRequire() {
        assertNoRequireAdded("(php/strle<caret>", "php/strlen")
    }

    fun testCompletingPhpInteropFormAddsNoRequire() {
        assertNoRequireAdded("(php/ar<caret>", "php/array_map")
    }

    /** Control: the guard must exempt `php` only, leaving real namespaces auto-importing. */
    fun testCompletingRealNamespaceStillImports() {
        myFixture.configureByText("php_interop_positive.phel", "(ns app\\main)\n(json/enco<caret>\n")
        myFixture.completeBasic()

        val item = myFixture.lookupElements?.firstOrNull { it.lookupString == "json/encode" }
        assertNotNull("expected `json/encode` to be offered", item)
        myFixture.lookup.currentItem = item
        myFixture.finishLookup('\n')

        val text = currentText()
        assertTrue("a real namespace should still be auto-imported, got:\n$text", "phel.json" in text)
    }

    private fun assertNoRequireAdded(body: String, expectedLookup: String) {
        myFixture.configureByText("php_interop_test.phel", "(ns app\\main)\n$body\n")
        myFixture.completeBasic()

        if (myFixture.lookupElementStrings?.contains(expectedLookup) == true) {
            myFixture.finishLookup('\n')
        }

        val text = currentText()
        assertFalse(
            "`php` is not a namespace; no `phel.php` require may be added. Got:\n$text",
            "phel.php" in text,
        )
        assertFalse(
            "no bare `php` require may be added either. Got:\n$text",
            Regex("""\(:require\s+php[\s)]""").containsMatchIn(text),
        )
    }

    private fun currentText(): String {
        val vf = myFixture.file.virtualFile
        val psi = PsiManager.getInstance(project).findFile(vf) as PhelFile
        return psi.text
    }
}
