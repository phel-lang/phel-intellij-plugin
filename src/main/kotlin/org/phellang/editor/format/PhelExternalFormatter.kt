package org.phellang.editor.format

import com.intellij.formatting.service.AsyncDocumentFormattingService
import com.intellij.formatting.service.AsyncFormattingRequest
import com.intellij.formatting.service.FormattingService
import com.intellij.psi.PsiFile
import org.phellang.language.psi.files.PhelFile
import java.io.File
import java.util.EnumSet

class PhelExternalFormatter : AsyncDocumentFormattingService() {

    override fun getName(): String = "phel fmt"

    override fun getNotificationGroupId(): String = NOTIFICATION_GROUP_ID

    override fun getFeatures(): MutableSet<FormattingService.Feature> =
        EnumSet.noneOf(FormattingService.Feature::class.java)

    override fun canFormat(file: PsiFile): Boolean = file is PhelFile

    override fun createFormattingTask(request: AsyncFormattingRequest): FormattingTask? {
        val basePath = request.context.project.basePath
        if (basePath == null) {
            request.onError(NOTIFICATION_TITLE, "No project base path available; cannot run phel fmt")
            return null
        }

        val binary = PhelFormatterBinaryLocator.locate(basePath)
        if (binary == null) {
            request.onError(
                NOTIFICATION_TITLE,
                "Phel binary not found. Looked for ./bin/phel and ./vendor/bin/phel relative to project root."
            )
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
            try {
                workFile.writeText(request.documentText)

                val started = ProcessBuilder(binary.absolutePath, "fmt", workFile.absolutePath)
                    .directory(workingDir)
                    .redirectErrorStream(true)
                    .start()
                process = started

                val output = started.inputStream.bufferedReader().use { it.readText() }
                val exitCode = started.waitFor()

                if (exitCode != 0) {
                    request.onError(
                        NOTIFICATION_TITLE,
                        output.ifBlank { "phel fmt exited with code $exitCode" }
                    )
                    return
                }

                request.onTextReady(workFile.readText())
            } catch (e: Exception) {
                request.onError(NOTIFICATION_TITLE, e.message ?: e.javaClass.simpleName)
            } finally {
                workFile.delete()
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
    }
}
