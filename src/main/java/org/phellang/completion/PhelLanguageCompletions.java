package org.phellang.completion;

import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.icons.AllIcons;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * Phel language constructs completions - special forms and macros
 */
public class PhelLanguageCompletions {

    private static final Icon SPECIAL_FORM_ICON = AllIcons.Nodes.Property;
    private static final Icon MACRO_ICON = AllIcons.Nodes.AbstractMethod;

    /**
     * Add Phel special forms
     */
    public static void addSpecialForms(@NotNull CompletionResultSet result, String prefix) {
        // Definition special forms
        addSpecialForm(result, "def", "(def name meta? value)", "Defines a global var");
        addSpecialForm(result, "defn", "(defn name doc? attr? [params*] body*)", "Defines a function");
        addSpecialForm(result, "defn-", "(defn- name doc? attr? [params*] body*)", "Defines a private function");
        addSpecialForm(result, "defmacro", "(defmacro name doc? attr? [params*] body*)", "Defines a macro");
        addSpecialForm(result, "defmacro-", "(defmacro- name doc? attr? [params*] body*)", "Defines a private macro");
        addSpecialForm(result, "defstruct", "(defstruct name [fields*])", "Defines a struct");
        addSpecialForm(result, "definterface", "(definterface name & methods)", "Defines an interface");
        addSpecialForm(result, "defexception", "(defexception name)", "Defines a new exception");
        addSpecialForm(result, "def-", "(def- name value)", "Define a private value that will not be exported");

        // Control flow special forms
        addSpecialForm(result, "if", "(if test then else?)", "Conditional expression");
        addSpecialForm(result, "when", "(when test & body)", "When test is true, evaluates body");
        addSpecialForm(result, "when-not", "(when-not test & body)", "When test is false, evaluates body");
        addSpecialForm(result, "when-let", "(when-let [binding test] & body)", "When test is truthy, binds and evaluates body");
        addSpecialForm(result, "if-let", "(if-let [binding test] then else?)", "Conditional with binding");
        addSpecialForm(result, "if-not", "(if-not test then else?)", "Inverted conditional");
        addSpecialForm(result, "case", "(case expr & clauses)", "Pattern matching on expr");
        addSpecialForm(result, "cond", "(cond & clauses)", "Multiple condition testing");

        // Binding and scoping
        addSpecialForm(result, "let", "(let [bindings*] expr*)", "Creates local bindings");
        addSpecialForm(result, "loop", "(loop [bindings*] expr*)", "Creates recursion point");
        addSpecialForm(result, "binding", "(binding [bindings*] expr*)", "Dynamic binding");
        addSpecialForm(result, "fn", "(fn name? [params*] expr*)", "Creates anonymous function");

        // Execution
        addSpecialForm(result, "do", "(do expr*)", "Evaluates exprs in sequence");
        addSpecialForm(result, "recur", "(recur expr*)", "Recursion to loop or function");

        // Meta and compilation
        addSpecialForm(result, "quote", "(quote form)", "Returns unevaluated form");
        addSpecialForm(result, "var", "(var value)", "Creates a variable");
        addSpecialForm(result, "ns", "(ns name & references)", "Creates/enters namespace");

        // Exception handling
        addSpecialForm(result, "try", "(try expr* catch* finally?)", "Exception handling");
        addSpecialForm(result, "catch", "(catch ExceptionType binding expr*)", "Exception catch clause");
        addSpecialForm(result, "finally", "(finally expr*)", "Exception finally clause");
        addSpecialForm(result, "throw", "(throw expr)", "Throws exception");

        // Loops and iteration
        addSpecialForm(result, "for", "(for seq-exprs body)", "List comprehension");
        addSpecialForm(result, "dofor", "(dofor seq-exprs body)", "For side effects");
        addSpecialForm(result, "foreach", "(foreach [binding coll] body*)", "PHP-style foreach");

        // Testing constructs (deftest is a macro, but commonly used)
        // Note: deftest is also available as test/deftest from the test namespace
        addSpecialForm(result, "deftest", "(deftest test-name & body)", "Defines a test function with no arguments");
        addSpecialForm(result, "is", "(is form & [message])", "Generic assertion macro for testing");
    }

