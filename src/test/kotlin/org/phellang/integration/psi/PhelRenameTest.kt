package org.phellang.integration.psi

import com.intellij.openapi.command.WriteCommandAction
import com.intellij.psi.PsiManager
import com.intellij.psi.util.PsiTreeUtil
import org.phellang.indexing.PhelProjectSymbolIndex
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

    fun testRenamingADefinitionWhoseNameCarriesATagRenamesTheName() {
        assertFullRename(
            source = "(ns app\\main)\n(defn ^map build [] {})\n(defn f [] (build))\n",
            symbolText = "build",
            newName = "make",
            expected = "(ns app\\main)\n(defn ^map make [] {})\n(defn f [] (make))\n",
        )
    }

    /** Renames the first occurrence of [symbolText], which in every source here is its definition. */
    private fun assertFullRename(source: String, symbolText: String, newName: String, expected: String) {
        val file = myFixture.configureByText("rename_full_${name}.phel", source) as PhelFile
        PhelProjectSymbolIndex.getInstance(project).refreshFileFromPsi(file)

        val definition = PsiTreeUtil.findChildrenOfType(file, PhelSymbol::class.java).first { it.text == symbolText }
        myFixture.renameElement(definition, newName)

        assertEquals(expected, file.text)
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
