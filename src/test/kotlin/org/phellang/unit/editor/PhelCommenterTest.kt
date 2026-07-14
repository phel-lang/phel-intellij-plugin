package org.phellang.unit.editor

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.phellang.editor.commenting.PhelCommenter

class PhelCommenterTest {

    private lateinit var commenter: PhelCommenter

    @BeforeEach
    fun setUp() {
        commenter = PhelCommenter()
    }

    @Test
    fun `getLineCommentPrefix should return semicolon`() {
        val prefix = commenter.lineCommentPrefix

        Assertions.assertEquals(";", prefix)
        Assertions.assertNotNull(prefix)
        Assertions.assertTrue(prefix.isNotEmpty())
    }

    @Test
    fun `getBlockCommentPrefix should return null since block comments are deprecated`() {
        val prefix = commenter.blockCommentPrefix

        Assertions.assertNull(prefix)
    }

    @Test
    fun `getBlockCommentSuffix should return null since block comments are deprecated`() {
        val suffix = commenter.blockCommentSuffix

        Assertions.assertNull(suffix)
    }

    @Test
    fun `getCommentedBlockCommentPrefix should return null`() {
        val prefix = commenter.commentedBlockCommentPrefix

        Assertions.assertNull(prefix)
    }

    @Test
    fun `getCommentedBlockCommentSuffix should return null`() {
        val suffix = commenter.commentedBlockCommentSuffix

        Assertions.assertNull(suffix)
    }

    @Test
    fun `line comment prefix should be single character`() {
        val prefix = commenter.lineCommentPrefix

        Assertions.assertEquals(1, prefix.length)
        Assertions.assertEquals(';', prefix[0])
    }

    @Test
    fun `commenter should be consistent across multiple instances`() {
        val commenter1 = PhelCommenter()
        val commenter2 = PhelCommenter()

        Assertions.assertEquals(commenter1.lineCommentPrefix, commenter2.lineCommentPrefix)
        Assertions.assertEquals(commenter1.blockCommentPrefix, commenter2.blockCommentPrefix)
        Assertions.assertEquals(commenter1.blockCommentSuffix, commenter2.blockCommentSuffix)
        Assertions.assertEquals(commenter1.commentedBlockCommentPrefix, commenter2.commentedBlockCommentPrefix)
        Assertions.assertEquals(commenter1.commentedBlockCommentSuffix, commenter2.commentedBlockCommentSuffix)
    }

    @Test
    fun `commenter should be consistent across multiple calls`() {
        repeat(5) {
            Assertions.assertEquals(";", commenter.lineCommentPrefix)
            Assertions.assertNull(commenter.blockCommentPrefix)
            Assertions.assertNull(commenter.blockCommentSuffix)
            Assertions.assertNull(commenter.commentedBlockCommentPrefix)
            Assertions.assertNull(commenter.commentedBlockCommentSuffix)
        }
    }

    @Test
    fun `comment prefixes and suffixes should be valid Phel syntax`() {
        val linePrefix = commenter.lineCommentPrefix

        Assertions.assertTrue(linePrefix.startsWith(";"))
        Assertions.assertNull(commenter.blockCommentPrefix)
        Assertions.assertNull(commenter.blockCommentSuffix)

        Assertions.assertFalse(linePrefix.contains(" "))
    }

    @Test
    fun `commenter should implement Commenter interface correctly`() {
        Assertions.assertDoesNotThrow { commenter.lineCommentPrefix }
        Assertions.assertDoesNotThrow { commenter.blockCommentPrefix }
        Assertions.assertDoesNotThrow { commenter.blockCommentSuffix }
        Assertions.assertDoesNotThrow { commenter.commentedBlockCommentPrefix }
        Assertions.assertDoesNotThrow { commenter.commentedBlockCommentSuffix }
    }

    @Test
    fun `block comment delimiters should not conflict with line comments`() {
        // Block comments are deprecated, so both should be null
        Assertions.assertNull(commenter.blockCommentPrefix)
        Assertions.assertNull(commenter.blockCommentSuffix)
    }

    @Test
    fun `commenter should follow Phel language conventions`() {
        Assertions.assertEquals(";", commenter.lineCommentPrefix) // Standard Lisp line comment
        // Phel does not support block comments
        Assertions.assertNull(commenter.blockCommentPrefix)
        Assertions.assertNull(commenter.blockCommentSuffix)

        // Phel doesn't support commented block comments (nested block comments)
        Assertions.assertNull(commenter.commentedBlockCommentPrefix)
        Assertions.assertNull(commenter.commentedBlockCommentSuffix)
    }

    @Test
    fun `comment syntax should be suitable for IDE integration`() {
        val linePrefix = commenter.lineCommentPrefix

        Assertions.assertTrue(linePrefix.isNotEmpty())
        Assertions.assertTrue(linePrefix.length <= 3)
        Assertions.assertFalse(linePrefix.contains('\n'))
    }
}
