package org.phellang.integration.psi

import com.intellij.openapi.command.WriteCommandAction
import com.intellij.psi.PsiManager
import com.intellij.psi.util.PsiTreeUtil
import org.phellang.integration.PhelIntegrationTestCase
import org.phellang.language.psi.PhelSymbol
import org.phellang.language.psi.files.PhelFile

/**
 * Ground-truth checks for [org.phellang.language.psi.references.PhelReference.handleElementRename].
 *
 * The rule the reference split had to preserve: renaming a qualified symbol changes only the name
 * part after the `/`, leaving the namespace qualifier intact. An unqualified symbol is renamed whole.
 */
class PhelRenameTest : PhelIntegrationTestCase() {

    fun testQualifiedSymbolRenamesOnlyTheNamePart() {
        assertRename(
            source = "(ns app\\main)\n(defn f [] (math/square 5))\n",
            symbolText = "math/square",
            newName = "cube",
            expected = "math/cube",
        )
    }

    fun testAliasedQualifierIsPreserved() {
        assertRename(
            source = "(ns app\\main (:require phel\\string :as s))\n(defn f [t] (s/upper-case t))\n",
            symbolText = "s/upper-case",
            newName = "trim",
            expected = "s/trim",
        )
    }

    fun testUnqualifiedSymbolIsRenamedWhole() {
        assertRename(
            source = "(ns app\\main)\n(defn f [] (helper 5))\n",
            symbolText = "helper",
            newName = "worker",
            expected = "worker",
        )
    }

    fun testBackslashPhpQualifierIsPreserved() {
        // `\` is the PHP FQCN separator, not the name separator; only the tail after `/` renames.
        assertRename(
            source = "(ns app\\main (:use \\DateTime))\n(defn f [] (DateTime/createFromFormat))\n",
            symbolText = "DateTime/createFromFormat",
            newName = "now",
            expected = "DateTime/now",
        )
    }

    private fun assertRename(source: String, symbolText: String, newName: String, expected: String) {
        // Unique path per class: the shared test project does not reliably clean files between runs.
        val vFile = myFixture.addFileToProject("src/rename_test.phel", source).virtualFile
        val phelFile = PsiManager.getInstance(project).findFile(vFile) as PhelFile

        val symbol = PsiTreeUtil.findChildrenOfType(phelFile, PhelSymbol::class.java)
            .first { it.text == symbolText }
        val reference = symbol.reference ?: error("symbol '$symbolText' has no reference")

        WriteCommandAction.runWriteCommandAction(project) {
            reference.handleElementRename(newName)
        }

        val renamed = PsiTreeUtil.findChildrenOfType(phelFile, PhelSymbol::class.java)
            .map { it.text }
        assertTrue(
            "expected a symbol named '$expected' after rename, got: $renamed",
            renamed.contains(expected),
        )
        assertFalse(
            "the original '$symbolText' should be gone after rename, got: $renamed",
            renamed.contains(symbolText),
        )
    }
}
