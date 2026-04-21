package org.phellang.completion.data

import org.phellang.completion.infrastructure.PhelCompletionPriority

internal fun registerAiFunctions(): List<PhelFunction> = listOf(
    PhelFunction(
        namespace = "ai",
        name = "ai/*http-post*",
        signature = "",
        completion = CompletionInfo(
            tailText = "HTTP POST seam",
            priority = PhelCompletionPriority.AI_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
HTTP POST seam. Rebind with <code>binding</code> in tests to inject a fake transport.
""",
            example = "(binding [*http-post* (fn [url opts] {:status 200 :body \"...\"})] ...)",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/ai.phel#L24",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "ai",
        name = "ai/build-index",
        signature = "(build-index texts & [opts])",
        completion = CompletionInfo(
            tailText = "Builds a searchable index from a collection of text strings",
            priority = PhelCompletionPriority.AI_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Builds a searchable index from a collection of text strings.<br /><br />
Embeds all texts and returns a vector of {:text, :embedding} maps<br />
  suitable for use with <code>nearest</code>.<br /><br />
Options are passed through to <code>embed</code>.
""",
            example = "(build-index [\"hello\" \"world\"])",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/ai.phel#L603",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "ai",
        name = "ai/chat",
        signature = "(chat messages & [opts])",
        completion = CompletionInfo(
            tailText = "Sends a chat completion request with a list of message maps",
            priority = PhelCompletionPriority.AI_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Sends a chat completion request with a list of message maps.<br /><br />
Each message is a map with :role and :content keys.<br />
  Returns the assistant's text response as a string.<br /><br />
Accepts an optional options map:<br />
    :system     - System prompt string<br />
    :model      - Override the configured model<br />
    :max-tokens - Override the configured max tokens<br />
    :provider   - Override the configured provider<br />
    :timeout    - HTTP timeout in seconds<br />
    :base-url   - Override the API base URL<br />
    :api-key    - Override the configured API key
""",
            example = "(chat [{:role \"user\" :content \"Hello!\"}])",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/ai.phel#L261",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "ai",
        name = "ai/chat-with-history",
        signature = "(chat-with-history history user-message & [opts])",
        completion = CompletionInfo(
            tailText = "Appends a new user message to an existing conversation history, sends it, and returns the updated...",
            priority = PhelCompletionPriority.AI_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Appends a new user message to an existing conversation history,<br />
  sends it, and returns the updated history with the assistant's response.<br /><br />
Useful for building multi-turn conversations.
""",
            example = "(chat-with-history [{:role \"user\" :content \"Hi\"}\n                              {:role \"assistant\" :content \"Hello!\"}]\n                             \"How are you?\")",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/ai.phel#L298",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "ai",
        name = "ai/chat-with-tools",
        signature = "(chat-with-tools messages tools & [opts])",
        completion = CompletionInfo(
            tailText = "Sends a chat request with tool definitions",
            priority = PhelCompletionPriority.AI_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Sends a chat request with tool definitions.<br /><br />
Returns a map:<br />
    :text       - Assistant text content (may be nil if model only called tools)<br />
    :tool-calls - Vector of {:name :id :input} maps for any tool calls<br />
    :stop-reason - Provider-specific stop reason<br />
    :raw        - The full provider response body<br /><br />
Options map accepts the same keys as <code>chat</code>.
""",
            example = "(chat-with-tools [{:role \"user\" :content \"What's the weather?\"}]\n                             [(tool \"get-weather\" \"Gets weather\" {:city {:type \"string\"}})])",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/ai.phel#L438",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "ai",
        name = "ai/complete",
        signature = "(complete prompt & [opts])",
        completion = CompletionInfo(
            tailText = "Sends a simple text completion request",
            priority = PhelCompletionPriority.AI_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Sends a simple text completion request.<br /><br />
Takes a prompt string and returns the assistant's text response.<br />
  This is a convenience wrapper around <code>chat</code> for single-turn interactions.
""",
            example = "(complete \"Explain monads in one sentence\")",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/ai.phel#L288",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/ai.phel#L19",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "ai",
        name = "ai/configure",
        signature = "(configure opts)",
        completion = CompletionInfo(
            tailText = "Merges the given options into the AI configuration",
            priority = PhelCompletionPriority.AI_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Merges the given options into the AI configuration.<br /><br />
Supported keys:<br />
    :provider    - :anthropic (default), :openai, or :voyageai<br />
    :model       - Model name string<br />
    :max-tokens  - Maximum tokens in response<br />
    :api-key     - API key string (or set via env var)<br />
    :base-url    - Override the API base URL<br />
    :timeout     - HTTP timeout in seconds (default 120)<br />
    :max-retries - Retry attempts on 429/5xx (default 2)
""",
            example = "(configure {:api-key \"sk-ant-...\" :model \"claude-sonnet-4-20250514\"})",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/ai.phel#L29",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "ai",
        name = "ai/cosine-similarity",
        signature = "(cosine-similarity a b)",
        completion = CompletionInfo(
            tailText = "Computes the cosine similarity between two numeric vectors",
            priority = PhelCompletionPriority.AI_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Computes the cosine similarity between two numeric vectors.<br />
  Returns a float between -1.0 and 1.0, where 1.0 means identical direction.
""",
            example = "(cosine-similarity [1 0] [0 1]) ; =&gt; 0.0",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/ai.phel#L524",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "ai",
        name = "ai/dot-product",
        signature = "(dot-product a b)",
        completion = CompletionInfo(
            tailText = "Computes the dot product of two numeric vectors",
            priority = PhelCompletionPriority.AI_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Computes the dot product of two numeric vectors.",
            example = "(dot-product [1 2 3] [4 5 6]) ; =&gt; 32",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/ai.phel#L508",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "ai",
        name = "ai/embed",
        signature = "(embed texts & [{:model model, :provider provider}])",
        completion = CompletionInfo(
            tailText = "Generates embeddings for one or more text strings",
            priority = PhelCompletionPriority.AI_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Generates embeddings for one or more text strings.<br /><br />
Returns a vector of embedding vectors (one per input text).<br />
  Uses OpenAI's text-embedding-3-small by default.<br /><br />
Options:<br />
    :model    - Override the embedding model name<br />
    :provider - :openai (default) or :voyageai
""",
            example = "(embed [\"hello world\"]) ; =&gt; [[0.123 -0.456 ...]]",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/ai.phel#L561",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "ai",
        name = "ai/embed-one",
        signature = "(embed-one text & [opts])",
        completion = CompletionInfo(
            tailText = "Generates an embedding for a single text string",
            priority = PhelCompletionPriority.AI_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Generates an embedding for a single text string.<br />
  Returns a single embedding vector.
""",
            example = "(embed-one \"hello world\") ; =&gt; [0.123 -0.456 ...]",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/ai.phel#L575",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "ai",
        name = "ai/extract",
        signature = "(extract schema text & [opts])",
        completion = CompletionInfo(
            tailText = "Extracts structured data from text using AI",
            priority = PhelCompletionPriority.AI_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Extracts structured data from text using AI.<br /><br />
<code>schema</code> is a map of field names to type descriptions. Each key becomes<br />
  a field in the output, and each value tells the AI what to extract.<br /><br />
Returns a Phel map with the schema keys populated from the AI response.<br /><br />
Options:<br />
    :system     - Override the system prompt<br />
    :model      - Override the configured model<br />
    :max-tokens - Override the configured max tokens
""",
            example = "(extract {:name \"string\" :age \"integer\"} \"John is 30 years old\")",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/ai.phel#L366",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "ai",
        name = "ai/extract-many",
        signature = "(extract-many schema text & [opts])",
        completion = CompletionInfo(
            tailText = "Extracts a list of structured items from text",
            priority = PhelCompletionPriority.AI_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Extracts a list of structured items from text.<br /><br />
Similar to <code>extract</code>, but returns a vector of maps when the text<br />
  contains multiple items matching the schema.
""",
            example = "(extract-many {:name \"string\" :role \"string\"} \"Alice is CEO, Bob is CTO\")",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/ai.phel#L391",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "ai",
        name = "ai/magnitude",
        signature = "(magnitude v)",
        completion = CompletionInfo(
            tailText = "Computes the magnitude (L2 norm) of a numeric vector",
            priority = PhelCompletionPriority.AI_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Computes the magnitude (L2 norm) of a numeric vector.",
            example = "(magnitude [3 4]) ; =&gt; 5.0",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/ai.phel#L517",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "ai",
        name = "ai/nearest",
        signature = "(nearest query-embedding index & [k])",
        completion = CompletionInfo(
            tailText = "Finds the k nearest items to a query embedding from an index",
            priority = PhelCompletionPriority.AI_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Finds the k nearest items to a query embedding from an index.<br /><br />
<code>index</code> is a vector of {:text "..." :embedding [...]} maps.<br />
  Returns a vector of {:text, :embedding, :similarity} maps sorted<br />
  by descending similarity.
""",
            example = "(nearest query-embedding index 5)",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/ai.phel#L587",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "ai",
        name = "ai/search",
        signature = "(search query index & [{:k k, :model model, :provider provider}])",
        completion = CompletionInfo(
            tailText = "Semantic search: embeds a query and finds the k nearest matches in a pre-built index",
            priority = PhelCompletionPriority.AI_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Semantic search: embeds a query and finds the k nearest matches<br />
  in a pre-built index.<br /><br />
Returns a vector of {:text, :embedding, :similarity} maps.
""",
            example = "(search \"greeting\" my-index 3)",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/ai.phel#L618",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "ai",
        name = "ai/tool",
        signature = "(tool tool-name description input-schema)",
        completion = CompletionInfo(
            tailText = "Creates a tool definition map for use with chat-with-tools",
            priority = PhelCompletionPriority.AI_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Creates a tool definition map for use with <code>chat-with-tools</code>.<br /><br />
<code>tool-name</code> is a string identifier for the tool.<br />
  <code>description</code> describes what the tool does.<br />
  <code>input-schema</code> is a map of parameter names to JSON Schema type maps.<br /><br />
The returned map is provider-agnostic; <code>chat-with-tools</code> converts it to<br />
  the provider's native format.
""",
            example = "(tool \"get-weather\" \"Gets weather for a city\" {:location {:type \"string\"}})",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/ai.phel#L422",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "ai",
        name = "ai/tool-calls",
        signature = "(tool-calls response)",
        completion = CompletionInfo(
            tailText = "Extracts tool call requests from an AI response",
            priority = PhelCompletionPriority.AI_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Extracts tool call requests from an AI response.<br /><br />
Accepts either a raw provider response body or a map produced by<br />
  <code>chat-with-tools</code>. Returns a vector of {:name :id :input} maps.
""",
            example = "(tool-calls response)",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/ai.phel#L470",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "ai",
        name = "ai/tool-result",
        signature = "(tool-result call-id result & [opts])",
        completion = CompletionInfo(
            tailText = "Builds a tool-result message for the given provider",
            priority = PhelCompletionPriority.AI_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Builds a tool-result message for the given provider.<br /><br />
<code>call-id</code> is the tool call id returned by the model.<br />
  <code>result</code> is the tool's output as a string (or value that will be stringified).<br /><br />
Optional <code>opts</code> can set :provider (defaults to current config).
""",
            example = "(tool-result \"call_abc\" \"72F sunny\")",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/ai.phel#L483",
                docs = "",
            ),
        ),
    )
)
