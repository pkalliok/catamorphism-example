(ns monads.core-test
  (:require [clojure.test :refer :all]
            [monads.core :refer :all]))

(deftest test-tree-tag
  (let [tagged (tree-tag example-tree :sometag)]
    (is (= (->> tagged :right :left :content) [:sometag 'right-left]))))

