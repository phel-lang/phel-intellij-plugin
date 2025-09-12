package org.phellang.completion;

import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.icons.AllIcons;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * Phel Core API completions - 400+ functions with documentation
 * Based on official Phel API documentation: https://phel-lang.org/documentation/api/
 */
public class PhelApiCompletions {

    private static final Icon FUNCTION_ICON = AllIcons.Nodes.Function;
    private static final Icon PREDICATE_ICON = AllIcons.Nodes.Method;

    /**
     * Add core Phel functions with documentation and type information
     * Enhanced with smart ranking system and context-aware prioritization
     */
    public static void addCoreFunctions(@NotNull CompletionResultSet result, String prefix, @NotNull com.intellij.psi.PsiElement element) {
        // Core data structure functions with context-aware ranking
        addFunctionWithContext(result, "count", "(count xs)", "Returns the number of items in the collection", FUNCTION_ICON, element);
        addFunctionWithContext(result, "get", "(get coll key)", "Returns the value mapped to key, nil if key not present", FUNCTION_ICON, element);
        addFunctionWithContext(result, "get-in", "(get-in coll [k & ks])", "Returns the value in a nested data structure", FUNCTION_ICON, element);
        addFunctionWithContext(result, "put", "(put coll key val)", "Returns a new collection with val added/updated at key", FUNCTION_ICON, element);
        addFunction(result, "put-in", "(put-in coll [k & ks] val)", "Puts a value into a nested data structure", FUNCTION_ICON);
        addFunction(result, "unset", "(unset coll key)", "Returns coll without key", FUNCTION_ICON);
        addFunction(result, "unset-in", "(unset-in coll [k & ks])", "Removes a value from a nested data structure", FUNCTION_ICON);
        addFunction(result, "update", "(update coll key f & args)", "Updates a value by applying f to current element", FUNCTION_ICON);
        addFunction(result, "update-in", "(update-in coll [k & ks] f & args)", "Updates a value in nested data structure", FUNCTION_ICON);

        // Collection creation
        addFunction(result, "list", "(list & items)", "Creates a new list", FUNCTION_ICON);
        addFunction(result, "vector", "(vector & items)", "Creates a new vector", FUNCTION_ICON);
        addFunction(result, "hash-map", "(hash-map & keyvals)", "Creates a new hash map", FUNCTION_ICON);
        addFunction(result, "set", "(set & items)", "Creates a new set", FUNCTION_ICON);

        // Sequence functions
        addFunction(result, "first", "(first coll)", "Returns the first item in the collection", FUNCTION_ICON);
        addFunction(result, "second", "(second coll)", "Returns the second item in the collection", FUNCTION_ICON);
        addFunction(result, "rest", "(rest coll)", "Returns collection without the first item", FUNCTION_ICON);
        addFunction(result, "next", "(next coll)", "Returns collection without the first item, or nil if empty", FUNCTION_ICON);
        addFunction(result, "last", "(last coll)", "Returns the last item in the collection", FUNCTION_ICON);
        addFunction(result, "peek", "(peek coll)", "Returns the last item for vectors, first for lists", FUNCTION_ICON);
        addFunction(result, "pop", "(pop coll)", "Returns collection with last item (vector) or first item (list) removed", FUNCTION_ICON);

        // Higher-order functions
        addFunction(result, "map", "(map f & xs)", "Returns collection with f applied to each item", FUNCTION_ICON);
        addFunction(result, "filter", "(filter pred xs)", "Returns collection of items for which pred returns true", FUNCTION_ICON);
        addFunction(result, "remove", "(remove pred xs)", "Returns collection of items for which pred returns false", FUNCTION_ICON);
        addFunction(result, "reduce", "(reduce f & xs)", "Reduces collection using f", FUNCTION_ICON);
        addFunction(result, "reduce2", "(reduce2 f init coll)", "Reduces collection using f with initial value", FUNCTION_ICON);
        addFunction(result, "keep", "(keep pred xs)", "Returns collection of non-nil results of pred applied to items", FUNCTION_ICON);
        addFunction(result, "keep-indexed", "(keep-indexed pred xs)", "Like keep but pred takes index and item", FUNCTION_ICON);
        addFunction(result, "map-indexed", "(map-indexed f xs)", "Like map but f takes index and item", FUNCTION_ICON);

        // Utility functions
        addFunction(result, "apply", "(apply f & args)", "Applies f to args, with last arg as sequence", FUNCTION_ICON);
        addFunction(result, "comp", "(comp & fs)", "Composes functions right to left", FUNCTION_ICON);
        addFunction(result, "partial", "(partial f & args)", "Returns function with args partially applied", FUNCTION_ICON);
        addFunction(result, "constantly", "(constantly x)", "Returns function that always returns x", FUNCTION_ICON);
        addFunction(result, "identity", "(identity x)", "Returns x unchanged", FUNCTION_ICON);
        addFunction(result, "complement", "(complement f)", "Returns function that returns opposite boolean of f", FUNCTION_ICON);

        // String functions
        addFunction(result, "str", "(str & args)", "Concatenates string representations of args", FUNCTION_ICON);
        addFunction(result, "print-str", "(print-str & args)", "Returns string representation of args separated by spaces", FUNCTION_ICON);
        addFunction(result, "format", "(format fmt & args)", "Returns formatted string", FUNCTION_ICON);

        // I/O functions
        addFunction(result, "print", "(print & args)", "Prints args to output", FUNCTION_ICON);
        addFunction(result, "println", "(println & args)", "Prints args followed by newline", FUNCTION_ICON);
        addFunction(result, "printf", "(printf fmt & args)", "Prints formatted string", FUNCTION_ICON);

        // Metadata and compilation
        addFunction(result, "meta", "(meta x)", "Returns metadata of x", FUNCTION_ICON);
        addFunction(result, "with-meta", "(with-meta x meta)", "Returns x with metadata", FUNCTION_ICON);
        addFunction(result, "eval", "(eval form)", "Evaluates form", FUNCTION_ICON);
        addFunction(result, "compile", "(compile form)", "Compiles form to PHP", FUNCTION_ICON);
        addFunction(result, "macroexpand", "(macroexpand form)", "Expands macro call", FUNCTION_ICON);
        addFunction(result, "macroexpand-1", "(macroexpand-1 form)", "Expands macro call one level", FUNCTION_ICON);

        // Variables
        addFunction(result, "var", "(var value)", "Creates new variable with value", FUNCTION_ICON);
        addFunction(result, "deref", "(deref variable)", "Returns value of variable", FUNCTION_ICON);
        addFunction(result, "set!", "(set! var value)", "Sets variable to value", FUNCTION_ICON);
        addFunction(result, "swap!", "(swap! var f & args)", "Atomically updates variable with f", FUNCTION_ICON);

        // Transients
        addFunction(result, "transient", "(transient coll)", "Returns transient version of collection", FUNCTION_ICON);
        addFunction(result, "persistent", "(persistent tcoll)", "Returns persistent version of transient", FUNCTION_ICON);

        // Set operations
        addFunction(result, "union", "(union & sets)", "Returns union of sets", FUNCTION_ICON);
        addFunction(result, "intersection", "(intersection s1 s2)", "Returns intersection of sets", FUNCTION_ICON);
        addFunction(result, "difference", "(difference set & sets)", "Returns difference of sets", FUNCTION_ICON);
        addFunction(result, "symmetric-difference", "(symmetric-difference s1 s2)", "Returns symmetric difference", FUNCTION_ICON);

        // Random functions
        addFunction(result, "rand", "(rand)", "Returns random number between 0 and 1", FUNCTION_ICON);
        addFunction(result, "rand-int", "(rand-int n)", "Returns random integer between 0 and n-1", FUNCTION_ICON);
        addFunction(result, "rand-nth", "(rand-nth coll)", "Returns random item from collection", FUNCTION_ICON);

        // Range and sequence generation
        addFunction(result, "range", "(range start? end step?)", "Returns range of numbers", FUNCTION_ICON);
        addFunction(result, "repeat", "(repeat n x)", "Returns sequence of n xs", FUNCTION_ICON);
        addFunction(result, "repeatedly", "(repeatedly n f)", "Returns sequence of n calls to f", FUNCTION_ICON);

        // More collection functions
        addFunction(result, "take", "(take n xs)", "Returns first n items", FUNCTION_ICON);
        addFunction(result, "take-while", "(take-while pred xs)", "Returns items while pred is true", FUNCTION_ICON);
        addFunction(result, "drop", "(drop n xs)", "Returns collection without first n items", FUNCTION_ICON);
        addFunction(result, "drop-while", "(drop-while pred xs)", "Returns collection after dropping while pred is true", FUNCTION_ICON);
        addFunction(result, "concat", "(concat arr & xs)", "Concatenates collections", FUNCTION_ICON);
        addFunction(result, "flatten", "(flatten xs)", "Flattens nested collections", FUNCTION_ICON);
        addFunction(result, "distinct", "(distinct xs)", "Returns collection with duplicates removed", FUNCTION_ICON);
        addFunction(result, "partition", "(partition n xs)", "Returns collection partitioned into chunks of size n", FUNCTION_ICON);
        addFunction(result, "interleave", "(interleave & xs)", "Interleaves collections", FUNCTION_ICON);
        addFunction(result, "interpose", "(interpose sep xs)", "Interposes sep between items", FUNCTION_ICON);

        // Sorting and comparison
        addFunction(result, "sort", "(sort xs)", "Returns sorted collection", FUNCTION_ICON);
        addFunction(result, "sort-by", "(sort-by keyfn xs)", "Returns collection sorted by keyfn", FUNCTION_ICON);
        addFunction(result, "compare", "(compare x y)", "Compares x and y", FUNCTION_ICON);
        addFunction(result, "max", "(max & args)", "Returns maximum value", FUNCTION_ICON);
        addFunction(result, "min", "(min & args)", "Returns minimum value", FUNCTION_ICON);
        addFunction(result, "extreme", "(extreme order args)", "Returns extreme value using order function", FUNCTION_ICON);
        
        // Additional documented functions from official API
        addFunction(result, "butlast", "(butlast xs)", "Returns all but the last element", FUNCTION_ICON);
        addFunction(result, "dedupe", "(dedupe xs)", "Returns collection with consecutive duplicates removed", FUNCTION_ICON);
        addFunction(result, "drop-last", "(drop-last n xs)", "Returns collection without last n items", FUNCTION_ICON);
        addFunction(result, "every?", "(every? pred xs)", "Returns true if pred is true for every element", PREDICATE_ICON);
        addFunction(result, "all?", "(all? pred xs)", "Returns true if pred is true for every element", PREDICATE_ICON);
        addFunction(result, "mapcat", "(mapcat f & xs)", "Maps f over collections then concatenates results", FUNCTION_ICON);
        addFunction(result, "assoc", "(assoc ds key value)", "Associates key with value in data structure", FUNCTION_ICON);
        addFunction(result, "assoc-in", "(assoc-in ds ks v)", "Associates value in nested data structure", FUNCTION_ICON);
        addFunction(result, "dissoc", "(dissoc ds key)", "Dissociates key from data structure", FUNCTION_ICON);
        addFunction(result, "dissoc-in", "(dissoc-in ds ks)", "Dissociates key from nested data structure", FUNCTION_ICON);
        addFunction(result, "comment", "(comment &)", "Comments out expressions", FUNCTION_ICON);
        addFunction(result, "difference-pair", "(difference-pair s1 s2)", "Returns difference between two sets as pair", FUNCTION_ICON);
        
        // Threading macros documented in API
        addFunction(result, "->", "(-> x & forms)", "Thread-first macro", FUNCTION_ICON);
        addFunction(result, "->>", "(->> x & forms)", "Thread-last macro", FUNCTION_ICON);
        addFunction(result, "as->", "(as-> expr name & forms)", "Thread with alias", FUNCTION_ICON);
        addFunction(result, "doto", "(doto x & forms)", "Evaluates forms with x as first argument", FUNCTION_ICON);
        
        // Symbol and meta functions from official API
        addFunction(result, "gensym", "(gensym)", "Generates a new unique symbol", FUNCTION_ICON);
        addFunction(result, "symbol", "(symbol name-or-ns & [name])", "Returns a new symbol for given string with optional namespace", FUNCTION_ICON);
        addFunction(result, "keyword", "(keyword x)", "Creates a new Keyword from a given string", FUNCTION_ICON);
        
        // Additional sequence navigation functions
        addFunction(result, "ffirst", "(ffirst xs)", "Same as (first (first xs))", FUNCTION_ICON);
        addFunction(result, "nfirst", "(nfirst xs)", "Same as (next (first xs))", FUNCTION_ICON);
        addFunction(result, "nnext", "(nnext xs)", "Same as (next (next xs))", FUNCTION_ICON);
        
        // Advanced sequence functions  
        addFunction(result, "take-last", "(take-last n xs)", "Takes the last n elements of xs", FUNCTION_ICON);
        addFunction(result, "take-nth", "(take-nth n xs)", "Returns every nth item in xs", FUNCTION_ICON);
        addFunction(result, "slice", "(slice xs & [offset & [length]])", "Extract a slice of xs", FUNCTION_ICON);
        
        // PHP conversion functions
        addFunction(result, "to-php-array", "(to-php-array xs)", "Create a PHP Array from a sequential data structure", FUNCTION_ICON);
        addFunction(result, "php-array-to-map", "(php-array-to-map arr)", "Converts a PHP Array to a map", FUNCTION_ICON);
        addFunction(result, "phel->php", "(phel->php x)", "Recursively converts a Phel data structure to a PHP array", FUNCTION_ICON);
        addFunction(result, "php->phel", "(php->phel x)", "Recursively converts a PHP array to Phel data structures", FUNCTION_ICON);
        addFunction(result, "php-indexed-array", "(php-indexed-array & xs)", "Creates a PHP indexed array from the given values", FUNCTION_ICON);
        addFunction(result, "php-associative-array", "(php-associative-array & xs)", "Creates a PHP associative array. An even number of parameters must be provided", FUNCTION_ICON);
        
        // Advanced collection functions
        addFunction(result, "select-keys", "(select-keys m ks)", "Returns a new map including key value pairs from m selected with of keys ks", FUNCTION_ICON);
        addFunction(result, "split-at", "(split-at n xs)", "Returns a vector of [(take n coll) (drop n coll)]", FUNCTION_ICON);
        addFunction(result, "split-with", "(split-with f xs)", "Returns a vector of [(take-while pred coll) (drop-while pred coll)]", FUNCTION_ICON);
        addFunction(result, "partition-by", "(partition-by f xs)", "Applies f to each value in xs, splitting them each time the return value changes", FUNCTION_ICON);
        addFunction(result, "tree-seq", "(tree-seq branch? children root)", "Returns a vector of the nodes in the tree, via a depth-first walk", FUNCTION_ICON);
        
        // Math functions
        addFunction(result, "sum", "(sum xs)", "Returns the sum of all elements is xs", FUNCTION_ICON);
        addFunction(result, "mean", "(mean xs)", "Returns the mean of xs", FUNCTION_ICON);
        addFunction(result, "median", "(median xs)", "Returns the median of xs", FUNCTION_ICON);
        addFunction(result, "coerce-in", "(coerce-in v min max)", "Returns v if it is in the range, or min if v is less than min, or max if v is greater than max", FUNCTION_ICON);
        
        // File I/O functions
        addFunction(result, "slurp", "(slurp filename & [opts])", "Reads entire file into a string", FUNCTION_ICON);
        addFunction(result, "spit", "(spit filename data & [opts])", "Writes data to file, returning number of bytes written", FUNCTION_ICON);
        
        // String/name functions
        addFunction(result, "name", "(name x)", "Returns the name string of a string, keyword or symbol", FUNCTION_ICON);
        addFunction(result, "namespace", "(namespace x)", "Return the namespace string of a symbol or keyword. Nil if not present", FUNCTION_ICON);
        addFunction(result, "full-name", "(full-name x)", "Return the namespace and name string of a string, keyword or symbol", FUNCTION_ICON);
        
        // Compilation functions
        addFunction(result, "read-string", "(read-string s)", "Reads the first phel expression from the string s", FUNCTION_ICON);
        addFunction(result, "eval", "(eval form)", "Evaluates a form and return the evaluated results", FUNCTION_ICON);
        addFunction(result, "compile", "(compile form)", "Returns the compiled PHP code string for the given form", FUNCTION_ICON);
        addFunction(result, "macroexpand", "(macroexpand form)", "Recursively expands the given form until it is no longer a macro call", FUNCTION_ICON);
        addFunction(result, "macroexpand-1", "(macroexpand-1 form)", "Expands the given form once if it is a macro call", FUNCTION_ICON);
        
        // Regex functions  
        addFunction(result, "re-seq", "(re-seq re s)", "Returns a sequence of successive matches of pattern in string", FUNCTION_ICON);
        
        // Generators
        addFunction(result, "iterate", "(iterate f x)", "Returns an infinite sequence of x, (f x), (f (f x)), and so on", FUNCTION_ICON);
    }

