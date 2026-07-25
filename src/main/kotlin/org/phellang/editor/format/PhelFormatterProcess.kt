package org.phellang.editor.format

import org.phellang.core.cli.PhelCliEnvironment
import java.io.File

/** Builds the `phel fmt` invocation, with the environment it needs to actually run. */
internal object PhelFormatterProcess {

    fun command(binary: File, target: File, workingDir: File, output: File): ProcessBuilder {
        val builder = ProcessBuilder(binary.absolutePath, "fmt", target.absolutePath)
            .directory(workingDir)
            .redirectErrorStream(true)
            // To a file rather than a pipe: nothing reads the stream until the process is waited on,
            // and a full pipe buffer would deadlock a chatty run against the timeout below.
            .redirectOutput(output)

        return PhelCliEnvironment.applyTo(builder)
    }
}
