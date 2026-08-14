package org.phellang.unit

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.io.File

/**
 * CHANGELOG.md is the single source of the draft release notes and the Marketplace "What's new"
 * (`patchPluginXml` renders the current version's section into `<change-notes>`), so a defect in it
 * ships to users.
 *
 * The 1.1.0 section carried ten entries twice: #262 reordered the Added block and left the
 * pre-reorder copies behind. Nothing caught it, and it stayed invisible until `patchChangelog` —
 * which deduplicates on rewrite — appeared to be "losing" entries at release time.
 *
 * A plain file scan, no platform fixture, mirroring ArchitectureBoundaryTest and
 * InspectionDescriptionsTest.
 */
class ChangelogIntegrityTest {

    private val changelog = File("CHANGELOG.md")

    /** One logical entry per bullet, continuation lines folded in, keyed by the version heading. */
    private fun entriesByVersion(): Map<String, List<String>> {
        val byVersion = linkedMapOf<String, MutableList<String>>()
        var version = "(preamble)"
        var current: StringBuilder? = null

        fun flush() {
            current?.let { byVersion.getOrPut(version) { mutableListOf() }.add(it.toString()) }
            current = null
        }

        for (line in changelog.readLines()) {
            when {
                line.startsWith("## ") -> {
                    flush()
                    version = line.removePrefix("## ").trim()
                }

                line.startsWith("- ") -> {
                    flush()
                    current = StringBuilder(line.removePrefix("- ").trim())
                }

                current != null && line.startsWith("  ") -> current!!.append(' ').append(line.trim())
                else -> flush()
            }
        }
        flush()
        return byVersion
    }

    @Test
    fun `no release repeats an entry`() {
        val duplicates = entriesByVersion().flatMap { (version, entries) ->
            entries.groupingBy { it }.eachCount()
                .filterValues { it > 1 }
                .map { (entry, count) -> "$version repeats x$count: ${entry.take(90)}" }
        }

        assertTrue(
            duplicates.isEmpty(),
            "CHANGELOG.md repeats entries within a release:\n${duplicates.joinToString("\n")}",
        )
    }

    @Test
    fun `the changelog parses into releases with entries`() {
        val byVersion = entriesByVersion().filterKeys { it.startsWith("[") }

        assertTrue(byVersion.isNotEmpty(), "expected version sections in CHANGELOG.md")
        // Unreleased is legitimately empty between releases; every published version is not.
        val empty = byVersion.filterKeys { !it.startsWith("[Unreleased]") }.filterValues { it.isEmpty() }
        assertTrue(empty.isEmpty(), "released versions with no entries: ${empty.keys}")
    }
}
