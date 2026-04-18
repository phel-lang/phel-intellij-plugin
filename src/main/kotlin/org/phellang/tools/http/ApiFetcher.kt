package org.phellang.tools.http

import com.google.gson.*
import com.google.gson.reflect.TypeToken
import org.phellang.tools.model.ApiFunction
import org.phellang.tools.model.ApiFunctionMeta
import java.lang.reflect.Type
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.time.Duration

class ApiFetcher {

    companion object {
        private const val API_URL = "https://phel-lang.org/api.json"
        private val TIMEOUT = Duration.ofSeconds(30)
    }

    private val httpClient = HttpClient.newBuilder().connectTimeout(TIMEOUT).build()

    private val gson: Gson = GsonBuilder()
        .registerTypeAdapter(ApiFunctionMeta::class.java, MetaDeserializer())
        .create()

    fun fetchApiFunctions(): List<ApiFunction> {
        println("Fetching Phel API from $API_URL...")

        val request =
            HttpRequest.newBuilder().uri(URI.create(API_URL)).timeout(TIMEOUT).header("Accept", "application/json")
                .header("User-Agent", "intellij-phel-support/api-generator").GET().build()

        val response = httpClient.send(request, HttpResponse.BodyHandlers.ofString())

        if (response.statusCode() != 200) {
            throw RuntimeException("Failed to fetch API: HTTP ${response.statusCode()}")
        }

        val json = response.body()
        println("Received ${json.length} bytes")

        val typeToken = object : TypeToken<List<ApiFunction>>() {}.type
        val functions: List<ApiFunction> = gson.fromJson(json, typeToken)

        println("Parsed ${functions.size} functions")
        return functions
    }

    private class MetaDeserializer : JsonDeserializer<ApiFunctionMeta?> {
        private val defaultGson = Gson()

        override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): ApiFunctionMeta? {
            if (!json.isJsonObject) {
                // meta is sometimes an array (e.g., empty PHP array []) — skip it
                return null
            }
            return defaultGson.fromJson(json, ApiFunctionMeta::class.java)
        }
    }
}
