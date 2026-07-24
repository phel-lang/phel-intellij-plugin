package org.phellang.tools.http

import com.google.gson.JsonParser
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.time.Duration
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

/**
 * Fetches native PHP function docs from the official php/doc-en repository.
 *
 * Resolves `master` to a commit SHA once so the whole snapshot is internally consistent, lists the
 * function XMLs for the requested extensions from the git tree (one API call), then downloads each
 * raw XML in parallel. The DocBook content lives at `reference/<extension>/functions/<name>.xml`.
 */
class PhpDocFetcher(private val coreExtensions: Set<String>) {

    data class FunctionXml(val extension: String, val xml: String)

    data class Snapshot(val ref: String, val functions: List<FunctionXml>)

    private val http = HttpClient.newBuilder().connectTimeout(TIMEOUT).build()

    fun fetchCoreFunctions(): Snapshot {
        val ref = resolveMasterSha()
        println("Pinned php/doc-en at $ref")

        val paths = listFunctionPaths(ref)
        println("Found ${paths.size} function XMLs across ${coreExtensions.size} core extensions")

        val functions = downloadInParallel(ref, paths)
        println("Downloaded ${functions.size} function XMLs")
        return Snapshot(ref, functions)
    }

    private fun resolveMasterSha(): String {
        val body = getString("https://api.github.com/repos/$REPO/branches/master")
        return JsonParser.parseString(body).asJsonObject
            .getAsJsonObject("commit").get("sha").asString
    }

    /** Every `reference/<ext>/functions/<name>.xml` for `ext` in [coreExtensions], as extension→path. */
    private fun listFunctionPaths(ref: String): List<Pair<String, String>> {
        val body = getString("https://api.github.com/repos/$REPO/git/trees/$ref?recursive=1")
        val tree = JsonParser.parseString(body).asJsonObject
        require(!tree.get("truncated").asBoolean) { "doc-en git tree was truncated; cannot enumerate reliably" }

        val pathRegex = Regex("^reference/([^/]+)/functions/[^/]+\\.xml$")
        return tree.getAsJsonArray("tree").mapNotNull { element ->
            val path = element.asJsonObject.get("path").asString
            val extension = pathRegex.matchEntire(path)?.groupValues?.get(1) ?: return@mapNotNull null
            if (extension in coreExtensions) extension to path else null
        }
    }

    private fun downloadInParallel(ref: String, paths: List<Pair<String, String>>): List<FunctionXml> {
        val pool = Executors.newFixedThreadPool(THREADS)
        try {
            val futures = paths.map { (extension, path) ->
                pool.submit<FunctionXml?> {
                    runCatching { FunctionXml(extension, getString("https://raw.githubusercontent.com/$REPO/$ref/$path")) }
                        .onFailure { System.err.println("  skipped $path: ${it.message}") }
                        .getOrNull()
                }
            }
            return futures.mapNotNull { it.get() }
        } finally {
            pool.shutdown()
            pool.awaitTermination(1, TimeUnit.MINUTES)
        }
    }

    private fun getString(url: String): String {
        val request = HttpRequest.newBuilder().uri(URI.create(url)).timeout(TIMEOUT)
            .header("User-Agent", "intellij-phel-support/php-doc-generator").GET().build()
        val response = http.send(request, HttpResponse.BodyHandlers.ofString())
        if (response.statusCode() != 200) {
            throw RuntimeException("HTTP ${response.statusCode()} for $url")
        }
        return response.body()
    }

    companion object {
        private const val REPO = "php/doc-en"
        private const val THREADS = 16
        private val TIMEOUT = Duration.ofSeconds(30)
    }
}
