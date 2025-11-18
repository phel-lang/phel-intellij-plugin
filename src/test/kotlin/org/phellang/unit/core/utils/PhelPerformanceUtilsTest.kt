package org.phellang.unit.core.utils

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.openapi.vfs.VirtualFile
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.phellang.core.utils.PhelPerformanceUtils

class PhelPerformanceUtilsTest {

    @Test
    fun `MAX_PSI_TREE_DEPTH constant should be defined`() {
        assertEquals(50, PhelPerformanceUtils.MAX_PSI_TREE_DEPTH)
    }

    @Test
    fun `MAX_FILE_SIZE_KB constant should be defined`() {
        assertEquals(100, PhelPerformanceUtils.MAX_FILE_SIZE_KB)
    }

    @Test
    fun `shouldSkipExpensiveOperations should return false for normal element`() {
        val element = mock(PsiElement::class.java)
        val file = mock(PsiFile::class.java)
        val virtualFile = mock(VirtualFile::class.java)

        `when`(element.containingFile).thenReturn(file)
        `when`(file.virtualFile).thenReturn(virtualFile)
        `when`(virtualFile.length).thenReturn(10L * 1024) // 10 KB
        `when`(element.parent).thenReturn(null)

        val result = PhelPerformanceUtils.shouldSkipExpensiveOperations(element)

        assertFalse(result, "Should not skip operations for normal element")
    }

    @Test
    fun `shouldSkipExpensiveOperations should return true for large file`() {
        val element = mock(PsiElement::class.java)
        val file = mock(PsiFile::class.java)
        val virtualFile = mock(VirtualFile::class.java)

        `when`(element.containingFile).thenReturn(file)
        `when`(file.virtualFile).thenReturn(virtualFile)
        `when`(virtualFile.length).thenReturn(200L * 1024) // 200 KB > MAX_FILE_SIZE_KB

        val result = PhelPerformanceUtils.shouldSkipExpensiveOperations(element)

        assertTrue(result, "Should skip operations for large file")
    }

    @Test
    fun `shouldSkipExpensiveOperations should return true for deep PSI tree`() {
        val elements = mutableListOf<PsiElement>()

        // Create a chain of 60 elements (> MAX_PSI_TREE_DEPTH)
        var current: PsiElement? = null
        (0 until 60).forEach { _ ->
            val element = mock(PsiElement::class.java)
            `when`(element.parent).thenReturn(current)
            elements.add(element)
            current = element
        }

        val deepElement = elements.last()
        val file = mock(PsiFile::class.java)
        val virtualFile = mock(VirtualFile::class.java)

        `when`(deepElement.containingFile).thenReturn(file)
        `when`(file.virtualFile).thenReturn(virtualFile)
        `when`(virtualFile.length).thenReturn(10L * 1024)

        val result = PhelPerformanceUtils.shouldSkipExpensiveOperations(deepElement)

        assertTrue(result, "Should skip operations for deep PSI tree")
    }

    @Test
    fun `shouldSkipExpensiveOperations should handle null file gracefully`() {
        val element = mock(PsiElement::class.java)
        `when`(element.containingFile).thenReturn(null)

        val result = PhelPerformanceUtils.shouldSkipExpensiveOperations(element)

        assertFalse(result)
    }

    @Test
    fun `shouldSkipExpensiveOperations should handle null virtual file gracefully`() {
        val element = mock(PsiElement::class.java)
        val file = mock(PsiFile::class.java)

        `when`(element.containingFile).thenReturn(file)
        `when`(file.virtualFile).thenReturn(null)
        `when`(element.parent).thenReturn(null)

        val result = PhelPerformanceUtils.shouldSkipExpensiveOperations(element)

        assertFalse(result)
    }

