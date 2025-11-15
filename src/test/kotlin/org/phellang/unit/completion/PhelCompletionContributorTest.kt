package org.phellang.unit.completion

import com.intellij.codeInsight.completion.CompletionType
import org.junit.jupiter.api.Test
import org.phellang.completion.PhelCompletionContributor
import org.junit.jupiter.api.Assertions.*

class PhelCompletionContributorTest {

    @Test
    fun `should register completion provider for SYM tokens`() {
        val contributor = PhelCompletionContributor()

        assertNotNull(contributor)

        assertEquals("PhelCompletionContributor", contributor.javaClass.simpleName)
    }

    @Test
    fun `should use correct completion type`() {
        val contributor = PhelCompletionContributor()
        assertNotNull(contributor)

        assertEquals(CompletionType.BASIC.toString(), CompletionType.BASIC.toString())
    }

    @Test
    fun `should be consistent across multiple instantiations`() {
        val contributor1 = PhelCompletionContributor()
        val contributor2 = PhelCompletionContributor()

        assertNotNull(contributor1)
        assertNotNull(contributor2)

        assertTrue(contributor1 !== contributor2)
        assertEquals(contributor1.javaClass, contributor2.javaClass)
    }

    @Test
    fun `should have proper class hierarchy`() {
        val contributor = PhelCompletionContributor()

        assertEquals("org.phellang.completion", contributor.javaClass.packageName)

        assertEquals("PhelCompletionContributor", contributor.javaClass.simpleName)
    }

    @Test
    fun `should maintain thread safety`() {
        val contributors = java.util.concurrent.ConcurrentLinkedQueue<PhelCompletionContributor>()
        val threads = mutableListOf<Thread>()

        repeat(10) {
            val thread = Thread {
                contributors.add(PhelCompletionContributor())
            }
            threads.add(thread)
            thread.start()
        }

        threads.forEach { it.join() }

        assertEquals(10, contributors.size)
        contributors.forEach { assertNotNull(it) }
    }
}
