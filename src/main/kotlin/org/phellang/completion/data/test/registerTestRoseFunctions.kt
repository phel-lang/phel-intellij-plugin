package org.phellang.completion.data.test

import org.phellang.completion.data.CompletionInfo
import org.phellang.completion.data.DocumentationInfo
import org.phellang.completion.data.DocumentationLinks
import org.phellang.completion.data.DeprecationInfo
import org.phellang.completion.data.PhelFunction

import org.phellang.completion.infrastructure.PhelCompletionPriority

internal fun registerTestRoseFunctions(): List<PhelFunction> = listOf(
    PhelFunction(
        namespace = "test.rose",
        name = "test.rose/int-rose-tree",
        signature = "(int-rose-tree n)",
        completion = CompletionInfo(
            tailText = "Rose tree for an integer n that shrinks toward zero",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Rose tree for an integer <code>n</code> that shrinks toward zero.
""",
            example = "(int-rose-tree 10)",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.41.0/src/phel/test/rose.phel#L225",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "test.rose",
        name = "test.rose/int-rose-tree-towards",
        signature = "(int-rose-tree-towards target n)",
        completion = CompletionInfo(
            tailText = "Rose tree for n whose children shrink strictly toward target",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Rose tree for <code>n</code> whose children shrink strictly toward <code>target</code>.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.41.0/src/phel/test/rose.phel#L232",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "test.rose",
        name = "test.rose/rose-bind",
        signature = "(rose-bind t f)",
        completion = CompletionInfo(
            tailText = "Monadic bind",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Monadic bind. <code>f</code> takes a value and returns a rose tree; the resulting<br />
  tree combines the shrinks of <code>t</code> (each fed through <code>f</code>) with the<br />
  shrinks produced by <code>f</code> on the root.
""",
            example = "(rose-bind (rose-pure 1) (fn [n] (rose-pure (inc n))))",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.41.0/src/phel/test/rose.phel#L109",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "test.rose",
        name = "test.rose/rose-children",
        signature = "(rose-children t)",
        completion = CompletionInfo(
            tailText = "Returns the lazy sequence of immediate child rose trees",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Returns the lazy sequence of immediate child rose trees.",
            example = "(rose-children (rose-pure 1)) ; =&gt; ()",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.41.0/src/phel/test/rose.phel#L74",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "test.rose",
        name = "test.rose/rose-filter",
        signature = "(rose-filter pred t)",
        completion = CompletionInfo(
            tailText = "Returns the subtree consisting only of nodes whose value satisfies pred",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns the subtree consisting only of nodes whose value satisfies<br />
  <code>pred</code>. Children that fail <code>pred</code> are pruned. If the root fails<br />
  <code>pred</code>, returns <code>nil</code> (caller is responsible for handling).
""",
            example = "(rose-filter pos? (rose-pure 1))",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.41.0/src/phel/test/rose.phel#L125",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "test.rose",
        name = "test.rose/rose-fmap",
        signature = "(rose-fmap f t)",
        completion = CompletionInfo(
            tailText = "Applies f to every value in rose tree t (the root and, lazily, every descendant)",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Applies <code>f</code> to every value in rose tree <code>t</code> (the root and, lazily,<br />
  every descendant).
""",
            example = "(rose-fmap inc (rose-pure 1)) ; root = 2",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.41.0/src/phel/test/rose.phel#L99",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "test.rose",
        name = "test.rose/rose-join",
        signature = "(rose-join t)",
        completion = CompletionInfo(
            tailText = "Flattens a rose tree of rose trees into a single rose tree",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Flattens a rose tree of rose trees into a single rose tree.",
            example = "(rose-join (rose-pure (rose-pure 1)))",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.41.0/src/phel/test/rose.phel#L138",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "test.rose",
        name = "test.rose/rose-map",
        signature = "(rose-map entry-trees)",
        completion = CompletionInfo(
            tailText = "Rose tree for a hash-map",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Rose tree for a hash-map. <code>entry-trees</code> is a vector of rose trees of<br />
  <code>[k v]</code> pairs. Shrinks by removing entries and by shrinking each<br />
  pair in place.
""",
            example = "(rose-map [...])",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.41.0/src/phel/test/rose.phel#L313",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "test.rose",
        name = "test.rose/rose-pure",
        signature = "(rose-pure x)",
        completion = CompletionInfo(
            tailText = "Leaf rose tree: value x with no shrink candidates",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Leaf rose tree: value <code>x</code> with no shrink candidates.
""",
            example = "(rose-pure 42)",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.41.0/src/phel/test/rose.phel#L52",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "test.rose",
        name = "test.rose/rose-root",
        signature = "(rose-root t)",
        completion = CompletionInfo(
            tailText = "Returns the root value of rose tree t",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns the root value of rose tree <code>t</code>.
""",
            example = "(rose-root (rose-pure 42)) ; =&gt; 42",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.41.0/src/phel/test/rose.phel#L67",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "test.rose",
        name = "test.rose/rose-shrinks",
        signature = "(rose-shrinks t)",
        completion = CompletionInfo(
            tailText = "Alias for rose-children: the lazy sequence of immediate smaller variants of the current root",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Alias for <code>rose-children</code>: the lazy sequence of immediate smaller<br />
  variants of the current root.
""",
            example = "(rose-shrinks (rose-pure 1)) ; =&gt; ()",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.41.0/src/phel/test/rose.phel#L81",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "test.rose",
        name = "test.rose/rose-string",
        signature = "(rose-string char-trees)",
        completion = CompletionInfo(
            tailText = "Rose tree for a string whose elements are char rose trees",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Rose tree for a string whose elements are char rose trees.",
            example = "(rose-string [(rose-pure \" \")])",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.41.0/src/phel/test/rose.phel#L296",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "test.rose",
        name = "test.rose/rose-tree",
        signature = "(rose-tree root children)",
        completion = CompletionInfo(
            tailText = "Rose tree constructor",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Rose tree constructor. <code>children</code> should be a lazy sequence of rose<br />
  trees (or any seqable value).
""",
            example = "(rose-tree 1 (list (rose-pure 0)))",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.41.0/src/phel/test/rose.phel#L59",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "test.rose",
        name = "test.rose/rose-vector",
        signature = "(rose-vector element-trees)",
        completion = CompletionInfo(
            tailText = "Rose tree for a vector whose children are built from the rose trees of its elements",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Rose tree for a vector whose children are built from the rose trees<br />
  of its elements. Shrinks by removing elements and by shrinking each<br />
  element in place.
""",
            example = "(rose-vector [(int-rose-tree 3) (int-rose-tree 5)])",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.41.0/src/phel/test/rose.phel#L266",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "test.rose",
        name = "test.rose/rose-vector-lax",
        signature = "(rose-vector-lax element-trees)",
        completion = CompletionInfo(
            tailText = "Like rose-vector but preserves original length; only shrinks each element in place (no removal)",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Like <code>rose-vector</code> but preserves original length; only shrinks each<br />
  element in place (no removal).
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.41.0/src/phel/test/rose.phel#L281",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "test.rose",
        name = "test.rose/rose-zip",
        signature = "(rose-zip trees)",
        completion = CompletionInfo(
            tailText = "Takes a vector of rose trees and returns a rose tree whose root is the vector of roots and whose ...",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Takes a vector of rose trees and returns a rose tree whose root is<br />
  the vector of roots and whose children shrink one position at a time.
""",
            example = "(rose-zip [(rose-pure 1) (rose-pure 2)])",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.41.0/src/phel/test/rose.phel#L170",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "test.rose",
        name = "test.rose/rose?",
        signature = "(rose? x)",
        completion = CompletionInfo(
            tailText = "Returns true if x looks like a rose tree",
            priority = PhelCompletionPriority.PREDICATE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns <code>true</code> if <code>x</code> looks like a rose tree.
""",
            example = "(rose? (rose-pure 1)) ; =&gt; true",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.41.0/src/phel/test/rose.phel#L89",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "test.rose",
        name = "test.rose/shrink-int",
        signature = "(shrink-int n)",
        completion = CompletionInfo(
            tailText = "Shrink strategy toward zero: halving steps (n/2, n/4, …) and then decrement toward zero",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Shrink strategy toward zero: halving steps (<code>n/2</code>, <code>n/4</code>, …) and then<br />
  decrement toward zero.
""",
            example = "(shrink-int 8)",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.41.0/src/phel/test/rose.phel#L211",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "test.rose",
        name = "test.rose/shrink-int-towards",
        signature = "(shrink-int-towards target n)",
        completion = CompletionInfo(
            tailText = "Returns a seq of integer candidates strictly closer to target than n",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns a seq of integer candidates strictly closer to <code>target</code> than<br />
  <code>n</code>. Yields the target first, then halving steps.
""",
            example = "(shrink-int-towards 0 10) ; =&gt; (0 5 7 8 9)",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.41.0/src/phel/test/rose.phel#L191",
                docs = "",
            ),
        ),
    )
)
