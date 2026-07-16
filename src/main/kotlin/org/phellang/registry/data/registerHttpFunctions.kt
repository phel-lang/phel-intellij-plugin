package org.phellang.registry.data

import org.phellang.registry.CompletionInfo
import org.phellang.registry.DocumentationInfo
import org.phellang.registry.DocumentationLinks
import org.phellang.registry.DeprecationInfo
import org.phellang.registry.PhelFunction

import org.phellang.registry.PhelCompletionPriority

internal fun registerHttpFunctions(): List<PhelFunction> = listOf(
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
                github = "https://github.com/phel-lang/phel-lang/blob/v0.48.0/src/phel/http.phel#L465",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "http",
        name = "http/files-from-globals",
        signature = "(files-from-globals & [files])",
        completion = CompletionInfo(
            tailText = "Extracts uploaded files from \$_FILES and normalizes them to uploaded-file structs",
            priority = PhelCompletionPriority.HTTP_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Extracts uploaded files from <code>${'$'}_FILES</code> and normalizes them to uploaded-file structs. Handles both flat and nested file specs from multipart uploads. Returns a map of field name to uploaded-file(s).
""",
            example = "(files-from-globals) ; =&gt; {:avatar (uploaded-file \"/tmp/phpYzdqkD\" 1024 0 \"photo.jpg\" \"image/jpeg\")}",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.48.0/src/phel/http.phel#L151",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "http",
        name = "http/headers-from-server",
        signature = "(headers-from-server & [server])",
        completion = CompletionInfo(
            tailText = "Extracts all HTTP headers from the \$_SERVER variable",
            priority = PhelCompletionPriority.HTTP_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Extracts all HTTP headers from the <code>${'$'}<em>SERVER</code> variable. Strips the <code>HTTP</em></code> prefix, normalizes underscores to hyphens, handles <code>CONTENT_*</code> keys, and resolves <code>REDIRECT_*</code> keys. Returns a map with keyword keys.
""",
            example = "(headers-from-server) ; =&gt; {:host \"example.com\" :content-type \"application/json\"}",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.48.0/src/phel/http.phel#L162",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "http",
        name = "http/html-response",
        signature = "(html-response status body)",
        completion = CompletionInfo(
            tailText = "Creates a response with status and an HTML body and a Content-Type: text/html; charset=utf-8 header",
            priority = PhelCompletionPriority.HTTP_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Creates a response with <code>status</code> and an HTML <code>body</code> and a<br />
  <code>Content-Type: text/html; charset=utf-8</code> header.
""",
            example = "(html-response 200 \"&lt;h1&gt;Hi&lt;/h1&gt;\") ; =&gt; (response 200 {:content-type \"text/html; charset=utf-8\"} \"&lt;h1&gt;Hi&lt;/h1&gt;\" \"1.1\" \"OK\")",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.48.0/src/phel/http.phel#L416",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "http",
        name = "http/json-response",
        signature = "(json-response status data)",
        completion = CompletionInfo(
            tailText = "Creates a response with status and a JSON-encoded data body and a Content-Type: application/json ...",
            priority = PhelCompletionPriority.HTTP_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Creates a response with <code>status</code> and a JSON-encoded <code>data</code> body and a<br />
  <code>Content-Type: application/json</code> header.
""",
            example = "(json-response 200 {:message \"pong\"}) ; =&gt; (response 200 {:content-type \"application/json\"} \"{\\\"message\\\":\\\"pong\\\"}\" \"1.1\" \"OK\")",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.48.0/src/phel/http.phel#L405",
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
                github = "https://github.com/phel-lang/phel-lang/blob/v0.48.0/src/phel/http.phel#L188",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "http",
        name = "http/request-from-globals",
        signature = "(request-from-globals)",
        completion = CompletionInfo(
            tailText = "Extracts a request from \$_SERVER, \$_GET, \$_POST, \$_COOKIE, \$_FILES and, for JSON requests, the ra...",
            priority = PhelCompletionPriority.HTTP_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Extracts a request from <code>${'$'}_SERVER</code>, <code>${'$'}_GET</code>, <code>${'$'}_POST</code>, <code>${'$'}_COOKIE</code>, <code>${'$'}_FILES</code> and, for JSON requests, the raw request body (<code>php://input</code>). Requires a web request context (PHP-FPM, mod_php, or <code>php -S</code>); it cannot be used from the REPL or a CLI script - use <code>request-from-map</code> for testing.
""",
            example = "(request-from-globals) ; =&gt; (request \"GET\" (uri ...) {...} nil {...} {...} {...} {...} \"1.1\" {})",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.48.0/src/phel/http.phel#L272",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "http",
        name = "http/request-from-globals-args",
        signature = "(request-from-globals-args server get-parameter post-parameter cookies files & [raw-body])",
        completion = CompletionInfo(
            tailText = "Extracts a request from args",
            priority = PhelCompletionPriority.HTTP_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Extracts a request from args. The optional <code>raw-body</code> (the raw request body<br />
  string) is decoded into <code>:parsed-body</code> when the <code>Content-Type</code> is<br />
  <code>application/json</code>; form bodies still come from <code>post-parameter</code>.
""",
            example = "(request-from-globals-args php/\$_SERVER php/\$_GET php/\$_POST php/\$_COOKIE php/\$_FILES (php/file_get_contents \"php://input\")) ; =&gt; (request \"GET\" (uri ...) {...} nil {...} {...} {...} {...} \"1.1\" {})",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.48.0/src/phel/http.phel#L237",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "http",
        name = "http/request-from-map",
        signature = "(request-from-map {:method method, :version version, :uri uri, :headers headers, :parsed-body parsed-body, :query-params query-params, :cookie-params cookie-params, :server-params server-params, :uploaded-files uploaded-files, :attributes attributes})",
        completion = CompletionInfo(
            tailText = "Creates a request struct from a map",
            priority = PhelCompletionPriority.HTTP_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Creates a request struct from a map. Supports the optional keys <code>:method</code>,<br />
  <code>:uri</code> (a string parsed via <code>uri-from-string</code>, or a uri struct used as-is),<br />
  <code>:headers</code>, <code>:parsed-body</code>, <code>:query-params</code>, <code>:cookie-params</code>,<br />
  <code>:server-params</code>, <code>:uploaded-files</code>, <code>:version</code>, and <code>:attributes</code>. Missing<br />
  keys default to nil, <code>{}</code>, <code>[]</code>, or <code>"1.1"</code> as appropriate.
""",
            example = "(request-from-map {:method \"POST\" :uri \"https://api.example.com/users\"}) ; =&gt; (request \"POST\" (uri ...) {} nil {} {} {} [] \"1.1\" {})",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.48.0/src/phel/http.phel#L280",
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
                github = "https://github.com/phel-lang/phel-lang/blob/v0.48.0/src/phel/http.phel#L188",
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
                github = "https://github.com/phel-lang/phel-lang/blob/v0.48.0/src/phel/http.phel#L318",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "http",
        name = "http/response-from-map",
        signature = "(response-from-map {:status status, :headers headers, :body body, :version version, :reason reason})",
        completion = CompletionInfo(
            tailText = "Creates a response struct from a map",
            priority = PhelCompletionPriority.HTTP_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Creates a response struct from a map. Optional keys: <code>:status</code> (defaults<br />
  200), <code>:headers</code> (<code>{}</code>), <code>:body</code> (<code>""</code>), <code>:version</code> (<code>"1.1"</code>), and<br />
  <code>:reason</code> (auto-filled from the HTTP status code when omitted).
""",
            example = "(response-from-map {:status 200 :body \"Hello World\"}) ; =&gt; (response 200 {} \"Hello World\" \"1.1\" \"OK\")",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.48.0/src/phel/http.phel#L383",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "http",
        name = "http/response-from-string",
        signature = "(response-from-string s)",
        completion = CompletionInfo(
            tailText = "Creates a 200 OK response with the given body string",
            priority = PhelCompletionPriority.HTTP_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Creates a 200 OK response with the given body string. Shorthand for<br />
  <code>(response-from-map {:body s})</code>.
""",
            example = "(response-from-string \"Hello World\") ; =&gt; (response 200 {} \"Hello World\" \"1.1\" \"OK\")",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.48.0/src/phel/http.phel#L397",
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
                github = "https://github.com/phel-lang/phel-lang/blob/v0.48.0/src/phel/http.phel#L318",
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
                github = "https://github.com/phel-lang/phel-lang/blob/v0.48.0/src/phel/http.phel#L111",
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
                github = "https://github.com/phel-lang/phel-lang/blob/v0.48.0/src/phel/http.phel#L111",
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
                github = "https://github.com/phel-lang/phel-lang/blob/v0.48.0/src/phel/http.phel#L10",
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
Extracts the URI from the <code>${'$'}_SERVER</code> variable. The scheme is read from<br />
  <code>HTTP_X_FORWARDED_PROTO</code>, <code>REQUEST_SCHEME</code>, or <code>HTTPS</code>; the path from<br />
  <code>REQUEST_URI</code>; and the host/port via the <code>${'$'}_SERVER</code> host fields. Returns a<br />
  uri struct.
""",
            example = "(uri-from-globals) ; =&gt; (uri \"https\" nil \"example.com\" 443 \"/path\" \"foo=bar\" nil)",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.48.0/src/phel/http.phel#L54",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "http",
        name = "http/uri-from-string",
        signature = "(uri-from-string url)",
        completion = CompletionInfo(
            tailText = "Parses a URI string into a uri struct",
            priority = PhelCompletionPriority.HTTP_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Parses a URI string into a uri struct. Handles UTF-8 URLs and IPv6 addresses in brackets. Throws <code>InvalidArgumentException</code> on a malformed string.
""",
            example = "(uri-from-string \"https://example.com/path?foo=bar\") ; =&gt; (uri \"https\" nil \"example.com\" nil \"/path\" \"foo=bar\" nil)",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.48.0/src/phel/http.phel#L82",
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
                github = "https://github.com/phel-lang/phel-lang/blob/v0.48.0/src/phel/http.phel#L10",
                docs = "",
            ),
        ),
    )
)