    /**
     * Add predicate functions (functions ending with ?)
     */
    public static void addPredicateFunctions(@NotNull CompletionResultSet result, String prefix, @NotNull com.intellij.psi.PsiElement element) {
        // Type predicates with context-aware ranking (boosted in filter contexts)
        addFunctionWithContext(result, "nil?", "(nil? x)", "Returns true if x is nil", PREDICATE_ICON, element);
        addFunctionWithContext(result, "some?", "(some? x)", "Returns true if x is not nil", PREDICATE_ICON, element);
        addFunctionWithContext(result, "true?", "(true? x)", "Returns true if x is true", PREDICATE_ICON, element);
        addFunctionWithContext(result, "false?", "(false? x)", "Returns true if x is false", PREDICATE_ICON, element);
        addFunctionWithContext(result, "truthy?", "(truthy? x)", "Returns true if x is truthy", PREDICATE_ICON, element);
        addFunctionWithContext(result, "boolean?", "(boolean? x)", "Returns true if x is boolean", PREDICATE_ICON, element);
        addFunctionWithContext(result, "number?", "(number? x)", "Returns true if x is number", PREDICATE_ICON, element);
        addFunctionWithContext(result, "int?", "(int? x)", "Returns true if x is integer", PREDICATE_ICON, element);
        addFunctionWithContext(result, "float?", "(float? x)", "Returns true if x is float", PREDICATE_ICON, element);
        addFunctionWithContext(result, "string?", "(string? x)", "Returns true if x is string", PREDICATE_ICON, element);
        addFunctionWithContext(result, "keyword?", "(keyword? x)", "Returns true if x is keyword", PREDICATE_ICON, element);
        addFunctionWithContext(result, "symbol?", "(symbol? x)", "Returns true if x is symbol", PREDICATE_ICON, element);
        addFunctionWithContext(result, "function?", "(function? x)", "Returns true if x is function", PREDICATE_ICON, element);
        addFunctionWithContext(result, "var?", "(var? x)", "Returns true if x is variable", PREDICATE_ICON, element);

        // Collection predicates
        addFunction(result, "list?", "(list? x)", "Returns true if x is list", PREDICATE_ICON);
        addFunction(result, "vector?", "(vector? x)", "Returns true if x is vector", PREDICATE_ICON);
        addFunction(result, "map?", "(map? x)", "Returns true if x is map", PREDICATE_ICON);
        addFunction(result, "hash-map?", "(hash-map? x)", "Returns true if x is hash map", PREDICATE_ICON);
        addFunction(result, "set?", "(set? x)", "Returns true if x is set", PREDICATE_ICON);
        addFunction(result, "seq?", "(seq? x)", "Returns true if x is sequence", PREDICATE_ICON);
        addFunction(result, "coll?", "(coll? x)", "Returns true if x is collection", PREDICATE_ICON);
        addFunction(result, "associative?", "(associative? x)", "Returns true if x supports get/put", PREDICATE_ICON);
        addFunction(result, "indexed?", "(indexed? x)", "Returns true if x supports indexed access", PREDICATE_ICON);
        addFunction(result, "empty?", "(empty? x)", "Returns true if collection is empty", PREDICATE_ICON);
        addFunction(result, "not-empty?", "(not-empty? x)", "Returns true if collection is not empty", PREDICATE_ICON);

        // Numeric predicates
        addFunction(result, "zero?", "(zero? x)", "Returns true if x is zero", PREDICATE_ICON);
        addFunction(result, "pos?", "(pos? x)", "Returns true if x is positive", PREDICATE_ICON);
        addFunction(result, "neg?", "(neg? x)", "Returns true if x is negative", PREDICATE_ICON);
        addFunction(result, "even?", "(even? x)", "Returns true if x is even", PREDICATE_ICON);
        addFunction(result, "odd?", "(odd? x)", "Returns true if x is odd", PREDICATE_ICON);
        addFunction(result, "one?", "(one? x)", "Returns true if x equals 1", PREDICATE_ICON);
        addFunction(result, "nan?", "(nan? x)", "Returns true if x is NaN", PREDICATE_ICON);

        // PHP interop predicates
        addFunction(result, "php-array?", "(php-array? x)", "Returns true if x is PHP array", PREDICATE_ICON);
        addFunction(result, "php-object?", "(php-object? x)", "Returns true if x is PHP object", PREDICATE_ICON);
        addFunction(result, "php-resource?", "(php-resource? x)", "Returns true if x is PHP resource", PREDICATE_ICON);
        
        // Additional documented predicates from official API
        addFunction(result, "not-empty?", "(not-empty? x)", "Returns true if collection is not empty", PREDICATE_ICON);
        
        // Missing predicates from official Phel core
        addFunction(result, "struct?", "(struct? x)", "Returns true if x is a struct", PREDICATE_ICON);
        addFunctionWithContext(result, "not-every?", "(not-every? pred xs)", "Returns false if (pred x) is logical true for every x in collection xs", PREDICATE_ICON, element);
        addFunctionWithContext(result, "not-any?", "(not-any? pred xs)", "Returns true if (pred x) is logical false for every x in xs", PREDICATE_ICON, element);
    }

