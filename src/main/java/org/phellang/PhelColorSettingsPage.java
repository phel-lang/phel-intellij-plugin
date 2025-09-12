package org.phellang;

import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.openapi.options.colors.AttributesDescriptor;
import com.intellij.openapi.options.colors.ColorDescriptor;
import com.intellij.openapi.options.colors.ColorSettingsPage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.Map;

/**
 * Color settings page for Phel language customization
 */
public class PhelColorSettingsPage implements ColorSettingsPage {

    private static final AttributesDescriptor[] DESCRIPTORS = new AttributesDescriptor[]{
            new AttributesDescriptor("Comments", PhelSyntaxHighlighter.COMMENT),
            new AttributesDescriptor("Form Comments (Commented Out)", PhelAnnotator.COMMENTED_OUT_FORM),
            new AttributesDescriptor("Strings", PhelSyntaxHighlighter.STRING),
            new AttributesDescriptor("Numbers", PhelSyntaxHighlighter.NUMBER),
            new AttributesDescriptor("Keywords", PhelSyntaxHighlighter.KEYWORD_IDENTIFIER),
            new AttributesDescriptor("Booleans", PhelSyntaxHighlighter.BOOLEAN),
            new AttributesDescriptor("Nil", PhelSyntaxHighlighter.NIL_LITERAL),
            new AttributesDescriptor("NAN", PhelSyntaxHighlighter.NAN_LITERAL),
            new AttributesDescriptor("Characters", PhelSyntaxHighlighter.CHARACTER),
            new AttributesDescriptor("Parentheses", PhelSyntaxHighlighter.PARENTHESES),
            new AttributesDescriptor("Brackets", PhelSyntaxHighlighter.BRACKETS),
            new AttributesDescriptor("Braces", PhelSyntaxHighlighter.BRACES),
            new AttributesDescriptor("Quote", PhelSyntaxHighlighter.QUOTE),
            new AttributesDescriptor("Syntax Quote", PhelSyntaxHighlighter.SYNTAX_QUOTE),
            new AttributesDescriptor("Unquote", PhelSyntaxHighlighter.UNQUOTE),
            new AttributesDescriptor("Unquote Splicing", PhelSyntaxHighlighter.UNQUOTE_SPLICING),
            new AttributesDescriptor("Symbols", PhelSyntaxHighlighter.SYMBOL),
            new AttributesDescriptor("Metadata", PhelSyntaxHighlighter.METADATA),
            new AttributesDescriptor("Dot Operator", PhelSyntaxHighlighter.DOT_OPERATOR),
            new AttributesDescriptor("Comma", PhelSyntaxHighlighter.COMMA),
            new AttributesDescriptor("Bad Characters", PhelSyntaxHighlighter.BAD_CHARACTER),
            // Additional descriptors for annotator colors
            new AttributesDescriptor("PHP Interop", PhelAnnotator.PHP_INTEROP),
            new AttributesDescriptor("PHP Variables", PhelAnnotator.PHP_VARIABLE),
            new AttributesDescriptor("Core Functions", PhelAnnotator.CORE_FUNCTION),
            new AttributesDescriptor("Macros", PhelAnnotator.MACRO),
            new AttributesDescriptor("Special Forms", PhelAnnotator.SPECIAL_FORM),
            new AttributesDescriptor("Namespace Prefix", PhelAnnotator.NAMESPACE_PREFIX),
            new AttributesDescriptor("Function Parameters", PhelAnnotator.FUNCTION_PARAMETER),
    };

    @Nullable
    @Override
    public Icon getIcon() {
        return PhelIcons.FILE;
    }

    @NotNull
    @Override
    public SyntaxHighlighter getHighlighter() {
        return new PhelSyntaxHighlighter();
    }

    @NotNull
    @Override
    public String getDemoText() {
        return "# Phel Syntax Highlighting Demo - Fixed and Complete!" +
                "\n# This file demonstrates all 25+ syntax highlighting features" +
                "\n" +
                "\n(ns demo\\syntax-showcase" +
                "\n  (:require phel\\core :refer [print-str str map filter])" +
                "\n  (:require phel\\http :as http)" +
                "\n  (:require phel\\str :as s)" +
                "\n  (:use \\DateTime \\Exception))" +
                "\n" +
                "\n# Boolean and nil literals - distinct colors" +
                "\n(def config {:debug true :prod false :cache nil :timeout NAN})" +
                "\n" +
                "\n# Numbers in all supported bases - unified number highlighting" +
                "\n(def numbers [42 0xFF 0b1010 0o777 3.14159 -1.5e10])" +
                "\n" +
                "\n# Character literals and strings - proper string highlighting" +
                "\n(def greeting \"Hello, Phel world!\")" +
                "\n(def special-chars [\\newline \\tab \\u03BB \\\\])" +
                "\n" +
                "\n# Advanced destructuring with highlighting" +
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
                "\n# Macro with full quote/unquote highlighting" +
                "\n(defmacro when-feature [feature & body]" +
                "\n  `(when (get config ,feature)" +
                "\n    (do ,@body)))" +
                "\n" +
                "\n# PHP interop with distinct highlighting" +
                "\n(def now (php/new DateTime))" +
                "\n(def formatted (php/-> now (format \"Y-m-d H:i:s\")))" +
                "\n(def static-call (php/:: DateTime (createFromFormat \"U\" \"1234567890\")))" +
                "\n(def server-data (get php/$_SERVER \"REQUEST_METHOD\"))" +
                "\n" +
                "\n# Namespace functions with prefix highlighting" +
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
                "\n# Core functions and predicates - specialized highlighting" +
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
                "\n# Threading macros with proper operator highlighting" +
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
                "\n  (test/run-tests))";
    }

    @Nullable
    @Override
    public Map<String, TextAttributesKey> getAdditionalHighlightingTagToDescriptorMap() {
        return null;
    }

    @NotNull
    @Override
    public AttributesDescriptor[] getAttributeDescriptors() {
        return DESCRIPTORS;
    }

    @NotNull
    @Override
    public ColorDescriptor[] getColorDescriptors() {
        return ColorDescriptor.EMPTY_ARRAY;
    }

    @NotNull
    @Override
    public String getDisplayName() {
        return "Phel";
    }
}