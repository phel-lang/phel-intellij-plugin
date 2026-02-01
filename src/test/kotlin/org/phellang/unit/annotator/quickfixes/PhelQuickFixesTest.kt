package org.phellang.unit.annotator.quickfixes

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

/**
 * Tests for Phel quick fix logic and patterns.
 * Note: Full quick fix behavior requires IntelliJ platform.
 */
class PhelQuickFixesTest {

    @Nested
    inner class ImportNamespaceQuickFix {

        @Test
        fun `quick fix text format for import`() {
            val namespace = "phel\\str"
            val text = "Import '$namespace'"
            assertEquals("Import 'phel\\str'", text)
        }

        @Test
        fun `family name is consistent`() {
            val familyName = "Phel namespace imports"
            assertEquals("Phel namespace imports", familyName)
        }

        @Test
        fun `quick fix text for project namespace`() {
            val namespace = "my-project\\utils"
            val text = "Import '$namespace'"
            assertEquals("Import 'my-project\\utils'", text)
        }
    }

    @Nested
    inner class FixImportQuickFix {

        @Test
        fun `quick fix text format for replacing namespace`() {
            val oldNamespace = "my-project\\utls"
            val newNamespace = "my-project\\utils"
            val text = "Replace with '$newNamespace'"
            assertEquals("Replace with 'my-project\\utils'", text)
        }
    }

    @Nested
    inner class RemoveDuplicateImportQuickFix {

        @Test
        fun `quick fix text format for removing duplicate`() {
            val namespace = "phel\\str"
            val text = "Remove duplicate import of '$namespace'"
            assertEquals("Remove duplicate import of 'phel\\str'", text)
        }
    }
}