    /**
     * Add collection manipulation functions
     */
    public static void addCollectionFunctions(@NotNull CompletionResultSet result, String prefix, @NotNull com.intellij.psi.PsiElement element) {
        addFunction(result, "cons", "(cons x xs)", "Returns new collection with item prepended", FUNCTION_ICON);
        addFunction(result, "conj", "(conj coll & items)", "Returns collection with items added", FUNCTION_ICON);
        addFunction(result, "push", "(push coll item)", "Returns collection with item pushed", FUNCTION_ICON);
        addFunction(result, "find", "(find coll key)", "Returns key-value pair for key in coll", FUNCTION_ICON);
        addFunction(result, "find-index", "(find-index pred xs)", "Returns index of first item matching pred", FUNCTION_ICON);
        addFunction(result, "contains?", "(contains? coll key)", "Returns true if coll contains key", PREDICATE_ICON);
        addFunction(result, "contains-value?", "(contains-value? coll value)", "Returns true if coll contains value", PREDICATE_ICON);
        addFunction(result, "keys", "(keys coll)", "Returns keys of associative collection", FUNCTION_ICON);
        addFunction(result, "values", "(values coll)", "Returns values of associative collection", FUNCTION_ICON);
        addFunction(result, "kvs", "(kvs coll)", "Returns key-value pairs", FUNCTION_ICON);
        addFunction(result, "pairs", "(pairs coll)", "Returns pairs from collection", FUNCTION_ICON);
        addFunction(result, "merge", "(merge & maps)", "Merges maps", FUNCTION_ICON);
        addFunction(result, "merge-with", "(merge-with f & maps)", "Merges maps using f for conflicts", FUNCTION_ICON);
        addFunction(result, "deep-merge", "(deep-merge & args)", "Deep merges maps", FUNCTION_ICON);
        addFunction(result, "invert", "(invert map)", "Returns map with keys and values swapped", FUNCTION_ICON);
        addFunction(result, "group-by", "(group-by f xs)", "Groups collection by result of f", FUNCTION_ICON);
        addFunction(result, "frequencies", "(frequencies xs)", "Returns frequency map", FUNCTION_ICON);
        addFunction(result, "zipcoll", "(zipcoll keys values)", "Creates map from keys and values", FUNCTION_ICON);
        addFunction(result, "zipmap", "(zipmap keys vals)", "Returns a new map with the keys mapped to the corresponding values", FUNCTION_ICON);
        
        // Additional collection functions from official API
        addFunction(result, "some", "(some pred xs)", "Returns the first logical true value of (pred x) for any x in xs, else nil", FUNCTION_ICON);
        addFunction(result, "not-empty", "(not-empty coll)", "Returns coll if it contains elements, otherwise nil", FUNCTION_ICON);
        addFunction(result, "shuffle", "(shuffle xs)", "Returns a random permutation of xs", FUNCTION_ICON);
    }

