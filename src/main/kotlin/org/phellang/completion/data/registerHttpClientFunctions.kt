package org.phellang.completion.data

import org.phellang.completion.infrastructure.PhelCompletionPriority

internal fun registerHttpClientFunctions(): List<PhelFunction> = listOf(
    PhelFunction(
        namespace = "http_client",
        name = "http_client/delete",
        signature = "(delete url & [opts])",
        completion = CompletionInfo(
            tailText = "Makes a DELETE request",
            priority = PhelCompletionPriority.HTTP_CLIENT_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Makes a DELETE request. Returns an http/response struct.",
            example = "(delete \"https://api.example.com/1\")",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.46.0/src/phel/http-client.phel#L128",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "http_client",
        name = "http_client/get",
        signature = "(get url & [opts])",
        completion = CompletionInfo(
            tailText = "Makes a GET request",
            priority = PhelCompletionPriority.HTTP_CLIENT_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Makes a GET request. Returns an http/response struct.",
            example = "(get \"https://example.com\")",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.46.0/src/phel/http-client.phel#L104",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "http_client",
        name = "http_client/head",
        signature = "(head url & [opts])",
        completion = CompletionInfo(
            tailText = "Makes a HEAD request",
            priority = PhelCompletionPriority.HTTP_CLIENT_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Makes a HEAD request. Returns an http/response struct.",
            example = "(head \"https://example.com\")",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.46.0/src/phel/http-client.phel#L144",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "http_client",
        name = "http_client/patch",
        signature = "(patch url & [opts])",
        completion = CompletionInfo(
            tailText = "Makes a PATCH request",
            priority = PhelCompletionPriority.HTTP_CLIENT_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Makes a PATCH request. Returns an http/response struct.",
            example = "(patch \"https://api.example.com/1\" {:json {:name \"Charlie\"}})",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.46.0/src/phel/http-client.phel#L136",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "http_client",
        name = "http_client/post",
        signature = "(post url & [opts])",
        completion = CompletionInfo(
            tailText = "Makes a POST request",
            priority = PhelCompletionPriority.HTTP_CLIENT_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Makes a POST request. Returns an http/response struct.",
            example = "(post \"https://api.example.com\" {:json {:name \"Alice\"}})",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.46.0/src/phel/http-client.phel#L112",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "http_client",
        name = "http_client/put",
        signature = "(put url & [opts])",
        completion = CompletionInfo(
            tailText = "Makes a PUT request",
            priority = PhelCompletionPriority.HTTP_CLIENT_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Makes a PUT request. Returns an http/response struct.",
            example = "(put \"https://api.example.com/1\" {:json {:name \"Bob\"}})",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.46.0/src/phel/http-client.phel#L120",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "http_client",
        name = "http_client/request",
        signature = "(request method url & [{:headers headers, :body body, :json json-body, :query-params query-params, :timeout timeout, :follow-redirects follow-redirects, :verify-ssl verify-ssl}])",
        completion = CompletionInfo(
            tailText = "Makes an HTTP request",
            priority = PhelCompletionPriority.HTTP_CLIENT_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Makes an HTTP request. Returns an http/response struct.",
            example = "(request :get \"https://example.com\" {:headers {:accept \"application/json\"}})",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.46.0/src/phel/http-client.phel#L69",
                docs = "",
            ),
        ),
    )
)
