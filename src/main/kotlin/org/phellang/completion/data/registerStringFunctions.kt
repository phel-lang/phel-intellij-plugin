package org.phellang.completion.data

import org.phellang.completion.infrastructure.PhelCompletionPriority

internal fun registerStringFunctions(): List<PhelFunction> = listOf(
    PhelFunction(
        namespace = "str",
        name = "str/blank?",
        signature = "(blank? s)",
        completion = CompletionInfo(
            tailText = "True if s is nil, empty, or contains only whitespace",
            priority = PhelCompletionPriority.PREDICATE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "True if s is nil, empty, or contains only whitespace.",
            example = "(blank? \"   \") ; =&gt; true",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/str.phel#L136",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "str",
        name = "str/capitalize",
        signature = "(capitalize s)",
        completion = CompletionInfo(
            tailText = "Converts first character to upper-case and all other characters to lower-case",
            priority = PhelCompletionPriority.STRING_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Converts first character to upper-case and all other characters to lower-case.",
            example = "(capitalize \"hELLO wORLD\") ; =&gt; \"Hello world\"",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/str.phel#L97",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "str",
        name = "str/chars",
        signature = "(chars s)",
        completion = CompletionInfo(
            tailText = "Returns a vector of characters from string s",
            priority = PhelCompletionPriority.STRING_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns a vector of characters from string s.<br /><br />
This is a convenience function for converting strings to character sequences.<br />
  Properly handles multibyte UTF-8 characters.
""",
            example = "(chars \"hello\") ; =&gt; [\"h\" \"e\" \"l\" \"l\" \"o\"]",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/str.phel#L11",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "str",
        name = "str/contains?",
        signature = "(contains? s substr)",
        completion = CompletionInfo(
            tailText = "True if s contains substr",
            priority = PhelCompletionPriority.PREDICATE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "True if s contains substr.",
            example = "(contains? \"hello world\" \"lo wo\") ; =&gt; true",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/str.phel#L161",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "str",
        name = "str/ends-with?",
        signature = "(ends-with? s substr)",
        completion = CompletionInfo(
            tailText = "True if s ends with substr",
            priority = PhelCompletionPriority.PREDICATE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "True if s ends with substr.",
            example = "(ends-with? \"hello world\" \"world\") ; =&gt; true",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/str.phel#L155",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "str",
        name = "str/escape",
        signature = "(escape s cmap)",
        completion = CompletionInfo(
            tailText = "Returns a new string with each character escaped according to cmap",
            priority = PhelCompletionPriority.STRING_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Returns a new string with each character escaped according to cmap.",
            example = "(escape \"hello\" {\"h\" \"H\" \"o\" \"O\"}) ; =&gt; \"HellO\"",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/str.phel#L179",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "str",
        name = "str/includes?",
        signature = "(includes? s substr)",
        completion = CompletionInfo(
            tailText = "True if s includes substr",
            priority = PhelCompletionPriority.PREDICATE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "True if s includes substr.",
            example = "(includes? \"hello world\" \"world\") ; =&gt; true",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/str.phel#L167",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "str",
        name = "str/index-of",
        signature = "(index-of s value & [from-index])",
        completion = CompletionInfo(
            tailText = "Returns the index of value in s, or nil if not found",
            priority = PhelCompletionPriority.STRING_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Returns the index of value in s, or nil if not found.",
            example = "(index-of \"hello world\" \"world\") ; =&gt; 6",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/str.phel#L191",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "str",
        name = "str/join",
        signature = "(join separator & [coll])",
        completion = CompletionInfo(
            tailText = "Returns a string of all elements in coll, separated by an optional separator",
            priority = PhelCompletionPriority.STRING_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Returns a string of all elements in coll, separated by an optional separator.",
            example = "(join \", \" [\"apple\" \"banana\" \"cherry\"]) ; =&gt; \"apple, banana, cherry\"",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/str.phel#L21",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "str",
        name = "str/last-index-of",
        signature = "(last-index-of s value & [from-index])",
        completion = CompletionInfo(
            tailText = "Returns the last index of value in s, or nil if not found",
            priority = PhelCompletionPriority.STRING_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Returns the last index of value in s, or nil if not found.",
            example = "(last-index-of \"hello world world\" \"world\") ; =&gt; 12",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/str.phel#L206",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "str",
        name = "str/lower-case",
        signature = "(lower-case s)",
        completion = CompletionInfo(
            tailText = "Converts string to all lower-case",
            priority = PhelCompletionPriority.STRING_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Converts string to all lower-case.",
            example = "(lower-case \"HELLO World\") ; =&gt; \"hello world\"",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/str.phel#L106",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "str",
        name = "str/pad-both",
        signature = "(pad-both s len & [pad-str])",
        completion = CompletionInfo(
            tailText = "Returns a string padded on both sides to length len",
            priority = PhelCompletionPriority.STRING_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Returns a string padded on both sides to length len.",
            example = "(pad-both \"hello\" 11) ; =&gt; \"   hello   \"",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/str.phel#L245",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "str",
        name = "str/pad-left",
        signature = "(pad-left s len & [pad-str])",
        completion = CompletionInfo(
            tailText = "Returns a string padded on the left side to length len",
            priority = PhelCompletionPriority.STRING_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Returns a string padded on the left side to length len.",
            example = "(pad-left \"hello\" 10) ; =&gt; \"     hello\"",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/str.phel#L233",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "str",
        name = "str/pad-right",
        signature = "(pad-right s len & [pad-str])",
        completion = CompletionInfo(
            tailText = "Returns a string padded on the right side to length len",
            priority = PhelCompletionPriority.STRING_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Returns a string padded on the right side to length len.",
            example = "(pad-right \"hello\" 10) ; =&gt; \"hello     \"",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/str.phel#L239",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "str",
        name = "str/re-quote-replacement",
        signature = "(re-quote-replacement replacement)",
        completion = CompletionInfo(
            tailText = "Escapes special characters in a replacement string for literal use",
            priority = PhelCompletionPriority.STRING_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Escapes special characters in a replacement string for literal use.",
            example = "(re-quote-replacement \"\$1.00\") ; =&gt; \"\\\$1.00\"",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/str.phel#L173",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "str",
        name = "str/repeat",
        signature = "(repeat s n)",
        completion = CompletionInfo(
            tailText = "Returns a string containing n copies of s",
            priority = PhelCompletionPriority.STRING_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Returns a string containing n copies of s.",
            example = "(repeat \"ha\" 3) ; =&gt; \"hahaha\"",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/str.phel#L45",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "str",
        name = "str/replace",
        signature = "(replace s match replacement)",
        completion = CompletionInfo(
            tailText = "Replaces all instances of match with replacement in s",
            priority = PhelCompletionPriority.STRING_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Replaces all instances of match with replacement in s.",
            example = "(replace \"hello world\" \"world\" \"there\") ; =&gt; \"hello there\"",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/str.phel#L51",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "str",
        name = "str/replace-first",
        signature = "(replace-first s match replacement)",
        completion = CompletionInfo(
            tailText = "Replaces the first instance of match with replacement in s",
            priority = PhelCompletionPriority.STRING_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Replaces the first instance of match with replacement in s.",
            example = "(replace-first \"hello world world\" \"world\" \"there\") ; =&gt; \"hello there world\"",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/str.phel#L68",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "str",
        name = "str/reverse",
        signature = "(reverse s)",
        completion = CompletionInfo(
            tailText = "Returns s with its characters reversed",
            priority = PhelCompletionPriority.STRING_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Returns s with its characters reversed.",
            example = "(reverse \"hello\") ; =&gt; \"olleh\"",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/str.phel#L38",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "str",
        name = "str/split",
        signature = "(split s re & [limit])",
        completion = CompletionInfo(
            tailText = "Splits string on a regular expression, returning a vector of parts",
            priority = PhelCompletionPriority.STRING_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Splits string on a regular expression, returning a vector of parts.",
            example = "(split \"hello world foo bar\" #\"\\s+\") ; =&gt; [\"hello\" \"world\" \"foo\" \"bar\"]",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/str.phel#L5",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "str",
        name = "str/split-lines",
        signature = "(split-lines s)",
        completion = CompletionInfo(
            tailText = "Splits s on \\n or \\r\\n",
            priority = PhelCompletionPriority.STRING_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Splits s on \\n or \\r\\n. Trailing empty lines are not returned.",
            example = "(split-lines \"hello\\nworld\\ntest\") ; =&gt; [\"hello\" \"world\" \"test\"]",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/str.phel#L227",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "str",
        name = "str/starts-with?",
        signature = "(starts-with? s substr)",
        completion = CompletionInfo(
            tailText = "True if s starts with substr",
            priority = PhelCompletionPriority.PREDICATE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "True if s starts with substr.",
            example = "(starts-with? \"hello world\" \"hello\") ; =&gt; true",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/str.phel#L149",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "str",
        name = "str/subs",
        signature = "(subs s start & [end])",
        completion = CompletionInfo(
            tailText = "Returns the substring of s from start (inclusive) to end (exclusive)",
            priority = PhelCompletionPriority.STRING_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Returns the substring of s from start (inclusive) to end (exclusive).",
            example = "(subs \"hello world\" 0 5) ; =&gt; \"hello\"",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/str.phel#L28",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "str",
        name = "str/trim",
        signature = "(trim s)",
        completion = CompletionInfo(
            tailText = "Removes whitespace from both ends of string",
            priority = PhelCompletionPriority.STRING_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Removes whitespace from both ends of string.",
            example = "(trim \"  hello  \") ; =&gt; \"hello\"",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/str.phel#L118",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "str",
        name = "str/trim-newline",
        signature = "(trim-newline s)",
        completion = CompletionInfo(
            tailText = "Removes all trailing newline or return characters from string",
            priority = PhelCompletionPriority.STRING_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Removes all trailing newline or return characters from string.",
            example = "(trim-newline \"hello\\n\\n\") ; =&gt; \"hello\"",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/str.phel#L85",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "str",
        name = "str/triml",
        signature = "(triml s)",
        completion = CompletionInfo(
            tailText = "Removes whitespace from the left side of string",
            priority = PhelCompletionPriority.STRING_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Removes whitespace from the left side of string.",
            example = "(triml \"  hello  \") ; =&gt; \"hello  \"",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/str.phel#L124",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "str",
        name = "str/trimr",
        signature = "(trimr s)",
        completion = CompletionInfo(
            tailText = "Removes whitespace from the right side of string",
            priority = PhelCompletionPriority.STRING_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Removes whitespace from the right side of string.",
            example = "(trimr \"  hello  \") ; =&gt; \"  hello\"",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/str.phel#L130",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "str",
        name = "str/upper-case",
        signature = "(upper-case s)",
        completion = CompletionInfo(
            tailText = "Converts string to all upper-case",
            priority = PhelCompletionPriority.STRING_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Converts string to all upper-case.",
            example = "(upper-case \"hello World\") ; =&gt; \"HELLO WORLD\"",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/str.phel#L112",
                docs = "",
            ),
        ),
    )
)
