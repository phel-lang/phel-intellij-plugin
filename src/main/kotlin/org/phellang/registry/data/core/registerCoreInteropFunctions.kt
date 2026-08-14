package org.phellang.registry.data.core

import org.phellang.registry.CompletionInfo
import org.phellang.registry.DocumentationInfo
import org.phellang.registry.DocumentationLinks
import org.phellang.registry.DeprecationInfo
import org.phellang.registry.PhelFunction

import org.phellang.registry.PhelCompletionPriority

internal fun registerCoreInteropFunctions(): List<PhelFunction> = listOf(
    PhelFunction(
        namespace = "core",
        name = "php-invoke",
        signature = "(php-invoke target method & args)",
        completion = CompletionInfo(
            tailText = "Calls the method method on target, where the method name is a value rather than a literal",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Calls the method <code>method</code> on <code>target</code>, where the method name is a value<br />
  rather than a literal. <code>target</code> is either an object or a class-name string<br />
  for a static method. Matches ClojureScript's <code>js-invoke</code>; use the dot<br />
  syntax (<code>(.format d "Y")</code>) whenever the name is known at compile time.
""",
            example = "(php-invoke (new DateTimeImmutable \"2024-03-10\") \"format\" \"Y\") ; =&gt; \"2024\"\n(let [m \"format\"] (php-invoke (new DateTimeImmutable \"2024-03-10\") m \"Y\")) ; =&gt; \"2024\"",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.50.0/src/phel/core/interop.phel#L60",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "set!",
        signature = "(set! place value)",
        completion = CompletionInfo(
            tailText = "Sets place to value and returns value, covering all three shapes Clojure's set",
            priority = PhelCompletionPriority.MACROS,
        ),
        documentation = DocumentationInfo(
            summary = """
Sets <code>place</code> to <code>value</code> and returns <code>value</code>, covering all three shapes<br />
  Clojure's <code>set!</code> covers.<br /><br />
A property access (<code>(.-prop obj)</code>) assigns the property, as does any other<br />
  place <code>php/oset</code> accepts. A <strong>qualified symbol naming a member of a PHP<br />
  class</strong> (<code>Foo/slot</code>) assigns that static property. Any other <strong>symbol</strong><br />
  names a dynamic var and assigns its current thread-local binding, throwing<br />
  when no <code>binding</code> frame is active, so it never writes the root by accident;<br />
  <code>alter-var-root</code> is the way to change a root.<br /><br />
A macro rather than a function because the place is a location, not a value.
""",
            example = "(let [o (new ArrayObject)] (set! (.-y o) 2024)) ; =&gt; 2024\n(def ^:dynamic *x* 0)\n(binding [*x* 1] (set! *x* 2) *x*) ; =&gt; 2",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.50.0/src/phel/core/interop.phel#L21",
                docs = "",
            ),
        ),
    )
)
