package org.phellang.unit.core

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import org.phellang.core.cli.PhelCliLocator
import java.nio.file.Path
import kotlin.io.path.createDirectories
import kotlin.io.path.createFile

class PhelCliLocatorTest {

    @Test
    fun `prefers bin phel over vendor bin phel`(@TempDir root: Path) {
        val bin = createExecutable(root, "bin/phel")
        createExecutable(root, "vendor/bin/phel")

        val located = PhelCliLocator.locate(root.toString())

        assertEquals(bin.toFile().absolutePath, located?.absolutePath)
    }

    @Test
    fun `falls back to vendor bin phel when bin phel missing`(@TempDir root: Path) {
        val vendor = createExecutable(root, "vendor/bin/phel")

        val located = PhelCliLocator.locate(root.toString())

        assertEquals(vendor.toFile().absolutePath, located?.absolutePath)
    }

    @Test
    fun `returns null when no candidate exists`(@TempDir root: Path) {
        assertNull(PhelCliLocator.locate(root.toString()))
    }

    @Test
    fun `ignores non-executable phel files`(@TempDir root: Path) {
        val path = root.resolve("bin/phel")
        path.parent.createDirectories()
        path.createFile()
        path.toFile().setExecutable(false)

        assertNull(PhelCliLocator.locate(root.toString()))
    }

    private fun createExecutable(root: Path, relative: String): Path {
        val path = root.resolve(relative)
        path.parent.createDirectories()
        path.createFile()
        path.toFile().setExecutable(true)
        return path
    }
}
