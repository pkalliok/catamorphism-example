(ns monads.core-test
  (:require [clojure.test :refer :all]
            [monads.core :refer :all]))

(deftest test-tree-tag
  (let [tagged (tree-tag example-tree :sometag)]
    (is (= (->> tagged :right :left :content) [:sometag 'right-left]))))

(defn test-tree-tag-running [f]
  (let [tagged (f example-tree 'foo)]
    (is (= (:content tagged) ['foo-3 'root]))))

(deftest test-impure (test-tree-tag-running tree-tag-running-impure))
(deftest test-threaded (test-tree-tag-running tree-tag-running-threaded))
(deftest test-bind (test-tree-tag-running tree-tag-running-bind))
(deftest test-domonad (test-tree-tag-running tree-tag-running))

(defn test-tree-tag-stoppable [f]
  (is (= (f example-tree 'foo 'right-right) nil))
  (is (= (:content (f example-tree 'foo 'right-rig)) ['foo-3 'root])))

(deftest test-stop-threaded
  (test-tree-tag-stoppable tree-tag-running-maybe-threaded))
(deftest test-stop-monadic
  (test-tree-tag-stoppable tree-tag-running-maybe))

(deftest tree-size-bounded-result
  (is (= (tree-size-bounded example-tree 100) 8)))

(deftest tree-size-bounded-interrupted
  (is (= (tree-size-bounded example-tree 10) 'unknown)))

