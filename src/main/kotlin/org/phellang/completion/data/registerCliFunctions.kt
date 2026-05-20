package org.phellang.completion.data

import org.phellang.completion.infrastructure.PhelCompletionPriority

internal fun registerCliFunctions(): List<PhelFunction> = listOf(
    PhelFunction(
        namespace = "cli",
        name = "cli/application",
        signature = "(application name-or-spec & [commands])",
        completion = CompletionInfo(
            tailText = "Build a Symfony Application",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Build a Symfony <code>Application</code>.<br /><br />
Single-arity (preferred):<br />
    (application {:name "mytool"<br />
                  :version "1.0.0"<br />
                  :commands [...]<br />
                  :default  "greet"<br />
                  :auto-exit? false<br />
                  :before    on-before-fn   ; receives ConsoleCommandEvent<br />
                  :after     on-after-fn    ; receives ConsoleTerminateEvent<br />
                  :on-error  on-error-fn    ; receives ConsoleErrorEvent<br />
                  :on-signal {:sigint cleanup-fn :sigterm cleanup-fn}})<br /><br />
Two-arity convenience:<br />
    (application "mytool" [command-specs...])
""",
            example = "(application {:name \"mytool\" :commands [spec]})",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.39.0/src/phel/cli.phel#L403",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "cli",
        name = "cli/arg",
        signature = "(arg ctx name)",
        completion = CompletionInfo(
            tailText = "Read (and coerce) an argument value by name",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Read (and coerce) an argument value by name.",
            example = "(arg ctx \"port\") ; coerced when spec has :coerce :int",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.39.0/src/phel/cli.phel#L102",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "cli",
        name = "cli/argv",
        signature = "(argv args)",
        completion = CompletionInfo(
            tailText = "Build an ArgvInput from a vector of string tokens",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Build an <code>ArgvInput</code> from a vector of string tokens. Positional args<br />
  and <code>--flag=value</code> are parsed just like a real shell command line.
""",
            example = "(argv [\"greet\" \"--loud\" \"alice\"])",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.39.0/src/phel/cli.phel#L458",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "cli",
        name = "cli/argv-with-stdin",
        signature = "(argv-with-stdin args stdin-string)",
        completion = CompletionInfo(
            tailText = "Like argv but also wires a STDIN stream so ask/confirm/choice prompts can be tested with canned a...",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Like <code>argv</code> but also wires a STDIN stream so <code>ask</code>/<code>confirm</code>/<code>choice</code><br />
  prompts can be tested with canned answers.
""",
            example = "(argv-with-stdin [\"greet\"] \"alice\\n\")",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.39.0/src/phel/cli.phel#L466",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "cli",
        name = "cli/ask",
        signature = "(ask ctx question & [default])",
        completion = CompletionInfo(
            tailText = "Free-text prompt",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Free-text prompt. <code>default</code> optional.
""",
            example = "(ask ctx \"Your name?\" \"alice\")",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.39.0/src/phel/cli.phel#L167",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "cli",
        name = "cli/ask-hidden",
        signature = "(ask-hidden ctx question)",
        completion = CompletionInfo(
            tailText = "Prompt without echoing (passwords)",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Prompt without echoing (passwords).",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.39.0/src/phel/cli.phel#L174",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "cli",
        name = "cli/buffered-output",
        signature = "(buffered-output)",
        completion = CompletionInfo(
            tailText = "Build a BufferedOutput that captures writes into a string",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Build a <code>BufferedOutput</code> that captures writes into a string.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.39.0/src/phel/cli.phel#L478",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "cli",
        name = "cli/caution",
        signature = "(caution ctx text)",
        completion = CompletionInfo(
            tailText = "Boxed caution message",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Boxed caution message.",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.39.0/src/phel/cli.phel#L164",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "cli",
        name = "cli/choice",
        signature = "(choice ctx question choices & [default])",
        completion = CompletionInfo(
            tailText = "Single-choice prompt over a vector of options",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Single-choice prompt over a vector of options.",
            example = "(choice ctx \"Env?\" [\"dev\" \"stg\" \"prod\"] \"dev\")",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.39.0/src/phel/cli.phel#L186",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "cli",
        name = "cli/command",
        signature = "(command spec)",
        completion = CompletionInfo(
            tailText = "Build a Symfony Command from a Phel spec map",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Build a Symfony <code>Command</code> from a Phel spec map.<br /><br />
Spec keys:<br />
    :name     command name, e.g. "build" or "app:build"     (REQUIRED)<br />
    :doc      short description (shown in <code>list</code>)<br />
    :help     long help text (shown in <code><cmd> --help</code>)<br />
    :aliases  vector of alternate names<br />
    :hidden?  true = hide from <code>list</code><br />
    :args     vector of arg specs <code>{:name :mode :doc :default :coerce :complete}</code><br />
    :opts     vector of option specs <code>{:name :short :mode :doc :default :coerce :complete}</code><br />
    :run      handler <code>(fn [ctx] ...)</code> — ctx = {:input :output …};<br />
              returns nil/0 for success, int for exit code.<br />
              Uncaught <code>Throwable</code> rendered as <code><error></code>, mapped to exit 1.<br /><br />
Modes     :required :optional :array (:args) + :none :negatable (:opts)<br />
  Coerce    :int :float :bool :keyword :edn<br />
  Complete  fn <code>(CompletionInput -> vector<string>)</code> for shell completion.
""",
            example = "(command {:name \"greet\" :run (fn [ctx] (success ctx \"Hi!\"))})",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.39.0/src/phel/cli.phel#L340",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "cli",
        name = "cli/comment-line",
        signature = "(comment-line ctx text)",
        completion = CompletionInfo(
            tailText = "Yellow comment line",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Yellow comment line.",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.39.0/src/phel/cli.phel#L143",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "cli",
        name = "cli/confirm",
        signature = "(confirm ctx question & [default?])",
        completion = CompletionInfo(
            tailText = "Yes/no prompt",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Yes/no prompt. <code>default?</code> defaults to true.
""",
            example = "(when (confirm ctx \"Deploy?\") ...)",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.39.0/src/phel/cli.phel#L179",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "cli",
        name = "cli/debug",
        signature = "(debug ctx text)",
        completion = CompletionInfo(
            tailText = "Write only when -vvv (debug)",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Write only when -vvv (debug).",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.39.0/src/phel/cli.phel#L137",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "cli",
        name = "cli/error",
        signature = "(error ctx text)",
        completion = CompletionInfo(
            tailText = "Red error line",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Red error line.",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.39.0/src/phel/cli.phel#L144",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "cli",
        name = "cli/info",
        signature = "(info ctx text)",
        completion = CompletionInfo(
            tailText = "Green info line",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Green info line.",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.39.0/src/phel/cli.phel#L142",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "cli",
        name = "cli/info-v",
        signature = "(info-v ctx text)",
        completion = CompletionInfo(
            tailText = "Write only when -v or higher",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Write only when -v or higher.",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.39.0/src/phel/cli.phel#L135",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "cli",
        name = "cli/info-vv",
        signature = "(info-vv ctx text)",
        completion = CompletionInfo(
            tailText = "Write only when -vv or higher",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Write only when -vv or higher.",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.39.0/src/phel/cli.phel#L136",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "cli",
        name = "cli/listing",
        signature = "(listing ctx items)",
        completion = CompletionInfo(
            tailText = "Bulleted list of items",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Bulleted list of items.",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.39.0/src/phel/cli.phel#L165",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "cli",
        name = "cli/note",
        signature = "(note ctx text)",
        completion = CompletionInfo(
            tailText = "Boxed note",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Boxed note.",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.39.0/src/phel/cli.phel#L163",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "cli",
        name = "cli/null-output",
        signature = "(null-output)",
        completion = CompletionInfo(
            tailText = "Build a NullOutput that discards writes",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Build a <code>NullOutput</code> that discards writes.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.39.0/src/phel/cli.phel#L482",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "cli",
        name = "cli/opt",
        signature = "(opt ctx name)",
        completion = CompletionInfo(
            tailText = "Read (and coerce) an option value by name",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Read (and coerce) an option value by name.",
            example = "(opt ctx \"verbose\") ; true when --verbose present and :mode :none",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.39.0/src/phel/cli.phel#L110",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "cli",
        name = "cli/output->string",
        signature = "(output->string output)",
        completion = CompletionInfo(
            tailText = "Drain a BufferedOutput into a string",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Drain a <code>BufferedOutput</code> into a string.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.39.0/src/phel/cli.phel#L486",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "cli",
        name = "cli/output-contains?",
        signature = "(output-contains? output-or-result substr)",
        completion = CompletionInfo(
            tailText = "Test helper",
            priority = PhelCompletionPriority.PREDICATE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Test helper. Accepts a <code>BufferedOutput</code>, a string, or a map <code>{:out s}</code>.
""",
            example = "(is (output-contains? res \"hello\"))",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.39.0/src/phel/cli.phel#L491",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "cli",
        name = "cli/progress-advance",
        signature = "(progress-advance bar & [n])",
        completion = CompletionInfo(
            tailText = "Advance by n",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Advance by n.",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.39.0/src/phel/cli.phel#L251",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "cli",
        name = "cli/progress-bar",
        signature = "(progress-bar ctx & [opts])",
        completion = CompletionInfo(
            tailText = "Create a ProgressBar bound to the context output",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Create a <code>ProgressBar</code> bound to the context output.<br /><br />
Options:<br />
    <code>:max</code>    total steps (0 = indeterminate)<br />
    <code>:format</code> preset (:normal :verbose :very-verbose :debug :minimal) or custom string
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.39.0/src/phel/cli.phel#L236",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "cli",
        name = "cli/progress-finish",
        signature = "(progress-finish bar)",
        completion = CompletionInfo(
            tailText = "Finish + newline",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Finish + newline.",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.39.0/src/phel/cli.phel#L252",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "cli",
        name = "cli/progress-start",
        signature = "(progress-start bar)",
        completion = CompletionInfo(
            tailText = "Start the bar",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Start the bar.",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.39.0/src/phel/cli.phel#L250",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "cli",
        name = "cli/run",
        signature = "(run app & [input output])",
        completion = CompletionInfo(
            tailText = "Run an Application and return its exit code",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Run an Application and return its exit code.<br /><br />
<code>(run app)</code>                reads ${'$'}argv from process;<br />
  <code>(run app input)</code>          uses supplied <code>InputInterface</code>;<br />
  <code>(run app input output)</code>   also pipes output (for tests).
""",
            example = "(run (application \"t\" [spec]))",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.39.0/src/phel/cli.phel#L440",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "cli",
        name = "cli/run-with-progress",
        signature = "(run-with-progress ctx coll step-fn & [opts])",
        completion = CompletionInfo(
            tailText = "Iterate coll and invoke (step-fn item bar) for each, handling start / advance / finish automatically",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Iterate <code>coll</code> and invoke <code>(step-fn item bar)</code> for each, handling<br />
  start / advance / finish automatically.
""",
            example = "(run-with-progress ctx files (fn [f _] (process f)))",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.39.0/src/phel/cli.phel#L254",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "cli",
        name = "cli/section",
        signature = "(section ctx text)",
        completion = CompletionInfo(
            tailText = "Sub-heading",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Sub-heading.",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.39.0/src/phel/cli.phel#L160",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "cli",
        name = "cli/stdin-lines",
        signature = "(stdin-lines)",
        completion = CompletionInfo(
            tailText = "Return a vector of lines from STDIN (trailing \\r\\n stripped)",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Return a vector of lines from STDIN (trailing <code>\r\n</code> stripped).<br />
  Useful for <code>cat data.txt | mytool</code>.
""",
            example = "(for [l :in (stdin-lines) :when (not= l \"\")] l)",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.39.0/src/phel/cli.phel#L270",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "cli",
        name = "cli/style",
        signature = "(style ctx)",
        completion = CompletionInfo(
            tailText = "Return a cached SymfonyStyle bound to the context so successive ask/confirm/progress calls share ...",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Return a cached <code>SymfonyStyle</code> bound to the context so successive<br />
  <code>ask</code>/<code>confirm</code>/progress calls share state.
""",
            example = "(-&gt; (style ctx) (php/-&gt; (success \"Done!\")))",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.39.0/src/phel/cli.phel#L150",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "cli",
        name = "cli/success",
        signature = "(success ctx text)",
        completion = CompletionInfo(
            tailText = "Boxed success message",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Boxed success message.",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.39.0/src/phel/cli.phel#L161",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "cli",
        name = "cli/table",
        signature = "(table ctx headers rows & [opts])",
        completion = CompletionInfo(
            tailText = "Render an ASCII table",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Render an ASCII table.<br /><br />
Options:<br />
    <code>:style</code>  one of :default :borderless :compact :symfony :box :box-double :markdown<br />
    <code>:widths</code> vector of column widths
""",
            example = "(table ctx [\"id\" \"name\"] [[1 \"alice\"] [2 \"bob\"]])",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.39.0/src/phel/cli.phel#L206",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "cli",
        name = "cli/title",
        signature = "(title ctx text)",
        completion = CompletionInfo(
            tailText = "Heavy heading",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Heavy heading.",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.39.0/src/phel/cli.phel#L159",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "cli",
        name = "cli/warning",
        signature = "(warning ctx text)",
        completion = CompletionInfo(
            tailText = "Boxed warning message",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Boxed warning message.",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.39.0/src/phel/cli.phel#L162",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "cli",
        name = "cli/write",
        signature = "(write ctx text)",
        completion = CompletionInfo(
            tailText = "Write text without trailing newline",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Write text without trailing newline.",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.39.0/src/phel/cli.phel#L125",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "cli",
        name = "cli/writeln",
        signature = "(writeln ctx text)",
        completion = CompletionInfo(
            tailText = "Write a line",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Write a line.",
            example = "(writeln ctx \"ready\")",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.39.0/src/phel/cli.phel#L118",
                docs = "",
            ),
        ),
    )
)
