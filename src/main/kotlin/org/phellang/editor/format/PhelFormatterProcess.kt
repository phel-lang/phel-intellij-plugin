package org.phellang.editor.format

import com.intellij.util.EnvironmentUtil
import java.io.File

/** Builds the `phel fmt` invocation, with the environment it needs to actually run. */
internal object PhelFormatterProcess {

    /**
     * `vendor/bin/phel` is a PHP script starting `#!/usr/bin/env php`, so running it needs `php` on
     * the `PATH` of the *child* process.
     *
     * [ProcessBuilder] inherits the IDE's environment, and an IDE launched from Dock, Spotlight or
     * Finder on macOS never sees the login shell's `PATH` — it gets `/usr/bin:/bin:/usr/sbin:/sbin`.
     * A `php` installed by Homebrew, Herd, asdf or mise is on none of those, so the shebang failed
     * with `env: php: No such file or directory` even though the binary itself was found and
     * executable.
     *
     * [EnvironmentUtil.getEnvironmentMap] is the platform's answer to exactly this: it reads the
     * environment from a login shell once and caches it. Everywhere else — Linux, Windows, or the
     * IDE started from a terminal — it returns the inherited environment, so this is a no-op there.
     */
    fun command(binary: File, target: File, workingDir: File, output: File): ProcessBuilder {
        val builder = ProcessBuilder(binary.absolutePath, "fmt", target.absolutePath)
            .directory(workingDir)
            .redirectErrorStream(true)
            // To a file rather than a pipe: nothing reads the stream until the process is waited on,
            // and a full pipe buffer would deadlock a chatty run against the timeout below.
            .redirectOutput(output)

        builder.environment().putAll(EnvironmentUtil.getEnvironmentMap())

        return builder
    }
}
