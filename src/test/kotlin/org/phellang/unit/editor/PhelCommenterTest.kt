package org.phellang.unit.editor

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.phellang.editor.PhelCommenter

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
    fun `getBlockCommentPrefix should return hash pipe`() {
        val prefix = commenter.blockCommentPrefix

        Assertions.assertEquals("#|", prefix)
        Assertions.assertNotNull(prefix)
        Assertions.assertTrue(prefix!!.isNotEmpty())
    }

    @Test
    fun `getBlockCommentSuffix should return pipe hash`() {
        val suffix = commenter.blockCommentSuffix

        Assertions.assertEquals("|#", suffix)
        Assertions.assertNotNull(suffix)
        Assertions.assertTrue(suffix!!.isNotEmpty())
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
    fun `block comment prefix and suffix should be balanced`() {
        val prefix = commenter.blockCommentPrefix
        val suffix = commenter.blockCommentSuffix

        Assertions.assertNotNull(prefix)
        Assertions.assertNotNull(suffix)
        Assertions.assertEquals(2, prefix!!.length)
        Assertions.assertEquals(2, suffix!!.length)

        // Should be mirror images
        Assertions.assertEquals("#", prefix.take(1))
        Assertions.assertEquals("#", suffix[1].toString())
        Assertions.assertEquals("|", prefix[1].toString())
        Assertions.assertEquals("|", suffix.take(1))
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
        // Test that multiple calls return the same values
        repeat(5) {
            Assertions.assertEquals(";", commenter.lineCommentPrefix)
            Assertions.assertEquals("#|", commenter.blockCommentPrefix)
            Assertions.assertEquals("|#", commenter.blockCommentSuffix)
            Assertions.assertNull(commenter.commentedBlockCommentPrefix)
            Assertions.assertNull(commenter.commentedBlockCommentSuffix)
        }
    }

    @Test
    fun `comment prefixes and suffixes should be valid Phel syntax`() {
        val linePrefix = commenter.lineCommentPrefix
        val blockPrefix = commenter.blockCommentPrefix
        val blockSuffix = commenter.blockCommentSuffix

        Assertions.assertTrue(linePrefix.startsWith(";"))

        Assertions.assertEquals("#|", blockPrefix)
        Assertions.assertEquals("|#", blockSuffix)

        Assertions.assertFalse(linePrefix.contains(" "))
        Assertions.assertFalse(blockPrefix!!.contains(" "))
        Assertions.assertFalse(blockSuffix!!.contains(" "))
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
        val linePrefix = commenter.lineCommentPrefix
        val blockPrefix = commenter.blockCommentPrefix
        val blockSuffix = commenter.blockCommentSuffix

        Assertions.assertFalse(blockPrefix!!.startsWith(linePrefix))
        Assertions.assertFalse(blockSuffix!!.startsWith(linePrefix))

        Assertions.assertFalse(blockPrefix.contains(linePrefix))
        Assertions.assertFalse(blockSuffix.contains(linePrefix))
    }

    @Test
    fun `commenter should follow Phel language conventions`() {
        Assertions.assertEquals(";", commenter.lineCommentPrefix) // Standard Lisp line comment
        Assertions.assertEquals("#|", commenter.blockCommentPrefix) // Common Lisp block comment start
        Assertions.assertEquals("|#", commenter.blockCommentSuffix) // Common Lisp block comment end

        // Phel doesn't support commented block comments (nested block comments)
        Assertions.assertNull(commenter.commentedBlockCommentPrefix)
        Assertions.assertNull(commenter.commentedBlockCommentSuffix)
    }

    @Test
    fun `comment syntax should be suitable for IDE integration`() {
        val linePrefix = commenter.lineCommentPrefix
        val blockPrefix = commenter.blockCommentPrefix
        val blockSuffix = commenter.blockCommentSuffix

        Assertions.assertTrue(linePrefix.isNotEmpty())
        Assertions.assertTrue(blockPrefix!!.isNotEmpty())
        Assertions.assertTrue(blockSuffix!!.isNotEmpty())

        Assertions.assertTrue(linePrefix.length <= 3)
        Assertions.assertTrue(blockPrefix.length <= 3)
        Assertions.assertTrue(blockSuffix.length <= 3)

        // Should not contain newlines or control characters
        Assertions.assertFalse(linePrefix.contains('\n'))
        Assertions.assertFalse(blockPrefix.contains('\n'))
        Assertions.assertFalse(blockSuffix.contains('\n'))
    }
}