    /**
     * Add arithmetic and math functions
     */
    public static void addArithmeticFunctions(@NotNull CompletionResultSet result, String prefix, @NotNull com.intellij.psi.PsiElement element) {
        addFunction(result, "+", "(+ & xs)", "Addition", FUNCTION_ICON);
        addFunction(result, "-", "(- & xs)", "Subtraction", FUNCTION_ICON);
        addFunction(result, "*", "(* & xs)", "Multiplication", FUNCTION_ICON);
        addFunction(result, "/", "(/ & xs)", "Division", FUNCTION_ICON);
        addFunction(result, "%", "(% dividend divisor)", "Modulo operation", FUNCTION_ICON);
        addFunction(result, "**", "(** a x)", "Exponentiation", FUNCTION_ICON);
        addFunction(result, "inc", "(inc x)", "Increments x by 1", FUNCTION_ICON);
        addFunction(result, "dec", "(dec x)", "Decrements x by 1", FUNCTION_ICON);
        addFunction(result, "mean", "(mean xs)", "Returns arithmetic mean", FUNCTION_ICON);

        // Bitwise operations
        addFunction(result, "bit-and", "(bit-and x y & args)", "Bitwise AND", FUNCTION_ICON);
        addFunction(result, "bit-or", "(bit-or x y & args)", "Bitwise OR", FUNCTION_ICON);
        addFunction(result, "bit-xor", "(bit-xor x y & args)", "Bitwise XOR", FUNCTION_ICON);
        addFunction(result, "bit-not", "(bit-not x)", "Bitwise NOT", FUNCTION_ICON);
        addFunction(result, "bit-shift-left", "(bit-shift-left x n)", "Bitwise left shift", FUNCTION_ICON);
        addFunction(result, "bit-shift-right", "(bit-shift-right x n)", "Bitwise right shift", FUNCTION_ICON);
        addFunction(result, "bit-set", "(bit-set x n)", "Sets bit at position n", FUNCTION_ICON);
        addFunction(result, "bit-clear", "(bit-clear x n)", "Clears bit at position n", FUNCTION_ICON);
        addFunction(result, "bit-flip", "(bit-flip x n)", "Flips bit at position n", FUNCTION_ICON);
        addFunction(result, "bit-test", "(bit-test x n)", "Tests bit at position n", PREDICATE_ICON);
    }

