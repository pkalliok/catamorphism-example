(ns monads.core
  (:require [clojure.algo.monads :refer
             [domonad with-monad defmonad state-m]]
  (:gen-class))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