    /**
     * Add Phel core macros
     */
    public static void addCoreMacros(@NotNull CompletionResultSet result, String prefix) {
        // Threading macros
        addMacro(result, "->", "(-> x & forms)", "Thread-first macro");
        addMacro(result, "->>", "(->> x & forms)", "Thread-last macro");
        addMacro(result, "as->", "(as-> expr name & forms)", "Thread with explicit binding");
        addMacro(result, "cond->", "(cond-> expr & clauses)", "Thread when conditions are met");
        addMacro(result, "cond->>", "(cond->> expr & clauses)", "Thread-last when conditions are met");
        addMacro(result, "some->", "(some-> expr & forms)", "Thread when not nil");
        addMacro(result, "some->>", "(some->> expr & forms)", "Thread-last when not nil");

        // Logical macros
        addMacro(result, "and", "(and & forms)", "Short-circuit logical AND");
        addMacro(result, "or", "(or & forms)", "Short-circuit logical OR");

        // Utility macros  
        addMacro(result, "doto", "(doto x & forms)", "Evaluates forms with x as first arg");
        addMacro(result, "comment", "(comment & body)", "Ignores body, returns nil");
        addMacro(result, "declare", "(declare & names)", "Forward declaration");

        // Comparison macros
        addMacro(result, "=", "(= & args)", "Equality comparison");
        addMacro(result, "not=", "(not= & args)", "Inequality comparison");
        addMacro(result, "<", "(< & args)", "Less than comparison");
        addMacro(result, "<=", "(<= & args)", "Less than or equal comparison");
        addMacro(result, ">", "(> & args)", "Greater than comparison");
        addMacro(result, ">=", "(>= & args)", "Greater than or equal comparison");
        addMacro(result, "<=>", "(<=> x y)", "Three-way comparison");
        addMacro(result, ">=<", "(>=< x y)", "Alternative comparison");

        // Boolean operations
        addMacro(result, "not", "(not x)", "Logical negation");
        addMacro(result, "id", "(id x y)", "Identity comparison");

        // Regex macros
        addMacro(result, "re-find", "(re-find pattern string)", "Find regex match");
        addMacro(result, "re-find-all", "(re-find-all pattern string)", "Find all regex matches");
        addMacro(result, "re-match", "(re-match pattern string)", "Full regex match");
        addMacro(result, "re-replace", "(re-replace string pattern replacement)", "Replace with regex");
        addMacro(result, "re-split", "(re-split string pattern)", "Split with regex");

        // Memoization
        addMacro(result, "memoize", "(memoize f)", "Returns memoized function");

        // Higher-order function utilities
        addMacro(result, "juxt", "(juxt & fns)", "Returns function that applies all fns to args");

        // Output utilities
        addMacro(result, "with-output-buffer", "(with-output-buffer & body)", "Captures output in buffer");
        
        // Additional macros from official Phel core
        addMacro(result, "if-let", "(if-let [binding test] then else?)", "If test is true, evaluates then with binding-form bound to the value of test");
        addMacro(result, "when-let", "(when-let [binding test] & body)", "When test is true, evaluates body with binding-form bound to the value of test");
        addMacro(result, "time", "(time expr)", "Evaluates expr and prints the time it took. Returns the value of expr");
        addMacro(result, "binding", "(binding [bindings*] expr*)", "Temporary redefines definitions while executing the body");
    }

    private static void addSpecialForm(@NotNull CompletionResultSet result, String name, String signature, String description) {
        // Special forms get high priority as they are core language constructs
        PhelCompletionRanking.addRankedCompletion(result, name, PhelCompletionRanking.Priority.SPECIAL_FORMS, 
                                                signature, description, SPECIAL_FORM_ICON);
    }

    private static void addMacro(@NotNull CompletionResultSet result, String name, String signature, String description) {
        // Macros get medium priority - less common than special forms but more than regular functions
        PhelCompletionRanking.addRankedCompletion(result, name, PhelCompletionRanking.Priority.MACROS, 
                                                signature, description, MACRO_ICON);
    }
}