    @Test
    fun `shouldSkipExpensiveOperations should handle file at exact limit`() {
        val element = mock(PsiElement::class.java)
        val file = mock(PsiFile::class.java)
        val virtualFile = mock(VirtualFile::class.java)

        `when`(element.containingFile).thenReturn(file)
        `when`(file.virtualFile).thenReturn(virtualFile)
        `when`(virtualFile.length).thenReturn(100L * 1024) // Exactly MAX_FILE_SIZE_KB
        `when`(element.parent).thenReturn(null)

        val result = PhelPerformanceUtils.shouldSkipExpensiveOperations(element)

        assertFalse(result, "Should not skip at exact limit")
    }

    @Test
    fun `shouldSkipExpensiveOperations should handle file just over limit`() {
        val element = mock(PsiElement::class.java)
        val file = mock(PsiFile::class.java)
        val virtualFile = mock(VirtualFile::class.java)

        `when`(element.containingFile).thenReturn(file)
        `when`(file.virtualFile).thenReturn(virtualFile)
        `when`(virtualFile.length).thenReturn((100L * 1024) + 1024) // 101 KB, over limit
        `when`(element.parent).thenReturn(null)

        val result = PhelPerformanceUtils.shouldSkipExpensiveOperations(element)

        assertTrue(result, "Should skip just over limit")
    }

    @Test
    fun `shouldSkipExpensiveOperations should handle PSI tree at exact depth limit`() {
        val elements = mutableListOf<PsiElement>()

        // Create exactly 50 elements (MAX_PSI_TREE_DEPTH)
        var current: PsiElement? = null
        (0 until 50).forEach { _ ->
            val element = mock(PsiElement::class.java)
            `when`(element.parent).thenReturn(current)
            elements.add(element)
            current = element
        }

        val deepElement = elements.last()
        val file = mock(PsiFile::class.java)
        val virtualFile = mock(VirtualFile::class.java)

        `when`(deepElement.containingFile).thenReturn(file)
        `when`(file.virtualFile).thenReturn(virtualFile)
        `when`(virtualFile.length).thenReturn(10L * 1024)

        val result = PhelPerformanceUtils.shouldSkipExpensiveOperations(deepElement)

        assertTrue(result, "Should skip at exact depth limit")
    }

    @Test
    fun `shouldSkipExpensiveOperations should be thread-safe`() {
        val element = mock(PsiElement::class.java)
        val file = mock(PsiFile::class.java)
        val virtualFile = mock(VirtualFile::class.java)

        `when`(element.containingFile).thenReturn(file)
        `when`(file.virtualFile).thenReturn(virtualFile)
        `when`(virtualFile.length).thenReturn(10L * 1024)
        `when`(element.parent).thenReturn(null)

        val results = mutableListOf<Boolean>()
        val threads = mutableListOf<Thread>()

        repeat(10) {
            val thread = Thread {
                val result = PhelPerformanceUtils.shouldSkipExpensiveOperations(element)
                synchronized(results) {
                    results.add(result)
                }
            }
            threads.add(thread)
            thread.start()
        }

        threads.forEach { it.join() }

        assertEquals(10, results.size)
        assertTrue(results.all { !it })
    }

    @Test
    fun `shouldSkipExpensiveOperations should handle exception in operation`() {
        val element = mock(PsiElement::class.java)
        `when`(element.containingFile).thenThrow(RuntimeException("Test error"))

        val result = PhelPerformanceUtils.shouldSkipExpensiveOperations(element)

        assertTrue(result, "Should skip on error to be safe")
    }

    @Test
    fun `shouldSkipExpensiveOperations should be callable multiple times`() {
        val element = mock(PsiElement::class.java)
        val file = mock(PsiFile::class.java)
        val virtualFile = mock(VirtualFile::class.java)

        `when`(element.containingFile).thenReturn(file)
        `when`(file.virtualFile).thenReturn(virtualFile)
        `when`(virtualFile.length).thenReturn(10L * 1024)
        `when`(element.parent).thenReturn(null)

        repeat(100) {
            val result = PhelPerformanceUtils.shouldSkipExpensiveOperations(element)
            assertFalse(result)
        }
    }
}
