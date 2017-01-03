(ns monads.core
  (:require [clojure.algo.monads :refer
             [domonad with-monad state-m update-state maybe-m state-t]]))

(defrecord tree [content left right])

(def example-tree
  (tree. 'root
         (tree. 'left
                (tree. 'left-left nil nil)
                nil)
         (tree. 'right
                (tree. 'right-left nil nil)
                (tree. 'right-right
                       nil
                       (tree. 'right-right-right nil nil)))))

; simple pure example: tagging with a static tag

(defn tree-tag [tree tag]
  (if (nil? tree) tree
    (let [left (tree-tag (:left tree) tag)
          right (tree-tag (:right tree) tag)]
      (tree. [tag (:content tree)] left right))))

; simple impure example: tagging with a running (incremented) tag

(defn tree-tag-running-impure [tree tag]
  (let [curval (atom 0)]
    (defn nextval [] (swap! curval inc))
    (defn tree-tag-rec [tree]
      (if (nil? tree) tree
        (let [left (tree-tag-rec (:left tree))
              my-tag (symbol (str tag "-" (nextval)))
              right (tree-tag-rec (:right tree))]
          (tree. [my-tag (:content tree)] left right))))
    (tree-tag-rec tree)))

; corresponding pure function, with threading

(defn tree-tag-running-threaded [tree tag]
  (defn tree-tag-rec [tree value]
    (if (nil? tree) [tree value]
      (let [[left val2] (tree-tag-rec (:left tree) value)
            my-tag (symbol (str tag "-" val2))
            val3 (inc val2)
            [right val4] (tree-tag-rec (:right tree) val3)]
      [(tree. [my-tag (:content tree)] left right) val4])))
  (first (tree-tag-rec tree 1)))

; corresponding pure function, with state monad

(defn tree-tag-running-bind [tree tag]
  (with-monad state-m
    (defn tree-tag-rec [tree]
      (if (nil? tree) (m-result tree)
        (m-bind (tree-tag-rec (:left tree)) (fn [left]
          (m-bind (update-state inc) (fn [my-val]
            (m-bind (tree-tag-rec (:right tree)) (fn [right]
              (m-result (tree. [(symbol (str tag "-" my-val)) (:content tree)]
                               left right))))))))))
    (first ((tree-tag-rec tree) 1))))

; corresponding pure function, with state monad + syntax

(defn tree-tag-running [tree tag]
  (with-monad state-m
    (defn tree-tag-rec [tree]
      (if (nil? tree) (m-result tree)
        (domonad [left (tree-tag-rec (:left tree))
                  my-val (update-state inc)
                  :let [my-tag (symbol (str tag "-" my-val))]
                  right (tree-tag-rec (:right tree))]
          (tree. [my-tag (:content tree)] left right))))
    (first ((tree-tag-rec tree) 1))))

; running tags with early quit, threaded version

(defn tree-tag-running-maybe-threaded [tree tag stopper]
  (defn tree-tag-rec [tree value]
    (cond (nil? tree) [tree value]
          (= stopper (:content tree)) nil
          :else
          (let [result1 (tree-tag-rec (:left tree) value)]
            (and result1
              (let [[left val2] result1
                    my-tag (symbol (str tag "-" val2))
                    val3 (inc val2)
                    result2 (tree-tag-rec (:right tree) val3)]
                (and result2
                  (let [[right val4] result2]
                    [(tree. [my-tag (:content tree)] left right) val4])))))))
  (let [result (tree-tag-rec tree 1)]
    (and result (first result))))

; running tags with early quit, monad version

(defn tree-tag-running-maybe [tree tag stopper]
  (with-monad (state-t maybe-m)
    (defn tree-tag-rec [tree]
      (cond (nil? tree) (m-result tree)
            (= stopper (:content tree)) m-zero
            :else
            (domonad [left (tree-tag-rec (:left tree))
                      my-val (update-state inc)
                      :let [my-tag (symbol (str tag "-" my-val))]
                      right (tree-tag-rec (:right tree))]
              (tree. [my-tag (:content tree)] left right)))))
  (let [result ((tree-tag-rec tree) 1)]
    (and result (first result))))

; defining our own monad: for instance, the step-counting monad

(defmonad count-m
  [m-result (fn [value] (fn [count k] (k (inc count) value)))
   m-bind (fn [mvalue f]
            (fn [count k]
              (mvalue (inc count)
                      (fn [count2 value] ((f value) (inc count) k)))))])

(defn run-count-m [mvalue] (mvalue 0 vector))

(defn step-over? [bound]
  (fn [count k]
    (k (inc count) (> count bound))))

