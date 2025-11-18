package org.phellang.completion.data

import org.phellang.completion.infrastructure.PhelCompletionPriority

internal fun registerStringFunctions(): List<DataFunction> = listOf(
    DataFunction("str/blank?", "(blank? s)", PhelCompletionPriority.STRING_FUNCTIONS, "str", "True if s is nil, empty, or contains only whitespace", """
<br /><code>(blank? s)</code><br /><br />
True if s is nil, empty, or contains only whitespace.<br />
<br />
"""),
    DataFunction("str/capitalize", "(capitalize s)", PhelCompletionPriority.STRING_FUNCTIONS, "str", "Converts first character to upper-case and all other characters to lower-case", """
<br /><code>(capitalize s)</code><br /><br />
Converts first character to upper-case and all other characters to lower-case.<br />
<br />
  <pre><code>(capitalize \"hELLO wORLD\")<br /># => \"Hello world\"</code></pre>
<br />
"""),
    DataFunction("str/contains?", "(contains? s substr)", PhelCompletionPriority.STRING_FUNCTIONS, "str", "True if s contains substr", """
<br /><code>(contains? s substr)</code><br /><br />
True if s contains substr.<br />
<br />
"""),
    DataFunction("str/ends-with?", "(ends-with? s substr)", PhelCompletionPriority.STRING_FUNCTIONS, "str", "True if s ends with substr", """
<br /><code>(ends-with? s substr)</code><br /><br />
True if s ends with substr.<br />
<br />
"""),
    DataFunction("str/escape", "(escape s cmap)", PhelCompletionPriority.STRING_FUNCTIONS, "str", "Returns a new string with each character escaped according to cmap", """
<br /><code>(escape s cmap)</code><br /><br />
Returns a new string with each character escaped according to cmap.<br />
<br />
  <pre><code>(escape \"hello\" {\"h\" \"H\" \"o\" \"O\"})<br /># => \"HellO\"</code></pre>
<br />
"""),
    DataFunction("str/includes?", "(includes? s substr)", PhelCompletionPriority.STRING_FUNCTIONS, "str", "True if s includes substr", """
<br /><code>(includes? s substr)</code><br /><br />
True if s includes substr.<br />
<br />
"""),
    DataFunction("str/index-of", "(index-of s value & [from-index])", PhelCompletionPriority.STRING_FUNCTIONS, "str", "Returns the index of value in s, or nil if not found", """
<br /><code>(index-of s value & [from-index])</code><br /><br />
Returns the index of value in s, or nil if not found.<br />
<br />
  <pre><code>(index-of \"hello world\" \"world\")<br /># => 6</code></pre>
<br />
"""),
    DataFunction("str/join", "(join separator & [coll])", PhelCompletionPriority.STRING_FUNCTIONS, "str", "Returns a string of all elements in coll, separated by an optional separator", """
<br /><code>(join separator & [coll])</code><br /><br />
Returns a string of all elements in coll, separated by an optional separator.<br />
<br />
  <pre><code>(join \", \" [\"apple\" \"banana\" \"cherry\"])<br /># => \"apple, banana, cherry\"</code></pre>
<br />
"""),
    DataFunction("str/last-index-of", "(last-index-of s value & [from-index])", PhelCompletionPriority.STRING_FUNCTIONS, "str", "Returns the last index of value in s, or nil if not found", """
<br /><code>(last-index-of s value & [from-index])</code><br /><br />
Returns the last index of value in s, or nil if not found.<br />
<br />
  <pre><code>(last-index-of \"hello world world\" \"world\")<br /># => 12</code></pre>
<br />
"""),
    DataFunction("str/lower-case", "(lower-case s)", PhelCompletionPriority.STRING_FUNCTIONS, "str", "Converts string to all lower-case", """
<br /><code>(lower-case s)</code><br /><br />
Converts string to all lower-case.<br />
<br />
  <pre><code>(lower-case \"HELLO World\")<br /># => \"hello world\"</code></pre>
<br />
"""),
    DataFunction("str/pad-both", "(pad-both s len & [pad-str])", PhelCompletionPriority.STRING_FUNCTIONS, "str", "Returns a string padded on both sides to length len", """
<br /><code>(pad-both s len & [pad-str])</code><br /><br />
Returns a string padded on both sides to length len.<br />
<br />
  <pre><code>(pad-both \"hello\" 11)<br /># => \"   hello   \"</code></pre>
<br />
"""),
    DataFunction("str/pad-left", "(pad-left s len & [pad-str])", PhelCompletionPriority.STRING_FUNCTIONS, "str", "Returns a string padded on the left side to length len", """
<br /><code>(pad-left s len & [pad-str])</code><br /><br />
Returns a string padded on the left side to length len.<br />
<br />
  <pre><code>(pad-left \"hello\" 10)<br /># => \"     hello\"</code></pre>
<br />
"""),
    DataFunction("str/pad-right", "(pad-right s len & [pad-str])", PhelCompletionPriority.STRING_FUNCTIONS, "str", "Returns a string padded on the right side to length len", """
<br /><code>(pad-right s len & [pad-str])</code><br /><br />
Returns a string padded on the right side to length len.<br />
<br />
  <pre><code>(pad-right \"hello\" 10)<br /># => \"hello     \"</code></pre>
<br />
"""),
    DataFunction("str/re-quote-replacement", "(re-quote-replacement replacement)", PhelCompletionPriority.STRING_FUNCTIONS, "str", "Escapes special characters in a replacement string for literal use", """
<br /><code>(re-quote-replacement replacement)</code><br /><br />
Escapes special characters in a replacement string for literal use.<br />
<br />
  <pre><code>(re-quote-replacement \"$1.00\")<br /># => \"\$1.00\"</code></pre>
<br />
"""),
    DataFunction("str/repeat", "(repeat s n)", PhelCompletionPriority.STRING_FUNCTIONS, "str", "Returns a string containing n copies of s", """
<br /><code>(repeat s n)</code><br /><br />
Returns a string containing n copies of s.<br />
<br />
  <pre><code>(repeat \"ha\" 3)<br /># => \"hahaha\"</code></pre>
<br />
"""),
    DataFunction("str/replace", "(replace s match replacement)", PhelCompletionPriority.STRING_FUNCTIONS, "str", "Replaces all instances of match with replacement in s", """
<br /><code>(replace s match replacement)</code><br /><br />
Replaces all instances of match with replacement in s.<br />
<br />
  <pre><code>(replace \"hello world\" \"world\" \"there\")<br /># => \"hello there\"</code></pre>
<br />
"""),
    DataFunction("str/replace-first", "(replace-first s match replacement)", PhelCompletionPriority.STRING_FUNCTIONS, "str", "Replaces the first instance of match with replacement in s", """
<br /><code>(replace-first s match replacement)</code><br /><br />
Replaces the first instance of match with replacement in s.<br />
<br />
  <pre><code>(replace-first \\"hello world world\\" \\"world\\" \\"there\\")<br /># => \\"hello there world\\"</code></pre>
<br />
"""),
    DataFunction("str/reverse", "(reverse s)", PhelCompletionPriority.STRING_FUNCTIONS, "str", "Returns s with its characters reversed", """
<br /><code>(reverse s)</code><br /><br />
Returns s with its characters reversed.<br />
<br />
  <pre><code>(reverse \"hello\")<br /># => \"olleh\"</code></pre>
<br />
"""),
    DataFunction("str/split", "(split s re & [limit])", PhelCompletionPriority.STRING_FUNCTIONS, "str", "Splits string on a regular expression, returning a vector of parts", """
<br /><code>(split s re & [limit])</code><br /><br />
Splits string on a regular expression, returning a vector of parts.<br />
<br />
  <pre><code>(split \"hello world foo bar\" #\"\s+\")<br /># => [\"hello\" \"world\" \"foo\" \"bar\"]</code></pre>
<br />
"""),
    DataFunction("str/split-lines", "(split-lines s)", PhelCompletionPriority.STRING_FUNCTIONS, "str", "Splits s on \n or \r\n. Trailing empty lines are not returned", """
<br /><code>(split-lines s)</code><br /><br />
Splits s on \n or \r\n. Trailing empty lines are not returned.<br />
<br />
"""),
    DataFunction("str/starts-with?", "(starts-with? s substr)", PhelCompletionPriority.STRING_FUNCTIONS, "str", "True if s starts with substr", """
<br /><code>(starts-with? s substr)</code><br /><br />
True if s starts with substr.<br />
<br />
"""),
    DataFunction("str/subs", "(subs s start & [end])", PhelCompletionPriority.STRING_FUNCTIONS, "str", "Returns the substring of s from start (inclusive) to end (exclusive)", """
<br /><code>(subs s start & [end])</code><br /><br />
Returns the substring of s from start (inclusive) to end (exclusive).<br />
<br />
  <pre><code>(subs \"hello world\" 0 5)<br /># => \"hello\"</code></pre>
<br />
"""),
    DataFunction("str/trim", "(trim s)", PhelCompletionPriority.STRING_FUNCTIONS, "str", "Removes whitespace from both ends of string", """
<br /><code>(trim s)</code><br /><br />
Removes whitespace from both ends of string.<br />
<br />
"""),
    DataFunction("str/trim-newline", "(trim-newline s)", PhelCompletionPriority.STRING_FUNCTIONS, "str", "Removes all trailing newline or return characters from string", """
<br /><code>(trim-newline s)</code><br /><br />
Removes all trailing newline or return characters from string.<br />
<br />
  <pre><code>(trim-newline \"hello\n\n\")<br /># => \"hello\"</code></pre>
<br />
"""),
    DataFunction("str/triml", "(triml s)", PhelCompletionPriority.STRING_FUNCTIONS, "str", "Removes whitespace from the left side of string", """
<br /><code>(triml s)</code><br /><br />
Removes whitespace from the left side of string.<br />
<br />
"""),
    DataFunction("str/trimr", "(trimr s)", PhelCompletionPriority.STRING_FUNCTIONS, "str", "Removes whitespace from the right side of string", """
<br /><code>(trimr s)</code><br /><br />
Removes whitespace from the right side of string.<br />
<br />
"""),
    DataFunction("str/upper-case", "(upper-case s)", PhelCompletionPriority.STRING_FUNCTIONS, "str", "Converts string to all upper-case", """
<br /><code>(upper-case s)</code><br /><br />
Converts string to all upper-case.<br />
<br />
  <pre><code>(upper-case \"hello World\")<br /># => \"HELLO WORLD\"</code></pre>
<br />
"""),
)
