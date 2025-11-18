package org.phellang.completion.data

import org.phellang.completion.infrastructure.PhelCompletionPriority

internal fun registerHttpFunctions(): List<DataFunction> = listOf(
    DataFunction("http/create-response-from-map", "", PhelCompletionPriority.HTTP_FUNCTIONS, "http", "", """
<br />
<br />
"""),
    DataFunction("http/create-response-from-string", "", PhelCompletionPriority.HTTP_FUNCTIONS, "http", "", """
<br />
<br />
"""),
    DataFunction("http/emit-response", "(emit-response response)", PhelCompletionPriority.HTTP_FUNCTIONS, "http", "Emits the response by sending headers and outputting the body", """
<br /><code>(emit-response response)</code><br /><br />
Emits the response by sending headers and outputting the body.<br />
<br />
  <pre><code>(emit-response (response-from-string \"Hello World\"))<br /># => nil</code></pre>
<br />
"""),
    DataFunction("http/files-from-globals", "(files-from-globals & [files])", PhelCompletionPriority.HTTP_FUNCTIONS, "http", "Extracts the files from `'${'$'}_'FILES` and normalizes them to a map of \"uploaded-file\"", """
<br /><code>(files-from-globals & [files])</code><br /><br />
Extracts the files from <b>'${'$'}_'FILES</b> and normalizes them to a map of \"uploaded-file\".<br />
<br />
  <pre><code>(files-from-globals)<br /># => <code>{:avatar (uploaded-file \"/tmp/phpYzdqkD\" 1024 0 \"photo.jpg\" \"image/jpeg\")}</code></code></pre>
<br />
"""),
    DataFunction("http/headers-from-server", "(headers-from-server & [server])", PhelCompletionPriority.HTTP_FUNCTIONS, "http", "Extracts all headers from the `${'$'}_SERVER` variable", """
<br /><code>(headers-from-server & [server])</code><br /><br />
Extracts all headers from the <b>${'$'}_SERVER</b> variable.<br />
<br />
  <pre><code>(headers-from-server)<br /># => <code>{:host \"example.com\" :content-type \"application/json\"}</code></code></pre>
<br />
"""),
    DataFunction("http/request", "(request method uri headers parsed-body query-params cookie-params server-params uploaded-files version attributes)", PhelCompletionPriority.HTTP_FUNCTIONS, "http", "Creates a new request struct", """
<br /><code>(request method uri headers parsed-body query-params cookie-params server-params uploaded-files version attributes)</code><br /><br />
Creates a new request struct.<br />
<br />
"""),
    DataFunction("http/request-from-globals", "(request-from-globals )", PhelCompletionPriority.HTTP_FUNCTIONS, "http", "Extracts a request from `'${'$'}_'SERVER`, `'${'$'}_'GET`, `'${'$'}_'POST`, `'${'$'}_'COOKIE` and `'${'$'}_'FILES`", """
<br /><code>(request-from-globals )</code><br /><br />
Extracts a request from <b>'${'$'}_'SERVER</b>, <b>'${'$'}_'GET</b>, <b>'${'$'}_'POST</b>, <b>'${'$'}_'COOKIE</b> and <b>'${'$'}_'FILES</b>.<br />
<br />
  <pre><code>(request-from-globals)<br /># => (request \"GET\" (uri ...) {...} nil {...} {...} {...} {...} \"1.1\" {})</code></pre>
<br />
"""),
    DataFunction("http/request-from-globals-args", "(request-from-globals-args server get-parameter post-parameter cookies files)", PhelCompletionPriority.HTTP_FUNCTIONS, "http", "Extracts a request from args", """
<br /><code>(request-from-globals-args server get-parameter post-parameter cookies files)</code><br /><br />
Extracts a request from args.<br />
<br />
  <pre><code>(request-from-globals-args php/'${'$'}_'SERVER php/'${'$'}_'GET php/'${'$'}_'POST php/'${'$'}_'COOKIE php/'${'$'}_'FILES)<br /># => (request \"GET\" (uri ...) {...} nil {...} {...} {...} {...} \"1.1\" {})</code></pre>
<br />
"""),
    DataFunction("http/request-from-map", "(request-from-map {:method method, :version version, :uri uri, :headers headers, :parsed-body parsed-body, :query-params query-params, :cookie-params cookie-params, :server-params server-params, :uploaded-files uploaded-files, :attributes attributes})", PhelCompletionPriority.HTTP_FUNCTIONS, "http", "Creates a request struct from a map with optional keys :method, :uri, :headers, etc", """
<br /><code>(request-from-map <code>{:method method, :version version, :uri uri, :headers headers, :parsed-body parsed-body, :query-params query-params, :cookie-params cookie-params, :server-params server-params, :uploaded-files uploaded-files, :attributes attributes}</code>)</code><br /><br />
Creates a request struct from a map with optional keys :method, :uri, :headers, etc.<br />
<br />
  <pre><code>(request-from-map <code>{:method \"POST\" :uri \"https://api.example.com/users\"}</code>)<br /># => (request \"POST\" (uri ...) {} nil {} {} {} [] \"1.1\" {})</code></pre>
<br />
"""),
    DataFunction("http/request?", "(request? x)", PhelCompletionPriority.HTTP_FUNCTIONS, "http", "Checks if `x` is an instance of the request struct", """
<br /><code>(request? x)</code><br /><br />
Checks if <b>x</b> is an instance of the request struct.<br />
<br />
"""),
    DataFunction("http/response", "(response status headers body version reason)", PhelCompletionPriority.HTTP_FUNCTIONS, "http", "Creates a new response struct", """
<br /><code>(response status headers body version reason)</code><br /><br />
Creates a new response struct.<br />
<br />
"""),
    DataFunction("http/response-from-map", "(response-from-map {:status status, :headers headers, :body body, :version version, :reason reason})", PhelCompletionPriority.HTTP_FUNCTIONS, "http", "Creates a response struct from a map with optional keys :status, :headers, :body, :version, and :reason", """
<br /><code>(response-from-map <code>{:status status, :headers headers, :body body, :version version, :reason reason}</code>)</code><br /><br />
Creates a response struct from a map with optional keys :status, :headers, :body, :version, and :reason.<br />
<br />
  <pre><code>(response-from-map <code>{:status 200 :body \"Hello World\"}</code>)<br /># => (response 200 {} \"Hello World\" \"1.1\" \"OK\")</code></pre>
<br />
"""),
    DataFunction("http/response-from-string", "(response-from-string s)", PhelCompletionPriority.HTTP_FUNCTIONS, "http", "Create a response from a string", """
<br /><code>(response-from-string s)</code><br /><br />
Create a response from a string.<br />
<br />
  <pre><code>(response-from-string \"Hello World\")<br /># => (response 200 {} \"Hello World\" \"1.1\" \"OK\")</code></pre>
<br />
"""),
    DataFunction("http/response?", "(response? x)", PhelCompletionPriority.HTTP_FUNCTIONS, "http", "Checks if `x` is an instance of the response struct", """
<br /><code>(response? x)</code><br /><br />
Checks if <b>x</b> is an instance of the response struct.<br />
<br />
"""),
    DataFunction("http/uploaded-file", "(uploaded-file tmp-file size error-status client-filename client-media-type)", PhelCompletionPriority.HTTP_FUNCTIONS, "http", "Creates a new uploaded-file struct", """
<br /><code>(uploaded-file tmp-file size error-status client-filename client-media-type)</code><br /><br />
Creates a new uploaded-file struct.<br />
<br />
"""),
    DataFunction("http/uploaded-file?", "(uploaded-file? x)", PhelCompletionPriority.HTTP_FUNCTIONS, "http", "Checks if `x` is an instance of the uploaded-file struct", """
<br /><code>(uploaded-file? x)</code><br /><br />
Checks if <b>x</b> is an instance of the uploaded-file struct.<br />
<br />
"""),
    DataFunction("http/uri", "(uri scheme userinfo host port path query fragment)", PhelCompletionPriority.HTTP_FUNCTIONS, "http", "Creates a new uri struct", """
<br /><code>(uri scheme userinfo host port path query fragment)</code><br /><br />
Creates a new uri struct.<br />
<br />
"""),
    DataFunction("http/uri-from-globals", "(uri-from-globals & [server])", PhelCompletionPriority.HTTP_FUNCTIONS, "http", "Extracts the URI from the `${'$'}_SERVER` variable", """
<br /><code>(uri-from-globals & [server])</code><br /><br />
Extracts the URI from the <b>${'$'}_SERVER</b> variable.<br />
<br />
  <pre><code>(uri-from-globals)<br /># => (uri \"https\" nil \"example.com\" 443 \"/path\" \"foo=bar\" nil)</code></pre>
<br />
"""),
    DataFunction("http/uri-from-string", "(uri-from-string url)", PhelCompletionPriority.HTTP_FUNCTIONS, "http", "Create a uri struct from a string", """
<br /><code>(uri-from-string url)</code><br /><br />
Create a uri struct from a string.<br />
<br />
  <pre><code>(uri-from-string \"https://example.com/path?foo=bar\")<br /># => (uri \"https\" nil \"example.com\" nil \"/path\" \"foo=bar\" nil)</code></pre>
<br />
"""),
    DataFunction("http/uri?", "(uri? x)", PhelCompletionPriority.HTTP_FUNCTIONS, "http", "Checks if `x` is an instance of the uri struct", """
<br /><code>(uri? x)</code><br /><br />
Checks if <b>x</b> is an instance of the uri struct.<br />
<br />
"""),
)
