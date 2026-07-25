package org.phellang.unit.core

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import org.phellang.core.cli.PhelCliLocator
import java.nio.file.Path
import kotlin.io.path.createDirectories
import kotlin.io.path.createFile

/**
 * `PATH` entries are passed explicitly throughout. The production overload reads them from the login
 * shell, which would otherwise make every assertion here depend on whether the machine running the
 * suite happens to have `phel` installed.
 */
class PhelCliLocatorTest {

    private val noPath = emptyList<String>()

    @Test
    fun `prefers bin phel over vendor bin phel`(@TempDir root: Path) {
        val bin = createExecutable(root, "bin/phel")
        createExecutable(root, "vendor/bin/phel")

        val located = PhelCliLocator.locate(root.toString(), noPath)

        assertEquals(bin.toFile().absolutePath, located?.absolutePath)
    }

    @Test
    fun `falls back to vendor bin phel when bin phel missing`(@TempDir root: Path) {
        val vendor = createExecutable(root, "vendor/bin/phel")

        val located = PhelCliLocator.locate(root.toString(), noPath)

        assertEquals(vendor.toFile().absolutePath, located?.absolutePath)
    }

    @Test
    fun `returns null when no candidate exists`(@TempDir root: Path) {
        assertNull(PhelCliLocator.locate(root.toString(), noPath))
    }

    @Test
    fun `ignores non-executable phel files`(@TempDir root: Path) {
        val path = root.resolve("bin/phel")
        path.parent.createDirectories()
        path.createFile()
        path.toFile().setExecutable(false)

        assertNull(PhelCliLocator.locate(root.toString(), noPath))
    }

    @Test
    fun `falls back to PATH when the project has no binary`(@TempDir root: Path, @TempDir elsewhere: Path) {
        val onPath = createExecutable(elsewhere, "phel")

        val located = PhelCliLocator.locate(root.toString(), listOf(elsewhere.toString()))

        assertEquals(onPath.toFile().absolutePath, located?.absolutePath)
    }

    /** A project pinning a version through Composer must win over whatever is installed globally. */
    @Test
    fun `prefers the project binary over one on PATH`(@TempDir root: Path, @TempDir elsewhere: Path) {
        val vendor = createExecutable(root, "vendor/bin/phel")
        createExecutable(elsewhere, "phel")

        val located = PhelCliLocator.locate(root.toString(), listOf(elsewhere.toString()))

        assertEquals(vendor.toFile().absolutePath, located?.absolutePath)
    }

    @Test
    fun `takes the first PATH entry holding an executable phel`(
        @TempDir root: Path,
        @TempDir first: Path,
        @TempDir second: Path,
    ) {
        val found = createExecutable(second, "phel")

        val located = PhelCliLocator.locate(root.toString(), listOf(first.toString(), second.toString()))

        assertEquals(found.toFile().absolutePath, located?.absolutePath)
    }

    @Test
    fun `ignores a non-executable phel on PATH`(@TempDir root: Path, @TempDir elsewhere: Path) {
        val path = elsewhere.resolve("phel")
        path.createFile()
        path.toFile().setExecutable(false)

        assertNull(PhelCliLocator.locate(root.toString(), listOf(elsewhere.toString())))
    }

    private fun createExecutable(root: Path, relative: String): Path {
        val path = root.resolve(relative)
        path.parent.createDirectories()
        path.createFile()
        path.toFile().setExecutable(true)
        return path
    }
}
