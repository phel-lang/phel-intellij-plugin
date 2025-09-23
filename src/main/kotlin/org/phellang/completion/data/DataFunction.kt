package org.phellang.completion.data

import org.phellang.completion.infrastructure.PhelCompletionPriority

data class DataFunction(
    val name: String,
    val signature: String,
    val priority: PhelCompletionPriority = PhelCompletionPriority.CORE_FUNCTIONS,
    val namespace: String,
    val description: String,
    val descriptionHtml: String
)
