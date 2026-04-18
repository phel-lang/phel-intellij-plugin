package org.phellang.completion.data

import org.phellang.completion.infrastructure.PhelCompletionPriority

internal fun registerAiFunctions(): List<PhelFunction> = listOf(
    PhelFunction(
        namespace = "ai",
        name = "ai/build-index",
        signature = "(build-index texts & [opts])",
        completion = CompletionInfo(
            tailText = "Builds a searchable index by embedding a collection of texts",
            priority = PhelCompletionPriority.AI_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Builds a searchable index by embedding a collection of texts.",
            example = "(build-index [\"hello\" \"world\"])",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/ai.phel#L492",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "ai",
        name = "ai/chat",
        signature = "(chat messages & [{:system system-prompt, :model model, :max-tokens max-tokens}])",
        completion = CompletionInfo(
            tailText = "Sends a chat completion request",
            priority = PhelCompletionPriority.AI_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Sends a chat completion request. Returns the assistant's text response.",
            example = "(chat [{:role \"user\" :content \"Hello!\"}])",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/ai.phel#L148",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "ai",
        name = "ai/chat-with-history",
        signature = "(chat-with-history history user-message & [opts])",
        completion = CompletionInfo(
            tailText = "Continues a conversation",
            priority = PhelCompletionPriority.AI_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Continues a conversation. Returns updated message history.",
            example = "(chat-with-history [{:role \"user\" :content \"Hi\"}\n                              {:role \"assistant\" :content \"Hello!\"}]\n                             \"How are you?\")",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/ai.phel#L188",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "ai",
        name = "ai/chat-with-tools",
        signature = "(chat-with-tools messages tools & [opts])",
        completion = CompletionInfo(
            tailText = "Chat with tool definitions",
            priority = PhelCompletionPriority.AI_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Chat with tool definitions. Returns full response with tool calls.",
            example = "(chat-with-tools [{:role \"user\" :content \"What's the weather?\"}]\n                             [(tool \"get-weather\" \"Gets weather\" {:city {:type \"string\"}})])",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/ai.phel#L329",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "ai",
        name = "ai/complete",
        signature = "(complete prompt & [opts])",
        completion = CompletionInfo(
            tailText = "Sends a text prompt to the AI",
            priority = PhelCompletionPriority.AI_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Sends a text prompt to the AI. Returns the response string.",
            example = "(complete \"Explain monads in one sentence\")",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/ai.phel#L177",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "ai",
        name = "ai/config",
        signature = "",
        completion = CompletionInfo(
            tailText = "Current AI configuration atom",
            priority = PhelCompletionPriority.AI_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Current AI configuration atom. Use <code>configure</code> to update.
""",
            example = "@ai/config",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/ai.phel#L17",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "ai",
        name = "ai/configure",
        signature = "(configure opts)",
        completion = CompletionInfo(
            tailText = "Merges options into the AI configuration",
            priority = PhelCompletionPriority.AI_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Merges options into the AI configuration.",
            example = "(configure {:api-key \"sk-ant-...\" :model \"claude-sonnet-4-20250514\"})",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/ai.phel#L22",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "ai",
        name = "ai/cosine-similarity",
        signature = "(cosine-similarity a b)",
        completion = CompletionInfo(
            tailText = "Computes cosine similarity between two vectors",
            priority = PhelCompletionPriority.AI_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Computes cosine similarity between two vectors. Returns -1.0 to 1.0.",
            example = "(cosine-similarity [1 0] [0 1]) ; =&gt; 0.0",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/ai.phel#L389",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "ai",
        name = "ai/dot-product",
        signature = "(dot-product a b)",
        completion = CompletionInfo(
            tailText = "Computes the dot product of two vectors",
            priority = PhelCompletionPriority.AI_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Computes the dot product of two vectors.",
            example = "(dot-product [1 2 3] [4 5 6]) ; =&gt; 32",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/ai.phel#L371",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "ai",
        name = "ai/embed",
        signature = "(embed texts & [{:model model, :provider provider}])",
        completion = CompletionInfo(
            tailText = "Generates text embeddings",
            priority = PhelCompletionPriority.AI_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Generates text embeddings. Returns a vector of float vectors.",
            example = "(embed [\"hello world\"]) ; =&gt; [[0.123 -0.456 ...]]",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/ai.phel#L444",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "ai",
        name = "ai/embed-one",
        signature = "(embed-one text & [opts])",
        completion = CompletionInfo(
            tailText = "Generates an embedding for a single text",
            priority = PhelCompletionPriority.AI_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Generates an embedding for a single text. Returns a float vector.",
            example = "(embed-one \"hello world\") ; =&gt; [0.123 -0.456 ...]",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/ai.phel#L462",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "ai",
        name = "ai/extract",
        signature = "(extract schema text & [{:system system-prompt, :model model, :max-tokens max-tokens}])",
        completion = CompletionInfo(
            tailText = "Extracts structured data from text using AI",
            priority = PhelCompletionPriority.AI_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Extracts structured data from text using AI. Returns a map matching the schema.",
            example = "(extract {:name \"string\" :age \"integer\"} \"John is 30 years old\")",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/ai.phel#L257",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "ai",
        name = "ai/extract-many",
        signature = "(extract-many schema text & [{:system system-prompt, :model model, :max-tokens max-tokens}])",
        completion = CompletionInfo(
            tailText = "Extracts a list of structured items from text",
            priority = PhelCompletionPriority.AI_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Extracts a list of structured items from text. Returns a vector of maps.",
            example = "(extract-many {:name \"string\" :role \"string\"} \"Alice is CEO, Bob is CTO\")",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/ai.phel#L283",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "ai",
        name = "ai/magnitude",
        signature = "(magnitude v)",
        completion = CompletionInfo(
            tailText = "Computes the L2 norm of a vector",
            priority = PhelCompletionPriority.AI_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Computes the L2 norm of a vector.",
            example = "(magnitude [3 4]) ; =&gt; 5.0",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/ai.phel#L381",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "ai",
        name = "ai/nearest",
        signature = "(nearest query-embedding index & [k])",
        completion = CompletionInfo(
            tailText = "Finds k nearest items to a query embedding by cosine similarity",
            priority = PhelCompletionPriority.AI_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Finds k nearest items to a query embedding by cosine similarity.",
            example = "(nearest query-embedding index 5)",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/ai.phel#L475",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "ai",
        name = "ai/search",
        signature = "(search query index & [{:k k, :model model, :provider provider}])",
        completion = CompletionInfo(
            tailText = "Searches an index for texts most similar to the query",
            priority = PhelCompletionPriority.AI_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Searches an index for texts most similar to the query.",
            example = "(search \"greeting\" my-index 3)",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/ai.phel#L508",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "ai",
        name = "ai/tool",
        signature = "(tool tool-name description input-schema)",
        completion = CompletionInfo(
            tailText = "Creates a tool definition for AI tool use",
            priority = PhelCompletionPriority.AI_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Creates a tool definition for AI tool use.",
            example = "(tool \"get-weather\" \"Gets weather for a city\" {:location {:type \"string\"}})",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/ai.phel#L315",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "ai",
        name = "ai/tool-calls",
        signature = "(tool-calls response)",
        completion = CompletionInfo(
            tailText = "Extracts tool call requests from a chat-with-tools response",
            priority = PhelCompletionPriority.AI_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Extracts tool call requests from a chat-with-tools response.",
            example = "(tool-calls response)",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/ai.phel#L351",
                docs = "",
            ),
        ),
    )
)
