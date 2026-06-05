package org.phellang.completion.data.core

import org.phellang.completion.data.CompletionInfo
import org.phellang.completion.data.DocumentationInfo
import org.phellang.completion.data.DocumentationLinks
import org.phellang.completion.data.DeprecationInfo
import org.phellang.completion.data.PhelFunction

import org.phellang.completion.infrastructure.PhelCompletionPriority

internal fun registerCoreMathFunctions(): List<PhelFunction> = listOf(
    PhelFunction(
        namespace = "core",
        name = "%",
        signature = "(% dividend divisor)",
        completion = CompletionInfo(
            tailText = "Alias for rem",
            priority = PhelCompletionPriority.ARITHMETIC_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Alias for <code>rem</code>. Returns the truncated remainder of <code>dividend</code> /<br />
  <code>divisor</code>. Result has the same sign as <code>dividend</code>.
""",
            example = "(% 11 2) ; =&gt; 1",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.41.0/src/phel/core/math.phel#L180",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "*",
        signature = "(* & xs)",
        completion = CompletionInfo(
            tailText = "Returns the product of all elements in xs",
            priority = PhelCompletionPriority.ARITHMETIC_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns the product of all elements in <code>xs</code>. All elements in <code>xs</code> must be<br />
numbers. If <code>xs</code> is empty, return 1.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.41.0/src/phel/core/math.phel#L125",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "*'",
        signature = "(*' & xs)",
        completion = CompletionInfo(
            tailText = "Auto-promoting variant of *",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Auto-promoting variant of <code>*</code>. Integer results are returned as<br />
  <code>BigInt</code>; floats and rationals pass through unchanged.
""",
            example = "(*' 2 3) ; =&gt; 6N",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.41.0/src/phel/core/math.phel#L231",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "**",
        signature = "(** a x)",
        completion = CompletionInfo(
            tailText = "Return a to the power of x",
            priority = PhelCompletionPriority.ARITHMETIC_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Return <code>a</code> to the power of <code>x</code>.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.41.0/src/phel/core/math.phel#L188",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "+",
        signature = "(+ & xs)",
        completion = CompletionInfo(
            tailText = "Returns the sum of all elements in xs",
            priority = PhelCompletionPriority.ARITHMETIC_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns the sum of all elements in <code>xs</code>. All elements <code>xs</code> must be numbers.<br />
  If <code>xs</code> is empty, return 0.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.41.0/src/phel/core/math.phel#L103",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "+'",
        signature = "(+' & xs)",
        completion = CompletionInfo(
            tailText = "Auto-promoting variant of +",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Auto-promoting variant of <code>+</code>. Integer results are returned as<br />
  <code>BigInt</code> so callers get explicit promotion semantics; floats and<br />
  rationals pass through unchanged. Equivalent to <code>+</code> for overflow<br />
  protection (Phel's <code>+</code> already auto-promotes on overflow), kept for<br />
  <code>.cljc</code> interop.
""",
            example = "(+' 1 2) ; =&gt; 3N",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.41.0/src/phel/core/math.phel#L212",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "-",
        signature = "(- & xs)",
        completion = CompletionInfo(
            tailText = "Returns the difference of all elements in xs",
            priority = PhelCompletionPriority.ARITHMETIC_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns the difference of all elements in <code>xs</code>. If <code>xs</code> is empty, return 0. If <code>xs</code><br />
  has one element, return the negative value of that element.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.41.0/src/phel/core/math.phel#L114",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "-'",
        signature = "(-' & xs)",
        completion = CompletionInfo(
            tailText = "Auto-promoting variant of -",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Auto-promoting variant of <code>-</code>. Integer results are returned as<br />
  <code>BigInt</code>; floats and rationals pass through unchanged.
""",
            example = "(-' 5 2) ; =&gt; 3N",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.41.0/src/phel/core/math.phel#L223",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "/",
        signature = "(/ & xs)",
        completion = CompletionInfo(
            tailText = "Returns the nominator divided by all the denominators",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns the nominator divided by all the denominators. If <code>xs</code> is empty,<br />
returns 1. If <code>xs</code> has one value, returns the reciprocal of x.<br /><br />
Integer division with a non-zero remainder returns a <code>Ratio</code><br />
  (e.g. <code>(/ 1 2) => 1/2</code>). Use <code>(/ 1.0 2)</code> or <code>(double ...)</code> for float<br />
  division.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.41.0/src/phel/core/math.phel#L136",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "NAN",
        signature = "",
        completion = CompletionInfo(
            tailText = "Constant for Not a Number (NAN) values",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Constant for Not a Number (NAN) values.",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.41.0/src/phel/core/math.phel#L99",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "NaN?",
        signature = "(NaN? x)",
        completion = CompletionInfo(
            tailText = "Checks if x is not a number",
            priority = PhelCompletionPriority.PREDICATE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Checks if <code>x</code> is not a number. Alias for <code>nan?</code>, matching Clojure's <code>NaN?</code>.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.41.0/src/phel/core/math.phel#L292",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "abs",
        signature = "(abs x)",
        completion = CompletionInfo(
            tailText = "Returns the absolute value of x",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns the absolute value of <code>x</code>.<br />
  Throws <code>InvalidArgumentException</code> if <code>x</code> is not a number; PHP's<br />
  permissive <code>abs(null) => 0</code> / <code>abs("abc")</code> coercions are rejected.
""",
            example = "(abs -5) ; =&gt; 5",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.41.0/src/phel/core/math.phel#L305",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "bigdec",
        signature = "(bigdec x)",
        completion = CompletionInfo(
            tailText = "Coerces x to a Phel\\Lang\\BigDecimal",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Coerces <code>x</code> to a <code>Phel\Lang\BigDecimal</code>. Accepts <code>BigDecimal</code> (returned<br />
  as-is), ints, floats (via the shortest round-trip decimal of the<br />
  float), <code>BigInt</code>, <code>Ratio</code> (computed via exact decimal division;<br />
  throws <code>ArithmeticError</code> when the expansion does not terminate,<br />
  matching <code>(bigdec 1/3)</code>), and numeric strings.
""",
            example = "(bigdec 1.5) ; =&gt; 1.5M\n(bigdec 1/2) ; =&gt; 0.5M\n(bigdec \"3.14\") ; =&gt; 3.14M",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.41.0/src/phel/core/math.phel#L571",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "bigdec?",
        signature = "(bigdec? x)",
        completion = CompletionInfo(
            tailText = "Returns true when x is a Phel\\Lang\\BigDecimal value",
            priority = PhelCompletionPriority.PREDICATE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns true when <code>x</code> is a <code>Phel\Lang\BigDecimal</code> value.
""",
            example = "(bigdec? 1.5M) ; =&gt; true",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.41.0/src/phel/core/math.phel#L557",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "bigint",
        signature = "(bigint x)",
        completion = CompletionInfo(
            tailText = "Coerces x to a Phel\\Lang\\BigInt",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Coerces <code>x</code> to a <code>Phel\Lang\BigInt</code>. Accepts ints, floats<br />
  (truncated toward zero, rejecting <code>NaN</code>/<code>Inf</code>), numeric strings, and<br />
  <code>BigInt</code> values.
""",
            example = "(bigint 42) ; =&gt; 42N\n(bigint 1.9) ; =&gt; 1N\n(bigint \"123\") ; =&gt; 123N",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.41.0/src/phel/core/math.phel#L703",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "bigint?",
        signature = "(bigint? x)",
        completion = CompletionInfo(
            tailText = "Returns true when x is a Phel\\Lang\\BigInt value",
            priority = PhelCompletionPriority.PREDICATE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns true when <code>x</code> is a <code>Phel\Lang\BigInt</code> value.
""",
            example = "(bigint? (php/:: \\Phel\\Lang\\BigInt (fromInt 1))) ; =&gt; true",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.41.0/src/phel/core/math.phel#L550",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "biginteger",
        signature = "(biginteger x)",
        completion = CompletionInfo(
            tailText = "Alias for bigint",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Alias for <code>bigint</code>. Coerces <code>x</code> to a <code>Phel\Lang\BigInt</code>.
""",
            example = "(biginteger 42) ; =&gt; 42N",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.41.0/src/phel/core/math.phel#L719",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "bit-and",
        signature = "(bit-and x y & args)",
        completion = CompletionInfo(
            tailText = "Bitwise and",
            priority = PhelCompletionPriority.ARITHMETIC_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Bitwise and.",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.41.0/src/phel/core/math.phel#L30",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "bit-clear",
        signature = "(bit-clear x n)",
        completion = CompletionInfo(
            tailText = "Clear bit an index n",
            priority = PhelCompletionPriority.ARITHMETIC_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Clear bit an index <code>n</code>.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.41.0/src/phel/core/math.phel#L80",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "bit-flip",
        signature = "(bit-flip x n)",
        completion = CompletionInfo(
            tailText = "Flip bit at index n",
            priority = PhelCompletionPriority.ARITHMETIC_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Flip bit at index <code>n</code>.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.41.0/src/phel/core/math.phel#L85",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "bit-not",
        signature = "(bit-not x)",
        completion = CompletionInfo(
            tailText = "Bitwise complement",
            priority = PhelCompletionPriority.ARITHMETIC_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Bitwise complement.",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.41.0/src/phel/core/math.phel#L54",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "bit-or",
        signature = "(bit-or x y & args)",
        completion = CompletionInfo(
            tailText = "Bitwise or",
            priority = PhelCompletionPriority.ARITHMETIC_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Bitwise or.",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.41.0/src/phel/core/math.phel#L38",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "bit-set",
        signature = "(bit-set x n)",
        completion = CompletionInfo(
            tailText = "Set bit an index n",
            priority = PhelCompletionPriority.ARITHMETIC_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Set bit an index <code>n</code>.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.41.0/src/phel/core/math.phel#L75",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "bit-shift-left",
        signature = "(bit-shift-left x n)",
        completion = CompletionInfo(
            tailText = "Bitwise shift left",
            priority = PhelCompletionPriority.ARITHMETIC_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Bitwise shift left.",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.41.0/src/phel/core/math.phel#L61",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "bit-shift-right",
        signature = "(bit-shift-right x n)",
        completion = CompletionInfo(
            tailText = "Bitwise shift right",
            priority = PhelCompletionPriority.ARITHMETIC_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Bitwise shift right.",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.41.0/src/phel/core/math.phel#L68",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "bit-test",
        signature = "(bit-test x n)",
        completion = CompletionInfo(
            tailText = "Test bit at index n",
            priority = PhelCompletionPriority.ARITHMETIC_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Test bit at index <code>n</code>.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.41.0/src/phel/core/math.phel#L90",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "bit-xor",
        signature = "(bit-xor x y & args)",
        completion = CompletionInfo(
            tailText = "Bitwise xor",
            priority = PhelCompletionPriority.ARITHMETIC_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Bitwise xor.",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.41.0/src/phel/core/math.phel#L46",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "byte",
        signature = "(byte x)",
        completion = CompletionInfo(
            tailText = "Coerces x to a signed 8-bit integer in the range -128",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Coerces <code>x</code> to a signed 8-bit integer in the range <code>-128..127</code>.<br />
   Decimal values are truncated toward zero. <code>Ratio</code> and <code>BigInt</code><br />
   values are accepted (truncate toward zero, then range-check). Values<br />
   outside the range or non-numeric inputs raise <code>InvalidArgumentException</code>.<br />
   Phel has no dedicated byte type, so the result is a plain PHP int.
""",
            example = "(byte 127) ; =&gt; 127\n(byte 1.9) ; =&gt; 1\n(byte -128) ; =&gt; -128",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.41.0/src/phel/core/math.phel#L389",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "ceil",
        signature = "(ceil x)",
        completion = CompletionInfo(
            tailText = "Returns the smallest integer not less than x",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns the smallest integer not less than <code>x</code>. Ints and <code>BigInt</code><br />
  values are returned unchanged. Ratios collapse via ceiling division.<br />
  Floats route through PHP's <code>ceil</code>.
""",
            example = "(ceil 1.2) ; =&gt; 2.0\n(ceil -1.7) ; =&gt; -1.0\n(ceil 7/3) ; =&gt; 3",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.41.0/src/phel/core/math.phel#L761",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "char",
        signature = "(char x)",
        completion = CompletionInfo(
            tailText = "Coerces x to a single-character string representing the given Unicode code point",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Coerces <code>x</code> to a single-character string representing the given<br />
   Unicode code point. Accepts a non-negative integer (the code point,<br />
   converted via <code>mb_chr</code>) or a single-character string, which is<br />
   returned as-is. Phel has no dedicated char type — character literals<br />
   such as <code>\A</code> are already single-character strings — so the result<br />
   is always a plain string. Matches Clojure's <code>char</code> for <code>.cljc</code><br />
   interop; raises <code>InvalidArgumentException</code> on negative ints,<br />
   non-single-character strings, and all other inputs.
""",
            example = "(char 65) ; =&gt; \"A\"\n(char 32) ; =&gt; \" \"\n(char \\A) ; =&gt; \"A\"",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.41.0/src/phel/core/math.phel#L406",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "coerce-in",
        signature = "(coerce-in v min max)",
        completion = CompletionInfo(
            tailText = "Returns v if it is in the range, or min if v is less than min, or max if v is greater than max",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns <code>v</code> if it is in the range, or <code>min</code> if <code>v</code> is less than <code>min</code>, or <code>max</code> if <code>v</code> is greater than <code>max</code>.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.41.0/src/phel/core/math.phel#L515",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "dec",
        signature = "(dec x)",
        completion = CompletionInfo(
            tailText = "Decrements x by one",
            priority = PhelCompletionPriority.ARITHMETIC_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Decrements <code>x</code> by one.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.41.0/src/phel/core/math.phel#L200",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "dec'",
        signature = "(dec' x)",
        completion = CompletionInfo(
            tailText = "Auto-promoting variant of dec",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Auto-promoting variant of <code>dec</code>. Integer results are returned as<br />
  <code>BigInt</code>; floats and rationals pass through unchanged.
""",
            example = "(dec' 1) ; =&gt; 0N",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.41.0/src/phel/core/math.phel#L247",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "decimal?",
        signature = "(decimal? x)",
        completion = CompletionInfo(
            tailText = "Alias for bigdec",
            priority = PhelCompletionPriority.PREDICATE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Alias for <code>bigdec?</code>. Returns true when <code>x</code> is a <code>Phel\Lang\BigDecimal</code>.
""",
            example = "(decimal? 1.5M) ; =&gt; true",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.41.0/src/phel/core/math.phel#L564",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "denominator",
        signature = "(denominator r)",
        completion = CompletionInfo(
            tailText = "Returns the denominator of r",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns the denominator of <code>r</code>. For rationals the denominator collapses<br />
  to a PHP int when it fits; integers and <code>BigInt</code> values report <code>1</code>.
""",
            example = "(denominator 1/2) ; =&gt; 2\n(denominator 5) ; =&gt; 1",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.41.0/src/phel/core/math.phel#L615",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "double",
        signature = "(double x)",
        completion = CompletionInfo(
            tailText = "Coerces x to a double",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Coerces <code>x</code> to a double. In PHP there is no distinction between float and<br />
   double; both map to the same native PHP float type. Alias for <code>float</code>.
""",
            example = "(double 1) ; =&gt; 1.0",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.41.0/src/phel/core/math.phel#L356",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "even?",
        signature = "(even? x)",
        completion = CompletionInfo(
            tailText = "Checks if x is even",
            priority = PhelCompletionPriority.PREDICATE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Checks if <code>x</code> is even.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.41.0/src/phel/core/math.phel#L255",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "extreme",
        signature = "(extreme order args)",
        completion = CompletionInfo(
            tailText = "Returns the most extreme value in args based on the binary order function",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns the most extreme value in <code>args</code> based on the binary <code>order</code> function.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.41.0/src/phel/core/math.phel#L460",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "float",
        signature = "(float x)",
        completion = CompletionInfo(
            tailText = "Coerces x to a float",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Coerces <code>x</code> to a float. In PHP there is no distinction between float and<br />
   double; both map to the same native PHP float type. <code>Ratio</code>,<br />
   <code>BigInt</code>, and <code>BigDecimal</code> values collapse to their float value;<br />
   everything else delegates to PHP's <code>floatval</code>, so non-numeric strings<br />
   return <code>0.0</code> and <code>nil</code> returns <code>0.0</code>. Non-numeric objects such as<br />
   keywords, vectors, or maps raise <code>InvalidArgumentException</code> instead of<br />
   leaking a raw PHP float-coercion warning.
""",
            example = "(float 1) ; =&gt; 1.0\n(float 1/2) ; =&gt; 0.5",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.41.0/src/phel/core/math.phel#L337",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "floor",
        signature = "(floor x)",
        completion = CompletionInfo(
            tailText = "Returns the largest integer not greater than x",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns the largest integer not greater than <code>x</code>. Ints and <code>BigInt</code><br />
  values are returned unchanged. Ratios collapse via floor division.<br />
  Floats route through PHP's <code>floor</code>.
""",
            example = "(floor 1.7) ; =&gt; 1.0\n(floor -1.2) ; =&gt; -2.0\n(floor 7/3) ; =&gt; 2",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.41.0/src/phel/core/math.phel#L744",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "inc",
        signature = "(inc x)",
        completion = CompletionInfo(
            tailText = "Increments x by one",
            priority = PhelCompletionPriority.ARITHMETIC_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Increments <code>x</code> by one.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.41.0/src/phel/core/math.phel#L194",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "inc'",
        signature = "(inc' x)",
        completion = CompletionInfo(
            tailText = "Auto-promoting variant of inc",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Auto-promoting variant of <code>inc</code>. Integer results are returned as<br />
  <code>BigInt</code>; floats and rationals pass through unchanged.
""",
            example = "(inc' 1) ; =&gt; 2N",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.41.0/src/phel/core/math.phel#L239",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "inf?",
        signature = "(inf? x)",
        completion = CompletionInfo(
            tailText = "Checks if x is infinite",
            priority = PhelCompletionPriority.PREDICATE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Checks if <code>x</code> is infinite.
""",
            example = "(inf? php/INF) ; =&gt; true",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.41.0/src/phel/core/math.phel#L298",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "int",
        signature = "(int x)",
        completion = CompletionInfo(
            tailText = "Coerces x to an integer",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Coerces <code>x</code> to an integer. <code>Ratio</code> and <code>BigDecimal</code> values truncate<br />
   toward zero (<code>(int 1/10)</code> is <code>0</code>, <code>(int -1.1M)</code> is <code>-1</code>); <code>BigInt</code><br />
   and <code>float</code> values collapse to their PHP int value or throw<br />
   <code>OverflowException</code> when outside the PHP int range (<code>NaN</code>/<code>Inf</code> floats<br />
   raise <code>InvalidArgumentException</code>). Numeric strings are parsed via PHP's<br />
   <code>intval</code>, and <code>nil</code> returns <code>0</code>. Non-numeric objects such as keywords,<br />
   vectors, or maps raise <code>InvalidArgumentException</code> instead of leaking a<br />
   raw PHP int-coercion warning.
""",
            example = "(int 1.9) ; =&gt; 1\n(int \"42\") ; =&gt; 42\n(int 1/10) ; =&gt; 0",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.41.0/src/phel/core/math.phel#L316",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "long",
        signature = "(long x)",
        completion = CompletionInfo(
            tailText = "Coerces x to a long integer",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Coerces <code>x</code> to a long integer. In PHP there is no distinction between int<br />
   and long; both map to the same native PHP int type. Alias for <code>int</code>.
""",
            example = "(long 1.9) ; =&gt; 1",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.41.0/src/phel/core/math.phel#L364",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "max",
        signature = "(max & numbers)",
        completion = CompletionInfo(
            tailText = "Returns the maximum of all arguments",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns the maximum of all arguments. Returns <code>##NaN</code> whenever any<br />
  numeric argument is <code>##NaN</code>.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.41.0/src/phel/core/math.phel#L478",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "max-key",
        signature = "(max-key k x & more)",
        completion = CompletionInfo(
            tailText = "Returns the arg for which (k arg) is largest",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns the arg for which (k arg) is largest. On ties, returns the latest argument.
""",
            example = "(max-key count \"bb\" \"aaa\" \"b\") ; =&gt; \"aaa\"",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.41.0/src/phel/core/math.phel#L501",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "mean",
        signature = "(mean xs)",
        completion = CompletionInfo(
            tailText = "Returns the mean of xs as a float",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns the mean of <code>xs</code> as a float.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.41.0/src/phel/core/math.phel#L527",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "median",
        signature = "(median xs)",
        completion = CompletionInfo(
            tailText = "Returns the median of xs",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns the median of <code>xs</code>. With an even-sized collection the result is<br />
  the float average of the two middle elements.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.41.0/src/phel/core/math.phel#L532",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "min",
        signature = "(min & numbers)",
        completion = CompletionInfo(
            tailText = "Returns the minimum of all arguments",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns the minimum of all arguments. Returns <code>##NaN</code> whenever any<br />
  numeric argument is <code>##NaN</code>.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.41.0/src/phel/core/math.phel#L469",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "min-key",
        signature = "(min-key k x & more)",
        completion = CompletionInfo(
            tailText = "Returns the arg for which (k arg) is smallest",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns the arg for which (k arg) is smallest. On ties, returns the latest argument.
""",
            example = "(min-key count \"bb\" \"aaa\" \"b\") ; =&gt; \"b\"",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.41.0/src/phel/core/math.phel#L487",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "mod",
        signature = "(mod dividend divisor)",
        completion = CompletionInfo(
            tailText = "Returns the floor remainder of dividend / divisor",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns the floor remainder of <code>dividend</code> / <code>divisor</code>. The result has<br />
  the same sign as <code>divisor</code>. Differs from <code>rem</code> on mixed-sign operands:<br />
  <code>(mod -7 3) => 2</code> while <code>(rem -7 3) => -1</code>.
""",
            example = "(mod 7 3) ; =&gt; 1\n(mod -7 3) ; =&gt; 2",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.41.0/src/phel/core/math.phel#L170",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "nan?",
        signature = "(nan? x)",
        completion = CompletionInfo(
            tailText = "Checks if x is not a number",
            priority = PhelCompletionPriority.PREDICATE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Checks if <code>x</code> is not a number.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.41.0/src/phel/core/math.phel#L285",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "neg?",
        signature = "(neg? x)",
        completion = CompletionInfo(
            tailText = "Checks if x is smaller than zero",
            priority = PhelCompletionPriority.PREDICATE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Checks if <code>x</code> is smaller than zero.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.41.0/src/phel/core/math.phel#L280",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "numerator",
        signature = "(numerator r)",
        completion = CompletionInfo(
            tailText = "Returns the numerator of r",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns the numerator of <code>r</code>. For rationals the numerator collapses to a<br />
  PHP int when it fits; for plain ints and <code>BigInt</code> values <code>r</code> is returned<br />
  unchanged.
""",
            example = "(numerator 1/2) ; =&gt; 1\n(numerator 5) ; =&gt; 5",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.41.0/src/phel/core/math.phel#L593",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "odd?",
        signature = "(odd? x)",
        completion = CompletionInfo(
            tailText = "Checks if x is odd",
            priority = PhelCompletionPriority.PREDICATE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Checks if <code>x</code> is odd.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.41.0/src/phel/core/math.phel#L260",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "one?",
        signature = "(one? x)",
        completion = CompletionInfo(
            tailText = "Checks if x is one",
            priority = PhelCompletionPriority.PREDICATE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Checks if <code>x</code> is one.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.41.0/src/phel/core/math.phel#L270",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "pos?",
        signature = "(pos? x)",
        completion = CompletionInfo(
            tailText = "Checks if x is greater than zero",
            priority = PhelCompletionPriority.PREDICATE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Checks if <code>x</code> is greater than zero.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.41.0/src/phel/core/math.phel#L275",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "quot",
        signature = "(quot dividend divisor)",
        completion = CompletionInfo(
            tailText = "Returns the truncated integer quotient of dividend / divisor",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns the truncated integer quotient of <code>dividend</code> / <code>divisor</code>.<br />
  Truncates toward zero. Throws <code>\DivisionByZeroError</code> when <code>divisor</code><br />
  is zero.
""",
            example = "(quot 7 3) ; =&gt; 2\n(quot -7 3) ; =&gt; -2",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.41.0/src/phel/core/math.phel#L151",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "rand",
        signature = "(rand)",
        completion = CompletionInfo(
            tailText = "Without arguments, returns a random number in [0, 1)",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Without arguments, returns a random number in <code>[0, 1)</code>. With one<br />
  argument <code>n</code>, returns a random number in <code>[0, n)</code>.
""",
            example = "(rand) ; =&gt; 0.42\n(rand 100) ; =&gt; 73.2",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.41.0/src/phel/core/math.phel#L439",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "rand-int",
        signature = "(rand-int n)",
        completion = CompletionInfo(
            tailText = "Returns a random number between 0 and n",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns a random number between 0 and <code>n</code>.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.41.0/src/phel/core/math.phel#L449",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "rand-nth",
        signature = "(rand-nth xs)",
        completion = CompletionInfo(
            tailText = "Returns a random item from xs",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Returns a random item from xs.",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.41.0/src/phel/core/math.phel#L454",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "rationalize",
        signature = "(rationalize x)",
        completion = CompletionInfo(
            tailText = "Converts x to a Ratio",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Converts <code>x</code> to a <code>Ratio</code>. Floats use the shortest decimal expansion<br />
  that round-trips back to the same float, so <code>(rationalize 0.1)</code> is <code>1/10</code><br />
  rather than the float-noise denominator. Ints, <code>BigInt</code>, and<br />
  integer-valued <code>BigDecimal</code> values become <code>n/1</code> and auto-collapse, so<br />
  they are returned as the integer value. <code>BigDecimal</code> with a fractional<br />
  part rationalizes through its canonical decimal form.
""",
            example = "(rationalize 0.5) ; =&gt; 1/2\n(rationalize 3) ; =&gt; 3\n(rationalize 1M) ; =&gt; 1",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.41.0/src/phel/core/math.phel#L683",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "rem",
        signature = "(rem dividend divisor)",
        completion = CompletionInfo(
            tailText = "Returns the truncated remainder of dividend / divisor",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns the truncated remainder of <code>dividend</code> / <code>divisor</code>. The result<br />
  has the same sign as <code>dividend</code> (matches PHP's <code>%</code>).
""",
            example = "(rem 7 3) ; =&gt; 1\n(rem -7 3) ; =&gt; -1",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.41.0/src/phel/core/math.phel#L161",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "round",
        signature = "(round x)",
        completion = CompletionInfo(
            tailText = "Rounds x to the nearest integer using PHP's round (half away from zero)",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Rounds <code>x</code> to the nearest integer using PHP's <code>round</code> (half away from<br />
  zero). Ints and <code>BigInt</code> values are returned unchanged. Ratios<br />
  and floats return floats.
""",
            example = "(round 1.5) ; =&gt; 2.0\n(round -1.5) ; =&gt; -2.0\n(round 5) ; =&gt; 5",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.41.0/src/phel/core/math.phel#L778",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "short",
        signature = "(short x)",
        completion = CompletionInfo(
            tailText = "Coerces x to a signed 16-bit integer in the range -32768",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Coerces <code>x</code> to a signed 16-bit integer in the range <code>-32768..32767</code>.<br />
   Decimal values are truncated toward zero. <code>Ratio</code> and <code>BigInt</code><br />
   values are accepted (truncate toward zero, then range-check). Values<br />
   outside the range or non-numeric inputs raise <code>InvalidArgumentException</code>.<br />
   Phel has no dedicated short type, so the result is a plain PHP int.
""",
            example = "(short 32767) ; =&gt; 32767\n(short 1.9) ; =&gt; 1\n(short -32768) ; =&gt; -32768",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.41.0/src/phel/core/math.phel#L372",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "sqrt",
        signature = "(sqrt x)",
        completion = CompletionInfo(
            tailText = "Returns the square root of x as a float",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns the square root of <code>x</code> as a float. Negative inputs return<br />
  <code>##NaN</code> (matches PHP's <code>sqrt</code>).
""",
            example = "(sqrt 9) ; =&gt; 3.0\n(sqrt 2) ; =&gt; 1.4142135623730951",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.41.0/src/phel/core/math.phel#L791",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "sum",
        signature = "(sum xs)",
        completion = CompletionInfo(
            tailText = "Returns the sum of all elements is xs",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns the sum of all elements is <code>xs</code>.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.41.0/src/phel/core/math.phel#L522",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "zero?",
        signature = "(zero? x)",
        completion = CompletionInfo(
            tailText = "Checks if x is zero",
            priority = PhelCompletionPriority.PREDICATE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Checks if <code>x</code> is zero.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.41.0/src/phel/core/math.phel#L265",
                docs = "",
            ),
        ),
    )
)
