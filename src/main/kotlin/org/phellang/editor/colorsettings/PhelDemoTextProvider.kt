package org.phellang.editor.colorsettings

class PhelDemoTextProvider {

    fun getDemoText(): String {
        return buildString {
            appendNamespaceSection()
            appendLiteralsSection()
            appendFunctionDefinitionSection()
            appendDataStructuresSection()
            appendMacrosSection()
            appendExceptionHandlingSection()
            appendCommentsSection()
        }
    }

    private fun StringBuilder.appendNamespaceSection() {
        append("(ns src.demo")
        append("\n  (:require phel.core :refer [print-str str map filter])")
        append("\n  (:require phel.http :as http)")
        append("\n  (:require phel.string :as s)")
        append("\n  (:use \\DateTime \\Exception))")
        append("\n")
    }

    private fun StringBuilder.appendLiteralsSection() {
        append("\n; Boolean and nil literals")
        append("\n(def config {:debug true :prod false :cache nil :timeout NAN})")
        append("\n")
        append("\n; Numbers in different bases")
        append("\n(def numbers [42 0xFF 0b1010 0o777 3.14159 -1.5e10 16rFF 2r1010])")
        append("\n")
        append("\n; Ratios, big-number suffixes, and symbolic numerics (Phel 0.36)")
        append("\n(def rationals [1/2 -3/4 22/7])")
        append("\n(def big-nums [42N 3.14M 1.5e3M])")
        append("\n(def edge-nums [##Inf ##-Inf ##NaN])")
        append("\n")
        append("\n; Character literals and strings")
        append("\n(def greeting \"Phel-lo world!\")")
        append("\n(def pattern #\"[a-z]+\")")
        append("\n")
    }

    private fun StringBuilder.appendFunctionDefinitionSection() {
        append("\n; Advanced destructuring")
        append("\n(defn process-data")
        append("\n  \"Function with comprehensive destructuring\"")
        append("\n  {:export true}")
        append("\n  [{:keys keys :method method :headers headers} users]")
        append("\n  (let [[first-user & rest-users] users")
        append("\n        {:name name :age age} first-user]")
        append("\n    (case method")
        append("\n      \"GET\" (handle-get-request name age)")
        append("\n      \"POST\" (handle-post-request keys headers)")
        append("\n      (throw (php/new Exception \"Unsupported method\")))))")
        append("\n")
        append("\n; Type-tag, memoize, and async metadata (Phel 0.36)")
        append("\n(defn ^:memoize ^int square [^int n] (php/* n n))")
        append("\n(defn ^:async fetch-remote [^\"?string\" url] (println url))")
        append("\n(def square-ref #'square)")
        append("\n")
        append("\n; Function with metadata and PHP interop")
        append("\n(defn ^{:doc \"Fetches user data from API\"} fetch-user")
        append("\n  [user-id]")
        append("\n  (let [response (http/get (str \"/api/users/\" user-id))")
        append("\n        data (php/-> response (json) (get \"data\"))]")
        append("\n    (when (php/isset data)")
        append("\n      {:id (php/-> data (get \"id\"))")
        append("\n       :name (php/-> data (get \"name\"))")
        append("\n       :email (php/-> data (get \"email\"))})))")
        append("\n")
    }

    private fun StringBuilder.appendDataStructuresSection() {
        append("\n; Complex data structures")
        append("\n(def user-database")
        append("\n  {:users [{:id 1 :name \"Alice\" :roles #{:admin :user}}")
        append("\n           {:id 2 :name \"Bob\" :roles #{:user}}]")
        append("\n   :settings {:theme \"dark\" :notifications true}")
        append("\n   :metadata ^{:version \"1.0\" :created-at \"2024-01-01\"}")
        append("\n             {:schema-version 2}})")
        append("\n")
        append("\n; Anonymous function shorthand")
        append("\n(def squared-numbers (map #(* % %) [1 2 3 4 5]))")
        append("\n(def add-ten (map #(+ %1 10) [1 2 3]))")
        append("\n")
        append("\n; Deref and atoms")
        append("\n(def counter (atom 0))")
        append("\n(println @counter)")
        append("\n")
        append("\n; Bitwise operations")
        append("\n(def flags (bit-or 0b0001 0b0010 0b0100))")
        append("\n(def masked (bit-and flags 0b0110))")
        append("\n")
    }

    private fun StringBuilder.appendMacrosSection() {
        append("\n; Macros and syntax-quote")
        append("\n(def processed-text")
        append("\n  (-> \"  Hello World  \"")
        append("\n      s/trim")
        append("\n      s/lower-case")
        append("\n      (s/replace \"world\" \"Phel\")")
        append("\n      (str \"!\")))")
        append("\n")
        append("\n; Syntax quote with unquote")
        append("\n(defmacro my-when [test & body]")
        append("\n  `(if ~test (do ~@body) nil))")
        append("\n")
    }

    private fun StringBuilder.appendExceptionHandlingSection() {
        append("\n; Exception handling")
        append("\n(defn safe-divide [a b]")
        append("\n  (try")
        append("\n    (/ a b)")
        append("\n    (catch \\DivisionByZeroError e")
        append("\n      (php/-> e (getMessage)))")
        append("\n    (finally")
        append("\n      (println \"Division attempt completed\"))))")
        append("\n")
    }

    private fun StringBuilder.appendCommentsSection() {
        append("\n; REPL and documentation")
        append("\n(comment")
        append("\n  \"This is a comment block for REPL experimentation\"")
        append("\n  (doc map)")
        append("\n  (require phel.test :as test)")
        append("\n  (test/run-tests))")
        append("\n")
        append("\n; Reader conditionals")
        append("\n#?(:phel (println \"Running on Phel\")")
        append("\n   :default (println \"Unknown platform\"))")
        append("\n")
        append("\n; Form comments")
        append("\n[#_:one :two :three]")
    }
}
