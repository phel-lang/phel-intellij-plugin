package org.phellang.completion.data

import org.phellang.completion.infrastructure.PhelCompletionPriority

internal fun registerHttpFunctions(): List<PhelFunction> = listOf(
    PhelFunction(
        namespace = "http",
        name = "http/create-response-from-map",
        signature = "",
        completion = CompletionInfo(
            tailText = "",
            priority = PhelCompletionPriority.DEPRECATED_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "",
            example = null,
            deprecation = DeprecationInfo(version = "Use response-from-map"),
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/http.phel#L345",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "http",
        name = "http/create-response-from-string",
        signature = "",
        completion = CompletionInfo(
            tailText = "",
            priority = PhelCompletionPriority.DEPRECATED_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "",
            example = null,
            deprecation = DeprecationInfo(version = "Use response-from-string"),
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/http.phel#L353",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "http",
        name = "http/emit-response",
        signature = "(emit-response response)",
        completion = CompletionInfo(
            tailText = "Emits the response by sending headers and outputting the body",
            priority = PhelCompletionPriority.HTTP_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Emits the response by sending headers and outputting the body.",
            example = "(emit-response (response-from-string \"Hello World\")) ; =&gt; nil",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/http.phel#L394",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "http",
        name = "http/files-from-globals",
        signature = "(files-from-globals & [files])",
        completion = CompletionInfo(
            tailText = "Extracts the files from \$_FILES and normalizes them to a map of \"uploaded-file\"",
            priority = PhelCompletionPriority.HTTP_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Extracts the files from <code>${'$'}_FILES</code> and normalizes them to a map of "uploaded-file".
""",
            example = "(files-from-globals) ; =&gt; {:avatar (uploaded-file \"/tmp/phpYzdqkD\" 1024 0 \"photo.jpg\" \"image/jpeg\")}",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/http.phel#L145",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "http",
        name = "http/headers-from-server",
        signature = "(headers-from-server & [server])",
        completion = CompletionInfo(
            tailText = "Extracts all headers from the \$_SERVER variable",
            priority = PhelCompletionPriority.HTTP_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Extracts all headers from the <code>${'$'}_SERVER</code> variable.
""",
            example = "(headers-from-server) ; =&gt; {:host \"example.com\" :content-type \"application/json\"}",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/http.phel#L155",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "http",
        name = "http/request",
        signature = "(request method uri headers parsed-body query-params cookie-params server-params uploaded-files version attributes)",
        completion = CompletionInfo(
            tailText = "Creates a new request struct",
            priority = PhelCompletionPriority.HTTP_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Creates a new request struct.",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/http.phel#L180",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "http",
        name = "http/request-from-globals",
        signature = "(request-from-globals)",
        completion = CompletionInfo(
            tailText = "Extracts a request from \$_SERVER, \$_GET, \$_POST, \$_COOKIE and \$_FILES",
            priority = PhelCompletionPriority.HTTP_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Extracts a request from <code>${'$'}_SERVER</code>, <code>${'$'}_GET</code>, <code>${'$'}_POST</code>, <code>${'$'}_COOKIE</code> and <code>${'$'}_FILES</code>.
""",
            example = "(request-from-globals) ; =&gt; (request \"GET\" (uri ...) {...} nil {...} {...} {...} {...} \"1.1\" {})",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/http.phel#L230",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "http",
        name = "http/request-from-globals-args",
        signature = "(request-from-globals-args server get-parameter post-parameter cookies files)",
        completion = CompletionInfo(
            tailText = "Extracts a request from args",
            priority = PhelCompletionPriority.HTTP_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Extracts a request from args.",
            example = "(request-from-globals-args php/\$_SERVER php/\$_GET php/\$_POST php/\$_COOKIE php/\$_FILES) ; =&gt; (request \"GET\" (uri ...) {...} nil {...} {...} {...} {...} \"1.1\" {})",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/http.phel#L207",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "http",
        name = "http/request-from-map",
        signature = "(request-from-map {:method method, :version version, :uri uri, :headers headers, :parsed-body parsed-body, :query-params query-params, :cookie-params cookie-params, :server-params server-params, :uploaded-files uploaded-files, :attributes attributes})",
        completion = CompletionInfo(
            tailText = "Creates a request struct from a map with optional keys :method, :uri, :headers, etc",
            priority = PhelCompletionPriority.HTTP_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Creates a request struct from a map with optional keys :method, :uri, :headers, etc.
""",
            example = "(request-from-map {:method \"POST\" :uri \"https://api.example.com/users\"}) ; =&gt; (request \"POST\" (uri ...) {} nil {} {} {} [] \"1.1\" {})",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/http.phel#L236",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "http",
        name = "http/request?",
        signature = "(request? x)",
        completion = CompletionInfo(
            tailText = "Checks if x is an instance of the request struct",
            priority = PhelCompletionPriority.PREDICATE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Checks if <code>x</code> is an instance of the request struct.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/http.phel#L180",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "http",
        name = "http/response",
        signature = "(response status headers body version reason)",
        completion = CompletionInfo(
            tailText = "Creates a new response struct",
            priority = PhelCompletionPriority.HTTP_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Creates a new response struct.",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/http.phel#L269",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "http",
        name = "http/response-from-map",
        signature = "(response-from-map {:status status, :headers headers, :body body, :version version, :reason reason})",
        completion = CompletionInfo(
            tailText = "Creates a response struct from a map with optional keys :status, :headers, :body, :version, and :...",
            priority = PhelCompletionPriority.HTTP_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Creates a response struct from a map with optional keys :status, :headers, :body, :version, and :reason.
""",
            example = "(response-from-map {:status 200 :body \"Hello World\"}) ; =&gt; (response 200 {} \"Hello World\" \"1.1\" \"OK\")",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/http.phel#L334",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "http",
        name = "http/response-from-string",
        signature = "(response-from-string s)",
        completion = CompletionInfo(
            tailText = "Create a response from a string",
            priority = PhelCompletionPriority.HTTP_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Create a response from a string.",
            example = "(response-from-string \"Hello World\") ; =&gt; (response 200 {} \"Hello World\" \"1.1\" \"OK\")",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/http.phel#L347",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "http",
        name = "http/response?",
        signature = "(response? x)",
        completion = CompletionInfo(
            tailText = "Checks if x is an instance of the response struct",
            priority = PhelCompletionPriority.PREDICATE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Checks if <code>x</code> is an instance of the response struct.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/http.phel#L269",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "http",
        name = "http/uploaded-file",
        signature = "(uploaded-file tmp-file size error-status client-filename client-media-type)",
        completion = CompletionInfo(
            tailText = "Creates a new uploaded-file struct",
            priority = PhelCompletionPriority.HTTP_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Creates a new uploaded-file struct.",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/http.phel#L105",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "http",
        name = "http/uploaded-file?",
        signature = "(uploaded-file? x)",
        completion = CompletionInfo(
            tailText = "Checks if x is an instance of the uploaded-file struct",
            priority = PhelCompletionPriority.PREDICATE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Checks if <code>x</code> is an instance of the uploaded-file struct.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/http.phel#L105",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "http",
        name = "http/uri",
        signature = "(uri scheme userinfo host port path query fragment)",
        completion = CompletionInfo(
            tailText = "Creates a new uri struct",
            priority = PhelCompletionPriority.HTTP_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Creates a new uri struct.",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/http.phel#L9",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "http",
        name = "http/uri-from-globals",
        signature = "(uri-from-globals & [server])",
        completion = CompletionInfo(
            tailText = "Extracts the URI from the \$_SERVER variable",
            priority = PhelCompletionPriority.HTTP_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Extracts the URI from the <code>${'$'}_SERVER</code> variable.
""",
            example = "(uri-from-globals) ; =&gt; (uri \"https\" nil \"example.com\" 443 \"/path\" \"foo=bar\" nil)",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/http.phel#L53",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "http",
        name = "http/uri-from-string",
        signature = "(uri-from-string url)",
        completion = CompletionInfo(
            tailText = "Create a uri struct from a string",
            priority = PhelCompletionPriority.HTTP_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Create a uri struct from a string.",
            example = "(uri-from-string \"https://example.com/path?foo=bar\") ; =&gt; (uri \"https\" nil \"example.com\" nil \"/path\" \"foo=bar\" nil)",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/http.phel#L77",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "http",
        name = "http/uri?",
        signature = "(uri? x)",
        completion = CompletionInfo(
            tailText = "Checks if x is an instance of the uri struct",
            priority = PhelCompletionPriority.PREDICATE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Checks if <code>x</code> is an instance of the uri struct.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/http.phel#L9",
                docs = "",
            ),
        ),
    )
)
