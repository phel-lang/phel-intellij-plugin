package org.phellang

import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighter
import com.intellij.openapi.options.colors.AttributesDescriptor
import com.intellij.openapi.options.colors.ColorDescriptor
import com.intellij.openapi.options.colors.ColorSettingsPage
import javax.swing.Icon

class PhelColorSettingsPage : ColorSettingsPage {
    override fun getIcon(): Icon {
        return PhelIcons.FILE
    }

    override fun getHighlighter(): SyntaxHighlighter {
        return PhelSyntaxHighlighter()
    }

    override fun getDemoText(): String {
        return "(ns src\\demo" +
                "\n  (:require phel\\core :refer [print-str str map filter])" +
                "\n  (:require phel\\http :as http)" +
                "\n  (:require phel\\str :as s)" +
                "\n  (:use \\DateTime \\Exception))" +
                "\n" +
                "\n# Boolean and nil literals" +
                "\n(def config {:debug true :prod false :cache nil :timeout NAN})" +
                "\n" +
                "\n# Numbers in different bases" +
                "\n(def numbers [42 0xFF 0b1010 0o777 3.14159 -1.5e10])" +
                "\n" +
                "\n# Character literals and strings" +
                "\n(def greeting \"Phel-lo world!\")" +
                "\n" +
                "\n# Advanced destructuring" +
                "\n(defn process-data" +
                "\n  \"Function with comprehensive destructuring\"" +
                "\n  {:export true}" +
                "\n  [{:keys keys :method method :headers headers} users]" +
                "\n  (let [[first-user & rest-users] users" +
                "\n        {:name name :age age} first-user]" +
                "\n    (case method" +
                "\n      \"GET\" (handle-get-request name age)" +
                "\n      \"POST\" (handle-post-request keys headers)" +
                "\n      (throw (php/new Exception \"Unsupported method\")))))" +
                "\n" +
                "\n# Macro with full quote/unquote" +
                "\n(defmacro when-feature [feature & body]" +
                "\n  `(when (get config ,feature)" +
                "\n    (do ,@body)))" +
                "\n" +
                "\n# PHP interop" +
                "\n(def now (php/new DateTime))" +
                "\n(def formatted (php/-> now (format \"Y-m-d H:i:s\")))" +
                "\n(def static-call (php/:: DateTime (createFromFormat \"U\" \"1234567890\")))" +
                "\n(def server-data (get php/\$_SERVER \"REQUEST_METHOD\"))" +
                "\n" +
                "\n(def processed (s/join \", \" (map str [1 2 3])))" +
                "\n(def response (http/response-from-map {:status 200 :body \"OK\"}))" +
                "\n" +
                "\n# Advanced control flow and loops" +
                "\n(def results" +
                "\n  (for [x :range [1 10]" +
                "\n        :when (even? x)" +
                "\n        :let [squared (* x x)]]" +
                "\n        :reduce [acc []]" +
                "\n    (conj acc {:num x :squared squared})))" +
                "\n" +
                "\n# Variable manipulation and metadata" +
                "\n(def ^:private secret (var 42))" +
                "\n(swap! secret inc)" +
                "\n(def readonly-value (deref secret))" +
                "\n" +
                "\n# Core functions and predicates" +
                "\n(defn analyze-collection [coll]" +
                "\n  (cond" +
                "\n    (empty? coll) :empty" +
                "\n    (vector? coll) :vector" +
                "\n    (map? coll) :map" +
                "\n    (set? coll) :set" +
                "\n    (list? coll) :list" +
                "\n    :else :unknown))" +
                "\n" +
                "\n# Bitwise operations" +
                "\n(def flags (bit-or 0b0001 0b0010 0b0100))" +
                "\n(def masked (bit-and flags 0b0110))" +
                "\n" +
                "\n# Macros" +
                "\n(def processed-text" +
                "\n  (-> \"  Hello World  \"" +
                "\n      s/trim" +
                "\n      s/lower-case" +
                "\n      (s/replace \"world\" \"Phel\")" +
                "\n      (str \"!\")))" +
                "\n" +
                "\n# Exception handling" +
                "\n(defn safe-divide [a b]" +
                "\n  (try" +
                "\n    (/ a b)" +
                "\n    (catch \\DivisionByZeroError e" +
                "\n      (php/-> e (getMessage)))" +
                "\n    (finally" +
                "\n      (println \"Division attempt completed\"))))" +
                "\n" +
                "\n# REPL and documentation" +
                "\n(comment" +
                "\n  \"This is a comment block for REPL experimentation\"" +
                "\n  (doc map)" +
                "\n  (require phel\\test :as test)" +
                "\n  (test/run-tests))" +
                "\n" +
                "\n#|" +
                "\nI am a multiline comment" +
                "\n" +
                "\nIf you know Lisp or Clojure, you'll feel right at home." +
                "\nIf you don't — this is a great place to start." +
                "\n|#"
    }

    override fun getAdditionalHighlightingTagToDescriptorMap(): MutableMap<String?, TextAttributesKey?>? {
        return null
    }

    override fun getAttributeDescriptors(): Array<AttributesDescriptor> {
        return DESCRIPTORS
    }

    override fun getColorDescriptors(): Array<ColorDescriptor> {
        return ColorDescriptor.EMPTY_ARRAY
    }

    override fun getDisplayName(): String {
        return "Phel Lang"
    }
}

private val DESCRIPTORS = arrayOf(
    AttributesDescriptor("Comments", PhelSyntaxHighlighter.COMMENT),
    AttributesDescriptor("Strings", PhelSyntaxHighlighter.STRING),
    AttributesDescriptor("Numbers", PhelSyntaxHighlighter.NUMBER),
    AttributesDescriptor("Keywords", PhelSyntaxHighlighter.KEYWORD_IDENTIFIER),
    AttributesDescriptor("Booleans", PhelSyntaxHighlighter.BOOLEAN),
    AttributesDescriptor("Nil", PhelSyntaxHighlighter.NIL_LITERAL),
    AttributesDescriptor("NAN", PhelSyntaxHighlighter.NAN_LITERAL),
    AttributesDescriptor("Characters", PhelSyntaxHighlighter.CHARACTER),
    AttributesDescriptor("Parentheses", PhelSyntaxHighlighter.PARENTHESES),
    AttributesDescriptor("Brackets", PhelSyntaxHighlighter.BRACKETS),
    AttributesDescriptor("Braces", PhelSyntaxHighlighter.BRACES),
    AttributesDescriptor("Quote", PhelSyntaxHighlighter.QUOTE),
    AttributesDescriptor("Syntax quote", PhelSyntaxHighlighter.SYNTAX_QUOTE),
    AttributesDescriptor("Unquote", PhelSyntaxHighlighter.UNQUOTE),
    AttributesDescriptor("Unquote splicing", PhelSyntaxHighlighter.UNQUOTE_SPLICING),
    AttributesDescriptor("Symbols", PhelSyntaxHighlighter.SYMBOL),
    AttributesDescriptor("Metadata", PhelSyntaxHighlighter.METADATA),
    AttributesDescriptor("Dot operator", PhelSyntaxHighlighter.DOT_OPERATOR),
    AttributesDescriptor("Comma", PhelSyntaxHighlighter.COMMA),
    AttributesDescriptor("Bad characters", PhelSyntaxHighlighter.BAD_CHARACTER),
)