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
                github = "https://github.com/phel-lang/phel-lang/blob/v0.46.0/src/phel/core/math.phel#L206",
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
Returns the product of all elements in <code>xs</code>. All elements in <code>xs</code> must be numbers. If <code>xs</code> is empty, return 1.
""",
            example = "(* 2 3 4) ; =&gt; 24",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.46.0/src/phel/core/math.phel#L151",
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
            example = "(*' 2 3) ; =&gt; 6",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.46.0/src/phel/core/math.phel#L263",
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
            example = "(** 2 8) ; =&gt; 256",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.46.0/src/phel/core/math.phel#L214",
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
Returns the sum of all elements in <code>xs</code>. All elements <code>xs</code> must be numbers. If <code>xs</code> is empty, return 0.
""",
            example = "(+ 1 2 3) ; =&gt; 6",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.46.0/src/phel/core/math.phel#L127",
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
            example = "(+' 1 2) ; =&gt; 3",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.46.0/src/phel/core/math.phel#L244",
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
Returns the difference of all elements in <code>xs</code>. If <code>xs</code> is empty, return 0. If <code>xs</code> has one element, return the negative value of that element.
""",
            example = "(- 10 3 2) ; =&gt; 5\n(- 4) ; =&gt; -4",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.46.0/src/phel/core/math.phel#L139",
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
            example = "(-' 5 2) ; =&gt; 3",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.46.0/src/phel/core/math.phel#L255",
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
            example = "(/ 1 2) ; =&gt; 1/2\n(/ 10 2) ; =&gt; 5",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.46.0/src/phel/core/math.phel#L163",
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
                github = "https://github.com/phel-lang/phel-lang/blob/v0.46.0/src/phel/core/math.phel#L123",
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
            example = "(NaN? ##NaN) ; =&gt; true",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.46.0/src/phel/core/math.phel#L338",
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
                github = "https://github.com/phel-lang/phel-lang/blob/v0.46.0/src/phel/core/math.phel#L352",
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
Coerces <code>x</code> to a <code>Phel\Lang\BigDecimal</code>. Accepts <code>BigDecimal</code> (returned as-is), ints, floats (via the shortest round-trip decimal of the float), <code>BigInt</code>, <code>Ratio</code> (computed via exact decimal division; throws <code>ArithmeticError</code> when the expansion does not terminate, matching <code>(bigdec 1/3)</code>), and numeric strings.
""",
            example = "(bigdec 1.5) ; =&gt; 1.5M\n(bigdec 1/2) ; =&gt; 0.5M\n(bigdec \"3.14\") ; =&gt; 3.14M",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.46.0/src/phel/core/math.phel#L616",
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
                github = "https://github.com/phel-lang/phel-lang/blob/v0.46.0/src/phel/core/math.phel#L602",
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
            example = "(bigint 42) ; =&gt; 42\n(bigint 1.9) ; =&gt; 1\n(bigint \"123\") ; =&gt; 123",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.46.0/src/phel/core/math.phel#L734",
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
                github = "https://github.com/phel-lang/phel-lang/blob/v0.46.0/src/phel/core/math.phel#L595",
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
            example = "(biginteger 42) ; =&gt; 42",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.46.0/src/phel/core/math.phel#L750",
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
            example = "(bit-and 12 10) ; =&gt; 8",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.46.0/src/phel/core/math.phel#L34",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "bit-clear",
        signature = "(bit-clear x n)",
        completion = CompletionInfo(
            tailText = "Returns the integer x with the bit at index n (0-based, least significant first) cleared to 0",
            priority = PhelCompletionPriority.ARITHMETIC_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns the integer <code>x</code> with the bit at index <code>n</code> (0-based, least significant first) cleared to 0.
""",
            example = "(bit-clear 7 1) ; =&gt; 5",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.46.0/src/phel/core/math.phel#L98",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "bit-flip",
        signature = "(bit-flip x n)",
        completion = CompletionInfo(
            tailText = "Returns the integer x with the bit at index n (0-based, least significant first) toggled",
            priority = PhelCompletionPriority.ARITHMETIC_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns the integer <code>x</code> with the bit at index <code>n</code> (0-based, least significant first) toggled.
""",
            example = "(bit-flip 5 1) ; =&gt; 7",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.46.0/src/phel/core/math.phel#L105",
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
            example = "(bit-not 0) ; =&gt; -1",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.46.0/src/phel/core/math.phel#L64",
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
            example = "(bit-or 12 10) ; =&gt; 14",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.46.0/src/phel/core/math.phel#L44",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "bit-set",
        signature = "(bit-set x n)",
        completion = CompletionInfo(
            tailText = "Returns the integer x with the bit at index n (0-based, least significant first) set to 1",
            priority = PhelCompletionPriority.ARITHMETIC_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns the integer <code>x</code> with the bit at index <code>n</code> (0-based, least significant first) set to 1.
""",
            example = "(bit-set 0 2) ; =&gt; 4",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.46.0/src/phel/core/math.phel#L91",
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
            example = "(bit-shift-left 1 4) ; =&gt; 16",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.46.0/src/phel/core/math.phel#L73",
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
            example = "(bit-shift-right 16 2) ; =&gt; 4",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.46.0/src/phel/core/math.phel#L82",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "bit-test",
        signature = "(bit-test x n)",
        completion = CompletionInfo(
            tailText = "Returns true if the bit at index n (0-based, least significant first) of the integer x is set, fa...",
            priority = PhelCompletionPriority.ARITHMETIC_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns <code>true</code> if the bit at index <code>n</code> (0-based, least significant first) of the integer <code>x</code> is set, <code>false</code> otherwise.
""",
            example = "(bit-test 5 0) ; =&gt; true",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.46.0/src/phel/core/math.phel#L112",
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
            example = "(bit-xor 12 10) ; =&gt; 6",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.46.0/src/phel/core/math.phel#L54",
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
Coerces <code>x</code> to a signed 8-bit integer in the range <code>-128..127</code>. Decimal values are truncated toward zero. <code>Ratio</code> and <code>BigInt</code> values are accepted (truncate toward zero, then range-check). Values outside the range or non-numeric inputs raise <code>InvalidArgumentException</code>. Phel has no dedicated byte type, so the result is a plain PHP int.
""",
            example = "(byte 127) ; =&gt; 127\n(byte 1.9) ; =&gt; 1\n(byte -128) ; =&gt; -128",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.46.0/src/phel/core/math.phel#L437",
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
Returns the smallest integer not less than <code>x</code>. Ints and <code>BigInt</code> values are returned unchanged. Ratios collapse via ceiling division. Floats route through PHP's <code>ceil</code>.
""",
            example = "(ceil 1.2) ; =&gt; 2\n(ceil -1.7) ; =&gt; -1\n(ceil 7/3) ; =&gt; 3",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.46.0/src/phel/core/math.phel#L790",
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
Coerces <code>x</code> to a single-character string representing the given Unicode code point. Accepts a non-negative integer (the code point, converted via <code>mb_chr</code>) or a single-character string, which is returned as-is. Phel has no dedicated char type — character literals such as <code>\A</code> are already single-character strings — so the result is always a plain string. Matches Clojure's <code>char</code> for <code>.cljc</code> interop; raises <code>InvalidArgumentException</code> on negative ints, non-single-character strings, and all other inputs.
""",
            example = "(char 65) ; =&gt; \"A\"\n(char 32) ; =&gt; \" \"\n(char \\A) ; =&gt; \"A\"",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.46.0/src/phel/core/math.phel#L444",
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
            example = "(coerce-in 5 0 10) ; =&gt; 5\n(coerce-in 15 0 10) ; =&gt; 10",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.46.0/src/phel/core/math.phel#L553",
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
            example = "(dec 5) ; =&gt; 4",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.46.0/src/phel/core/math.phel#L230",
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
            example = "(dec' 1) ; =&gt; 0",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.46.0/src/phel/core/math.phel#L279",
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
                github = "https://github.com/phel-lang/phel-lang/blob/v0.46.0/src/phel/core/math.phel#L609",
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
Returns the denominator of <code>r</code>. For rationals the denominator collapses to a PHP int when it fits; integers and <code>BigInt</code> values report <code>1</code>.
""",
            example = "(denominator 1/2) ; =&gt; 2\n(denominator 5) ; =&gt; 1",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.46.0/src/phel/core/math.phel#L654",
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
Coerces <code>x</code> to a double. In PHP there is no distinction between float and double; both map to the same native PHP float type. Alias for <code>float</code>.
""",
            example = "(double 1) ; =&gt; 1",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.46.0/src/phel/core/math.phel#L403",
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
            example = "(even? 4) ; =&gt; true",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.46.0/src/phel/core/math.phel#L287",
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
            example = "(extreme &gt; [1 5 2]) ; =&gt; 5",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.46.0/src/phel/core/math.phel#L494",
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
            example = "(float 1) ; =&gt; 1\n(float 1/2) ; =&gt; 0.5",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.46.0/src/phel/core/math.phel#L384",
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
Returns the largest integer not greater than <code>x</code>. Ints and <code>BigInt</code> values are returned unchanged. Ratios collapse via floor division. Floats route through PHP's <code>floor</code>.
""",
            example = "(floor 1.7) ; =&gt; 1\n(floor -1.2) ; =&gt; -2\n(floor 7/3) ; =&gt; 2",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.46.0/src/phel/core/math.phel#L775",
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
            example = "(inc 1) ; =&gt; 2",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.46.0/src/phel/core/math.phel#L222",
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
            example = "(inc' 1) ; =&gt; 2",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.46.0/src/phel/core/math.phel#L271",
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
                github = "https://github.com/phel-lang/phel-lang/blob/v0.46.0/src/phel/core/math.phel#L345",
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
                github = "https://github.com/phel-lang/phel-lang/blob/v0.46.0/src/phel/core/math.phel#L363",
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
Coerces <code>x</code> to a long integer. In PHP there is no distinction between int and long; both map to the same native PHP int type. Alias for <code>int</code>.
""",
            example = "(long 1.9) ; =&gt; 1",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.46.0/src/phel/core/math.phel#L410",
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
Returns the maximum of all arguments. Returns <code>##NaN</code> whenever any numeric argument is <code>##NaN</code>.
""",
            example = "(max 3 1 2) ; =&gt; 3",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.46.0/src/phel/core/math.phel#L515",
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
                github = "https://github.com/phel-lang/phel-lang/blob/v0.46.0/src/phel/core/math.phel#L539",
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
            example = "(mean [1 2 3]) ; =&gt; 2",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.46.0/src/phel/core/math.phel#L569",
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
Returns the median of <code>xs</code>. With an even-sized collection the result is the float average of the two middle elements.
""",
            example = "(median [3 1 2]) ; =&gt; 2\n(median [1 2 3 4]) ; =&gt; 2.5",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.46.0/src/phel/core/math.phel#L576",
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
Returns the minimum of all arguments. Returns <code>##NaN</code> whenever any numeric argument is <code>##NaN</code>.
""",
            example = "(min 3 1 2) ; =&gt; 1",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.46.0/src/phel/core/math.phel#L505",
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
                github = "https://github.com/phel-lang/phel-lang/blob/v0.46.0/src/phel/core/math.phel#L525",
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
                github = "https://github.com/phel-lang/phel-lang/blob/v0.46.0/src/phel/core/math.phel#L196",
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
            example = "(nan? ##NaN) ; =&gt; true",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.46.0/src/phel/core/math.phel#L329",
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
            example = "(neg? -2) ; =&gt; true",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.46.0/src/phel/core/math.phel#L322",
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
Returns the numerator of <code>r</code>. For rationals the numerator collapses to a PHP int when it fits; for plain ints and <code>BigInt</code> values <code>r</code> is returned unchanged.
""",
            example = "(numerator 1/2) ; =&gt; 1\n(numerator 5) ; =&gt; 5",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.46.0/src/phel/core/math.phel#L634",
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
            example = "(odd? 3) ; =&gt; true",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.46.0/src/phel/core/math.phel#L294",
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
            example = "(one? 1) ; =&gt; true",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.46.0/src/phel/core/math.phel#L308",
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
            example = "(pos? 3) ; =&gt; true",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.46.0/src/phel/core/math.phel#L315",
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
Returns the truncated integer quotient of <code>dividend</code> / <code>divisor</code>. Truncates toward zero. Throws <code>\DivisionByZeroError</code> when <code>divisor</code> is zero.
""",
            example = "(quot 7 3) ; =&gt; 2\n(quot -7 3) ; =&gt; -2",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.46.0/src/phel/core/math.phel#L180",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "rand",
        signature = "(rand)\n(rand n)",
        completion = CompletionInfo(
            tailText = "Without arguments, returns a random number in [0, 1)",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Without arguments, returns a random number in <code>[0, 1)</code>. With one argument <code>n</code>, returns a random number in <code>[0, n)</code>.
""",
            example = "(rand) ; =&gt; 0.42\n(rand 100) ; =&gt; 73.2",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.46.0/src/phel/core/math.phel#L470",
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
            example = "(rand-int 100) ; =&gt; 42",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.46.0/src/phel/core/math.phel#L479",
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
            example = "(rand-nth [:a :b :c]) ; =&gt; :b",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.46.0/src/phel/core/math.phel#L486",
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
Converts <code>x</code> to a <code>Ratio</code>. Floats use the shortest decimal expansion that round-trips back to the same float, so <code>(rationalize 0.1)</code> is <code>1/10</code> rather than the float-noise denominator. Ints, <code>BigInt</code>, and integer-valued <code>BigDecimal</code> values become <code>n/1</code> and auto-collapse, so they are returned as the integer value. <code>BigDecimal</code> with a fractional part rationalizes through its canonical decimal form.
""",
            example = "(rationalize 0.5) ; =&gt; 1/2\n(rationalize 3) ; =&gt; 3\n(rationalize 1M) ; =&gt; 1",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.46.0/src/phel/core/math.phel#L719",
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
Returns the truncated remainder of <code>dividend</code> / <code>divisor</code>. The result has the same sign as <code>dividend</code> (matches PHP's <code>%</code>).
""",
            example = "(rem 7 3) ; =&gt; 1\n(rem -7 3) ; =&gt; -1",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.46.0/src/phel/core/math.phel#L188",
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
Rounds <code>x</code> to the nearest integer using PHP's <code>round</code> (half away from zero). Ints and <code>BigInt</code> values are returned unchanged. Ratios and floats return floats.
""",
            example = "(round 1.5) ; =&gt; 2\n(round -1.5) ; =&gt; -2\n(round 5) ; =&gt; 5",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.46.0/src/phel/core/math.phel#L805",
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
Coerces <code>x</code> to a signed 16-bit integer in the range <code>-32768..32767</code>. Decimal values are truncated toward zero. <code>Ratio</code> and <code>BigInt</code> values are accepted (truncate toward zero, then range-check). Values outside the range or non-numeric inputs raise <code>InvalidArgumentException</code>. Phel has no dedicated short type, so the result is a plain PHP int.
""",
            example = "(short 32767) ; =&gt; 32767\n(short 1.9) ; =&gt; 1\n(short -32768) ; =&gt; -32768",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.46.0/src/phel/core/math.phel#L430",
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
            example = "(sqrt 9) ; =&gt; 3\n(sqrt 2) ; =&gt; 1.4142135623731",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.46.0/src/phel/core/math.phel#L816",
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
            example = "(sum [1 2 3]) ; =&gt; 6",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.46.0/src/phel/core/math.phel#L562",
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
            example = "(zero? 0) ; =&gt; true",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.46.0/src/phel/core/math.phel#L301",
                docs = "",
            ),
        ),
    )
)
