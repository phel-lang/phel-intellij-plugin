package org.phellang.editor.format

import com.intellij.formatting.service.AsyncDocumentFormattingService
import com.intellij.formatting.service.AsyncFormattingRequest
import com.intellij.formatting.service.FormattingService
import com.intellij.psi.PsiFile
import org.phellang.core.cli.PhelCliLocator
import org.phellang.language.psi.files.PhelFile
import java.io.File
import java.util.EnumSet
import java.util.concurrent.TimeUnit

class PhelExternalFormatter : AsyncDocumentFormattingService() {

    override fun getName(): String = "phel fmt"

    override fun getNotificationGroupId(): String = NOTIFICATION_GROUP_ID

    override fun getFeatures(): MutableSet<FormattingService.Feature> =
        EnumSet.noneOf(FormattingService.Feature::class.java)

    /**
     * Only when the project actually has a `phel` binary.
     *
     * Claiming a file this service cannot format would shadow [PhelFormattingModelBuilder]: the
     * platform stops at the first formatting service that accepts the file, so Reformat Code would
     * report an error instead of falling back to the built-in formatter.
     */
    override fun canFormat(file: PsiFile): Boolean {
        if (file !is PhelFile) return false
        val basePath = file.project.basePath ?: return false
        return PhelCliLocator.locate(basePath) != null
    }

    override fun createFormattingTask(request: AsyncFormattingRequest): FormattingTask? {
        val basePath = request.context.project.basePath
        if (basePath == null) {
            request.onError(NOTIFICATION_TITLE, "No project base path available; cannot run phel fmt")
            return null
        }

        val binary = PhelCliLocator.locate(basePath)
        if (binary == null) {
            request.onError(NOTIFICATION_TITLE, PhelCliLocator.NOT_FOUND_MESSAGE)
            return null
        }

        return PhelFormattingTask(request, binary, File(basePath))
    }

    private class PhelFormattingTask(
        private val request: AsyncFormattingRequest,
        private val binary: File,
        private val workingDir: File,
    ) : FormattingTask {

        @Volatile
        private var process: Process? = null

        override fun run() {
            val workFile = File.createTempFile("phel-fmt-", ".phel")
            val outputFile = File.createTempFile("phel-fmt-out-", ".log")
            try {
                workFile.writeText(request.documentText)

                val started = PhelFormatterProcess.command(binary, workFile, workingDir, outputFile).start()
                process = started

                if (!started.waitFor(TIMEOUT_SECONDS, TimeUnit.SECONDS)) {
                    started.destroyForcibly()
                    request.onError(NOTIFICATION_TITLE, "phel fmt did not finish within $TIMEOUT_SECONDS seconds")
                    return
                }

                val output = outputFile.readText().trim()
                if (started.exitValue() != 0) {
                    request.onError(
                        NOTIFICATION_TITLE,
                        output.ifBlank { "phel fmt exited with code ${started.exitValue()}" }
                    )
                    return
                }

                request.onTextReady(workFile.readText())
            } catch (e: Exception) {
                request.onError(NOTIFICATION_TITLE, e.message ?: e.javaClass.simpleName)
            } finally {
                workFile.delete()
                outputFile.delete()
            }
        }

        override fun cancel(): Boolean {
            process?.destroyForcibly()
            return true
        }

        override fun isRunUnderProgress(): Boolean = true
    }

    private companion object {
        const val NOTIFICATION_GROUP_ID = "Phel"
        const val NOTIFICATION_TITLE = "Phel formatter"

        /** Generous for a formatter, but bounded: a hung process must not block the editor forever. */
        const val TIMEOUT_SECONDS = 30L
    }
}