    private static void addFunction(@NotNull CompletionResultSet result, String name, String signature, String description, Icon icon) {
        // Use smart ranking with default API function priority
        PhelCompletionRanking.Priority priority = PhelCompletionRanking.Priority.API_FUNCTIONS;
        
        // Boost priority for commonly used functions
        if (isCommonBuiltin(name)) {
            priority = PhelCompletionRanking.Priority.COMMON_BUILTINS;
        } else if (isCollectionFunction(name)) {
            priority = PhelCompletionRanking.Priority.COLLECTION_FUNCTIONS;
        }
        
        PhelCompletionRanking.addRankedCompletion(result, name, priority, signature, description, icon);
    }
    
    /**
     * Enhanced version with contextual ranking
     */
    private static void addFunctionWithContext(@NotNull CompletionResultSet result, String name, String signature, String description, Icon icon, @NotNull com.intellij.psi.PsiElement context) {
        PhelCompletionRanking.Priority priority = PhelCompletionRanking.getContextualPriority(name, context, "API Function");
        PhelCompletionRanking.addRankedCompletion(result, name, priority, signature, description, icon);
    }
    
    /**
     * Check if function is commonly used builtin
     */
    private static boolean isCommonBuiltin(String name) {
        return java.util.Arrays.asList(
            "map", "filter", "reduce", "get", "put", "count", "first", "rest",
            "+", "-", "*", "/", "=", "<", ">", "<=", ">=", 
            "str", "print", "println", "nil?", "empty?"
        ).contains(name);
    }
    
    /**
     * Check if function operates on collections
     */
    private static boolean isCollectionFunction(String name) {
        return java.util.Arrays.asList(
            "conj", "cons", "concat", "reverse", "sort", "sort-by", "group-by",
            "partition", "take", "drop", "take-while", "drop-while", 
            "assoc", "dissoc", "keys", "vals", "merge"
        ).contains(name);
    }
}
