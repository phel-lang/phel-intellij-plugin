package org.phellang.core.cli

import com.intellij.util.EnvironmentUtil

/**
 * The environment a `phel` invocation needs to actually run.
 *
 * `vendor/bin/phel` is a PHP script starting `#!/usr/bin/env php`, so running it needs `php` on the
 * `PATH` of the *child* process.
 *
 * [ProcessBuilder] inherits the IDE's environment, and an IDE launched from Dock, Spotlight or
 * Finder on macOS never sees the login shell's `PATH` — it gets `/usr/bin:/bin:/usr/sbin:/sbin`. A
 * `php` installed by Homebrew, Herd, asdf or mise is on none of those, so the shebang failed with
 * `env: php: No such file or directory` even though the binary itself was found and executable.
 *
 * [EnvironmentUtil.getEnvironmentMap] is the platform's answer to exactly this: it reads the
 * environment from a login shell once and caches it. Everywhere else — Linux, Windows, or the IDE
 * started from a terminal — it returns the inherited environment, so this is a no-op there.
 */
object PhelCliEnvironment {

    /** The login shell's environment, as a map suitable for any process-launching API. */
    fun loginShellEnvironment(): Map<String, String> = EnvironmentUtil.getEnvironmentMap()

    fun applyTo(builder: ProcessBuilder): ProcessBuilder {
        builder.environment().putAll(loginShellEnvironment())
        return builder
    }
}
