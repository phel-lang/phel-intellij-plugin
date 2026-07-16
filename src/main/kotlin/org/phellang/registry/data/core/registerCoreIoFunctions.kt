package org.phellang.registry.data.core

import org.phellang.registry.CompletionInfo
import org.phellang.registry.DocumentationInfo
import org.phellang.registry.DocumentationLinks
import org.phellang.registry.DeprecationInfo
import org.phellang.registry.PhelFunction

import org.phellang.registry.PhelCompletionPriority

internal fun registerCoreIoFunctions(): List<PhelFunction> = listOf(
    PhelFunction(
        namespace = "core",
        name = "->",
        signature = "(-> x & forms)",
        completion = CompletionInfo(
            tailText = "Threads the expr through the forms",
            priority = PhelCompletionPriority.MACROS,
        ),
        documentation = DocumentationInfo(
            summary = """
Threads the expr through the forms. Inserts <code>x</code> as the second item in the first form, making a list of it if it is not a list already. If there are more forms, insert the first form as the second item in the second form, etc.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.48.0/src/phel/core/io.phel#L250",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "->>",
        signature = "(->> x & forms)",
        completion = CompletionInfo(
            tailText = "Threads the expr through the forms",
            priority = PhelCompletionPriority.MACROS,
        ),
        documentation = DocumentationInfo(
            summary = """
Threads the expr through the forms. Inserts <code>x</code> as the last item in the first form, making a list of it if it is not a list already. If there are more forms, insert the first form as the last item in the second form, etc.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.48.0/src/phel/core/io.phel#L263",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "as->",
        signature = "(as-> expr name & forms)",
        completion = CompletionInfo(
            tailText = "Binds name to expr, evaluates the first form in the lexical context of that binding, then binds n...",
            priority = PhelCompletionPriority.MACROS,
        ),
        documentation = DocumentationInfo(
            summary = """
Binds <code>name</code> to <code>expr</code>, evaluates the first form in the lexical context of that binding, then binds name to that result, repeating for each successive form, returning the result of the last form.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.48.0/src/phel/core/io.phel#L324",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "binding",
        signature = "(binding bindings & body)",
        completion = CompletionInfo(
            tailText = "Temporarily rebinds dynamic vars while executing body",
            priority = PhelCompletionPriority.MACROS,
        ),
        documentation = DocumentationInfo(
            summary = """
Temporarily rebinds dynamic vars while executing <code>body</code>.<br /><br />
Each var in <code>bindings</code> must be tagged <code>^:dynamic</code> at its <code>def</code>. The<br />
  rebinding is fiber-local: concurrent fibers observe independent<br />
  values, and child futures (<code>future</code>, <code>async</code>, <code>future-fiber</code>)<br />
  inherit the bindings active at their creation.<br /><br />
Throws at runtime if any var in the bindings vector is not<br />
  <code>:dynamic</code>. To swap a non-dynamic var for the duration of an<br />
  expression (e.g. mocking in tests), use <code>with-redefs</code>.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.48.0/src/phel/core/io.phel#L450",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "cond->",
        signature = "(cond-> expr & clauses)",
        completion = CompletionInfo(
            tailText = "Takes an expression and a set of test/form pairs",
            priority = PhelCompletionPriority.MACROS,
        ),
        documentation = DocumentationInfo(
            summary = """
Takes an expression and a set of test/form pairs. Threads <code>expr</code> (via <code>-></code>) through each form for which the corresponding test expression is true. Note that, unlike <code>cond</code> branching, <code>cond-></code> threading does not short-circuit after the first true test expression.
""",
            example = "(cond-&gt; 1 true inc false (* 42) true (* 3)) ; =&gt; 6",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.48.0/src/phel/core/io.phel#L343",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "cond->>",
        signature = "(cond->> expr & clauses)",
        completion = CompletionInfo(
            tailText = "Takes an expression and a set of test/form pairs",
            priority = PhelCompletionPriority.MACROS,
        ),
        documentation = DocumentationInfo(
            summary = """
Takes an expression and a set of test/form pairs. Threads <code>expr</code> (via <code>->></code>) through each form for which the corresponding test expression is true. Note that, unlike <code>cond</code> branching, <code>cond->></code> threading does not short-circuit after the first true test expression.
""",
            example = "(cond-&gt;&gt; [1 2 3] true (map inc) false (filter odd?)) ; =&gt; @[2 3 4]",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.48.0/src/phel/core/io.phel#L357",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "csv-seq",
        signature = "(csv-seq filename)\n(csv-seq filename options)",
        completion = CompletionInfo(
            tailText = "Returns a lazy sequence of rows from a CSV file",
            priority = PhelCompletionPriority.COLLECTION_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Returns a lazy sequence of rows from a CSV file.",
            example = "(take 10 (csv-seq \"data.csv\")) ; =&gt; [[\"col1\" \"col2\"] [\"val1\" \"val2\"] ...]",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.48.0/src/phel/core/io.phel#L193",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "dbg",
        signature = "(dbg)\n(dbg expr)",
        completion = CompletionInfo(
            tailText = "Prints [file:line] form => value to the standard error stream and returns the value unchanged, so...",
            priority = PhelCompletionPriority.MACROS,
        ),
        documentation = DocumentationInfo(
            summary = """
Prints <code>[file:line] form => value</code> to the standard error stream and returns the value unchanged, so it can wrap any subexpression without restructuring code. With no argument prints just <code>[file:line]</code> as a reached-here marker and returns nil.
""",
            example = "(defn area [w h] (* (dbg w) h))\n(area 3 4) ; stderr: [src/main.phel:12] w =&gt; 3",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.48.0/src/phel/core/io.phel#L88",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "dbg-write",
        signature = "(dbg-write message)",
        completion = CompletionInfo(
            tailText = "Writes message to the standard error stream",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Writes <code>message</code> to the standard error stream. Output primitive behind the <code>dbg</code> macro; redefine it with <code>with-redefs</code> to capture debug output in tests.
""",
            example = "(dbg-write \"debug line\\n\")",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.48.0/src/phel/core/io.phel#L69",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "doto",
        signature = "(doto x & forms)",
        completion = CompletionInfo(
            tailText = "Evaluates x then calls all of the methods and functions with the value of x supplied at the front...",
            priority = PhelCompletionPriority.MACROS,
        ),
        documentation = DocumentationInfo(
            summary = """
Evaluates x then calls all of the methods and functions with the value of x supplied at the front of the given arguments. The forms are evaluated in order. Returns x.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.48.0/src/phel/core/io.phel#L331",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "file-seq",
        signature = "(file-seq path)",
        completion = CompletionInfo(
            tailText = "Returns a lazy sequence of all files and directories in a directory tree",
            priority = PhelCompletionPriority.COLLECTION_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Returns a lazy sequence of all files and directories in a directory tree.",
            example = "(filter #(php/str_ends_with % \".phel\") (file-seq \"src/\")) ; =&gt; [\"src/file1.phel\" \"src/file2.phel\" ...]",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.48.0/src/phel/core/io.phel#L167",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "format",
        signature = "(format fmt & xs)",
        completion = CompletionInfo(
            tailText = "Returns a formatted string",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns a formatted string. See PHP's <a href="https://www.php.net/manual/en/function.sprintf.php">sprintf</a> for more information.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.48.0/src/phel/core/io.phel#L58",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "line-seq",
        signature = "(line-seq filename)",
        completion = CompletionInfo(
            tailText = "Returns a lazy sequence of lines from a file",
            priority = PhelCompletionPriority.COLLECTION_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Returns a lazy sequence of lines from a file.",
            example = "(take 10 (line-seq \"large-file.txt\")) ; =&gt; [\"line1\" \"line2\" ...]",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.48.0/src/phel/core/io.phel#L155",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "print",
        signature = "(print & xs)",
        completion = CompletionInfo(
            tailText = "Prints the given values to the default output stream",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Prints the given values to the default output stream. Returns nil.",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.48.0/src/phel/core/io.phel#L45",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "print-str",
        signature = "(print-str & xs)",
        completion = CompletionInfo(
            tailText = "Same as print",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Same as print. But instead of writing it to an output stream, the resulting string is returned.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.48.0/src/phel/core/io.phel#L31",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "printf",
        signature = "(printf fmt & xs)",
        completion = CompletionInfo(
            tailText = "Output a formatted string",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Output a formatted string. See PHP's <a href="https://www.php.net/manual/en/function.printf.php">printf</a> for more information.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.48.0/src/phel/core/io.phel#L63",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "println",
        signature = "(println & xs)",
        completion = CompletionInfo(
            tailText = "Same as print followed by a newline",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Same as print followed by a newline.",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.48.0/src/phel/core/io.phel#L51",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "re-find",
        signature = "(re-find re s)",
        completion = CompletionInfo(
            tailText = "Returns the first match of pattern in string, or nil if no match",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns the first match of pattern in string, or nil if no match. If the pattern has groups, returns a vector of [full-match group1 group2 ...].
""",
            example = "(re-find #\"\\d+\" \"abc123def\") ; =&gt; \"123\"",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.48.0/src/phel/core/io.phel#L399",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "re-matches",
        signature = "(re-matches re s)",
        completion = CompletionInfo(
            tailText = "Returns the match, if any, of string to pattern",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns the match, if any, of string to pattern. If the pattern has groups, returns a vector of [full-match group1 group2 ...]. Returns nil if no match. Unlike re-find, the entire string must match.
""",
            example = "(re-matches #\"(\\d+)-(\\d+)\" \"12-34\") ; =&gt; [\"12-34\" \"12\" \"34\"]",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.48.0/src/phel/core/io.phel#L412",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "re-pattern",
        signature = "(re-pattern s)",
        completion = CompletionInfo(
            tailText = "Returns a PCRE pattern string from s",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns a PCRE pattern string from <code>s</code>. If <code>s</code> is already delimited, returns it as-is. Otherwise wraps in <code>/</code> delimiters.
""",
            example = "(re-pattern \"\\\\d+\") ; =&gt; \"/\\\\d+/\"",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.48.0/src/phel/core/io.phel#L375",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "re-seq",
        signature = "(re-seq re s)",
        completion = CompletionInfo(
            tailText = "Returns a sequence of successive matches of pattern in string",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Returns a sequence of successive matches of pattern in string.",
            example = "(re-seq #\"\\d+\" \"a1b2c3\") ; =&gt; [\"1\" \"2\" \"3\"]",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.48.0/src/phel/core/io.phel#L387",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "read-file-lazy",
        signature = "(read-file-lazy filename)\n(read-file-lazy filename chunk-size)",
        completion = CompletionInfo(
            tailText = "Returns a lazy sequence of byte chunks from a file",
            priority = PhelCompletionPriority.COLLECTION_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Returns a lazy sequence of byte chunks from a file.",
            example = "(take 5 (read-file-lazy \"large-file.bin\" 1024)) ; =&gt; [\"chunk1\" \"chunk2\" ...]",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.48.0/src/phel/core/io.phel#L179",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "slurp",
        signature = "(slurp path & [opts])",
        completion = CompletionInfo(
            tailText = "Reads entire file or URL into a string",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Reads entire file or URL into a string.",
            example = "(slurp \"file.txt\") ; =&gt; \"file contents\"",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.48.0/src/phel/core/io.phel#L113",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "some->",
        signature = "(some-> x & forms)",
        completion = CompletionInfo(
            tailText = "Threads x through the forms like -> but stops when a form returns nil",
            priority = PhelCompletionPriority.MACROS,
        ),
        documentation = DocumentationInfo(
            summary = """
Threads <code>x</code> through the forms like <code>-></code> but stops when a form returns <code>nil</code>.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.48.0/src/phel/core/io.phel#L276",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "some->>",
        signature = "(some->> x & forms)",
        completion = CompletionInfo(
            tailText = "Threads x through the forms like ->> but stops when a form returns nil",
            priority = PhelCompletionPriority.MACROS,
        ),
        documentation = DocumentationInfo(
            summary = """
Threads <code>x</code> through the forms like <code>->></code> but stops when a form returns <code>nil</code>.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.48.0/src/phel/core/io.phel#L300",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "spit",
        signature = "(spit filename data & [opts])",
        completion = CompletionInfo(
            tailText = "Writes data to file, returning number of bytes that were written or nil on failure",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Writes data to file, returning number of bytes that were written or <code>nil</code> on failure. Accepts <code>opts</code> map for overriding default PHP file_put_contents arguments, as example to append to file use <code>{:flags php/FILE_APPEND}</code>.<br /><br />
See PHP's <a href="https://www.php.net/manual/en/function.file-put-contents.php">file_put_contents</a> for more information.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.48.0/src/phel/core/io.phel#L141",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "with-bindings",
        signature = "(with-bindings m & body)",
        completion = CompletionInfo(
            tailText = "Like binding but takes a map of Var -> value instead of a binding vector",
            priority = PhelCompletionPriority.MACROS,
        ),
        documentation = DocumentationInfo(
            summary = """
Like <code>binding</code> but takes a map of <code>Var -> value</code> instead of a binding vector. Same dynamic-only enforcement: throws when any key resolves to a non-dynamic var.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.48.0/src/phel/core/io.phel#L477",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "with-open",
        signature = "(with-open bindings & body)",
        completion = CompletionInfo(
            tailText = "Evaluates body with the names bound as in let, then closes every bound resource in a finally clau...",
            priority = PhelCompletionPriority.MACROS,
        ),
        documentation = DocumentationInfo(
            summary = """
Evaluates body with the names bound as in <code>let</code>, then closes every bound<br />
  resource in a <code>finally</code> clause — in reverse binding order — even when the<br />
  body throws. Objects exposing a <code>close</code> method are closed by calling it,<br />
  stream resources with <code>php/fclose</code>; <code>nil</code> bindings are skipped.
""",
            example = "(with-open [f (php/fopen \"data.txt\" \"r\")] (php/fgets f))",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.48.0/src/phel/core/io.phel#L223",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "with-output-buffer",
        signature = "(with-output-buffer & body)",
        completion = CompletionInfo(
            tailText = "Everything that is printed inside the body will be stored in a buffer",
            priority = PhelCompletionPriority.MACROS,
        ),
        documentation = DocumentationInfo(
            summary = """
Everything that is printed inside the body will be stored in a buffer. The result of the buffer is returned.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.48.0/src/phel/core/io.phel#L20",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "with-redefs",
        signature = "(with-redefs bindings & body)",
        completion = CompletionInfo(
            tailText = "Temporarily replaces the root values of vars while executing body",
            priority = PhelCompletionPriority.MACROS,
        ),
        documentation = DocumentationInfo(
            summary = """
Temporarily replaces the root values of vars while executing <code>body</code>.<br /><br />
Accepts any var, dynamic or not. The previous root values are<br />
  captured before <code>body</code> runs and restored on exit, including when<br />
  <code>body</code> throws. The replacement is process-global: every fiber sees<br />
  the new root for the duration of the body, which makes this a<br />
  convenient mocking primitive for tests but unsafe for concurrent<br />
  production code. Use <code>binding</code> for fiber-local rebinding of<br />
  <code>^:dynamic</code> vars.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.48.0/src/phel/core/io.phel#L464",
                docs = "",
            ),
        ),
    )
)
