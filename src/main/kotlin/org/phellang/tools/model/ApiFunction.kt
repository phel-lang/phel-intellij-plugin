package org.phellang.tools.model

import com.google.gson.annotations.SerializedName

data class ApiFunction(
    val namespace: String,
    val name: String,
    val description: String,
    val doc: String,
    val signatures: List<String>,
    val githubUrl: String,
    val docUrl: String,
    val meta: ApiFunctionMeta
)

data class ApiFunctionMeta(
    val doc: String? = null,
    val example: String? = null,
    val deprecated: String? = null,
    @SerializedName("superseded-by") val supersededBy: String? = null,
    val macro: Boolean? = null,
    @SerializedName("min-arity") val minArity: Int? = null,
    val inline: Map<String, Any>? = null,
    @SerializedName("inline-arity") val inlineArity: Map<String, Any>? = null,
    @SerializedName("see-also") val seeAlso: Map<String, Any>? = null,
    @SerializedName("start-location") val startLocation: Map<String, Any>? = null,
    @SerializedName("end-location") val endLocation: Map<String, Any>? = null
)
